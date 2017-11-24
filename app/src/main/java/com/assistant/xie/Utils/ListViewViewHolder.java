package com.assistant.xie.Utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by XIE on 2017/11/24.
 * 封装ViewHolder工具类
 */

public class ListViewViewHolder {

    /**
     * 封装ViewHolder工具类
     */
    private final SparseArray<View> mViews;
    private View mConvertView;

    private ListViewViewHolder(Context context, ViewGroup parent, int layoutId,
                               int position) {
        this.mViews = new SparseArray<>();
        mViews.clear();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public static ListViewViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ListViewViewHolder(context, parent, layoutId, position);
        }
        return (ListViewViewHolder) convertView.getTag();
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    //为TextView设置文本
    public ListViewViewHolder setText(int viewId,String text){
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }
}