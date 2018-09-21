package com.example.a846252219.todaynews.presenter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.example.a846252219.todaynews.Constant;
import com.example.a846252219.todaynews.bean.ResponseInfo;
import com.example.a846252219.todaynews.net.ResponseInfoApi;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 用于封装所有 网络
 */

public abstract class BasePresenter {
    private HashMap<Integer, String> errorMap = null;
    ResponseInfoApi responseInfoApi;

    @SuppressLint("UseSparseArrays")
    BasePresenter() {
        errorMap = new HashMap<>();
        errorMap.put(404, "请求链接地址无效");
        errorMap.put(500, "请求链接地址缺失");
        String a = (Constant.BASEURL).replaceAll(Constant.BASEURL1, Constant.BASEURL5);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(a)
                .build();
        responseInfoApi = retrofit.create(ResponseInfoApi.class);
//        Call<ResponseInfo> call = rersenterInfoApi.getHomeData();
//        call.enqueue(new Callback<ResponseInfo>() {
//            @Override
//            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseInfo> call, Throwable t) {
//
//            }
//        });
    }

    //声明一个处理请求成功失败的内部类
    class CallbackAdapter implements Callback<ResponseInfo> {

        @Override
        public void onResponse(@NonNull Call<ResponseInfo> call, @NonNull Response<ResponseInfo> response) {
            //服务器返回的json数据，封装的response
            ResponseInfo responseInfo = response.body();
            assert responseInfo != null;
            int retcode = responseInfo.getRetcode();
            //response为200时，data的数据才有意义
            if (retcode == 200) {
                String json = responseInfo.getData();
                //json解析，展示，不同的页面返回的json会有差距，所以无法在此处具体的json解析
                //具体的解析过程交由具体的子类来进行处理
                parseJson(json);
            } else {
                //服务器返回的retcode的值为多少.决定具体的异常
                String errorMessage = errorMap.get(retcode);
                //自定义一个异常类型，用于告知此异常是通过状态码获取出来的

                onFailure(call, new RuntimeException(errorMessage));
            }
        }

        @Override
        public void onFailure(@NonNull Call<ResponseInfo> call, @NonNull Throwable t) {
            if (t instanceof RuntimeException) {
                //将异常获取出来，交由子类处理
                showErrorMessage(t.getMessage());
            }
            showErrorMessage("服务器忙，请稍后重试");
        }
    }

    protected abstract void showErrorMessage(String s);

    protected abstract void parseJson(String json);
}
