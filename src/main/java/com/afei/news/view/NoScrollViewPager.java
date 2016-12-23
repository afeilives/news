package com.afei.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**自定义的viewPager
 * 1:禁止滑动
 * 2：禁止拦截子viewPager事件
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;//禁止拦截子view的事件
    }
}
