package com.cmrl.customer.activity.book.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmrl.customer.R;
import com.cmrl.customer.helper.AppHelper;
import com.cmrl.customer.model.History;
import com.cmrl.customer.preference.CMRLConstants;

import java.util.ArrayList;

/**
 * Created by Mathan on 15-07-2019.
 */

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder> implements CMRLConstants {

    private Context mContext;
    private ArrayList<History> mHistory;
    private Callback mCallback;

    public RidesAdapter(Context aContext, ArrayList<History> details, Callback callback) {
        mContext = aContext;
        mHistory = details;
        mCallback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View aView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inflate_ride_history, parent, false);
        return new ViewHolder(aView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final History detail = mHistory.get(position);

        holder.mDate.setText(AppHelper.INSTANCE.convertDateFormat(detail.bookedDateTime,
                DD_MM_YY_ZONE, MMM_DD_EEE));

        String ticket = detail.noOfSeats == 1 ? "Ticket" : "Tickets";
        holder.mTicket.setText(String.format("%s - %s %s", detail.bookingNo, detail.noOfSeats, ticket));

        holder.mFrom.setText(detail.pickupLocation);
        holder.mTo.setText(detail.dropLocation);

        holder.mFare.setText(String.format("%s %s", IND_RUPEE, detail.fare));

        holder.mStatus.setText(detail.bookingStatus);

        if (detail.bookingStatus.toLowerCase().equals("cancelled") ||
                detail.bookingStatus.toLowerCase().equals("rejected")) {
            holder.mStatus.setTextColor(mContext.getResources().getColor(R.color.app_red));
        } else if (detail.bookingStatus.toLowerCase().equals("completed") ||
                detail.bookingStatus.toLowerCase().equals("confirmed")) {
            holder.mStatus.setTextColor(mContext.getResources().getColor(R.color.app_green));
        }

        holder.mMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detail.tripId != 0)
                    mCallback.openDetail(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHistory.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mDate, mTicket, mFrom, mTo, mFare, mStatus;
        LinearLayout mMainLayout;

        ViewHolder(View aView) {
            super(aView);

            mMainLayout = aView.findViewById(R.id.inflate_ride_main_layout);

            mDate = aView.findViewById(R.id.inflate_ride_date);
            mTicket = aView.findViewById(R.id.inflate_ride_tickets);
            mFrom = aView.findViewById(R.id.inflate_ride_from);
            mTo = aView.findViewById(R.id.inflate_ride_to);
            mFare = aView.findViewById(R.id.inflate_ride_fare);
            mStatus = aView.findViewById(R.id.inflate_ride_status);
        }
    }

    public interface Callback {
        void openDetail(int position);
    }
}
