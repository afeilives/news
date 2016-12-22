package com.afei.news.utils;

import android.app.Activity;

/**
 * 网络缓存工具类
 * Created by fei on 2016/12/22.
 */

public class CacheUtils {
    //写缓存
    public static void setCache(String url, String json, Activity activity){
        PrefUtils.putString(url,json,activity);
    }
    //读缓存
    public static String getCache(String url, Activity activity){
       return PrefUtils.getString(url,null,activity);
    }
}
