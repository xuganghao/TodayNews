package com.example.a846252219.todaynews.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a846252219.todaynews.Constant;
import com.example.a846252219.todaynews.R;
import com.example.a846252219.todaynews.bean.NewTabBean;
import com.example.a846252219.todaynews.db.DBHelper;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.j256.ormlite.dao.Dao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewDetailActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE = 100;
    @BindView(R.id.lv_back)
    ImageView lvBack;
    @BindView(R.id.lv_favorite)
    ImageView lvFavorite;
    @BindView(R.id.lv_share)
    ImageView lvShare;
    @BindView(R.id.lv_textsize)
    ImageView lvTextsize;
    @BindView(R.id.weview)
    WebView weview;
    @BindView(R.id.iv_talk)
    ImageView ivTalk;
    private WebSettings settings;
    private String[] str = new String[]{"超大","大号","普通","小号","极小"};
    private NewTabBean.NewsBean newsBean;


    //是否正在播报的标识
    private boolean isSpeaking = false;//true  表示正在播报   false  表示没有播报
    //是否是从头开始播报文本的标识
    private boolean startSpeaking = true;//true  表示从头播报  false  表示从暂停的部分开始播报


    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener(){
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
            startSpeaking = true;
        }
        //缓冲进度回调
//percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {}
        //开始播放
        public void onSpeakBegin() {}
        //暂停播放
        public void onSpeakPaused() {}
        //播放进度回调
//percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {}
        //恢复播放回调接口
        public void onSpeakResumed() {}
        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {}
    };
    private SpeechSynthesizer mTts;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);
        ButterKnife.bind(this);

        //1.创建 SpeechSynthesizer 对象, 第二个参数：本地合成时传 InitListener
        mTts = SpeechSynthesizer.createSynthesizer(this, null);

        //获取前一个界面传递的对象
        newsBean = (NewTabBean.NewsBean) getIntent().getSerializableExtra("newsBean");
        //Log.i("p", "newsBean.getUrl" + newsBean.getUrl());
        weview.loadUrl(newsBean.getUrl().replaceAll(Constant.BASEURL1,Constant.BASEURL5));
        settings = weview.getSettings();
        settings.setBuiltInZoomControls(true);//出现放大或缩小按钮
        settings.setUseWideViewPort(true);//支持双击放大（对wap网页不支持，pc；wed）
        settings.setJavaScriptEnabled(true);//支持js
        weview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                weview.loadUrl(url);
                return false;
            }
        });

        weview.setWebChromeClient(new WebChromeClient());


        //参数一:实现接口类的对象,接口名称任意定义
        //参数二:接口的别名,别名需要和html中编写调用方法的类的名称一致
        weview.addJavascriptInterface(new JsCallAndroid() {
            @Override
            @JavascriptInterface
            public void back() {
                Toast.makeText(NewDetailActivity.this, "js调用了android 结束界面的代码", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, "Android");

        //申请运行时权限
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE);
    }

    private void speak(String context) {

//2.合成参数设置，详见《MSC Reference Manual》SpeechSynthesizer 类
//设置发音人（更多在线发音人，用户可参见 附录13.2
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        //设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");
//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");
//设置音量，范围 0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
//设置云端
//设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
//保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
//仅支持保存为 pcm 和 wav 格式，如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
//3.开始合成
        mTts.startSpeaking(context, mSynListener);
    }


    //监听请求结果的代码
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //sd卡的权限给了此应用
            Log.i("","sd卡权限给应用");

        }else{
            //sd卡的权限拒绝
            Log.i("","sd卡的权限拒绝");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick({R.id.lv_back, R.id.lv_favorite, R.id.lv_share, R.id.lv_textsize, R.id.iv_talk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lv_back:
                break;
            case R.id.lv_favorite:
                fav();
                break;
            case R.id.lv_share:
                showShare();
                break;
            case R.id.lv_textsize:
                showTextsizeDialog();
                break;
            case R.id.iv_talk:
                //调用js代码弹出对话框
               // weview.loadUrl("javascript:wave()");
                if (!isSpeaking){
                    //播报
                    isSpeaking=true;
                    if (startSpeaking){
                        //从头播报
                        initSpeak();
                    }else{
                        //继续播报
                        mTts.resumeSpeaking();
                    }

                }else{
                    //处于暂停位置停止播报
                    isSpeaking=false;
                    mTts.pauseSpeaking();
                }

                break;
        }
    }

    private void initSpeak() {
        //初始化需要播报的文本内容，文本内容过多解析他就是一个耗时的操作
        new Thread(){
            @Override
            public void run() {

                try {
                    //解析html中p标签的内容的过程
                    Document document = Jsoup.parse(new URL(newsBean.getUrl().replace(Constant.BASEURL1,Constant.BASEURL5)), 10000);
                    //针对p标签进行解析
                    Elements elements = document.select("p");
                    //解析每一个p标签的内容
                    Iterator<Element> iterator = elements.iterator();
                    StringBuilder stringBuilder = new StringBuilder();
                    //判断是否解析完的条件，如果没有解析完就一直向下解析，解析完了就跳出循环
                  while (iterator.hasNext()){
                      Element element = iterator.next();
                      String content = element.text();
                      stringBuilder.append(content);
                  }
                  Log.i("qqq","stringBuilder.toString()="+stringBuilder.toString());
                    String context = stringBuilder.toString();
                    startSpeaking=false;//一旦调用了此方法，从头播放就变成了从暂停位置播放
                    speak(context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        //title标题，印象笔记，邮箱，信息，微信，人人网和QQ空间使用
        //oks.setTitle("标题");
        //titleUrl是标题的网络链接，QQ和QQ空间使用
        //oks.setTitleUrl("http://sharesdk.cn");
        //text是分享文本，所有平台都需要使用这个字段
        oks.setText("这是一段测试文本");
        //imagepath是图片的本地路径，linked—In以外的平台都支持此参数
        oks.setImagePath("/sdcard/text.jpg");//确保SDcard下面存在此照片，高版本手机读取SD卡是，需要运行时权限
        //url仅在微信（包括好友或朋友圈）中使用，新浪微博中使用
        oks.setUrl("http://www.itheima.com");
        //comment是我对这了分享的评论，仅在人人网或QQ空间使用
        //oks.setComment("我是测试文本");
        //site是分享此内容的网站名称，仅在QQ空间使用
        //oks.setSite(getString(R.string.app_name));
        //SiteUrl是分享此内容的网站地址，仅QQ空间使用
        //oks.setSiteUrl("http://sharesdk.cn");

        //启动分享GUI
        oks.show(this);

    }

    //定义一个接口，在接口中有一个和html中点击事件同名的方法，js调用android
    public interface JsCallAndroid{
        public void back();
    }

    private void fav() {
        //1.让建库表操作执行
        DBHelper dbHelper = new DBHelper(this);
        //2.通过dbHelper对象获取可以对t_news表进行怎删改查的dao对象
        Dao<NewTabBean.NewsBean,Integer> dao = dbHelper.getDao(NewTabBean.NewsBean.class);
        //3.通过dao拿到查询操作，如果现在从前一个界面传递过来的newsBean对象，
        //3.1如果已经存在于数据库中，则点击按钮是取消
        //3.2如果没有存在于数据库中，则点击按钮是收藏
        try {
            NewTabBean.NewsBean bean = dao.queryForId(newsBean.getId());
            if (bean==null){
                Toast.makeText(this,"收藏成功",Toast.LENGTH_LONG).show();
                dao.create(newsBean);
            }else{
                Toast.makeText(this,"取消收藏",Toast.LENGTH_LONG).show();
                dao.delete(newsBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void showTextsizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
switch (i){
    case 0://超大
//        settings.setTextSize(WebSettings.TextSize.LARGEST);
    settings.setTextZoom(200);
        break;
    case 1://大号
//        settings.setTextSize(WebSettings.TextSize.LARGER);
        settings.setTextZoom(150);
        break;
    case 2://普通
//        settings.setTextSize(WebSettings.TextSize.NORMAL);
        settings.setTextZoom(100);
        break;
    case 3://小号
//        settings.setTextSize(WebSettings.TextSize.SMALLER);
        settings.setTextZoom(75);
        break;
    case 4://极小
//       settings.setTextSize(WebSettings.TextSize.SMALLEST);
        settings.setTextZoom(50);
        break;
}
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
