package com.assistant.xie.model.news;

import com.assistant.xie.Utils.CommonMethods;
import com.assistant.xie.model.news.channel.netease.Channel;
import com.assistant.xie.model.news.channel.netease.bean.ImgListNewsInfo;
import com.assistant.xie.model.news.channel.netease.bean.LargerImgNewsInfo;
import com.assistant.xie.model.news.channel.netease.bean.NewsInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIE on 2017/12/29.
 * 解析新闻接口类
 */

public class NewsInfoParse {

    /**
     * 解析网易新文
     *
     * @param responseStr responseStr
     * @param channelCode 频道号
     * @return List<NewsInfo>
     * @see Channel
     */
    public static List<NewsInfo> parseNeteaseNews(String responseStr, String channelCode) {
        List<NewsInfo> result = null;
        if (responseStr != null && !CommonMethods.isEmptyString(responseStr)) {
            try {
                responseStr = responseStr.substring("artiList(".length(), responseStr.length() - 1);
                JSONObject root = new JSONObject(responseStr);
                JSONArray datas = root.getJSONArray(channelCode);
                result = new ArrayList<>();
                for (int i = 0; i < datas.length(); i++) {
                    JSONObject data = (JSONObject) datas.get(i);
                    NewsInfo info;
                    switch (data.getInt("imgsrc3gtype")) {
                        case NewsInfo.TYPE_IMG_LIST_NEWS_INFO:
                            info = new ImgListNewsInfo();
                            JSONArray jsonImgextra = data.optJSONArray("imgextra");
                            if (jsonImgextra != null) {
                                List<String> imgextra = new ArrayList<>();
                                for (int j = 0; j < jsonImgextra.length(); j++) {
                                    imgextra.add(((JSONObject) jsonImgextra.get(j)).getString("imgsrc"));
                                }
                                ((ImgListNewsInfo) info).setImgextra(imgextra);
                            }
                            break;
                        case NewsInfo.TYPE_LAGER_IMG_NEWS_INFO:
                            info = new LargerImgNewsInfo();
                            ((LargerImgNewsInfo) info).setSkipType(data.optString("skipType"));
                            break;
                        default:
                            info = new NewsInfo();
                            break;
                    }
                    info.setTitle(data.getString("title"));
                    info.setCommentCount(data.getInt("commentCount"));
                    info.setImgsrc(data.getString("imgsrc"));
                    info.setPtime(data.getString("ptime"));
                    info.setImgsrc3gtype(data.getInt("imgsrc3gtype"));
                    info.setUrl(data.getString("url"));
                    info.setSkipURL(data.optString("skipURL"));
                    result.add(info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
