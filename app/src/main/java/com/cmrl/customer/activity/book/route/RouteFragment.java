package com.cmrl.customer.activity.book.route;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.cmrl.customer.activity.book.BookingActivity;
import com.cmrl.customer.api.AppServices;
import com.cmrl.customer.base.BaseFragment;
import com.cmrl.customer.helper.AppDialogs;
import com.cmrl.customer.helper.AppHelper;
import com.cmrl.customer.http.Response;
import com.cmrl.customer.http.ResponseListener;
import com.cmrl.customer.model.Routes;
import com.cmrl.customer.utils.RecyclerSectionItemDecorationList;

import java.util.ArrayList;

/**
 * Created by Mathan on 18-07-2019.
 */

@SuppressLint("ValidFragment")
public class RouteFragment extends BaseFragment implements RouteAdapter.Callback, ResponseListener {

    RecyclerView mRouteRecycler;
    RouteAdapter mAdapter;
    ArrayList<Routes> mRouteData = new ArrayList<>();
    SwipeRefreshLayout mSwipe;
    Context mContext;
    View mView;
    Routes mRoutes;

    public RouteFragment(Routes routes) {
        this.mRoutes = routes;
    }


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
                loadData();
            }
        });

        initRecycler();

        return false;
    }

    private void loadData() {
        if (checkInternet()) {
            AppServices.getRoutes(mContext, this, 3);
//            AppServices.getRoutes(mContext, mRoutes.routeId);
        }
    }

    private void initRecycler() {
        mRouteRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mRouteRecycler.setHasFixedSize(true);
        mAdapter = new RouteAdapter(mContext, mRouteData, this);
        mRouteRecycler.setAdapter(mAdapter);

        RecyclerSectionItemDecorationList itemDecoration = new RecyclerSectionItemDecorationList(
                R.id.inflate_header, R.layout.layout_inflate_route_header,
                getResources().getDimensionPixelSize(R.dimen.invite_list_heading),
                true, getSectionCallback(mRouteData));
        mRouteRecycler.addItemDecoration(itemDecoration);

        initData(mRoutes.routeSlots);
    }

    private void initData(ArrayList<Routes> routeSlots) {
        mRouteData.clear();
        mRouteData.addAll(routeSlots);

        // Adding Slot name manually
        for (int i = 0; i < mRouteData.size(); i++) {
            mRouteData.get(i).subId = String.format("SLOT %s", mRouteData.get(i).id);
        }

        mSwipe.setRefreshing(false);
        AppHelper.INSTANCE.showNoData(mView, mRouteData.size() == 0, R.drawable.ic_book_cab, "No Routes Found!");
        mAdapter.notifyDataSetChanged();
    }


    private RecyclerSectionItemDecorationList.SectionCallback getSectionCallback(final ArrayList<Routes> routes) {
        try {
            return new RecyclerSectionItemDecorationList.SectionCallback() {
                @Override
                public boolean isSection(int position) {
                    try {
                        if (!routes.get(position).subId.equals("")) {
                            return (position == 0 || !routes.get(position).subId.toLowerCase().equals(routes.get(position - 1).subId.toLowerCase()));
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
                        aFirstLetter = routes.get(position).subId.toUpperCase();
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

    @Override
    public void select(int aPosition) {
        startActivity(new Intent(mContext, BookingActivity.class));
    }

    @Override
    public void onResponse(Response response) {
        AppDialogs.hideProgressDialog();
        mSwipe.setRefreshing(false);
        try {
            if (response != null) {
                if (response.requestType == AppServices.API.routes.hashCode()) {
                    if (response.isSuccess()) {
                        Routes routes = ((Routes) response);
                        initData(routes.data.get(0).routeSlots);
                    } else AppDialogs.okAction(mContext, response.message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
