package com.cmrl.customer.activity.book.route;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmrl.customer.R;
import com.cmrl.customer.model.Route;
import com.cmrl.customer.preference.CMRLConstants;

import java.util.ArrayList;

/**
 * Created by Mathan on 17-07-2019.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ItemViewHolder> implements CMRLConstants {

    private Context mContext;
    private ArrayList<Route> mDetails;

    public RouteAdapter(Context aContext, ArrayList<Route> details) {
        mContext = aContext;
        mDetails = details;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inflate_route, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Route detail = mDetails.get(position);
    }

    @Override
    public int getItemCount() {
        return mDetails.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ItemViewHolder(View aView) {
            super(aView);
        }
    }
}
