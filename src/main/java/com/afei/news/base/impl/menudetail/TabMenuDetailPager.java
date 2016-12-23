package com.afei.news.base.impl.menudetail;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.afei.news.base.BaseMenuDetailPager;

/**
 * 页签详情页
 * Created by fei on 2016/12/23.
 */

public class TabMenuDetailPager extends BaseMenuDetailPager {
    public TabMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText("标签页");
        return textView;
    }
}
