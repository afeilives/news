package com.afei.news.base.impl.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afei.news.R;
import com.afei.news.base.BaseMenuDetailPager;
import com.afei.news.domain.NewsMenuData;
import com.afei.news.domain.TabDetailData;
import com.afei.news.global.Constants;
import com.afei.news.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;


/**
 * 页签详情页
 * Created by fei on 2016/12/23.
 */

public class TabMenuDetailPager extends BaseMenuDetailPager {
    private NewsMenuData.DataBean.ChildrenBean tabTitle;
    @ViewInject(R.id.vpi_top_news)
    private CirclePageIndicator topNewsCirclePageIndicator;
    @ViewInject(R.id.tv_top_news_title)
    private  TextView tvTopNewsTitle;
    @ViewInject(R.id.vp_top_news)
    private ViewPager vpTopNews;
//    @ViewInject(R.id.lv_list_news)
//    private ListView lvListNews;
    private String mUrl;
    private ArrayList<TabDetailData.DataBean.TopnewsBean> topNews;
    private ArrayList<TabDetailData.DataBean.NewsBean> listNews;


    public TabMenuDetailPager(Activity activity, NewsMenuData.DataBean.ChildrenBean tabData) {
        super(activity);
        tabTitle = tabData;
        mUrl = Constants.SERVER_URL+tabData.getUrl() ;//标签页里数据的URL
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_tab_detail,null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        //判断是否有缓存数据
        String tabDataCaChe = CacheUtils.getCache(mUrl,mActivity);
        if(!TextUtils.isEmpty(tabDataCaChe)){
            processResult(tabDataCaChe);//如果有，就解析缓存
        }

        //还要从服务器获取数据，为了更新缓存，优化用户体验
        getDataFromServer();

        vpTopNews.setAdapter(new TopNewsPagerAdapter());
        topNewsCirclePageIndicator.setViewPager(vpTopNews);//为圆圈指示器绑定ViewPager
        tvTopNewsTitle.setText(topNews.get(0).getTitle());//初始化第一个头条新闻
        topNewsCirclePageIndicator.onPageSelected(0);//调整小圆点不会来的bug
        topNewsCirclePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvTopNewsTitle.setText(topNews.get(position).getTitle());//设置头条新闻的标题

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


//        lvListNews.setAdapter(new ListNewsAdapter());

    }


    /**
     * 此方法用于从服务器获取数据
     */
    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;//从服务器拿到数据
                processResult(result);//解析数据
                CacheUtils.setCache(mUrl,result,mActivity);//更新缓存

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    /**
     * 此处用gson解析json数据
     * @param tabDataCaChe
     */
    private void processResult(String tabDataCaChe) {
        Gson gson = new Gson();
        TabDetailData tabDetailData = gson.fromJson(tabDataCaChe, TabDetailData.class);
        //获取头条新闻数据
        topNews = tabDetailData.getData().getTopnews();
        Log.d("TopNews", this.topNews.toString());
        listNews = tabDetailData.getData().getNews();//获取列表新闻数据
        Log.d("ListNews",listNews.toString());

    }

    //头条新闻的适配器
    public class TopNewsPagerAdapter extends PagerAdapter{

        private final BitmapUtils bitmapUtils;

        public TopNewsPagerAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
        }

        @Override
        public int getCount() {
            return topNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mActivity);
            //BitmapUtils可以从服务器上获取图片并解析给view
            bitmapUtils.display(imageView,topNews.get(position).getTopimage());
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    //新闻列表的适配器
    public class ListNewsAdapter extends BaseAdapter{

        private final BitmapUtils bitmapUtils;

        public ListNewsAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
        }

        @Override
        public int getCount() {
            return listNews.size();
        }

        @Override
        public Object getItem(int position) {
            return listNews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                view = View.inflate(mActivity,R.layout.lv_item_list_news,null);
                viewHolder.imageView = (ImageView) view.findViewById(R.id.iv_list_news_img);
                viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_list_news_title);
                viewHolder.tvDate = (TextView) view.findViewById(R.id.tv_list_news_date);
                view.setTag(viewHolder);//将viewHolder存储在view中
            }else{
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            //设置listview的item数据
            bitmapUtils.display(viewHolder.imageView,listNews.get(position).getUrl());
            viewHolder.tvTitle.setText(listNews.get(position).getTitle());
            viewHolder.tvDate.setText(listNews.get(position).getPubdate());

            return view;
        }
        private class ViewHolder {
            ImageView imageView;
            TextView tvTitle;
            TextView tvDate;

        }
    }
}
