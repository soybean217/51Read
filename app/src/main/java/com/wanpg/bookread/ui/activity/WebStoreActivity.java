package com.wanpg.bookread.ui.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanpg.bookread.BaseActivity;
import com.wanpg.bookread.R;
import com.wanpg.bookread.common.ShuPengConfig;
import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.db.DaoManager;
import com.wanpg.bookread.manager.ShelfManager;

public class WebStoreActivity extends BaseActivity {
    WebView webView;
    LinearLayout ll_progress_bar;
    /**
     * 注意: JS_INTERFACE = "jsInterface" 为固定值，不准修改
     */
    private static final String JS_INTERFACE = "jsInterface";


    private ImageButton ib_home, ib_previous, ib_next, ib_refresh;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case 1:
                        ll_progress_bar.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
            }
        };
        initData();
    }

    private void initData() {
        // TODO Auto-generated method stub
        Intent intent = getIntent();
        String href = intent.getStringExtra("HREF");
        Log.d("web_url", href);
        initUI(href);
    }

	private void initUI(String href) {
        // TODO Auto-generated method stub
        ((TextView) findViewById(R.id.tv_title)).setText("在线阅读");
        setBackEvent(findViewById(R.id.ib_back));
        ib_home = (ImageButton) findViewById(R.id.web_home);
        ib_previous = (ImageButton) findViewById(R.id.web_previous);
        ib_next = (ImageButton) findViewById(R.id.web_next);
        ib_refresh = (ImageButton) findViewById(R.id.web_refresh);

        ib_home.setOnClickListener(new MyClickListener());
        ib_previous.setOnClickListener(new MyClickListener());
        ib_next.setOnClickListener(new MyClickListener());
        ib_refresh.setOnClickListener(new MyClickListener());


        webView = (WebView) findViewById(R.id.wv_main);
        ll_progress_bar = (LinearLayout) findViewById(R.id.ll_progress_bar);
        ll_progress_bar.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.requestFocus(View.FOCUS_DOWN);
        /**
         * 注意： JS_INTERFACE 为固定值，不准修改
         */
        webView.addJavascriptInterface(new JavaScriptInterface(), JS_INTERFACE);

        webView.setWebViewClient(new MyWebViewClient(handler));

        webView.setWebChromeClient(new WebChromeClient() {

            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                Log.d("MyApplication", message + " -- From line " + lineNumber + " of " + sourceID);
            }

        });
        webView.loadUrl(href);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private class MyClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.equals(ib_home)) {
                webView.loadUrl(ShuPengConfig.SHUPENG_URL);
            } else if (v.equals(ib_previous)) {
                webView.goBack();
            } else if (v.equals(ib_next)) {
                webView.goForward();
            } else if (v.equals(ib_refresh)) {
                webView.reload();
            }
        }

    }

    private void setArrows() {
        // TODO Auto-generated method stub
        if (webView.canGoBack()) {
            ib_previous.setClickable(true);
            ib_previous.setImageResource(R.drawable.arrow_previous);
        } else {
            ib_previous.setClickable(false);
            ib_previous.setImageResource(R.drawable.arrow_previous_disable);
        }


        if (webView.canGoForward()) {
            ib_next.setClickable(true);
            ib_next.setImageResource(R.drawable.arrow_next);
        } else {
            ib_next.setClickable(false);
            ib_next.setImageResource(R.drawable.arrow_next_disable);
        }
    }

    /**
     * 重要的回调类 调用js 获得当前阅读页的json信息 具体json 格式对应
     * http://code.google.com/p/shupeng-api/downloads/list
     */
    class JavaScriptInterface {
    	@JavascriptInterface
    	public String setBookInfo(String json) {
            System.out.println("json:" + json);
            JSONObject jo;
            JSONObject bookJo;
            try {
                jo = new JSONObject(json);
                bookJo = jo.getJSONObject("book");
                ShelfBook b = new ShelfBook();
                String chapterId = jo.getString("id");
                int bookId = bookJo.getInt("id");
                Log.d("chapterId", chapterId);
                //此部分为公共
                b.readMode = ShelfBook.POS_ONLINE;
                b.bookName = bookJo.getString("name");
                b.posInShelf = 1;

                //此部分为本地书籍信息，
                b.bookPath = "";
                b.coverPath = "";
                b.author = bookJo.getString("author");


                //此部分为网络书籍信息
                b.thumb = bookJo.getString("thumb");
                b.bookId = bookId;
                b.url = ShuPengConfig.SHUPENG_URL + "#/book/" + bookId;
                b.chapterId = chapterId;
                b.chapterUrl = ShuPengConfig.SHUPENG_URL + "#/read/" + chapterId;
                
                DaoManager.getInstance().getShelfDao().saveOrUpdateOneBook(b);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }
    }


    private class MyWebViewClient extends WebViewClient {

        private static final String TAG = "web";
        private Handler mHandler;

        public MyWebViewClient(Handler handler) {
            mHandler = handler;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("url : " + url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            System.out.println("finish url : " + url);
            mHandler.sendEmptyMessage(1);
            setArrows();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            System.out.println("start url : " + url);
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
            Log.i(TAG, "onScaleChanged " + oldScale + ": " + newScale);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.i(TAG, "onReceivedError ");
        }
    }

}
