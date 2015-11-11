package com.wanpg.bookread.ui.software;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanpg.bookread.BaseActivity;
import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;

public class SoftClassifyGridAdapter extends BaseAdapter {

    private BaseActivity mActivity;
    private List<Map<String, Object>> listItems;
    private ViewHolder holder;
    private int extraViewPos = -1;
    private BaseFragment mFragment;
    public SoftClassifyGridAdapter(BaseFragment fragment, List<Map<String, Object>> listItems) {
        // TODO Auto-generated constructor stub
    	this.mFragment = fragment;
        this.mActivity = mFragment.mActivity;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (listItems == null) {
            return 0;
        } else {
            if (listItems.size() % 2 == 1) {
                extraViewPos = listItems.size();
                return extraViewPos + 1;
            } else {
                return listItems.size();
            }
        }
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        if (listItems == null) {
            return null;
        } else {
            if (arg0 != extraViewPos) {
                return listItems.get(arg0);
            }
            return null;
        }
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int pos, View v, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (v == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            v = inflater.inflate(R.layout.layout_soft_classify_cell, null);
            holder.ll_main = (LinearLayout) v.findViewById(R.id.rl_soft_classify_cell);
            holder.iv_icon = (ImageView) v.findViewById(R.id.iv_soft_classify_cell);
            holder.tv_title = (TextView) v.findViewById(R.id.tv_soft_classify_cell);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        if (pos == extraViewPos) {
            holder.iv_icon.setVisibility(View.GONE);
            holder.tv_title.setVisibility(View.GONE);
        } else {
            holder.iv_icon.setImageDrawable(mActivity.getResources().getDrawable(Integer.parseInt(listItems.get(pos).get("IMAGE").toString())));
            holder.tv_title.setText(String.valueOf(listItems.get(pos).get("NAME")));

            final int x = pos;
            holder.ll_main.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Bundle bundle = new Bundle();
                    bundle.putString("NAME", listItems.get(x).get("NAME").toString());
                    bundle.putString("APPKEY", listItems.get(x).get("APPKEY").toString());
                    mFragment.onFragmentDo(BaseFragment.TYPE_TO_SOFTWARE_CLASSIFY, bundle);
                }
            });
        }
        return v;
    }

    private class ViewHolder {
        public LinearLayout ll_main;
        public ImageView iv_icon;
        public TextView tv_title;
    }

}
