package com.wanpg.bookread.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.wanpg.bookread.common.PhoneInfo;
import com.wanpg.bookread.utils.DisplayUtil;

/**
 * 可以向右滑动显示右边栏的relayoutLayout
 *
 * @author Jinpeng
 */
public class ScrollRelativeLayout extends RelativeLayout {

    private MenuStatusEnum mCurMenuState;
    private MenuStatusEnum mLastMenuState;

    /**
     * 刷新的状态
     */
    public enum MenuStatusEnum {
        /**
         * 菜单关闭状态 *
         */
        MENU_COLSED,
        /**
         * 菜单打开状态 *
         */
        MENU_OPENED,
        /**
         * 菜单触摸滑动状态 *
         */
        MENU_TOUCH_SLIDE,
        /**
         * 菜单自动滑动状态 *
         */
        MENU_AUTO_SLIDE,
        /**
         * 菜单触摸左侧预备打开状态 *
         */
        MENU_TOUCH_RIGHT_EDGE
    }

    int startX = 0;
    public Scroller mScroller;
    int disWidth = 480;
    Context context;
    public int maxX = 0;

    float menuOpenTouchDownX = 0f;
    float menuOpenTouchCurX = 0f;

    public ScrollRelativeLayout(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        this.context = context;
        init();
    }

    public ScrollRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
        init();
    }

    public ScrollRelativeLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        mScroller = new Scroller(context);
        disWidth = PhoneInfo.disWidthPx;
        if (disWidth < 320) {
            disWidth = DisplayUtil.getDisWidth();
        }
        maxX = DisplayUtil.dp2px(180);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        // TODO Auto-generated method stub
        if (mCurMenuState != MenuStatusEnum.MENU_TOUCH_RIGHT_EDGE) {
            switch (getMenuBarState()) {
                case 0:
                    mCurMenuState = MenuStatusEnum.MENU_COLSED;
                    break;
                case 1:
                    mCurMenuState = MenuStatusEnum.MENU_OPENED;
                    break;
                case -1:
                    if (e.getAction() == MotionEvent.ACTION_MOVE || e.getAction() == MotionEvent.ACTION_UP) {
                        mCurMenuState = MenuStatusEnum.MENU_TOUCH_SLIDE;
                    } else {
                        mCurMenuState = MenuStatusEnum.MENU_AUTO_SLIDE;
                    }
                    break;
            }
        }

        menuOpenTouchCurX = e.getX();

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            menuOpenTouchDownX = e.getX();
            //按下手指时menu为关闭状态
            if (mCurMenuState == MenuStatusEnum.MENU_COLSED) {
                //当前点击的位置为屏幕最左边   20dp以内的范围可以花开menu
                if (menuOpenTouchDownX <= disWidth && menuOpenTouchDownX > (disWidth - maxX / 15) && e.getY() > DisplayUtil.dp2px(48)) {
                    mCurMenuState = MenuStatusEnum.MENU_TOUCH_RIGHT_EDGE;
                    return true;
                } else {
                    return false;
                }
            }

            //按下手指为menu打开状态
            if (mCurMenuState == MenuStatusEnum.MENU_OPENED) {
                //触摸位置在menu上
                if (menuOpenTouchDownX > (disWidth - maxX)) {
                    return false;
                }
                //触摸在menu外
                if (menuOpenTouchDownX <= (disWidth - maxX)) {
                    return true;
                }
            }

            //按下手指为menu滑动状态
            if (mCurMenuState == MenuStatusEnum.MENU_AUTO_SLIDE) {
                return true;
            }
        }
        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            if (mCurMenuState == MenuStatusEnum.MENU_TOUCH_RIGHT_EDGE) {
                return true;
            }
            if (mCurMenuState == MenuStatusEnum.MENU_TOUCH_SLIDE) {
                return true;
            }

            if (mCurMenuState == MenuStatusEnum.MENU_OPENED) {
                return true;
            }
        }
        if (e.getAction() == MotionEvent.ACTION_UP) {
            //按下手指起来后menu打开状态
            if (mCurMenuState == MenuStatusEnum.MENU_OPENED) {
                //触摸在menu外
                if (menuOpenTouchCurX == menuOpenTouchDownX) {
                    return true;
                }
            }

            if (mCurMenuState == MenuStatusEnum.MENU_TOUCH_SLIDE) {
                return true;
            }


            //
            if (mCurMenuState == MenuStatusEnum.MENU_AUTO_SLIDE) {
                return true;
            }

            if (mCurMenuState == MenuStatusEnum.MENU_TOUCH_RIGHT_EDGE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // TODO Auto-generated method stub
        if (mCurMenuState != MenuStatusEnum.MENU_TOUCH_RIGHT_EDGE) {
            switch (getMenuBarState()) {
                case 0:
                    mCurMenuState = MenuStatusEnum.MENU_COLSED;
                    break;
                case 1:
                    mCurMenuState = MenuStatusEnum.MENU_OPENED;
                    break;
                case -1:
                    if (e.getAction() == MotionEvent.ACTION_MOVE || e.getAction() == MotionEvent.ACTION_UP) {
                        mCurMenuState = MenuStatusEnum.MENU_TOUCH_SLIDE;
                    } else {
                        mCurMenuState = MenuStatusEnum.MENU_AUTO_SLIDE;
                    }
                    break;
            }
        }


        menuOpenTouchCurX = e.getX();

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (mCurMenuState == MenuStatusEnum.MENU_TOUCH_RIGHT_EDGE) {
                //当前点击的位置为屏幕最左边   20dp以内的范围可以花开menu
                return true;
            }

            //按下手指为menu打开状态
            if (mCurMenuState == MenuStatusEnum.MENU_OPENED) {
                //触摸位置在menu上
                if (menuOpenTouchDownX > (disWidth - maxX)) {
                    return false;
                }
                //触摸在menu外
                if (menuOpenTouchDownX <= (disWidth - maxX)) {
                    return true;
                }
            }

            //按下手指为menu滑动状态
            if (mCurMenuState == MenuStatusEnum.MENU_AUTO_SLIDE) {
                return true;
            }
        }
        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            if (mCurMenuState == MenuStatusEnum.MENU_TOUCH_RIGHT_EDGE) {
                if (this.getScrollX() >= 0 && this.getScrollX() <= maxX) {
                    scrollLayout((int) (menuOpenTouchDownX - menuOpenTouchCurX));
                }
                if (this.getScrollX() > maxX) {
                    scrollLayout(maxX);
                }
                if (this.getScrollX() < 0) {
                    scrollLayout(0);
                }
                return true;
            }
            if (mCurMenuState == MenuStatusEnum.MENU_TOUCH_SLIDE) {
                if (this.getScrollX() >= 0 && this.getScrollX() <= maxX) {
                    if (mLastMenuState == MenuStatusEnum.MENU_OPENED) {
                        scrollLayout((int) (maxX - menuOpenTouchCurX + menuOpenTouchDownX));
                    } else {
                        scrollLayout((int) (menuOpenTouchDownX - menuOpenTouchCurX));
                    }
                }
                return true;
            }

            if (mCurMenuState == MenuStatusEnum.MENU_OPENED) {
                if (menuOpenTouchCurX > menuOpenTouchDownX) {
                    mLastMenuState = MenuStatusEnum.MENU_OPENED;
                    scrollLayout((int) (maxX - menuOpenTouchCurX + menuOpenTouchDownX));
                }
                return true;
            }
        }
        if (e.getAction() == MotionEvent.ACTION_UP) {
            //按下手指起来后menu打开状态
            int scrollX = this.getScrollX();
        	if (mCurMenuState == MenuStatusEnum.MENU_OPENED) {
                //触摸在menu外
                if (menuOpenTouchCurX == menuOpenTouchDownX) {
                    closeMenuBarAuto();
                    return true;
                }
            }

            if (mCurMenuState == MenuStatusEnum.MENU_TOUCH_SLIDE) {
                this.postInvalidate();
                int x = 0;
                if (scrollX > maxX * 2 / 3 && scrollX<(maxX-10)) {
                    x = maxX - this.getScrollX();
                } else {
                    x = -this.getScrollX();
                }
                startAnimation(scrollX, x, Math.abs(x) * 2);
                mCurMenuState = MenuStatusEnum.MENU_AUTO_SLIDE;
                return true;
            }


            //
            if (mCurMenuState == MenuStatusEnum.MENU_AUTO_SLIDE) {
                return true;
            }

            if (mCurMenuState == MenuStatusEnum.MENU_TOUCH_RIGHT_EDGE) {
                this.postInvalidate();
                int x = 0;
                if (this.getScrollX() > maxX / 3) {
                    x = maxX - this.getScrollX();
                } else {
                    x = -this.getScrollX();
                }
                startAnimation(this.getScrollX(), x, Math.abs(x) * 2);
                mCurMenuState = MenuStatusEnum.MENU_AUTO_SLIDE;
                return true;
            }
        }
        return false;
    }


    public void abortAnimation() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
    }

    public void scrollLayout(int x) {
        // TODO Auto-generated method stub
        this.scrollTo(x, 0);
//		this.postInvalidate();
        this.invalidate();
    }

    private void startAnimation(int x, int disX, int delayMillis) {
        mScroller.startScroll(x, 0, disX, 0, delayMillis);
    }

    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollLayout(mScroller.getCurrX());
        }
    }

    public void openMenuBarAuto() {
        abortAnimation();
        //viewMenuBar.setClickable(true);
        startX = 0;
//		this.postInvalidate();
        this.invalidate();
        startAnimation(startX, maxX, maxX * 2);
        mCurMenuState = MenuStatusEnum.MENU_OPENED;
    }

    public void closeMenuBarAuto() {
        abortAnimation();
        startX = maxX;
//		this.postInvalidate();
        this.invalidate();
        startAnimation(startX, -maxX, maxX * 2);
        mCurMenuState = MenuStatusEnum.MENU_COLSED;
    }

    /**
     * 返回menu打开的状态
     *
     * @return 0 -- menu关闭状态
     * 1 -- menu打开状态
     * -1 -- menu正在滑动状态
     */
    public int getMenuBarState() {
        int x = this.getScrollX();
        if (x == 0) {
            return 0;
        } else if (x == maxX) {
            return 1;
        } else {
            return -1;
        }
    }
}
