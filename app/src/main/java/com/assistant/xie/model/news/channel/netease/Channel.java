package com.assistant.xie.model.news.channel.netease;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by XIE on 2018/1/16.
 */

public class Channel implements Serializable {

    private String name;
    private String code;

    public Channel(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code + "wangning";
    }

    public void setCode(String code) {
        this.code = code;
    }

}
