package com.assistant.xie.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by XIE on 2017/11/24.
 * ListView通用adapter
 */

public abstract class ListViewBaseAdapter<T> extends BaseAdapter {
    private Context context;
    private List<T> list;
    private int layoutId;

    public ListViewBaseAdapter(Context context, List<T> list, int layoutId) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewViewHolder holder = ListViewViewHolder.get(context, convertView, parent, layoutId, position);
        convert(holder, list.get(position));
        return holder.getConvertView();
    }

    //将此方法开放出去，在adapter中实现
    public abstract void convert(ListViewViewHolder holder, T t);
}
