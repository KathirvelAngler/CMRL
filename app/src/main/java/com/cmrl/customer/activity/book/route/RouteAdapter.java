package com.cmrl.customer.activity.book.route;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
    private Callback mCallback;

    RouteAdapter(Context aContext, ArrayList<Route> details, Callback callback) {
        mContext = aContext;
        mDetails = details;
        mCallback = callback;
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

        holder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.select(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDetails.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mItem;

        ItemViewHolder(View aView) {
            super(aView);

            mItem = aView.findViewById(R.id.inflate_route_item);
        }


    }

    public interface Callback {
        void select(int aPosition);
    }
}
