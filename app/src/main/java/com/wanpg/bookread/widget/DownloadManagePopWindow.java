package com.wanpg.bookread.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.wanpg.bookread.R;
import com.wanpg.bookread.download.DownItem;
import com.wanpg.bookread.ui.activity.BookDownloadActivity;
import com.wanpg.bookread.utils.DisplayUtil;

public class DownloadManagePopWindow {

    private Context context;
    private Handler handler;
    private DownItem item;
    private PopupWindow pwWindow;
    private LinearLayout ll_add, ll_del, ll_info, ll_cancel;

    public DownloadManagePopWindow(Context context, Handler handler) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.handler = handler;
    }

    /**
     * 打开popwindow
     *
     * @return
     */
    public void openPopWindow(DownItem item, View parent) {
        this.item = item;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_download_manage, null);
        ll_add = (LinearLayout) view.findViewById(R.id.ll_add);
        ll_del = (LinearLayout) view.findViewById(R.id.ll_del);
        ll_info = (LinearLayout) view.findViewById(R.id.ll_info);
        ll_cancel = (LinearLayout) view.findViewById(R.id.ll_cancel);
        
        if(item.status == DownItem.STATUS_FINISHED){
        	ll_add.setVisibility(View.VISIBLE);
        	ll_del.setVisibility(View.VISIBLE);
        	ll_info.setVisibility(View.GONE);
        	ll_cancel.setVisibility(View.GONE);
        }else{
        	ll_add.setVisibility(View.GONE);
        	ll_del.setVisibility(View.GONE);
        	ll_info.setVisibility(View.VISIBLE);
        	ll_cancel.setVisibility(View.VISIBLE);
        }
        
        pwWindow = new PopupWindow(view, DisplayUtil.dp2px(134), DisplayUtil.dp2px(70), true);

        pwWindow.setBackgroundDrawable(context.getResources().getDrawable(R.color.transparent));
        pwWindow.setAnimationStyle(R.style.AnimationFade);
        pwWindow.showAsDropDown(parent, 0, -15);
        

        ll_add.setOnClickListener(new MyClickListener());
        ll_del.setOnClickListener(new MyClickListener());
        ll_info.setOnClickListener(new MyClickListener());
        ll_cancel.setOnClickListener(new MyClickListener());
    }

    /**
     * 关闭popwindow
     */
    public void closePopWindow() {
        if (pwWindow != null) {
            if (pwWindow.isShowing()) {
                pwWindow.dismiss();
            }
        }
    }


    private class MyClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.equals(ll_add)) {
                Message msg = handler.obtainMessage(BookDownloadActivity.ADD_ITEM_SHELF_NOTOFY, item);
                handler.sendMessage(msg);
            } else if (v.equals(ll_del)) {
                Message msg = handler.obtainMessage(BookDownloadActivity.DEL_ITEM_NOTOFY, item);
                handler.sendMessage(msg);
            } else if (v.equals(ll_info)) {
            	Message msg = handler.obtainMessage(BookDownloadActivity.INFO_ITEM_NOTOFY, item);
                handler.sendMessage(msg);
            } else if (v.equals(ll_cancel)){
                Message msg = handler.obtainMessage(BookDownloadActivity.CANCEL_ITEM_NOTOFY, item);
                handler.sendMessage(msg);
            }
            closePopWindow();
        }
    }
}
