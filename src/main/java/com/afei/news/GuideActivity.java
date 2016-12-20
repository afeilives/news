package com.afei.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.afei.news.utils.PrefUtils;

import java.util.ArrayList;

/**
 * Created by fei on 2016/11/12.
 */

public class GuideActivity extends Activity implements View.OnClickListener {
    private ViewPager vpGuide;
    private int[] imageIds = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private ArrayList<ImageView> imageViewArrayList  = new ArrayList<ImageView>();
    private Button btnStart;
    private LinearLayout llContainer;
    private float pointDistance;//小圆点间的距离
    private ImageView ivRedPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        btnStart = (Button) findViewById(R.id.btn_start);
        vpGuide.setAdapter(new myPagerAdapter());
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        ivRedPoint = (ImageView) findViewById(R.id.iv_redPoint);
        btnStart.setOnClickListener(this);
        //新手指导界面初始化
        initGuide();

        //设置viewpager的页面滑动监听器
        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当滑动pager时，动态设置小红点的位置(离父窗体左边距)：移动的百分比*原点间距+原点间距*当前的页数
                float redPointPosition = positionOffset*pointDistance+position*pointDistance;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                params.leftMargin = (int) redPointPosition;
                ivRedPoint.setLayoutParams(params);


            }

            @Override
            public void onPageSelected(int position) {
                if (position==imageIds.length-1){
                    btnStart.setVisibility(View.VISIBLE);
                }else {
                    btnStart.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //用小红点的视图树观察监听布局是否加载完成，这样小圆点的位置就确定了，可以得到圆点间距
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //移除监听
                ivRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //获取圆点间距
                pointDistance = llContainer.getChildAt(1).getLeft()-llContainer.getChildAt(0).getLeft();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                //标记已经进行过新手指导
                PrefUtils.putBoolean("is_guide_show",true,getApplicationContext());
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
                break;
        }

    }

    //viewPager的适配器
    private class myPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return imageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = imageViewArrayList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    //新手指导界面初始化
    public void initGuide(){
        for (int i = 0 ;i<imageIds.length;i++) {
            ImageView ivPager = new ImageView(getApplicationContext());
            ivPager.setBackgroundResource(imageIds[i]);
            imageViewArrayList.add(ivPager);

            //初始化小圆点
            ImageView ivCicle = new ImageView(this);
            ivCicle.setBackgroundResource(R.drawable.shape_cicle_default);

            //设置圆点间距离
            if (i>0) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 30;
                ivCicle.setLayoutParams(params);
            }

            llContainer.addView(ivCicle);
        }

    }
}
