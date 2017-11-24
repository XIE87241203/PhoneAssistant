package com.assistant.xie.model.phone_state;

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
 * Created by XIE on 2017/11/24.
 * 手机状态Adapter
 */

public class PhoneStateAdapter extends ListViewBaseAdapter<PhoneStateInfo> {
    PhoneStateAdapter(Context context, List<PhoneStateInfo> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    public void convert(ListViewViewHolder holder, PhoneStateInfo phoneStateInfo) {
        holder.setText(R.id.tv_item, phoneStateInfo.getInfo());
    }
}
