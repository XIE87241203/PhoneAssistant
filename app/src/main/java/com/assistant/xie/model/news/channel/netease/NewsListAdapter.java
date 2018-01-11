package com.assistant.xie.model.news.channel.netease;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.assistant.xie.R;
import com.assistant.xie.Utils.GlideUtils;
import com.assistant.xie.model.base.BaseWebViewActivity;
import com.xie.functionalrecyclerlayout.adapter.AutoLoadRecyclerAdapter;
import com.xie.functionalrecyclerlayout.holder.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIE on 2017/12/29.
 * 网易新闻适配器
 */

public class NewsListAdapter extends AutoLoadRecyclerAdapter<BaseRecyclerViewHolder> {
    private Context context;
    private List<NewsInfo> list;

    NewsListAdapter(Context context, List<NewsInfo> list) {
        super(context);
        this.context = context;
        this.list = list;
    }

    @Override
    protected BaseRecyclerViewHolder onCreateViewHolderNew(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder viewHolder = null;
        switch (viewType) {
            case 0:
                viewHolder = BaseRecyclerViewHolder.createViewHolder(context, parent, R.layout.news_netease_list_item_type1);
                break;
            case 1:
                viewHolder = BaseRecyclerViewHolder.createViewHolder(context, parent, R.layout.news_netease_list_item_type2);
                break;
            case 2:
                viewHolder = BaseRecyclerViewHolder.createViewHolder(context, parent, R.layout.news_netease_list_item_type3);
                break;
        }
        return viewHolder;
    }

    @Override
    protected void onBindViewHolderNew(BaseRecyclerViewHolder holder, final int position) {
        final Intent intent = new Intent(context, BaseWebViewActivity.class);
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_comment_count = holder.getView(R.id.tv_comment_count);
        switch (getItemViewTypeNew(position)) {
            case 0:
                ImageView iv_img = holder.getView(R.id.iv_img);
                //加载图片
                GlideUtils.loadImage(context, list.get(position).getImgsrc(), R.color.defaultImageHolderColor, iv_img);
                //跳转链接
                intent.putExtra("url", list.get(position).getUrl());
                break;
            case 1:
                LinearLayout ll_image = holder.getView(R.id.ll_image);
                List<ImageView> imageViewList = new ArrayList<>();
                imageViewList.add(addImageView(ll_image, false));
                for (int i = 0; i < list.get(position).getImgextra().size(); i++) {
                    boolean isEnd = i == (list.get(position).getImgextra().size() - 1);
                    imageViewList.add(addImageView(ll_image, isEnd));
                }
                //加载图片
                GlideUtils.loadImage(context, list.get(position).getImgsrc(), R.color.defaultImageHolderColor, imageViewList.get(0));
                intent.putExtra("url", list.get(position).getSkipURL());
                for (int i = 0; i < list.get(position).getImgextra().size(); i++) {
                    //加载图片
                    GlideUtils.loadImage(context, list.get(position).getImgextra().get(i), R.color.defaultImageHolderColor, imageViewList.get(i + 1));
                }
                break;
            case 2:
                ImageView iv_image = holder.getView(R.id.iv_image);
                GlideUtils.loadImage(context, list.get(position).getImgsrc(), R.color.defaultImageHolderColor, iv_image);
                intent.putExtra("url", list.get(position).getSkipURL());
                break;
        }

        //共同属性设置
        tv_title.setText(list.get(position).getTitle());
        tv_time.setText(list.get(position).getPtime());
        tv_comment_count.setText(String.format(context.getString(R.string.news_netease_comment_count), list.get(position).getCommentCount()));
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("title", list.get(position).getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected int getItemViewTypeNew(int position) {
        if ((list.get(position).getImgsrc3gtype() - 1) > 2 || (list.get(position).getImgsrc3gtype() - 1) < 0) {
            return 0;
        } else {
            return list.get(position).getImgsrc3gtype() - 1;
        }
    }

    @Override
    public int getRealItemCount() {
        return list.size();
    }


    private ImageView addImageView(LinearLayout linearLayout, boolean isEnd) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        if (!isEnd)
            lp.setMarginEnd((int) context.getResources().getDimension(R.dimen.news_netease_item2_img_margen_end));
        linearLayout.addView(imageView, lp);
        return imageView;
    }
}
