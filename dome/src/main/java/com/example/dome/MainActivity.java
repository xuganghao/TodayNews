package com.example.dome;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_get)
    Button btnGet;
    @BindView(R.id.btn_post)
    Button btnPost;
    @BindView(R.id.btn_post_map)
    Button btnPostMap;
    @BindView(R.id.btn_post_json)
    Button btnPostJson;
    @BindView(R.id.btn_post_file)
    Button btnPostFile;
    @BindView(R.id.btn_post_files)
    Button btnPostFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //butterknife 黄油刀 代替findViewById和注册点击事件
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //retrofit 发送网络请求 ，添加retrofit相依的jar包
    }

    @OnClick({R.id.btn_get, R.id.btn_post, R.id.btn_post_map, R.id.btn_post_json, R.id.btn_post_file, R.id.btn_post_files})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                getDate();
                break;
            case R.id.btn_post:
                postDate();
                break;
            case R.id.btn_post_map:
                postMapDate();
                break;
            case R.id.btn_post_json:
                postJsonDate();
                break;
            case R.id.btn_post_file:
                postFile();
                break;
            case R.id.btn_post_files:
                postMapFiles();
                break;
        }
    }

    private void postMapFiles() {
        //准备多张图片
        FileOutputStream fileOutputStream = null;
        FileOutputStream fileOutputStream1 = null;
        try {
            File file = new File(getFilesDir(), "image1");
            fileOutputStream = new FileOutputStream(file);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.a);
            //将bitmap写入指定的文件方法
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);


            File file1 = new File(getFilesDir(), "image2");
            fileOutputStream1 = new FileOutputStream(file1);
            Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.b);
            //将bitmap写入指定的文件方法
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream1);

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://httpbin.org")
                    .build();
            ResponseInfoApi responseInfoApi = retrofit.create(ResponseInfoApi.class);

            //创建RequestBoy,用于封装RequestBody
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            //创建RequestBody,用于封装构建RequestBody
            RequestBody requestFile1 =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file1);

            //
            HashMap<String, RequestBody> requestBodyMap = new HashMap<>();
            requestBodyMap.put("ectimg", requestFile);
            requestBodyMap.put("listimage", requestFile1);;

            Call<ResponseBody> call = responseInfoApi.uploadMapFiles(requestBodyMap);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        ResponseBody responseBody = response.body();
                        String json = responseBody.string();
                        Log.i("retrofit", "post map files =" + json);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("retrofit", "请求失败");
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void postFile() {
        try {
            File file = new File(getFilesDir(), "image");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.a);
            //将bitmap写入指定的文件方法
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            //2.将图片上传到服务器
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://httpbin.org")
                    .build();
            ResponseInfoApi responseInfoApi = retrofit.create(ResponseInfoApi.class);
            //创建RequestBody,用于封装RequestBody
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/from-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("actimg", file.getName(), requestFile);
            Call<ResponseBody> call = responseInfoApi.uploadFile(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        ResponseBody responseBody = response.body();
                        String json = responseBody.string();
                        Log.i("retrofit", "post file =" + json);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("retrofit", "请求失败");
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void postJsonDate() {
        //生产一段Json
        /*
        {
        "name","student"
        "age"，"18"
        "sex","man"
        .......
               }
        * **/
        Student student = new Student("student", "18", "man", "100001");
        Gson gson = new Gson();
        String json = gson.toJson(student);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://httpbin.org")
                .build();
        ResponseInfoApi responseInfoApi = retrofit.create(ResponseInfoApi.class);
        //需要将Json串封装在requestBady中，才可以发送给服务器
        final ResponseBody responseBody = ResponseBody.create(MediaType.parse("application/json;charset=utf-8"), json);
        Call<ResponseBody> call = responseInfoApi.postJson(responseBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ResponseBody responseBody = response.body();
                    String json = responseBody.string();
                    Log.i("retrofit", "post json =" + json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("retrofit", "请求失败");
            }
        });
    }

    private void postMapDate() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://httpbin.org")
                .build();
        ResponseInfoApi responseInfoApi = retrofit.create(ResponseInfoApi.class);
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("name", "teacher");
        paramMap.put("age", "20");
        paramMap.put("sex", "man");
        paramMap.put("userid", "10000");
        Call<ResponseBody> call = responseInfoApi.postMapDate(paramMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ResponseBody responseBody = response.body();
                    String json = responseBody.string();
                    Log.i("retrofit", "post map json =" + json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("retrofit", "请求失败");
            }
        });
    }

    private void postDate() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://httpbin.org")
                .build();
        ResponseInfoApi responseInfoApi = retrofit.create(ResponseInfoApi.class);
        Call<ResponseBody> call = responseInfoApi.postDate("teacher", "18");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    ResponseBody responseBody = response.body();
                    String json = responseBody.string();
                    Log.i("retrofit", "post json =" + json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.i("retrofit", "请求失败");
            }
        });
    }

    public void getDate() {
        Retrofit retrofit = new Retrofit.Builder()
                //服务器返回数据json,告知服务器返回的json数据通过什么gson解析
                .addConverterFactory(GsonConverterFactory.create())
                //约定请求链接地址前半部分
                // http://www.itheima.com//todaynew/home.jsp  获取首页数据
                // http://www.itheima.com//todaynew/user.jsp  获取个人用户数据
                .baseUrl("http://httpbin.org")
                .build();
        //通过 retrofit发送网络请求
        //完整请求链接 请求方式（get post） 请求参数  请求结果
        ResponseInfoApi responseInfoApi = retrofit.create(ResponseInfoApi.class);
        Call<ResponseBody> call = responseInfoApi.getDate("teacher");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    ResponseBody responseBody = response.body();
                    String json = responseBody.string();
                    Log.i("retrofit", "json =" + json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("retrofit", "请求失败");
            }
        });

    }
}
