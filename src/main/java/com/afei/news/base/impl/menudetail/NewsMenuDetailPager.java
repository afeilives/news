package com.afei.news.base.impl.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.afei.news.MainActivity;
import com.afei.news.R;
import com.afei.news.base.BaseMenuDetailPager;
import com.afei.news.domain.NewsMenuData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻详情页
 * Created by fei on 2016/12/22.
 */

public class NewsMenuDetailPager extends BaseMenuDetailPager {
    private List<NewsMenuData.DataBean.ChildrenBean> tabDatas;//标签名,遍历这个集合就知道要几个标签页了
    private ArrayList<TabMenuDetailPager> tabPagers;//标签页的集合
    @ViewInject(R.id.vpi_tabpager_title)
    private TabPageIndicator mTabPageIndicator;//标签页指示器

    @ViewInject(R.id.vp_news_menu_detail)
    private ViewPager vpNewsDetail;
    private int currentItem;

    public NewsMenuDetailPager(Activity activity, List<NewsMenuData.DataBean.ChildrenBean> children) {
        super(activity);
        tabDatas = children;
    }

    @Override
    public View initView() {
       View view = View.inflate(mActivity, R.layout.pager_menu_detail_news,null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData() {

        //初始化n个标签页
        tabPagers = new ArrayList<TabMenuDetailPager>();
        for (NewsMenuData.DataBean.ChildrenBean tabData: tabDatas
             ) {
            TabMenuDetailPager tabMenuDetailPager = new TabMenuDetailPager(mActivity,tabData);
            tabPagers.add(tabMenuDetailPager);
        }

        vpNewsDetail.setAdapter(new NewsDetailPagerAdapter());

        //要在viewpager设置完数据后，关联viewpager
        mTabPageIndicator.setViewPager(vpNewsDetail);//把标签页（TabMenuDetailPager）和标题（TabPageIndicator）关联


        /*为标签页添加页面滑动监听。当滑动到第一个页面时，把侧边栏设为可用，其他页面设为不可用，
        这样做避免了侧边栏拦截标签页的滑动事件
         */
        mTabPageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    //第一个页面时，侧边栏可用
                    setSlidingMenuEable(true);

                }else {
                    //其他页面时，侧边栏不可用
                    setSlidingMenuEable(false);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class NewsDetailPagerAdapter extends PagerAdapter {

        //重写此方法，返回指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return tabDatas.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return tabPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabMenuDetailPager tabMenuDetailPager = tabPagers.get(position);
            container.addView(tabMenuDetailPager.mRootView);//每个viewpager容器维护一个标签页
            tabMenuDetailPager.initData();//初始化标签页数据
            return tabMenuDetailPager.mRootView;
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


    /**
     * 此方法用于将标签切换到下一标签页
     * @param view
     */
    @OnClick(R.id.iv_next_tab)
    public void nextTab(View view){
        currentItem = vpNewsDetail.getCurrentItem();
        currentItem++;
        vpNewsDetail.setCurrentItem(currentItem);
    }
}
