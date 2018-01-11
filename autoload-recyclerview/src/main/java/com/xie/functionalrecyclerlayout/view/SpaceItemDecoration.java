package com.xie.functionalrecyclerlayout.view;

/**
 * Created by iSmartGo-XIE on 2017/10/30.、
 * RecycleView的item间距
 */

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xie.functionalrecyclerlayout.adapter.AutoLoadRecyclerAdapter;
import com.xie.functionalrecyclerlayout.utils.DisplayUtil;

/**
 * recycleview的item边距
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    //上下所有均有间隔
    private float leftMargin, rightMargin;
    private float topMargin, bottomMargin, firstMargin;
    private float dividerHeight;
    private AutoLoadRecyclerView recyclerView;
    private Context context;

    /**
     * 类似Listview的item间隔,单位dp
     *
     * @param dividerHeight
     */
    public SpaceItemDecoration(Context context, AutoLoadRecyclerView recyclerView, float dividerHeight) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.dividerHeight = dividerHeight;
    }

    public SpaceItemDecoration(Context context, AutoLoadRecyclerView recyclerView, float leftMargin, float topMargin, float rightMargin, float bottomMargin) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.leftMargin = leftMargin;
        this.topMargin = topMargin;
        this.rightMargin = rightMargin;
        this.bottomMargin = bottomMargin;
    }

    /**
     * @param leftMargin   左边距
     * @param topMargin    上边距
     * @param rightMargin  右边距
     * @param bottomMargin 下边距
     * @param firstMargin  第一个item附加的上边距
     */
    public SpaceItemDecoration(Context context, AutoLoadRecyclerView recyclerView, float leftMargin, float topMargin, float rightMargin, float bottomMargin, float firstMargin) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.leftMargin = leftMargin;
        this.topMargin = topMargin;
        this.rightMargin = rightMargin;
        this.bottomMargin = bottomMargin;
        this.firstMargin = firstMargin;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        AutoLoadRecyclerAdapter adapter = recyclerView.getAutoLoadRecyclerAdapter();
        //去除头部和尾部的边距
        if (adapter != null && parent.getChildAdapterPosition(view) >= adapter.getHeadersCount() && parent.getChildAdapterPosition(view) < adapter.getHeadersCount() + adapter.getRealItemCount()) {
            if (dividerHeight != 0) {
                if (parent.getChildAdapterPosition(view) > adapter.getHeadersCount()) {
                    outRect.top = DisplayUtil.dip2px(context, dividerHeight);
                }
            } else {
                if (firstMargin != 0 && parent.getChildAdapterPosition(view) == adapter.getHeadersCount()) {
                    outRect.top = DisplayUtil.dip2px(context, topMargin + firstMargin);
                } else {
                    outRect.top = DisplayUtil.dip2px(context, topMargin);
                }
                outRect.right = DisplayUtil.dip2px(context, rightMargin);
                outRect.left = DisplayUtil.dip2px(context, leftMargin);
                outRect.bottom = DisplayUtil.dip2px(context, bottomMargin);
            }
        }
    }

}