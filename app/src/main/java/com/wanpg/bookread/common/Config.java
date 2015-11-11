package com.wanpg.bookread.common;

import android.os.Environment;

/**
 * 软件的一些参数
 *
 * @author wanpg
 */
public class Config {

    /**
     * 阅读设置的SharedPreferences文件名 *
     */
    public final static String CONFIG_READ = "config_read";
    /**
     * 软件设置的SharedPreferences文件名 *
     */
    public final static String CONFIG_SOFT = "config_soft";

    /**
     * 翻页效果无*
     */
    public final static String PAGE_CHANGE_NONE = "none";
    /**
     * 翻页效果仿真*
     */
    public final static String PAGE_CHANGE_FLIP = "flip";
    /**
     * 翻页效果横向滑动*
     */
    public final static String PAGE_CHANGE_SLIDE = "slide";
    /**
     * 翻页效果横向覆盖*
     */
    public final static String PAGE_CHANGE_COVER = "cover";


    /**
     * 软件背景_系统自带 *
     */
    public final static String SOFT_SKIN_SYSTEM = "skin_system";
    /**
     * 软件背景_用户自定义 *
     */
    public final static String SOFT_SKIN_CUSTOM = "skin_custom";


    /**
     * 程序的SDCARD的路径 *
     */
    public static String BOOK_SD = Environment.getExternalStorageDirectory() + "/51read";
    /**
     * 书籍的文件夹路径 *
     */
    public static String BOOK_SD_BOOK = BOOK_SD + "/book";
    /**
     * 书籍封面的文件夹路径 *
     */
    public static String BOOK_SD_COVER = BOOK_SD + "/cover";
    /**
     * 软件的临时文件路径 *
     */
    public static String BOOK_SD_TEMP = BOOK_SD + "/tmp";
    
    /**
     * 手机的IMEI码，电信手机会在此码后加字符"c"，共15位 *
     */
    public static String IMEI;

    /**
     * 易查的搜索url
     */
    public static final String YICHA_SEARCH_URL = "http://tbook.yicha.cn/tb/ss.y?at=manual&key=";

}
