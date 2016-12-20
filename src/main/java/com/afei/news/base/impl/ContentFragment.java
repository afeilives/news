package com.afei.news.base.impl;

import android.view.View;

import com.afei.news.R;
import com.afei.news.base.BaseFragment;

/**主界面Fragment：
 * Created by fei on 2016/12/14.
 */

public class ContentFragment extends BaseFragment {
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content,null);
//        TextView textView = new TextView(mActivity);
//        textView.setText("I am is contentFragment");
//        textView.setTextSize(22);
//        fLContent = (FrameLayout) view.findViewById(R.id.fl_fragment_content);
//        fLContent.addView(textView);

        return view;
    }
}
