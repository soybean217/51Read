package com.wanpg.bookread.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.wanpg.bookread.BaseActivity;
import com.wanpg.bookread.BaseApplication;
import com.wanpg.bookread.R;
import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.db.DaoManager;
import com.wanpg.bookread.db.ShelfDao;
import com.wanpg.bookread.download.DownItem;
import com.wanpg.bookread.download.DownloadObserver;
import com.wanpg.bookread.download.DownloadTask;
import com.wanpg.bookread.ui.MainActivity;
import com.wanpg.bookread.ui.adapter.DownloadListAdapter;
import com.wanpg.bookread.ui.book.StoreBookShowFragment;
import com.wanpg.bookread.widget.DownloadManagePopWindow;
import com.wanpg.bookread.widget.Notice;

/**
 * 下载界面
 *
 * @author Jinpeng
 */
public class BookDownloadActivity extends BaseActivity {

	List<DownItem> listDownload = new ArrayList<DownItem>();
	List<DownItem> listFinished = new ArrayList<DownItem>();

	DownloadListAdapter mDownloadListAdapter;
	private ListView mListView;
	Handler handler;

	public static final int DEL_ITEM_NOTOFY = 2;
	public static final int ADD_ITEM_SHELF_NOTOFY = 3;
	public static final int CANCEL_ITEM_NOTOFY = 4;
	public static final int INFO_ITEM_NOTOFY = 5;

	public int fragment_container_id = R.id.fragment_container;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_download);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 0:
					DownItem item1 = (DownItem) msg.obj;
					mDownloadListAdapter.updateItem(item1);
					break;
				case 1:
					mDownloadListAdapter.notifyDataSetChanged();
					break;
				case ADD_ITEM_SHELF_NOTOFY:
					DownItem item = (DownItem) msg.obj;
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
						Notice.showToast("添加书架成功！");
					}else{
						Notice.showToast("书架已经存在这本书！");
					}
					break;
				case DEL_ITEM_NOTOFY:
					DownItem item2 = (DownItem) msg.obj;
					BaseApplication.getInstance().downloadService.deleteOneFinishedTask(item2);
					listFinished.remove(item2);
					mDownloadListAdapter.updateFinish(listFinished);
					break;
				case CANCEL_ITEM_NOTOFY:
					DownItem item3 = (DownItem) msg.obj;
					BaseApplication.getInstance().downloadService.deleteOneDownloadTaskFromThread(item3);
					listDownload.remove(item3);
					mDownloadListAdapter.updateDownload(listDownload);
					break;
				case INFO_ITEM_NOTOFY:
					DownItem item4 = (DownItem) msg.obj;
					StoreBookShowFragment storeBookShowFragment = new StoreBookShowFragment();
					Bundle bundle = new Bundle();
					bundle.putInt("bookId", item4.bookId);
					storeBookShowFragment.setArguments(bundle);
					mFragmentManager.beginTransaction()
					.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_right,
							R.anim.slide_in_from_right, R.anim.slide_out_from_right)
							.add(R.id.fragment_container, storeBookShowFragment)
							.addToBackStack(null)
							.commit();
					break;
				default:
					break;
				}
			}
		};
		initData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DownloadTask.getInstance().setObserver(mDownloadObserver);
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		DownloadTask.getInstance().setObserver(null);
	}

	private DownloadObserver mDownloadObserver = new DownloadObserver() {

		@Override
		protected void update(DownItem item) {
			// TODO Auto-generated method stub
			for (int i = 0; i < listDownload.size(); i++) {
				if (listDownload.get(i).downloadId == item.downloadId) {
					listDownload.remove(i);
					listDownload.add(i, item);
					break;
				}
			}
			mDownloadListAdapter.updateItem(item);
		}

		@Override
		protected void notice(DownItem item) {
			// TODO Auto-generated method stub
			if (item.status == DownItem.STATUS_FINISHED) {
				getDownInfo();
				mDownloadListAdapter.update(listDownload, listFinished);
			}else if(item.status == DownItem.STATUS_ERROR){
				for (int i = 0; i < listDownload.size(); i++) {
					if (listDownload.get(i).downloadId == item.downloadId) {
						listDownload.remove(i);
						listDownload.add(i, item);
						break;
					}
				}
				mDownloadListAdapter.updateItem(item);
			}
		}
	};


	private void initData() {
		// TODO Auto-generated method stub
		getDownInfo();
		initUI();
	}

	private void initUI() {
		// TODO Auto-generated method stub

		((TextView) findViewById(R.id.tv_title)).setText("下载管理");

        setBackEvent(findViewById(R.id.ib_back));
        

		mListView = (ListView) findViewById(R.id.lv);

		mDownloadListAdapter = new DownloadListAdapter(this, listDownload, listFinished);
		mListView.setAdapter(mDownloadListAdapter);
	}

	private DownloadManagePopWindow mDMPopWindow;
	public void showPop(View view, DownItem item) {
		// TODO Auto-generated method stub
		if(mDMPopWindow==null)
			mDMPopWindow = new DownloadManagePopWindow(this, handler);
		mDMPopWindow.openPopWindow(item, view);
	}
	
	private void getDownInfo() {
		// TODO Auto-generated method stub
		//从服务获得信息
		listDownload = DownloadTask.getInstance().getDownloadTask();
		listFinished = DownloadTask.getInstance().getFinishedTask();	
	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(mFragmentManager.getBackStackEntryCount()>0){
			mFragmentManager.popBackStack();
		}else{
			Intent intent = new Intent(BookDownloadActivity.this, MainActivity.class);
			intent.putExtra("isFromDown", true);
			startActivity(intent);
			super.onBackPressed();
		}
	}
}
