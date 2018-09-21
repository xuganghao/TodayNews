package com.example.a846252219.todaynews.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.a846252219.todaynews.R;
import com.example.a846252219.todaynews.adapter.NewsAdapter;
import com.example.a846252219.todaynews.bean.NewTabBean;
import com.example.a846252219.todaynews.db.DBHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 846252219 on 2018/8/2.
 */

public class FavListActivity extends AppCompatActivity {
    @BindView(R.id.lv_fav_news)
    ListView lvFavNews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list);
        ButterKnife.bind(this);

        //从数据库中将收藏过的数据查询出来
        try {
            DBHelper dbHelper = new DBHelper(this);
            Dao<NewTabBean.NewsBean,Integer> dao = dbHelper.getDao(NewTabBean.NewsBean.class);
            List<NewTabBean.NewsBean> newsList = dao.queryForAll();

            NewsAdapter newsAdapter = new NewsAdapter((ArrayList<NewTabBean.NewsBean>) newsList);
            lvFavNews.setAdapter(newsAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
