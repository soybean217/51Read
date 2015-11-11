package com.wanpg.bookread.ui.read.epub;

public class EpubJavaScript {

	
	public static String getWebCss(int w,int h,int marginSpace){
		
		//该CSS决定了epub的html内容“分屏排版
		return " html { "
              + " padding: "+marginSpace+"px 0px "+marginSpace+"px "+marginSpace+"px; height: "+(h-marginSpace*2)+"px; "
              + " -webkit-column-gap: "+marginSpace+"px; "
              + " -webkit-column-width: "+(w-marginSpace)+"px;"
              + " width : "+(w-marginSpace)+"px;"
              + "}"
          	  + " img {"
              + " display: block;"
              + " margin-left: auto;"
              + " margin-right: auto;"
              + " max-height: 100% !important;"
              + " max-width: 100% !important;"
              + " height : auto !important;"
              + "}";
	}
}
