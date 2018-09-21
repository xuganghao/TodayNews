package com.example.a846252219.todaynews.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a846252219.todaynews.Constant;
import com.example.a846252219.todaynews.SharePreUtil;
import com.example.a846252219.todaynews.activity.MainActivity;
import com.example.a846252219.todaynews.activity.NewDetailActivity;
import com.example.a846252219.todaynews.adapter.NewsAdapter;
import com.example.a846252219.todaynews.bean.NewTabBean;
import com.example.a846252219.todaynews.bean.ResponseInfo;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by 846252219 on 2018/6/23.
 */

public class NewTabPresenter extends BasePresenter {
    private MainActivity activity;
    private PullToRefreshListView pull_refresh_list;
    private String url;
    private ArrayList<NewTabBean.NewsBean> tempList;
    private NewTabBean newTabBean;
    private final ListView listView;
    private boolean isRefresh;
    private String moreUrl;
    private List<NewTabBean.NewsBean> newsList;

    public NewTabPresenter(MainActivity mainActivity, String url, PullToRefreshListView pull_refresh_list) {
        this.url = url;
        this.activity = mainActivity;
        this.pull_refresh_list = pull_refresh_list;
        tempList = new ArrayList<>();
        //获取PullRefreshListView中真正的listView用于设置数据适配器
        listView = pull_refresh_list.getRefreshableView();
    }

    @Override
    protected void showErrorMessage(String string) {
        Log.i("", "请求失败");
        //虽然请求失败了，用户的行为依然是向下拖拽的，或者是向上拖拽，如果不调用此方法
        //则容易出现加载更多脚底，或者下拉刷新头不隐藏的情况出现
        pull_refresh_list.onRefreshComplete();
    }

    @Override
    protected void parseJson(String json) {
        // Log.i("","请求成功="+json);
        Gson gson = new Gson();
        newTabBean = gson.fromJson(json, NewTabBean.class);
        moreUrl = newTabBean.getMore();
        // Log.i("","newTabBean.getNews().get().getUrl()="+newTabBean.getNews().get(0).getUrl());
        //准备数据的方法
        initData();
        //将解析的数据放入到界面
    }

    private void initData() {
        newsList = newTabBean.getNews();
        //如果现在是刷新，则需要将原有集合中的数据清空，然后添加最新数据
        //如果现在是加载，则需要保留原有集合中的数据，
        if (isRefresh) {
            tempList.clear();
        }
        tempList.addAll(newsList);
        //先获取sp中记录的已读新闻的id字符串 id#id#id#
        String ids = SharePreUtil.getString(activity, Constant.IDS, "");
        String[] split = ids.split("#");
        //将字符串转换成集合
        ArrayList<String> stringlist = new ArrayList<>();
        for (int i = 0;i<split.length;i++){
            stringlist.add(split[i]);
        }
        //判断集合中id是否存在云tempList集合的每一个对象
        for (int i = 0;i<tempList.size();i++){
            NewTabBean.NewsBean newsBean = tempList.get(i);
            int id = newsBean.getId();
            if (stringlist.contains(id+"")){
                newsBean.setRead(true);
            }else {
                newsBean.setRead(false);
            }
        }
        //支持下拉刷新上拉加载
        pull_refresh_list.setMode(PullToRefreshListView.Mode.BOTH);
        //对pull_refresh_list下拉和上拉时间监听
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新的方法
                refieshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载的方法
                getMoreData();
            }
        });
        final NewsAdapter newsAdapter = new NewsAdapter(tempList);
        listView.setAdapter(newsAdapter);
        //一点刷新或者加载结束，就要掉用刷新或加载结束隐藏调用头或者加载脚底的方法
        pull_refresh_list.onRefreshComplete();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               // Log.i("","i="+i);
                //应为项目中集成了上拉加载下拉刷新的自定义控件所以i在计数是，记录下了下拉刷新头目的索引位置
                NewTabBean.NewsBean newsBean = tempList.get(i - 1);
                //开启一个全新的activity，用来展示新闻详情
                Intent intent = new Intent(activity, NewDetailActivity.class);
                intent.putExtra("newsBean",newsBean);
                activity.startActivity(intent);
                //如果这个新闻已读，则需要将新闻唯一性的id记录在本地，记录方式如下10001#10002#
                String ids = SharePreUtil.getString(activity, Constant.IDS, "");
                if(!newsBean.isRead()){
                     newsBean.setRead(true);
                     SharePreUtil.saveString(activity,Constant.IDS,newsBean.getId()+"#"+ids);
                     //将变更后的newsBean通知数据适配器更新
                    newsAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void getMoreData() {
        //加载，另外的一个连接地址中发送请求，拿到更多的数据后，需要将更多的数据添加在原有数据后面
        isRefresh = false;
        if (!TextUtils.isEmpty(moreUrl)) {
            String[] strArray = moreUrl.split("/");
            String dirPath = strArray[0];
            String fileName = strArray[1];
            if (!TextUtils.isEmpty(dirPath) && !TextUtils.isEmpty(fileName)) {
                Call<ResponseInfo> call = responseInfoApi.getNewTabData(dirPath, fileName);
                call.enqueue(new CallbackAdapter());
            } else {
                //虽然此处链接地址是空的，依然有做一个向上加载更多的行为，也就是也就是拖拽出加载更多的脚底
                //拖拽出来的脚底需要隐藏
                hiddenFooter();
            }
        } else {
            hiddenFooter();
        }
    }

    private void hiddenFooter() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pull_refresh_list.onRefreshComplete();
                            Toast.makeText(activity, "没有下一页的数据了！", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void refieshData() {
        //刷新，在原有的基础上在发送一次网络请求，如果服务器有新数据就展示最新数据
        //如果没有就展示原有数据
        isRefresh = true;
        getNewTabData();
    }

    //请求网络的方法
    public void getNewTabData() {
        String[] strArray = url.split("/");
        String dirPath = strArray[0];
        String fileName = strArray[1];
        if (!TextUtils.isEmpty(dirPath) && !TextUtils.isEmpty(fileName)) {
            Call<ResponseInfo> call = responseInfoApi.getNewTabData(dirPath, fileName);
            call.enqueue(new CallbackAdapter());
        }
    }
}
