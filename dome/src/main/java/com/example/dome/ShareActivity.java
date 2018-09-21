package com.example.dome;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by 846252219 on 2018/8/2.
 */

public class ShareActivity extends Activity {
    private static final int REQUEST_WRITE = 100;
    @BindView(R.id.btn_share)
    Button btnShare;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);

        //申请运行时权限
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE);
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

    @OnClick(R.id.btn_share)
    public void onViewClicked() {
        showShare();
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
}
