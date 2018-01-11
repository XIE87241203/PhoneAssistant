package com.assistant.xie.model.news.channel.netease;

import java.util.List;

/**
 * Created by XIE on 2017/12/29.
 * 网易新闻信息类
 */

public class NewsInfo {
    private String title;//标题
    private String imgsrc;//图片地址
    private String commentCount;//评论数
    private String ptime;//发布时间
    private int imgsrc3gtype;//新闻类型 1：普通新闻 2：多图新闻 3：大图
    private String url;//类型1用的跳转链接
    private String skipURL;//类型2,3用的跳转链接
    private List<String> imgextra;//类型2的排在imgsrc后的图片列表
    private String skipType;//文章类型 imgsrc3gtype=3时使用 其中：photoset图集，video视频


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public int getImgsrc3gtype() {
        return imgsrc3gtype;
    }

    public void setImgsrc3gtype(int imgsrc3gtype) {
        this.imgsrc3gtype = imgsrc3gtype;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSkipURL() {
        return skipURL;
    }

    public void setSkipURL(String skipURL) {
        this.skipURL = skipURL;
    }

    public List<String> getImgextra() {
        return imgextra;
    }

    public void setImgextra(List<String> imgextra) {
        this.imgextra = imgextra;
    }
}
