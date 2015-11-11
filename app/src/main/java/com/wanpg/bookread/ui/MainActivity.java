package com.wanpg.bookread.ui;

import java.util.Stack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.wanpg.bookread.BaseApplication;
import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.BaseFragment.OnFragmentDoListener;
import com.wanpg.bookread.R;
import com.wanpg.bookread.download.DownloadTask;
import com.wanpg.bookread.slidemenu.SlidingMenu;
import com.wanpg.bookread.slidemenu.app.SlidingFragmentActivity;
import com.wanpg.bookread.ui.book.BookCommentFragment;
import com.wanpg.bookread.ui.book.StoreBoardShowFragment;
import com.wanpg.bookread.ui.book.StoreBookRankFragment;
import com.wanpg.bookread.ui.book.StoreBookRankListFragment;
import com.wanpg.bookread.ui.book.StoreBookShowFragment;
import com.wanpg.bookread.ui.book.StoreCategoryFragment;
import com.wanpg.bookread.ui.book.StoreCategoryInfoFragment;
import com.wanpg.bookread.ui.book.StoreCategoryListFragment;
import com.wanpg.bookread.ui.book.StoreHotBookFragment;
import com.wanpg.bookread.ui.book.StoreNetNovelListFragment;
import com.wanpg.bookread.ui.book.StoreSearchResultFragment;
import com.wanpg.bookread.ui.hall.ListMenuFragment;
import com.wanpg.bookread.ui.hall.MainFragment;
import com.wanpg.bookread.ui.software.SoftClassifyListFragment;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.utils.TextUtil;
import com.wanpg.bookread.widget.Notice;
import com.wanpg.bookread.widget.ScrollFragmentLayout;

/**
 * 主界面
 *
 * @author Jinpeng
 */
public class MainActivity extends SlidingFragmentActivity {

	public boolean isFragmentOpen = false;

	private ListMenuFragment mMenuFragment;
	private SlidingMenu mSlidingMenu;
	private ScrollFragmentLayout mScrollFragmentLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initSlideMenu(savedInstanceState);
		setContentView(R.layout.activity_new_test);
		initUI();
		showBookMain();
	}

	private void initSlideMenu(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = mFragmentManager.beginTransaction();
			mMenuFragment = new ListMenuFragment();
			t.replace(R.id.menu_frame, mMenuFragment);
			t.commit();
		} else {
			mMenuFragment = (ListMenuFragment)mFragmentManager.findFragmentById(R.id.menu_frame);
		}
		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setMode(SlidingMenu.RIGHT);
		mSlidingMenu.setBehindWidth(DisplayUtil.dp2px(180));
		mSlidingMenu.setShadowWidth(DisplayUtil.dp2px(10));
		mSlidingMenu.setShadowDrawable(R.drawable.shadowright);
		//		mSlidingMenu.setBehindOffset(50);
		mSlidingMenu.setFadeEnabled(false);
		//		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSlidingMenu.setBehindScrollScale(0);
		mMenuFragment.setOnFragmentDoListener(new OnFragmentDoListener() {

			@Override
			public void onFragmentDo(int type, Object object) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void initUI() {
		// TODO Auto-generated method stub
		mScrollFragmentLayout = (ScrollFragmentLayout) findViewById(R.id.fragment_container);
	}

	private boolean showTmpFrag() {
		// TODO Auto-generated method stub
		BaseFragment mTmpFragment = popFragFromStack();
		if(mTmpFragment==null){
			return false;
		}else{
			transLeftInRightOut();
			transaction = mFragmentManager.beginTransaction();
			if(mCurFragment!=null){
				transaction.setCustomAnimations(animOut, animOut);
				transaction.detach(mCurFragment);
			}
			transaction.setCustomAnimations(animIn, animIn);
			transaction.attach(mTmpFragment);
			transaction.commit();
			mCurFragment = mTmpFragment;
			return true;
		}
	}

	private FragmentTransaction transaction;
	private MainFragment mBookMainFragment;
	private BaseFragment mCurFragment;
	private int animIn, animOut;
	private void showBookMain() {
		// TODO Auto-generated method stub
		mScrollFragmentLayout.setCanTouchScroll(false);
		transaction = mFragmentManager.beginTransaction();
		if(mCurFragment!=null){
			transaction.setCustomAnimations(animOut, animOut);
			transaction.detach(mCurFragment);
		}
		transaction.setCustomAnimations(animIn, animIn);
		if(mBookMainFragment==null){
			mBookMainFragment = new MainFragment();
			mBookMainFragment.setOnFragmentDoListener(new OnFragmentDoListener() {

				@Override
				public void onFragmentDo(int type, Object object) {
					// TODO Auto-generated method stub
					switch (type) {
					case BaseFragment.TYPE_TO_BACK:
						if(!mSlidingMenu.isMenuShowing()){
							mSlidingMenu.showMenu();
							return;
						}
						exitApp();
						break;
					case BaseFragment.TYPE_TO_CHANGE_SLIDEMODE:
						getSlidingMenu().setTouchModeAbove(TextUtil.obj2int(object));
						break;
					case BaseFragment.TYPE_TO_STORE_BOARD:
						addFragToStack(mCurFragment);
						transRightInLeftOut();
						showStoreBoard((Bundle) object);
						break;
					case BaseFragment.TYPE_TO_BOOK_DETAIL:
						addFragToStack(mCurFragment);
						transRightInLeftOut();
						showBookDetail((Bundle) object);
						break;
					case BaseFragment.TYPE_TO_BOOK_CATEGORY_LIST:
						addFragToStack(mCurFragment);
						transRightInLeftOut();
						showBookCategoryList((Bundle) object);
						break;
					case BaseFragment.TYPE_TO_BOOK_CATEGORY_INFO:
						addFragToStack(mCurFragment);
						transRightInLeftOut();
						showBookCategoryInfo((Bundle) object);
						break;
					case BaseFragment.TYPE_TO_BOOK_RANK:
						addFragToStack(mCurFragment);
						transRightInLeftOut();
						showBookRank();
						break;
					case BaseFragment.TYPE_TO_BOOK_CATEGORY:
						addFragToStack(mCurFragment);
						transRightInLeftOut();
						showCategory();
						break;
					case BaseFragment.TYPE_TO_BOOK_HOT:
						addFragToStack(mCurFragment);
						transRightInLeftOut();
						showHotBook();
						break;
					case BaseFragment.TYPE_TO_BOOK_NET_NOVEL:
						addFragToStack(mCurFragment);
						transRightInLeftOut();
						showNetNovel((Bundle) object);
						break;
					case BaseFragment.TYPE_TO_BOOK_SEARCH_RESULT:
						addFragToStack(mCurFragment);
						transRightInLeftOut();
						showSearchResult((Bundle) object);
						break;
					case BaseFragment.TYPE_TO_SOFTWARE_CLASSIFY:
						addFragToStack(mCurFragment);
						transRightInLeftOut();
						showSoftWareClassify((Bundle) object);
						break;
					default:
						break;
					}
				}
			});
			transaction.replace(R.id.fragment_container, mBookMainFragment);
		}else{
			transaction.attach(mBookMainFragment);
		}
		transaction.commit();
		mCurFragment = mBookMainFragment;
		clearFragStack();
	}

	private void showStoreBoard(Bundle data) {
		// TODO Auto-generated method stub
		transaction = mFragmentManager.beginTransaction();
		if(mCurFragment!=null){
			transaction.setCustomAnimations(animOut, animOut);
			transaction.detach(mCurFragment);
		}
		StoreBoardShowFragment storeBoardShowFragment = new StoreBoardShowFragment();
		storeBoardShowFragment.setArguments(data);
		storeBoardShowFragment.setOnFragmentDoListener(new OnFragmentDoListener() {

			@Override
			public void onFragmentDo(int type, Object object) {
				// TODO Auto-generated method stub
				switch (type) {
				case BaseFragment.TYPE_TO_BACK:
					transLeftInRightOut();
					if(!showTmpFrag())
						showBookMain();
					break;
				case BaseFragment.TYPE_TO_BOOK_DETAIL:
					addFragToStack(mCurFragment);
					transRightInLeftOut();
					showBookDetail((Bundle) object);
					break;
				default:
					break;
				}
			}
		});
		transaction.setCustomAnimations(animIn, animIn);
		transaction.replace(R.id.fragment_container, storeBoardShowFragment);
		transaction.commit();
		mCurFragment = storeBoardShowFragment;
	}

	private void showBookDetail(Bundle b) {
		// TODO Auto-generated method stub
		transaction = mFragmentManager.beginTransaction();
		if(mCurFragment!=null){
			transaction.setCustomAnimations(animOut, animOut);
			transaction.detach(mCurFragment);
		}
		StoreBookShowFragment storeBookShowFragment = new StoreBookShowFragment();
		storeBookShowFragment.setArguments(b);
		storeBookShowFragment.setOnFragmentDoListener(new OnFragmentDoListener() {

			@Override
			public void onFragmentDo(int type, Object data) {
				// TODO Auto-generated method stub
				switch (type) {
				case BaseFragment.TYPE_TO_BACK:
					transLeftInRightOut();
					if(!showTmpFrag())
						showBookMain();
					break;
				case BaseFragment.TYPE_TO_BOOK_DETAIL:
					addFragToStack(mCurFragment);
					transRightInLeftOut();
					showBookDetail((Bundle) data);
					break;
				case BaseFragment.TYPE_TO_BOOK_COMMENT:
					addFragToStack(mCurFragment);
					transRightInLeftOut();
					showBookComment((Bundle) data);
					break;
				default:
					break;
				}
			}
		});
		transaction.setCustomAnimations(animIn, animIn);
		transaction.replace(R.id.fragment_container, storeBookShowFragment);
		transaction.commit();
		mCurFragment = storeBookShowFragment;
	}

	private void showBookComment(Bundle b) {
		// TODO Auto-generated method stub
		transaction = mFragmentManager.beginTransaction();
		if(mCurFragment!=null){
			transaction.setCustomAnimations(animOut, animOut);
			transaction.detach(mCurFragment);
		}
		BookCommentFragment fragment = new BookCommentFragment();
		fragment.setOnFragmentDoListener(new OnFragmentDoListener() {

			@Override
			public void onFragmentDo(int type, Object data) {
				// TODO Auto-generated method stub
				switch (type) {
				case BaseFragment.TYPE_TO_BACK:
					transLeftInRightOut();
					if(!showTmpFrag())
						showBookMain();
					break;

				default:
					break;
				}
			}
		});
		fragment.setArguments(b);
		transaction.setCustomAnimations(animIn, animIn);
		transaction.replace(R.id.fragment_container, fragment);
		transaction.commit();
		mCurFragment = fragment;
	}

	private void showBookCategoryList(Bundle b) {
		// TODO Auto-generated method stub
		transaction = mFragmentManager.beginTransaction();
		if(mCurFragment!=null){
			transaction.setCustomAnimations(animOut, animOut);
			transaction.detach(mCurFragment);
		}
		StoreCategoryListFragment fragment = new StoreCategoryListFragment();
		fragment.setOnFragmentDoListener(new OnFragmentDoListener() {

			@Override
			public void onFragmentDo(int type, Object data) {
				// TODO Auto-generated method stub
				switch (type) {
				case BaseFragment.TYPE_TO_BACK:
					transLeftInRightOut();
					if(!showTmpFrag())
						showBookMain();
					break;
				case BaseFragment.TYPE_TO_BOOK_DETAIL:
					addFragToStack(mCurFragment);
					transRightInLeftOut();
					showBookDetail((Bundle) data);
					break;
				default:
					break;
				}
			}
		});
		fragment.setArguments(b);
		transaction.setCustomAnimations(animIn, animIn);
		transaction.replace(R.id.fragment_container, fragment);
		transaction.commit();
		mCurFragment = fragment;
	}

	private void showBookCategoryInfo(Bundle b) {
		// TODO Auto-generated method stub
		transaction = mFragmentManager.beginTransaction();
		if(mCurFragment!=null){
			transaction.setCustomAnimations(animOut, animOut);
			transaction.detach(mCurFragment);
		}
		StoreCategoryInfoFragment fragment = new StoreCategoryInfoFragment();
		fragment.setOnFragmentDoListener(new OnFragmentDoListener() {

			@Override
			public void onFragmentDo(int type, Object data) {
				// TODO Auto-generated method stub
				switch (type) {
				case BaseFragment.TYPE_TO_BACK:
					transLeftInRightOut();
					if(!showTmpFrag())
						showBookMain();
					break;
				case BaseFragment.TYPE_TO_BOOK_CATEGORY_LIST:
					addFragToStack(mCurFragment);
					transRightInLeftOut();
					showBookCategoryList((Bundle) data);
					break;
				case BaseFragment.TYPE_TO_BOOK_DETAIL:
					addFragToStack(mCurFragment);
					transRightInLeftOut();
					showBookDetail((Bundle) data);
					break;
				default:
					break;
				}
			}
		});
		fragment.setArguments(b);
		transaction.setCustomAnimations(animIn, animIn);
		transaction.replace(R.id.fragment_container, fragment);
		transaction.commit();
		mCurFragment = fragment;
	}

	private void showCategory() {
		// TODO Auto-generated method stub
		transaction = mFragmentManager.beginTransaction();
		if(mCurFragment!=null){
			transaction.setCustomAnimations(animOut, animOut);
			transaction.detach(mCurFragment);
		}
		StoreCategoryFragment fragment = new StoreCategoryFragment();
		fragment.setOnFragmentDoListener(new OnFragmentDoListener() {

			@Override
			public void onFragmentDo(int type, Object data) {
				// TODO Auto-generated method stub
				switch (type) {
				case BaseFragment.TYPE_TO_BACK:
					transLeftInRightOut();
					if(!showTmpFrag())
						showBookMain();
					break;
				case BaseFragment.TYPE_TO_BOOK_CATEGORY_LIST:
					addFragToStack(mCurFragment);
					transRightInLeftOut();
					showBookCategoryList((Bundle) data);
					break;
				default:
					break;
				}
			}
		});
		transaction.setCustomAnimations(animIn, animIn);
		transaction.replace(R.id.fragment_container, fragment);
		transaction.commit();
		mCurFragment = fragment;
	}

	private void showBookRank() {
		// TODO Auto-generated method stub
		transaction = mFragmentManager.beginTransaction();
		if(mCurFragment!=null){
			transaction.setCustomAnimations(animOut, animOut);
			transaction.detach(mCurFragment);
		}
		StoreBookRankFragment fragment = new StoreBookRankFragment();
		fragment.setOnFragmentDoListener(new OnFragmentDoListener() {

			@Override
			public void onFragmentDo(int type, Object data) {
				// TODO Auto-generated method stub
				switch (type) {
				case BaseFragment.TYPE_TO_BACK:
					transLeftInRightOut();
					if(!showTmpFrag())
						showBookMain();
					break;
				case BaseFragment.TYPE_TO_BOOK_RANK_LIST:
					addFragToStack(mCurFragment);
					transRightInLeftOut();
					showBookRankList((Bundle) data);
					break;
				default:
					break;
				}
			}
		});
		transaction.setCustomAnimations(animIn, animIn);
		transaction.replace(R.id.fragment_container, fragment);
		transaction.commit();
		mCurFragment = fragment;
	}

	private void showBookRankList(Bundle b) {
		// TODO Auto-generated method stub
		transaction = mFragmentManager.beginTransaction();
		if(mCurFragment!=null){
			transaction.setCustomAnimations(animOut, animOut);
			transaction.detach(mCurFragment);
		}
		StoreBookRankListFragment fragment = new StoreBookRankListFragment();
		fragment.setOnFragmentDoListener(new OnFragmentDoListener() {

			@Override
			public void onFragmentDo(int type, Object data) {
				// TODO Auto-generated method stub
				switch (type) {
				case BaseFragment.TYPE_TO_BACK:
					transLeftInRightOut();
					if(!showTmpFrag())
						showBookMain();
					break;
				case BaseFragment.TYPE_TO_BOOK_DETAIL:
					addFragToStack(mCurFragment);
					transRightInLeftOut();
					showBookDetail((Bundle) data);
					break;
				default:
					break;
				}
			}
		});
		fragment.setArguments(b);
		transaction.setCustomAnimations(animIn, animIn);
		transaction.replace(R.id.fragment_container, fragment);
		transaction.commit();
		mCurFragment = fragment;
	}

	private void showHotBook() {
		// TODO Auto-generated method stub
		transaction = mFragmentManager.beginTransaction();
		if(mCurFragment!=null){
			transaction.setCustomAnimations(animOut, animOut);
			transaction.detach(mCurFragment);
		}
		StoreHotBookFragment fragment = new StoreHotBookFragment();
		fragment.setOnFragmentDoListener(new OnFragmentDoListener() {

			@Override
			public void onFragmentDo(int type, Object data) {
				// TODO Auto-generated method stub
				switch (type) {
				case BaseFragment.TYPE_TO_BACK:
					transLeftInRightOut();
					if(!showTmpFrag())
						showBookMain();
					break;
				case BaseFragment.TYPE_TO_BOOK_DETAIL:
					addFragToStack(mCurFragment);
					transRightInLeftOut();
					showBookDetail((Bundle) data);
					break;

				default:
					break;
				}
			}
		});
		transaction.setCustomAnimations(animIn, animIn);
		transaction.replace(R.id.fragment_container, fragment);
		transaction.commit();
		mCurFragment = fragment;
	}

	private void showNetNovel(Bundle bundle) {
		// TODO Auto-generated method stub
		transaction = mFragmentManager.beginTransaction();
		if(mCurFragment!=null){
			transaction.setCustomAnimations(animOut, animOut);
			transaction.detach(mCurFragment);
		}
		StoreNetNovelListFragment fragment = new StoreNetNovelListFragment();
		fragment.setOnFragmentDoListener(new OnFragmentDoListener() {

			@Override
			public void onFragmentDo(int type, Object data) {
				// TODO Auto-generated method stub
				switch (type) {
				case BaseFragment.TYPE_TO_BACK:
					transLeftInRightOut();
					if(!showTmpFrag())
						showBookMain();
					break;
				case BaseFragment.TYPE_TO_BOOK_DETAIL:
					addFragToStack(mCurFragment);
					transRightInLeftOut();
					showBookDetail((Bundle) data);
					break;

				default:
					break;
				}
			}
		});
		fragment.setArguments(bundle);
		transaction.setCustomAnimations(animIn, animIn);
		transaction.replace(R.id.fragment_container, fragment);
		transaction.commit();
		mCurFragment = fragment;
	}

	private void showSearchResult(Bundle b) {
		// TODO Auto-generated method stub
		transaction = mFragmentManager.beginTransaction();
		if(mCurFragment!=null){
			transaction.setCustomAnimations(animOut, animOut);
			transaction.detach(mCurFragment);
		}
		StoreSearchResultFragment storeSearchResultFragment = new StoreSearchResultFragment();
		storeSearchResultFragment.setOnFragmentDoListener(new OnFragmentDoListener() {

			@Override
			public void onFragmentDo(int type, Object data) {
				// TODO Auto-generated method stub
				switch (type) {
				case BaseFragment.TYPE_TO_BACK:
					transLeftInRightOut();
					if(!showTmpFrag())
						showBookMain();
					break;
				case BaseFragment.TYPE_TO_BOOK_DETAIL:
					addFragToStack(mCurFragment);
					transRightInLeftOut();
					showBookDetail((Bundle) data);
					break;
				default:
					break;
				}
			}
		});
		storeSearchResultFragment.setArguments(b);
		transaction.setCustomAnimations(animIn, animIn);
		transaction.replace(R.id.fragment_container, storeSearchResultFragment);
		transaction.commit();
		mCurFragment = storeSearchResultFragment;
	}

	private void showSoftWareClassify(Bundle b) {
		// TODO Auto-generated method stub
		transaction = mFragmentManager.beginTransaction();
		if(mCurFragment!=null){
			transaction.setCustomAnimations(animOut, animOut);
			transaction.detach(mCurFragment);
		}
        SoftClassifyListFragment fragment = new SoftClassifyListFragment();
        fragment.setOnFragmentDoListener(new OnFragmentDoListener() {
			
			@Override
			public void onFragmentDo(int type, Object data) {
				// TODO Auto-generated method stub
				switch (type) {
				case BaseFragment.TYPE_TO_BACK:
					transLeftInRightOut();
					if(!showTmpFrag())
						showBookMain();
					break;
				default:
					break;
				}
			}
		});
        fragment.setArguments(b);
		transaction.setCustomAnimations(animIn, animIn);
		transaction.replace(R.id.fragment_container, fragment);
		transaction.commit();
		mCurFragment = fragment;
	}
	
	private void transLeftInRightOut() {
		// TODO Auto-generated method stub
		animIn = R.anim.slide_in_from_left;
		animOut = R.anim.slide_out_from_right;
	}

	private void transRightInLeftOut() {
		// TODO Auto-generated method stub
		animIn = R.anim.slide_in_from_right;
		animOut = R.anim.slide_out_from_left;
	}


	private Stack<BaseFragment> mTmpFragStack;
	private void addFragToStack(BaseFragment baseFragment) {
		// TODO Auto-generated method stub
		if(mTmpFragStack==null){
			mTmpFragStack = new Stack<BaseFragment>();
		}
		mTmpFragStack.push(baseFragment);
	}

	private BaseFragment popFragFromStack() {
		// TODO Auto-generated method stub
		if(mTmpFragStack==null || mTmpFragStack.isEmpty()){
			return null;
		}
		return mTmpFragStack.pop();
	}

	private void clearFragStack() {
		// TODO Auto-generated method stub
		if(mTmpFragStack!=null && !mTmpFragStack.isEmpty())
			mTmpFragStack.clear();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub		
		if(mCurFragment!=null){
			mCurFragment.onBackPressed();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			mSlidingMenu.toggle();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//设置isSoftLiving为false
		if (BaseApplication.getInstance().isNetBroadcastBind) {
			DownloadTask.getInstance().unRegisterNetConnectionBroadcast();
		}
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		mCurFragment.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 计算退出时间 *
	 */
	private long exitTime = 0;

	/**
	 * 退出程序，带有时间计算
	 */
	public void exitApp() {
		long time = System.currentTimeMillis();
		if ((time - exitTime) > 2000) {
			exitTime = time;
			Notice.showToast("再次按下返回键退出程序！");
		} else {
			finish();
		}
	}
	
	
	private void removeFragment(FragmentTransaction t, BaseFragment bf){
		t.detach(bf);
	}
	
	private void addFragment(FragmentTransaction t, BaseFragment bf) {
		// TODO Auto-generated method stub
		t.attach(bf);
	}
}

