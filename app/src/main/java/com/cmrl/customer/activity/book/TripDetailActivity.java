package com.cmrl.customer.activity.book;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmrl.customer.R;
import com.cmrl.customer.activity.book.adapter.TripAdapter;
import com.cmrl.customer.api.AppServices;
import com.cmrl.customer.base.BaseActivity;
import com.cmrl.customer.helper.AppDialogs;
import com.cmrl.customer.helper.AppHelper;
import com.cmrl.customer.helper.ImageLoader;
import com.cmrl.customer.http.Response;
import com.cmrl.customer.http.ResponseListener;
import com.cmrl.customer.model.TripDetails;

import java.util.ArrayList;

import static com.cmrl.customer.preference.CMRLConstants.DD_MMM_yy_HH_MM;
import static com.cmrl.customer.preference.CMRLConstants.DD_MM_YY_ZONE;

/**
 * Created by Mathan on 15-07-2019.
 */

public class TripDetailActivity extends BaseActivity implements View.OnClickListener, ResponseListener {

    RecyclerView mTripRecycler;
    Context mContext;
    ImageView mBack, mRouteMap;
    TripAdapter mAdapter;
    ArrayList<TripDetails> mDetails = new ArrayList<>();
    LinearLayout mInvoice, mSupport;
    TripDetails mTripDetails;
    TextView mPick, mDrop, mPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        mContext = this;

        mTripRecycler = findViewById(R.id.activity_trip_recycler);
        mBack = findViewById(R.id.header_app_back);

        mRouteMap = findViewById(R.id.activity_trip_map);

        mPick = findViewById(R.id.activity_trip_pickup_location);
        mDrop = findViewById(R.id.activity_trip_drop_location);
        mPayment = findViewById(R.id.activity_trip_payment);

        mInvoice = findViewById(R.id.activity_trip_invoice);
        mSupport = findViewById(R.id.activity_trip_support);

        clickListener();

        initBundle();

        initRecycler();
    }

    private void getDetails(int id) {
        if (checkInternet()) {
            AppDialogs.showProgressDialog(mContext);
            AppServices.getTripDetails(mContext, id);
        } else AppDialogs.okAction(mContext, getString(R.string.no_internet));
    }

    private void initRecycler() {
        mTripRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        mTripRecycler.setHasFixedSize(true);
        mAdapter = new TripAdapter(mContext, mDetails);
        mTripRecycler.setAdapter(mAdapter);
    }

    private void initDetails(TripDetails mTripDetails) {
        ArrayList<String> aDetails = new ArrayList<>();
        aDetails.add(mTripDetails.customerName);
        aDetails.add(String.valueOf(mTripDetails.fare));
        aDetails.add(mTripDetails.cabNo);
        aDetails.add(String.format("%s %s", mTripDetails.tickets, mTripDetails.tickets == 1 ? "Ticket" : "Tickets"));
        aDetails.add(String.format("%s / %s", mTripDetails.traveledTime, mTripDetails.traveledDistance));

        @SuppressLint("Recycle") TypedArray icon = getResources().obtainTypedArray(R.array.trip_details_icon);

        for (int i = 0; i < icon.length(); i++) {
            TripDetails details = new TripDetails();
            details.icon = icon.getDrawable(i);
            details.detail = aDetails.get(i);
            mDetails.add(details);
        }

        mPick.setText(String.format("%s - %s",
                AppHelper.INSTANCE.convertDateFormat(mTripDetails.pickTime, DD_MM_YY_ZONE, DD_MMM_yy_HH_MM),
                mTripDetails.pickLocation));
        mDrop.setText(String.format("%s - %s",
                AppHelper.INSTANCE.convertDateFormat(mTripDetails.dropTime, DD_MM_YY_ZONE, DD_MMM_yy_HH_MM),
                mTripDetails.dropLocation));
        mPayment.setText(mTripDetails.paymentMode);

        if (!mTripDetails.routeUrl.isEmpty())
            ImageLoader.loadImage(mContext, mRouteMap, mTripDetails.routeUrl,
                    R.drawable.bg_booking, R.drawable.bg_booking);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHeader();
    }

    @Override
    public boolean initBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            getDetails(intent.getExtras().getInt("trip_id"));
        }
        return true;
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
        mSupport.setOnClickListener(this);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_app_back:
                onBackPressed();
                break;
            case R.id.activity_trip_invoice:

                break;
            case R.id.activity_trip_support:
                startActivity(new Intent(mContext, ConfirmationActivity.class));
                break;
        }
    }

    @Override
    public void onResponse(Response response) {
        try {
            AppDialogs.hideProgressDialog();
            if (response != null) {
                if (response.requestType == AppServices.API.tripDetails.hashCode()) {
                    if (response.isSuccess()) {
                        mTripDetails = ((TripDetails) response).details;
                        initDetails(mTripDetails);
                    } else AppDialogs.okAction(mContext, response.message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

