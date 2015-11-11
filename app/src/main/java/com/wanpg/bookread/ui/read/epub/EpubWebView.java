package com.wanpg.bookread.ui.read.epub;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanpg.bookread.BaseActivity;
import com.wanpg.bookread.utils.FileUtil;

public class EpubWebView extends WebView {

	private String epubHtmlFileUrl = "";
	private String basedir = "";
	private Activity activity;
	private int scrollWidth = 0;
	private int totalPage = 0;
	private float anchorPosition = 0;
	private boolean isLoadComplete = false;
	private int curPosition = 0;
	private TextView mTextView = null;

	//private InputStream mInputStream = null;
	
	public EpubWebView(Context context) {
		super(context);
	}

	public EpubWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EpubWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private byte lock_loadUrl[] = new byte[1];

	public void setAct(Activity tHIS){
		this.activity = tHIS;
	}

	public void execJavaScript (String js) {
		super.loadUrl("javascript:"+js);
	}

	public int getTotalPage(){
		return this.totalPage;
	}

	public float getAnchorPostion(){
		return this.anchorPosition;
	}

	public boolean isComplete(){
		return this.isLoadComplete;
	}

	public void setCurWebViewPosition(int position){
		this.curPosition = position;
	}

	@Override
	public void loadUrl(String url) {

		int widthDip = UIHelper.getScreenWidthDip(activity);
		int heightDip = UIHelper.getScreenHeightDip(activity);
		String css = EpubJavaScript.getWebCss(widthDip, heightDip, 10);

		if(!this.epubHtmlFileUrl.equals(url)){
			this.epubHtmlFileUrl = url;
		}
		synchronized (lock_loadUrl) {
			freeMemory(); 
			String file=this.epubHtmlFileUrl.replace("file:///", "");
			this.basedir = this.epubHtmlFileUrl.substring(0,this.epubHtmlFileUrl.lastIndexOf("/")+1);
			String html = "";
			String data = "";
			try {
				byte [] bHtmlData = FileUtil.readFileByBytes(file);
				html = new String(bHtmlData,"UTF-8");

				int pos = html.indexOf("</head>");
				if (pos != -1 ){
					data = html.substring(0, pos)+"\n<meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; minimum-scale=1.0; maximum-scale=1.0\"/>\n<style type='text/css'> \n<!--\n"
							+ css +"\n-->\n </style>\n" + html.substring(pos,html.length()) ;
				}else {
					pos = html.indexOf("<body");
					if(pos != -1){
						data = "<head>"+html.substring(0, pos)+"\n<meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; minimum-scale=1.0; maximum-scale=1.0\"/>\n<style type='text/css'> \n<!--\n"
								+ css +"\n-->\n </style>\n" +"</head>\n" + html.substring(pos,html.length()) ;
					}else{
						data = html;
					}
				}
				super.loadDataWithBaseURL(this.basedir, data, "text/html", "UTF-8", null);
				setEpubProperties();
			} catch (IOException e) {
				e.printStackTrace();
			}    			   
		}
	}

	//set setting of webview
	private void setEpubProperties(){
		//unable scroll bar
		this.setHorizontalScrollBarEnabled(false);
		//enable JavaScript
		this.getSettings().setJavaScriptEnabled(true);
		//add new interface that contract with Javascript
		this.addJavascriptInterface(new JavaScriptInterface(), "Android");
		//set setWebChromeClient object and get onProgressChanged and onConsoleMessage methonds
		this.setWebChromeClient(new EpubWebChromeClient());
		//set WebviewClient object and get onPageFinished methond
		this.setWebViewClient(new EpubWebviewClient());
		//make the webview can't scroll
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

	}
	
	private class EpubWebChromeClient extends WebChromeClient{

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			if(mTextView != null){
				mTextView.setText(newProgress+"%");
				if(newProgress == 100){
					mTextView.setText("99%");
				}
			}
		}

		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			// TODO Auto-generated method stub
			return super.onConsoleMessage(consoleMessage);
		}
	}

	//calling after webview object finish loading html
	private class EpubWebviewClient extends WebViewClient{

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			setProcessBar();
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			//through Javascript to get the scollWidth of webview
			String js = " var scrollWidth = document.documentElement.scrollWidth;" +
					" Android.setScrollWidthInterface(scrollWidth);" +
					//可以用来记忆当前的位置，直接使用scrollTo设置到某x轴
					" window.scrollTo("+EpubWebView.this.curPosition+",0);" +
					" Android.setCompleteInterface();";
			execJavaScript(js);
		}

	} 

	//set process bar
	private void setProcessBar(){
		mTextView = new TextView(activity);
		mTextView.setTextSize(50);
		mTextView.setGravity(Gravity.CENTER);
		EpubWebView.this.addView(mTextView,new LinearLayout.LayoutParams( UIHelper.getScreenWidth(activity),
				UIHelper.getScreenHeight(activity)));
	}

	//set the value of scrollWidth
	private class JavaScriptInterface{
		@JavascriptInterface
		public void setScrollWidthInterface(int scrollWidth){
			EpubWebView.this.scrollWidth = scrollWidth;
			EpubWebView.this.totalPage = (int)Math.ceil(((double)scrollWidth)/UIHelper.getScreenWidthDip(activity));
		}
		
		@JavascriptInterface
		public void setCompleteInterface(){
			EpubWebView.this.isLoadComplete = true;
			//make progress bar disappear
			if(mTextView != null){
				EpubWebView.this.post(new Runnable() {
					@Override
					public void run() {
						removeView(mTextView);
					}
				});
			}
		}
	}

}
