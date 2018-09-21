package com.example.a846252219.todaynews.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.a846252219.todaynews.R;

/**
 * Created by 846252219 on 2018/8/11.
 */

public class MyMediaPlayer implements View.OnClickListener {

    private Context context;
    private TextureView ttv;
    private ImageView ivPlayPause;
    private ProgressBar progressBar;
    private MediaPlayer mediaPlayer;
    private Surface surface;
    private View view;

    public MyMediaPlayer(Context context){
        this.context=context;
        view = View.inflate(context, R.layout.item_media_player, null);
        ttv = view.findViewById(R.id.ttv);
        ivPlayPause = view.findViewById(R.id.iv_play_pause);
        progressBar = view.findViewById(R.id.progressbar);

        //监听TextureView是否已经准备好加载视频
        ttv.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {




            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                //如果现在TextureView可以使用，则需要准备mediaplayer对象管理视频
                mediaPlayer = new MediaPlayer();
                //播放视频的容器Surface
                surface = new Surface(surfaceTexture);

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });
        ivPlayPause.setOnClickListener(this);

    }
    //视屏加载的准备工作，在视频加载过程中让其显示进度条
    public void begin(final String uri){
        progressBar.setVisibility(View.VISIBLE);
        //在进度条展示几秒后，让其隐藏
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                //播放视频
                playVideo(uri);
            }
        },1000);
    }

    /**
     * @param uri   播放视频的链接地址
     *              异步加载视频，播放
     */
    private void playVideo(String uri) {
        //通过mediaplay加载uri播放视频
        try {
            //指定视频的播放路径
            mediaPlayer.setDataSource(context, Uri.parse(uri));
            //指定视屏播放容器对象
            mediaPlayer.setSurface(surface);
            //如果视频播放完成后，循环播放
            mediaPlayer.setLooping(true);
            //视频播放的声音
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //指定视频的异步加载
            mediaPlayer.prepareAsync();
            //异步加载的结果进行监听
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //暂停，继续
    public void pauseOrResume(){
        //如果现在视频正在播放，点击就停止
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            //将图片替换成暂停的图案
            ivPlayPause.setImageResource(R.mipmap.play_icon);
        }else{
            //如果视频处于停止状态，点击就播放
            mediaPlayer.start();
            ivPlayPause.setImageResource(R.mipmap.pause_video);
        }
    }

    @Override
    public void onClick(View view) {
        pauseOrResume();
    }
    //释放播放视频资源的方法
    public void release(){
        if(mediaPlayer!=null){
           mediaPlayer.release();
        }
    }
    //返回的就是播放视频的view对象
    public View getRootView(){
        return view;
    }
}
