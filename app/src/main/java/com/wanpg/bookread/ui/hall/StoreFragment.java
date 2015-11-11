package com.wanpg.bookread.ui.hall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.api.ShuPengApi;
import com.wanpg.bookread.common.ShuPengConfig;
import com.wanpg.bookread.data.BookBoard;
import com.wanpg.bookread.logic.BackTask;
import com.wanpg.bookread.ui.MainActivity;
import com.wanpg.bookread.ui.adapter.BookNewUpdateListAdapter;
import com.wanpg.bookread.ui.book.BookNewUpdateFragment;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.widget.AsyncImageView;
import com.wanpg.bookread.widget.ChildViewPager;
import com.wanpg.bookread.widget.ChildViewPager.OnSingleTouchListener;
import com.wanpg.bookread.widget.HeightBasedChildListView;
import com.wanpg.bookread.widget.SwitchAdBarView;
import com.wanpg.bookread.widget.SwitchAdBarView.OnCheckedPageListener;

/**
 * 书城页面
 *
 * @author Jinpeng
 */
public class StoreFragment extends BaseFragment {

	private List<BookBoard> listBoard = new ArrayList<BookBoard>();

	private List<Map<String, Object>> listBook_bzrm, listBook_jdqb,
	listBook_dsyq, listBook_xhqh, listBook_wxxx, listBook_jszz,
	listBook_yxjj, listBook_kbxy, listBook_zxgx;

	private Handler handler;

	private static final int DATA_LOAD_COMPLETE_NOTIFY = 1;
	private static final int DATA_LOAD_FAIL_NOTIFY = 2;
	private final int HOT_BOOK_VP_CHANGE_NOTIFY = 3;
	private final int LOADING_UI_NOTIFY = 4;
	//private HeightBasedChildGridView cgv_store;

	private HeightBasedChildListView hbclv_zxgx;
	private LinearLayout ll_zxgx;

	private LinearLayout ll_girl, ll_good, ll_rank, ll_sort;

	private ListView lv_bookstore_main;
	private List<List<Map<String, Object>>> listBookStore = new ArrayList<List<Map<String, Object>>>();
	private List<Map<String, Object>> listHotBook;

	private SwitchAdBarView adBarView;
	private ChildViewPager cvp_bookstore_hot;
	private int hotCurIndex = 0;
	private List<View> hotViewList = new ArrayList<View>();

	private View v_load;

	private MainActivity activity;
	private View parent;

	private boolean b01, b02;

	private boolean isInitUI = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case DATA_LOAD_COMPLETE_NOTIFY:
					if (!isInitUI) {
						initUI();
					}
					break;
				case DATA_LOAD_FAIL_NOTIFY:
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
				case HOT_BOOK_VP_CHANGE_NOTIFY:
					hotCurIndex++;
					if (hotCurIndex >= listHotBook.size()) {
						hotCurIndex = 0;
					}
					cvp_bookstore_hot.setCurrentItem(hotCurIndex);
					break;
				case LOADING_UI_NOTIFY:
					v_load.findViewById(R.id.loading).setVisibility(View.VISIBLE);
					v_load.findViewById(R.id.load_fail).setVisibility(View.GONE);
					break;
				default:
					break;
				}
			}
		};

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(!resetView(parent)){
			activity = (MainActivity) getActivity();

			parent = inflater.inflate(R.layout.layout_bookstore, null);

			v_load = parent.findViewById(R.id.in_loading);
			lv_bookstore_main = (ListView) parent.findViewById(R.id.lv_bookstore_main);

			lv_bookstore_main.setVisibility(View.GONE);
			v_load.setVisibility(View.VISIBLE);
			initData();
		}
		return parent;
	}

	private void initData() {
		// TODO Auto-generated method stub
		b01 = false;
		b02 = false;
		isInitUI = false;
		handler.sendEmptyMessage(LOADING_UI_NOTIFY);

		new BackTask() {

			@Override
			public void doInThreadWhenCalling(String info) {
				// TODO Auto-generated method stub

			}

			@Override
			public String doInThread() {
				// TODO Auto-generated method stub
				listBoard = ShuPengApi.getRecommendList();
				//畅销书
				listBook_bzrm = ShuPengApi.getHotBooks(1, 3);
				//经典全本 cid 35  getNetnovelListByCid
				listBook_jdqb = ShuPengApi.getNetNovel(35, 1, 3);
				//都市言情  cid 31
				listBook_dsyq = ShuPengApi.getNetNovel(31, 1, 3);
				//恐怖悬疑 cid 76  sid
				listBook_kbxy = ShuPengApi.getSecondCategoryBookList(1, 3, 76, 87);
				//玄幻奇幻   cid  29
				listBook_xhqh = ShuPengApi.getNetNovel(29, 1, 3);


				if (listBoard != null && listBook_bzrm != null && listBook_jdqb != null && listBook_dsyq != null && listBook_kbxy != null && listBook_xhqh != null) {
					b01 = true;
					return "SUCCESS";
				} else {
					return "FAIL";
				}
			}

			@Override
			public void doBeforeThread() {
				// TODO Auto-generated method stub
			}

			@Override
			public void doAfterThread(String result) {
				// TODO Auto-generated method stub
				if (result.equals("SUCCESS")) {
					handler.sendEmptyMessage(DATA_LOAD_COMPLETE_NOTIFY);
				} else if (result.equals("FAIL")) {
					handler.sendEmptyMessage(DATA_LOAD_FAIL_NOTIFY);
				}
			}
		}.submit();

		new BackTask() {

			@Override
			public void doInThreadWhenCalling(String info) {
				// TODO Auto-generated method stub

			}

			@Override
			public String doInThread() {
				// TODO Auto-generated method stub
				//武侠仙侠  cid 30
				listBook_wxxx = ShuPengApi.getNetNovel(30, 1, 3);
				//军事战争  cid 32
				listBook_jszz = ShuPengApi.getSecondCategoryBookList(1, 3, 221, 222);
				//游戏竞技 一级分类  cid 33
				listBook_yxjj = ShuPengApi.getNetNovel(33, 1, 3);

				listHotBook = ShuPengApi.getNetNovel(28, 1, 5);

				listBook_zxgx = ShuPengApi.getUpdateNetnovel(1, 5);

				if (listBook_wxxx != null && listBook_jszz != null && listBook_yxjj != null && listHotBook != null && listBook_zxgx != null) {
					b02 = true;
					return "SUCCESS";
				} else {
					return "FAIL";
				}
			}

			@Override
			public void doBeforeThread() {
				// TODO Auto-generated method stub
			}

			@Override
			public void doAfterThread(String result) {
				// TODO Auto-generated method stub
				if (result.equals("SUCCESS")) {
					handler.sendEmptyMessage(DATA_LOAD_COMPLETE_NOTIFY);
				} else if (result.equals("FAIL")) {
					handler.sendEmptyMessage(DATA_LOAD_FAIL_NOTIFY);
				}
			}
		}.submit();

	}


	private void initUI() {
		// TODO Auto-generated method stub
		if (!b01 || !b02) {
			return;
		}
		lv_bookstore_main.setVisibility(View.VISIBLE);
		v_load.setVisibility(View.GONE);

		isInitUI = true;

		/** 初始化head开始 **/
		View head = LayoutInflater.from(activity).inflate(R.layout.layout_bookstore_head, null);

		adBarView = (SwitchAdBarView) head.findViewById(R.id.sav_switchbar);
		int disWidth = DisplayUtil.getDisWidth();
		int height = disWidth * 230 / 640;
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		adBarView.setLayoutParams(params);
		adBarView.setActivity(activity);
		//cgv_store = (HeightBasedChildGridView) parent.findViewById(R.id.cgv_store);
		adBarView.setListItems(listBoard);

		cvp_bookstore_hot = (ChildViewPager) head.findViewById(R.id.cvp_bookstore_hot);
		for (int i = 0; i < listHotBook.size(); i++) {
			View v = LayoutInflater.from(activity).inflate(R.layout.store_hot_book_cell, null);
			hotViewList.add(v);
		}
		MyViewPagerAdapter pagerAdapter = new MyViewPagerAdapter(hotViewList);
		cvp_bookstore_hot.setAdapter(pagerAdapter);
		cvp_bookstore_hot.setCurrentItem(hotCurIndex);
		handler.sendEmptyMessageDelayed(HOT_BOOK_VP_CHANGE_NOTIFY, 3000);

		adBarView.setOnCheckedPageListener(new OnCheckedPageListener() {

			@Override
			public void onChecked(int pos) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("BOARD_NAME", listBoard.get(pos).name);
				bundle.putInt("BOARD_ID", listBoard.get(pos).id);
				bundle.putString("BOARD_BANNER", listBoard.get(pos).banner);
				onFragmentDo(TYPE_TO_STORE_BOARD, bundle);
			}
		});

		cvp_bookstore_hot.setOnSingleTouchListener(new OnSingleTouchListener() {

			@Override
			public void onSingleTouch() {
				// TODO Auto-generated method stub
				int pos = cvp_bookstore_hot.getCurrentItem();
				Bundle bundle = new Bundle();
				bundle.putInt("bookId", Integer.parseInt(listHotBook.get(pos).get("bookId").toString()));
				onFragmentDo(TYPE_TO_BOOK_DETAIL, bundle);
			}
		});
		cvp_bookstore_hot.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				handler.removeMessages(HOT_BOOK_VP_CHANGE_NOTIFY);
				handler.sendEmptyMessageDelayed(HOT_BOOK_VP_CHANGE_NOTIFY, 4000);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		ll_girl = (LinearLayout) head.findViewById(R.id.ll_girl);
		ll_good = (LinearLayout) head.findViewById(R.id.ll_good);
		ll_rank = (LinearLayout) head.findViewById(R.id.ll_rank);
		ll_sort = (LinearLayout) head.findViewById(R.id.ll_sort);

		ll_girl.setOnClickListener(new MyOnClickListener());
		ll_good.setOnClickListener(new MyOnClickListener());
		ll_rank.setOnClickListener(new MyOnClickListener());
		ll_sort.setOnClickListener(new MyOnClickListener());

		lv_bookstore_main.addHeaderView(head);
		/** 初始化head结束 **/

		/** 初始化foot开始 **/
		View foot = LayoutInflater.from(activity).inflate(R.layout.layout_bookstore_foot, null);
		hbclv_zxgx = (HeightBasedChildListView) foot.findViewById(R.id.hbclv_zxgx);
		ll_zxgx = (LinearLayout) foot.findViewById(R.id.ll_zxgx);
		BookNewUpdateListAdapter zxgx_adapter = new BookNewUpdateListAdapter(activity, listBook_zxgx);
		hbclv_zxgx.setAdapter(zxgx_adapter);
		//更多
		ll_zxgx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BookNewUpdateFragment fragment = new BookNewUpdateFragment();
				mActivity.mFragmentManager.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_right,
						R.anim.slide_in_from_right, R.anim.slide_out_from_right)
						.add(R.id.fragment_container, fragment)
						.addToBackStack(null)
						.commit();
			}
		});
		lv_bookstore_main.addFooterView(foot);
		/** 初始化foot结束 **/


		/** 初始化正常部分 **/
		listBookStore.add(listBook_bzrm);
		listBookStore.add(listBook_jdqb);
		listBookStore.add(listBook_dsyq);
		listBookStore.add(listBook_kbxy);
		listBookStore.add(listBook_xhqh);
		listBookStore.add(listBook_wxxx);
		listBookStore.add(listBook_jszz);
		listBookStore.add(listBook_yxjj);

		lv_bookstore_main.setAdapter(new BookStoreListAdapter());
	}

	private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.equals(ll_girl)) {
				Bundle bundle = new Bundle();
				bundle.putInt("cid", 110);
				bundle.putString("cname", "女生频道");
				onFragmentDo(TYPE_TO_BOOK_CATEGORY_INFO, bundle);
			} else if (v.equals(ll_good)) {
				Bundle bundle = new Bundle();
				bundle.putInt("cid", 176);
				bundle.putInt("scid", 185);
				bundle.putString("scname", "情色专区");
				onFragmentDo(TYPE_TO_BOOK_CATEGORY_LIST, bundle);
			} else if (v.equals(ll_rank)) {
				onFragmentDo(TYPE_TO_BOOK_RANK, null);
			} else if (v.equals(ll_sort)) {
				onFragmentDo(TYPE_TO_BOOK_CATEGORY, null);
			}
		}
	}

	private class BookStoreListAdapter extends BaseAdapter {

		ViewHolder holder;
		String[] sTitles = new String[]{
				"本周热门", "经典全本", "都市言情", "恐怖悬疑", "玄幻奇幻", "武侠仙侠", "军事战争", "游戏竞技"
		};

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listBookStore.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listBookStore.get(position);
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
				convertView = LayoutInflater.from(activity).inflate(R.layout.layout_bookstore_list_cell, null);
				holder = new ViewHolder();
				holder.tv_title = (TextView) convertView.findViewById(R.id.cell_title);
				holder.ll_more = (LinearLayout) convertView.findViewById(R.id.ll_more);
				holder.rl_01 = (RelativeLayout) convertView.findViewById(R.id.cell_01);
				holder.rl_02 = (RelativeLayout) convertView.findViewById(R.id.cell_02);
				holder.rl_03 = (RelativeLayout) convertView.findViewById(R.id.cell_03);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final String title = sTitles[position];
			holder.tv_title.setText(title);
			initOneBook(holder.rl_01, listBookStore.get(position).get(0));
			initOneBook(holder.rl_02, listBookStore.get(position).get(1));
			initOneBook(holder.rl_03, listBookStore.get(position).get(2));

			holder.ll_more.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (title.equals("本周热门")) {
						//本周热门
						onFragmentDo(TYPE_TO_BOOK_HOT, null);
					} else if (title.equals("经典全本")) {
						//经典全本
						Bundle bundle = new Bundle();
						bundle.putInt("cid", 35);
						bundle.putString("cname", "经典全本");
						onFragmentDo(TYPE_TO_BOOK_NET_NOVEL, bundle);
					} else if (title.equals("都市言情")) {
						//都市言情
						Bundle bundle = new Bundle();
						bundle.putInt("cid", 31);
						bundle.putString("cname", "都市言情");
						onFragmentDo(TYPE_TO_BOOK_NET_NOVEL, bundle);
					} else if (title.equals("恐怖悬疑")) {
						Bundle bundle = new Bundle();
						bundle.putInt("cid", 76);
						bundle.putInt("scid", 87);
						bundle.putString("scname", "恐怖悬疑");
						onFragmentDo(TYPE_TO_BOOK_CATEGORY_LIST, bundle);
					} else if (title.equals("玄幻奇幻")) {
						//玄幻奇幻
						Bundle bundle = new Bundle();
						bundle.putInt("cid", 29);
						bundle.putString("cname", "玄幻奇幻");
						onFragmentDo(TYPE_TO_BOOK_NET_NOVEL, bundle);
					} else if (title.equals("武侠仙侠")) {
						//武侠仙侠
						Bundle bundle = new Bundle();
						bundle.putInt("cid", 30);
						bundle.putString("cname", "武侠仙侠");
						onFragmentDo(TYPE_TO_BOOK_NET_NOVEL, bundle);
					} else if (title.equals("军事战争")) {
						//军事战争
						Bundle bundle = new Bundle();
						bundle.putInt("cid", 221);
						bundle.putInt("scid", 222);
						bundle.putString("scname", "军事战争");
						onFragmentDo(TYPE_TO_BOOK_CATEGORY_LIST, bundle);
					} else if (title.equals("游戏竞技")) {
						//游戏竞技
						Bundle bundle = new Bundle();
						bundle.putInt("cid", 33);
						bundle.putString("cname", "游戏竞技");
						onFragmentDo(TYPE_TO_BOOK_NET_NOVEL, bundle);
					}
				}
			});


			return convertView;
		}

		private void initOneBook(RelativeLayout rl, final Map<String, Object> data) {
			RelativeLayout main = (RelativeLayout) rl.findViewById(R.id.rl_bookstore_cell_main);
			AsyncImageView cover = (AsyncImageView) rl.findViewById(R.id.iv_bookstore_recommend_cell);
			TextView name = (TextView) rl.findViewById(R.id.tv_bookstore_recommend_cell_title);
			TextView intro = (TextView) rl.findViewById(R.id.tv_bookstore_recommend_cell_content);
			name.setText(data.get("name").toString());
			intro.setText(data.get("intro").toString());
			String imageHref = ShuPengConfig.BOOK_COVER_MAIN_B + data.get("thumb").toString();

			cover.setImageUrl(imageHref, R.drawable.ic_launcher, DisplayUtil.dp2px(40), DisplayUtil.dp2px(56));
			main.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Bundle bundle = new Bundle();
					bundle.putInt("bookId", Integer.parseInt(data.get("bookId").toString()));
					onFragmentDo(TYPE_TO_BOOK_DETAIL, bundle);
				}
			});
		}

		private class ViewHolder {
			public LinearLayout ll_more;
			public TextView tv_title;
			public RelativeLayout rl_01, rl_02, rl_03;
		}
	}


	private class MyViewPagerAdapter extends PagerAdapter {

		private List<View> viewList;

		public MyViewPagerAdapter(List<View> viewList) {
			this.viewList = viewList;
		}

		//销毁position位置的界面
		@Override
		public void destroyItem(View v, int position, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) v).removeView(viewList.get(position));

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		//获取当前窗体界面数
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return viewList.size();
		}

		//初始化position位置的界面
		@Override
		public Object instantiateItem(View v, int position) {
			// TODO Auto-generated method stub
			final int x = position;
			if (x >= listHotBook.size()) {
				return null;
			}

			RelativeLayout rl_bookstore_hot_main = (RelativeLayout) viewList.get(x).findViewById(R.id.rl_bookstore_hot_main);
			AsyncImageView iv_bookstore_hot_cover = (AsyncImageView) viewList.get(x).findViewById(R.id.iv_bookstore_hot_cover);
			TextView tv_bookstore_hot_title = (TextView) viewList.get(x).findViewById(R.id.tv_bookstore_hot_title);
			TextView tv_bookstore_hot_author = (TextView) viewList.get(x).findViewById(R.id.tv_bookstore_hot_author);
			TextView tv_bookstore_hot_info = (TextView) viewList.get(x).findViewById(R.id.tv_bookstore_hot_info);

			//热门图书部分
			String imageHref = ShuPengConfig.BOOK_COVER_MAIN_B + listHotBook.get(x).get("thumb").toString();
			iv_bookstore_hot_cover.setImageUrl(imageHref, R.drawable.ic_launcher, DisplayUtil.dp2px(45), DisplayUtil.dp2px(63));

			tv_bookstore_hot_title.setText(listHotBook.get(x).get("name").toString());
			tv_bookstore_hot_author.setText("作者:" + listHotBook.get(x).get("author").toString());
			tv_bookstore_hot_info.setText(listHotBook.get(x).get("intro").toString());

			rl_bookstore_hot_main.setOnClickListener(new MyOnClickListener());


			((ViewPager) v).addView(viewList.get(x));
			return viewList.get(x);
		}

		// 判断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View v, Object arg1) {
			// TODO Auto-generated method stub
			return v == arg1;
		}


		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
