package com.assistant.xie.Utils;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by XIE on 2017/11/27.
 * 悬浮窗管理类
 */

public class FloatingManager {
    private WindowManager mWindowManager;
    private static FloatingManager mInstance;

    public static FloatingManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FloatingManager(context.getApplicationContext());
        }
        return mInstance;
    }

    private FloatingManager(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);//获得WindowManager对象
    }

    /**
     * 添加悬浮窗
     *
     * @param view view
     * @param params params
     * @return boolean
     */
    public boolean addView(View view, WindowManager.LayoutParams params) {
        try {
            mWindowManager.addView(view, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 移除悬浮窗
     *
     * @param view view
     * @return boolean
     */
    public boolean removeView(View view) {
        try {
            mWindowManager.removeView(view);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新悬浮窗参数
     *
     * @param view view
     * @param params params
     * @return boolean
     */
    public boolean updateView(View view, WindowManager.LayoutParams params) {
        try {
            mWindowManager.updateViewLayout(view, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
