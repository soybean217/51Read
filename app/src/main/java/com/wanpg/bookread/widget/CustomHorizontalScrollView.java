package com.wanpg.bookread.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class CustomHorizontalScrollView extends HorizontalScrollView {

    private float curX = 0;
    private float curY = 0;
    private float oldX = 0;
    private float oldY = 0;

    public CustomHorizontalScrollView(Context context, AttributeSet attrs,
                                      int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public CustomHorizontalScrollView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            oldX = curX;
//            oldY = curY;
//            return super.onInterceptTouchEvent(ev);
//        }
//        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//            if (curX != oldY) {
//                return true;
//            } else {
//                return super.onInterceptTouchEvent(ev);
//            }
//        }
//        if (ev.getAction() == MotionEvent.ACTION_UP) {
//            return super.onInterceptTouchEvent(ev);
//        }
//        return super.onInterceptTouchEvent(ev);
    	return true;
    }


}
