package com.wanpg.bookread.ui.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

public class MyViewPagerAdapter extends PagerAdapter {

    private List<View> viewList;

    public MyViewPagerAdapter(List<View> viewList) {
        this.viewList = viewList;
    }

    public MyViewPagerAdapter() {
        super();
        // TODO Auto-generated constructor stub
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
        ((ViewPager) v).addView(viewList.get(position));
        return viewList.get(position);
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
    
    public View getView(int pos){
    	return viewList.get(pos);
    }

}
