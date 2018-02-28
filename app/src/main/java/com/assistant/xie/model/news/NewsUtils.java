package com.assistant.xie.model.news;

import android.content.Context;

import com.assistant.xie.R;
import com.assistant.xie.Utils.DisplayUtil;
import com.assistant.xie.model.news.channel.netease.bean.NewsInfo;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by XIE on 2018/2/28.
 * 新闻工具类
 */

public class NewsUtils {
    private static NewsUtils instance;
    private Context context;

    private NewsUtils(Context context) {
        this.context = context.getApplicationContext();
    }

    public static NewsUtils getInstance(Context context) {
        if (instance == null) {
            instance = new NewsUtils(context);
        }
        return instance;
    }

    /**
     * 评论数单位转换
     *
     * @param commentsNum 评论数
     * @return String
     */
    public String commentsUnitConversion(int commentsNum) {
        String unit = "";
        Double resuleDouble;
        if (commentsNum < 10000) {
            resuleDouble = (double) commentsNum;
        } else if (commentsNum < 100000000) {
            resuleDouble = ((double) commentsNum) / 10000;
            unit = "万";
        } else {
            resuleDouble = ((double) commentsNum) / 100000000;
            unit = "亿";
        }
        return new DecimalFormat("#.#").format(resuleDouble) + unit;
    }

    public String dateUnitConversion(String date) {
        SimpleDateFormat newsDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat omitDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String result = "";
        try {
            Date newsDate = newsDateFormat.parse(date);
            long newsTime = newsDateFormat.parse(date).getTime();
            Date nowDate = new Date();
            long nowTime = new Date().getTime();
            long difference = (nowTime - newsTime) / 1000;
            if (difference < 0) {
                result = date;
            } else if (difference < 60) {
                result = (difference) + "秒前";
            } else if (difference < 60 * 60) {
                result = (difference / 60) + "分钟前";
            } else if (difference < 24 * 60 * 60) {
                result = (difference / (60 * 60)) + "小时前";
            } else if (difference < 7 * 24 * 60 * 60) {
                result = (difference / (24 * 60 * 60)) + "日前";
            } else {
                result = omitDateFormat.format(newsTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 补全图片大小参数
     *
     * @param url          原图片url
     * @param imgsrc3gtype NewsInfo的类型
     * @return 补全的图片url
     * @see NewsInfo
     */
    public String getImgRequestUrl(String url, int imgsrc3gtype) {
        int width = 0;
        int height = 0;
        switch (imgsrc3gtype) {
            case NewsInfo.TYPE_DEFAULT_NEWS_INFO:
            case NewsInfo.TYPE_IMG_LIST_NEWS_INFO:
                width = (int) context.getResources().getDimension(R.dimen.news_netease_item1_img_width);
                height = (int) (context.getResources().getDimension(R.dimen.news_netease_item1_height) - context.getResources().getDimension(R.dimen.news_netease_item1_vertical_padding) * 2);
                break;
            case NewsInfo.TYPE_LAGER_IMG_NEWS_INFO:
                width = (int) (DisplayUtil.getInstance().getScreenWidth(context) - context.getResources().getDimension(R.dimen.news_netease_item3_horizontal_padding));
                height = (int) context.getResources().getDimension(R.dimen.news_netease_item3_img_height);
                break;
        }
        return url + "?imageView&thumbnail=" + width + "y" + height + "&quality=45&type=webp&interlace=1&enlarge=1";
    }
}
