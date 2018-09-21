package com.example.dome;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * Created by 846252219 on 2018/6/24.
 */

public class ListActivity extends Activity {

    private PullToRefreshListView pullToRefreshListView;
    private ArrayList<String> stringList;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //获取支持下拉刷新上拉加载的控件
        pullToRefreshListView = findViewById(R.id.pull_refresh_list);
        //2.
        ListView listView = pullToRefreshListView.getRefreshableView();
        //3.给ListView配置适配器
        initData();
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        //4.开启下拉刷新上拉加载的功能
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                refreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                getMoreData();
            }
        });
    }

    private void getMoreData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    stringList.add( "Add字符串的加载");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter.notifyDataSetChanged();
//告知下拉刷新上拉加载的listView加载完毕
                            pullToRefreshListView.onRefreshComplete();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    private void refreshData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    stringList.add(0, "Add字符串的刷新");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter.notifyDataSetChanged();
//告知下拉刷新上拉加载的listView加载完毕
                            pullToRefreshListView.onRefreshComplete();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return stringList.size();
        }

        @Override
        public String getItem(int i) {
            return stringList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(ListActivity.this);
            textView.setText(getItem(i));
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            return textView;
        }
    }

    private void initData() {
        stringList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            stringList.add("String" + i);

        }
    }
}
