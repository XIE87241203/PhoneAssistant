package com.assistant.xie.model.news.channel.netease.bean;

import com.assistant.xie.model.news.channel.netease.bean.NewsInfo;

import java.util.List;

/**
 * Created by XIE on 2017/12/29.
 * 网易新闻信息类
 */

public class ImgListNewsInfo extends NewsInfo {
    private List<String> imgextra;//类型2的排在imgsrc后的图片列表

    public List<String> getImgextra() {
        return imgextra;
    }

    public void setImgextra(List<String> imgextra) {
        this.imgextra = imgextra;
    }
}
