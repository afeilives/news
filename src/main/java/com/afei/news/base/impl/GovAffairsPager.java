package com.afei.news.base.impl;

import android.app.Activity;
import android.widget.TextView;

import com.afei.news.base.BasePager;

/**
 * 政务
 * Created by fei on 2016/12/20.
 */

public class GovAffairsPager extends BasePager {
    public GovAffairsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("政务");
        TextView textView = new TextView(mActivity);
        textView.setText("政务");
        flContent.addView(textView);

    }
}
