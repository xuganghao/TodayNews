package com.example.a846252219.todaynews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.a846252219.todaynews.R;
import com.example.a846252219.todaynews.view.MyGridLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 846252219 on 2018/8/13.
 */

public class GridLayoutActivity extends Activity {

    @BindView(R.id.gi_top)
    MyGridLayout giTop;
    @BindView(R.id.gi_bottom)
    MyGridLayout giBottom;

    //提供两个GridLayout中需要显示的文本内容
    private String[] topStr = new String[]{"标题1","标题2","标题3","标题4","标题5","标题6","标题7","标题8","标题9"};
    private String[] bottomStr = new String[]{"@标题A1","@标题B2","@标题C3","@标题D4","@标题E5","@标题F6","@标题G7","@标题H8","@标题I9"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gred_layout);
        ButterKnife.bind(this);

        giTop.setEnable(true);
        giBottom.setEnable(false);

        //将以上两个不同的字符串添加在不同的MyGridLayout中
        giTop.addItems(topStr);
        giBottom.addItems(bottomStr);

        //给顶部的GridLayout内的每一个textView注册点击事件
        giTop.setOnCustomerClick(new MyGridLayout.OnCustomerClick() {
            @Override
            public void cuotomerOnClick(TextView textView) {
                //参数中的textView就是点中的textView
                //点中顶部GridLayout中的textView，将顶部的textView删除，将删除的textView在底部的GridLayout中显示
                giTop.removeView(textView);
                giBottom.addItemView(textView.getText().toString());
            }
        });
        //给底部的GridLayout内的每一个textView注册点击事件
        giBottom.setOnCustomerClick(new MyGridLayout.OnCustomerClick() {
            @Override
            public void cuotomerOnClick(TextView textView) {
                //参数中的textView就是点中的textView
                //如果是点中底部GridLayout中的textView，则相反
                giBottom.removeView(textView);
                giTop.addItemView(textView.getText().toString());
            }
        });
    }
}
