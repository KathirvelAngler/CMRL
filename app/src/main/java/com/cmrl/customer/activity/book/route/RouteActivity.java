package com.cmrl.customer.activity.book.route;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmrl.customer.R;
import com.cmrl.customer.model.Routes;
import com.cmrl.customer.utils.DepthPageTransformer;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Mathan on 17-07-2019.
 */

public class RouteActivity extends FragmentActivity implements View.OnClickListener {

    ImageView mBack, mLeftArrow, mRightArrow;
    TextView mRouteTitle;
    Context mContext;
    View mView;
    ViewPager mViewPager;
    Routes mRoutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        mView = getWindow().getDecorView().getRootView();

        mContext = this;
        mBack = findViewById(R.id.header_app_back);
        mViewPager = findViewById(R.id.activity_route_viewpager);

        mLeftArrow = findViewById(R.id.activity_route_left_arrow);
        mRightArrow = findViewById(R.id.activity_route_right_arrow);
        mRouteTitle = findViewById(R.id.activity_route_header);

        initHeader();

        clickListener();

        initBundle();

    }

    private void initBundle() {
        try {
            Intent intent = getIntent();
            if (intent != null) {
                mRoutes = new Gson().fromJson(intent.getExtras().getString("routes"), Routes.class);
            }

            initArrow(0);
            initViewPager();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void initViewPager() {
        FragmentManager aFragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new ScreenSlidePagerAdapter(aFragmentManager, mRoutes));
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                initArrow(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initArrow(int i) {
        mRouteTitle.setText(mRoutes.data.get(i).dropStopName);
        mLeftArrow.setVisibility(View.VISIBLE);
        mRightArrow.setVisibility(View.VISIBLE);
        if (i == 0)
            mLeftArrow.setVisibility(View.INVISIBLE);
        if (i == mRoutes.data.size() - 1)
            mRightArrow.setVisibility(View.INVISIBLE);

    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        Routes aRoutes;

        ScreenSlidePagerAdapter(FragmentManager fm, Routes routes) {
            super(fm);
            this.aRoutes = routes;
        }

        @Override
        public Fragment getItem(int position) {
            return new RouteFragment(aRoutes.data.get(position));
        }

        @Override
        public int getCount() {
            return aRoutes.data.size();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initHeader() {
        ((TextView) findViewById(R.id.header_app_header)).setText(getString(R.string.available_routes));
    }

    public boolean clickListener() {
        mBack.setOnClickListener(this);
        mLeftArrow.setOnClickListener(this);
        mRightArrow.setOnClickListener(this);
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_app_back:
                onBackPressed();
                break;
            case R.id.activity_route_left_arrow:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                break;
            case R.id.activity_route_right_arrow:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                break;
        }
    }
}
