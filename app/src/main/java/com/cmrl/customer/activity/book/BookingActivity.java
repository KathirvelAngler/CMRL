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
import com.cmrl.customer.model.Booking;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.cmrl.customer.preference.CMRLConstants.AVAILABLE;
import static com.cmrl.customer.preference.CMRLConstants.BOOKED;
import static com.cmrl.customer.preference.CMRLConstants.DD_MMM_YY;
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
    ArrayList<Booking> mBooking = new ArrayList<>();
    int mSpanCount = 8, maxSeats, mAvailable, mRouteId;
    double mPrice = 0, mTotal = 0;
    LinearLayout mFareLayout;
    TextView mFare, mBookDate, mBookTime, mBookCab, mBookNoSeat, mBookFrom, mBookTo;
    Button mBookSeat;
    ImageView mBack, mBookMap;
    Booking mDetails;

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
        mBookTime = findViewById(R.id.activity_book_time);
        mBookCab = findViewById(R.id.activity_book_cab);
        mBookNoSeat = findViewById(R.id.activity_book_seat);
        mBookFrom = findViewById(R.id.activity_book_from);
        mBookTo = findViewById(R.id.activity_book_to);
        mBookMap = findViewById(R.id.activity_book_map);

        clickListener();

        initBundle();

    }

    @Override
    public boolean initBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            getDetails(intent.getExtras().getInt("pick_id"),
                    intent.getExtras().getInt("drop_id"),
                    intent.getExtras().getInt("route_id"));
        }
        return true;

    }

    private void getDetails(int pickId, int dropId, int routeId) {
        if (checkInternet()) {
            mRouteId = routeId;
            AppDialogs.showProgressDialog(mContext);
            AppServices.getBookDetails(mContext, pickId, dropId, routeId);
        } else AppDialogs.okAction(mContext, getString(R.string.no_internet));
    }

    private void initRecycler() {
        mSeatSelection.setLayoutManager(new GridLayoutManager(mContext,
                mBooking.size() > mSpanCount ? mSpanCount : mBooking.size()));
        mSeatSelection.setHasFixedSize(true);
        mAdapter = new BookSeatAdapter(mContext, mBooking, this);
        mSeatSelection.setAdapter(mAdapter);
    }

    private void initDetails(Booking mDetails) {
        maxSeats = mDetails.cabCapacity;
        mAvailable = mDetails.availableSeats;
        mPrice = mDetails.fare;

        mBookDate.setText(AppHelper.INSTANCE.convertDateFormat(mDetails.dateTime, DD_MM_YY_ZONE, DD_MMM_YY));
        mBookTime.setText(mDetails.scheduleSlot);
        mBookCab.setText(mDetails.cabNumber);
        mBookNoSeat.setText(String.valueOf(mDetails.availableSeats));
        mBookFrom.setText(mDetails.pickup.location);
        mBookTo.setText(mDetails.drop.location);

        if (!mDetails.routeUrl.isEmpty())
            ImageLoader.loadImage(mContext, mBookMap, mDetails.routeUrl,
                    R.drawable.bg_booking, R.drawable.bg_booking);

        mBooking.clear();
        for (int i = 0; i < maxSeats; i++) {
            Booking booking = new Booking();
            if (i < maxSeats - mAvailable)
                booking.available = BOOKED;
            mBooking.add(booking);
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
        mBooking.get(aPosition).available = isChecked ? SELECTED : AVAILABLE;
//        mAdapter.notifyDataSetChanged();

        initTotal(isChecked);
    }

    private void initTotal(boolean isAdd) {
        if (isAdd)
            mTotal += mPrice;
        else mTotal -= mPrice;

        if (mTotal == 0) {
            mFareLayout.setVisibility(View.INVISIBLE);
        } else {
            mFareLayout.setVisibility(View.VISIBLE);
            mFare.setText(String.format("%s %s", IND_RUPEE, mTotal));
        }

        mBookSeat.setEnabled(mTotal != 0);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.activity_seat_book) {
            bookTicket();
        } else if (i == R.id.header_app_back) {
            onBackPressed();
        }
    }

    private void bookTicket() {
        if (checkInternet()) {
            mDetails.routeId = mRouteId;
            mDetails.totalTickets = getTicketCount();
            Intent intent = new Intent(mContext, PaymentActivity.class);
            intent.putExtra("booking_data", new Gson().toJson(mDetails));
            startActivity(intent);
        } else AppDialogs.okAction(mContext, getString(R.string.no_internet));
    }

    private int getTicketCount() {
        int count = 0;
        for (int i = 0; i < mBooking.size(); i++) {
            if (mBooking.get(i).available == SELECTED)
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
                        mDetails = ((Booking) response).details;
                        initDetails(mDetails);
                    } else AppDialogs.okAction(mContext, response.message);
                } else if (response.requestType == AppServices.API.bookTicket.hashCode()) {
                    if (response.isSuccess()) {
                        Intent intent = new Intent(mContext, ConfirmationActivity.class);
                        intent.putExtra("booking", new Gson().toJson(response));
                        startActivity(intent);
                    } else AppDialogs.okAction(mContext, response.message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
