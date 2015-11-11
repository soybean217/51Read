package com.wanpg.bookread.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wanpg.bookread.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookSearchAdvanceListViewAdapter extends BaseAdapter {


    private Context context;
    private List<String> listItems;

    private ViewHolder holder;
    private LayoutInflater layoutInflater;
    private Handler handler;
    private String parentPath;
    //private List<String> listChecked=new ArrayList<String>();;
    private Map<Integer, Boolean> mapIsChecked;

    public BookSearchAdvanceListViewAdapter(Context context, List<String> listItems, Handler handler) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.listItems = listItems;
        this.handler = handler;
        layoutInflater = LayoutInflater.from(context);
        initMapIsChecked(getCount());
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listItems.get(position + 1);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.booksearch_advance_cell, null);
            holder = new ViewHolder();
            holder.cb_checkbox = (CheckBox) convertView.findViewById(R.id.cb_booksearch_checkbox);
            holder.tv_bookname = (TextView) convertView.findViewById(R.id.tv_booksearch_bookname);
            holder.rl_back = (RelativeLayout) convertView.findViewById(R.id.rl_search_cell_back);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_bookname.setText(listItems.get(position));

        final String strPath = parentPath + "/" + listItems.get(position);
        final int x = position;
        holder.cb_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                Log.d("wanpg", "--变化前--" + mapIsChecked.get(x) + "---" + x);
                if (isChecked) {
                    mapIsChecked.put(x, true);
                } else {
                    mapIsChecked.put(x, false);
                }
                Log.d("wanpg", "--变化后--" + mapIsChecked.get(x) + "---" + x);
            }
        });
        holder.cb_checkbox.setChecked(mapIsChecked.get(position));
        holder.rl_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Message msg = handler.obtainMessage(0, strPath);
                handler.sendMessage(msg);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        public CheckBox cb_checkbox;
        public TextView tv_bookname;
        public RelativeLayout rl_back;
    }


    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public void setListItems(List<String> listItems) {
        this.listItems = listItems;
        initMapIsChecked(getCount());
    }

    private void initMapIsChecked(int x) {
        mapIsChecked = new HashMap<Integer, Boolean>();
        for (int i = 0; i < x; i++) {
            mapIsChecked.put(i, false);
        }
    }

    public Map<Integer, Boolean> getMapIsChecked() {
        return mapIsChecked;
    }
}
