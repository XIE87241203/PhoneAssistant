package com.assistant.xie.model.news;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.assistant.xie.model.news.channel.netease.Channel;
import com.assistant.xie.model.news.channel.netease.NewsFragment;

import java.util.List;

/**
 * Created by XIE on 2017/12/29.
 * FragmentPagerAdapter
 */

public class NewsFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<NewsFragment> fragmentList;
    private List<Channel> channelList;

    NewsFragmentPagerAdapter(FragmentManager fm,List<Channel> channelList, List<NewsFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.channelList = channelList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return channelList.get(position).getName();
    }
}
