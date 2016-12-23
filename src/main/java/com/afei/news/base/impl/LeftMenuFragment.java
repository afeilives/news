package com.afei.news.base.impl;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afei.news.MainActivity;
import com.afei.news.R;
import com.afei.news.base.BaseFragment;
import com.afei.news.domain.NewsMenuData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 左侧栏Fragment：
 * Created by fei on 2016/12/14.
 */

public class LeftMenuFragment extends BaseFragment {
    private List<NewsMenuData.DataBean> menuData;
    private int mCurrentPos;

    @ViewInject(R.id.lv_left_menu)
    private ListView lvLeftMneu;
    private LeftMenuAdapter mAdapter;

    @Override
    //初始化侧边栏布局
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        ViewUtils.inject(this,view);//注入view和事件
        return view;
    }


    //侧边栏listView的适配器
    class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return menuData.size();
        }

        @Override
        public NewsMenuData.DataBean getItem(int position) {
            return menuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NewsMenuData.DataBean dataBean = getItem(position);
            View view = View.inflate(mActivity, R.layout.lv_item_left_menu, null);
            TextView textView =  (TextView) view.findViewById(R.id.tv_menu);
            textView.setText(dataBean.getTitle());
            if (mCurrentPos==position){
                textView.setEnabled(true);//当前位置等于正在绘制的textview时，把textView状态设为enable
            }else{
                textView.setEnabled(false);
            }
            return view;
        }
    }

    /**
     * 此方法用于设置侧边栏数据，让在新闻中心解析的数据传递过来
     *
     * @param newsMenuData
     */
    public void setData(List<NewsMenuData.DataBean> newsMenuData) {
        menuData = newsMenuData;
        mAdapter = new LeftMenuAdapter();
        lvLeftMneu.setAdapter(mAdapter);
        lvLeftMneu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;//记录当前的位置
                mAdapter.notifyDataSetChanged();//刷新适配器

                //通知新闻中心，准备菜单详情页
                prepareCurrentMenuDetailPager(position);

                //当点击title关闭侧边栏
                MainActivity mainActivity = (MainActivity) mActivity;
                SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
                slidingMenu.toggle();//侧边栏的开关（如果开就关，如果关就开）
            }
        });

        mCurrentPos = 0;//重置侧边栏的默认选择条目为第一个
    }

    /**
     * 准备当前菜单条目的详情页
     */
    private void prepareCurrentMenuDetailPager(int position) {
        MainActivity mainActivity = (MainActivity) mActivity;
        //获取contentFragment
        ContentFragment contentFragment = mainActivity.getContentFragment();
        //获取新闻中心页面
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        //为新闻中心页面设置菜单详情页
        newsCenterPager.setCurrentMenuDetailPager(position);
    }
}
