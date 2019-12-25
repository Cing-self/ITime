package com.example.caijinhong.itime.data;

import android.graphics.Bitmap;

/**
 * Created by cai jin hong on 2019/11/12.
 */

public class TimeItem {
    private String title;
    private String remark;
    private Bitmap image;
    private String time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TimeItem(String title, String remark, Bitmap image, String time) {
        this.title = title;
        this.remark = remark;
        this.image = image;
        this.time = time;
    }
}
