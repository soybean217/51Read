package com.wanpg.bookread.ui.book;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.api.ShuPengApi;
import com.wanpg.bookread.data.BookBoard;
import com.wanpg.bookread.logic.BackTask;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.widget.AsyncImageView;
import com.wanpg.bookread.widget.DropRefreshClickLoadListView;
import com.wanpg.bookread.widget.DropRefreshClickLoadListView.OnLoadListener;
import com.wanpg.bookread.widget.DropRefreshClickLoadListView.OnRefreshListener;
import com.wanpg.bookread.widget.Notice;

public class StoreHotBoardListFragment extends BaseFragment {

	private static final int LOAD_DATA_SUCCESS = 0;
	private static final int LOAD_DATA_FAIL = 1;
	private static final int UPDATE_DATA_SUCCESS = 2;
	private static final int UPDATE_DATA_FAIL = 3;
	private static final int BOTTOM_LOAD_DATA_SUCCESS = 4;
	private static final int BOTTOM_LOAD_DATA_FAIL = 5;

	private View v_main;
	private DropRefreshClickLoadListView lv_board_list;
	private View v_load;
	private List<BookBoard> listBoards = null;
	private List<BookBoard> listTmp;
	private MyAdapter adapter;
	private int pIndex = 1;
	private int pSize = 10;
	private int imageWidth = 0;
	private int imageHeight = 0;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case LOAD_DATA_SUCCESS:
				v_load.setVisibility(View.GONE);
				lv_board_list.setVisibility(View.VISIBLE);
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
				listBoards = listTmp;
				adapter.setListItems(listBoards);
				adapter.notifyDataSetChanged();
				break;
			case UPDATE_DATA_FAIL:
				Notice.showToast("没有新数据！");
				break;
			case BOTTOM_LOAD_DATA_SUCCESS:
				listBoards.addAll(listTmp);
				adapter.setListItems(listBoards);
				adapter.notifyDataSetChanged();
				if (listTmp.size() < pSize) {
					lv_board_list.hideFootMoreView();
				}
				break;
			case BOTTOM_LOAD_DATA_FAIL:
				Notice.showToast("没有新数据！");
				lv_board_list.hideFootMoreView();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(!resetView(v_main)){
			imageWidth = DisplayUtil.getDisWidth() - 2 * DisplayUtil.dp2px(1);
			imageHeight = imageWidth * 23 / 64;

			v_main = inflater.inflate(R.layout.fragment_store_hot_board, null);
			v_main.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			((TextView) v_main.findViewById(R.id.tv_title)).setText("精选专辑");
			setHeadBackEvent(v_main.findViewById(R.id.ib_back));

			lv_board_list = (DropRefreshClickLoadListView) v_main.findViewById(R.id.lv_board_list);
			v_load = v_main.findViewById(R.id.in_loading);
			v_load.setVisibility(View.VISIBLE);
			lv_board_list.setVisibility(View.GONE);
			initData();
		}
		return v_main;
	}

	private void initData() {
		// TODO Auto-generated method stub
		new BackTask() {
			@Override
			public String doInThread() {
				// TODO Auto-generated method stub
				if (listBoards == null) {
					listBoards = ShuPengApi.getBoardModes(pIndex, pSize);
				}
				if (listBoards == null) {
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

			@Override
			public void doInThreadWhenCalling(String info) {
				// TODO Auto-generated method stub

			}
		}.submit();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		adapter = new MyAdapter(getActivity(), listBoards, lv_board_list);
		lv_board_list.setAdapter(adapter);

		lv_board_list.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new BackTask() {

					@Override
					public void doInThreadWhenCalling(String info) {
						// TODO Auto-generated method stub

					}

					@Override
					public void doBeforeThread() {
						// TODO Auto-generated method stub

					}

					@Override
					public void doAfterThread(String result) {
						// TODO Auto-generated method stub
						lv_board_list.onRefreshComplete();
					}

					@Override
					public String doInThread() {
						// TODO Auto-generated method stub
						pIndex = 1;
						listTmp = ShuPengApi.getBoardModes(pIndex, pSize);
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
		lv_board_list.setOnLoadListener(new OnLoadListener() {

			@Override
			public void onLoad() {
				// TODO Auto-generated method stub
				new BackTask() {

					@Override
					public void doInThreadWhenCalling(String info) {
						// TODO Auto-generated method stub

					}

					@Override
					public void doBeforeThread() {
						// TODO Auto-generated method stub
						lv_board_list.onLoadComplete();
					}

					@Override
					public void doAfterThread(String result) {
						// TODO Auto-generated method stub
					}

					@Override
					public String doInThread() {
						// TODO Auto-generated method stub
						pIndex++;
						listTmp = ShuPengApi.getBoardModes(pIndex, pSize);
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

	private class MyAdapter extends BaseAdapter {

		private Context context;
		private List<BookBoard> listItems;
		private ListView listView;
		private ViewHolder holder;

		public MyAdapter(Context context, List<BookBoard> listItems, ListView listView) {
			// TODO Auto-generated constructor stub
			this.context = context;
			this.listItems = listItems;
			this.listView = listView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (listItems == null) {
				return 0;
			} else {
				return listItems.size();
			}
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			if (listItems == null) {
				return null;
			} else {
				return listItems.get(arg0);
			}
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if (arg1 == null) {
				arg1 = LayoutInflater.from(context).inflate(R.layout.board_list_cell, null);
				int w = android.widget.AbsListView.LayoutParams.FILL_PARENT;
				android.widget.AbsListView.LayoutParams layoutParams = new android.widget.AbsListView.LayoutParams(w, imageHeight);
				arg1.setLayoutParams(layoutParams);
				holder = new ViewHolder();
				holder.imageView = (AsyncImageView) arg1.findViewById(R.id.iv_board);
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}

			final int x = arg0;
			String imageHref = listItems.get(x).banner;
			holder.imageView.setImageUrl(imageHref, R.drawable.image_load, imageWidth, imageHeight);

			holder.imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					FragmentTransaction transaction = mActivity.mFragmentManager.beginTransaction();
					StoreBoardShowFragment fragment = new StoreBoardShowFragment();
					Bundle bundle = new Bundle();
					bundle.putString("BOARD_NAME", listItems.get(x).name);
					bundle.putInt("BOARD_ID", listItems.get(x).id);
					bundle.putString("BOARD_BANNER", listItems.get(x).banner);
					fragment.setArguments(bundle);

					transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_right,
							R.anim.slide_in_from_right, R.anim.slide_out_from_right);
					transaction.add(R.id.fragment_container, fragment);
					transaction.addToBackStack(null);
					transaction.commit();
				}
			});

			return arg1;
		}

		private class ViewHolder {
			public AsyncImageView imageView;
		}

		public void setListItems(List<BookBoard> listItems) {
			this.listItems = listItems;
		}
	}
}
