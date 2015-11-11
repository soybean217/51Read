package com.wanpg.bookread.ui.software;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.umeng.newxp.controller.ExchangeDataService;
import com.umeng.newxp.view.ExchangeViewManager;
import com.umeng.newxp.view.GridTemplateConfig;
import com.wanpg.bookread.R;
import com.wanpg.bookread.common.YouMengConfig;
import com.wanpg.bookread.ui.MainActivity;
import com.wanpg.bookread.widget.CustomListView;

public class SoftRecommendPage {

    private View parent;
    private MainActivity activity;
    private LayoutInflater inflater;

    public SoftRecommendPage(MainActivity activity, View parent) {
        // TODO Auto-generated constructor stub
        this.parent = parent;
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        initData();
    }

    private void initData() {
        // TODO Auto-generated method stub
        initUI();
    }

    private void initUI() {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.soft_recommend_icon_layout, null);
        final ListView listViewIcon = (ListView) v.findViewById(R.id.lv_soft_recommend_icon);
        ExchangeDataService service_recommend_icon = new ExchangeDataService();
        service_recommend_icon.appkey = YouMengConfig.APPKEY_SOFT_RECOMMEND_ICON;
        service_recommend_icon.autofill = 0;
        service_recommend_icon.setTemplate(1);
        ExchangeViewManager exchangeViewManager = new ExchangeViewManager(activity, service_recommend_icon);
        exchangeViewManager.setGridTemplateConfig(new GridTemplateConfig().setMaxPsize(10).setNumColumns(5).setVerticalSpacing(13));
        exchangeViewManager.addView((ViewGroup) v, listViewIcon);

        final CustomListView listViewList = (CustomListView) parent.findViewById(R.id.clv_soft_recommend_list);
        ExchangeDataService service_recommend_list = new ExchangeDataService();
        service_recommend_list.appkey = YouMengConfig.APPKEY_SOFT_RECOMMEND_ICON;
        service_recommend_list.autofill = 0;
        ExchangeViewManager exchangeViewManager1 = new ExchangeViewManager(activity, service_recommend_list);
        exchangeViewManager1.addView((ViewGroup) listViewList.getParent(), listViewList);

        listViewList.setHeadView(v);
    }
}
