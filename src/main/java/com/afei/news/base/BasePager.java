package com.afei.news.base;

import android.app.Activity;
import android.view.View;

import com.afei.news.R;

/**五个标签页的基类
 * Created by fei on 2016/12/15.
 */

public class BasePager {
    public  Activity mActivity;
    public View mRootView;

    public BasePager(Activity activity) {
         mActivity =  activity;
    }
    public void ininView(){
        mRootView = mActivity.findViewById(R.id.ll_base_pager);

    }
}
