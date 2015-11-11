package com.wanpg.bookread.widget;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wanpg.bookread.common.ShuPengConfig;
import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.manager.ImageManager;
import com.wanpg.bookread.ui.read.epub.EpubKernel;
import com.wanpg.bookread.utils.FileUtil;

public class AsyncShelfImageView extends ImageView {

	public AsyncShelfImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public AsyncShelfImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public AsyncShelfImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	private ShelfBook mBook;
	private String mImageUrl = "";
	private String mBookPath = "";
	/**
	 * 设置默认图片的资源id和目标url
	 * @param _resId
	 * @param _adapter
	 * @param _pos
	 */
	public void setCover(int resId, ShelfBook book) {
		this.mBook = book;
		mImageUrl = ShuPengConfig.BOOK_COVER_MAIN_R + mBook.thumb;
		mBookPath = mBook.bookPath;
		bitmap = ImageManager.getBitmapByPath(mBook.coverPath);
		if(bitmap==null){
			if(mBook.readMode.equals(ShelfBook.POS_LOCAL_SDCARD)){
				bitmap = ImageManager.getBitmapByKey(mBookPath);
			}else{
				bitmap = ImageManager.getBitmapByKey(mImageUrl);
			}
			if (bitmap != null) {
				this.setImageBitmap(bitmap);
			} else {
				this.setImageResource(resId);
				startDownload();
			}
		}else{
			this.setImageBitmap(bitmap);
		}
	}

	private Bitmap bitmap;
	public void startDownload() {

		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mBook.readMode.equals(ShelfBook.POS_LOCAL_SDCARD)) {
					String bookType = FileUtil.getFileType(mBookPath);

					if (bookType.equals("epub")) {
						try {
							EpubKernel epubKernel = EpubKernel.newInstance();
							epubKernel.openEpubFile(mBookPath);
							bitmap = BitmapFactory.decodeFile(epubKernel.getCoverPath());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
					if(bitmap!=null){
						ImageManager.putBitmapToCache(bitmap, mBookPath);
						ImageManager.saveBitmapToFile(bitmap, mBookPath);
					}
				} else if (mBook.readMode.equals(ShelfBook.POS_ONLINE) || mBook.readMode.equals(ShelfBook.POS_LOCAL_SHUPENG)) {
					bitmap = getHttpBitmap(mImageUrl); 
					if(bitmap!=null){
						ImageManager.putBitmapToCache(bitmap, mImageUrl);
						ImageManager.saveBitmapToFile(bitmap, mImageUrl);
					}
				}

				if(bitmap!=null){
					post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AsyncShelfImageView.this.setImageBitmap(bitmap);
						}
					});
				}
			}
		}.start();
	}


	/**
	 * 获取网络图片资源
	 *
	 * @param url
	 * @return
	 */
	public Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap bitmap = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		try {
			myFileURL = new URL(url);
			//获得连接
			conn = (HttpURLConnection) myFileURL.openConnection();
			//设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			//连接设置获得数据流
			conn.setDoInput(true);
			//不使用缓存
			conn.setUseCaches(false);
			//这句可有可无，没有影响
			conn.connect();
			//得到数据流
			is = conn.getInputStream();
			//解析得到图片
			//保存到本地
			return BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
}
