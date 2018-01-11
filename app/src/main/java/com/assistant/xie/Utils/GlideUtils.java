package com.assistant.xie.Utils;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IntegerRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by XIE on 2018/1/4.
 * Glide图片加载框架
 * 不要再非主线程里面使用Glide加载图片，如果真的使用了，请把context参数换成getApplicationContext
 */

public class GlideUtils {

    /**
     * 显示网络照片
     * @param context 不要再非主线程里面使用Glide加载图片，如果真的使用了，请把context参数换成getApplicationContext
     * @param url 图片地址
     * @param placeHolder 占位图resourceId
     * @param imageView  imageView
     */
    public static void loadImage(Context context, String url, int placeHolder, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(placeHolder)
                .crossFade()
                .into(imageView);
    }

}
