package com.example.a846252219.todaynews.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a846252219.todaynews.Constant;
import com.example.a846252219.todaynews.MyBitmapUtils;
import com.example.a846252219.todaynews.R;
import com.example.a846252219.todaynews.bean.PicBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 846252219 on 2018/8/7.
 */

public class PicAdapter extends BaseAdapter {
    private List<PicBean.NewsBean> data;
    private MyBitmapUtils myBitmapUtils;

    public PicAdapter(Activity activity, List<PicBean.NewsBean> newsList) {
        this.data = newsList;
        myBitmapUtils = new MyBitmapUtils(activity);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public PicBean.NewsBean getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.item_news_pic, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        PicBean.NewsBean newsBean = getItem(i);
        viewHolder.tvTitle.setText(newsBean.getTitle().replace(Constant.BASEURL1,Constant.BASEURL5));
        viewHolder.ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //给组图模块设置图片
        myBitmapUtils.setImageBitmap(viewHolder.ivImage,newsBean.getListimage().replace(Constant.BASEURL1,Constant.BASEURL5));
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
