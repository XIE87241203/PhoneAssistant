package com.assistant.xie.model.news;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.assistant.xie.R;
import com.assistant.xie.model.base.BaseActivity;
import com.assistant.xie.model.news.channel.netease.Channel;
import com.assistant.xie.model.news.channel.netease.NewsFragment;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends BaseActivity {
    private ViewPager viewpager;
    private TabLayout tablayout;
    private List<NewsFragment> fragmentList;
    private List<Channel> channelList;
    private NewsFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initView();
        initData();
    }

    private void initView() {
        viewpager = findViewById(R.id.viewpager);
        tablayout = findViewById(R.id.tablayout);
        fragmentList = new ArrayList<>();
        channelList = new ArrayList<>();
        adapter = new NewsFragmentPagerAdapter(getSupportFragmentManager(), channelList, fragmentList);
        viewpager.setAdapter(adapter);
        //设置最多缓存页面
        viewpager.setOffscreenPageLimit(3);
        tablayout.setupWithViewPager(viewpager);
    }

    private void initData() {
        channelList.addAll(getChannelList());
        for (Channel channel : channelList) {
            fragmentList.add(NewsFragment.newInstance(channel));
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取频道列表
     *
     * @return
     */
    private List<Channel> getChannelList() {
        List<Channel> channelList = new ArrayList<>();
        channelList.add(new Channel("要闻", "BBM54PGA"));
        channelList.add(new Channel("社会", "BCR1UC1Q"));
        channelList.add(new Channel("国内", "BD29LPUB"));
        channelList.add(new Channel("国际", "BD29MJTV"));
        channelList.add(new Channel("科技", "BA8D4A3R"));
        channelList.add(new Channel("娱乐", "BA10TA81"));
        channelList.add(new Channel("游戏", "BAI6RHDK"));
        channelList.add(new Channel("体育", "BA8E6OEO"));
        channelList.add(new Channel("军事", "BAI67OGG"));
        return channelList;
    }
}
