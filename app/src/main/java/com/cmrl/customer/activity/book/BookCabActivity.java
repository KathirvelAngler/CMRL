package com.cmrl.customer.activity.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajithvgiri.searchdialog.OnSearchItemSelected;
import com.ajithvgiri.searchdialog.SearchListItem;
import com.ajithvgiri.searchdialog.SearchableDialog;
import com.cmrl.customer.R;
import com.cmrl.customer.activity.book.route.RouteActivity;
import com.cmrl.customer.api.AppServices;
import com.cmrl.customer.base.BaseActivity;
import com.cmrl.customer.helper.AppDialogs;
import com.cmrl.customer.http.Response;
import com.cmrl.customer.http.ResponseListener;
import com.cmrl.customer.model.Routes;
import com.cmrl.customer.model.Stops;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Mathan on 12-07-2019.
 */

public class BookCabActivity extends BaseActivity implements View.OnClickListener, ResponseListener {

    Button mSearchCab;
    Context mContext;
    ArrayList<SearchListItem> mData;
    TextView mPickLocation, mDropLocation;
    ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_cab);

        mContext = this;
        mBack = findViewById(R.id.header_app_back);

        mSearchCab = findViewById(R.id.activity_book_search_cab);
        mPickLocation = findViewById(R.id.activity_book_pick_location);
        mDropLocation = findViewById(R.id.activity_book_drop_location);

        clickListener();
    }

    private void initDialog(String title, ArrayList<SearchListItem> mData, final TextView view) {

        SearchableDialog searchableDialog = new SearchableDialog(this, mData, title);
        searchableDialog.show();
        searchableDialog.setOnItemSelected(new OnSearchItemSelected() {
            @Override
            public void onClick(int i, SearchListItem searchListItem) {
                view.setText(searchListItem.getTitle());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHeader();
    }

    @Override
    public boolean initBundle() {
        return false;
    }

    @Override
    public boolean initHeader() {
        ((TextView) findViewById(R.id.header_app_header)).setText(getString(R.string.book_cab));
        return true;
    }

    @Override
    public boolean clickListener() {
        mSearchCab.setOnClickListener(this);
        mPickLocation.setOnClickListener(this);
        mDropLocation.setOnClickListener(this);
        mBack.setOnClickListener(this);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_app_back:
                onBackPressed();
                break;
            case R.id.activity_book_pick_location:
                getStations();
                break;
            case R.id.activity_book_drop_location:
                getStops();
                break;
            case R.id.activity_book_search_cab:
                searchRoutes();
                break;
        }

    }

    private void searchRoutes() {
        if (checkInternet()) {
            AppDialogs.showProgressDialog(mContext);
            AppServices.searchRoutes(mContext, 1, 5);
        } else AppDialogs.okAction(mContext, getString(R.string.no_internet));
    }

    private void getStops() {
        if (checkInternet()) {
            AppDialogs.showProgressDialog(mContext);
            AppServices.getStops(mContext);
        } else AppDialogs.okAction(mContext, getString(R.string.no_internet));
    }

    private void getStations() {
        if (checkInternet()) {
            AppDialogs.showProgressDialog(mContext);
            AppServices.getStations(mContext);
        } else AppDialogs.okAction(mContext, getString(R.string.no_internet));
    }

    @Override
    public void onResponse(Response response) {
        try {
            AppDialogs.hideProgressDialog();
            if (response != null) {
                if (response.requestType == AppServices.API.stations.hashCode()) {
                    if (response.isSuccess()) {
                        Stops stops = ((Stops) response);
                        if (stops.data.size() > 0) {
                            mData = new ArrayList<>();
                            for (int i = 0; i < stops.data.size(); i++) {
                                SearchListItem data = new SearchListItem(i, stops.data.get(i).name);
                                mData.add(data);
                            }
                            initDialog("Pick", mData, mPickLocation);
                        }
                    } else AppDialogs.okAction(mContext, response.message);
                } else if (response.requestType == AppServices.API.stops.hashCode()) {
                    if (response.isSuccess()) {
                        Stops stops = ((Stops) response);
                        if (stops.data.size() > 0) {
                            mData = new ArrayList<>();
                            for (int i = 0; i < stops.data.size(); i++) {
                                SearchListItem data = new SearchListItem(i, stops.data.get(i).name);
                                mData.add(data);
                            }
                            initDialog("Drop", mData, mDropLocation);
                        }
                    } else AppDialogs.okAction(mContext, response.message);
                } else if (response.requestType == AppServices.API.routes.hashCode()) {
                    if (response.isSuccess()) {
                        Routes routes = ((Routes) response);
                        if (routes.data.size() > 0) {
                            Intent intent = new Intent(mContext, RouteActivity.class);
                            intent.putExtra("routes", new Gson().toJson(routes));
                            startActivity(intent);
                        } else AppDialogs.okAction(mContext, "No Routes Available!");
                    } else AppDialogs.okAction(mContext, response.message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
