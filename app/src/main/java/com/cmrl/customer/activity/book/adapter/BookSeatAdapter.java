package com.cmrl.customer.activity.book.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmrl.customer.R;
import com.cmrl.customer.model.Seat;
import com.cmrl.customer.preference.CMRLConstants;

import java.util.ArrayList;

/**
 * Created by Mathan on 15-07-2019.
 */

public class BookSeatAdapter extends RecyclerView.Adapter<BookSeatAdapter.ViewHolder> implements CMRLConstants {

    private Context mContext;
    private ArrayList<Seat> mSeats;
    private Callback mCallback;

    public BookSeatAdapter(Context aContext, ArrayList<Seat> aSeats, Callback callback) {
        mContext = aContext;
        mSeats = aSeats;
        mCallback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View aView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inflate_seat, parent, false);
        return new ViewHolder(aView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Seat seat = mSeats.get(position);
        holder.mSeat.setEnabled(seat.available != BOOKED);

        switch (seat.available) {
            case BOOKED:
                holder.mSeat.setBackgroundResource(R.drawable.bg_border_gray_filled);
                break;
            case AVAILABLE:
                holder.mSeat.setBackgroundResource(R.drawable.bg_border_gray);
                break;
            case SELECTED:
                holder.mSeat.setBackgroundResource(R.drawable.bg_border_blue_filled);
                break;
        }

        holder.mSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.select(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSeats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mSeat;

        ViewHolder(View aView) {
            super(aView);
            mSeat = aView.findViewById(R.id.inflate_book_seat);
        }
    }

    public interface Callback {
        void select(int aPosition);
    }
}
