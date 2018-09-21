package com.example.a846252219.todaynews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.a846252219.todaynews.R;
import com.example.a846252219.todaynews.activity.FavListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 846252219 on 2018/6/21.
 */

public class UserFragment extends BaseFragment {
    @BindView(R.id.btn_fav)
    Button btnFav;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_user, null);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_fav)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), FavListActivity.class);
        startActivity(intent);
    }
}
