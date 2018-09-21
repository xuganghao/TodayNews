package com.example.a846252219.todaynews.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.a846252219.todaynews.R;
import com.example.a846252219.todaynews.fragment.HomeFragment;
import com.example.a846252219.todaynews.fragment.PicFragment;
import com.example.a846252219.todaynews.fragment.UserFragment;
import com.example.a846252219.todaynews.fragment.VideoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rg_home)
    RadioButton btnHome;
    @BindView(R.id.rg_video)
    RadioButton btnVideo;
    @BindView(R.id.btn_asq)
    Button btnAsq;
    @BindView(R.id.rg_pic)
    RadioButton btnPic;
    @BindView(R.id.rg_user)
    RadioButton btnUser;
    @BindView(R.id.rg_group)
    RadioGroup rgGroup;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //进入应用是要默认选中首页
        rgGroup.check(R.id.rg_home);
        HomeFragment homeFragment = new HomeFragment();
        //通过homeFragment来替换顶部的帧布局中的内容
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, homeFragment).commit();

        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //选中条目发生的方法
                switch (i) {
                    case R.id.rg_home:
                        HomeFragment homeFragment = new HomeFragment();
                        //通过homeFragment来替换顶部的帧布局中的内容
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, homeFragment).commit();
                        Log.i("retrofit", "home");
                        break;
                    case R.id.rg_pic:
                        PicFragment picFragment = new PicFragment();
                        //通过homeFragment来替换顶部的帧布局中的内容
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, picFragment).commit();
                        Log.i("retrofit", "pic");
                        break;
                    case R.id.rg_user:
                        UserFragment userFragment = new UserFragment();
                        //通过homeFragment来替换顶部的帧布局中的内容
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, userFragment).commit();
                        Log.i("retrofit", "user");
                        break;
                    case R.id.rg_video:
                        VideoFragment groupFragment = new VideoFragment();
                        //通过homeFragment来替换顶部的帧布局中的内容
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, groupFragment).commit();
                        Log.i("retrofit", "group");
                        break;
                }
            }
        });
    }


    @OnClick(R.id.btn_asq)
    public void onViewClicked() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            //创建动画，指定时长
            Transition slide = new Slide();
            slide.setDuration(1000);
            //指定activity进入和出去的动画效果
            getWindow().setEnterTransition(slide);
            getWindow().setExitTransition(slide);
            //开启新界面
            Intent intentSlide = new Intent(MainActivity.this, QuestActivity.class);
            startActivity(intentSlide,
                    ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
        }
    }
}
