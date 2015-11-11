package com.wanpg.bookread.utils;

/**
 * Created by Jinpeng on 13-7-19.
 */
public class TextUtil {

    public static boolean isEmpty(String str){
        if(str == null || str.equals("")) return true;
        return false;
    }

    public static boolean isString2Int(String str){
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
    
    public static int obj2int(Object object){
    	try {
    		return Integer.parseInt(object.toString());
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
    }
}
