package com.example.a846252219.todaynews.presenter;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

import com.example.a846252219.todaynews.adapter.PicAdapter;
import com.example.a846252219.todaynews.bean.PicBean;
import com.example.a846252219.todaynews.bean.ResponseInfo;
import com.google.gson.Gson;

import retrofit2.Call;

/**
 * Created by 846252219 on 2018/8/7.
 */

public class picPresenter extends BasePresenter{

    private Activity activity;
    private ListView lvPic;
    private PicBean picBean;

    public picPresenter(Activity activity,ListView lvPic) {
        this.activity=activity;
        this.lvPic=lvPic;
    }

    @Override
    protected void showErrorMessage(String s) {
        Log.i("","请求有异常");
    }

    @Override
    protected void parseJson(String json) {
    //    Log.i("","请求成功 json="+json);
        Gson gson = new Gson();
        picBean = gson.fromJson(json,PicBean.class);

        //把数据放在数据适配器中
        PicAdapter picAdapter = new PicAdapter(activity,picBean.getNews());
        lvPic.setAdapter(picAdapter);
    }
    public void getPicData(){
        Call<ResponseInfo> call = responseInfoApi.getPicData("photos", "photos_1.json");
        call.enqueue(new CallbackAdapter());
    }
}
