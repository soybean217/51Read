package com.wanpg.bookread.ui.hall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.ui.adapter.MenuBarListAdapter;

public class ListMenuFragment extends BaseFragment{

	
	private View mMainView;
	
	private ListView lv;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mMainView = inflater.inflate(R.layout.layout_left_menu_bar, null);
		
		lv = (ListView) mMainView.findViewById(R.id.lv_menu_bar);
		lv.setAdapter(new MenuBarListAdapter(this));
		return mMainView;
	}
	
}
