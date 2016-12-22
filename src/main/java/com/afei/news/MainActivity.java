package com.afei.news;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.afei.news.base.impl.ContentFragment;
import com.afei.news.base.impl.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by fei on 2016/11/12.
 */

public class MainActivity extends SlidingFragmentActivity {


    private final String TAG_MENU = "TAG_MENU";
    private final String TAG_CONTENT = "TAG_CONTENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏标题栏和状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.left_menu);

        //得到slidingMenu对象
        SlidingMenu slidingMenu = getSlidingMenu();
        //设置预留大小
        slidingMenu.setBehindOffset(300);
        //设置滑动的模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        initFragment();

    }

    //初始化Fragment
    public void initFragment() {
        //获取fragment管理器
        FragmentManager sfm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = sfm.beginTransaction();//开始事务
        //用LeftMenuFragment替换掉左侧栏
        fragmentTransaction.replace(R.id.fl_left_menu,new LeftMenuFragment(), TAG_MENU);
        //用ContentFragment替换掉主界面
        fragmentTransaction.replace(R.id.fl_main,new ContentFragment(), TAG_CONTENT);
        fragmentTransaction.commit();//提交事务

    }

    /**
     * 获取侧边栏的Fragment
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {
        //获取fragment管理器
        FragmentManager sfm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) sfm.findFragmentByTag(TAG_MENU);
        return fragment;

    }

    /**
     * 获取主界面的Fragment
     * @return
     */
    public ContentFragment getContentFragment() {
        //获取fragment管理器
        FragmentManager sfm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) sfm.findFragmentByTag(TAG_CONTENT);
        return fragment;

    }
}
