package com.yizhuoyan.service;

import java.io.File;
import java.io.RandomAccessFile;



/**
 * Created by ben on 11/15/18.
 */
public class OutMD5Service {

    public final String SIGN_CHARS = "yi";

    public void doOneFile(File f)throws Exception{

        RandomAccessFile raf=new RandomAccessFile(f,"rw");
        raf.seek(raf.length());
        raf.write(SIGN_CHARS.getBytes("iso8859-1"));
        raf.close();
    }
    public static void main(String[] ss)throws Exception{
        new OutMD5Service().doOneFile(new File("/home/ben/Videos/1.txt"));

    }
}
