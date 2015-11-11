package com.wanpg.bookread.download;


/**
 * 下载观察者
 *
 * @author Jinpeng
 */
public abstract class DownloadObserver {

	/**
	 * 更新
	 *
	 * @param item
	 */
	public void updateNotify(DownItem item) {
		if(item!=null){
			update(item);
		}
	}

	/**
	 * 提示，状态改变时候的提醒
	 *
	 * @param status
	 * @param item
	 */
	public void noticeNotify(DownItem item) {
		notice(item);
	}

	/**
	 * 更新中调用此处
	 */
	protected abstract void update(DownItem item);

	/**
	 * 在一些节点处调用此方法
	 */
	protected abstract void notice(DownItem item);

}
