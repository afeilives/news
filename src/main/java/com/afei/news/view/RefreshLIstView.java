package com.afei.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afei.news.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 能下拉刷新的listview
 * Created by fei on 2016/12/26.
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener{

    private View refreshView;
    @ViewInject(R.id.tv_refresh_state)
    private TextView tvRefreshState;
    @ViewInject(R.id.tv_refresh_time)
    private TextView tvRefreshTime;
    @ViewInject(R.id.iv_refresh_arrow)
    private ImageView ivRefreshArrow;
    @ViewInject(R.id.pb_refresh)
    private ProgressBar pbRefresh;
    private int refreshViewHeight;//刷新的view的高度
    private int startY = -1;

    private final int STATE_PULL_TO_REFRESH = 1;//下拉刷新状态
    private final int STATE_RELEASE_TO_REFRESH = 2;//松开刷新状态
    private final int STATE_REFRESHING = 3;//正在刷新状态
    private int mCurrentState;//用来记录refreshView的当前状态
    private RotateAnimation animaDown;
    private RotateAnimation animaUp;
    private OnRefreshListener mListener;
    private View footerView;
    private int footerViewHeight;

    private boolean isLoadMore;

    public RefreshListView(Context context) {
        super(context);
        initRefreshView();
        initFooterView();

    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRefreshView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRefreshView();
        initFooterView();
    }

    private void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.lv_footer_list_news,null);
        this.addFooterView(footerView);

        footerView.measure(0,0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0,-footerViewHeight,0,0);//隐藏上拉加载更多的布局

        setOnScrollListener(this);
    }


    private void initRefreshView() {
        refreshView = View.inflate(getContext(), R.layout.lv_refresh_list_news, null);
        ViewUtils.inject(this, refreshView);
        this.addHeaderView(refreshView);


        //初始化listview时隐藏刷新(1:得到refreshView的高度；2：设置refreshview的-paddingTop)
//        getHeight();//此方法的不到refreshView的高度，因为refreshVIew还没有绘制完成
        refreshView.measure(0, 0);//手动测量
        refreshViewHeight = refreshView.getMeasuredHeight();//获取refreshView的高度
        refreshView.setPadding(0, -refreshViewHeight, 0, 0);//隐藏显示刷新的view


        initAnimation();//初始化箭头动画
        setCurrentTime();//初始化当前时间
    }

    private void initAnimation() {
        animaUp = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animaUp.setDuration(500);
        animaUp.setFillAfter(true);//动画填充完保持

        animaDown = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animaUp.setDuration(500);
        animaUp.setFillAfter(true);//动画填充完保持
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {//当手指按下的是头条新闻页的时候，事件可能会被拦截MotionEvent.ACTION_DOWN:
                    startY = (int) ev.getY();
                }

                if (mCurrentState == STATE_REFRESHING) {//如果当前正在刷新，则跳出
                    break;
                }
                int stopY = (int) ev.getY();
                int dy = stopY - startY;
                if (dy > 0 && getFirstVisiblePosition() == 0) {//向下滑并当前的第一个可见条目位置是0，显示refreshView
                    int paddingTopValue = dy - refreshViewHeight;//计算paddingTop值

                    if (paddingTopValue < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
                        mCurrentState = STATE_PULL_TO_REFRESH;//记录当前状态为下拉状态
                        refreshState();
                    } else if (paddingTopValue >= 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
                        mCurrentState = STATE_RELEASE_TO_REFRESH;//记录当前状态为松开状态
                        refreshState();
                    }
                    refreshView.setPadding(0, paddingTopValue, 0, 0);//重置refreshView
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP://对手指松开时处于的两种（下拉刷新，松开刷新）状态做处理
                startY = -1;//起始坐标重置

                if (mCurrentState == STATE_PULL_TO_REFRESH) {//处于下拉状态
                    refreshView.setPadding(0, -refreshViewHeight, 0, 0);//隐藏refreshView

                } else if (mCurrentState == STATE_RELEASE_TO_REFRESH) {//处于松开状态
                    //跳转到正在刷新状态
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
                    refreshView.setPadding(0, 0, 0, 0);//显示refreshView
                    //回调刷新监听
                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }

    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                tvRefreshState.setText("下拉刷新");
                pbRefresh.setVisibility(INVISIBLE);
                ivRefreshArrow.startAnimation(animaDown);
                break;
            case STATE_RELEASE_TO_REFRESH:
                tvRefreshState.setText("松开刷新");
                pbRefresh.setVisibility(INVISIBLE);
                ivRefreshArrow.startAnimation(animaUp);
                break;
            case STATE_REFRESHING:
                tvRefreshState.setText("正在刷新");
                pbRefresh.setVisibility(VISIBLE);
                ivRefreshArrow.clearAnimation();//清除动画才能设置为不可见
                ivRefreshArrow.setVisibility(INVISIBLE);
                break;
            default:
                break;
        }
    }

    //设置当前的时间
    private void setCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        tvRefreshTime.setText(time);
    }



    //刷新完成
    public void onRefreshComplete(boolean success) {
        if (!isLoadMore){
            refreshView.setPadding(0, -refreshViewHeight, 0, 0);//隐藏refreshView
            pbRefresh.setVisibility(INVISIBLE);//设置进度条不可见
            mCurrentState = STATE_PULL_TO_REFRESH;//当前状态设置为默认状态
            tvRefreshState.setText("下拉刷新");
            if (success) {
                setCurrentTime();//刷新成功，重置当前时间
            }
        }else {
            footerView.setPadding(0,-footerViewHeight,0,0);//隐藏脚布局
            isLoadMore = false;

        }


    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE){
            if (getLastVisiblePosition()>=this.getCount()-1 && !isLoadMore){
                isLoadMore = true;
                footerView.setPadding(0,0,0,0);//显示脚布局
                setSelection(getCount()-1);
                if (mListener!=null){
                    mListener.onLoadMore();
                }
            }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    //设置刷新事件监听
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;

    }
    /**
     * 刷新事件监听
     */
    public interface OnRefreshListener {
        public void onRefresh();
        public void onLoadMore();
    }












}



























































