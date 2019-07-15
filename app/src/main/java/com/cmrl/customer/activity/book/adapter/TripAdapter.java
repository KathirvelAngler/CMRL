package com.cmrl.customer.activity.book.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmrl.customer.R;
import com.cmrl.customer.model.TripDetails;
import com.cmrl.customer.preference.CMRLConstants;

import java.util.ArrayList;

/**
 * Created by Mathan on 15-07-2019.
 */

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> implements CMRLConstants {

    private Context mContext;
    private ArrayList<TripDetails> mDetails;

    public TripAdapter(Context aContext, ArrayList<TripDetails> details) {
        mContext = aContext;
        mDetails = details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View aView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inflate_trip_details, parent, false);
        return new ViewHolder(aView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        TripDetails detail = mDetails.get(position);

        holder.mLable.setText(detail.detail);
        holder.mIcon.setImageDrawable(detail.icon);
    }

    @Override
    public int getItemCount() {
        return mDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mLable;
        ImageView mIcon;

        ViewHolder(View aView) {
            super(aView);
            mIcon = aView.findViewById(R.id.inflate_trip_icon);
            mLable = aView.findViewById(R.id.inflate_trip_name);
        }
    }
}
