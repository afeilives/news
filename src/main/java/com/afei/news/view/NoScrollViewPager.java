package com.afei.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by fei on 2016/12/21.
 */

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context){
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //让返回值为true就可以禁用viewpager的滑动
        return true;
    }
}
