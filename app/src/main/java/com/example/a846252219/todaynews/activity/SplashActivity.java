package com.example.a846252219.todaynews.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.a846252219.todaynews.R;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

public class SplashActivity extends AppCompatActivity {
    @butterknife.BindView(R.id.videoview)
    VideoView videoview;
    @butterknife.BindView(R.id.tv_enter)
    TextView tvEnter;
    private boolean isEnter = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        butterknife.ButterKnife.bind(this);
        initVideo();
        initView();
        tvEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击后进入主页面
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                //已经进入主页面，点击后就没必要在进入
                isEnter = true;
            }
        });

        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5a6802c9");
    }
    //让videoview冲满屏幕
    private void initView() {
        //获取屏幕宽高
        int heightPixels = getResources().getDisplayMetrics().heightPixels;
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        //用videoview的父容器来控制宽高
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(widthPixels, heightPixels);
        //设定一些规则，视频四周与屏幕四周对齐
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //让 包含了params设置在videoview中
        videoview.setLayoutParams(params);
    }

    //准备视频
    private void initVideo() {
        //1.加载视屏的方法
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.kr36);
        //2.告知videoview播放指定路径下的视频
        videoview.setVideoURI(uri);
        //3.让视频播放起来
        videoview.start();
        //视频播放完成后的跳转
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (isEnter) {
                    return;
                }
                //在视屏播放完成后跳转
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
