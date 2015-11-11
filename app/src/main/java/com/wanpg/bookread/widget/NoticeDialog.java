package com.wanpg.bookread.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.TextView;

import com.wanpg.bookread.R;
import com.wanpg.bookread.utils.TextUtil;

public class NoticeDialog {
	private Activity mActivity;
    private ProgressDialog mProgressDialog;
    public NoticeDialog(Activity activity) {
		mActivity = activity;
	}

	public void showWaitDialog(String text) {
		showWaitDialog(text, true);
	}

	public void showWaitDialog(final String text, final boolean cancelable) {
        if(Thread.currentThread().getName().equals("main")){
            show(text,cancelable);
        }else{
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    show(text,cancelable);
                }
            });
        }
	}

    private void show(String text, boolean cancelable){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setCanceledOnTouchOutside(false);
        } 
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.show();
        mProgressDialog.setContentView(R.layout.notice_dialog);
        if(!TextUtil.isEmpty(text))
        	((TextView)mProgressDialog.findViewById(R.id.text)).setText(text);
    }
	
	public void dismissDialog(){
		if((mActivity!=null && !mActivity.isFinishing()) && (mProgressDialog != null && mProgressDialog.isShowing())){
			if(Thread.currentThread().getName().equals("main")){
                mProgressDialog.dismiss();
            }else{
                mProgressDialog.dismiss();
            }
		}
	}
}
