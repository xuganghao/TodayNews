package com.example.a846252219.todaynews.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.a846252219.todaynews.R;

/**
 * Created by 846252219 on 2018/8/10.
 */

public class ListItemView extends FrameLayout {

    private MyMediaPlayer myMediaPlayer;

    public ListItemView(@NonNull Context context) {
        this(context,null);
    }

    public ListItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public ListItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.list_item_view_item,null);
       ImageView ivBackground =  view.findViewById(R.id.iv_background);
        ImageView inPlay = view.findViewById(R.id.iv_play);
        addView(view);
    }

    //添加MyMedioPlayer中的view对象方法
    public void addMyMedioPlayer(MyMediaPlayer myMediaPlayer){
        this.myMediaPlayer=myMediaPlayer;
         addView( myMediaPlayer.getRootView());
    }
    //移除MyMedioPlayer的view对象从ListItemView中方法，并且暂停视频播放
    public void removeMyMediaPlayer(){
        myMediaPlayer.release();
        this.removeView(myMediaPlayer.getRootView());

    }
}
