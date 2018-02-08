package com.xie.functionalrecyclerlayout.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xie.functionalrecyclerlayout.R;

/**
 * Created by XIE on 2018/1/12.
 * 多功能RecyclerView整合布局
 */

public class FunctionRecyclerLayout extends RelativeLayout {
    private AutoLoadRecyclerView recyclerView;
    private TextView tv_top_btn;//回到顶部按钮
    private boolean topBtnEnable;//回到顶部按钮是否可用
    private int firstPageItemNum = -1;//第一页有多少item
    private SwipeRefreshLayout refreshLayout;//刷新布局

    public FunctionRecyclerLayout(Context context) {
        super(context);
        init(context);
    }

    public FunctionRecyclerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FunctionRecyclerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        refreshLayout = new SwipeRefreshLayout(context);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(context,R.color.colorPrimary));
        LayoutParams refreshLP = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(refreshLayout,refreshLP);
        recyclerView = new AutoLoadRecyclerView(context);
        recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (topBtnEnable) {
                    int firstVisibleItemPosition = 0;
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) layoutManager);
                        if (linearLayoutManager.findFirstVisibleItemPosition() == 0 && linearLayoutManager.findLastVisibleItemPosition() != firstPageItemNum) {
                            firstPageItemNum = linearLayoutManager.findLastVisibleItemPosition();
                        }
                        firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        StaggeredGridLayoutManager staggeredGridLayoutManager = ((StaggeredGridLayoutManager) layoutManager);
                        if (staggeredGridLayoutManager.findFirstVisibleItemPositions(null)[0] == 0 && staggeredGridLayoutManager.findLastVisibleItemPositions(null)[1] != firstPageItemNum) {
                            firstPageItemNum = staggeredGridLayoutManager.findLastVisibleItemPositions(null)[1];
                        }
                        firstVisibleItemPosition = staggeredGridLayoutManager.findFirstVisibleItemPositions(null)[0];
                    }
                    if (firstVisibleItemPosition > firstPageItemNum) {
                        if (tv_top_btn.getVisibility() == INVISIBLE)
                            tv_top_btn.setVisibility(VISIBLE);
                    } else {
                        if (tv_top_btn.getVisibility() == VISIBLE)
                            tv_top_btn.setVisibility(INVISIBLE);
                    }
                }
            }
        });
        LayoutParams recyclerLP = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        refreshLayout.addView(recyclerView, recyclerLP);
        tv_top_btn = new TextView(context);
        tv_top_btn.setText(context.getString(R.string.function_top_btn_text));
        tv_top_btn.setTextColor(ContextCompat.getColor(context, R.color.function_top_btn_text_color));
        tv_top_btn.setBackgroundResource(R.drawable.bg_radius_7f000000_20dp);
        tv_top_btn.setGravity(Gravity.CENTER);
        tv_top_btn.setTextSize(25);
        tv_top_btn.setPadding(0, 0, 0, (int) context.getResources().getDimension(R.dimen.function_top_btn_padding_bottom));
        LayoutParams topBtnLP = new LayoutParams((int) context.getResources().getDimension(R.dimen.function_top_btn_height), (int) context.getResources().getDimension(R.dimen.function_top_btn_width));
        topBtnLP.setMargins(0, 0, (int) context.getResources().getDimension(R.dimen.function_top_btn_margen_end), (int) context.getResources().getDimension(R.dimen.function_top_btn_margen_bottom));
        topBtnLP.addRule(ALIGN_PARENT_BOTTOM);
        topBtnLP.addRule(ALIGN_PARENT_END);
        addView(tv_top_btn, topBtnLP);
        tv_top_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                smollScrollToTopFast();
            }
        });
        tv_top_btn.setVisibility(INVISIBLE);
    }

    /**
     * 目前动画只支持瀑布流和线性布局，其他布局无动画
     */
    private void smollScrollToTopFast() {
        if (firstPageItemNum != -1 && recyclerView.getAdapter().getItemCount() > 10 && recyclerView.getAdapter().getItemCount() > 2 * firstPageItemNum) {
            recyclerView.scrollToPosition(2 * firstPageItemNum);
        }
        recyclerView.smoothScrollToPosition(0);
    }

    /**
     * 设置回到顶部箭头是否可用
     *
     * @param enable enable
     */
    public void setTopBtnEnable(boolean enable) {
        topBtnEnable = enable;
        if (!enable) {
            firstPageItemNum = -1;
        }
    }

    public AutoLoadRecyclerView getRecyclerView() {
        return recyclerView;
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }
}
