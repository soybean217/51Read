package com.wanpg.bookread.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanpg.bookread.R;
import com.wanpg.bookread.common.ShuPengConfig;
import com.wanpg.bookread.ui.activity.WebStoreActivity;

import java.util.List;
import java.util.Map;

public class BookNewUpdateListAdapter extends BaseAdapter {

    ViewHolder holder;
    List<Map<String, Object>> listItems;
    Context context;

    public BookNewUpdateListAdapter(Context context, List<Map<String, Object>> listItems) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public View getView(int pos, View v, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.store_update_list_cell, null);
            holder = new ViewHolder();
            holder.ll_main = (LinearLayout) v.findViewById(R.id.store_update_cell_main);
            holder.tv_time = (TextView) v.findViewById(R.id.tv_time);
            holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
            holder.tv_chapterInfo = (TextView) v.findViewById(R.id.tv_chapterInfo);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        String name = listItems.get(pos).get("name").toString();
        String time = listItems.get(pos).get("updatetime").toString();
        String info = "最新章节：" + listItems.get(pos).get("chapter").toString();
        final String chapterId = listItems.get(pos).get("chapterid").toString();
        holder.tv_name.setText(name);
        holder.tv_time.setText(time);
        holder.tv_chapterInfo.setText(info);
        holder.ll_main.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(context, WebStoreActivity.class);
                intent.putExtra("HREF", ShuPengConfig.SHUPENG_URL + "#/read/" + chapterId);
                context.startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        if (listItems == null) {
            return null;
        } else {
            return listItems.get(arg0);
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (listItems == null) {
            return 0;
        } else {
            return listItems.size();
        }
    }

    class ViewHolder {
        public TextView tv_name;
        public TextView tv_time;
        public TextView tv_chapterInfo;
        public LinearLayout ll_main;
    }

    public void setListItems(List<Map<String, Object>> listItems) {
        // TODO Auto-generated method stub
        this.listItems = listItems;
    }

}
