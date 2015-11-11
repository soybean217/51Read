package com.wanpg.bookread.ui.read.epub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;

import com.wanpg.bookread.ui.read.BookBasePaint;
import com.wanpg.bookread.ui.read.epub.EpubKernel.EpubChapter;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.utils.MathUtil;
import com.wanpg.bookread.utils.Util;

/**
 * 绘制epub阅读页面的画笔模型
 *
 * @author Jinpeng
 */
public class EpubPaint extends BookBasePaint{
	private Paint mPaint;
	private Paint mMsgPaint;
	private Paint mImgPaint;
	private int mWidth, mHeight;
	private int mVisibleWidth, mVisibleHeight;
	private int marginWidth = 15, marginHeightDp = 20;
	private int mFontSize = 27;
	private int mFontColor = Color.BLACK;
	private int mBackColor = Color.BLACK; // 背景颜色
	private int mLineSpaceing = 3;
	private int mLineCount = 0;

	/**
	 * 是否显示顶部信息 *
	 */
	private boolean isShowHeadMsg;
	/**
	 * 是否显示底部信息 *
	 */
	private boolean isShowBottomMsg;
	/**
	 * 电池是否用百分数显示 *
	 */
	private boolean isBatteryPercent;

	private Bitmap mBackBitmap = null;
	private Vector<String> mLines = new Vector<String>();

	private String chapterTitle = "";
	private String bookName = "";

	private boolean isFirstPage, isLastPage;
	private int m_msgFontSize = 17;

	private Bitmap bmBattery = null;
	private EpubKernel mEpubKernel;
	/**
	 * 当此页为图片的时候，mlines的0索引位置是此tag
	 */
	private String imgTag = "this_page_is_img!";
	private int batteryLevel;
	private int sX,sY,sZ,eX,eY,eZ;
	private String mPercent;

	public void setReadPos(int curChapterIndex, int curLineIndex, int curStrIndex){
		sX = curChapterIndex;
		sY = curLineIndex;
		sZ = curStrIndex;
		eX = sX;
		eY = sY;
		eZ = sZ;
	}
	
	public void setPosByChapterid(String chapterId){
		sX = mEpubKernel.getSpineMap().indexOfValue(chapterId);
		sY = 0;
		sZ = 0;
		eX = sX;
		eY = sY;
		eZ = sZ;
	}
	
	public EpubPaint(EpubKernel ek) {
		// TODO Auto-generated constructor stub
		mEpubKernel = ek;
	}

	public void initPaint() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Align.LEFT);
		mPaint.setTextSize(mFontSize);
		mPaint.setColor(mFontColor);
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - DisplayUtil.dp2px(marginHeightDp) * 2;
		mLineCount = (int) (mVisibleHeight / (mFontSize + mLineSpaceing)); // 可显示的行数
		bookName = mEpubKernel.getTitle();
		if (mWidth > 540) {
			m_msgFontSize = 25;
		}
		mMsgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mMsgPaint.setAntiAlias(true);
		mMsgPaint.setColor(mFontColor);
		mMsgPaint.setTextSize(m_msgFontSize);
		mImgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}
	
	private boolean isOutChapter(int y, int z, EpubChapter mChapter){
		if(mChapter==null)
			return true;
		ArrayList<HashMap<String, String>> listThis = mChapter.content;
		if(y>=listThis.size()){
			return true;
		}else if(y==listThis.size()-1){
			if(listThis.get(y).get("type").equals("text")){
				if(z >= listThis.get(y).get("text").length()){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isBeforeChapter(int y, int z, EpubChapter mChapter){
		if(mChapter==null){
			return true;
		}
		if(y<0){
			return true;
		}else if(y==0){
			if(z<=0){
				return true;
			}
		}
		return false;
	}

	private Vector<String> pageDown() {
		String strParagraph = "";
		int x = eX;
		int y = eY;
		int z = eZ;
		EpubChapter mCurChapter = getChapter(x);
		while(isOutChapter(y, z, mCurChapter)){
			x++;
			if(x>=getChapterSize()){
				sX = getChapterSize()-1;
				mCurChapter = getChapter(x);
				sY = mCurChapter.content.size() - 1;
				if(sY<0){
					sZ = 0;
				}else{
					Map<String, String> map = mCurChapter.content.get(sY);
					sZ = map.get(map.get("type")).length();
				}
				eX = sX;
				eY = sY;
				eZ = sZ;
				return mLines;
			}
			y=0;
			z=0;
			mCurChapter = getChapter(x);
		}
		if(y<0) y=0;
		if(z<0) z=0;
		Vector<String> lines = new Vector<String>();
		chapterTitle = mCurChapter.title;
		ArrayList<HashMap<String, String>> listThis = mCurChapter.content;
		while (lines.size() < mLineCount && y < listThis.size()) {
			String type = listThis.get(y).get("type");
			if (type.equals("img")) {
				String imgHref = listThis.get(y).get("img");
				lines.add(imgTag);
				lines.add(imgHref);
				y++;
				z = 0;
				break;
			} else if (type.equals("text")) {
				strParagraph = listThis.get(y).get("text").substring(z);
				while (strParagraph.length() > 0) {
					int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
					lines.add(strParagraph.substring(0, nSize));
					strParagraph = strParagraph.substring(nSize);
					z += nSize;
					if (lines.size() >= mLineCount) {
						break;
					}
				}
				if (strParagraph==null || strParagraph.length() == 0) {
//					lines.add("");
					y++;
					z = 0;
				}
			}
		}
		sX = eX;
		sY = eY;
		sZ = eZ;
		eX = x;
		eY = y;
		eZ = z;
		return lines;
	}

	private Vector<String> pageUp() {
		// TODO Auto-generated method stub
		int x = sX;
		int y = sY;
		int z = sZ;
		EpubChapter mCurChapter = getChapter(x);
		while(isBeforeChapter(y, z, mCurChapter)){
			x--;
			if(x<0){
				sX = 0;
				sY = 0;
				sZ = 0;
				eX = 0;
				eY = 0;
				eZ = 0;
				return mLines;
			}
			mCurChapter = getChapter(x);
			y=mCurChapter.content.size()-1;
			if(y<0){
				z=0;
			}else{
				Map<String, String> map = mCurChapter.content.get(y);
				z=map.get(map.get("type")).length();
			}
		}
		Vector<String> lines = new Vector<String>();
		chapterTitle = mCurChapter.title;
		ArrayList<HashMap<String, String>> listThis = mCurChapter.content;
		while (lines.size() < mLineCount && y >=0) {
			if(z>0){
				//要找本一行的
				String type = listThis.get(y).get("type");
				if(type.equals("img")){
					if(lines.size()>0){
						break;
					}else{
						String imgHref = listThis.get(y).get("img");
						lines.add(imgTag);
						lines.add(imgHref);
						y--;
						z = 0;
						break;
					}
				}else{
					Vector<String> tmplines = new Vector<String>();
					String strParagraph = listThis.get(y).get("text").substring(0, z);
					while (strParagraph.length() > 0) {
						int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
						tmplines.add(strParagraph.substring(0, nSize));
						strParagraph = strParagraph.substring(nSize);
					}
					//				if (strParagraph.length() == 0) {
					//					lines.add("");
					//				}
					int size = tmplines.size();
					int needCount = mLineCount;
//					if(lines.size()>0){
						needCount = mLineCount - lines.size();
//					}
					//继续往前读
					for(int i=size-needCount-1;i>=0;i--){
						tmplines.remove(i);
					}
					StringBuilder sbTmp = new StringBuilder();
					for(String str : tmplines){
						sbTmp.append(str);
					}
					z -= sbTmp.toString().length();
//					if(lines.size()>0){
//						lines.add(0, "");
//					}
					lines.addAll(0, tmplines);
				}
			}else{
				y--;
				if(y>=0)
					z = listThis.get(y).get(listThis.get(y).get("type")).length();
			}
		}
		eX = sX;
		eY = sY;
		eZ = sZ;
		sX = x;
		sY = y;
		sZ = z;
		return lines;
	}

	@Override
	public void setBmBattery(Bitmap bmBattery, int batteryLevel) {
		this.bmBattery = bmBattery;
		this.batteryLevel = batteryLevel;
	}

	public void nextPage() {
		if(eX >= getChapterSize()){
			//最后一页
			isLastPage = true;
			return ;
		}else if(eX == getChapterSize()-1){
			if(isOutChapter(eY, eZ, getChapter(eX))){
				isLastPage = true;
				return;
			}
		}
		isLastPage = false;
		mLines.clear();
		mLines = pageDown();
		mPercent = calPercent(eX, eY, eZ);
	}

	private String calPercent(int x, int y, int z) {
		// TODO Auto-generated method stub
		int totalSize = getChapterSize();
		if(x<0) x=0;
		if(x>=totalSize) x = totalSize-1;
		EpubChapter ec = getChapter(x);
		int lineSize = ec.content.size();
//		int strSize = 0;
		if(y<0) y=0;
//		if(z<0) z=0;
//		if(lineSize>0 && y<lineSize){
//			Map<String, String> map = ec.content.get(y);
//			strSize = map.get(map.get("type")).length();
//		}
		float totalP = 0;
		if(lineSize>0){
			totalP = y * 1f / lineSize * 100;
		}
		if(totalP>=100){
			return "100%"; 
		}else if(totalP>=10 && totalP<100){
			return MathUtil.countPercent(y, lineSize, 1);
		}else{
			return MathUtil.countPercent(y, lineSize, 2);
		}
	}
	
	public void prePage() {
		if(sX<0){
			isFirstPage = true;
			return;
		}else if(sX == 0){
			if(isBeforeChapter(sY, sZ, getChapter(eX))){
				isFirstPage = true;
				return;
			}
		}
		isFirstPage = false;
		mLines.clear();
		mLines = pageUp();
		mPercent = calPercent(eX, eY, eZ);
	}

	private EpubChapter getChapter(int chapterIndex) {
		// TODO Auto-generated method stub
		return mEpubKernel.parseChapter(mEpubKernel.getSpineMap().get(chapterIndex));
	}
	
	private int getChapterSize(){
		return mEpubKernel.getSpineMap().size();
	}

	private Rect mBgSrc = new Rect();
	private Rect mBgDst = new Rect();
	public void onDrawPage(Canvas c) {
		float x = 0;
		float y = DisplayUtil.dp2px(marginHeightDp) * 7 / 6 - mLineSpaceing;
		float lenStr = 0;
		if (mBackBitmap == null)
			c.drawColor(mBackColor);
		else{
			mBgSrc.left = 0;
			mBgSrc.top = 0;
			mBgSrc.right = mBackBitmap.getWidth();
			mBgSrc.bottom = mBackBitmap.getHeight();
			mBgDst.left = 0;
			mBgDst.right = 0;
			mBgDst.right = c.getWidth();
			mBgDst.bottom = c.getHeight();
			c.drawBitmap(mBackBitmap, mBgSrc, mBgDst, null);
		}
		if (mLines == null || mLines.size() == 0) {
			return;
		}
		if (mLines.get(0).equals(imgTag)) {
			Bitmap imgBm = null;
			int outW = mWidth * 6 / 8;
			int outH = mHeight * 6 / 8;
			int drawW = outW;
			int drawH = outH;
			Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(mEpubKernel.getEpubBaseDir() + "/" + mLines.get(1), options);
			int bmW = options.outWidth;
			int bmH = options.outHeight;
			int scale = 1;
			if (bmW > bmH) {
				//横图
				scale = (int) (bmW / (float) outW);

				if (bmW < outW) {
					drawW = bmW;
					drawH = bmH;
				} else {
					drawH = bmH * outW / bmW;
				}
			} else {
				scale = (int) (bmH / (float) outH);
				if (bmH < outH) {
					drawW = bmW;
					drawH = bmH;
				} else {
					drawW = bmW * outH / bmH;
				}
			}
			options.outWidth = drawW;
			options.outHeight = drawH;
			options.inSampleSize = scale;
			options.inJustDecodeBounds = false;
			imgBm = BitmapFactory.decodeFile(mEpubKernel.getEpubBaseDir() + "/" + mLines.get(1), options);
			if (imgBm != null) {
				c.drawBitmap(imgBm, (mWidth - drawW) / 2, (mHeight - drawH) / 2, mImgPaint);
			}
		} else {
			//逐行取出str，并画到界面上
			for (String strLine : mLines) {
				float lenTmp = mPaint.measureText(strLine);
				if (lenTmp > lenStr) {
					lenStr = lenTmp;
				}
			}
			x = (mWidth - lenStr) / 2;
			for (String strLine : mLines) {
				y += mFontSize + mLineSpaceing;
				c.drawText(strLine, x, y, mPaint);
			}
		}

		//画出标题，章节，进度，时间，电量
		if (isShowHeadMsg) {
			onDrawHeadMsg(c);
		}
		if (isShowBottomMsg) {
			onDrawBottomMsg(c);
		}
	}
	
	Rect mBatterySrc = new Rect();
	Rect mBatteryDst = new Rect();
	//画出标题，章节，进度，时间，电量
	private void onDrawBottomMsg(Canvas canvas) {
		String percent = String.format(Locale.CHINESE, "共%1$d章　第%2$d章　%3$s", getChapterSize(), sX+1, mPercent);
		canvas.drawText(percent, marginWidth * 3 / 2, mHeight - DisplayUtil.dp2px(marginHeightDp) / 4, mMsgPaint);

		float lenBattery = 0f;
		float heiBattery = 0f;
		if (isBatteryPercent) {
			lenBattery = mMsgPaint.measureText("电量:" + batteryLevel + "%");
			canvas.drawText("电量:" + batteryLevel + "%", mWidth - marginWidth * 3 / 2 - lenBattery, mHeight - DisplayUtil.dp2px(marginHeightDp) / 4, mMsgPaint);
		} else {          
			if(bmBattery!=null && !bmBattery.isRecycled()){
				lenBattery = m_msgFontSize * 45 / 30;
				heiBattery = lenBattery * bmBattery.getHeight() / bmBattery.getWidth();
				mBatteryDst.left = (int) (mWidth - marginWidth * 3 / 2 - lenBattery);
				mBatteryDst.top = (int) (mHeight - DisplayUtil.dp2px(marginHeightDp) / 4 - heiBattery + 2);
				mBatteryDst.right = (int) (mBatteryDst.left + lenBattery);
				mBatteryDst.bottom = (int) (mBatteryDst.top + heiBattery);
	
				mBatterySrc.top = 0;
				mBatterySrc.left = 0;
				mBatterySrc.right = bmBattery.getWidth();
				mBatterySrc.bottom = bmBattery.getHeight();
				canvas.drawBitmap(bmBattery, mBatterySrc, mBatteryDst, mMsgPaint);
			}
		}

		String time = Util.getSysTime();
		float lenTime = mMsgPaint.measureText(time);
		canvas.drawText(time, mWidth - marginWidth * 3 / 2 - lenTime - lenBattery * 3 / 2, mHeight - DisplayUtil.dp2px(marginHeightDp) / 4, mMsgPaint);
	}


	private void onDrawHeadMsg(Canvas canvas) {
		// TODO Auto-generated method stub
		//画出标题
		String sName = bookName.replace(".epub", "");
		canvas.drawText(sName, marginWidth * 3 / 2, DisplayUtil.dp2px(marginHeightDp) * 4 / 5, mMsgPaint);

		//章节名称
		if (chapterTitle.length() > 10) {
			chapterTitle = chapterTitle.substring(0, 9).concat("...");
		}
		float lenTitle = mMsgPaint.measureText(chapterTitle);
		if (!chapterTitle.equals("")) {
			canvas.drawText(chapterTitle, mWidth - marginWidth * 3 / 2 - lenTitle, DisplayUtil.dp2px(marginHeightDp) * 4 / 5, mMsgPaint);
		}
	}

	public void setMarginWidth(int marginWidth) {
		this.marginWidth = marginWidth;
	}

	public void setFontSize(int mFontSize) {
		this.mFontSize = mFontSize;
	}

	public void setFontColor(int mFontColor) {
		this.mFontColor = mFontColor;
	}

	public void setLineSpaceing(int mLineSpaceing) {
		this.mLineSpaceing = mLineSpaceing;
	}

	public void setBackBitmap(Bitmap mBackBitmap) {
		this.mBackBitmap = mBackBitmap;
	}

	public boolean isFirstPage() {
		return isFirstPage;
	}

	public boolean isLastPage() {
		return isLastPage;
	}

	public void setDisWidth(int disWidth) {
		this.mWidth = disWidth;
	}

	public void setDisHeight(int disHeight) {
		this.mHeight = disHeight;
	}

	public void setShowHeadMsg(boolean isShowHeadMsg) {
		this.isShowHeadMsg = isShowHeadMsg;
	}

	public void setShowBottomMsg(boolean isShowBottomMsg) {
		this.isShowBottomMsg = isShowBottomMsg;
	}

	public void setBatteryPercent(boolean isBatteryPercent) {
		this.isBatteryPercent = isBatteryPercent;
	}
	
	public int[] getCurPos(){
		return new int[]{sX, sY, sZ};
	}

	public void resetPos() {
		// TODO Auto-generated method stub
		eX = sX;
		eY = sY;
		eZ = sZ;
	}

}
