package com.afei.news.base.impl;

import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;

import com.afei.news.MainActivity;
import com.afei.news.base.BasePager;
import com.afei.news.domain.NewsMenuData;
import com.afei.news.global.Constants;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 新闻中心
 * Created by fei on 2016/12/20.
 */

public class NewsCenterPager extends BasePager {
    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("新闻");
        TextView textView = new TextView(mActivity);
        textView.setText("新闻中心");
        flContent.addView(textView);

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

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                Toast.makeText(mActivity,msg,Toast.LENGTH_SHORT);

            }
        });



    }

    /**
     * 解析从服务器拿到的数据：gson解析
     */
    private void processResult(String result) {
        Gson gSon = new Gson();
        NewsMenuData newsMenuData = gSon.fromJson(result, NewsMenuData.class);
       String data =  newsMenuData.getData().toString();

        //获取侧边栏对象，把解析的数据设置给侧边栏
        MainActivity mainActivity = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        leftMenuFragment.setData(newsMenuData.getData());



    }
}
