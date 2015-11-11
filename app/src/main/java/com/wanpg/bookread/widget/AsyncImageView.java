package com.wanpg.bookread.widget;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wanpg.bookread.R;
import com.wanpg.bookread.manager.ImageManager;

public class AsyncImageView extends ImageView {

    public static final int IMAGE_CORE_POOL_SIZE = 8;
    public static ExecutorService IMAGE_THREAD_POOL = Executors.newFixedThreadPool(IMAGE_CORE_POOL_SIZE);
    private String imageUrl;

    private int w = 0;
    private int h = 0;

    public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public AsyncImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        
    }

    
    public void setImageUrl(String imageUrl, int resId) {
    	setImageUrl(imageUrl, resId, 0, 0);
	}
    
    public void setImageUrl(String imageUrl) {
    	setImageUrl(imageUrl, R.drawable.ic_launcher);
	}
    
    /**
     * 设置默认图片的资源id和目标url
     *
     * @param _resId
     * @param _imageUrl
     */
    public void setImageUrl(String imageUrl, int resId, int w, int h) {
        this.imageUrl = imageUrl;
        this.w = w;
        this.h = h;
        Bitmap bmCache = ImageManager.getBitmapByKey(imageUrl);
        if (bmCache != null) {
            this.setImageBitmap(bmCache);
        } else {
            this.setImageResource(resId);
            startDownload();
        }
    }

    private void startDownload() {
        IMAGE_THREAD_POOL.submit(new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                final Bitmap bm = getImage(imageUrl);
                if (bm != null) {
                    AsyncImageView.this.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							AsyncImageView.this.setImageBitmap(bm);
						}
					});
                }
            }
        });
    }


    /**
     * 得到图片，首先判断缓冲区
     */
    private Bitmap getImage(String imageUrl) {
        Bitmap bitmap = ImageManager.getBitmapByKey(imageUrl);
        if (bitmap != null) {
            return bitmap;
        } else {
            bitmap = getHttpBitmap(imageUrl);
            if (bitmap != null) {
                if (w != 0 && h != 0) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
                }
                ImageManager.putBitmapToCache(bitmap, imageUrl);
                ImageManager.saveBitmapToFile(bitmap, imageUrl);
                return bitmap;
            } else {
                return null;
            }
        }
    }


    /**
     * 获取网络图片资源
     *
     * @param url
     * @return
     */
    private Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

}
