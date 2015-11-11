package com.wanpg.bookread.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class PageNone extends PageView {

    public PageNone(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public PageNone(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PageNone(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	private float mDownX = 0;
	
	boolean isTouch = false;
	@Override
    protected void onDraw(Canvas canvas) {
		if(isTouch){
			canvas.drawBitmap(mNextPageBitmap, 0, 0, mPaint);
		}else{
			canvas.drawBitmap(mCurPageBitmap, 0, 0, mPaint);
		}
		isTouch = false;
    }
	
	@Override
	public void onDrawWhenNoTouch() {
		// TODO Auto-generated method stub
		this.postInvalidate();
	}

	@Override
	public boolean doTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			isTouch = true;
			postInvalidate();
			return true;
		}
		return false;
	}

	@Override
	public void startAnimation(int delayMillis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void abortAnimation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canPageOver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void startOverPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isToNextPage() {
		// TODO Auto-generated method stub
		if(mDownX > mWidth / 2){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void calDownXY(float x, float y) {
		// TODO Auto-generated method stub
		mDownX = x;
	}

	@Override
	public void calCurXY(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calPreXY() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calNextXY() {
		// TODO Auto-generated method stub
		
	}

}
