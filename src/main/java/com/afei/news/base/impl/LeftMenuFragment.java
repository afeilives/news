package com.afei.news.base.impl;

import android.view.View;

import com.afei.news.R;
import com.afei.news.base.BaseFragment;

/**左侧栏Fragment：
 * Created by fei on 2016/12/14.
 */

public class LeftMenuFragment extends BaseFragment {

    @Override

    public View initView() {
        View view = View.inflate(mActivity,R.layout.fragment_left_menu,null);
//        TextView textView = new TextView(mActivity);
//        textView.setText("I am is leftMenuFragment");
//        textView.setTextSize(22);
//        fLLeftmenu = (FrameLayout) view.findViewById(R.id.fl_fragment_left_menu);
//        fLLeftmenu.addView(textView);


        return view;
    }
}