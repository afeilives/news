package com.afei.news.base.impl;

import android.app.Activity;
import android.widget.TextView;

import com.afei.news.base.BasePager;

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
    }
}
