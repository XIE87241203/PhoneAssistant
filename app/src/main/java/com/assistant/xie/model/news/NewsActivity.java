package com.assistant.xie.model.news;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.assistant.xie.R;
import com.assistant.xie.Utils.HttpRequestUtils;
import com.assistant.xie.model.base.BaseActivity;
import com.assistant.xie.model.news.channel.netease.NewsFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class NewsActivity extends BaseActivity implements NewsFragment.OnFragmentInteractionListener {
    private ViewPager viewpager;
    private TabLayout tablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initView();
    }

    private void initView() {
        viewpager = findViewById(R.id.viewpager);
        tablayout = findViewById(R.id.tablayout);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(NewsFragment.newInstance(NewsFragment.CHANNEL_163));
        fragmentList.add(NewsFragment.newInstance(NewsFragment.CHANNEL_163));
        //设置标题
        List<String> titleList = new ArrayList<>();
        titleList.add("新闻");
        titleList.add("娱乐");
        NewsFragmentPagerAdapter adapter = new NewsFragmentPagerAdapter(getSupportFragmentManager(),titleList,fragmentList);
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
