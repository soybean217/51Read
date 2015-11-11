package com.wanpg.bookread.ui.book;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.api.ShuPengApi;
import com.wanpg.bookread.logic.BackTask;
import com.wanpg.bookread.manager.ShelfManager;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.utils.WidgetUtil;

public class StoreCategoryFragment extends BaseFragment {
	private static final int LOAD_DATA_SUCCESS = 0;
	private static final int LOAD_DATA_FAIL = 1;

	private List<Map<String, Object>> listNet = null;
	private List<Map<String, Object>> listBook = null;


	private View v_main = null;

	private View v_load;

	private Button bt_store_category_net, bt_store_category_book;

	private ListView lv_store_category_net, lv_store_category_book;
	private LinearLayout ll_category_main;

	/**
	 * 选择按钮的指示
	 * 0--为左侧的网络文学
	 * 1--为右侧的畅销书
	 */
	private int chooseStatus = 1;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case LOAD_DATA_SUCCESS:
				v_load.setVisibility(View.GONE);
				ll_category_main.setVisibility(View.VISIBLE);
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
			v_main = inflater.inflate(R.layout.fragment_store_category, null);
			v_main.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			((TextView) v_main.findViewById(R.id.tv_title)).setText("图书分类");
			setHeadBackEvent(v_main.findViewById(R.id.ib_back));
			ll_category_main = (LinearLayout) v_main.findViewById(R.id.ll_category_main);
			bt_store_category_net = (Button) v_main.findViewById(R.id.bt_store_category_net);
			bt_store_category_book = (Button) v_main.findViewById(R.id.bt_store_category_book);

			lv_store_category_net = (ListView) v_main.findViewById(R.id.lv_store_category_net);
			lv_store_category_book = (ListView) v_main.findViewById(R.id.lv_store_category_book);

			v_load = v_main.findViewById(R.id.in_loading);
			v_load.setVisibility(View.VISIBLE);
			ll_category_main.setVisibility(View.GONE);

			initData();
		}
		return v_main;
	}

	private void initData() {
		// TODO Auto-generated method stub
		new BackTask() {

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

			@Override
			public String doInThread() {
				// TODO Auto-generated method stub
				if (ShelfManager.getManager().mapCategory == null) {
					ShelfManager.getManager().mapCategory = ShuPengApi.getCategoryList();
				}
				if (ShelfManager.getManager().mapCategory == null) {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler.sendEmptyMessage(LOAD_DATA_FAIL);
				} else {
					listNet = ShelfManager.getManager().mapCategory.get("网络文学");
					listBook = ShelfManager.getManager().mapCategory.get("畅销书");
					handler.sendEmptyMessage(LOAD_DATA_SUCCESS);
				}
				return null;
			}
		}.submit();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		chooseStatus = 1;
		bt_store_category_net.setBackgroundResource(R.drawable.subtab_left_selector);
		bt_store_category_book.setBackgroundResource(R.drawable.subtab_item_bg_selected_right);

		bt_store_category_net.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (chooseStatus != 0) {
					//加载网络文学的内容
					chooseStatus = 0;
					bt_store_category_net.setBackgroundResource(R.drawable.subtab_item_bg_selected_left);
					bt_store_category_book.setBackgroundResource(R.drawable.subtab_right_selector);
					lv_store_category_net.setVisibility(View.VISIBLE);
					lv_store_category_book.setVisibility(View.GONE);
				}
			}
		});
		bt_store_category_book.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (chooseStatus != 1) {
					//加载畅销书的内容
					chooseStatus = 1;
					bt_store_category_net.setBackgroundResource(R.drawable.subtab_left_selector);
					bt_store_category_book.setBackgroundResource(R.drawable.subtab_item_bg_selected_right);
					lv_store_category_net.setVisibility(View.GONE);
					lv_store_category_book.setVisibility(View.VISIBLE);
				}
			}
		});
		lv_store_category_net.setVisibility(View.GONE);
		lv_store_category_book.setVisibility(View.VISIBLE);

		MyAdapter myAdapterNet = new MyAdapter(getActivity(), listNet, lv_store_category_net);
		lv_store_category_net.setAdapter(myAdapterNet);
		MyAdapter myAdapterBook = new MyAdapter(getActivity(), listBook, lv_store_category_book);
		lv_store_category_book.setAdapter(myAdapterBook);
	}


	private class MyAdapter extends BaseAdapter {

		private Context context;
		private List<Map<String, Object>> listItems;
		private ViewHolder holder;
		private List<Integer> listOpened = new ArrayList<Integer>();
		private LayoutParams layoutParams;
		private int cellHeight;
		private ListView listView;

		public MyAdapter(Context context, List<Map<String, Object>> listItems, ListView listView) {
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
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (listItems == null) {
				return null;
			} else {
				return listItems.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.store_category_list_cell, null);
				layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, DisplayUtil.dp2px(42));
				convertView.setLayoutParams(layoutParams);
				holder = new ViewHolder();

				holder.rl_main = (RelativeLayout) convertView.findViewById(R.id.rl_category_main);
				holder.tv_main = (TextView) convertView.findViewById(R.id.tv_category_main);
				holder.iv_main = (ImageView) convertView.findViewById(R.id.iv_category_main);
				holder.lv_second = (ListView) convertView.findViewById(R.id.lv_second_category);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final int x = position;
			final View v1 = convertView;
			holder.iv_main.setTag(x);

			holder.tv_main.setText(listItems.get(x).get("name").toString());

			@SuppressWarnings("unchecked")
			final List<Map<String, Object>> listDesc = (List<Map<String, Object>>) listItems.get(x).get("desc");
			SimpleAdapter adapter = new SimpleAdapter(context, listDesc, R.layout.store_category_second_list_cell,
					new String[]{"name"}, new int[]{R.id.tv_second_category});
			holder.lv_second.setAdapter(adapter);
			WidgetUtil.setListViewHeightBasedOnChildren(holder.lv_second);

			holder.rl_main.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (listOpened.contains(x)) {
						int a = listOpened.indexOf(x);
						listOpened.remove(a);
						((ImageView) listView.findViewWithTag(x)).setImageResource(R.drawable.item_down);
						cellHeight = holder.rl_main.getHeight();

					} else {
						listOpened.add(x);
						((ImageView) listView.findViewWithTag(x)).setImageResource(R.drawable.item_up);
						cellHeight = holder.lv_second.getHeight() + holder.rl_main.getHeight() + 1;
					}
					layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, cellHeight);
					v1.setLayoutParams(layoutParams);
				}
			});

			holder.lv_second.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Bundle bundle = new Bundle();
					bundle.putInt("cid", Integer.parseInt(listItems.get(x).get("cid").toString()));
					bundle.putInt("scid", Integer.parseInt(listDesc.get(arg2).get("scid").toString()));
					bundle.putString("scname", listDesc.get(arg2).get("name").toString());
					onFragmentDo(TYPE_TO_BOOK_CATEGORY_LIST, bundle);
				}
			});

			return convertView;
		}

		private class ViewHolder {
			public RelativeLayout rl_main;
			public TextView tv_main;
			public ImageView iv_main;

			public ListView lv_second;
		}
	}
}
