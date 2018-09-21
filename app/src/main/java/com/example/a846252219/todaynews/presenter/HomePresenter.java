package com.example.a846252219.todaynews.presenter;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.a846252219.todaynews.adapter.MyFragmentPagerAdapter;
import com.example.a846252219.todaynews.bean.NewBean;
import com.example.a846252219.todaynews.bean.ResponseInfo;
import com.example.a846252219.todaynews.fragment.HomeFragment;
import com.example.a846252219.todaynews.fragment.NewTabFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by 846252219 on 2018/6/21.
 */

public class HomePresenter extends BasePresenter {
    private HomeFragment homeFragment;
    private List<NewBean> newTabList;
    //不同模块的链接地址集合

    public HomePresenter(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    @Override
    protected void showErrorMessage(String s) {
        Log.i("retrofit", "s=" + s);
    }

    @Override
    protected void parseJson(String json) {

        Gson gson = new Gson();
        //参数二 方式一：如果传送类的字节码文件，则意味着解析后的值，都用次字节码文件只想对象进行储存
        //方法二：如果传递type，则意味着用一个集合进行储存json中所有对象
        newTabList = gson.fromJson(json
                , new TypeToken<List<NewBean>>() {
                }.getType());
        initData();
    }

    private void initData() {
        List<String> titleList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();
        //维护中tabLayout需要显示内容集合，集合中对象多少个，则vrewpager中页面就有多少个
        for (int i = 0; i < newTabList.size(); i++) {
            titleList.add(newTabList.get(i).getTitle());
            NewTabFragment newTabFragment = new NewTabFragment();
            newTabFragment.setUrl(newTabList.get(i).getUrl());
            //所有页面Fragment都填加在集合中
            fragmentList.add(newTabFragment);

        }
//给viewPager设置数据适配器
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(
                homeFragment.getChildFragmentManager(), fragmentList, titleList);
        homeFragment.viewPager.setAdapter(myFragmentPagerAdapter);
        //让tabLayout和VewPresenter进行绑定，建立一个一一对应的关系
        homeFragment.tabLayout.setupWithViewPager( homeFragment.viewPager);
    }

    //发送网络请求
    public void getHomeData() {
        Call<ResponseInfo> call = responseInfoApi.getHomeData();
        call.enqueue(new CallbackAdapter());
    }
}
