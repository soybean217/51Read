package com.wanpg.bookread.utils;

import java.text.Collator;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class Util {

    /**
     * 将秒数时间改为目标格式yyyy-MM-dd hh:mm
     *
     * @param millis
     * @return
     */
    public static String changeMillisToDate(long millis) {
//		String s="Tue, 22 January 2013 10:05:18 +0800";
//		Date date1=null;
//		//设置读取字符串的日期格式
//		SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss Z",Locale.US);
//		try {
//			//读取给定的字符串
//			date1=sdf.parse(s);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        Date date1 = new Date(millis);
        //设定目标字符串的日期格式
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //格式化日期
        String date2 = sdf1.format(date1);
        return date2;
    }

    /**
     * 将所给数组进行首字母排序
     *
     * @param arrays
     * @return String[]
     */
    public static String[] sortArrays(String[] arrays) {
        /*设置语言环境*/
        Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
        Arrays.sort(arrays, com);
        return arrays;
    }


    /**
     * @param x 分子
     * @param y 分母
     * @return 0.00%的字符串
     */
    public static String countPercent(int x, int y) {
        double a = x;
        double b = y;
        String s = "0.00%";
        if (a > 0) {
            double c = a / b;
            //获取格式化对象
            NumberFormat nt = NumberFormat.getPercentInstance();
            //设置百分数精确度2即保留两位小数
            nt.setMinimumFractionDigits(2);
            s = nt.format(c);
        }
        return s;
    }


    /**
     * 获取系统时间并格式化为HH:MM的24小时制
     */
    public static String getSysTime() {
        String strTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        strTime = sdf.format(new Date());
        return strTime;
    }

    /**
     * 获取系统时间并格式化为yyyy-MM-dd HH:mm
     *
     * @return
     */
    public static String getSysDateTime() {
        String strTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        strTime = sdf.format(new Date());
        return strTime;
    }

    /**
     * 将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
     *
     * @param s 原文件名
     * @return 重新编码后的文件名
     */
    public static String toUtf8String(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        try {
            char c;
            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);
                if (c >= 0 && c <= 255) {
                    sb.append(c);
                } else {
                    byte[] b;
                    b = Character.toString(c).getBytes("utf-8");
                    for (int j = 0; j < b.length; j++) {
                        int k = b[j];
                        if (k < 0)
                            k += 256;
                        sb.append("%" + Integer.toHexString(k).toUpperCase());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


}
