package com.afei.news.base.impl.menudetail;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.afei.news.base.BaseMenuDetailPager;

/**
 * 组图详情页
 * Created by fei on 2016/12/22.
 */

public class PhotoMenuDetailPager extends BaseMenuDetailPager {
    public PhotoMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText("组图详情页");
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
