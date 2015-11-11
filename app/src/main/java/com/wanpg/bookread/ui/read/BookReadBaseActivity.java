package com.wanpg.bookread.ui.read;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wanpg.bookread.BaseActivity;
import com.wanpg.bookread.R;
import com.wanpg.bookread.common.Config;
import com.wanpg.bookread.paint.PageFlip;
import com.wanpg.bookread.paint.PageNone;
import com.wanpg.bookread.paint.PageSlide;
import com.wanpg.bookread.paint.PageView;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.utils.ImageUtil;

public abstract class BookReadBaseActivity extends BaseActivity{
	

	protected RelativeLayout mMainLayout;	
	protected Bitmap bmBG = null;
	protected int disWidth,disHeight;	
	/**
	 * 是否是全屏模式 
	 */
	public boolean isFullScreen;
	
	/**
	 * 页面切换的效果  none  flip   slide *
	 */
	protected String pageChangeMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		setContentView(R.layout.activity_book_read);
		mMainLayout = (RelativeLayout) findViewById(R.id.rl_book_read_layout);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//注销Receiver
		unregisterReceiver(batteryReceiver);
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setLockTimeToMnie();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		setLockTimeToSystem();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		recycleBitmap();
		super.onStop();
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		DisplayUtil.setScreenOffTime(sysLocalScreenTime);
		commitSharedPreferencesData();//将一些参数写入SharedPreferences
		super.onSaveInstanceState(outState);
	}
	

	private void recycleBitmap() {
		// TODO Auto-generated method stub
		if(curPageBitmap!=null && !curPageBitmap.isRecycled())
			curPageBitmap.recycle();
		if(nextPageBitmap!=null && !nextPageBitmap.isRecycled())
			nextPageBitmap.recycle();
		if(bmBG!=null && !bmBG.isRecycled())
			bmBG.recycle();
		curPageBitmap = null;
		nextPageBitmap = null;
		bmBG = null;
	}
	
	@SuppressLint("NewApi")
	public void setScreenMode(boolean isFullScreen){
		if (isFullScreen) {
			if (android.os.Build.VERSION.SDK_INT >= 14) {
				getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }else{
    			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
		} else {
			if (android.os.Build.VERSION.SDK_INT >= 14) {
				getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
			}else{
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}
		}
	}	
	
	protected Bitmap bmBattery = null;
	protected int batteryLevel = 0;
	private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			batteryLevel = arg1.getIntExtra("level", 0);
			resetBatteryBitmap();
		}
	};
	
	/**
	 * 是否打开了夜间模式 *
	 */
	protected boolean isMoonOpen = false;
	private void resetBatteryBitmap(){
		bmBattery = ImageUtil.getBatteryBm(BookReadBaseActivity.this, isMoonOpen, batteryLevel);
	}
	
	
	protected PageView mPageView;
	protected BookBasePaint mPaint;	/**
	 * 是否使用音量键翻页 *
	 */
	protected boolean isUseVolumeButton;
	
	protected Bitmap curPageBitmap;
	protected Bitmap nextPageBitmap;
	protected Canvas curPageCanvas;
	protected Canvas nextPageCanvas;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			onShowPopWindow(mMainLayout, isFullScreen);
			return true;
		}
		
		if (isUseVolumeButton) {
			if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
				mPageView.abortAnimation();
				mPageView.calPreXY();
				mPaint.onDrawPage(curPageCanvas);
				mPaint.setBmBattery(bmBattery, batteryLevel);
				mPaint.prePage();
				if (mPaint.isFirstPage()) {
					Toast.makeText(this, "已经是第一页了！", Toast.LENGTH_SHORT).show();
					return true;
				}
				mPaint.onDrawPage(nextPageCanvas);
				mPageView.setBitmaps(curPageBitmap, nextPageBitmap);
				mPageView.startOverPage();
				return true;
			}

			if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
				//下一页
				mPageView.abortAnimation();
				mPaint.onDrawPage(curPageCanvas);
				mPageView.calNextXY();
				mPaint.setBmBattery(bmBattery, batteryLevel);
				mPaint.nextPage();
				if (mPaint.isLastPage()) {
					Toast.makeText(this, "已经是最后一页了！", Toast.LENGTH_SHORT).show();
					return true;
				}
				mPaint.onDrawPage(nextPageCanvas);
				mPageView.setBitmaps(curPageBitmap, nextPageBitmap);
				mPageView.startOverPage();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	protected void initPageView() {
		// TODO Auto-generated method stub
		if (pageChangeMode.equals(Config.PAGE_CHANGE_NONE)) {
			mPageView = new PageNone(this);
		} else if (pageChangeMode.equals(Config.PAGE_CHANGE_FLIP)) {
			mPageView = new PageFlip(this);
		} else if (pageChangeMode.equals(Config.PAGE_CHANGE_SLIDE)) {
			mPageView = new PageSlide(this);
		} else if (pageChangeMode.equals(Config.PAGE_CHANGE_COVER)) {

		}
		mMainLayout.removeAllViews();
		mMainLayout.addView(mPageView,  new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		mPaint.nextPage();
		mPaint.setBmBattery(bmBattery, batteryLevel);
		mPaint.onDrawPage(curPageCanvas);
		mPageView.setBitmaps(curPageBitmap, nextPageBitmap);

		mPageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent e) {
				// TODO Auto-generated method stub
				if (e.getAction() == MotionEvent.ACTION_DOWN) {
					setScreenMode(isFullScreen);
					if(isFullScreen){
						if(e.getY() < 20 || e.getY() > disHeight - 20){
							return false;
						}
					}
					if (e.getX() > disWidth * 2 / 5 && e.getX() < disWidth * 3 / 5) {
						onShowPopWindow(mMainLayout, isFullScreen);
						return false;
					}
					mPageView.abortAnimation();
					mPageView.calDownXY(e.getX(), e.getY());
					mPaint.onDrawPage(curPageCanvas);
					mPaint.setBmBattery(bmBattery, batteryLevel);
					if (mPageView.isToNextPage()) {
						mPaint.nextPage();
						if (mPaint.isLastPage()) {
							Toast.makeText(BookReadBaseActivity.this, "已经是最后一页了！", Toast.LENGTH_SHORT).show();
							return false;
						}
					} else {
						mPaint.prePage();
						if (mPaint.isFirstPage()) {
							Toast.makeText(BookReadBaseActivity.this, "已经是第一页了！", Toast.LENGTH_SHORT).show();
							return false;
						}
					}
					mPaint.onDrawPage(nextPageCanvas);
					mPageView.setBitmaps(curPageBitmap, nextPageBitmap);
				}
				return mPageView.doTouchEvent(e);
			}
		});
	}
	
	
	/**
	 * 锁屏时间，按照毫秒计算*
	 */
	protected int lockScreenTime;
	private int sysLocalScreenTime = 1 * 60 * 1000;
	private void setLockTimeToMnie(){
		sysLocalScreenTime = DisplayUtil.getScreenOffTime();
		DisplayUtil.setScreenOffTime(lockScreenTime);
	}
	
	private void setLockTimeToSystem(){
		DisplayUtil.setScreenOffTime(sysLocalScreenTime);
	}
	
	public abstract void onShowPopWindow(View parent, boolean isFullScreen);
	/**
	 * 提交数据到SharedPreferences
	 */
	public abstract void commitSharedPreferencesData();
	/**
	 * 读取数据从SharedPreferences
	 */
	public abstract void readSharedPreferencesData();
}
