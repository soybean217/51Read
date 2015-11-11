package com.wanpg.bookread.download;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.wanpg.bookread.common.Config;
import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.db.DaoManager;
import com.wanpg.bookread.db.DownloadDao;

/**
 * 下载服务
 *
 * @author Jinpeng
 */
public class DownloadService extends Service {

    public static final int STOP_TASK_NOTIFY = 1;
    public static final int DELETE_TASK_NOTIFY = 2;
    public static final int FINISHED_SUCCESS_NOTIFY = 3;
    public static final int FINISHED_FAIL_NOTIFY = 4;

    public static boolean isServiceOn = false;

    public MyBinder mBinder;

    private Context context;

    private DownloadDao downloadDao;
    private DownloadTask downloadTask;
    @SuppressLint("HandlerLeak")
    public Handler downloadHandler = new Handler() {
        public void handleMessage(Message msg) {
            int downloadId = 0;
            if (msg.obj != null) {
                downloadId = Integer.parseInt(msg.obj.toString());
            }
            switch (msg.what) {
                case STOP_TASK_NOTIFY:
                    updateOneTask(downloadTask.threadMap.get(downloadId));
                    break;
                case DELETE_TASK_NOTIFY:
                    deleteOneDownloadTaskAfterThread(downloadId);
                    break;
                case FINISHED_SUCCESS_NOTIFY:
                    finishedSuccess(downloadId);
                    break;
                case FINISHED_FAIL_NOTIFY:
                    updateOneTask(downloadTask.threadMap.get(downloadId));
                    break;
                default:
                    break;
            }
        }
    };

    public class MyBinder extends Binder {
        /**
         * 获取 Service 实例
         * @return
         */
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context = DownloadService.this;
        mBinder = new MyBinder();
        isServiceOn = true;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        context = DownloadService.this;
        initData();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        isServiceOn = false;
        destoryServiceCheck();
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    private void initData() {
        // TODO Auto-generated method stub
    	downloadDao = DaoManager.getInstance().getDownloadDao();
    	downloadTask = DownloadTask.getInstance();
    }


    /**
     * 添加一个任务
     * @param item
     */
    public void addTask(DownItem item) {
        item.status = DownItem.STATUS_WAIT;
        if (downloadTask.threadMap.containsKey(item.downloadId)) {
            Toast.makeText(context, "已经存在相同的任务！", Toast.LENGTH_SHORT).show();
        } else {
            downloadDao.addOneDownloadItem(item);
            downloadTask.addTask(item);
            downloadTask.start(item.downloadId);
        }
    }
    
    /**
     * 停止一个任务
     * @param downloadItem
     */
    public void stopOneTask(DownItem item) {
    	downloadTask.stop(item.downloadId);
    }

    public void continueOneTask(DownItem item) {
    	downloadTask.continueTask(item);
    }
    
    

    public void deleteOneDownloadTaskFromThread(DownItem item) {
        downloadTask.clearNotification(item.downloadId);
    	int downloadStatus = downloadTask.threadMap.get(item.downloadId).status;
        if (downloadStatus == DownItem.STATUS_WAIT || downloadStatus == DownItem.STATUS_DOWNLOADING) {
            downloadTask.threadMap.get(item.downloadId).status = DownItem.STATUS_DELETE;
        } else if (downloadStatus == DownItem.STATUS_ERROR || downloadStatus == DownItem.STATUS_PAUSE) {
            deleteOneDownloadTaskAfterThread(item.downloadId);
        }
    }

    public void deleteOneDownloadTaskAfterThread(int downloadId) {
    	downloadDao.delDownloadItem(downloadTask.threadMap.get(downloadId));
        downloadTask.threadMap.remove(downloadId);
    }

    public void deleteOneFinishedTask(DownItem item) {
        downloadDao.delDownloadItem(item);
        downloadTask.threadMap.remove(item.downloadId);
    }

    private void updateOneTask(DownItem item) {
        // TODO Auto-generated method stub
        downloadDao.updateOneDownloadItem(item);
    }

    private void finishedSuccess(int downloadId) {
        // TODO Auto-generated method stub
        //取出下载内容
        DownItem item = downloadTask.threadMap.get(downloadId);
        //更新数据库
        updateOneTask(item);
        SharedPreferences spc = getSharedPreferences(Config.CONFIG_SOFT, MODE_PRIVATE);
        if(spc.getBoolean("isAutoPutDownloadToShelf", true)){
	        //加入本地书架
	        ShelfBook shelfBookMode = new ShelfBook();
	        //此部分为公共
	        shelfBookMode.readMode = ShelfBook.POS_LOCAL_SHUPENG;
	        shelfBookMode.bookName = item.name;
	        shelfBookMode.posInShelf = -1;
	        shelfBookMode.author = item.author;
	
	        //此部分为本地书籍信息，
	        shelfBookMode.bookPath = item.savePath;
	        shelfBookMode.coverPath = "";
	        shelfBookMode.innerFileType = item.innerFileType;
	
	        //此部分为网络书籍信息
	        shelfBookMode.thumb = item.coverUrl;
	        shelfBookMode.bookId = item.bookId;
	        shelfBookMode.url = "";
	        shelfBookMode.chapterId = "";
	        shelfBookMode.chapterUrl = "";
	
	        DaoManager.getInstance().getShelfDao().saveOrUpdateOneBook(shelfBookMode);
        }
    }

    private void destoryServiceCheck() {
    	downloadDao.exitDownloadCheck(downloadTask.threadMap);
    }
}
