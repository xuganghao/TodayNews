package com.example.a846252219.todaynews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.a846252219.todaynews.R;
import com.example.a846252219.todaynews.activity.GridLayoutActivity;
import com.example.a846252219.todaynews.presenter.HomePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 846252219 on 2018/6/21.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.tabLayout)
    public TabLayout tabLayout;
    @BindView(R.id.viewPager)
    public ViewPager viewPager;
    Unbinder unbinder;
    @BindView(R.id.iv_add)
    ImageView ivAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //加载布局
        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        unbinder = ButterKnife.bind(this, view);
        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //请求网络获取数据，将数据填充在布局上
        HomePresenter homePresenter = new HomePresenter(this);
        homePresenter.getHomeData();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_add)
    public void onViewClicked() {
        //开启一个新的Activity
        Intent intent = new Intent(getActivity(), GridLayoutActivity.class);
        startActivity(intent);
    }
}
