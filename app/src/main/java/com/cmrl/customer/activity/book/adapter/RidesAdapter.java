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
import com.cmrl.customer.model.TripDetails;
import com.cmrl.customer.preference.CMRLConstants;

import java.util.ArrayList;

/**
 * Created by Mathan on 15-07-2019.
 */

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder> implements CMRLConstants {

    private Context mContext;
    private ArrayList<TripDetails> mDetails;

    public RidesAdapter(Context aContext, ArrayList<TripDetails> details) {
        mContext = aContext;
        mDetails = details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View aView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inflate_ride_history, parent, false);
        return new ViewHolder(aView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        TripDetails detail = mDetails.get(position);

        if (position == 0) {
            holder.mStatus.setText("Inprogress");
            holder.mStatus.setTextColor(mContext.getResources().getColor(R.color.app_light_blue));
        } else if (position == 3) {
            holder.mStatus.setText("Cancelled");
            holder.mStatus.setTextColor(mContext.getResources().getColor(R.color.app_red));
        } else {
            holder.mStatus.setText("Completed");
            holder.mStatus.setTextColor(mContext.getResources().getColor(R.color.app_green));
        }
    }

    @Override
    public int getItemCount() {
        return mDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mStatus;

        ViewHolder(View aView) {
            super(aView);

            mStatus = aView.findViewById(R.id.inflate_ride_status);
        }
    }
}
