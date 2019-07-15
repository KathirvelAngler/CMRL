package com.cmrl.customer.activity.book;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmrl.customer.R;
import com.cmrl.customer.activity.book.adapter.TripAdapter;
import com.cmrl.customer.base.BaseActivity;
import com.cmrl.customer.model.TripDetails;

import java.util.ArrayList;

/**
 * Created by Mathan on 15-07-2019.
 */

public class TripDetailActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView mTripRecycler;
    Context mContext;
    ImageView mBack;
    TripAdapter mAdapter;
    ArrayList<TripDetails> mDetails = new ArrayList<>();
    LinearLayout mInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        mContext = this;

        mTripRecycler = findViewById(R.id.activity_trip_recycler);
        mBack = findViewById(R.id.header_app_back);
        mInvoice = findViewById(R.id.activity_trip_invoice);

        clickListener();

        initRecycler();
    }

    private void initRecycler() {
        mTripRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mTripRecycler.setHasFixedSize(true);
        mAdapter = new TripAdapter(mContext, mDetails);
        mTripRecycler.setAdapter(mAdapter);

        initDetails();
    }

    private void initDetails() {
        ArrayList<String> aDetails = new ArrayList<>();
        aDetails.add("Mathan Kumar");
        aDetails.add("Maruti Swift");
        aDetails.add("10 Mins / 2.1 KM");
        aDetails.add("60");
        aDetails.add("2 Tickets");

        @SuppressLint("Recycle") TypedArray icon = getResources().obtainTypedArray(R.array.trip_details_icon);

        for (int i = 0; i < icon.length(); i++) {
            TripDetails details = new TripDetails();
            details.icon = icon.getDrawable(i);
            details.detail = aDetails.get(i);
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
        ((TextView) findViewById(R.id.header_app_header)).setText(getString(R.string.trip_details));
        return false;
    }

    @Override
    public boolean clickListener() {
        mBack.setOnClickListener(this);
        mInvoice.setOnClickListener(this);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_app_back:
                onBackPressed();
                break;
            case R.id.activity_trip_invoice:
                startActivity(new Intent(mContext, RideHistoryActivity.class));
                break;
        }
    }
}
