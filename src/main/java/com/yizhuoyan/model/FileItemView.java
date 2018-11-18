package com.yizhuoyan.model;

import com.yizhuoyan.util.Util;

import java.io.File;
import java.io.Serializable;

/**
 * Created by ben on 11/18/18.
 */
public class FileItemView implements Comparable<FileItemView>,Serializable{
    private final File file;
    private final int index;
    private int donePercent;

    public FileItemView(int index,File file) {
        this.file = file;
        this.index = index;
    }

    public File getFile() {
        return file;
    }



    public int getIndex() {
        return index;
    }



    public double getDonePercent() {
        return donePercent;
    }

    public void setDonePercent(int donePercent) {
        this.donePercent = donePercent;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null)return false;
        if(obj instanceof FileItemView){

            return file.equals(((FileItemView) obj).file);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return file.hashCode();
    }

    @Override
    public int compareTo(FileItemView o) {
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(index+1).append(") ")
                .append('(')
                .append(donePercent)
                .append("%)")
                .append(file.getPath())
                .append('(')
                .append(Util.formatFileLength(file.length()))
                .append(')');
        return sb.toString();
    }
}
