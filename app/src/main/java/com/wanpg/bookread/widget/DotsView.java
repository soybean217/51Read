package com.wanpg.bookread.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class DotsView extends View{

	public DotsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public DotsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DotsView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		for(int i=0;i<mDotCount;i++){
			if(i == mSelectIndex)
				paint.setColor(mSelectColor);
			else
				paint.setColor(mNormalColor);
			float leftNeed = left + (mDotSize+mMargin) * i;
			canvas.drawOval(new RectF(leftNeed, top, leftNeed+mDotSize, top + mDotSize), paint);
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;
		mHeight = h;
		calPos();
	}
	
	private void calPos() {
		// TODO Auto-generated method stub
		left = (mWidth-(mDotSize * mDotCount + mMargin * (mDotCount-1)))/2f;
		top = (mHeight - mDotSize)/2f;
	}
	
	
	private float left = 0;
	private float top = 0;
	private int mDotSize = 8;
	private int mMargin = 4;
	private int mDotCount = 1;
	public void setDot(int dotCount, int dotSize, int margin){
		setDot(dotCount, dotSize, margin, Color.LTGRAY, Color.GRAY);
	}
	
	private int mSelectColor = Color.LTGRAY;
	private int mNormalColor = Color.GRAY;
	private int mWidth = 480;
	private int mHeight = 30;
	public void setDot(int dotCount, int dotSize, int margin, int selectColor, int normalColor){
		mDotCount= dotCount;
		this.mDotSize = dp2px(dotSize);
		this.mMargin = dp2px(margin);
		mSelectColor = selectColor;
		mNormalColor = normalColor;
//		measure(mDotSize * dotCount + mMargin * (dotCount-1), mDotSize);
		calPos();
	}
	
	private int mSelectIndex = 0;

	public int getSelectIndex() {
		return mSelectIndex;
	}

	public void setSelectIndex(int mSelectIndex) {
		this.mSelectIndex = mSelectIndex;
		this.invalidate();
	}
	
	private int dp2px(float dpValue) {
		return (int) (dpValue * getResources().getDisplayMetrics().density + 0.5f);
	}
}
