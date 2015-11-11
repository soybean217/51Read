package com.wanpg.bookread.widget;

import android.widget.Toast;

import com.wanpg.bookread.BaseApplication;
import com.wanpg.bookread.utils.TextUtil;

public class Notice {

	private static Toast mToast;
	public static void showToast(String info){
		if(TextUtil.isEmpty(info))
			return ;
		if(mToast==null){
			mToast = Toast.makeText(BaseApplication.getInstance(), "", Toast.LENGTH_SHORT);
		}
//		mToast.cancel();
		mToast.setText(info);
		int duation = Toast.LENGTH_SHORT;
		if(info.length()>15)
			duation = Toast.LENGTH_LONG;		
		mToast.setDuration(duation);
		mToast.show();
	}
}
