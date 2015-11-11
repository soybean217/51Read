package com.wanpg.bookread.api;

public class Api {

	public static boolean isMainThread() {
		// TODO Auto-generated method stub
		return Thread.currentThread().getName().equals("main");
	}
	
	public static void callback(ResultListener listener, int status, Object info) {
		if (listener != null) listener.onResult(status, info);
	}
	
	public interface ResultListener {
		int SUCCESS = 1;
		/** 请求失败 */
		int FAILURE = 2;
		/** 请求错误，未返回 */
		int ERROR = 3;
		/** 参数不合法 */
		int INVALIDATE = 4;
		/** 无网络连接 */
		int NETWORK_UNAVAILABLE = 5;
		void onResult(int status, Object info);
	}
	
	public static void sumitNewThread(Runnable runnable) {
		// TODO Auto-generated method stub
		new Thread(runnable).start();
	}
	
}
