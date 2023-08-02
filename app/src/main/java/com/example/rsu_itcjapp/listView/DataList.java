package com.example.rsu_itcjapp.listView;

public class DataList {

    private String name;
    private int iconImage;

    public DataList(String name, int iconImage) {
        this.name = name;
        this.iconImage = iconImage;
    }

    public String getName() {
        return name;
    }

    public int getIconImage() {
        return iconImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIconImage(int iconImage) {
        this.iconImage = iconImage;
    }
}
