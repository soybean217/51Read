package com.wanpg.bookread.paint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public abstract class PageView extends View{

	public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}

	public PageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public PageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	@SuppressLint("NewApi")
	protected void init() {
		// TODO Auto-generated method stub
		try {
			if(android.os.Build.VERSION.SDK_INT >= 11){
				setLayerType(LAYER_TYPE_SOFTWARE, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.FILL);
	}

    protected Bitmap mCurPageBitmap = null; // 当前页
    protected Bitmap mNextPageBitmap = null;
    protected int mWidth,mHeight;
    protected Paint mPaint;

	
    public void setBitmaps(Bitmap bm1, Bitmap bm2) {
        mCurPageBitmap = bm1;
        mNextPageBitmap = bm2;
    }
    
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;
		mHeight = h;
		if(mScreenSizeChanged!=null){
			mScreenSizeChanged.onChanged(w, h);
		}
	}
	
	private OnScreenSizeChanged mScreenSizeChanged;
	public interface OnScreenSizeChanged {
		void onChanged(int width, int height);
	}
	
	public void setScreenSizeChangeCallBack(OnScreenSizeChanged mScreenSizeChanged){
		this.mScreenSizeChanged = mScreenSizeChanged;
	}
	
    public abstract void onDrawWhenNoTouch();
    public abstract boolean doTouchEvent(MotionEvent event);
    public abstract void startAnimation(int delayMillis);
    public abstract void abortAnimation();
    public abstract void calDownXY(float x, float y);
    public abstract void calCurXY(float x, float y);
    public abstract void calPreXY();
    public abstract void calNextXY();
	public abstract boolean canPageOver();
	public abstract void startOverPage();
	public abstract boolean isToNextPage();
}
