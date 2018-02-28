package com.assistant.xie.model.news.channel.netease.bean;

/**
 * Created by XIE on 2018/2/28.
 */

public class LargerImgNewsInfo extends NewsInfo {
    private String skipType;//文章类型 imgsrc3gtype=3时使用 其中：photoset图集，video视频

    public String getSkipType() {
        return skipType;
    }

    public void setSkipType(String skipType) {
        this.skipType = skipType;
    }
}
