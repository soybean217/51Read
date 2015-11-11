package com.wanpg.bookread.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wanpg.bookread.R;
import com.wanpg.bookread.utils.FileUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

public class FileBrowserListViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ViewHolder holder;
    private List<Map<String, String>> listItems;
    private Handler handler;

    private String parentPath;


    private int ICON_FOLDER = R.drawable.bookitem_folder;
    private int ICON_TXT = R.drawable.bookitem_txt;
    private int ICON_EPUB = R.drawable.bookitem_rar;
    private int ICON_RAR = R.drawable.bookitem_rar;
    private int ICON_UMD = R.drawable.bookitem_umd;

    public FileBrowserListViewAdapter(Context context, List<Map<String, String>> listItems, Handler handler) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.listItems = listItems;
        this.handler = handler;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listItems.get(position);
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
            convertView = layoutInflater.inflate(R.layout.file_browser_cell, null);
            holder = new ViewHolder();
            holder.iv_file_icon = (ImageView) convertView.findViewById(R.id.iv_file_browser_icon);
            holder.tv_file_msg = (TextView) convertView.findViewById(R.id.tv_file_browser_msg);
            holder.tv_file_name = (TextView) convertView.findViewById(R.id.tv_file_browser_filename);
            holder.rl_file_back = (RelativeLayout) convertView.findViewById(R.id.rl_file_browser_back);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String fileName = listItems.get(position).get("FILENAME");
        String fileMsg = listItems.get(position).get("FILEMSG");
        final String filePath = parentPath + "/" + fileName;

        holder.tv_file_name.setText(fileName);
        holder.tv_file_msg.setText(fileMsg);
        final File f = new File(filePath);

        if (f.isDirectory()) {
            holder.iv_file_icon.setImageDrawable(context.getResources().getDrawable(ICON_FOLDER));
        } else {
            String strType = FileUtil.getFileType(fileName);

            if (strType.equals(".epub")) {
                holder.iv_file_icon.setImageDrawable(context.getResources().getDrawable(ICON_EPUB));
            } else if (strType.equals(".txt")) {
                holder.iv_file_icon.setImageDrawable(context.getResources().getDrawable(ICON_TXT));
            } else if (strType.equals(".rar")) {
                holder.iv_file_icon.setImageDrawable(context.getResources().getDrawable(ICON_RAR));
            } else {
                holder.iv_file_icon.setImageDrawable(context.getResources().getDrawable(ICON_UMD));
            }

        }

        holder.rl_file_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Message msg = null;
                if (f.isDirectory()) {
                    if (f.list().length > 0) {
                        msg = handler.obtainMessage(1000, filePath);
                    } else {
                        return;
                    }
                } else {
                    msg = handler.obtainMessage(1001, filePath);

                }
                handler.sendMessage(msg);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        public ImageView iv_file_icon;
        public TextView tv_file_name;
        public TextView tv_file_msg;
        public RelativeLayout rl_file_back;
    }

    public void setListItems(List<Map<String, String>> listItems) {
        this.listItems = listItems;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }
}
