package com.afei.news.base.impl;

import android.app.Activity;
import android.widget.TextView;

import com.afei.news.base.BasePager;

/**
 * 首页
 * Created by fei on 2016/12/20.
 */

public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("首页");
        TextView textView = new TextView(mActivity);
        textView.setText("智慧上海");
        flContent.addView(textView);

    }
}
