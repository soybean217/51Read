package com.wanpg.bookread.ui.book;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.api.ShuPengApi;
import com.wanpg.bookread.logic.BackTask;
import com.wanpg.bookread.widget.HeightBasedChildListView;

public class StoreBookRankFragment extends BaseFragment {

	private final int LOAD_DATA_SUCCESS = 0;
	private final int LOAD_DATA_FAIL = 1;

	private HeightBasedChildListView hbclv_cxs, hbclv_wlwx;

	List<Map<String, Object>> listRanks;

	List<Map<String, Object>> listNet = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> listBook = new ArrayList<Map<String, Object>>();

	private View v_main = null;
	private ScrollView sv_main;
	private View v_load;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case LOAD_DATA_SUCCESS:
				v_load.setVisibility(View.GONE);
				sv_main.setVisibility(View.VISIBLE);
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
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(!resetView(v_main)){
			v_main = inflater.inflate(R.layout.fragment_store_rank, null);
			v_main.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			((TextView) v_main.findViewById(R.id.tv_title)).setText("排行榜");
			setHeadBackEvent(v_main.findViewById(R.id.ib_back));

			sv_main = (ScrollView) v_main.findViewById(R.id.sv_main);

			v_load = v_main.findViewById(R.id.in_loading);
			v_load.setVisibility(View.VISIBLE);
			sv_main.setVisibility(View.GONE);
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
				listRanks = ShuPengApi.getRankList();

				if (listRanks != null) {
					for (Map<String, Object> map : listRanks) {
						String channel = map.get("channel").toString();
						if (channel.equals("wb")) {
							listNet.add(map);
						} else if (channel.equals("eb")) {
							listBook.add(map);
						}
					}
					handler.sendEmptyMessage(LOAD_DATA_SUCCESS);
				} else {

					handler.sendEmptyMessage(LOAD_DATA_FAIL);
				}

				return null;
			}

		}.submit();

	}

	private void initUI() {
		// TODO Auto-generated method stub
		hbclv_cxs = (HeightBasedChildListView) v_main.findViewById(R.id.hbclv_cxs);
		hbclv_wlwx = (HeightBasedChildListView) v_main.findViewById(R.id.hbclv_wlwx);

		MyAdapter myAdapterBook = new MyAdapter(getActivity(), listBook);
		hbclv_cxs.setAdapter(myAdapterBook);

		MyAdapter myAdapterNet = new MyAdapter(getActivity(), listNet);
		hbclv_wlwx.setAdapter(myAdapterNet);

	}

	private class MyAdapter extends BaseAdapter {
		private Context context;
		private List<Map<String, Object>> listItems;
		private ViewHolder holder;

		public MyAdapter(Context context, List<Map<String, Object>> listItems) {
			this.context = context;
			this.listItems = listItems;
		}

		@Override
		public int getCount() {
			if (listItems == null) {
				return 0;
			} else {
				return listItems.size();
			}
		}

		@Override
		public Object getItem(int i) {
			if (listItems == null) {
				return null;
			} else {
				return listItems.get(i);
			}
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(context).inflate(R.layout.store_rank_list_cell, null);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_rank_name);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			final int x = i;
			final String name = listItems.get(x).get("topname").toString();
			holder.tv_name.setText(name);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Bundle bundle = new Bundle();
					bundle.putString("topname", name);
					ArrayList list = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
					list.add(listItems.get(x).get("toplist"));
					bundle.putParcelableArrayList("toplist", list);
					onFragmentDo(TYPE_TO_BOOK_RANK_LIST, bundle);
				}
			});
			return view;
		}

		private class ViewHolder {
			TextView tv_name;
		}
	}
}
