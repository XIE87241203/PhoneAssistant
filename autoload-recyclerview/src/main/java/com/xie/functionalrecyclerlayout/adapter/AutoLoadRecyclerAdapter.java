package com.xie.functionalrecyclerlayout.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xie.functionalrecyclerlayout.holder.BaseRecyclerViewHolder;
import com.xie.functionalrecyclerlayout.utils.DisplayUtil;
import com.xie.functionalrecyclerlayout.view.AutoLoadFooter;
import com.xie.functionalrecyclerlayout.view.AutoLoadRecyclerView;

import java.util.List;

/**
 * Created by iSmartGo-XIE on 2017/7/6.
 * 支持设置头部和脚部功能，同时可以设置一个空布局和位于头部和item的一个副头部（一般用于添加边距）
 *
 * addDataAndRefreshData和resetDataAndRefreshData方法可以用于快速添加数据
 * 结合AutoLoadRecyclerView使用
 * @see AutoLoadRecyclerView
 */

public abstract class AutoLoadRecyclerAdapter<T extends BaseRecyclerViewHolder> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_NULL_DATA_HEADER = 200000;//空布局头部
    private static final int BASE_ITEM_TYPE_FIRST_MARGEN_TOP = 200001;//第一个item的上边距View
    private static final int BASE_ITEM_TYPE_FOOTER = 200002;
    private static final int SPECIAL_ITEM_TYPE_REFRESH_FOOTER = 1000000;

    protected Context context;

    //加载更多监听
    private OnLoadMoreListener onLoadMoreListener;
    //加载更多布局
    private AutoLoadFooter loadMoreFooterView;
    //加载更多状态
    private boolean isPullLoading = false;
    //自动加载开关
    private boolean isAutoLoadMore = false;

    //容器
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    //代替onCreateViewHolder
    protected abstract BaseRecyclerViewHolder onCreateViewHolderNew(ViewGroup parent, int viewType);

    //代替getItemViewType
    protected abstract int getItemViewTypeNew(int position);

    //代替onBindViewHolder
    protected abstract void onBindViewHolderNew(BaseRecyclerViewHolder holder, int position);

    //获取内容Item数量
    public abstract int getRealItemCount();

    /**
     * 不添加边距的构造方法
     *
     * @param context
     */
    public AutoLoadRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            //头部
            BaseRecyclerViewHolder holder = BaseRecyclerViewHolder.createViewHolder(parent.getContext(), mHeaderViews.get(viewType));
            return holder;

        } else if (mFootViews.get(viewType) != null) {
            //尾部
            BaseRecyclerViewHolder holder = BaseRecyclerViewHolder.createViewHolder(parent.getContext(), mFootViews.get(viewType));
            return holder;
        }
        //内容部分
        return onCreateViewHolderNew(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return getItemViewTypeNew(position - getHeadersCount());
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            return;
        }
        if (isFooterViewPos(position)) {
            return;
        }
        onBindViewHolderNew(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public void onViewAttachedToWindow(BaseRecyclerViewHolder holder) {
        //处理StaggeredGridLayout类型
        int position = holder.getAdapterPosition();
        if (position == RecyclerView.NO_POSITION) {
            position = holder.getLayoutPosition();
        }
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        //处理gridLayout类型

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (mHeaderViews.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    } else if (mFootViews.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null)
                        return spanSizeLookup.getSpanSize(position);
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    /**
     * 判断是不是Header
     *
     * @param position position
     * @return boolean
     */
    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    /**
     * 判断是不是Footer
     *
     * @param position position
     * @return boolean
     */
    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    /**
     * 添加Header
     *
     * @param view view
     */
    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    /**
     * 添加并隐藏空布局
     *
     * @return
     */
    public View addNullDataUIHeaderView(@LayoutRes int id) {
        //在外面套布局，防止GONE时显示异常
        LinearLayout linearLayout = new LinearLayout(context);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(lp);
        LayoutInflater.from(context).inflate(id, linearLayout);
        mHeaderViews.put(BASE_ITEM_TYPE_NULL_DATA_HEADER, linearLayout);
        setNullDataUIHeaderVisibility(false);
        return linearLayout;
    }

    /**
     * 移除空布局
     */
    public void removeNullDataUIHeaderView() {
        int index = mHeaderViews.indexOfKey(BASE_ITEM_TYPE_NULL_DATA_HEADER);
        if (index != -1) {
            mHeaderViews.remove(BASE_ITEM_TYPE_NULL_DATA_HEADER);
        }
    }

    /**
     * 设置空布局隐藏或显示
     *
     * @param isVisible
     */
    public void setNullDataUIHeaderVisibility(boolean isVisible) {
        int index = mHeaderViews.indexOfKey(BASE_ITEM_TYPE_NULL_DATA_HEADER);
        if (index != -1) {
            if (isVisible) {
                ((LinearLayout) mHeaderViews.get(BASE_ITEM_TYPE_NULL_DATA_HEADER)).getChildAt(0).setVisibility(View.VISIBLE);
            } else {
                ((LinearLayout) mHeaderViews.get(BASE_ITEM_TYPE_NULL_DATA_HEADER)).getChildAt(0).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 删除Header
     *
     * @param view view
     */
    public void removeHeaderView(View view) {
        int index = mHeaderViews.indexOfValue(view);
        if (index != -1) {
            mHeaderViews.removeAt(index);
        }
    }


    /**
     * 添加Footer
     *
     * @param view view
     */
    public void addFooterView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    /**
     * 删除Footer
     *
     * @param view view
     */
    public void removeFooterView(View view) {
        int index = mFootViews.indexOfValue(view);
        if (index != -1) {
            mFootViews.removeAt(index);
        }
    }

    /**
     * 设置刷新footer
     *
     * @param view view
     */
    private void setLoadMoreFooter(View view) {
        mFootViews.put(SPECIAL_ITEM_TYPE_REFRESH_FOOTER + BASE_ITEM_TYPE_FOOTER, view);
    }

    /**
     * 获取Header Item数量
     *
     * @return int
     */
    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    /**
     * 获取HeaderViews
     *
     * @return SparseArrayCompat
     */
    public SparseArrayCompat<View> getHeaderViews() {
        return mHeaderViews;
    }

    /**
     * 获取HeaderViews
     *
     * @return SparseArrayCompat
     */
    public SparseArrayCompat<View> getFootViews() {
        return mFootViews;
    }

    /**
     * 获取Footer Item数量
     *
     * @return int
     */
    public int getFootersCount() {
        return mFootViews.size();
    }


    /**
     * 开始加载
     */
    public void startLoadMore() {
        //过滤同一页面重复请求
        if (isPullLoading || onLoadMoreListener == null)
            return;
        loadMoreFooterView.setState(AutoLoadFooter.STATE_LOADING);
        isPullLoading = true;
        onLoadMoreListener.onLoadMore();
    }

    /**
     * 加载更多回调
     * @param onLoadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        if (loadMoreFooterView == null) {
            loadMoreFooterView = new AutoLoadFooter(context);
            loadMoreFooterView.setState(AutoLoadFooter.STATE_NORMAL);
            setLoadMoreFooter(loadMoreFooterView);
        }
    }

    /**
     * 加载完成
     */
    public void finishLoadMore() {
        if (loadMoreFooterView == null) return;
        loadMoreFooterView.setState(AutoLoadFooter.STATE_NORMAL);
        isPullLoading = false;
    }

    /**
     * 停止加载,显示“没有更多了”
     */
    public void stopLoadMore() {
        if (loadMoreFooterView == null) return;
        loadMoreFooterView.setState(AutoLoadFooter.STATE_HINT);
        isPullLoading = false;
    }

    public boolean isPullLoading() {
        return isPullLoading;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    /**
     * 自动加载开关
     *
     * @return isAutoLoadMore
     */
    public boolean isAutoLoadMore() {
        return isAutoLoadMore;
    }

    /**
     * 添加一个Item的上边距
     *
     * @param margenDp 间隔距离
     */
    public void addFristMargenTopView(float margenDp) {
        //添加上边距
        View paddingView = new View(context);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(context, margenDp));
        paddingView.setLayoutParams(lp);
        mHeaderViews.put(BASE_ITEM_TYPE_FIRST_MARGEN_TOP, paddingView);
    }

    /**
     * 开启或关闭自动加载功能(显示/隐藏加载提示)
     *
     * @param autoLoadMore boolean 若为true则往列表里面加入刷新footer
     */
    public void setAutoLoadMore(boolean autoLoadMore) {
        isAutoLoadMore = autoLoadMore;
        if (autoLoadMore) {
            if (loadMoreFooterView != null) {
                loadMoreFooterView.show();
            }
        } else {
            if (loadMoreFooterView != null) {
                loadMoreFooterView.hide();
            }
        }
    }

    /**
     * 暂停或者启动自动加载功能(仅仅暂停功能，加载提示还存在)
     *
     * @param autoLoadMore boolean 是否可自动刷新
     */
    public void setAutoLoadEnable(boolean autoLoadMore) {
        isAutoLoadMore = autoLoadMore;
    }

    /**
     * 合并list并刷新数据(两个list必须是同一数据类型)
     *
     * @param list     Adapter内的数据list
     * @param moreList 需要添加的数据list
     */
    public void addDataAndRefreshData(@NonNull List list, List moreList) {
        if (moreList == null || moreList.isEmpty()) return;
        int size = list.size() + getHeadersCount();
        list.addAll(moreList);
        if (size > 0) {
            notifyItemChanged(size, moreList.size());
        } else {
            notifyDataSetChanged();
        }
    }

    /**
     * 重设并刷新数据(两个list必须是同一数据类型)
     *
     * @param list     Adapter内的数据list
     * @param moreList 需要添加的数据list
     */
    public void resetDataAndRefreshData(@NonNull RecyclerView recyclerView, @NonNull List list, List moreList) {
        list.clear();
        if (moreList != null && !moreList.isEmpty()) {
            list.addAll(moreList);
        }
        notifyDataSetChanged();
        if (!list.isEmpty()) {
            recyclerView.scrollToPosition(0);
        }
    }

}
