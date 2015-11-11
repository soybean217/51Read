package com.wanpg.bookread.ui.read;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class BookBasePaint {

	
	public abstract boolean isFirstPage();
	public abstract boolean isLastPage();
	public abstract void setBmBattery(Bitmap bm, int level);
	public abstract void onDrawPage(Canvas c);
	public abstract void prePage();
	public abstract void nextPage();
}
