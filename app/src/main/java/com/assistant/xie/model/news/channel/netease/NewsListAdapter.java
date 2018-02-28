package com.assistant.xie.model.news.channel.netease;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.assistant.xie.R;
import com.assistant.xie.Utils.CommonMethods;
import com.assistant.xie.Utils.GlideUtils;
import com.assistant.xie.model.base.BaseWebViewActivity;
import com.assistant.xie.model.news.NewsUtils;
import com.assistant.xie.model.news.channel.netease.bean.ImgListNewsInfo;
import com.assistant.xie.model.news.channel.netease.bean.LargerImgNewsInfo;
import com.assistant.xie.model.news.channel.netease.bean.NewsInfo;
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
            case NewsInfo.TYPE_IMG_LIST_NEWS_INFO:
                viewHolder = BaseRecyclerViewHolder.createViewHolder(context, parent, R.layout.news_netease_list_item_type2);
                break;
            case NewsInfo.TYPE_LAGER_IMG_NEWS_INFO:
                viewHolder = BaseRecyclerViewHolder.createViewHolder(context, parent, R.layout.news_netease_list_item_type3);
                break;
            default:
                viewHolder = BaseRecyclerViewHolder.createViewHolder(context, parent, R.layout.news_netease_list_item_type1);
                break;
        }
        return viewHolder;
    }

    @Override
    protected void onBindViewHolderNew(BaseRecyclerViewHolder holder, final int position) {
        final Intent intent = new Intent(context, BaseWebViewActivity.class);
        final NewsInfo newsInfo = list.get(position);
        if (CommonMethods.isEmptyString(newsInfo.getSkipURL()) || getItemViewTypeNew(position) == 0) {
            intent.putExtra("url", newsInfo.getUrl());
        } else {
            intent.putExtra("url", newsInfo.getSkipURL());
        }
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_comment_count = holder.getView(R.id.tv_comment_count);
        switch (newsInfo.getImgsrc3gtype()) {
            case NewsInfo.TYPE_IMG_LIST_NEWS_INFO:
                ImgListNewsInfo imgListNewsInfo = (ImgListNewsInfo) newsInfo;
                List<ImageView> imageViewList = new ArrayList<>();
                imageViewList.add((ImageView) holder.getView(R.id.img_1));
                imageViewList.add((ImageView) holder.getView(R.id.img_2));
                imageViewList.add((ImageView) holder.getView(R.id.img_3));
                //加载图片
                GlideUtils.loadImage(context, NewsUtils.getInstance(context).getImgRequestUrl(newsInfo.getImgsrc(), newsInfo.getImgsrc3gtype()), R.color.defaultImageHolderColor, imageViewList.get(0));
                int imgNum = 3;
                if (imgListNewsInfo.getImgextra().size() <= 3) {
                    imgNum = imgListNewsInfo.getImgextra().size();
                }
                for (int i = 0; i < imgNum; i++) {
                    //加载图片
                    GlideUtils.loadImage(context, NewsUtils.getInstance(context).getImgRequestUrl(imgListNewsInfo.getImgextra().get(i), newsInfo.getImgsrc3gtype()), R.color.defaultImageHolderColor, imageViewList.get(i + 1));
                }
                break;
            case NewsInfo.TYPE_LAGER_IMG_NEWS_INFO:
                LargerImgNewsInfo largerImgNewsInfo = (LargerImgNewsInfo) newsInfo;
                ImageView iv_image = holder.getView(R.id.iv_img);
                TextView tv_type = holder.getView(R.id.tv_type);
                GlideUtils.loadImage(context, NewsUtils.getInstance(context).getImgRequestUrl(largerImgNewsInfo.getImgsrc(), newsInfo.getImgsrc3gtype()), R.color.defaultImageHolderColor, iv_image);
                switch (largerImgNewsInfo.getSkipType()) {
                    case "photoset":
                        tv_type.setVisibility(View.VISIBLE);
                        tv_type.setText("图集");
                        break;
                    case "video":
                        tv_type.setVisibility(View.VISIBLE);
                        tv_type.setText("视频");
                        break;
                    default:
                        tv_type.setVisibility(View.GONE);
                        break;
                }
                break;
            default:
                ImageView iv_img = holder.getView(R.id.iv_img);
                if (CommonMethods.isEmptyString(newsInfo.getImgsrc())) {
                    iv_img.setVisibility(View.GONE);
                } else {
                    //加载图片
                    iv_img.setVisibility(View.VISIBLE);
                    GlideUtils.loadImage(context, NewsUtils.getInstance(context).getImgRequestUrl(newsInfo.getImgsrc(), newsInfo.getImgsrc3gtype()), R.color.defaultImageHolderColor, iv_img);
                }
                break;
        }
        //共同属性设置
        tv_title.setText(newsInfo.getTitle());
        tv_time.setText(NewsUtils.getInstance(context).dateUnitConversion(newsInfo.getPtime()));
        tv_comment_count.setText(String.format(context.getString(R.string.news_netease_comment_count), NewsUtils.getInstance(context).commentsUnitConversion(newsInfo.getCommentCount())));
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("title", newsInfo.getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected int getItemViewTypeNew(int position) {
        return list.get(position).getImgsrc3gtype();
    }

    @Override
    public int getRealItemCount() {
        return list.size();
    }
}
