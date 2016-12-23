package com.afei.news.base.impl.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.afei.news.base.BaseMenuDetailPager;

/**
 * 页签详情页
 * Created by fei on 2016/12/23.
 */

public class TabMenuDetailPager extends BaseMenuDetailPager {
    private String tabTitle;
    private TextView mTextView;

    public TabMenuDetailPager(Activity activity, String tabData) {
        super(activity);
        tabTitle = tabData;
    }

    @Override
    public View initView() {
        mTextView = new TextView(mActivity);
        return mTextView;
    }

    @Override
    public void initData() {
        mTextView.setText(tabTitle);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextColor(Color.RED);

    }
}
