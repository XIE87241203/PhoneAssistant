package com.assistant.xie.model.news.channel.netease;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assistant.xie.R;
import com.assistant.xie.Utils.DisplayUtil;
import com.assistant.xie.Utils.HttpRequestUtils;
import com.assistant.xie.Utils.NetErrorLayoutManager;
import com.assistant.xie.model.base.BaseFragment;
import com.assistant.xie.model.news.NewsInfoParse;
import com.xie.functionalrecyclerlayout.adapter.AutoLoadRecyclerAdapter;
import com.xie.functionalrecyclerlayout.view.AutoLoadRecyclerView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends BaseFragment implements AutoLoadRecyclerAdapter.OnLoadMoreListener {
    public static final int CHANNEL_163 = 1;//网易新闻
    private static final String ARG_CHANNEL = "channel";
    private int channel;
    private AutoLoadRecyclerView recyclerView;
    private NewsListAdapter adapter;
    private List<NewsInfo> dataList;
    private NetErrorLayoutManager netErrorLayoutManager;

    private OnFragmentInteractionListener mListener;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * @param channel 新闻来源
     * @return A new instance of fragment NewsFragment.
     * @see #CHANNEL_163
     */
    public static NewsFragment newInstance(int channel) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CHANNEL, channel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            channel = getArguments().getInt(ARG_CHANNEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        initView(rootView);
        initData();
        return rootView;
    }

    private void initData() {
        requestData(false);
    }

    private void requestData(final boolean isLoadMore) {
        String url = "http://3g.163.com/touch/reconstruct/article/list/BBM54PGAwangning/";
        int offset;
        if(isLoadMore){
            offset = dataList.size();
        }else{
            offset = 0;
        }
        url = url + offset + "-10.html";
        HttpRequestUtils.getInstance().request(url, HttpRequestUtils.TYPE_REQUEST_GET, null, 0, new HttpRequestUtils.HttpRequestCallback() {
            @Override
            public void onResponse(final String responseStr, int httpWhat) {
                if (getActivity() != null) {
                    adapter.finishLoadMore();
                    final List<NewsInfo> result = NewsInfoParse.parseNeteaseNews(responseStr);
                    if (result != null) {
                        if (isLoadMore) {
                            adapter.addDataAndRefreshData(dataList, result);
                        } else {
                            netErrorLayoutManager.setNetErrorLayoutVisibility(false);
                            adapter.resetDataAndRefreshData(recyclerView, dataList, result);
                        }
                    } else if (!isLoadMore) {
                        netErrorLayoutManager.setNetErrorLayoutVisibility(true);
                    }
                }
            }

            @Override
            public void onFailure(IOException e, int httpWhat) {
                adapter.finishLoadMore();
                if (!isLoadMore) {
                    netErrorLayoutManager.setNetErrorLayoutVisibility(true);
                }
            }
        });
    }

    @Override
    public void onLoadMore() {
        requestData(true);
    }

    private void initView(View rootView) {
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setItemMargin(0.5f);
        dataList = new ArrayList<>();
        adapter = new NewsListAdapter(getContext(), dataList);
        recyclerView.setAdapter(adapter);
        adapter.setAutoLoadEnable(true);
        adapter.setOnLoadMoreListener(this);
        netErrorLayoutManager = new NetErrorLayoutManager(rootView, recyclerView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData(false);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Fragment与Activity传递信息
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
