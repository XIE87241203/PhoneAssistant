package com.assistant.xie.model.main;

/**
 * Created by XIE on 2017/11/23.
 * 首页列表信息
 */

public class MainActivityInfo {
    private String name; //模块名
    private String describe; //模块描述
    private Class<?> cls; //模块主页

    /**
     * @param name     模块名
     * @param describe 模块描述
     * @param cls      //模块主页
     */
    public MainActivityInfo(String name, String describe, Class<?> cls) {
        this.name = name;
        this.describe = describe;
        this.cls = cls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }
}
