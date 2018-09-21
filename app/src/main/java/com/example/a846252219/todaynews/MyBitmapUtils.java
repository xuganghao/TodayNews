package com.example.a846252219.todaynews;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by 846252219 on 2018/8/10.
 */

public class MyBitmapUtils {

    private MyBitmapCacheUtils myBitmapCacheUtils;
    private MyBitmapFileCacheUtils myBitmapFileCacheUtils;
    private MyBitmapNetUtils myBitmapNetUtils;

    public MyBitmapUtils(Context context){
        //1.内存缓存的工具类
        myBitmapCacheUtils = new MyBitmapCacheUtils();
        //2.文件缓存的工具类
        myBitmapFileCacheUtils = new MyBitmapFileCacheUtils(context,myBitmapCacheUtils);
        //3.网络缓存的工具类
        myBitmapNetUtils = new MyBitmapNetUtils(context,myBitmapCacheUtils,myBitmapFileCacheUtils);
    }
    //先从内存缓存中取图片，取不到就去文件缓存取图片，取不到就去网络下载图片
    public void setImageBitmap(ImageView imageView,String imgUrl){
       //1.内存缓存取图片
        Bitmap bitmap = myBitmapCacheUtils.getBitmap(imgUrl);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
            Log.i("","内存中获取图片");
            return;
        }
        //2.文件中获取图片
        Bitmap fileBitmap = myBitmapFileCacheUtils.getFileBitmap(imgUrl);
        if (fileBitmap!=null){
            imageView.setImageBitmap(fileBitmap);
            Log.i("","文件中获取图片");
            return;
        }
        //3.网络中获取图片
        myBitmapNetUtils.setBitmapImage(imageView,imgUrl);

    }
}
