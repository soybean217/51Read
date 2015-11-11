package com.wanpg.bookread.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

import com.wanpg.bookread.utils.FileUtil;
import com.wanpg.bookread.utils.SDCardUtil;
import com.wanpg.bookread.utils.TextUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashHandler implements UncaughtExceptionHandler {
    public static String TMP_LOG;
	private static CrashHandler mCrashHandler = new CrashHandler();
	private CrashHandler() {}
	public static CrashHandler getInstance() {
		return mCrashHandler;
	}
	
	private final String TAG = "CrashHandler";
	private Context mContext;
	public void init(Context context) {
		mContext = context;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e(TAG, "Thread id:" + thread.getId() + " name:" + thread.getName() + " message:" + ex.getMessage());
		ex.printStackTrace();
		if (handleException(ex)) {
			
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	private boolean handleException(Throwable ex) {
		PackageManager pm = mContext.getPackageManager();
		StringBuilder sb = new StringBuilder();
		try {
			PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
			sb.append("versionCode=").append(pi.versionCode);
			sb.append(",versionName=").append(pi.versionName);
			sb.append(",model=").append(Build.MODEL);
			Log.e(TAG, "CrashHandler error data:" + sb.toString());

			String errorMessage = extractErrorMessage(ex);
			Log.e(TAG, "CrashHandler error message:" + errorMessage);
			save2LocalFile(errorMessage);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private String extractErrorMessage(Throwable ex) {
		StringBuilder result = new StringBuilder("\r\n\r\n");
		result.append("Exception occured at ");
		result.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		result.append("\r\n");
		if (ex != null) {
			StringWriter info = new StringWriter();
			PrintWriter printWriter = new PrintWriter(info);
			try {
				ex.printStackTrace(printWriter);
				Throwable cause = ex.getCause();
				while (cause != null) {
					cause.printStackTrace(printWriter);
					cause = cause.getCause();
				}
				result.append(info.toString());				
			} finally {
				try {
					if (info != null) info.close();
					if (printWriter != null) printWriter.close();
				} catch(Exception e) {}
			}
		}
		return result.toString();
	}
	
	private void save2LocalFile(String result) {
        //写入文件
        FileWriter fw = null;
        BufferedWriter out = null;
        try {
        	//创建目录
        	if (SDCardUtil.isSDCardAvailable()) {
        		FileUtil.createFolderIfNotExist(getLogPath());
        	}else {
                return;
            }
        	//每个月一个LOG文件
            String fileName = "crash-" + new SimpleDateFormat("yyyyMM").format(new Date()) + ".log";
            File file = new File(getLogPath() + fileName);
            if (!FileUtil.createFileIfNotExist(file)){
                return ;
            }
            fw = new FileWriter(file, true);
            out = new BufferedWriter(fw);
            out.write(result, 0, result.length()-1);
            out.flush();
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	try {
        		if (fw != null) fw.close();
        		if (out != null) out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}

    private String getLogPath(){
        if (TextUtil.isEmpty(TMP_LOG)){
            TMP_LOG = mContext.getExternalCacheDir() + "/logs/";
        }
        return TMP_LOG;
    }

}
