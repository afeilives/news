package com.afei.news.base.impl;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.afei.news.MainActivity;
import com.afei.news.R;
import com.afei.news.base.BaseFragment;
import com.afei.news.base.BasePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;


/**
 * 主界面Fragment：
 * Created by fei on 2016/12/14.
 */

public class ContentFragment extends BaseFragment {
    public ViewPager vpContent;
    public ArrayList<BasePager> basePagers;
    private RadioGroup rgBottonTab;

    /**
     * 初始化主界面布局
     *
     * @return
     */
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        vpContent = (ViewPager) view.findViewById(R.id.vp_content);
        rgBottonTab = (RadioGroup) view.findViewById(R.id.rg_bottom_tab);
        return view;
    }

    /**
     * 初始化ContentPager数据
     */
    @Override
    public void initData() {
        basePagers = new ArrayList<BasePager>();
        basePagers.add(new HomePager(mActivity));
        basePagers.add(new NewsCenterPager(mActivity));
        basePagers.add(new GovAffairsPager(mActivity));
        basePagers.add(new SmartServicePager(mActivity));
        basePagers.add(new SettingPager(mActivity));

        vpContent.setAdapter(new ContentPagerAdapter());

        //设置底栏的事件监听
        rgBottonTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //首页
                    case R.id.rbtn_home:
                        vpContent.setCurrentItem(0, false);//设置当前页面并禁用viewpager的平滑效果
                        setSlidingMenuEable(false);
                        basePagers.get(0).initData();//初始化该条目页面的数据
                        break;
                    //新闻中心
                    case R.id.rbtn_newscenter:
                        vpContent.setCurrentItem(1, false);//禁用viewpager的平滑效果
                        setSlidingMenuEable(true);
                        basePagers.get(1).initData();//初始化该条目页面的数据

                        break;
                    //政务
                    case R.id.rbtn_govaffair:
                        vpContent.setCurrentItem(2, false);//禁用viewpager的平滑效果
                        setSlidingMenuEable(true);
                        basePagers.get(2).initData();//初始化该条目页面的数据

                        break;

                    //智慧服务
                    case R.id.rbtn_smartservice:
                        vpContent.setCurrentItem(3, false);//禁用viewpager的平滑效果
                        setSlidingMenuEable(true);
                        basePagers.get(3).initData();//初始化该条目页面的数据

                        break;

                    //设置
                    case R.id.rbtn_setting:
                        vpContent.setCurrentItem(4, false);//禁用viewpager的平滑效果
                        setSlidingMenuEable(false);
                        basePagers.get(4).initData();//初始化该条目页面的数据

                        break;

                    default:
                        break;
                }

            }
        });
        //默认显示首页
        vpContent.setCurrentItem(0);
        basePagers.get(0).initData();//初始化首页的数据
        setSlidingMenuEable(false);//禁用侧边栏
    }

    /**
     * 获取新闻中心页面
     */
    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) basePagers.get(1);
    }

    class ContentPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = basePagers.get(position);
            container.addView(basePager.mRootView);//将五个标签页面添加到容器中
//            basePager.initData();//最好不要在这初始化五个标签页的数据（因为viewpager会加载下一个页面数据）
            return basePager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 设置侧边栏是否可用
     * @param eable
     */
    public void setSlidingMenuEable(boolean eable){
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();//得到SlidingMenu对象
        if(!eable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }

    }
}
