package com.wanpg.bookread.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import android.os.Handler;
import android.os.Message;

import com.wanpg.bookread.BaseApplication;
import com.wanpg.bookread.common.Config;
import com.wanpg.bookread.db.DaoManager;
import com.wanpg.bookread.utils.FileUtil;
import com.wanpg.bookread.utils.TextUtil;

public class DownloadThread extends Thread {

	public DownItem item;
	private int lastStatus = DownItem.STATUS_WAIT;
	private Handler handler;
	private BaseApplication mApplication;
	private DownloadTask mTask;
	public DownloadThread(DownItem item, Handler handler) {
		// TODO Auto-generated constructor stub
		this.item = item;
		this.handler = handler;
		mApplication = BaseApplication.getInstance();
		mTask = DownloadTask.getInstance();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		doBeforeThread();
		String result = doInThread();//线程运行之中
		doAfterThread(result);
	}

	/**
	 * 线程运行之前的代码
	 */
	public void doBeforeThread() {
		Message msg = new Message();
		msg.what = DownloadTask.NOTIFYCATION_NOTICE;
		msg.obj = item;
		handler.sendMessage(msg);
		
		lastStatus = item.status; 
		item.status = DownItem.STATUS_PREPARE;
		if(lastStatus == DownItem.STATUS_WAIT){
			//默认是等待状态
			try {
				System.setProperty("http.keepAlive", "false");
				URL url = new URL(item.url);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setReadTimeout(6000);
				connection.setRequestMethod("GET");
				item.fileLength = connection.getContentLength();//获取下载的总大小 
				item.contentType = connection.getContentType();//获取下载的类型 
				item.url = connection.getURL().toString();//此处修改downloadItem的url为此url，以便于下一次重新下载
				if (item.fileLength <= 0) {
					connection.disconnect();
					url = new URL(item.url);
					connection = null;
					connection = (HttpURLConnection) url.openConnection();
					connection.setReadTimeout(6000);
					connection.setRequestMethod("GET");
					item.fileLength = connection.getContentLength();//获取下载的总大小
					item.contentType = connection.getContentType();//获取下载的类型
					item.url = connection.getURL().toString();//此处修改downloadItem的url为此url，以便于下一次重新下载
				}
				String decodeUrl = URLDecoder.decode(item.url, "GBK");//防止url的是urlcode模式
				String fileType = "";
				if (FileUtil.checkDownloadIsOk(decodeUrl)) {
					fileType = decodeUrl.substring(decodeUrl.lastIndexOf("."));//根据真实的url获取文件的扩展名
				}else{ 
					fileType = item.innerFileType;
				}
				fileType = fileType.replaceAll("\\.", "");
				item.savePath = Config.BOOK_SD_BOOK + "/" + item.name + "." +fileType;//设置保存路径         
				item.contentType = fileType;//设置给item
				item.finishedLength = 0;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			File saveFile = new File(item.savePath);
			if(saveFile!=null && saveFile.exists()){

			}else{
				item.finishedLength = 0;
			}
		}
		item.status = DownItem.STATUS_DOWNLOADING;
	}

	/**
	 * 在线程中运行的代码
	 *
	 * @return
	 */
	public String doInThread() {
		boolean isAppend = false;
		//计算已经下载的偏移量
		long off = item.finishedLength;

		//拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
		HttpURLConnection connection = null;
		BufferedInputStream inSource = null;
		BufferedOutputStream bufOut = null;

		try {
			System.setProperty("http.keepAlive", "false");
			URL getUrl = new URL(item.url);
			//根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
			//返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
			connection = (HttpURLConnection) getUrl.openConnection();
			connection.setReadTimeout(6000);
			connection.setRequestMethod("GET");
			File saveFile = new File(item.savePath);//创建指定路径的下载文件
			if(saveFile.exists()){
				if(off >0 ){
					connection.setRequestProperty("Range", "bytes=" + off );
					item.finishedLength = off;
					isAppend = true;
				}else{
					if(lastStatus == DownItem.STATUS_WAIT){
						//说明是第一次下载,之前有下载的，将这个名字命名加上()
						item.savePath = checkSavePath(item.savePath);
						saveFile = new File(item.savePath);
						item.name = saveFile.getName().substring(0, saveFile.getName().lastIndexOf("."));
					}
				}
			}else{
				File parent = new File(saveFile.getParent());
				if (!parent.exists()) parent.mkdirs();
				item.finishedLength = 0;
			}
			inSource = new BufferedInputStream(connection.getInputStream());//取得输入流，并使用Reader读取
			bufOut = new BufferedOutputStream(new FileOutputStream(saveFile, isAppend));
			int len = 0;
			// 缓冲数组
			byte[] b = new byte[768];
			while ((len = inSource.read(b)) != -1) {
				if (item.status == DownItem.STATUS_PAUSE || item.status == DownItem.STATUS_DELETE) {
					break;
				}
				bufOut.write(b, 0, len);
				item.finishedLength += len;//此处统计现在下载了多少，刷新进度
				doUpdateInThread(null);
				//                    if(item.finishedLength<item.fileLength){
				//                    	//下载出错
				//                    	return "FAIL";
				//                    }
			}
			bufOut.flush();   // 刷新此缓冲的输出流         
			return "SUCCESS";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "FAIL";
		} finally {
			//最后的处理
			try {
				if(bufOut!=null) bufOut.close();
				if(inSource!=null) inSource.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(connection!=null) connection.disconnect();
		}
	}

	/**
	 * 线程运行后的代码
	 *
	 * @param result
	 */
	public void doAfterThread(String result) {

		if (result.equals("SUCCESS")) {
			if (item.status == DownItem.STATUS_PAUSE) {
				//停止
				Message msg = mApplication.downloadService.downloadHandler.obtainMessage(DownloadService.STOP_TASK_NOTIFY, item.downloadId);
				mApplication.downloadService.downloadHandler.sendMessage(msg);
			} else if (item.status == DownItem.STATUS_DELETE) {
				//删除
				Message msg = mApplication.downloadService.downloadHandler.obtainMessage(DownloadService.DELETE_TASK_NOTIFY, item.downloadId);
				mApplication.downloadService.downloadHandler.sendMessage(msg);
				FileUtil.del(new File(item.savePath));
			} else {
				item.status = DownItem.STATUS_FINISHED;
				Message msg = mApplication.downloadService.downloadHandler.obtainMessage(DownloadService.FINISHED_SUCCESS_NOTIFY, item.downloadId);
				mApplication.downloadService.downloadHandler.sendMessage(msg);
			}
		} else {
			//下载失败
			item.status = DownItem.STATUS_ERROR;
			Message msg = mApplication.downloadService.downloadHandler.obtainMessage(DownloadService.FINISHED_FAIL_NOTIFY, item.downloadId);
			mApplication.downloadService.downloadHandler.sendMessage(msg);
			FileUtil.del(new File(item.savePath));
		}
		
		Message msg = new Message();
		msg.what = DownloadTask.NOTIFYCATION_NOTICE;
		msg.obj = item;
		handler.sendMessage(msg);
		
		Message msg1 = new Message();
		msg1.what = DownloadTask.OBSERVER_NOTICE;
		msg1.obj = item;
		handler.sendMessage(msg1);
		
		mTask.threadMap.put(item.downloadId, item);
		DaoManager.getInstance().getDownloadDao().updateOneDownloadItem(item);
	}


	int lastLength = 0;
	/**
	 * 被唤起时的更新代码
	 *
	 * @param info
	 */
	public void doUpdateInThread(Object info) {
		Message msg = new Message();
		msg.what = DownloadTask.NOTIFYCATION_UPDATE;
		msg.obj = item;
		msg.arg1 = lastLength;
		handler.sendMessage(msg);
		
		Message msg1 = new Message();
		msg1.what = DownloadTask.OBSERVER_UPDATE;
		msg1.obj = item;
		handler.sendMessage(msg1);
		lastLength = (int) item.finishedLength;
	}

	private String checkSavePath(String str) {
		// TODO Auto-generated method stub
		int pointPos = str.lastIndexOf(".");
		int posStart = str.lastIndexOf("(");
		int posEnd = str.lastIndexOf(")");
		if(posEnd == pointPos-1){
			//在.之前有）
			String s1 = str.substring(posStart+1, posEnd);
			if(TextUtil.isString2Int(s1)){
				int a = Integer.parseInt(s1);
				str = str.substring(0, posStart)+"("+(a+1)+")"+str.substring(pointPos);
			}else{
				str = str.substring(0, pointPos)+"(1)"+str.substring(pointPos);
			}
		}else{
			//直接在.之前加上（1）
			str = str.substring(0, pointPos)+"(1)"+str.substring(pointPos);
		}
		if(new File(str).exists()){
			str = checkSavePath(str);
		}
		return str;
	}
}