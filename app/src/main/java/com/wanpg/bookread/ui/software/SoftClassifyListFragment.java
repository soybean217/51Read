package com.wanpg.bookread.ui.software;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.newxp.controller.ExchangeDataService;
import com.umeng.newxp.view.ExchangeViewManager;
import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;

/**
 * 软件类别的展示界面
 *
 * @author Jinpeng
 */
public class SoftClassifyListFragment extends BaseFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	private View v_main;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(!resetView(v_main)){
			v_main = inflater.inflate(R.layout.activity_soft_classifty_list, null);
			Bundle bundle = getArguments();
			String appKey = bundle.getString("APPKEY");
			String name = bundle.getString("NAME");
			setHeadBackEvent(v_main.findViewById(R.id.ib_back));

			((TextView) v_main.findViewById(R.id.tv_title)).setText(name);

			ListView listView = (ListView) v_main.findViewById(R.id.list);

			ExchangeDataService service_soft_list = new ExchangeDataService();
			service_soft_list.appkey = appKey;
			service_soft_list.autofill = 0;

			ExchangeViewManager exchangeViewManager = new ExchangeViewManager(getActivity(), service_soft_list);
			exchangeViewManager.addView((ViewGroup) listView.getParent(), listView);
		}
		return v_main;
	}
}
