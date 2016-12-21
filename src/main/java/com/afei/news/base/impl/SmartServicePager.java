package com.afei.news.base.impl;

import android.app.Activity;
import android.widget.TextView;

import com.afei.news.base.BasePager;

/**
 * 智慧服务
 * Created by fei on 2016/12/20.
 */

public class SmartServicePager extends BasePager {
    public SmartServicePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("智慧服务");
        TextView textView = new TextView(mActivity);
        textView.setText("智慧服务");
        flContent.addView(textView);
    }
}
