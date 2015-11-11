package com.wanpg.bookread.ui.book;

import java.util.ArrayList;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.api.ShuPengApi;
import com.wanpg.bookread.logic.BackTask;
import com.wanpg.bookread.manager.ShelfManager;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.widget.HeightBasedChildListView;

public class StoreCategoryInfoFragment extends BaseFragment {

	private final int LOAD_DATA_SUCCESS = 0;
	private final int LOAD_DATA_FAIL = 1;
	private final int START_LOAD_DATA = 2;

	private List<Map<String, Object>> listCategoryAll = null;
	private List<Map<String, Object>> listCategoryHead = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> listCategoryContent = new ArrayList<Map<String, Object>>();

	private List<LinearLayout> listHeadLayout = new ArrayList<LinearLayout>();

	private View v_main = null;

	private View v_load;

	private ScrollView sv_main;

	private LinearLayout ll_head;

	private HeightBasedChildListView lv_content;

	private int cId;
	private String cName;

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
			case START_LOAD_DATA:
				initData();
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
		Bundle bundle = getArguments();
		cId = bundle.getInt("cid");
		cName = bundle.getString("cname");
		//        ArrayList list = bundle.getParcelableArrayList("headlist");
		//        listCategoryHead = (List<Map<String, Object>>) list.get(0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(!resetView(v_main)){
			v_main = inflater.inflate(R.layout.fragment_store_category_info, null);
			v_main.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			((TextView) v_main.findViewById(R.id.tv_title)).setText(cName);
			setHeadBackEvent(v_main.findViewById(R.id.ib_back));

			sv_main = (ScrollView) v_main.findViewById(R.id.sv_main);
			v_load = v_main.findViewById(R.id.in_loading);
			v_load.setVisibility(View.VISIBLE);
			sv_main.setVisibility(View.GONE);
			//v_main.openFragment();
			handler.sendEmptyMessage(START_LOAD_DATA);
		}
		return v_main;
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
					List<Map<String, Object>> listNet = ShelfManager.getManager().mapCategory.get("网络文学");
					List<Map<String, Object>> listBook = ShelfManager.getManager().mapCategory.get("畅销书");

					for (Map<String, Object> map : listNet) {
						if (Integer.parseInt(map.get("cid").toString()) == cId) {
							listCategoryHead = (List<Map<String, Object>>) map.get("desc");
							break;
						}
					}

					for (Map<String, Object> map : listBook) {
						if (Integer.parseInt(map.get("cid").toString()) == cId) {
							listCategoryHead = (List<Map<String, Object>>) map.get("desc");
							break;
						}
					}

					listCategoryAll = ShuPengApi.getCategoryFirstList(cId);
					if (listCategoryAll == null) {
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handler.sendEmptyMessage(LOAD_DATA_FAIL);
					} else {
						for (Map<String, Object> map : listCategoryAll) {
							listCategoryContent.add(map);
							int scid_01 = Integer.parseInt(map.get("scid").toString());
							for (Map<String, Object> map1 : listCategoryHead) {
								int scid_02 = Integer.parseInt(map1.get("scid").toString());
								if (scid_01 == scid_02) {
									listCategoryContent.remove(map);
									break;
								}
							}
						}
						handler.sendEmptyMessage(LOAD_DATA_SUCCESS);
					}
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
		initHead();
		lv_content = (HeightBasedChildListView) v_main.findViewById(R.id.lv_content);
		MyAdapter adapter = new MyAdapter(listCategoryContent);
		lv_content.setAdapter(adapter);
	}

	private void initHead() {
		ll_head = (LinearLayout) v_main.findViewById(R.id.ll_head);
		int headSize = listCategoryHead.size();
		int rowCount = headSize / 4;
		int columnCount = headSize % 4;
		for (int i = 0; i < rowCount; i++) {
			LinearLayout ll = new LinearLayout(mActivity);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, DisplayUtil.dp2px(35));
			params.setMargins(0, 1, 0, 0);
			ll.setLayoutParams(params);
			listHeadLayout.add(ll);
			ll_head.addView(ll);
		}

		for (int i = 0; i < headSize; i++) {
			int curRow = i / 4;
			int curCol = i % 4;
			TextView tv = new TextView(mActivity);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
			if (curCol == 3) {
				params.setMargins(0, 0, 0, 0);
			} else {
				params.setMargins(0, 0, 1, 0);
			}
			params.weight = 1;
			tv.setLayoutParams(params);

			final int scid = Integer.parseInt(listCategoryHead.get(i).get("scid").toString());
			final String scname = listCategoryHead.get(i).get("name").toString();

			tv.setText(scname);
			tv.setGravity(Gravity.CENTER);
			tv.setBackgroundResource(R.drawable.white_orange_selector);
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Bundle bundle = new Bundle();
					bundle.putInt("cid", cId);
					bundle.putInt("scid", scid);
					bundle.putString("scname", scname);
					onFragmentDo(TYPE_TO_BOOK_CATEGORY_LIST, bundle);
				}
			});
			listHeadLayout.get(curRow).addView(tv);
		}
	}

	private class MyAdapter extends BaseAdapter {

		private List<Map<String, Object>> listItems;
		private ViewHolder holder;

		public MyAdapter(List<Map<String, Object>> listItems) {
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
				view = LayoutInflater.from(mActivity).inflate(R.layout.store_first_category_info_cell, null);
				holder.tv_scname = (TextView) view.findViewById(R.id.tv_scname);
				holder.ll_more = (LinearLayout) view.findViewById(R.id.ll_more);
				holder.hbclv_scategory = (HeightBasedChildListView) view.findViewById(R.id.hbclv_scategory);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final int scid = Integer.parseInt(listItems.get(i).get("scid").toString());
			final String scname = listItems.get(i).get("name").toString();
			holder.tv_scname.setText(scname);
			final List<Map<String, Object>> listTmp = (List<Map<String, Object>>) listItems.get(i).get("booklist");
			//            BookListAdapter adapter = new BookListAdapter(context,(List<Map<String, Object>>)listItems.get(i).get("booklist"),holder.hbclv_scategory);
			//            holder.hbclv_scategory.setAdapter(adapter);

			SimpleAdapter simpleAdapter = new SimpleAdapter(mActivity, listTmp, R.layout.store_category_info_book_list_cell, new String[]{"bookname"}, new int[]{R.id.tv_name});
			holder.hbclv_scategory.setAdapter(simpleAdapter);


			holder.hbclv_scategory.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Bundle bundle = new Bundle();
					bundle.putInt("bookId", Integer.parseInt(listTmp.get(arg2).get("bookid").toString()));
					onFragmentDo(TYPE_TO_BOOK_DETAIL, bundle);
				}
			});

			holder.ll_more.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Bundle bundle = new Bundle();
					bundle.putInt("cid", cId);
					bundle.putInt("scid", scid);
					bundle.putString("scname", scname);
					onFragmentDo(TYPE_TO_BOOK_CATEGORY_LIST, bundle);
				}
			});
			return view;
		}

		private class ViewHolder {
			public TextView tv_scname;
			public LinearLayout ll_more;
			public HeightBasedChildListView hbclv_scategory;
		}
	}
}
