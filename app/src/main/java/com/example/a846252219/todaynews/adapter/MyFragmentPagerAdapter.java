package com.example.a846252219.todaynews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 846252219 on 2018/6/23.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<String> titleList;
    private List<Fragment> fragmentList;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList=fragmentList;
        this.titleList=titleList;
    }

    @Override
    public Fragment getItem(int position) {
        //根据索引返回Fragment的对象
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        //指定viewpager中一共有几个界面

        return fragmentList.size();
    }

    //给和viewpager绑定tabLyout设置标题内容

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
