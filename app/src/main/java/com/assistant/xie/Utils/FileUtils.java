package com.assistant.xie.Utils;

import android.os.Environment;

/**
 * Created by XIE on 2017/11/23.
 */

public class FileUtils {

    /**
     * 判断sd卡是否可用
     * @return true:可用 false:不可用
     */
    public static boolean hasSdCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // sd card 可用
            return true;
        } else {
            // 当前不可用
            return false;
        }
    }
}
