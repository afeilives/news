package com.afei.news.base.impl.menudetail;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.afei.news.base.BaseMenuDetailPager;

/**
 * 专题详情页
 * Created by fei on 2016/12/22.
 */

public class TopicMenuDetailPager extends BaseMenuDetailPager {
    public TopicMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText("专题详情页");
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
