package com.wanpg.bookread.ui.book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.api.ShuPengApi;
import com.wanpg.bookread.logic.BackTask;
import com.wanpg.bookread.ui.adapter.BookListAdapter;
import com.wanpg.bookread.utils.DisplayUtil;

public class StoreBookRankListFragment extends BaseFragment {
	private static final int LOAD_DATA_SUCCESS = 0;
	private static final int LOAD_DATA_FAIL = 1;

	private View v_main = null;

	private View v_load;

	private List<Map<String, Object>> listHead;
	private List<Button> listHeadButton = new ArrayList<Button>();
	private String topName;

	private ListView lv_store_rank_list;

	private LinearLayout ll_rank_head;

	private Map<String, BookListAdapter> mapListAdapter = new HashMap<String, BookListAdapter>();

	private int curPageIndex = 0;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case LOAD_DATA_SUCCESS:
				v_load.setVisibility(View.GONE);
				lv_store_rank_list.setVisibility(View.VISIBLE);
				initPage(Integer.parseInt(msg.obj.toString()));
				break;
			case LOAD_DATA_FAIL:
				v_load.findViewById(R.id.loading).setVisibility(View.GONE);
				v_load.findViewById(R.id.load_fail).setVisibility(View.VISIBLE);
				v_load.findViewById(R.id.load_fail_button).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						initPageData(curPageIndex);
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
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		topName = bundle.getString("topname");
		ArrayList list = bundle.getParcelableArrayList("toplist");
		listHead = (List<Map<String, Object>>) list.get(0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(!resetView(v_main)){
			v_main = inflater.inflate(R.layout.fragment_store_rank_list, null);
			v_main.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			((TextView) v_main.findViewById(R.id.tv_title)).setText(topName + "排行榜");
			setHeadBackEvent(v_main.findViewById(R.id.ib_back));
			lv_store_rank_list = (ListView) v_main.findViewById(R.id.lv_store_rank_list);
			v_load = v_main.findViewById(R.id.in_loading);
			v_load.setVisibility(View.VISIBLE);
			lv_store_rank_list.setVisibility(View.GONE);
			initUI();
		}
		return v_main;
	}


	private void initUI() {
		// TODO Auto-generated method stub
		initHead();
		initPageData(curPageIndex);
	}


	private void initHead() {
		ll_rank_head = (LinearLayout) v_main.findViewById(R.id.ll_store_rank_head);
		for (int i = 0; i < listHead.size(); i++) {


			ImageView iv = new ImageView(getActivity());
			iv.setImageResource(R.drawable.subtab_divider);
			iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT));

			Button bt = new Button(getActivity());
			bt.setText(listHead.get(i).get("name").toString());
			bt.setGravity(Gravity.CENTER);
			bt.setTextSize(13);
			bt.setTextColor(getResources().getColor(R.color.black));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dp2px(70), DisplayUtil.dp2px(35));
			bt.setLayoutParams(params);
			if (i == 0) {
				bt.setBackgroundResource(R.drawable.subtab_item_bg_selected_left);
				ll_rank_head.addView(bt);
				ll_rank_head.addView(iv);
			} else if (i == listHead.size() - 1) {
				bt.setBackgroundResource(R.drawable.subtab_right_selector);
				ll_rank_head.addView(bt);
			} else {
				bt.setBackgroundResource(R.drawable.subtab_center_selector);
				ll_rank_head.addView(bt);
				ll_rank_head.addView(iv);
			}
			bt.setOnClickListener(new MyHeadButtonClickListener());
			listHeadButton.add(bt);
		}
	}


	private class MyHeadButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			for (int i = 0; i < listHeadButton.size(); i++) {
				Button vb = listHeadButton.get(i);
				if (i == 0) {
					vb.setBackgroundResource(R.drawable.subtab_left_selector);
				} else if (i == listHeadButton.size() - 1) {
					vb.setBackgroundResource(R.drawable.subtab_right_selector);
				} else {
					vb.setBackgroundResource(R.drawable.subtab_center_selector);
				}
				if (v.equals(vb)) {
					if (i == 0) {
						vb.setBackgroundResource(R.drawable.subtab_item_bg_selected_left);
					} else if (i == listHeadButton.size() - 1) {
						vb.setBackgroundResource(R.drawable.subtab_item_bg_selected_right);
					} else {
						vb.setBackgroundResource(R.drawable.subtab_item_bg_selected_center);
					}
					//在此进行当前排行榜的信息
					curPageIndex = i;
					initPageData(curPageIndex);
				}
			}
		}
	}

	private void initPageData(final int i) {

		new BackTask() {

			@Override
			public void doBeforeThread() {
				// TODO Auto-generated method stub
				v_load.setVisibility(View.VISIBLE);
				lv_store_rank_list.setVisibility(View.GONE);
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
				BookListAdapter adapter;
				String name = listHeadButton.get(i).getText().toString();
				if (mapListAdapter.containsKey(name)) {
					adapter = mapListAdapter.get(name);
				} else {
					List<Map<String, Object>> list = ShuPengApi.getTopListInfo(Integer.parseInt(listHead.get(i).get("id").toString()), 1, 30);
					if (list == null) {
						handler.sendEmptyMessage(LOAD_DATA_FAIL);
						return null;
					}
					adapter = new BookListAdapter(StoreBookRankListFragment.this, list);
					mapListAdapter.put(name, adapter);
				}
				Message msg = handler.obtainMessage(LOAD_DATA_SUCCESS, i);
				handler.sendMessage(msg);
				return null;
			}
		}.submit();


	}

	private void initPage(int i) {
		if (i == curPageIndex) {
			lv_store_rank_list = (ListView) v_main.findViewById(R.id.lv_store_rank_list);
			BookListAdapter adapter = mapListAdapter.get(listHeadButton.get(i).getText().toString());
			lv_store_rank_list.setAdapter(adapter);
		}
	}


}
