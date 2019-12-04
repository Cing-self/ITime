package com.example.caijinhong.itime.data;

/**
 * Created by cai jin hong on 2019/11/12.
 */

public class TimeItem {
    private String name;
    private double price;
    private int pictureId;

    public TimeItem(String name, double price, int pictureId) {
        this.name = name;
        this.price = price;
        this.pictureId = pictureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }
}
