package com.wanpg.bookread.utils;

import java.text.NumberFormat;

public class MathUtil {
    
    /**
     * 计算百分数
     * @param x 分子
     * @param y 分母
     * @param maxNumPoint 精确到小数点后几位
     * @return
     */
    public static String countPercent(double x, double y, int maxNumPoint) {
        String s = "0%";
        if (x > 0) {
            double c = x / y;
            //获取格式化对象
            NumberFormat nt = NumberFormat.getPercentInstance();
            //设置百分数精确度2即保留两位小数
            nt.setMinimumFractionDigits(0);
            nt.setMaximumFractionDigits(maxNumPoint);
            s = nt.format(c);
        }
        return s;
    }
    
    /**
     * 计算百分数
     * @param x 分子
     * @param y 分母
     * @param maxNumPoint 精确到小数点后几位
     * @return
     */
    public static String countPercent(long x, long y, int maxNumPoint) {
    	double a = x;
    	double b = y;
        String s = "0%";
        if (a > 0) {
            double c = a / b;
            //获取格式化对象
            NumberFormat nt = NumberFormat.getPercentInstance();
            //设置百分数精确度2即保留两位小数
            nt.setMinimumFractionDigits(0);
            nt.setMaximumFractionDigits(maxNumPoint);
            s = nt.format(c);
        }
        return s;
    }
    
    public static String countFileSize(long fileSize){
    	String type = "KB";
    	float num = 0f;
    	String s = "0";
    	if(fileSize>0){
	    	if(fileSize % (1024 * 1024) >0){
	    		num = fileSize / 1024f / 1024f;
	    		type = "MB";
	    	}else{
	    		num = fileSize / 1024f;
	    		type = "KB";
	    	}
	    	NumberFormat nt = NumberFormat.getIntegerInstance();
            //设置百分数精确度2即保留两位小数
            nt.setMinimumFractionDigits(0);
            nt.setMaximumFractionDigits(2);
            s = nt.format(num);
    	}
    	return s + type;
    }
    
}
