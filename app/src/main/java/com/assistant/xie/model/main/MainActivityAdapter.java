package com.assistant.xie.model.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.assistant.xie.R;
import com.assistant.xie.Utils.ListViewBaseAdapter;
import com.assistant.xie.Utils.ListViewViewHolder;

import java.util.List;

/**
 * Created by XIE on 2017/11/23.
 * 首页列表的Adapter
 */

public class MainActivityAdapter extends ListViewBaseAdapter<MainActivityInfo> {

    MainActivityAdapter(Context context, List<MainActivityInfo> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    public void convert(ListViewViewHolder holder, MainActivityInfo mainActivityInfo) {
        holder.setText(R.id.tv_title, mainActivityInfo.getName());
        holder.setText(R.id.tv_dec, mainActivityInfo.getDescribe());
    }
}
