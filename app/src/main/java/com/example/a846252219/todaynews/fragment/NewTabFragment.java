package com.example.a846252219.todaynews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a846252219.todaynews.R;
import com.example.a846252219.todaynews.activity.MainActivity;
import com.example.a846252219.todaynews.presenter.NewTabPresenter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by 846252219 on 2018/6/23.
 */

public class NewTabFragment extends BaseFragment {

    @BindView(R.id.pull_refresh_list)
    PullToRefreshListView pull_refresh_list;
    Unbinder unbinder;
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragemt_new_tab, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        NewTabPresenter newtabPresenter = new NewTabPresenter((MainActivity) getActivity(),url,pull_refresh_list);

        newtabPresenter.getNewTabData();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
