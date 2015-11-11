package com.wanpg.bookread.ui.hall;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.common.PhoneInfo;
import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.db.DaoManager;
import com.wanpg.bookread.logic.FileParseHelper;
import com.wanpg.bookread.slidemenu.SlidingMenu;
import com.wanpg.bookread.ui.adapter.MainFragmentAdapter;
import com.wanpg.bookread.ui.software.SoftwareFragment;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.widget.Notice;
import com.wanpg.bookread.widget.ViewPager;
import com.wanpg.bookread.widget.ViewPager.OnPageChangeListener;

public class MainFragment extends BaseFragment{

	private View mMainView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(!resetView(mMainView)){
			mMainView = inflater.inflate(R.layout.main_fragment, null);
			initUI();
		}
		return mMainView;
	}
	private ViewPager vp_main;
	private TextView tv_head_shelf, tv_head_store, tv_head_search, tv_head_recommend;

	private ImageView ivBottomLine;
	private int bottomLineWidth;
	private int offset = 0;
	private int position_one;
	private int position_two;
	private int position_three;
	private int currIndex = 0;

	private MainFragmentAdapter mMainFragmentAdapter;
	private ShelfFragment mShelfFragment;
	private StoreFragment mStoreFragment;
	private SearchFragment mSearchFragment;
	private SoftwareFragment mSoftwareFragment;

	/**
	 * 初始化界面
	 */
	private void initUI() {

		//初始化head菜单栏参数
		tv_head_shelf = (TextView) mMainView.findViewById(R.id.tv_head_shelf);
		tv_head_store = (TextView) mMainView.findViewById(R.id.tv_head_store);
		tv_head_search = (TextView) mMainView.findViewById(R.id.tv_head_search);
		tv_head_recommend = (TextView) mMainView.findViewById(R.id.tv_head_recommend);

		tv_head_shelf.setOnClickListener(mHeadMenuClickListener);
		tv_head_store.setOnClickListener(mHeadMenuClickListener);
		tv_head_search.setOnClickListener(mHeadMenuClickListener);
		tv_head_recommend.setOnClickListener(mHeadMenuClickListener);

		ivBottomLine = (ImageView) mMainView.findViewById(R.id.iv_bottom_line);
		bottomLineWidth = ivBottomLine.getLayoutParams().width;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(bottomLineWidth, DisplayUtil.dp2px(4));
		params.setMargins(((PhoneInfo.disWidthPx / 4 - bottomLineWidth) / 2), 0, 0, 0);
		ivBottomLine.setLayoutParams(params);

		offset = (int) ((PhoneInfo.disWidthPx / 4.0 - bottomLineWidth) / 2);
		Log.i("MainActivity", "offset=" + offset);

		position_one = (int) (PhoneInfo.disWidthPx / 4.0);
		position_two = position_one * 2;
		position_three = position_one * 3;


		//初始化viewPager参数
		vp_main = (ViewPager) mMainView.findViewById(R.id.vp_main);

		mShelfFragment = new ShelfFragment();
		mStoreFragment = new StoreFragment();
		mSearchFragment = new SearchFragment();
		mSoftwareFragment = new SoftwareFragment();
		mShelfFragment.setOnFragmentDoListener(mChildFragmentDoListener);
		mStoreFragment.setOnFragmentDoListener(mChildFragmentDoListener);
		mSearchFragment.setOnFragmentDoListener(mChildFragmentDoListener);
		mSoftwareFragment.setOnFragmentDoListener(mChildFragmentDoListener);
		ArrayList<BaseFragment> list = new ArrayList<BaseFragment>();
		list.add(mShelfFragment);
		list.add(mStoreFragment);
		list.add(mSearchFragment);
		list.add(mSoftwareFragment);


		mMainFragmentAdapter = new MainFragmentAdapter(getFragmentManager(), list);
		vp_main.setOffscreenPageLimit(0);
		vp_main.setAdapter(mMainFragmentAdapter);

		vp_main.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				initChildView(arg0);
				switch (arg0) {
				case 3:
					onFragmentDo(TYPE_TO_CHANGE_SLIDEMODE, SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					onFragmentDo(TYPE_TO_CHANGE_SLIDEMODE, SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
				if(arg0==1){
					
				}else{
					
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stu
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		vp_main.setCurrentItem(0);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		currIndex = 0;
		initChildView(vp_main.getCurrentItem());
	}

	private void initChildView(int pos) {
		// TODO Auto-generated method stub
		Animation animation = null;
		switch (pos) {
		case 0:
			if (currIndex == 1) {
				animation = new TranslateAnimation(position_one, 0, 0, 0);
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(position_two, 0, 0, 0);
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(position_three, 0, 0, 0);
			}
			break;
		case 1:
			if (currIndex == 0) {
				animation = new TranslateAnimation(offset, position_one, 0, 0);
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(position_two, position_one, 0, 0);
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(position_three, position_one, 0, 0);
			}
			break;
		case 2:
			if (currIndex == 0) {
				animation = new TranslateAnimation(offset, position_two, 0, 0);
			} else if (currIndex == 1) {
				animation = new TranslateAnimation(position_one, position_two, 0, 0);
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(position_three, position_two, 0, 0);
			}
			break;
		case 3:
			if (currIndex == 0) {
				animation = new TranslateAnimation(offset, position_three, 0, 0);
			} else if (currIndex == 1) {
				animation = new TranslateAnimation(position_one, position_three, 0, 0);
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(position_two, position_three, 0, 0);
			}
			break;
		}
		currIndex = pos;
		if(animation == null){
			ivBottomLine.clearAnimation();
		}else{
			animation.setFillAfter(true);
			animation.setDuration(300);
			ivBottomLine.startAnimation(animation);
		}

	}
	
	private OnFragmentDoListener mChildFragmentDoListener = new OnFragmentDoListener() {
		
		@Override
		public void onFragmentDo(int type, Object object) {
			// TODO Auto-generated method stub
			MainFragment.this.onFragmentDo(type, object);
		}
	};

	private OnClickListener mHeadMenuClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.equals(tv_head_shelf)) {
				vp_main.setCurrentItem(0, true);
			} else if (v.equals(tv_head_store)) {
				vp_main.setCurrentItem(1, true);
			} else if (v.equals(tv_head_search)) {
				vp_main.setCurrentItem(2, true);
			} else if (v.equals(tv_head_recommend)) {
				vp_main.setCurrentItem(3, true);
			}

		}
	};

	private void openOneSDBook(String path) {
		// TODO Auto-generated method stub
		try {
			ShelfBook bookMode = FileParseHelper.getShelfBookByPath(path);
			if (bookMode == null) {
				Notice.showToast("打开文件的内容为空！");
			} else {
				if(mActivity.openBook(bookMode))
					DaoManager.getInstance().getShelfDao().saveOrUpdateOneBook(bookMode);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 1) {
			//进行书架的操作
			openOneSDBook(data.getStringExtra("choosePath"));
		}
	}
}
