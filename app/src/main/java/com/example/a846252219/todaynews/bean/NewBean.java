package com.example.a846252219.todaynews.bean;

/**
 * Created by 846252219 on 2018/6/22.
 */

public class NewBean {
    private int id;
    private String title;
    private int type;
    private String url;

    public NewBean(int id, String title, int type, String url) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
