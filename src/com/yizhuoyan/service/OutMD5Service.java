package com.yizhuoyan.service;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Random;



/**
 * Created by ben on 11/15/18.
 */
public class OutMD5Service {
	private static final Random RANDOM=new Random();

    public void doOneFile(File f)throws Exception{
    	
        RandomAccessFile raf=new RandomAccessFile(f,"rw");
        raf.seek(raf.length());
        byte[] bs=new byte[31];
        RANDOM.nextBytes(bs);
        raf.write(bs);
        raf.close();
    }
  
}
