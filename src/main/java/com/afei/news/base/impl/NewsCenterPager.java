package com.afei.news.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.afei.news.MainActivity;
import com.afei.news.base.BaseMenuDetailPager;
import com.afei.news.base.BasePager;
import com.afei.news.base.impl.menudetail.InteractMenuDetailPager;
import com.afei.news.base.impl.menudetail.NewsMenuDetailPager;
import com.afei.news.base.impl.menudetail.PhotoMenuDetailPager;
import com.afei.news.base.impl.menudetail.TopicMenuDetailPager;
import com.afei.news.domain.NewsMenuData;
import com.afei.news.global.Constants;
import com.afei.news.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * 新闻中心
 * Created by fei on 2016/12/20.
 */

public class NewsCenterPager extends BasePager {
    public ArrayList<BaseMenuDetailPager> mMenuDetailPagers;
    public NewsMenuData newsMenuData;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("新闻");

        //看是否有缓存，有的话解析缓存
        String cache = CacheUtils.getCache(Constants.CATEGORIES_URL,mActivity);
        if (!TextUtils.isEmpty(cache)){
            processResult(cache);
        }
        //此处还要 从服务器解析数据的目的是：刷新缓存，优化用户体验
        getDataFromServer();
    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, Constants.CATEGORIES_URL, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;//获取json字符串
                processResult(result);
                CacheUtils.setCache(Constants.CATEGORIES_URL,result,mActivity);//写缓存

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT);

            }
        });


    }

    /**
     * 解析从服务器拿到的数据：gson解析
     */
    private void processResult(String result) {
        Gson gSon = new Gson();
        newsMenuData = gSon.fromJson(result, NewsMenuData.class);

        //获取侧边栏对象，把解析的数据设置给侧边栏
        MainActivity mainActivity = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        leftMenuFragment.setData(newsMenuData.getData());

        //初始化侧边栏的菜单详情页
        mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity, newsMenuData.getData().get(0).getChildren()));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotoMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));

        //新闻详情页作为默认页面
        setCurrentMenuDetailPager(0);

    }

    /**
     * 设置与侧边栏对应得菜单详情页
     * @param position
     */
    public void setCurrentMenuDetailPager(int position) {
        BaseMenuDetailPager baseMenuDetailPager = mMenuDetailPagers.get(position);
        baseMenuDetailPager.initData();
        //清除framelayout上所有的view
        flContent.removeAllViews();
        flContent.addView(baseMenuDetailPager.mRootView);

        //设置标题
        tvTitle.setText(newsMenuData.getData().get(position).getTitle());
    }
}
