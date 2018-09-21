package com.example.a846252219.todaynews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 846252219 on 2018/8/10.
 */

class MyBitmapNetUtils {
    private Context context;
    //内存缓存
    private MyBitmapCacheUtils myBitmapCacheUtils;
    //文件缓存
    private MyBitmapFileCacheUtils myBitmapFileCacheUtils;

    public MyBitmapNetUtils(Context context,MyBitmapCacheUtils myBitmapCacheUtils, MyBitmapFileCacheUtils myBitmapFileCacheUtils) {
        this.myBitmapCacheUtils=myBitmapCacheUtils;
        this.myBitmapFileCacheUtils=myBitmapFileCacheUtils;
        this.context=context;
    }
//给指定控件设置图片（url）方法
    public void setBitmapImage(ImageView imageView,String imgUrl){
//优先给ImageView设置一张默认图片，让其在异步加载图片的过程中有一张默认图片展示
        imageView.setImageResource(R.mipmap.pic_item_list_default);
        //通过AcyncTask异步下载图片
        new MyAcyncTask().execute(imageView,imgUrl);

    }
    class MyAcyncTask extends AsyncTask<Object,String,Bitmap>{

        private ImageView imageView;
        private String imgUrl;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Object[] params) {
            //因为doInBackground是运行在子线程中的方法，在此方法中异步下载图片，下载完成的图片需要在主线程中给控件展示
            //在下载完图片后，将图片的Bitmap对象作为反回值结果return
            imageView = (ImageView) params[0];
            imgUrl = (String) params[1];

            Bitmap bitmapFromNet = getBitmapFromNet(imgUrl);

            if (bitmapFromNet!=null){
                //将这个图片在内存中存储一份
                myBitmapCacheUtils.saveBitmap(imgUrl,bitmapFromNet);
                //在文件中存储一份
                myBitmapFileCacheUtils.saveFileBitmap(imgUrl,bitmapFromNet);
            }


            //给imgView设置一个标志，此标志就对应着图片的下载链接地址
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageView.setTag(imgUrl);
                }
            });

            return bitmapFromNet;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap!=null){
                //先要找到Bitmap填充的控件
                String url = (String) imageView.getTag();
                if (!TextUtils.isEmpty(url)&&url.equals(imgUrl)){
                    imageView.setImageBitmap(bitmap);
                }
                super.onPostExecute(bitmap);
            }
        }
    }

    private Bitmap getBitmapFromNet(String imgUrl) {
        //HttpURLConnection
        try {
            Log.i("","网络中获取图片");
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(2000);
            connection.setConnectTimeout(2000);
            if (connection.getResponseCode()==200){
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
