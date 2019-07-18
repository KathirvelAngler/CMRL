package com.cmrl.customer.activity.book.route;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmrl.customer.R;
import com.cmrl.customer.activity.book.adapter.RidesAdapter;
import com.cmrl.customer.base.BaseActivity;
import com.cmrl.customer.model.TripDetails;

import java.util.ArrayList;


/**
 * Created by Mathan on 15-07-2019.
 */

public class RideHistoryActivity extends BaseActivity implements View.OnClickListener {

    ImageView mBack;
    Context mContext;
    RecyclerView mRideRecycler;
    RidesAdapter mAdapter;
    ArrayList<TripDetails> mDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);

        mContext = this;
        mBack = findViewById(R.id.header_app_back);
        mRideRecycler = findViewById(R.id.activity_rides_recycler);

        clickListener();

        initRecycler();
    }

    private void initRecycler() {
        mRideRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mRideRecycler.setHasFixedSize(true);
        mAdapter = new RidesAdapter(mContext, mDetails);
        mRideRecycler.setAdapter(mAdapter);

        initDetails();
    }

    private void initDetails() {
        for (int i = 0; i < 25; i++) {
            TripDetails details = new TripDetails();
            details.detail = String.format("Ride %s", i + 1);
            mDetails.add(details);
        }

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
}
