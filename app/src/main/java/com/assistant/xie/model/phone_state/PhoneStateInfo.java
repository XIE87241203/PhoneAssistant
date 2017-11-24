package com.assistant.xie.model.phone_state;

/**
 * Created by XIE on 2017/11/24.
 * 手机状态信息
 */

public class PhoneStateInfo {
    private String title = "";
    private String value = "获取中...";

    public PhoneStateInfo(String title) {
        this.title = title;
    }

    public PhoneStateInfo(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInfo(){
        return title + ": " + value;
    }
}
