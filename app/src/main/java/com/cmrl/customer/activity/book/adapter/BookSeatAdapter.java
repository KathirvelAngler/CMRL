package com.cmrl.customer.activity.book.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.cmrl.customer.R;
import com.cmrl.customer.model.Booking;
import com.cmrl.customer.preference.CMRLConstants;

import java.util.ArrayList;

/**
 * Created by Mathan on 15-07-2019.
 */

public class BookSeatAdapter extends RecyclerView.Adapter<BookSeatAdapter.ViewHolder> implements CMRLConstants {

    private Context mContext;
    private ArrayList<Booking> mBookings;
    private Callback mCallback;

    public BookSeatAdapter(Context aContext, ArrayList<Booking> aBookings, Callback callback) {
        mContext = aContext;
        mBookings = aBookings;
        mCallback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View aView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inflate_seat, parent, false);
        return new ViewHolder(aView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Booking booking = mBookings.get(position);
        holder.mSeat.setEnabled(booking.available != BOOKED);

        switch (booking.available) {
            case BOOKED:
                holder.mSeat.setChecked(true);
                break;
            case AVAILABLE:
                holder.mSeat.setChecked(false);
                break;
            case SELECTED:
                holder.mSeat.setChecked(true);
                break;
        }

        holder.mSeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCallback.select(position, holder.mSeat.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBookings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox mSeat;

        ViewHolder(View aView) {
            super(aView);
            this.setIsRecyclable(false);
            mSeat = aView.findViewById(R.id.inflate_book_seat);
        }
    }

    public interface Callback {
        void select(int aPosition, boolean isChecked);
    }
}
