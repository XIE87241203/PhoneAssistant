package com.assistant.xie.Utils;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.LinearLayout;

import com.assistant.xie.R;

/**
 * Created by XIE on 2018/1/11.
 * 网络错误布局管理器
 */

public class NetErrorLayoutManager {
    private LinearLayout ll_net_error;//网络错误布局
    private View contentView;//内容View

    /**
     * 布局内需要有网络错误布局ll_net_error
     * @param rootView 总根布局
     * @param contentView 要隐藏的内容布局
     * @param onClickListener 点击网络错误布局的回调，传null则点击无动作
     */
    public NetErrorLayoutManager(View rootView, View contentView, View.OnClickListener onClickListener) {
        ll_net_error = rootView.findViewById(R.id.ll_net_error);
        this.contentView = contentView;
        if(onClickListener!=null){
            ll_net_error.setOnClickListener(onClickListener);
        }
    }

    public void setNetErrorLayoutVisibility(boolean visibility){
        if(visibility){
            ll_net_error.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.INVISIBLE);
        }else{
            ll_net_error.setVisibility(View.INVISIBLE);
            contentView.setVisibility(View.VISIBLE);
        }
    }
}
