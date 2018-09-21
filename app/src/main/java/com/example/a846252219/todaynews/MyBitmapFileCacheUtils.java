package com.example.a846252219.todaynews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by 846252219 on 2018/8/10.
 */

public class MyBitmapFileCacheUtils {

    private MyBitmapCacheUtils myBitmapCacheUtils;
    private Context context;

    public MyBitmapFileCacheUtils(Context context, MyBitmapCacheUtils myBitmapCacheUtils) {
        this.context=context;
        this.myBitmapCacheUtils=myBitmapCacheUtils;
    }
    //读去图片
    public Bitmap getFileBitmap(String url){
        //1.对图片的URL进行md5，图片在存储的时候就进行了一个md5的操作
        String fileName = MD5Util.Md5(url);
        File file = new File(context.getFilesDir(), fileName);
        //2.读取图片
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        //3.将读取到的图片添加在内存中，方便下一次快速访问
        if (bitmap!=null){
            myBitmapCacheUtils.saveBitmap(url,bitmap);
        }

        return bitmap;
    }
    //存储图片
    public void saveFileBitmap(String url,Bitmap bitmap){
        //1.对图片的URL进行md5，图片在存储的时候就进行了一个md5的操作
        String fileName = MD5Util.Md5(url);

        File file = new File(context.getFilesDir(), fileName);

        //将Bitmap图片写入到文件中
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
