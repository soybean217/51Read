package com.wanpg.bookread.ui.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.fb.FeedbackAgent;
import com.wanpg.bookread.BaseActivity;
import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.ui.activity.BookDownloadActivity;
import com.wanpg.bookread.ui.activity.BookFileBrowserActivity;
import com.wanpg.bookread.ui.activity.BookSettingReadActivity;
import com.wanpg.bookread.ui.activity.BookSettingSoftActivity;
import com.wanpg.bookread.widget.Notice;

public class MenuBarListAdapter extends BaseAdapter {

    private BaseActivity mActivity;
    private BaseFragment mFragment;
    private LayoutInflater inflater;
    private ViewHolder holder;

    private String[] menuNames = {
            "本地图书","阅读选项","下载管理","系统设置","意见反馈","退出"
    };
    private int[] menuIcons = {
            R.drawable.menu_localfile,
            R.drawable.menu_readset,
            R.drawable.menu_download,
            R.drawable.menu_softset,
            R.drawable.menu_feedback,
            R.drawable.menu_exit
    };

    public MenuBarListAdapter(BaseFragment mFragment) {
        // TODO Auto-generated constructor stub
    	this.mFragment = mFragment;
        this.mActivity = mFragment.mActivity;
        inflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (menuNames == null) {
            return 0;
        } else {
            return menuNames.length;
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (menuNames == null) {
            return null;
        } else {
            return menuNames[position];
        }
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
            convertView = inflater.inflate(R.layout.list_menu_cell, null);
            holder = new ViewHolder();
            holder.rl_main = (RelativeLayout) convertView.findViewById(R.id.rl_menu_bar_main);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_menu_bar);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_menu_bar_icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String name = menuNames[position];

        holder.tv_name.setText(name);
        holder.iv_icon.setImageResource(menuIcons[position]);
        holder.rl_main.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //String strName = "主题切换,本地图书,阅读选项,下载管理,系统设置,意见反馈,退出";
                if (name.equals("主题切换")) {
                    Notice.showToast("主题切换");
                    return;
                } else if (name.equals("本地图书")) {
//					BookFileBrowserFragment fragment = new BookFileBrowserFragment();
//					BookMainActivity.fragmentManager.beginTransaction()
//						.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_right, 
//							R.anim.slide_in_from_right, R.anim.slide_out_from_right)
//						.add(BookMainActivity.fragment_container_id, fragment)
//						.addToBackStack(null)
//						.commit();
                    Intent intent = new Intent();
                    intent.setClass(mActivity, BookFileBrowserActivity.class);
                    mActivity.startActivityForResult(intent, 1);
                    mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
                } else if (name.equals("阅读选项")) {
                    Intent intent = new Intent(mActivity, BookSettingReadActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
                } else if (name.equals("下载管理")) {
                    Intent intent = new Intent(mActivity, BookDownloadActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
                } else if (name.equals("系统设置")) {
                    Intent intent = new Intent(mActivity, BookSettingSoftActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
                } else if (name.equals("意见反馈")) {
                    FeedbackAgent agent = new FeedbackAgent(mActivity);
                    agent.startFeedbackActivity();
                    mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
                } else if (name.equals("退出")) {
                    mActivity.finish();
                    return;
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        public RelativeLayout rl_main;
        public TextView tv_name;
        public ImageView iv_icon;
    }

}
