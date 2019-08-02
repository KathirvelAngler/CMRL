package com.cmrl.customer.activity.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmrl.customer.R;
import com.cmrl.customer.activity.book.adapter.BookSeatAdapter;
import com.cmrl.customer.api.AppServices;
import com.cmrl.customer.base.BaseActivity;
import com.cmrl.customer.helper.AppDialogs;
import com.cmrl.customer.helper.AppHelper;
import com.cmrl.customer.helper.ImageLoader;
import com.cmrl.customer.http.Response;
import com.cmrl.customer.http.ResponseListener;
import com.cmrl.customer.model.Seat;

import java.util.ArrayList;

import static com.cmrl.customer.preference.CMRLConstants.AVAILABLE;
import static com.cmrl.customer.preference.CMRLConstants.BOOKED;
import static com.cmrl.customer.preference.CMRLConstants.DD_MMM_YY_HH_MM;
import static com.cmrl.customer.preference.CMRLConstants.DD_MM_YY_ZONE;
import static com.cmrl.customer.preference.CMRLConstants.IND_RUPEE;
import static com.cmrl.customer.preference.CMRLConstants.SELECTED;

/**
 * Created by Mathan on 13-07-2019.
 */

public class BookingActivity extends BaseActivity implements BookSeatAdapter.Callback,
        View.OnClickListener, ResponseListener {
    RecyclerView mSeatSelection;
    Context mContext;
    BookSeatAdapter mAdapter;
    ArrayList<Seat> mSeat = new ArrayList<>();
    int mSpanCount = 8, maxSeats, mAvailable;
    double mPrice = 0, mTotal = 0;
    LinearLayout mFareLayout;
    TextView mFare, mBookDate, mBookCab, mBookNoSeat, mBookFrom, mBookTo;
    Button mBookSeat;
    ImageView mBack, mBookMap;
    Seat mDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        mContext = this;

        mSeatSelection = findViewById(R.id.seat_selection_recycler);
        mFareLayout = findViewById(R.id.seat_selection_fare_layout);
        mFare = findViewById(R.id.seat_selection_fare);
        mBookSeat = findViewById(R.id.activity_seat_book);
        mBack = findViewById(R.id.header_app_back);

        mBookDate = findViewById(R.id.activity_book_date);
        mBookCab = findViewById(R.id.activity_book_cab);
        mBookNoSeat = findViewById(R.id.activity_book_seat);
        mBookFrom = findViewById(R.id.activity_book_from);
        mBookTo = findViewById(R.id.activity_book_to);
        mBookMap = findViewById(R.id.activity_book_map);

        clickListener();

        initBundle();

    }

    @Override
    public boolean initBundle() { Intent intent = getIntent();
        if (intent != null) {
            getDetails(intent.getExtras().getInt("pick_id"),
                    intent.getExtras().getInt("drop_id"),
                    intent.getExtras().getInt("route_id"));
        }
        return true;

    }

    private void getDetails(int pickId, int dropId, int routeId) {
        if (checkInternet()) {
            AppDialogs.showProgressDialog(mContext);
            AppServices.getBookDetails(mContext, pickId, dropId, routeId);
        } else AppDialogs.okAction(mContext, getString(R.string.no_internet));
    }

    private void initRecycler() {
        mSeatSelection.setLayoutManager(new GridLayoutManager(mContext,
                mSeat.size() > mSpanCount ? mSpanCount : mSeat.size()));
        mSeatSelection.setHasFixedSize(true);
        mAdapter = new BookSeatAdapter(mContext, mSeat, this);
        mSeatSelection.setAdapter(mAdapter);
    }

    private void initDetails(Seat mDetails) {
        maxSeats = mDetails.cabCapacity;
        mAvailable = mDetails.availableSeats;
        mPrice = mDetails.fare;

        mBookDate.setText(AppHelper.INSTANCE.convertDateFormat(mDetails.dateTime, DD_MM_YY_ZONE, DD_MMM_YY_HH_MM));
        mBookCab.setText(mDetails.cabNumber);
        mBookNoSeat.setText(String.valueOf(mDetails.availableSeats));
        mBookFrom.setText(mDetails.firstStopName);
        mBookTo.setText(mDetails.lastStopName);

        if (!mDetails.routeUrl.isEmpty())
            ImageLoader.loadImage(mContext, mBookMap, mDetails.routeUrl,
                    R.drawable.bg_booking, R.drawable.bg_booking);

        mSeat.clear();
        for (int i = 0; i < maxSeats; i++) {
            Seat seat = new Seat();
            if (i < maxSeats - mAvailable)
                seat.available = BOOKED;
            mSeat.add(seat);
        }

        initRecycler();

        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHeader();
    }

    @Override
    public boolean initHeader() {
        ((TextView) findViewById(R.id.header_app_header)).setText(getString(R.string.book_confirm));
        return false;
    }

    @Override
    public boolean clickListener() {
        mBookSeat.setOnClickListener(this);
        mBack.setOnClickListener(this);
        return true;
    }

    @Override
    public void select(int aPosition, boolean isChecked) {
        mSeat.get(aPosition).available = isChecked ? SELECTED : AVAILABLE;
//        mAdapter.notifyDataSetChanged();

        initTotal(isChecked);
    }

    private void initTotal(boolean isAdd) {
        if (isAdd)
            mTotal += mPrice;
        else mTotal -= mPrice;

        if (mTotal == 0) {
            mFareLayout.setVisibility(View.GONE);
        } else {
            mFareLayout.setVisibility(View.VISIBLE);
            mFare.setText(String.format("%s %s", IND_RUPEE, mTotal));
        }

        mBookSeat.setEnabled(mTotal != 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_seat_book:
//                Toast.makeText(mContext, String.valueOf(getTicketCount()), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, TripDetailActivity.class);
                intent.putExtra("trip_id", mDetails.tripId);
                startActivity(intent);
                break;
            case R.id.header_app_back:
                onBackPressed();
                break;
        }
    }

    private int getTicketCount() {
        int count = 0;
        for (int i = 0; i < mSeat.size(); i++) {
            if (mSeat.get(i).available == SELECTED)
                count++;
        }
        return count;
    }

    @Override
    public void onResponse(Response response) {
        try {
            AppDialogs.hideProgressDialog();
            if (response != null) {
                if (response.requestType == AppServices.API.bookDetails.hashCode()) {
                    if (response.isSuccess()) {
                        mDetails = ((Seat) response).details;
                        initDetails(mDetails);
                    } else AppDialogs.okAction(mContext, response.message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
