package com.example.dome;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by 846252219 on 2018/6/20.
 */

public interface ResponseInfoApi {
     //完整请求链接 请求方式（get post） 请求参数  请求结果
    //http://httpbinn.org/get?name="老师"
    @GET("get")
    Call<ResponseBody> getDate(@Query("name") String name);
     //http://httpbin.org/post?name="teacher"&age="18"
    //表单上传数据
    @FormUrlEncoded
    @POST("post")
    Call<ResponseBody> postDate(@Field("name") String name,@Field("age") String age);
    //以map的形式给服务器上传数据
    @FormUrlEncoded
    @POST("post")
    Call<ResponseBody> postMapDate(@FieldMap()Map<String,String > map);

    @Headers({"Conent-type: application/json","Accept:application/json"})
    @POST("post")
    Call<ResponseBody> postJson(@Body ResponseBody body);

    //上传图片的方法
    @Multipart
    @POST("post")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file);

    //上传多张图片的方法
    @Multipart
    @POST("post")
    Call<ResponseBody> uploadMapFiles(@PartMap()Map<String, RequestBody>maps);

}
