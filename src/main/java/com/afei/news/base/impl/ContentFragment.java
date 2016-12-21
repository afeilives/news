package com.afei.news.base.impl;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.afei.news.R;
import com.afei.news.base.BaseFragment;
import com.afei.news.base.BasePager;

import java.util.ArrayList;


/**主界面Fragment：
 * Created by fei on 2016/12/14.
 */

public class ContentFragment extends BaseFragment {
    public ViewPager vpContent;
    public ArrayList<BasePager> basePagers;

    /**
     * 初始化主界面布局
     * @return
     */
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content,null);
        vpContent = (ViewPager) view.findViewById(R.id.vp_content);
        return view;
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        basePagers = new ArrayList<BasePager>();
        basePagers.add(new HomePager(mActivity));
        basePagers.add(new NewsCenterPager(mActivity));
        basePagers.add(new SettingPager(mActivity));
        basePagers.add(new GovAffairsPager(mActivity));
        basePagers.add(new SmartServicePager(mActivity));
        vpContent.setAdapter(new ContentPagerAdapter());
    }

    class ContentPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = basePagers.get(position);
            container.addView(basePager.mRootView);
            basePager.initData();
            return basePager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
