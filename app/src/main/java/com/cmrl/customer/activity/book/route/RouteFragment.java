package com.cmrl.customer.activity.book.route;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmrl.customer.R;
import com.cmrl.customer.base.BaseFragment;
import com.cmrl.customer.helper.AppHelper;
import com.cmrl.customer.model.Route;
import com.cmrl.customer.utils.RecyclerSectionItemDecorationList;

import java.util.ArrayList;

/**
 * Created by Mathan on 18-07-2019.
 */

public class RouteFragment extends BaseFragment {

    RecyclerView mRouteRecycler;
    RouteAdapter mAdapter;
    ArrayList<Route> mRoutes = new ArrayList<>();
    SwipeRefreshLayout mSwipe;
    Context mContext;
    View mView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.viewpager_route, container, false);
        init(mView);
        return mView;
    }

    @Override
    protected void onBackPressed() {

    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public boolean init(View view) {
        mContext = getActivity();

        mRouteRecycler = view.findViewById(R.id.activity_route_recycler);
        mSwipe = view.findViewById(R.id.activity_route_swipe);

        AppHelper.INSTANCE.swipeRefColor(mContext, mSwipe);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(false);
            }
        });

        initRecycler();

        return false;
    }

    private void initRecycler() {
        mRouteRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mRouteRecycler.setHasFixedSize(true);
        mAdapter = new RouteAdapter(mContext, mRoutes);
        mRouteRecycler.setAdapter(mAdapter);

        RecyclerSectionItemDecorationList itemDecoration = new RecyclerSectionItemDecorationList(
                R.id.inflate_header, R.layout.layout_inflate_route_header,
                getResources().getDimensionPixelSize(R.dimen.invite_list_heading),
                true, getSectionCallback(mRoutes));
        mRouteRecycler.addItemDecoration(itemDecoration);

        initData(true);
    }

    private void initData(boolean isShow) {
        mRoutes.clear();
        for (int i = 0; i < 20; i++) {
            Route route = new Route();
            if (i == 0 || i == 3 || i == 6)
                route.name = String.format("Aoute %s", i + 1);
            else route.name = "Route";
            mRoutes.add(route);
        }
        mSwipe.setRefreshing(false);
        AppHelper.INSTANCE.showNoData(mView, mRoutes.size() == 0, "No Routes Found!");
        mAdapter.notifyDataSetChanged();
    }


    private RecyclerSectionItemDecorationList.SectionCallback getSectionCallback(final ArrayList<Route> users) {
        try {
            return new RecyclerSectionItemDecorationList.SectionCallback() {
                @Override
                public boolean isSection(int position) {
                    try {
                        if (!users.get(position).name.equals("")) {
                            return (position == 0 || !users.get(position).name.toLowerCase().equals(users.get(position - 1).name.toLowerCase()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }

                @Override
                public String getSectionHeader(int position) {
                    String aFirstLetter = null;
                    try {
                        aFirstLetter = users.get(position).name.toUpperCase();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return aFirstLetter != null ? aFirstLetter : "";
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean initBundle() {
        return false;
    }

    @Override
    public boolean clickListener() {
        return false;
    }
}
