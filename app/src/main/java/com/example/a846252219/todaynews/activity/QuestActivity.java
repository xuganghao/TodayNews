package com.example.a846252219.todaynews.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.Transition;

import com.example.a846252219.todaynews.R;

/**
 * Created by 846252219 on 2018/6/21.
 */

public class QuestActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        //进入此activity动画和突出此activity的动画
        //在开启界面中也需要指定动画，Slide特效
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition slide = new Slide();
            slide.setDuration(1000);
            getWindow().setEnterTransition(slide);//进入时使用特效
            getWindow().setExitTransition(slide);//退出时使用特效
        }
    }
}
