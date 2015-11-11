package com.wanpg.bookread.utils;

import android.os.Environment;

/**
 * Created by Jinpeng on 13-12-22.
 */
public class SDCardUtil {

    public static boolean isSDCardAvailable() {
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

}
