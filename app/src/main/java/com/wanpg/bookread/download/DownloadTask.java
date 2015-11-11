package com.wanpg.bookread.download;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.wanpg.bookread.BaseApplication;
import com.wanpg.bookread.R;
import com.wanpg.bookread.db.DaoManager;
import com.wanpg.bookread.ui.activity.BookDownloadActivity;
import com.wanpg.bookread.utils.MathUtil;

public class DownloadTask {
	/** 下载线程同时运行的数量 **/
	public final int CORE_POOL_SIZE = 3;
	/** 下载线程池  **/
	public ExecutorService DOWNLOAD_THREAD_POOL = Executors.newFixedThreadPool(CORE_POOL_SIZE);
	/** 存储下载任务的map，线程安全的 **/
	public ConcurrentHashMap<Integer, DownItem> threadMap = new ConcurrentHashMap<Integer, DownItem>();
	public static DownloadTask self = null;

	private Bitmap mDownloadingBM = null;
	private Bitmap mFinishSuccessBM = null;
	private Bitmap mFinishErrorBM = null;

	private NotificationManager notificationManager;
	private NotificationCompat.Builder builder;
	private Notification notification;
	private BaseApplication application;


	public static final int OBSERVER_NOTICE = 0;
	public static final int OBSERVER_UPDATE = 1;

	public static final int NOTIFYCATION_NOTICE = 2;
	public static final int NOTIFYCATION_UPDATE = 3;
	public static final int NOTIFYCATION_CLEAR = 4;

	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case OBSERVER_NOTICE:
				if(observer!=null)
					observer.noticeNotify((DownItem) msg.obj);
				break;
			case OBSERVER_UPDATE:
				if(observer!=null)
					observer.updateNotify((DownItem) msg.obj);
				break;
			case NOTIFYCATION_NOTICE:
				showNotification((DownItem) msg.obj);
				break;
			case NOTIFYCATION_UPDATE:
				updateNotification((DownItem) msg.obj, msg.arg1);
				break;
			case NOTIFYCATION_CLEAR:
				clearNotification(msg.arg1);
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 此处接受系统对网络连接的广播
	 */
	private BroadcastReceiver netConnectionBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//在这里接受系统对网络连接与否的广播
			ConnectivityManager connectMgr = (ConnectivityManager) BaseApplication.getInstance().getSystemService(Activity.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectMgr.getActiveNetworkInfo();
			if (activeNetworkInfo == null || (!activeNetworkInfo.isConnected())) {
				//没有活跃的网络连接，或者网络连接不可用

			} else if (activeNetworkInfo.isConnected()) {
				//网络连接可用

			}
		}
	};

	public DownloadTask() {
		// TODO Auto-generated constructor stub
		init();
		initData();
	}

	private void initData() {
		List<DownItem> list = DaoManager.getInstance().getDownloadDao().queryDownloadItems();
		if(threadMap==null)
			threadMap = new ConcurrentHashMap<Integer, DownItem>();
		else
			threadMap.clear();
		for (DownItem item : list) {
			threadMap.put(item.downloadId, item);
		}
	}


	public static DownloadTask getInstance() {
		if (self == null) {
			self = new DownloadTask();
		}
		return self;
	}

	private void init() {
		// TODO Auto-generated method stub
		application = BaseApplication.getInstance();
		registerNetConnectionBroadcast();
	}

	/**
	 * 添加任务
	 * @param item
	 */
	public void addTask(DownItem item) {
		// TODO Auto-generated method stub
		threadMap.put(item.downloadId, item);
	}

	public void continueTask(DownItem item) {
		if(!threadMap.containsKey(item.downloadId)){
			addTask(item);
		}
		start(item.downloadId);
	}

	public void start(int downloadId) {
		// TODO Auto-generated method stub
		DOWNLOAD_THREAD_POOL.submit(new DownloadThread(threadMap.get(downloadId), mHandler));
	}

	public void stop(int downloadId) {
		// TODO Auto-generated method stub
		threadMap.get(downloadId).status = DownItem.STATUS_PAUSE;
	}

	public int getThreadCount() {
		return threadMap.size();
	}


	/**
	 * 获得已经完成的任务
	 * @return
	 */
	public List<DownItem> getFinishedTask() {
		List<DownItem> list = new ArrayList<DownItem>();
		Iterator<Integer> iterator = threadMap.keySet().iterator();
		while (iterator.hasNext()) {
			int downLoadId = iterator.next();
			DownItem item = threadMap.get(downLoadId);
			if (item.status == DownItem.STATUS_FINISHED) {
				list.add(item);
			}
		}
		return list;
	}

	/**
	 * 获得没有完成的任务
	 * @return
	 */
	public List<DownItem> getDownloadTask() {
		List<DownItem> list = new ArrayList<DownItem>();
		Iterator<Integer> iterator = threadMap.keySet().iterator();
		while (iterator.hasNext()) {
			int downLoadId = iterator.next();
			DownItem item = threadMap.get(downLoadId);
			if (item.status != DownItem.STATUS_FINISHED) {
				list.add(item);
			}
		}
		return list;
	}


	public void registerNetConnectionBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(ConnectivityManager.ACTION_BACKGROUND_DATA_SETTING_CHANGED);
		BaseApplication.getInstance().registerReceiver(netConnectionBroadcastReceiver, filter);
		BaseApplication.getInstance().isNetBroadcastBind = true;
	}

	public void unRegisterNetConnectionBroadcast() {
		if (netConnectionBroadcastReceiver != null) {
			BaseApplication.getInstance().isNetBroadcastBind = false;
			BaseApplication.getInstance().unregisterReceiver(netConnectionBroadcastReceiver);
		}
	}

	private DownloadObserver observer ;
	public void setObserver(DownloadObserver observer){
		this.observer = observer;
	}


	 public void clearNotification(int id) {
		 // TODO Auto-generated method stub
		 notificationManager.cancel(id);
	 }

	 private void showNotification(DownItem item) {
		 //notification = new Notification(R.drawable.ic_launcher, result, System.currentTimeMillis()); 
		 if(notificationManager==null)
			 notificationManager = (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);

		 // 点击该通知后要跳转的Activity
		 Intent notificationIntent = new Intent(application, BookDownloadActivity.class);
		 PendingIntent contentItent = PendingIntent.getActivity(application, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		 if(builder==null)
			 builder = new NotificationCompat.Builder(application);
		 
		 String ticker = "";
		 String title = item.name;
		 String content = "";
		 int iconId = 0;
		 Bitmap iconBm = null;
		 boolean isAutoCancel = true;
		 boolean isOnGoing = false;
		 switch (item.status) {
		 case DownItem.STATUS_WAIT:
		 case DownItem.STATUS_PREPARE:
			 ticker = "正在下载";
			 content = "正在准备";
			 iconId = R.drawable.downloading_icon;
			 if(mDownloadingBM==null){
				 mDownloadingBM = BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), R.drawable.downloading_big_icon);
			 }
			 iconBm = mDownloadingBM;
			 isAutoCancel = false;
			 isOnGoing = true;
			 break;
		 case DownItem.STATUS_DOWNLOADING:
			 ticker = "正在下载";
			 content = "正在下载";
			 iconId = R.drawable.downloading_icon;
			 if(mDownloadingBM==null){
				 mDownloadingBM = BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), R.drawable.downloading_big_icon);
			 }
			 iconBm = mDownloadingBM;
			 isAutoCancel = false;
			 isOnGoing = true;
			 break;
		 case DownItem.STATUS_ERROR:
			 ticker = "下载出错";
			 content = "下载出错";
			 iconId = R.drawable.download_error_icon;
			 if(mFinishErrorBM==null){
				 mFinishErrorBM = BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), R.drawable.download_error_big_icon);
			 }
			 iconBm = mFinishErrorBM;
			 isAutoCancel = true;
			 isOnGoing = false;
			 break;
		 case DownItem.STATUS_FINISHED:
			 ticker = "下载完成";
			 content = "下载完成";
			 iconId = R.drawable.download_finished_icon;
			 if(mFinishSuccessBM==null){
				 mFinishSuccessBM = BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), R.drawable.download_finished_big_icon);
			 }
			 iconBm = mFinishSuccessBM;
			 isAutoCancel = true;
			 isOnGoing = false;
			 break;
		 case DownItem.STATUS_PAUSE:
		 case DownItem.STATUS_DELETE:
			 clearNotification(item.downloadId);
			 return ;
		 default:
			 return ;
		 }
		 
		 builder.setSmallIcon(iconId);
		 builder.setLargeIcon(iconBm);
		 builder.setTicker(ticker);//设置开始的提醒
		 builder.setContentTitle(title);//设置内容标题
		 builder.setContentText(content);//设置内容
		 builder.setAutoCancel(isAutoCancel);//点击后是否消失
		 builder.setOnlyAlertOnce(true);//声音和震动只提醒一次
		 builder.setDefaults(Notification.DEFAULT_ALL);//设置声音灯光的提醒
		 builder.setContentIntent(contentItent);//设置intent跳转默认
		 builder.setOngoing(isOnGoing);//是否放入正在运行
		 
		 notification = builder.build();

		 notificationManager.notify(item.downloadId, notification);
	 }

	 private void updateNotification(DownItem item , long lastLength) {
		 // TODO Auto-generated method stub
		 if(item==null) return;
		 if(item.fileLength>0){
			 int progress = (int) ((double) item.finishedLength / (double) item.fileLength * 100);

			 if (progress >= 0 && progress % 5 == 0) {
				 builder.setContentText("已完成:" + progress + "%");
				 notification = builder.build();
				 notificationManager.notify(item.downloadId, notification);
			 }
		 }else{
			 if(item.finishedLength - lastLength > 2048){
				 lastLength = item.finishedLength;
				 builder.setContentText("已下载:" + MathUtil.countFileSize(lastLength));
				 notification = builder.build();
				 notificationManager.notify(item.downloadId, notification);
			 }
		 }
	 }



}