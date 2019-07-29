package com.cmrl.customer.activity.book.route;

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
import com.cmrl.customer.model.Routes;
import com.cmrl.customer.preference.CMRLConstants;

import java.util.ArrayList;

/**
 * Created by Mathan on 17-07-2019.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ItemViewHolder> implements CMRLConstants {

    private Context mContext;
    private ArrayList<Routes> mRoutes;
    private Callback mCallback;

    RouteAdapter(Context aContext, ArrayList<Routes> details, Callback callback) {
        mContext = aContext;
        mRoutes = details;
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
        Routes routes = mRoutes.get(position);

        holder.mCabNumber.setText(routes.cabNumber);
        holder.mTime.setText(routes.scheduleSlot);
        holder.mSeat.setText(String.valueOf(routes.availableSeats));
        holder.mRoute.setText(String.valueOf(routes.routeCode));

        holder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.select(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mItem;
        TextView mCabNumber, mTime, mSeat, mRoute;

        ItemViewHolder(View aView) {
            super(aView);

            mItem = aView.findViewById(R.id.inflate_route_item);
            mCabNumber = aView.findViewById(R.id.inflate_route_cab_number);
            mTime = aView.findViewById(R.id.inflate_route_cab_time);
            mSeat = aView.findViewById(R.id.inflate_route_cab_seat);
            mRoute = aView.findViewById(R.id.inflate_route_cab_route);
        }


    }

    public interface Callback {
        void select(int aPosition);
    }
}
