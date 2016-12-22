package com.afei.news.base.impl;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afei.news.R;
import com.afei.news.base.BaseFragment;
import com.afei.news.domain.NewsMenuData;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 左侧栏Fragment：
 * Created by fei on 2016/12/14.
 */

public class LeftMenuFragment extends BaseFragment {
    private ArrayList<String> menuTitles = new ArrayList<String>();

    @ViewInject(R.id.lv_left_menu)
    private ListView lvLeftMneu;

    @Override
    //初始化侧边栏布局
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        ViewUtils.inject(this,view);//注入view和事件
        return view;
    }


    class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return menuTitles.size();
        }

        @Override
        public String getItem(int position) {
            return menuTitles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String title = getItem(position);
            View view = View.inflate(mActivity, R.layout.lv_item_left_menu, null);
            TextView textView =  (TextView) view.findViewById(R.id.tv_menu);
            textView.setText(title);
            return view;
        }
    }

    /**
     * 此方法用于设置侧边栏数据，让在新闻中心解析的数据传递过来
     *
     * @param newsMenuData
     */
    public void setData(List<NewsMenuData.DataBean> newsMenuData) {

        for (NewsMenuData.DataBean data : newsMenuData){

            menuTitles.add(data.getTitle());//把解析数据中的title取出并存入ArrayList集合中

        }
        LeftMenuAdapter mAdapter = new LeftMenuAdapter();
        lvLeftMneu.setAdapter(mAdapter);

    }
}
