package com.cmrl.customer.activity.book;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmrl.customer.R;
import com.cmrl.customer.activity.book.adapter.RidesAdapter;
import com.cmrl.customer.activity.dashboard.HomeActivity;
import com.cmrl.customer.api.AppServices;
import com.cmrl.customer.base.BaseActivity;
import com.cmrl.customer.helper.AppDialogs;
import com.cmrl.customer.helper.AppHelper;
import com.cmrl.customer.http.Response;
import com.cmrl.customer.http.ResponseListener;
import com.cmrl.customer.model.History;
import com.cmrl.customer.utils.PermissionChecker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;


/**
 * Created by Mathan on 15-07-2019.
 */

public class RideHistoryActivity extends BaseActivity implements View.OnClickListener,
        ResponseListener, RidesAdapter.Callback {

    ImageView mBack;
    Context mContext;
    RecyclerView mRideRecycler;
    RidesAdapter mAdapter;
    ArrayList<History> mHistory = new ArrayList<>();
    SwipeRefreshLayout mSwipe;
    View mView;

    String[] mPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    PermissionChecker mChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);

        mContext = this;
        mChecker = new PermissionChecker();

        mView = getWindow().getDecorView().getRootView();

        mBack = findViewById(R.id.header_app_back);
        mRideRecycler = findViewById(R.id.activity_rides_recycler);
        mSwipe = findViewById(R.id.activity_history_swipe);

        mChecker.askAllPermissions(mContext, mPermissions);

        clickListener();

        initRecycler();
    }

    private void initRecycler() {
        mRideRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mRideRecycler.setHasFixedSize(true);
        mAdapter = new RidesAdapter(mContext, mHistory, this);
        mRideRecycler.setAdapter(mAdapter);

        initHistory(true);
    }

    private void initHistory(boolean isShow) {
        if (checkInternet()) {
            if (isShow)
                AppDialogs.showProgressDialog(mContext);
            AppServices.getHistory(mContext, "1231231230");
        } else AppDialogs.okAction(mContext, getString(R.string.no_internet));

        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHeader();
    }

    @Override
    public boolean initBundle() {
        return false;
    }

    @Override
    public boolean initHeader() {
        ((TextView) findViewById(R.id.header_app_header)).setText(getString(R.string.your_rides));
        return false;
    }

    @Override
    public boolean clickListener() {

        AppHelper.INSTANCE.swipeRefColor(mContext, mSwipe);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initHistory(false);
            }
        });

        mBack.setOnClickListener(this);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_app_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onResponse(Response response) {
        try {
            AppDialogs.hideProgressDialog();
            mSwipe.setRefreshing(false);
            if (response != null) {
                if (response.requestType == AppServices.API.history.hashCode()) {
                    if (response.isSuccess()) {
                        initData(((History) response).histories);
                    } else AppDialogs.okAction(mContext, response.message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData(ArrayList<History> histories) {
        mHistory.clear();
        mHistory.addAll(histories);
        AppHelper.INSTANCE.showNoData(mView, mHistory.size() == 0, R.drawable.home_history, "No Rides Found!");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void openDetail(int position) {
        if (checkInternet()) {
            Intent intent = new Intent(mContext, TripDetailActivity.class);
            intent.putExtra("trip_id", mHistory.get(position).tripId);
            startActivity(intent);
        } else AppDialogs.okAction(mContext, getString(R.string.no_internet));
    }

    @Override
    public void openMap(int position) {
        LatLng mRouteLatLng = new LatLng(mHistory.get(position).pickupLat, mHistory.get(position).pickupLng);
        if (mChecker.checkAllPermission(mContext, mPermissions))
            getCurrentLocation(mRouteLatLng);
    }

    private void getCurrentLocation(final LatLng mRouteLatLng) {
        try {
            AppHelper.INSTANCE.getCurrentLocation(mContext, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    AppDialogs.hideProgressDialog();
                    if (location != null) {
                        LatLng start = new LatLng(location.getLatitude(), location.getLongitude());
                        AppHelper.INSTANCE.navigateGoogleMap(mContext, start, mRouteLatLng);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateHome();
    }

    private void navigateHome() {
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
