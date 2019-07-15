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
import com.cmrl.customer.base.BaseActivity;
import com.cmrl.customer.model.Seat;

import java.util.ArrayList;

import static com.cmrl.customer.preference.CMRLConstants.AVAILABLE;
import static com.cmrl.customer.preference.CMRLConstants.BOOKED;
import static com.cmrl.customer.preference.CMRLConstants.IND_RUPEE;
import static com.cmrl.customer.preference.CMRLConstants.SELECTED;

/**
 * Created by Mathan on 13-07-2019.
 */

public class BookingActivity extends BaseActivity implements BookSeatAdapter.Callback, View.OnClickListener {
    RecyclerView mSeatSelection;
    Context mContext;
    BookSeatAdapter mAdapter;
    ArrayList<Seat> mSeat = new ArrayList<>();
    int mSpanCount = 8, maxSeats = 6;
    double mPrice = 15, mTotal = 0;
    LinearLayout mFareLayout;
    TextView mFare;
    Button mBookSeat;
    ImageView mBack;

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

        clickListener();

        initDate();

        initRecycler();
    }

    private void initRecycler() {
        mSeatSelection.setLayoutManager(new GridLayoutManager(mContext,
                mSeat.size() > mSpanCount ? mSpanCount : mSeat.size()));
        mSeatSelection.setHasFixedSize(true);
        mAdapter = new BookSeatAdapter(mContext, mSeat, this);
        mSeatSelection.setAdapter(mAdapter);
    }

    private void initDate() {
        mSeat.clear();
        for (int i = 0; i < maxSeats; i++) {
            Seat seat = new Seat();
            seat.status = String.valueOf(i + 1);
            if (i == 0 || i == 3)
                seat.available = BOOKED;
            mSeat.add(seat);
        }

        if (mAdapter != null)
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
    public void select(int aPosition) {
        mSeat.get(aPosition).available = (mSeat.get(aPosition).available == SELECTED ? AVAILABLE : SELECTED);
        mAdapter.notifyDataSetChanged();

        initTotal(mSeat.get(aPosition).available == SELECTED);
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
                startActivity(new Intent(mContext, TripDetailActivity.class));
                break;
            case R.id.header_app_back:
                onBackPressed();
                break;
        }
    }
}
