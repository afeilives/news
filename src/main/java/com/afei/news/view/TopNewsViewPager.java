package com.afei.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by fei on 2016/12/24.
 */

public class TopNewsViewPager extends ViewPager {

    private int startX;
    private int startY;

    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     *父控件和祖宗控件拦截该子控件的事件:
     * 1:新闻头条滑到第一个&&还向左边滑
     * 2:新闻头条滑到最后一个&&还向右滑
     * 3:上下滑
     *
     */



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);//请求父控件及祖宗控件不要拦截
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();

                int dx = endX - startX;
                int dy = endY - startY;

                if (Math.abs(dx)>Math.abs(dy)) {//左右移动
                    if(dx>0){//右移
                        if(getCurrentItem()==0){//第一个页面
                            getParent().requestDisallowInterceptTouchEvent(false);//请求父控件及祖宗控件拦截
                        }

                    }else {//左移
                        if (getCurrentItem()==(getAdapter().getCount()-1)){//最后一页
                            getParent().requestDisallowInterceptTouchEvent(false);//请求父控件及祖宗控件拦截
                        }

                    }

                }else {//上下移动
                    getParent().requestDisallowInterceptTouchEvent(false);//请求父控件及祖宗控件拦截

                }
                break;
                default:
                    break;
        }

        return super.dispatchTouchEvent(ev);
    }

}
