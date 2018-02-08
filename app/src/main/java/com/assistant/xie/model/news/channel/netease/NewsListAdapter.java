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
import com.assistant.xie.Utils.CommonMethods;
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
        if (CommonMethods.isEmptyString(list.get(position).getSkipURL()) || getItemViewTypeNew(position) == 0) {
            intent.putExtra("url", list.get(position).getUrl());
        } else {
            intent.putExtra("url", list.get(position).getSkipURL());
        }
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_comment_count = holder.getView(R.id.tv_comment_count);
        switch (getItemViewTypeNew(position)) {
            case 0:
                ImageView iv_img = holder.getView(R.id.iv_img);
                if(CommonMethods.isEmptyString(list.get(position).getImgsrc())){
                    iv_img.setVisibility(View.GONE);
                }else{
                    //加载图片
                    iv_img.setVisibility(View.VISIBLE);
                    GlideUtils.loadImage(context, list.get(position).getImgsrc(), R.color.defaultImageHolderColor, iv_img);
                }
                break;
            case 1:
                List<ImageView> imageViewList = new ArrayList<>();
                imageViewList.add((ImageView) holder.getView(R.id.img_1));
                imageViewList.add((ImageView) holder.getView(R.id.img_2));
                imageViewList.add((ImageView) holder.getView(R.id.img_3));
                //加载图片
                GlideUtils.loadImage(context, list.get(position).getImgsrc(), R.color.defaultImageHolderColor, imageViewList.get(0));
                int imgNum = 3;
                if (list.get(position).getImgextra().size() <= 3) {
                    imgNum = list.get(position).getImgextra().size();
                }
                for (int i = 0; i < imgNum; i++) {
                    //加载图片
                    GlideUtils.loadImage(context, list.get(position).getImgextra().get(i), R.color.defaultImageHolderColor, imageViewList.get(i + 1));
                }
                break;
            case 2:
                ImageView iv_image = holder.getView(R.id.iv_image);
                GlideUtils.loadImage(context, list.get(position).getImgsrc(), R.color.defaultImageHolderColor, iv_image);
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
}
