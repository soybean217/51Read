package com.wanpg.bookread.ui.book;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.api.ShuPengApi;
import com.wanpg.bookread.logic.BackTask;
import com.wanpg.bookread.ui.adapter.BookNewUpdateListAdapter;
import com.wanpg.bookread.widget.DropRefreshClickLoadListView;
import com.wanpg.bookread.widget.DropRefreshClickLoadListView.OnLoadListener;
import com.wanpg.bookread.widget.DropRefreshClickLoadListView.OnRefreshListener;
import com.wanpg.bookread.widget.Notice;

public class BookNewUpdateFragment extends BaseFragment {

	private static final int LOAD_DATA_SUCCESS = 0;
	private static final int LOAD_DATA_FAIL = 1;
	private static final int UPDATE_DATA_SUCCESS = 2;
	private static final int UPDATE_DATA_FAIL = 3;
	private static final int BOTTOM_LOAD_DATA_SUCCESS = 4;
	private static final int BOTTOM_LOAD_DATA_FAIL = 5;

	private View v_main = null;

	private View v_load;

	private DropRefreshClickLoadListView lv_book_list;

	private List<Map<String, Object>> listBooks;
	private List<Map<String, Object>> listTmp;

	private BookNewUpdateListAdapter listAdapter;

	private int pIndex = 1;
	private int pSize = 20;


	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case LOAD_DATA_SUCCESS:
				v_load.setVisibility(View.GONE);
				lv_book_list.setVisibility(View.VISIBLE);
				initUI();
				break;
			case LOAD_DATA_FAIL:
				v_load.findViewById(R.id.loading).setVisibility(View.GONE);
				v_load.findViewById(R.id.load_fail).setVisibility(View.VISIBLE);
				v_load.findViewById(R.id.load_fail_button).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						initData();
					}
				});
				break;
			case UPDATE_DATA_SUCCESS:
				listBooks = listTmp;
				listAdapter.setListItems(listBooks);
				listAdapter.notifyDataSetChanged();
				break;
			case UPDATE_DATA_FAIL:
				Notice.showToast("没有新数据！");
				break;
			case BOTTOM_LOAD_DATA_SUCCESS:
				listBooks.addAll(listTmp);
				listAdapter.setListItems(listBooks);
				listAdapter.notifyDataSetChanged();
				if (listTmp.size() < pSize) {
					lv_book_list.hideFootMoreView();
				}
				break;
			case BOTTOM_LOAD_DATA_FAIL:
				Notice.showToast("没有新数据！");
				lv_book_list.hideFootMoreView();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(!resetView(v_main)){
			v_main = inflater.inflate(R.layout.activity_store_book_list, null);
			v_main.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			((TextView) v_main.findViewById(R.id.tv_title)).setText("最新更新");
			setHeadBackEvent(v_main.findViewById(R.id.ib_back));

			lv_book_list = (DropRefreshClickLoadListView) v_main.findViewById(R.id.lv_book_list);
			v_load = v_main.findViewById(R.id.in_loading);
			v_load.setVisibility(View.VISIBLE);
			lv_book_list.setVisibility(View.GONE);
			initData();
		}
		return v_main;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	private void initData() {
		// TODO Auto-generated method stub
		new BackTask() {

			@Override
			public void doInThreadWhenCalling(String info) {
				// TODO Auto-generated method stub

			}

			@Override
			public String doInThread() {
				// TODO Auto-generated method stub
				if (listBooks == null) {
					listBooks = ShuPengApi.getUpdateNetnovel(pIndex, pSize);
				}
				if (listBooks == null) {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler.sendEmptyMessage(LOAD_DATA_FAIL);
				} else {
					handler.sendEmptyMessage(LOAD_DATA_SUCCESS);
				}
				return null;
			}

			@Override
			public void doBeforeThread() {
				// TODO Auto-generated method stub
				v_load.findViewById(R.id.loading).setVisibility(View.VISIBLE);
				v_load.findViewById(R.id.load_fail).setVisibility(View.GONE);
			}

			@Override
			public void doAfterThread(String result) {
				// TODO Auto-generated method stub

			}
		}.submit();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		listAdapter = new BookNewUpdateListAdapter(mActivity, listBooks);
		lv_book_list.setAdapter(listAdapter);
		if (listBooks.size() < pSize) {
			lv_book_list.hideFootMoreView();
		}
		lv_book_list.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new BackTask() {

					@Override
					public void doBeforeThread() {
						// TODO Auto-generated method stub

					}

					@Override
					public void doAfterThread(String result) {
						// TODO Auto-generated method stub
						lv_book_list.onRefreshComplete();
					}

					@Override
					public void doInThreadWhenCalling(String info) {
						// TODO Auto-generated method stub

					}

					@Override
					public String doInThread() {
						// TODO Auto-generated method stub
						pIndex = 1;
						listTmp = ShuPengApi.getUpdateNetnovel(pIndex, pSize);
						if (listTmp == null) {
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							handler.sendEmptyMessage(UPDATE_DATA_FAIL);
						} else {
							handler.sendEmptyMessage(UPDATE_DATA_SUCCESS);
						}
						return null;
					}
				}.submit();
			}
		});

		lv_book_list.setOnLoadListener(new OnLoadListener() {

			@Override
			public void onLoad() {
				// TODO Auto-generated method stub
				new BackTask() {

					@Override
					public void doBeforeThread() {
						// TODO Auto-generated method stub

					}

					@Override
					public void doAfterThread(String result) {
						// TODO Auto-generated method stub
						lv_book_list.onLoadComplete();
					}

					@Override
					public void doInThreadWhenCalling(String info) {
						// TODO Auto-generated method stub

					}

					@Override
					public String doInThread() {
						// TODO Auto-generated method stub
						pIndex++;
						listTmp = ShuPengApi.getUpdateNetnovel(pIndex, pSize);
						if (listTmp == null) {
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							pIndex--;
							handler.sendEmptyMessage(BOTTOM_LOAD_DATA_FAIL);
						} else {
							handler.sendEmptyMessage(BOTTOM_LOAD_DATA_SUCCESS);
						}
						return null;
					}
				}.submit();
			}
		});
	}

}
