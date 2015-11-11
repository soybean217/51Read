package com.wanpg.bookread.ui.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.ClipData.Item;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wanpg.bookread.BaseApplication;
import com.wanpg.bookread.R;
import com.wanpg.bookread.common.ShuPengConfig;
import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.db.DaoManager;
import com.wanpg.bookread.db.ShelfDao;
import com.wanpg.bookread.download.DownItem;
import com.wanpg.bookread.ui.activity.BookDownloadActivity;
import com.wanpg.bookread.utils.MathUtil;
import com.wanpg.bookread.utils.TextUtil;
import com.wanpg.bookread.widget.AsyncImageView;
import com.wanpg.bookread.widget.Notice;

public class DownloadListAdapter extends BaseAdapter {
	private List<DownItem> listDownload, listFinish;
	private BookDownloadActivity mActivity;
	private HashMap<ViewHolder, Integer> mHolderMap;
	private int downSize,finishSize;
	public DownloadListAdapter(BookDownloadActivity mActivity, List<DownItem> listDownload, List<DownItem> listFinish) {
		// TODO Auto-generated constructor stub
		this.listDownload = listDownload;
		this.listFinish = listFinish;
		this.mActivity = mActivity;
		mHolderMap = new HashMap<ViewHolder, Integer>();
		downSize = 0;
		finishSize = 0;
		if (listDownload != null ) {
			downSize = listDownload.size();
		} 
		if(listFinish!=null){
			finishSize = listFinish.size();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return downSize + finishSize;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if (listDownload == null) {
			return null;
		} else {
			return listDownload.get(arg0);
		}
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(mActivity).inflate(R.layout.list_download_cell, null);
			holder = new ViewHolder();
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.info = (TextView) view.findViewById(R.id.download_info);
			holder.image = (AsyncImageView) view.findViewById(R.id.icon);
			holder.downloadBtn = (LinearLayout) view.findViewById(R.id.download_btn);
			holder.finishBtn = (LinearLayout) view.findViewById(R.id.finish_btn);
			holder.progress = (ProgressBar) view.findViewById(R.id.progress);
			holder.titleArea = view.findViewById(R.id.title_area);
			holder.title = (TextView) view.findViewById(R.id.title);
			holder.content = view.findViewById(R.id.content);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		if(position<downSize){
			if(position == 0){
				holder.titleArea.setVisibility(View.VISIBLE);
				holder.title.setText("下载中");
			}else{
				holder.titleArea.setVisibility(View.GONE);
			}
			
			holder.progress.setVisibility(View.VISIBLE);
			holder.info.setVisibility(View.VISIBLE);
			holder.finishBtn.setVisibility(View.GONE);
			holder.downloadBtn.setVisibility(View.VISIBLE);
			final DownItem item = listDownload.get(position);
			holder.name.setTag(item.downloadId);
			mHolderMap.put(holder, item.downloadId);
			holder.name.setText(item.name);
			int progress = (int) (((double) item.finishedLength / (double) item.fileLength) * 100);
			holder.progress.setProgress(progress);
			holder.info.setText(item.finishedLength + "k/" + item.fileLength + "k   " + progress + "%");
			holder.image.setImageUrl(ShuPengConfig.BOOK_COVER_MAIN_B + item.coverUrl, R.drawable.default_book_cover);
			switch (item.status) {
			case DownItem.STATUS_PAUSE:
			case DownItem.STATUS_FINISHED:
				//出错下载
				((ImageView) holder.downloadBtn.findViewById(R.id.iv)).setImageResource(R.drawable.download_continue_icon);
				((TextView) holder.downloadBtn.findViewById(R.id.tv)).setText("继续");
				break;
			case DownItem.STATUS_DOWNLOADING:
			case DownItem.STATUS_WAIT:
				//正在下载
				((ImageView) holder.downloadBtn.findViewById(R.id.iv)).setImageResource(R.drawable.download_stop_icon);
				((TextView) holder.downloadBtn.findViewById(R.id.tv)).setText("暂停");
				break;

			default:
				break;
			}
			holder.downloadBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(item.status == DownItem.STATUS_PAUSE 
							|| item.status == DownItem.STATUS_ERROR){
						//继续下载
						BaseApplication.getInstance().downloadService.continueOneTask(item);
						((ImageView) v.findViewById(R.id.iv)).setImageResource(R.drawable.download_stop_icon);
						((TextView) v.findViewById(R.id.tv)).setText("暂停");
					}else if(item.status == DownItem.STATUS_DOWNLOADING
							|| item.status == DownItem.STATUS_WAIT){
						//继续下载
						BaseApplication.getInstance().downloadService.stopOneTask(item);
						((ImageView) v.findViewById(R.id.iv)).setImageResource(R.drawable.download_continue_icon);
						((TextView) v.findViewById(R.id.tv)).setText("继续");
					}
				}
			});
			
			holder.content.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mActivity.showPop(v.findViewById(R.id.main), item);
				}
			});
		}else{
			if(position == downSize){
				holder.titleArea.setVisibility(View.VISIBLE);
				holder.title.setText("已完成");
			}else{
				holder.titleArea.setVisibility(View.GONE);
			}
			holder.progress.setVisibility(View.GONE);
			holder.info.setVisibility(View.GONE);
			holder.finishBtn.setVisibility(View.VISIBLE);
			holder.downloadBtn.setVisibility(View.GONE);

			final DownItem item = listFinish.get(position-downSize);
			holder.name.setText(item.name);
			holder.image.setImageUrl(ShuPengConfig.BOOK_COVER_MAIN_B + item.coverUrl, R.drawable.default_book_cover);
			
			holder.finishBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
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

					ShelfDao shelfDao = DaoManager.getInstance().getShelfDao();
					ShelfBook tmpBook = shelfDao.getShelfBookByBook(shelfBookMode);
					if(tmpBook==null){
						shelfDao.saveOrUpdateOneBook(shelfBookMode);
					}
					mActivity.openBook(tmpBook);
				}
			});

			holder.content.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mActivity.showPop(v.findViewById(R.id.main), item);
				}
			});
		}	
		
		return view;
	}

	private class ViewHolder {
		public TextView name, info;
		public View downloadBtn,finishBtn;
		public AsyncImageView image;
		public ProgressBar progress;
		public View titleArea;
		public View content;
		public TextView title;
	}
	
	public void updateItem(DownItem item) {
		// TODO Auto-generated method stub
		Iterator<ViewHolder> it = mHolderMap.keySet().iterator();
		while(it.hasNext()){
			ViewHolder holder = it.next();
			if(holder!=null){
				if(TextUtil.obj2int(holder.name.getTag()) == item.downloadId){
					int progress = (int) (((double) item.finishedLength / (double) item.fileLength) * 100);
					holder.progress.setProgress(progress);
					holder.info.setText(MathUtil.countFileSize(item.finishedLength) 
							+ "/" + MathUtil.countFileSize(item.fileLength) 
							+ "   " + MathUtil.countPercent(item.finishedLength, item.fileLength, 2));
					
					if(item.status == DownItem.STATUS_ERROR){
						((ImageView) holder.downloadBtn.findViewById(R.id.iv)).setImageResource(R.drawable.download_continue_icon);
						((TextView) holder.downloadBtn.findViewById(R.id.tv)).setText("继续");
					}					
					break;
				}
			}
		}
	}
	
	public void update(List<DownItem> listDownload, List<DownItem> listFinish) {
		// TODO Auto-generated constructor stub
		this.listDownload = listDownload;
		this.listFinish = listFinish;
		downSize = 0;
		finishSize = 0;
		if (listDownload != null ) {
			downSize = listDownload.size();
		} 
		if(listFinish!=null){
			finishSize = listFinish.size();
		}
		this.notifyDataSetChanged();
	}
	
	public void updateDownload(List<DownItem> listDownload){
		this.listDownload = listDownload;
		downSize = 0;
		if (listDownload != null ) {
			downSize = listDownload.size();
		} 
		this.notifyDataSetChanged();
	}
	public void updateFinish(List<DownItem> listFinish){
		this.listFinish = listFinish;
		finishSize = 0;
		if(listFinish!=null){
			finishSize = listFinish.size();
		}
		this.notifyDataSetChanged();
	}
}



