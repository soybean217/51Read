package com.wanpg.bookread.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class ScrollFragmentLayout extends RelativeLayout {

	public enum FragmentStatusEnum {
		/**
		 * fragment关闭状态 *
		 */
		FRAG_CLOSED,
		/**
		 * fragment打开状态 *
		 */
		FRAG_OPEN,
		/**
		 * 触摸滑动状态 *
		 */
		FRAG_TOUCH_SLIDE,
		/**
		 * 自动滑动状态 *
		 */
		FRAG_AUTO_SLIDE
	}

	private float curX;
	private float curY;
	private float downX;
	private float downY;
	private Scroller mScroller;
	private int disWidth = 480;

	private boolean isCanTouchScroll = false;

	public ScrollFragmentLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public ScrollFragmentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public ScrollFragmentLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}


	public void init() {
		// TODO Auto-generated method stub
		mScroller = new Scroller(getContext());
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		disWidth = wm.getDefaultDisplay().getWidth();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		if(isCanTouchScroll){
			curX = e.getX();
			curY = e.getY();
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				downX = curX;
				downY = curY;
			}
			if (e.getAction() == MotionEvent.ACTION_MOVE) {
				//			if (!CustomHorizontalScrollView.isTouchHorizontalScrollView) {
				if (Math.abs(curX - downX) > Math.abs(curY - downY)) {
					return true;
				}
				//			}
			}
			if (e.getAction() == MotionEvent.ACTION_UP) {

			}
			return false;
		}else{
			return super.onInterceptTouchEvent(e);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		if(isCanTouchScroll){
			curX = e.getX();
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				//			if (mActivity.mFragmentManager.getBackStackEntryCount() != 0) {
				//
				//				return true;
				//			}
				if (getCurStatus() == 1) {
					return true;
				}
			}
			if (e.getAction() == MotionEvent.ACTION_MOVE) {
				float disX = downX - curX;
				if (disX <= 0 && disX > -disWidth) {
					scrollLayout((int) disX);
				} else {
					downX = curX;
				}
				return true;
			}
			if (e.getAction() == MotionEvent.ACTION_UP) {
				if (getCurStatus() == 1) {
					int scrollX = this.getScrollX();
					this.postInvalidate();
					if (scrollX > -disWidth / 3) {
						startAnimation(scrollX, -scrollX, Math.abs(scrollX) * 2);
					} else if (scrollX <= -disWidth / 3) {
						startAnimation(scrollX, -disWidth - scrollX, Math.abs(-disWidth - scrollX) * 2);
					}
				}
				return true;
			}
			return false;
		}else{
			return super.onTouchEvent(e);
		}
	}

	public void abortAnimation() {
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
		}
	}

	public void scrollLayout(int x) {
		// TODO Auto-generated method stub
		this.scrollTo(x, 0);
		this.invalidate();
		if (getCurStatus() == 2) {
			//			mActivity.mFragmentManager.popBackStack();
			abortAnimation();
		}
	}

	public void startAnimation(int x, int disX, int delayMillis) {
		abortAnimation();
		mScroller.startScroll(x, 0, disX, 0, delayMillis);
	}

	/**
	 * 获得当前fragment的状态
	 *
	 * @return 0--打开状态
	 * 1--中间滑动状态
	 * 2--关闭状态
	 * 3--滑过了的状态
	 * 4--其他状态
	 */
	public int getCurStatus() {
		int a = this.getScrollX();
		if (a == 0) {//打开状态
			return 0;
		} else if (a < 0 && a > -disWidth) {//中间滑动状态
			return 1;
		} else if (a == -disWidth) {//关闭状态
			return 2;
		} else if (a > 0) {//滑过了的状态
			return 3;
		}
		return 4;
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			scrollLayout(mScroller.getCurrX());
		}
	}

	public void setCanTouchScroll(boolean isCanTouchScroll) {
		this.isCanTouchScroll = isCanTouchScroll;
	}

}
