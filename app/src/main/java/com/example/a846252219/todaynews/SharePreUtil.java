package com.example.a846252219.todaynews;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 846252219 on 2018/6/26.
 */

public class SharePreUtil {

    private static SharedPreferences config;

    public static String getString(Context context,String key,String defValue){
        if (config==null) {
            config = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
       return config.getString(key,defValue);
    }
    public static void saveString(Context context,String key, String value){
        if (config==null) {
            config = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
     config.edit().putString(key,value).commit();
    }
}
