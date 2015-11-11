package com.wanpg.bookread;

import java.io.File;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.umeng.analytics.MobclickAgent;
import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.ui.activity.WebStoreActivity;
import com.wanpg.bookread.ui.read.epub.BookReadEpubActivity;
import com.wanpg.bookread.ui.read.txt.BookReadTxtActivity;
import com.wanpg.bookread.utils.FileUtil;
import com.wanpg.bookread.utils.TextUtil;
import com.wanpg.bookread.widget.Notice;
import com.wanpg.bookread.widget.NoticeDialog;

/**
 * Activity基础类，其它Activity要继承此类，一些固定的常用参数在此处定义
 *
 * @author 王金鹏
 */
public class BaseActivity extends FragmentActivity {

	public static final int INTERNET_CONN_FAIL = 0;

	public FragmentManager mFragmentManager;

	public NoticeDialog mNotice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		//下面注掉的这句是手动发送错误
		//MobclickAgent.reportError(Context context,String error)
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//设置屏幕没有title，标题栏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置屏幕锁定纵向
		mFragmentManager = getSupportFragmentManager();
		mNotice = new NoticeDialog(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void setBackEvent(View v){
		if(v!=null){
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					onBackPressed();
				}
			});
		}
	}

	public void showToast(final String toast) {
		// TODO Auto-generated method stub
		if(TextUtil.isEmpty(toast))
			return ;
		if(Thread.currentThread().getName().equals("main"))
			Notice.showToast(toast);
		else
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Notice.showToast(toast);
				}
			});
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_from_right);
	}


	/**
	 * 打开书架上指定索引的数据
	 *
	 * @param pos
	 */
	public boolean openBook(ShelfBook shelfBookmode) {
		if(shelfBookmode==null)
			return false;
		Intent intent = new Intent();
		if (shelfBookmode.readMode.equals(ShelfBook.POS_LOCAL_SDCARD)) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("shelfBookmode", shelfBookmode);
			String sType = FileUtil.getFileType(new File(shelfBookmode.bookPath).getName());
			if (sType.equalsIgnoreCase("txt")) {
				intent.setClass(this, BookReadTxtActivity.class);
			} else if (sType.equalsIgnoreCase("epub")) {
				intent.setClass(this, BookReadEpubActivity.class);
			} else {
				Notice.showToast("抱歉，目前不支持此种格式的书籍！");
				return false;
			}
			intent.putExtras(bundle);
		} else if (shelfBookmode.readMode.equals(ShelfBook.POS_LOCAL_SHUPENG)) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("shelfBookmode", shelfBookmode);
			String sType = FileUtil.getFileType(new File(shelfBookmode.bookPath).getName());
			if (sType.equalsIgnoreCase("txt")) {
				intent.setClass(this, BookReadTxtActivity.class);
			} else if (sType.equalsIgnoreCase("epub")) {
				intent.setClass(this, BookReadEpubActivity.class);
			} else if (sType.equalsIgnoreCase("rar")) {
				String innerFileType = shelfBookmode.innerFileType;
				if (FileUtil.checkRarHasTarget(shelfBookmode.bookPath, innerFileType)) {
					if (innerFileType.equalsIgnoreCase("txt")) {
						intent.setClass(this, BookReadTxtActivity.class);
					} else if (innerFileType.equalsIgnoreCase("epub")) {
						intent.setClass(this, BookReadEpubActivity.class);
					} else {
						Notice.showToast("抱歉，目前不支持此种格式的书籍！");
						return false;
					}
				} else {
					Notice.showToast("抱歉，目前不支持此种格式的书籍！");
					return false;
				}
			} else if (sType.equalsIgnoreCase("zip")) {
				String innerFileType = shelfBookmode.innerFileType;
				if (FileUtil.checkZipHasTarget(shelfBookmode.bookPath, innerFileType)) {
					if (innerFileType.equalsIgnoreCase("txt")) {
						intent.setClass(this, BookReadTxtActivity.class);
					} else if (innerFileType.equalsIgnoreCase("epub")) {
						intent.setClass(this, BookReadEpubActivity.class);
					} else {
						Notice.showToast("抱歉，目前不支持此种格式的书籍！");
						return false;
					}
				} else {
					Notice.showToast("抱歉，目前不支持此种格式的书籍！");
					return false;
				}
			} else {
				Notice.showToast("抱歉，目前不支持此种格式的书籍！");
				return false;
			}
			intent.putExtras(bundle);
		} else if (shelfBookmode.readMode.equals(ShelfBook.POS_ONLINE)) {
			intent.setClass(this, WebStoreActivity.class);
			intent.putExtra("HREF", shelfBookmode.chapterUrl);
		}
		this.startActivity(intent);
		return true;
	}
}
