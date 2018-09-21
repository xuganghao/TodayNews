package com.example.a846252219.todaynews.global;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 846252219 on 2018/8/7.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
