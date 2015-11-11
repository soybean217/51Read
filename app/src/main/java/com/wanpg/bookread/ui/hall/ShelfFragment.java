package com.wanpg.bookread.ui.hall;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.db.DBHelper.ShelfBookTable;
import com.wanpg.bookread.db.Dao.DataBaseObserver;
import com.wanpg.bookread.db.DaoManager;
import com.wanpg.bookread.db.ShelfDao;
import com.wanpg.bookread.ui.adapter.BookShelfGridViewAdapter;

public class ShelfFragment extends BaseFragment {

	private GridView gv_bookshelf;
	private BookShelfGridViewAdapter bsgva;
	//private ListView lv_bookshelf;
	//private BookShelfListViewAdapter bslva;
	private View mMainView;
	public final static int UPDATE_BOOK_SHELF_NOTIFY = 0;
	private ShelfDao mShelfDao;
	private List<ShelfBook> mBooks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mShelfDao = DaoManager.getInstance().getShelfDao();
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(!resetView(mMainView)){
			mMainView = inflater.inflate(R.layout.layout_bookshelf, null);
			initUI();
		}
		return mMainView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bindData();
		mShelfDao.setDBObserver(mDataBaseObserver);
	}
	
	private DataBaseObserver mDataBaseObserver = new DataBaseObserver() {
		
		@Override
		public void onDataChanged(String dbName, int flag) {
			// TODO Auto-generated method stub
			if(dbName.equals(ShelfBookTable.TABLE_NAME)){
				bindData();
			}
		}
	};
	
	private void initData() {
		// TODO Auto-generated method stub
		mBooks = new ArrayList<ShelfBook>();
	}
	
	private void initUI() {
		// TODO Auto-generated method stub
		gv_bookshelf = (GridView) mMainView.findViewById(R.id.gv_bookshelf);
		bsgva = new BookShelfGridViewAdapter(this, mBooks);
		gv_bookshelf.setAdapter(bsgva);
	}
	
	
	private void bindData() {
		// TODO Auto-generated method stub
		mBooks = mShelfDao.getShelfBooks();
		mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				bsgva.update(mBooks);
			}
		});
	}
}
