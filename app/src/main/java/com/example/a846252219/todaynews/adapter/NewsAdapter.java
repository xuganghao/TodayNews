package com.example.a846252219.todaynews.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a846252219.todaynews.Constant;
import com.example.a846252219.todaynews.R;
import com.example.a846252219.todaynews.bean.NewTabBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 846252219 on 2018/6/24.
 */

public class NewsAdapter extends BaseAdapter {
    private static final int ITEM_TEXT_PIC = 0;//图片+文本条目状态码
    private static final int ITEM_PIC = 1;//纯图片条目状态码
    private final ArrayList<NewTabBean.NewsBean> data;
    private List<NewTabBean.NewsBean> newsBeanList;
    private NewTabBean.NewsBean newsBean;
    private NewTabBean.NewsBean bean;

    public NewsAdapter(ArrayList<NewTabBean.NewsBean> newsList) {
        this.data = newsList;

    }

    //告知现在的数据适配器，条目类型有两种
    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }
    //判断每一个索引位置的条目类型是什么


    @Override
    public int getItemViewType(int position) {
        NewTabBean.NewsBean newsBean = data.get(position);
        if (newsBean.getType() == 0) {
            //返回单张图片的条目类型状态码
            return ITEM_TEXT_PIC;
        } else {
            //返回多张图片的条目类型状态码
            return ITEM_PIC;
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public NewTabBean.NewsBean getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("Assert")
    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {
        int itemViewType = getItemViewType(i);

        if (itemViewType == ITEM_TEXT_PIC) {
            TextPicViewHolder textPicViewHolder = null;
            //填充图片+文本条目状态码
            if (convertview == null) {
                convertview = View.inflate(viewGroup.getContext(), R.layout.item_text_pic, null);
                textPicViewHolder = new TextPicViewHolder(convertview);
                convertview.setTag(textPicViewHolder);
            }else {
                textPicViewHolder = (TextPicViewHolder) convertview.getTag();
            }

            //克隆newsBean对象
            /**
             * comment : true
             * commentlist : http://10.0.2.2:8080/jrtt/10007/comment_1.json
             * commenturl : http://jrtt.qianlong.com/client/user/newComment/35319
             * id : 35311
             * listimage : http://10.0.2.2:8080/jrtt/10007/1.jpg
             * pubdate : 2014-10-1113:18
             * title : 网上大讲堂第368期预告：义务环保人人有责
             * type : 0
             * url : http://10.0.2.2:8080/jrtt/10007/724D6A55496A11726628.html
             * listimage1 : http://10.0.2.2:8080/jrtt/10007/4.jpg
             * listimage2 : http://10.0.2.2:8080/jrtt/10007/5.jpg
             */
           // bean = null;
//            try {
                newsBean = new NewTabBean.NewsBean();
                newsBeanList = new ArrayList<NewTabBean.NewsBean>();
                newsBeanList.add(newsBean);
//            //NewTabBean.NewsBean bean = null;
//            bean.setComment(true);
            try {
                bean = (NewTabBean.NewsBean) newsBeanList.get(0).clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
//            } catch (CloneNotSupportedException e) {
//                e.printStackTrace();
//            }
//            if (bean.getCommentlist()==null){
//                bean.setCommentlist(Constant.BASEURL4);
//                }
//                if (bean.getListimage() ==null){
//                    bean.getListimage().replaceAll(Constant.BASEURL1,Constant.BASEURL2);
//                }
//                if (bean.getListimage1() ==null){
//                    bean.getListimage1().replaceAll(Constant.BASEURL1,Constant.BASEURL2);
//                }
//                if (bean.getListimage2() ==null){
//                    bean.getListimage2().replaceAll(Constant.BASEURL1,Constant.BASEURL2);
//                }
//                if (bean.getUrl() ==null){
//                    bean.getUrl().replaceAll(Constant.BASEURL1,Constant.BASEURL2);
//                }

                                //     Log.i("","ffff"+bean.getListimage1());

            bean = getItem(i);



            textPicViewHolder.tvTitle.setText(bean.getTitle());
            textPicViewHolder.tvTime.setText(bean.getPubdate());
            //Glide图片异步加载框架，加载服务器中的框架，图片的三级缓存
            //内存，图片存储在内存中，如果下次还要用图片，不从网络中下载，而从内存中中加载（1）
            //文件，图片存储在本地文件中，如果下次还要用图片，不从网络中下载，而从本地文件中加载（2）
            //网络，图片还放在服务器中，本地放置的是图片的链接地址（3）
            Glide.with(viewGroup.getContext())
                    .load(bean.getListimage()
                    .replaceAll(Constant.BASEURL1,Constant.BASEURL5)).into(textPicViewHolder.ivImage);


            //判断一下，newsBean对象对象Read字段值即可
            if (bean.isRead()){
                textPicViewHolder.tvTitle.setTextColor(Color.RED);
                textPicViewHolder.tvTime.setTextColor(Color.RED);
            }else {
                textPicViewHolder.tvTitle.setTextColor(Color.BLACK);
                textPicViewHolder.tvTime.setTextColor(Color.BLACK);
            }
            return convertview;

        } else {
            //填充纯图片条目状态码
            PicViewHolder picViewHolder = null;
            if (convertview == null) {
                convertview = View.inflate(viewGroup.getContext(), R.layout.item_pic, null);
                picViewHolder = new PicViewHolder(convertview);
                convertview.setTag(picViewHolder);
            }else {
                picViewHolder = (PicViewHolder) convertview.getTag();
            }
            NewTabBean.NewsBean bean = getItem(i);
            picViewHolder.tvTime.setText(bean.getPubdate());
            Glide.with(viewGroup.getContext()).load(bean.getListimage()
                    .replaceAll(Constant.BASEURL1,Constant.BASEURL5)).into(picViewHolder.ivImage1);
            Glide.with(viewGroup.getContext()).load(bean.getListimage1()
                    .replaceAll(Constant.BASEURL1,Constant.BASEURL5)).into(picViewHolder.ivImage2);
            Glide.with(viewGroup.getContext()).load(bean.getListimage2()
                    .replaceAll(Constant.BASEURL1,Constant.BASEURL5)).into(picViewHolder.ivImage3);
            if (bean.isRead()){
                picViewHolder.tvTime.setTextColor(Color.RED);
            }else {
                picViewHolder.tvTime.setTextColor(Color.BLACK);
            }
            return convertview;
        }
        //1.复用view
        //2.指定view减少findViewById
        //3.给viewHolder指定为静态


    }

    static class TextPicViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.iv_image)
        ImageView ivImage;

        TextPicViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class PicViewHolder {
        @BindView(R.id.iv_image1)
        ImageView ivImage1;
        @BindView(R.id.iv_image2)
        ImageView ivImage2;
        @BindView(R.id.iv_image3)
        ImageView ivImage3;
        @BindView(R.id.tv_time)
        TextView tvTime;

        PicViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
