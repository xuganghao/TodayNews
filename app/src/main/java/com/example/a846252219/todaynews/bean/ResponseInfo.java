package com.example.a846252219.todaynews.bean;

/**
 * Created by 846252219 on 2018/6/21.
 */

public class ResponseInfo {
    private int retcode;
    private String data;

    public ResponseInfo(int retcode, String data) {
        this.retcode = retcode;
        this.data = data;
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
