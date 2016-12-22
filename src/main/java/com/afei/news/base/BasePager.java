package com.afei.news.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afei.news.MainActivity;
import com.afei.news.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


/**五个标签页的基类
 * Created by fei on 2016/12/15.
 */

public abstract class BasePager {
    public  Activity mActivity;
    public View mRootView;
    public TextView tvTitle;
    public ImageButton imgbtnMenu;
    public FrameLayout flContent;


    public BasePager(Activity activity) {
        mActivity =  activity;
        initView();

    }

    /*
    初始化布局
     */
    public void initView(){
        mRootView = View.inflate(mActivity,R.layout.base_pager,null);
        tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        imgbtnMenu = (ImageButton) mRootView.findViewById(R.id.imgbtn_menu);
        flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);

        imgbtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) mActivity;
                SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
                slidingMenu.setVisibility(View.VISIBLE);
            }
        });

    }

    /*
    初始化数据
     */
    public abstract void initData();
}
