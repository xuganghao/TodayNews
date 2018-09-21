package com.example.a846252219.todaynews;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 维护一个lruCache储存和读取图片，保证内存不溢出
 */

public class MyBitmapCacheUtils {

    private LruCache<String, Bitmap> stringBitmapLruCache;

    public MyBitmapCacheUtils(){
       //1.创建一个LruChche对象,后面的括号是指定LruCache集合最大的可用空间
       int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        stringBitmapLruCache = new LruCache<String, Bitmap>(maxSize){


            @Override
            protected int sizeOf(String key, Bitmap value) {
                //每一张存入map的图片都需要知道图片大小
                //获取每一行中像素所占的大小 * 行数 = 图片大小
                return value.getRowBytes() * value.getHeight();
            }
        };
   }

    /**
     * @param key      图片链接地址
     * @param bitmap   图片对象
     *                 在内存中储存一张图片
     */
   public void saveBitmap(String key, Bitmap bitmap){
        stringBitmapLruCache.put(key,bitmap);
   }

    /**
     * @param key     图片链接地址
     * @return        图片bitmap对象
     */
   public Bitmap getBitmap(String key){
       return stringBitmapLruCache.get(key);
   }
}
