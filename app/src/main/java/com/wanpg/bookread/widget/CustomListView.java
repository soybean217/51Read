package com.wanpg.bookread.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * 可以自定义为ListView添加headView
 *
 * @author wanpg
 */
public class CustomListView extends ListView {


    private Context context;
    private View headView, footView;

    public CustomListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        this.context = context;
        init();
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
        init();
    }

    public CustomListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        init();
    }


    private void init() {
        // TODO Auto-generated method stub
    }

    private void initHeadView() {
        // TODO Auto-generated method stub
        this.addHeaderView(headView);
    }

    private void initFootView() {
        // TODO Auto-generated method stub
        this.addFooterView(footView);
    }

    public View getHeadView() {
        return headView;
    }

    public void setHeadView(View headView) {
        this.headView = headView;
        initHeadView();
    }

    public View getFootView() {
        return footView;
    }

    public void setFootView(View footView) {
        this.footView = footView;
        initFootView();
    }
}
