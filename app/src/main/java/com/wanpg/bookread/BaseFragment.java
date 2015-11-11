package com.wanpg.bookread;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.wanpg.bookread.utils.LogUtil;

public class BaseFragment extends Fragment{
	
	public BaseActivity mActivity;
	public static final int TYPE_TO_BACK = 0;
	public static final int TYPE_TO_CHANGE_SLIDEMODE = 1;
	public static final int TYPE_TO_STORE_BOARD = 2;
	public static final int TYPE_TO_BOOK_DETAIL = 3;
	public static final int TYPE_TO_BOOK_COMMENT = 4;
	public static final int TYPE_TO_BOOK_CATEGORY_LIST = 5;
	public static final int TYPE_TO_BOOK_CATEGORY_INFO = 6;
	public static final int TYPE_TO_BOOK_CATEGORY = 7;
	public static final int TYPE_TO_BOOK_RANK = 8;
	public static final int TYPE_TO_BOOK_HOT = 9;
	public static final int TYPE_TO_BOOK_NET_NOVEL = 10;
	public static final int TYPE_TO_BOOK_SEARCH_RESULT = 11;
	public static final int TYPE_TO_BOOK_RANK_LIST = 12;
	public static final int TYPE_TO_SOFTWARE_CLASSIFY = 13;
	public static final int TYPE_TO_DOWNLOAD = 14;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = (BaseActivity) getActivity();
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
        LogUtil.D("wanpg", this.getClass().getSimpleName()+"_onDestroyView");
        super.onDestroyView();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getSimpleName());
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getSimpleName());
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	public void setHeadBackEvent(View v){
		if(v!=null){
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					mActivity.onBackPressed();
					onBackPressed();
				}
			});
		}
	}
	
	public void onBackPressed(){
		onFragmentDo(TYPE_TO_BACK, null);
	}
	
	public void onFragmentDo(int type, Object object){
		if(fragmentDoListener!=null)
			fragmentDoListener.onFragmentDo(type, object);
	}

	public void setOnFragmentDoListener(OnFragmentDoListener fragmentDoListener) {
		this.fragmentDoListener = fragmentDoListener;
	}
	private OnFragmentDoListener fragmentDoListener;
	public interface OnFragmentDoListener{
		void onFragmentDo(int type, Object data);
	}
	
	protected boolean resetView(View view) {
		// TODO Auto-generated method stub
		if(view==null){
			return false;
		}else{
			try {
				((ViewGroup)view.getParent()).removeView(view);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}
}
