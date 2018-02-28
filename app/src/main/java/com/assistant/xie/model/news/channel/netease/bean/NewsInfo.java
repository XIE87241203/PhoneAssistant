package com.assistant.xie.model.news.channel.netease.bean;

/**
 * Created by XIE on 2018/2/28.
 * 新闻信息类
 */

public class NewsInfo {
    //普通类型
    public static final int TYPE_DEFAULT_NEWS_INFO = 1;
    //图片列表类型
    public static final int TYPE_IMG_LIST_NEWS_INFO = 2;
    //大图类型
    public static final int TYPE_LAGER_IMG_NEWS_INFO = 3;

    private String title;//标题
    private String imgsrc;//图片地址
    private String commentCount;//评论数
    private String ptime;//发布时间
    private String skipURL;//优先跳转链接
    private String url;//次要跳转链接
    private int imgsrc3gtype;//新闻类型 1：普通新闻 2：多图新闻 3：大图

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

    public String getSkipURL() {
        return skipURL;
    }

    public void setSkipURL(String skipURL) {
        this.skipURL = skipURL;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getImgsrc3gtype() {
        return imgsrc3gtype;
    }

    public void setImgsrc3gtype(int imgsrc3gtype) {
        this.imgsrc3gtype = imgsrc3gtype;
    }
}
