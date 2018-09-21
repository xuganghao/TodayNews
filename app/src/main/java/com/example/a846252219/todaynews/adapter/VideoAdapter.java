package com.example.a846252219.todaynews.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.a846252219.todaynews.view.ListItemView;
import com.example.a846252219.todaynews.view.MyMediaPlayer;

/**
 * Created by 846252219 on 2018/8/10.
 */

public class VideoAdapter extends BaseAdapter{
    private String uri;
    private Activity activity;
    private ListItemView currentView;//记录目前那个listItenView正在播放视频

    public VideoAdapter(Activity activity,String uri){
        this.activity=activity;
        this.uri=uri;
    }
    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView ==null){
            //在此处不能复用convertView对象时，创建一个view对象，将此view对象赋值给view即可
            final ListItemView listItemView = new ListItemView(viewGroup.getContext());
            listItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentView!=null) {
                        //之前有一个条目被点过，即已经有一个条目在加载，再次点击另一个条目
                        //则需要之前播放视频的条目移除视频播放的view对象
                        currentView.removeMyMediaPlayer();
                    }

                    //1.创建一个MyMediaPlayer对象
                    MyMediaPlayer myMediaPlayer = new MyMediaPlayer(activity);
                    //2.让MyMediaPlayer播放视频
                    myMediaPlayer.begin(uri);
                    //3.将MyMediaPlayer中的view添加在listItemView内部
                    listItemView.addMyMedioPlayer(myMediaPlayer);
                    //4.记录目前添加过myMedioPlayer的listItemView对象
                    currentView = listItemView;

                }
            });
            convertView =listItemView;
        }else if (convertView !=null&& convertView ==currentView){
            //如果convertView可以被复用，并且复用的view就是正在播放视频的view对象
            //让复用的的convertView结束播放
            currentView.removeMyMediaPlayer();

        }
        return convertView;
    }
}
