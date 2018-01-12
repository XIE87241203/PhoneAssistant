package com.assistant.xie.model.news;

import com.assistant.xie.Utils.CommonMethods;
import com.assistant.xie.Utils.HttpRequestUtils;
import com.assistant.xie.model.news.channel.netease.NewsInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by XIE on 2017/12/29.
 * 解析新闻接口类
 */

public class NewsInfoParse {

    public static List<NewsInfo> parseNeteaseNews(String responseStr) {
        List<NewsInfo> result = null;
        if (responseStr != null && !CommonMethods.isEmptyString(responseStr)) {
            try {
                responseStr = responseStr.substring("artiList(".length(), responseStr.length() - 1);
                JSONObject root = new JSONObject(responseStr);
                JSONArray datas = root.getJSONArray("BBM54PGAwangning");
                result = new ArrayList<>();
                for (int i = 0; i < datas.length(); i++) {
                    JSONObject data = (JSONObject) datas.get(i);
                    NewsInfo info = new NewsInfo();
                    info.setTitle(data.getString("title"));
                    info.setCommentCount(data.getString("commentCount"));
                    info.setImgsrc(data.getString("imgsrc"));
                    info.setPtime(data.getString("ptime"));
                    info.setImgsrc3gtype(data.getInt("imgsrc3gtype"));
                    info.setUrl(data.getString("url"));
                    info.setSkipURL(data.optString("skipURL"));
                    JSONArray jsonImgextra = data.optJSONArray("imgextra");
                    if (jsonImgextra != null) {
                        List<String> imgextra = new ArrayList<>();
                        for (int j = 0; j < jsonImgextra.length(); j++) {
                            imgextra.add(((JSONObject) jsonImgextra.get(j)).getString("imgsrc"));
                        }
                        info.setImgextra(imgextra);
                    }

                    result.add(info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
