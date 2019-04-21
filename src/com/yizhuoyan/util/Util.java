package com.yizhuoyan.util;

import java.text.DecimalFormat;

/**
 * Created by ben on 11/15/18.
 */
public class Util {

    public static final DecimalFormat DECIMAL_FORMAT=new DecimalFormat("###.##");
    private static final double KB=1024,MB=KB*1024,GB=MB*1024;
    static public String formatFileLength(long len){
        if(len<MB){
            return DECIMAL_FORMAT.format(len/KB)+"k";
        }else if(len<GB){
            return DECIMAL_FORMAT.format(len/MB)+"M";
        }else{
            return DECIMAL_FORMAT.format(len/GB)+"G";
        }
    }
    public static void main(String[] ss){
        System.out.println(formatFileLength(102434));
    }

}
