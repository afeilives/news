package com.afei.news.base.impl;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
                Log.d("NewsCenterPager",result);
                processResult(result);

//                Log.d("NewsCenterPager","onsuccess");
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.d("NewsCenterPager",msg);
                error.printStackTrace();
                Toast.makeText(mActivity,msg,Toast.LENGTH_SHORT);

            }
        });



    }

    /**
     * 解析从服务器拿到的数据：gson解析
     */
    private void processResult(String result) {
        Log.d("haha","111111111");
        Gson gSon = new Gson();
        Log.d("haha","222222222222");
        NewsMenuData newsMenuData = gSon.fromJson(result, NewsMenuData.class);
        Log.d("haha","3333333333");
       String data =  newsMenuData.getData().toString();
        Log.d("haha","44444444");
    }
}
