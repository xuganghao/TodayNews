package com.example.a846252219.todaynews.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.a846252219.todaynews.R;

import java.util.ArrayList;

/**
 * Created by 846252219 on 2018/8/13.
 */

public class MyGridLayout extends GridLayout {
    private Context context;
    private ArrayList<Rect> rectList;
    private View currentView;
    private OnCustomerClick customerClick;
    private boolean isEnable;

    public MyGridLayout(Context context) {
        this(context,null);
    }

    public MyGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public MyGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        setLayoutTransition(new LayoutTransition());
    }
    //添加字符串文本内容的方法
    public void addItems(String[] str){

        for (int i = 0;i<str.length;i++){
            addItemView(str[i]);
        }

        if (isEnable){
            //对GridLayout控件的拖拽监听
            setOnDragListener(onDragListener);
        }else{
            setOnDragListener(null);
        }

    }

    public void addItemView(String s) {
        //1.创建多个TextView让后添加上str对应索引位置的字符串
        final TextView textView = new TextView(context);
        textView.setText(s);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        //给textview设置背景，背景包含选择器的效果
        textView.setBackgroundResource(R.drawable.selector_textview_bg);
        //让textview与背景有一定内间距
        textView.setPadding(30,30,30,30);
        //让textview文字位于背景最中间位置
        textView.setGravity(Gravity.CENTER);

        LayoutParams layoutParams = new LayoutParams();
        layoutParams.setMargins(30,30,30,30);
        //2.让TextView添加在MyGridLayout控件内部
        addView(textView,layoutParams);
        if (isEnable){
            textView.setOnLongClickListener(onLongClickListener);
        }else{
            textView.setOnClickListener(null);
        }

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //你知道点中的textView属于顶部的GridLayout，还是属于底部的GridLayout内部的对象？
                //点中顶部GridLayout中的textView，将顶部的textView删除，将删除的textView在底部的GridLayout中显示
                //如果是点中底部GridLayout中的textView，则相反
                //用回调解决上述问题
                if (customerClick!=null){
                    customerClick.cuotomerOnClick(textView);
                }

            }
        });

    }

    /**
     * @param isEnable true 长按能被选中和拖拽
     *                false 长按不能被选中或拖拽
     */
    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    //1.定义一个接口
    //2.定义未实现业务逻辑方法一个
    //3.在需要触发这段逻辑的地方，调用接口实现类对象，实现该方法
    public interface OnCustomerClick{
        public void cuotomerOnClick(TextView textView);
    }

    public void setOnCustomerClick(OnCustomerClick customerClick){
        this.customerClick=customerClick;
    }
    private OnLongClickListener onLongClickListener =new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view){
            //1.给选中的view提供一个阴影效果
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
                view.startDragAndDrop(null, new DragShadowBuilder(view), null, 0);
            }else{
                view.startDrag(null,new DragShadowBuilder(view),null,0);
            }
            //2.让textview为不可用的状态，不可用的状态为红色边框
            view.setEnabled(false);
            //3.目前选中的textview对象记录下来
            currentView = view;
            return true;
        }
    };
    //屏幕中一共有多少个矩形，占有的区域都有哪些
    public void getAllRect(){
        //创建一个矩形的集合，用于添加多个矩形对象
        rectList = new ArrayList<>();
        //1.矩形的个数 == 添加在GridLayout中TextView的个数
        int childCount = getChildCount();
        //2.获取每一个矩形所在地区域
        for (int i = 0;i<childCount;i++){
            View childAt = getChildAt(i);
            //根据四个坐标获取矩形所在区域，从而构建一个矩形对象
            Rect rect = new Rect(childAt.getLeft(), childAt.getTop(),
                    childAt.getRight(), childAt.getBottom());
            rectList.add(rect);
        }
    }

    /**
     * @param event     触发的事件对象，event对象可以拿到手指所在位置的X和Y的坐标
     *        目前手指移动到哪个矩形区域内,返回落在矩形内的检索位置
     */
    public int getIndex(DragEvent event){
        int locationX = (int) event.getX();
        int locationY = (int) event.getY();
        int index = -1;
        //判断locationX和LoactionY落在那一个区域内
        for(int i = 0;i<rectList.size();i++){
            Rect rect = rectList.get(i);
            //判断rect是否包含locationX和LoactionY即可
            if(rect.contains(locationX,locationY)){
                index = i;
                break;
            }
        }
        return index;
    }


    private OnDragListener onDragListener = new OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent event) {
            switch (event.getAction()){
                case DragEvent.ACTION_DRAG_STARTED:
                    //在刚开始拖拽时，需要知道屏幕中有多少个矩形
                    Log.i("","开始拖拽");
                    getAllRect();
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.i("","进入拖拽");
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    Log.i("","拖拽到某一位置");
                    //判断目前拖拽的位置，落在那个矩形区域内
                    int index = getIndex(event);
                    if (index!=-1 && currentView!=null && currentView!=getChildAt(index)){
                        //移除拖拽过程中的view对象
                        MyGridLayout.this.removeView(currentView);
                        //将新的view对，添加在index上
                        MyGridLayout.this.addView(currentView,index);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.i("","拖拽结束");
                    if (currentView!=null){
                        //在此处修改Endbled值是为了将边框颜色变回黑色
                        currentView.setEnabled(true);
                    }
                    break;
            }
            return true;
        }
    };
}
