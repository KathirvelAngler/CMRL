package com.cmrl.customer.activity.book.route;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmrl.customer.R;
import com.cmrl.customer.helper.AppDialogs;
import com.cmrl.customer.helper.AppHelper;
import com.cmrl.customer.model.Routes;
import com.cmrl.customer.utils.DepthPageTransformer;
import com.cmrl.customer.utils.PermissionChecker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Mathan on 17-07-2019.
 */

public class RouteActivity extends FragmentActivity implements View.OnClickListener {

    ImageView mBack, mLeftArrow, mRightArrow;
    TextView mRouteTitle, mDistance;
    Context mContext;
    View mView;
    ViewPager mViewPager;
    Routes mRoutes;
    LinearLayout mMapNavigation;
    LatLng mRouteLatLng;

    String[] mPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    PermissionChecker mChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        mView = getWindow().getDecorView().getRootView();

        mContext = this;
        mChecker = new PermissionChecker();
        mBack = findViewById(R.id.header_app_back);
        mViewPager = findViewById(R.id.activity_route_viewpager);

        mMapNavigation = findViewById(R.id.activity_route_map_navigation);

        mLeftArrow = findViewById(R.id.activity_route_left_arrow);
        mRightArrow = findViewById(R.id.activity_route_right_arrow);
        mRouteTitle = findViewById(R.id.activity_route_header);
        mDistance = findViewById(R.id.inflate_route_cab_distance);

        mChecker.askAllPermissions(mContext, mPermissions);

        initHeader();

        clickListener();

        initBundle();

    }

    private void initBundle() {
        try {
            Intent intent = getIntent();
            if (intent != null) {
                mRoutes = (Routes) intent.getExtras().get("routes");
                initArrow(0);
                initViewPager();
            } else AppDialogs.okAction(mContext, getString(R.string.something));
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
        mRouteLatLng = new LatLng(mRoutes.data.get(i).pickupLat, mRoutes.data.get(i).pickupLng);
        mRouteTitle.setText(mRoutes.data.get(i).stopName);
        mDistance.setText(mRoutes.data.get(i).duration);
        if (mRoutes.data.get(i).duration.toLowerCase().equals("na"))
            mDistance.setVisibility(View.GONE);
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
            return new RouteFragment(mRoutes.pickId, mRoutes.stopId, aRoutes.data.get(position));
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
        mMapNavigation.setOnClickListener(this);
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
            case R.id.activity_route_map_navigation:
                if (mChecker.checkAllPermission(mContext, mPermissions) && mRouteLatLng != null)
                    getCurrentLocation();
                break;
            case R.id.activity_route_left_arrow:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                break;
            case R.id.activity_route_right_arrow:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                break;
        }
    }

    private void getCurrentLocation() {
        try {
            AppHelper.INSTANCE.getCurrentLocation(mContext, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    AppDialogs.hideProgressDialog();
                    if (location != null) {
                        LatLng start = new LatLng(location.getLatitude(), location.getLongitude());
                        LatLng end = mRouteLatLng;
                        AppHelper.INSTANCE.navigateGoogleMap(mContext, start, end);
                    }
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    AppDialogs.hideProgressDialog();
                    AppDialogs.okAction(mContext, e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
