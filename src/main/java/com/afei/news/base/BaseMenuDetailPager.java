package com.afei.news.base;

import android.app.Activity;
import android.view.View;

/**
 * 侧边栏菜单详情页的基类
 * Created by fei on 2016/12/22.
 */

public abstract class BaseMenuDetailPager {

    public  Activity mActivity;
    public View mRootView;

    public BaseMenuDetailPager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
}

    /**
     * 初始化布局
     * @return
     */
    public abstract View initView();

    /**
     * 初始化数据
     */
    public  void initData(){}

}
