package com.ksyun.customservice;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.ksyun.customservice.jiekou.MyInterface.FinishActivityListener;
import com.ksyun.customservice.adapter.FeedBackViewPagerAdapter;
import com.ksyun.customservice.custom.CustomRelativeLayout;
import com.ksyun.customservice.custom.viewpagerindicator.TabPageIndicator;
import com.ksyun.customservice.custom.viewpagerindicator.UnderlinePageIndicatorEx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/17.
 */
public class FeedBackKindActivity extends BaseActivity implements FinishActivityListener{

    private FeedBackViewPagerAdapter mContentAdapter;
    private ViewPager mPager;
    private TabPageIndicator mTabPageIndicator;
    private UnderlinePageIndicatorEx mUnderlinePageIndicator;
    private ImageView back;

    @Override
    public void onFinishActivity() {
        finish();
    }

    private int COUNT = 0;
    private List<String[]> mContentList;
    private CustomRelativeLayout parent_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_kind_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        }

        parent_layout = (CustomRelativeLayout) findViewById(R.id.parent_layout);
        parent_layout.setFinishActivityListener(this);
        initData();
        setupViews();
    }

    private void initData() {

        mContentList = new ArrayList<String[]>();
        String[] titleStrings = getResources().getStringArray(R.array.feedback);
        String[] commerce = getResources().getStringArray(R.array.commerce);
        String[] techology = getResources().getStringArray(R.array.techology);
        String[] record = getResources().getStringArray(R.array.record);
        String[] other = getResources().getStringArray(R.array.other);
        mContentList.add(commerce);
        mContentList.add(techology);
        mContentList.add(record);
        mContentList.add(other);
        mContentAdapter = new FeedBackViewPagerAdapter(getSupportFragmentManager());
        mContentAdapter.setTitleList(titleStrings);
        mContentAdapter.setContentList(mContentList);

    }
    private void setupViews() {
        mPager = (ViewPager) findViewById(R.id.message_viewpager);
        mPager.setOffscreenPageLimit(0);
        mPager.setAdapter(mContentAdapter);
        mTabPageIndicator = (TabPageIndicator) findViewById(R.id.tab_indicator);
        mTabPageIndicator.setViewPager(mPager);
        mUnderlinePageIndicator = (UnderlinePageIndicatorEx) findViewById(R.id.underline_indicator);
        mUnderlinePageIndicator.setViewPager(mPager);
        mUnderlinePageIndicator.setFades(false);
        mTabPageIndicator.setOnPageChangeListener(mUnderlinePageIndicator);

    }

}
