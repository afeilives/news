package com.afei.news.base.impl.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afei.news.R;
import com.afei.news.base.BaseMenuDetailPager;
import com.afei.news.domain.NewsMenuData;
import com.afei.news.domain.TabDetailData;
import com.afei.news.global.Constants;
import com.afei.news.utils.CacheUtils;
import com.afei.news.view.RefreshListView;
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
    @ViewInject(R.id.vpi_top_news)
    private CirclePageIndicator topNewsCirclePageIndicator;
    @ViewInject(R.id.tv_top_news_title)
    private  TextView tvTopNewsTitle;
    @ViewInject(R.id.vp_top_news)
    private ViewPager vpTopNews;
    @ViewInject(R.id.lv_list_news)
    private RefreshListView lvListNews;
    private String mUrl;//12个标签页数据的url
    private ArrayList<TabDetailData.DataBean.TopnewsBean> topNews;
    private ArrayList<TabDetailData.DataBean.NewsBean> listNews;
    private String mMoreUrl;
    private String mMoreDataUrl;
    private ListNewsAdapter mListNewsAdapter;
    private TopNewsPagerAdapter mTopNewsPagerAdapter;
    private NewsMenuData.DataBean.ChildrenBean mTabData;
    private TabDetailData tabDetailData;

    public TabMenuDetailPager(Activity activity, NewsMenuData.DataBean.ChildrenBean tabData) {
        super(activity);
        mTabData = tabData;
        mUrl = Constants.SERVER_URL+tabData.getUrl() ;//标签页里数据的URL

    }

    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.pager_tab_detail,null);
        ViewUtils.inject(this,view);
        View listHeader = View.inflate(mActivity, R.layout.lv_header_list_news, null);
        lvListNews.addHeaderView(listHeader);
        ViewUtils.inject(this,listHeader);//注入listview的头布局(listHeader)事件

        lvListNews.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }
            @Override
            public void onLoadMore() {
                if (!TextUtils.isEmpty(mMoreUrl)){
                    mMoreDataUrl = Constants.SERVER_URL + mMoreUrl;
                    getMoreDataFromServer();//从服务器加载更多
                }else {
                    lvListNews.onRefreshComplete(true);//收起加载更多
                    Toast.makeText(mActivity,"没有更多数据了",Toast.LENGTH_SHORT).show();
                }

            }


        });
        return view;
    }


    @Override
    public void initData() {
        //判断是否有缓存数据
        String tabDataCaChe = CacheUtils.getCache(mUrl,mActivity);
        if(!TextUtils.isEmpty(tabDataCaChe)){
            processResult(tabDataCaChe,false);//如果有，就解析缓存
        }

        //还要从服务器获取数据，为了更新缓存，优化用户体验
        getDataFromServer();



    }


    /**
     * 此方法用于从服务器获取数据
     */
    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET,mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;//从服务器拿到数据
                processResult(result,false);//解析数据
                CacheUtils.setCache(mUrl,result,mActivity);//更新缓存

                lvListNews.onRefreshComplete(true);//刷新后重置相关数据

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                System.out.println("000000000000000000");
                error.printStackTrace();
                lvListNews.onRefreshComplete(false);//刷新后重置相关数据
                Toast.makeText(mActivity,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 此方法用于加载列表新闻的下页数据
     */
    private void getMoreDataFromServer() {
        HttpUtils http = new HttpUtils();
        System.out.println("加载更多数据"+mMoreDataUrl);
        http.send(HttpRequest.HttpMethod.GET, mMoreDataUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processResult(result,true);
                lvListNews.onRefreshComplete(true);//获取数据完成，隐藏加载更多

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                Toast.makeText(mActivity,msg,Toast.LENGTH_SHORT).show();
                lvListNews.onRefreshComplete(false);//获取数据完成，隐藏加载更多

            }
        });
    }

    /**
     * 此处用gson解析json数据
     * @param tabDataCaChe
     */
    private void processResult(String tabDataCaChe,boolean isMore) {
        Gson gson = new Gson();
        tabDetailData = gson.fromJson(tabDataCaChe, TabDetailData.class);
        mMoreUrl = tabDetailData.getData().getMore();//获取更多数据url

        if (!isMore){
            //初始化头条新闻
            topNews = tabDetailData.getData().getTopnews();
            if (topNews!=null){
                mTopNewsPagerAdapter = new TopNewsPagerAdapter();
                vpTopNews.setAdapter(mTopNewsPagerAdapter);
                topNewsCirclePageIndicator.setViewPager(vpTopNews);//为圆圈指示器绑定ViewPager
                //此处要为指示器设置页面滑动监听，而不是给ViewPager
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

                tvTopNewsTitle.setText(topNews.get(0).getTitle());//初始化第一个头条新闻
                topNewsCirclePageIndicator.onPageSelected(0);//调整小圆点不会来的bug
            }

            //初始化列表新闻
            listNews = tabDetailData.getData().getNews();
            if (listNews!=null){
                mListNewsAdapter = new ListNewsAdapter();
                lvListNews.setAdapter(mListNewsAdapter);
            }

        }else {

            ArrayList<TabDetailData.DataBean.NewsBean> moreDatas = tabDetailData.getData().getNews();
            System.out.println("要追加的数据"+moreDatas);
            //获取更多数据
            listNews.addAll(moreDatas);//追加数据
            mListNewsAdapter.notifyDataSetChanged();//刷新适配器
        }
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
            bitmapUtils.display(viewHolder.imageView,listNews.get(position).getListimage());
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
