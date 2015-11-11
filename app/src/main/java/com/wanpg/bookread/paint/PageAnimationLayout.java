package com.wanpg.bookread.paint;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class PageAnimationLayout extends RelativeLayout{

	public PageAnimationLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public PageAnimationLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public PageAnimationLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
	}

	public static final int MODE_SLIDE = 0;

	public static final int LAST_PAGE = 0, NEXT_PAGE = 1;
	
	private int mCurDirect;
	private Scroller mScroller;
	private int width,height;
	private void init() {
		// TODO Auto-generated method stub
		mScroller = new Scroller(getContext());
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mCurMode = MODE_SLIDE;
	}

	public void setView(View v){
		this.addView(v, lp);
	}
	
	public void setVieGroup(ViewGroup vg){
		this.addView(vg, lp);
	}
	
	LayoutParams lp;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return true;
	}

	public void setAnOtherView(View v){
		if(mCurDirect == NEXT_PAGE){
			this.addView(v, 0, lp);
			v.scrollTo(0, 0);
		}else{
			this.addView(v, 1, lp);
			v.scrollTo(width, 0);
		}
		invalidate();
	}
	
	public void setAnOtherView(ViewGroup vg){
		System.gc();
		if(mCurDirect == NEXT_PAGE){
			this.addView(vg, 0, lp);
			vg.scrollTo(0, 0);
		}else{
			this.addView(vg, 1, lp);
			vg.scrollTo(width, 0);
		}
		invalidate();
	}
	
	public void calDownPos(float x, float y) {
		// TODO Auto-generated method stub
		downX = x;
		if(downX>width/2){
			mCurDirect = NEXT_PAGE;
		}else{
			mCurDirect = LAST_PAGE;
		}
	}

	private float curX = 0f;
	private float downX = 0f;
	public boolean doTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		curX = event.getX();
		Log.d("wanpg", "当前的触摸位置："+curX);
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			abortAnimation();
			calDownPos(event.getX(), event.getY());
			return true;
		}

		if(event.getAction() == MotionEvent.ACTION_MOVE){
			int disX = 0;
			if(mCurDirect == NEXT_PAGE){
				disX = 0;
				if(curX > downX)
					disX = 0;
				else{
					disX = (int) (downX - curX);
				}
			}else{
				disX = width;
				if(curX < downX)
					disX = width;
				else
					disX = (int) (width - (curX - downX));
			}
			
			scrollLayout(disX);
			return true;
		}

		if(event.getAction() == MotionEvent.ACTION_UP){
			int disX = 0;
			if(mCurDirect == NEXT_PAGE){
				if(curX > downX)
					return true;
				disX = (int) (width - (downX - curX));
			}else{
				if(curX < downX)
					return true;
				disX = - (int) (width - (downX - curX));
			}
			int delay = Math.abs(disX * 1000 / width);
			startAnimation((int) curX, disX, delay);
			return true;
		}
		return true;
	}

	private int mCurMode;
	public void scrollLayout(int x) {
		// TODO Auto-generated method stub
		if(mCurMode == MODE_SLIDE){
			if(getChildCount()<2)
				return;
			View v = getChildAt(1);
			v.scrollTo(x, 0);
			if(mCurDirect == NEXT_PAGE){
				if(v.getScrollX()>=width){
					//已经滑到边缘，删除上层view
					abortAnimation();
					this.removeViewAt(1);
				}
			}else{
				if(v.getScrollX()<=0){
					//已经滑到边缘，删除上层view
					abortAnimation();
					v.scrollTo(0, 0);
					this.removeViewAt(0);
				}
			}
		}
		this.postInvalidate();
	}

	private void startAnimation(int x, int disX, int delayMillis) {
		abortAnimation();
		mScroller.startScroll(x, 0, disX, 0, delayMillis);
	}

	public void abortAnimation() {
		mScroller.abortAnimation();
	}
	


	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			scrollLayout(mScroller.getCurrX());
		}
	}
	
	
	public int getCurDirect(){
		return mCurDirect;
	}

}
