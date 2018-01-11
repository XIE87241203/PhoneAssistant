/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.xie.functionalrecyclerlayout.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xie.functionalrecyclerlayout.R;


@SuppressLint("InflateParams")
public class AutoLoadFooter extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_LOADING = 1;
    public final static int STATE_HINT = 2;


    private Context mContext;

    private View mContentView;
    //private View mProgressBar;
    private RelativeLayout rl_loading;
    private TextView mHintView;
    private ProgressDrawable mProgressDrawable;

    public AutoLoadFooter(Context context) {
        super(context);
        initView(context);
    }

    public AutoLoadFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setState(int state) {
        if (state == STATE_HINT) {
            rl_loading.setVisibility(View.GONE);
            mHintView.setVisibility(View.VISIBLE);
        } else if (state == STATE_LOADING) {
            rl_loading.setVisibility(View.VISIBLE);
            mHintView.setVisibility(View.GONE);
        } else if (state == STATE_NORMAL) {
            rl_loading.setVisibility(View.GONE);
            mHintView.setVisibility(View.GONE);
        }
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }

    private void initView(Context context) {
        mContext = context;
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.autoloadrecyclerview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mContentView = moreView.findViewById(R.id.rl_footer_content);
        //进度条
        mProgressDrawable = new ProgressDrawable();
        mProgressDrawable.setColor(0xff999999);
        mProgressDrawable.start();
        rl_loading = (RelativeLayout) moreView.findViewById(R.id.rl_loading);
        ImageView mImageView = (ImageView) moreView.findViewById(R.id.img_loading);
        mImageView.setImageDrawable(mProgressDrawable);
        mHintView = (TextView) moreView.findViewById(R.id.footer_hint_textview);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

}
