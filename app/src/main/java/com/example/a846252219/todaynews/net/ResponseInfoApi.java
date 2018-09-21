package com.example.a846252219.todaynews.net;

import com.example.a846252219.todaynews.bean.ResponseInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by 846252219 on 2018/6/21.
 */

public interface ResponseInfoApi {
    //首页请求数据的方法
    @GET("home.json")
    Call<ResponseInfo>getHomeData();

    //也签约请求数据的方法
    @GET("{dirPath}/{filename}")
    Call<ResponseInfo> getNewTabData(@Path("dirPath") String dirPath,
                                    @Path("filename") String filename);

    @GET("{dirPath}/{filename}")
    Call<ResponseInfo> getPicData(@Path("dirPath") String dirPath,@Path("filename") String filename);
}
