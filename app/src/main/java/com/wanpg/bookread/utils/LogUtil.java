package com.wanpg.bookread.utils;

/**
 * Created by Jinpeng on 13-7-19.
 */
public class LogUtil {

    static boolean isDebug = true;

    public static void D(String tag, String info){
        if(isDebug && !TextUtil.isEmpty(info)){
            android.util.Log.d(tag, info);
        }
    }

    public static void E(String tag, String info){
        if(isDebug && !TextUtil.isEmpty(info)){
            android.util.Log.e(tag, info);
        }
    }

    public static void I(String tag, String info){
        if(isDebug && !TextUtil.isEmpty(info)){
            android.util.Log.i(tag, info);
        }
    }

    public static void V(String tag, String info){
        if(isDebug && !TextUtil.isEmpty(info)){
            android.util.Log.v(tag, info);
        }
    }

    public static void W(String tag, String info){
        if(isDebug && !TextUtil.isEmpty(info)){
            android.util.Log.w(tag, info);
        }
    }

}
