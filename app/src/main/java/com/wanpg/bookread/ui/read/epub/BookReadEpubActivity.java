package com.wanpg.bookread.ui.read.epub;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.wanpg.bookread.R;
import com.wanpg.bookread.common.Config;
import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.logic.FileParseHelper;
import com.wanpg.bookread.manager.ShelfManager;
import com.wanpg.bookread.ui.read.BookReadBaseActivity;
import com.wanpg.bookread.ui.read.txt.BookReadTxtActivity;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.utils.FileUtil;

/**
 * epub格式的书籍阅读界面
 *
 * @author Jinpeng
 */
public class BookReadEpubActivity extends BookReadBaseActivity {

	public ShelfBook bookMode;

	private String mBookName;
	private String mBookPath;
	public String mTmpPath;
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

	/**
	 * 行间距 *
	 */
	public int lineSpace;
	/**
	 * 左右页边距 *
	 */
	public int marginWidth;
	
	/**
	 * 是否打开了夜间模式 *
	 */
	private boolean isMoonOpen;

	private int fontColor;
	public int fontSizeDp;

	/**
	 * 本页文章的开始和结束的字符位置 *
	 */
	private int whichLine, whichChapter, whichIndex;
	public int bookBGPicture;
	public int bookOldBG;

	public EpubPaint epubPaint;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		FileUtil.delFolderChild(new File(Config.BOOK_SD_TEMP));
		super.onDestroy();
	}
	
	
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		initAll();
		super.onRestart();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		commitSharedPreferencesData();//将一些参数写入SharedPreferences
		super.onPause();
	}


	private void initData() {
		mNotice.showWaitDialog("正在加载...");
		new Thread(){
			@Override
			public void run() {
				Intent intent = getIntent();
				Bundle bundle = intent.getExtras();
				bookMode = (ShelfBook) bundle.getSerializable("shelfBookmode");
				mBookPath = bookMode.bookPath;
				mBookName = bookMode.bookName;
				mTmpPath = FileParseHelper.parseEpubPath(mBookPath);
				try {
					EpubKernel.newInstance().openEpubFile(mTmpPath);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mNotice.dismissDialog();
				initAll();
			}
		}.start();
	}

	private void initAll(){
		readSharedPreferencesData();//从SharedPreferences读取所需数据
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				initCanvas();
				initEpubPaint();
				initPageView();
			}
		});
	}

	private void initEpubPaint() {
		if(epubPaint==null){
			epubPaint = new EpubPaint(EpubKernel.getInstance());
			mPaint = epubPaint;
			epubPaint.setReadPos(whichChapter, whichLine, whichIndex);
		}else{
			epubPaint.resetPos();
		}
		epubPaint.setDisWidth(disWidth);
		epubPaint.setDisHeight(disHeight);

		epubPaint.setFontColor(fontColor);//设置画笔字体颜色
		epubPaint.setFontSize(DisplayUtil.dp2px(fontSizeDp));
		bmBG = BitmapFactory.decodeResource(getResources(), bookBGPicture).copy(Bitmap.Config.RGB_565, true);
		epubPaint.setBackBitmap(bmBG);
		epubPaint.setLineSpaceing(lineSpace);
		epubPaint.setMarginWidth(marginWidth);
		epubPaint.setBatteryPercent(isBatteryPercent);
		epubPaint.setShowBottomMsg(isShowBottomMsg);
		epubPaint.setShowHeadMsg(isShowHeadMsg);
		epubPaint.initPaint();//设置画笔风格
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
			disHeight = disHeight - DisplayUtil.getStatusHeight(this);
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
		initEpubPaint();
		epubPaint.setBmBattery(bmBattery, batteryLevel);
		epubPaint.onDrawPage(curPageCanvas);

		mPageView.setBitmaps(curPageBitmap, nextPageBitmap);
		mPageView.onDrawWhenNoTouch();
	}


	public void changeShowMode() {
		// TODO Auto-generated method stub
		initCanvas();
		initEpubPaint();
		initPageView();
	}

	public void changeTheme() {
		// TODO Auto-generated method stub
		if (isMoonOpen) {
			isMoonOpen = false;
			fontColor = getResources().getColor(R.color.black);
		}
		initEpubPaint();
		epubPaint.setBmBattery(bmBattery, batteryLevel);
		epubPaint.onDrawPage(curPageCanvas);

		mPageView.setBitmaps(curPageBitmap, nextPageBitmap);
		mPageView.postInvalidate();
	}


	@Override
	public void readSharedPreferencesData() {
		// TODO Auto-generated method stub
		String configBook = "config" + mBookPath.replace("/", "_");
		SharedPreferences spc = getSharedPreferences(configBook, MODE_PRIVATE);
		whichChapter = spc.getInt("whichChapter", 0);
		whichLine = spc.getInt("whichLine", 0);
		whichIndex = spc.getInt("whichIndex", 0);

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
		if(epubPaint!=null){
			String configBook = "config" + mBookPath.replace("/", "_");
			SharedPreferences spc = getSharedPreferences(configBook, MODE_PRIVATE);
			Editor editor = spc.edit();
			int[] pos = epubPaint.getCurPos();
			editor.putInt("whichChapter", pos[0]);
			editor.putInt("whichLine", pos[1]);
			editor.putInt("whichIndex", pos[2]);
			editor.commit();
		}

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

	public boolean isMoonOpen() {
		return isMoonOpen;
	}


	public void setMoonOpen(boolean isMoonOpen) {
		this.isMoonOpen = isMoonOpen;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == ShelfManager.CATALOG_REQUEST_CODE && resultCode == ShelfManager.CATALOG_RESULT_CODE) {
			Bundle bundle = data.getExtras();
			String chapterId = bundle.getString("chapter_id");
			epubPaint.setPosByChapterid(chapterId);
//			epubPaint.nextPage();
//			epubPaint.prePage();
//			epubPaint.onDrawPage(curPageCanvas);
//			epubPaint.onDrawPage(nextPageCanvas);
//			mPageView.setBitmaps(curPageBitmap, nextPageBitmap);
//			mPageView.onDrawWhenNoTouch();
		}
	}

	@Override
	public void onShowPopWindow(View parent, boolean isFullScreen) {
		// TODO Auto-generated method stub
		new BookReadEpubSetPopWindow(this).openPopWindow(mMainLayout, isFullScreen);
	}

}
