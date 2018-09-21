package com.example.a846252219.todaynews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.a846252219.todaynews.R;
import com.example.a846252219.todaynews.presenter.picPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 846252219 on 2018/6/21.
 */

public class PicFragment extends BaseFragment {
    @BindView(R.id.lv_pic)
    ListView lvPic;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_pic, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //给ListView准备数据
        picPresenter picPresenter = new picPresenter(getActivity(),lvPic);
        picPresenter.getPicData();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
