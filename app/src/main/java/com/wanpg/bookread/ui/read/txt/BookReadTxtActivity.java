package com.wanpg.bookread.ui.read.txt;

import java.io.File;
import java.nio.MappedByteBuffer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.wanpg.bookread.R;
import com.wanpg.bookread.common.Config;
import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.logic.FileParseHelper;
import com.wanpg.bookread.manager.ShelfManager;
import com.wanpg.bookread.ui.read.BookReadBaseActivity;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.utils.FileUtil;
import com.wanpg.bookread.widget.BookReadTxtSetPopWindow;
import com.wanpg.bookread.widget.Notice;

/**
 * txt格式书籍阅读界面
 *
 * @author Jinpeng
 */
public class BookReadTxtActivity extends BookReadBaseActivity {

	public ShelfBook bookMode;

	private String BOOKNAME;
	private String BOOKPATH;

	public String tmpPath;

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

	private MappedByteBuffer bookByte = null;

	public int fontColor;
	public int fontSizeDp;
	public Typeface typeface;
	/**
	 * 行间距 *
	 */
	public int lineSpace;
	/**
	 * 左右页边距 *
	 */
	public int marginWidth;

	/**
	 * 本页文章的开始和结束的字符位置 *
	 */
	private int pageNowBegin, pageNowEnd;
	public int bookBGPicture;
	public int bookOldBG;

	public TxtPaint txtPaint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		initData();
		initPageView();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		FileUtil.delFolderChild(new File(Config.BOOK_SD_TEMP));
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (bookByte != null) {
			commitSharedPreferencesData();
		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		readSharedPreferencesData();
		initCanvas();
		initTxtPaint();
		initPageView();
		super.onRestart();
	}


	private void initData() {

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		bookMode = (ShelfBook) bundle.getSerializable("shelfBookmode");
		BOOKPATH = bookMode.bookPath;
		BOOKNAME = bookMode.bookName;

		tmpPath = FileParseHelper.parseTxtPath(BOOKPATH);

		bookByte = FileParseHelper.parseTXTMapedByte(tmpPath);

		if (bookByte == null) {
			//读取数据出错
			Notice.showToast("打开的文件有误！");
			this.finish();
			return;
		}

		readSharedPreferencesData();
		initCanvas();

		//初始化txtPaint，并初始化一些参数
		txtPaint = new TxtPaint(pageNowBegin, pageNowEnd);
		mPaint = txtPaint;
		initTxtPaint();
		txtPaint.setM_mbBuf(bookByte);//将bookByte传入txtpaint函数中
		txtPaint.setBookName(BOOKNAME);

	}

	@Override
	public void readSharedPreferencesData() {
		// TODO Auto-generated method stub
		String configBook = "config" + BOOKPATH.replace("/", "_");
		SharedPreferences spc = getSharedPreferences(configBook, MODE_PRIVATE);
		pageNowBegin = spc.getInt("pageNowBegin", 0);
		pageNowEnd = spc.getInt("pageNowEnd", 0);

		SharedPreferences spc1 = getSharedPreferences(Config.CONFIG_READ, MODE_PRIVATE);

		fontColor = spc1.getInt("fontColor", this.getResources().getColor(R.color.black));
		fontSizeDp = spc1.getInt("fontSizeDp", 18);
		bookBGPicture = spc1.getInt("bookBGPicture", R.drawable.bookread_back1);
		bookOldBG = spc1.getInt("bookOldBG", R.drawable.bookread_back1);
		isMoonOpen = spc1.getBoolean("isMoonOpen", false);
		lineSpace = spc1.getInt("lineSpace", 3);
		marginWidth = spc1.getInt("marginWidth", 15);
		isShowHeadMsg = spc1.getBoolean("isShowHeadMsg", true);
		isShowBottomMsg = spc1.getBoolean("isShowBottomMsg", true);
		isBatteryPercent = spc1.getBoolean("isBatteryPercent", false);
		isFullScreen = spc1.getBoolean("isFullScreen", true);
		isUseVolumeButton = spc1.getBoolean("isUseVolumeButton", false);
		lockScreenTime = spc1.getInt("lockScreenTime", 5 * 60 * 1000);
		pageChangeMode = spc1.getString("pageChangeMode", Config.PAGE_CHANGE_FLIP);
	}

	
	@Override
	public void commitSharedPreferencesData() {
		// TODO Auto-generated method stub
		String configBook = "config" + BOOKPATH.replace("/", "_");
		SharedPreferences spc = getSharedPreferences(configBook, MODE_PRIVATE);
		Editor editor = spc.edit();
		editor.putInt("pageNowBegin", txtPaint.getM_mbBufBegin());
		editor.putInt("pageNowEnd", txtPaint.getM_mbBufEnd());
		editor.commit();

		SharedPreferences spc1 = getSharedPreferences(Config.CONFIG_READ, MODE_PRIVATE);
		Editor e = spc1.edit();
		e.putInt("fontColor", fontColor);
		e.putInt("fontSizeDp", fontSizeDp);
		e.putInt("bookBGPicture", bookBGPicture);
		e.putInt("bookOldBG", bookOldBG);
		e.putBoolean("isMoonOpen", isMoonOpen);
		e.putString("pageChangeMode", pageChangeMode);
		e.putInt("lineSpace", lineSpace);
		e.putInt("marginWidth", marginWidth);
		e.putBoolean("isShowHeadMsg", isShowHeadMsg);
		e.putBoolean("isShowBottomMsg", isShowBottomMsg);
		e.putBoolean("isBatteryPercent", isBatteryPercent);
		e.putBoolean("isFullScreen", isFullScreen);
		e.putBoolean("isUseVolumeButton", isUseVolumeButton);
		e.putInt("lockScreenTime", lockScreenTime);
		e.putString("pageChangeMode", pageChangeMode);
		e.commit();
	}

	private void initTxtPaint() {
		txtPaint.setmWidth(disWidth);
		txtPaint.setmHeight(disHeight);

		txtPaint.setM_textColor(fontColor);//设置画笔字体颜色
		txtPaint.setM_fontSize(DisplayUtil.dp2px(fontSizeDp));
		bmBG = BitmapFactory.decodeResource(getResources(), bookBGPicture).copy(Bitmap.Config.RGB_565, true);
		bmBG = Bitmap.createScaledBitmap(bmBG, disWidth, disHeight, true);
		txtPaint.setM_backBitmap(bmBG);
		txtPaint.setM_lineSpaceing(lineSpace);
		txtPaint.setMarginWidth(marginWidth);
		txtPaint.setShowBottomMsg(isShowBottomMsg);
		txtPaint.setShowHeadMsg(isShowHeadMsg);
		txtPaint.setBatteryPercent(isBatteryPercent);
		txtPaint.initPaint();//设置画笔风格
	}

	/**
	 * 改变夜间模式
	 */
	public void changeMoonMode() {
		if (isMoonOpen) {
			isMoonOpen = false;
			fontColor = getResources().getColor(R.color.black);
			bookBGPicture = bookOldBG;
		} else {
			isMoonOpen = true;
			bookOldBG = bookBGPicture;
			fontColor = getResources().getColor(R.color.beige);
			bookBGPicture = R.drawable.bookread_backmoon;
		}
		initTxtPaint();
		txtPaint.setBmBattery(bmBattery, batteryLevel);
		txtPaint.onDrawPage(curPageCanvas);

		mPageView.setBitmaps(curPageBitmap, nextPageBitmap);
		mPageView.onDrawWhenNoTouch();
	}


	public void changeShowMode() {
		// TODO Auto-generated method stub
		initTxtPaint();
		txtPaint.curPage();
		txtPaint.onDrawPage(curPageCanvas);
		mPageView.setBitmaps(curPageBitmap, nextPageBitmap);
		mPageView.onDrawWhenNoTouch();    }


	public void changeTheme() {
		// TODO Auto-generated method stub
		if (isMoonOpen) {
			isMoonOpen = false;
			fontColor = getResources().getColor(R.color.black);
		}
		initTxtPaint();
		txtPaint.curPage();
		txtPaint.onDrawPage(curPageCanvas);
		mPageView.setBitmaps(curPageBitmap, nextPageBitmap);
		mPageView.onDrawWhenNoTouch();
	}

	private void initCanvas() {
		setScreenMode(isFullScreen);
		if(isFullScreen){
			int[] size = DisplayUtil.getDeviceSize(this);
			disWidth = size[0];
			disHeight = size[1];
		}else{
			float[] size = DisplayUtil.getWindowSize(this);
			disWidth = (int) size[0];
			disHeight = (int) size[1];
			disHeight = disHeight - DisplayUtil.getStatusHeight(BookReadTxtActivity.this);
		}

		//pageBackBitmap=BitmapFactory.decodeResource(this.getResources(), bookBGPicture);
		//当前显示页面的bitMap
		curPageBitmap = Bitmap.createBitmap(disWidth, disHeight, Bitmap.Config.RGB_565);
		//下一页显示页面的bitMap
		nextPageBitmap = Bitmap.createBitmap(disWidth, disHeight, Bitmap.Config.RGB_565);
		//创建画布，本页和下一页
		curPageCanvas = new Canvas(curPageBitmap);
		nextPageCanvas = new Canvas(nextPageBitmap);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == ShelfManager.CATALOG_REQUEST_CODE && resultCode == ShelfManager.CATALOG_RESULT_CODE) {
			Bundle bundle = data.getExtras();
			pageNowBegin = bundle.getInt("pageBegin");
			pageNowEnd = bundle.getInt("pageEnd");

			txtPaint.setM_mbBufBegin(pageNowBegin);
			txtPaint.setM_mbBufEnd(pageNowEnd);

			txtPaint.setBmBattery(bmBattery, batteryLevel);
			txtPaint.prePage();
			txtPaint.onDrawPage(curPageCanvas);

			mPageView.setBitmaps(curPageBitmap, nextPageBitmap);
			mPageView.onDrawWhenNoTouch();
		}
	}


	public boolean isMoonOpen() {
		return isMoonOpen;
	}


	public void setMoonOpen(boolean isMoonOpen) {
		this.isMoonOpen = isMoonOpen;
	}

	@Override
	public void onShowPopWindow(View parent, boolean isFullScreen) {
		// TODO Auto-generated method stub
		new BookReadTxtSetPopWindow(BookReadTxtActivity.this).openPopWindow(parent, isFullScreen);
	}


}
