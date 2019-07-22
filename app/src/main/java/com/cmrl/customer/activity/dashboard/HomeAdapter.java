package com.cmrl.customer.activity.dashboard;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmrl.customer.R;

import java.util.ArrayList;

/**
 * Created by Mathan on 09-07-2019.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private FragmentActivity mContext;
    private ArrayList<HomeFragment.Home> myMenuList;
    private Callback mCallback;

    public HomeAdapter(FragmentActivity aContext, ArrayList<HomeFragment.Home> aMenus, HomeFragment aCallback) {
        mContext = aContext;
        myMenuList = aMenus;
        this.mCallback = aCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View aView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inflate_home, parent, false);
        return new ViewHolder(aView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        HomeFragment.Home menu = myMenuList.get(position);

        holder.mMenuName.setText(menu.name);
        holder.mMenuIcon.setImageDrawable(menu.icon);

        holder.mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.loadMenu(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myMenuList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mMenuName;
        CardView mMenu;
        ImageView mMenuIcon;

        ViewHolder(View aView) {
            super(aView);
            mMenuName = aView.findViewById(R.id.inflate_home_menu_name);
            mMenuIcon = aView.findViewById(R.id.inflate_home_menu_icon);
            mMenu = aView.findViewById(R.id.inflate_home_menu);
        }
    }

    public interface Callback {
        void loadMenu(int aPosition);
    }
}
