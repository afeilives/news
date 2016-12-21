package com.afei.news.base.impl;

import android.app.Activity;
import android.widget.TextView;

import com.afei.news.base.BasePager;

/**
 * 设置
 * Created by fei on 2016/12/20.
 */

public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("设置");
        TextView textView = new TextView(mActivity);
        textView.setText("设置");
        flContent.addView(textView);
    }
}
