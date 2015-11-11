package com.wanpg.bookread;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.wanpg.bookread.api.ShuPengApi;
import com.wanpg.bookread.common.Config;
import com.wanpg.bookread.common.CrashHandler;
import com.wanpg.bookread.common.PhoneInfo;
import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.db.DaoManager;
import com.wanpg.bookread.download.DownloadService;
import com.wanpg.bookread.download.DownloadService.MyBinder;
import com.wanpg.bookread.utils.DeviceUtil;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.utils.FileUtil;
import com.wanpg.bookread.utils.LogUtil;
import com.wanpg.bookread.widget.Notice;

public class BaseApplication extends Application {

	/**
	 * 下载用的服务
	 */
	public DownloadService downloadService;
	/**
	 * 绑定服务的connection
	 */
	private ServiceConnection mServiceConnection;
	private boolean isBindService = false;
	public boolean isDownloadActivityOpen = false;
	public boolean isSoftLiving = false;
	public boolean isShelfDataLoadOver = false;
	private static BaseApplication THIS;

	private boolean isFirstOpen = true;

	public boolean isNetBroadcastBind = false;

	public boolean isSdCardOk = false;

	public static BaseApplication getInstance() {
		// TODO Auto-generated method stub
		return THIS;
	}

	public BroadcastReceiver SDCardStateBroadReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			isSdCardOk = DeviceUtil.isSDCardAvailable();
			if (!isSdCardOk) {
				Notice.showToast("储存卡已拔出，本地图书阅读和添加功能暂时不能使用，请选择在线阅读！");
			}
		}
	};


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		THIS = this;
		LogUtil.D("wanpg", "application_oncreate");
		CrashHandler.getInstance().init(THIS);
		bindDownloadService();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
		registerReceiver(SDCardStateBroadReceiver, intentFilter);
		initShelfData();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		unregisterReceiver(SDCardStateBroadReceiver);
		unBindDownService();
		super.onTerminate();
	}

	/**
	 * 初始化书架信息
	 */
	private void initShelfData() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				initPhoneInfo();
				SharedPreferences spc = getSharedPreferences(Config.CONFIG_SOFT, MODE_PRIVATE);
				isFirstOpen = spc.getBoolean("isFirstOpen", true);
				if (isFirstOpen && DeviceUtil.isSDCardAvailable()) {
					copyDefultBook();
				}
				ShuPengApi.initShuPengAppkey(THIS);
				isShelfDataLoadOver = true;
			}
		}.start();
	}


	/**
	 * 复制自带的书
	 */
	private void copyDefultBook() {
		InputStream insBook = null;
		InputStream insCover = null;
		try {

			String sBook[] = this.getBaseContext().getAssets().list("book");
			String sCover[] = this.getBaseContext().getAssets().list("cover");

			List<ShelfBook> list = new ArrayList<ShelfBook>();
			for (int i = 0; i < sBook.length; i++) {
				insBook = this.getBaseContext().getAssets().open("book/" + sBook[i]);
				insCover = this.getBaseContext().getAssets().open("cover/" + sCover[i]);
				String sName = "";
				if (sBook[i].equals("ehsz.txt")) {
					sName = "二号首长";
				} else if (sBook[i].equals("gdzsj.txt")) {
					sName = "官道之色戒";
				} else if (sBook[i].equals("hzqr.txt")) {
					sName = "合租情人";
				} else if (sBook[i].equals("rmssjcld.txt")) {
					sName = "人脉式设计出来的";
				}

				String bookPath = Config.BOOK_SD_BOOK + "/" + sName + ".txt";
				String coverPath = Config.BOOK_SD_COVER + "/" + sName + ".cover";

				File f1 = new File(bookPath);
				File f2 = new File(coverPath);
				FileUtil.copyStreamFile(insBook, f1);
				FileUtil.copyStreamFile(insCover, f2);
				insBook = null;
				insCover = null;

				ShelfBook shelfBookMode = new ShelfBook();
				//此部分为公共
				shelfBookMode.readMode = ShelfBook.POS_LOCAL_SDCARD;
				shelfBookMode.bookName = sName;
				shelfBookMode.posInShelf = -1;
				shelfBookMode.author = "";

				//此部分为本地书籍信息，
				shelfBookMode.bookPath = bookPath;
				shelfBookMode.coverPath = coverPath;
				shelfBookMode.innerFileType = "";

				//此部分为网络书籍信息
				shelfBookMode.thumb = "";
				shelfBookMode.bookId = 0;
				shelfBookMode.url = "";
				shelfBookMode.chapterId = "";
				shelfBookMode.chapterUrl = "";
				list.add(shelfBookMode);
			}            
			DaoManager.getInstance().getShelfDao().saveShelfBooks(list);

			//在此处对第一次打开软件进行更改
			getSharedPreferences(Config.CONFIG_SOFT, MODE_PRIVATE).edit().putBoolean("isFirstOpen", false).commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (insBook != null) {
					insBook.close();
				}
				if (insCover != null) {
					insCover.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 绑定service
	 */
	public void bindDownloadService() {
		// TODO Auto-generated method stub
		mServiceConnection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				downloadService = null;
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				MyBinder mBinder = (MyBinder) service;
				downloadService = mBinder.getService();
			}
		};
		startService(new Intent(this, DownloadService.class));
		bindService(new Intent(this, DownloadService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
		isBindService = true;
	}

	/**
	 * 解除下载服务的绑定
	 */
	public void unBindDownService() {
		if (isBindService) {
			unbindService(mServiceConnection);
			isBindService = false;
		}
	}


	public void initPhoneInfo() {
		PhoneInfo.disWidthPx = DisplayUtil.getDisWidth();
		PhoneInfo.disHeightPx = DisplayUtil.getDisHeight();
	}




}
