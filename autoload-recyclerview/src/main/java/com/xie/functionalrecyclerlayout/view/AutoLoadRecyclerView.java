package com.xie.functionalrecyclerlayout.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;


import com.xie.functionalrecyclerlayout.adapter.AutoLoadRecyclerAdapter;

import java.util.List;

/**
 * Created by iSmartGo-XIE on 2017/7/5.
 * 需要配合AutoLoadRecyclerAdapter使用
 * 调用setItemMargin()方法可以给RecyclerView快速设置item间距
 * @see AutoLoadRecyclerAdapter
 */

public class AutoLoadRecyclerView extends RecyclerView {

    private Context context;
    private AutoLoadRecyclerAdapter autoLoadRecyclerAdapter;

    /**
     * @param context context
     */
    public AutoLoadRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof AutoLoadRecyclerAdapter) {
            this.autoLoadRecyclerAdapter = (AutoLoadRecyclerAdapter) adapter;
        }
    }

    private void initView(Context context) {
        this.context = context;
        setLayoutManager(new LinearLayoutManager(context));

        //设置滚动监听
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (autoLoadRecyclerAdapter != null && autoLoadRecyclerAdapter.isAutoLoadMore() && dy > 0 &&
                        !recyclerView.canScrollVertically(1) && !autoLoadRecyclerAdapter.isPullLoading()
                        && autoLoadRecyclerAdapter.getRealItemCount() > 0) {
                    //上滑操作
                    autoLoadRecyclerAdapter.startLoadMore();
                }
            }
        });
    }

    /**
     * 打开默认局部刷新动画
     */
    public void openDefaultAnimator() {
        this.getItemAnimator().setAddDuration(120);
        this.getItemAnimator().setChangeDuration(250);
        this.getItemAnimator().setMoveDuration(250);
        this.getItemAnimator().setRemoveDuration(120);
        ((SimpleItemAnimator) this.getItemAnimator()).setSupportsChangeAnimations(true);
    }

    /**
     * 关闭默认局部刷新动画
     */
    public void closeDefaultAnimator() {
        this.getItemAnimator().setAddDuration(0);
        this.getItemAnimator().setChangeDuration(0);
        this.getItemAnimator().setMoveDuration(0);
        this.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) this.getItemAnimator()).setSupportsChangeAnimations(false);
    }


    public AutoLoadRecyclerAdapter getAutoLoadRecyclerAdapter() {
        return autoLoadRecyclerAdapter;
    }

    /**
     * @param leftMargin   item的左边距（不包括头部和尾部）(单位dp)
     * @param topMargin    item的上边距（不包括头部和尾部）(单位dp)
     * @param rightMargin  item的右边距（不包括头部和尾部）(单位dp)
     * @param bottomMargin item的下边距（不包括头部和尾部）(单位dp)
     */
    public void setItemMargin(float leftMargin, float topMargin, float rightMargin, float bottomMargin) {
        //设置边距
        addItemDecoration(new SpaceItemDecoration(context, this, leftMargin, topMargin, rightMargin, bottomMargin));
    }

    /**
     * 类似Listview的item间隔,单位dp
     */
    public void setItemMargin(float dividerHeight) {
        //设置边距
        addItemDecoration(new SpaceItemDecoration(context, this, dividerHeight));
    }
}

