package com.wanpg.bookread.paint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;

public class PageSlide extends PageView {

	/**拖拽点 */
	float mTouchCurX = 0f;
	float mTouchDownX = 0f;

	float mCurPagePointX = 0f;
	float mNextPagePointX = 0f;

	float mTouchCurToDownDisX = 0;

	Scroller mScroller;

	public PageSlide(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public PageSlide(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PageSlide(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	@SuppressLint("NewApi")
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		mScroller = new Scroller(getContext());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		calPagePoint();
		drawCurPageArea(canvas, mCurPageBitmap);
		drawNextPageArea(canvas, mNextPageBitmap);
	}
	
	@Override
	public void calDownXY(float x, float y) {
		// TODO Auto-generated method stub
		mTouchDownX = x;
	}

	@Override
	public void calCurXY(float x, float y) {
		// TODO Auto-generated method stub
		mTouchCurX = x;
		if(isToNextPage()){
			if(mTouchCurX>mTouchDownX){
				mTouchCurToDownDisX = 0;
				return ;
			}
		}else{
			if(mTouchCurX<mTouchDownX){
				mTouchCurToDownDisX = 0;
				return ;
			}
		}
		mTouchCurToDownDisX = mTouchCurX - mTouchDownX;
	}
	
	private void calPagePoint() {

		mCurPagePointX = mTouchCurToDownDisX;
		if (mTouchCurToDownDisX > 0) {
			//向右滑动，则下一页在左侧
			mNextPagePointX = mTouchCurToDownDisX - mWidth;
		} else {
			mNextPagePointX = mTouchCurToDownDisX + mWidth;
		}
	}

	private void drawCurPageArea(Canvas canvas, Bitmap bitmap) {
		Log.d("wanpg", "当前x=" + mCurPagePointX);
		canvas.drawBitmap(bitmap, mCurPagePointX, 0, mPaint);
	}

	private void drawNextPageArea(Canvas canvas, Bitmap bitmap) {
		canvas.drawBitmap(bitmap, mNextPagePointX, 0, mPaint);
	}

	@Override
	public boolean doTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			//如果是down，开始记录当前的触摸点
			mTouchCurX = event.getX();
		}
		calCurXY(event.getX(), event.getY());
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			this.postInvalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			startAnimation(800);
			postInvalidate();
		}
		return true;
	}

	@Override
	public void startAnimation(int delayMillis) {
		// TODO Auto-generated method stub
		int dx = 0;
		// 水平方向滑动的距离，负值会使滚动向左滚动
		if (mTouchCurToDownDisX > 0) {
			//是向右滑动，整体向右移动,dx为负值
			dx = (int) -(mTouchCurToDownDisX - mWidth);

		} else if (mTouchCurToDownDisX < 0) {
			//向左滑动，dx为正值
			dx = (int) -(mTouchCurToDownDisX + mWidth);
		} else if (mTouchCurToDownDisX == 0) {
			if (mTouchDownX < mWidth / 2) {
				dx = (int) -(mTouchCurToDownDisX - mWidth);
			} else {
				dx = (int) -(mTouchCurToDownDisX + mWidth);
			}
			mTouchCurX = mTouchDownX;
		}
		mScroller.startScroll((int) mTouchCurX, 0, dx, 0, delayMillis);
	}

	public void abortAnimation() {
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
		}
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			float x = mScroller.getCurrX();
			mTouchCurToDownDisX = x - mTouchDownX;
			this.postInvalidate();
		}
	}

	@Override
	public boolean canPageOver() {
		// TODO Auto-generated method stub
		if (Math.abs(mTouchCurToDownDisX) > mWidth / 10) {
			return true;
		} else if (mTouchCurToDownDisX == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isToNextPage() {
		// TODO Auto-generated method stub
		if (mTouchDownX < (mWidth / 2)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void onDrawWhenNoTouch() {
		// TODO Auto-generated method stub
		this.postInvalidate();
	}

	@Override
	public void startOverPage() {
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
