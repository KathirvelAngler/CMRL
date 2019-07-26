package com.cmrl.customer.activity.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
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
    ImageView mBack, mCurrentLocation;
    int PLACE_PICKER_REQUEST = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_cab);

        mContext = this;
        mBack = findViewById(R.id.header_app_back);

        mSearchCab = findViewById(R.id.activity_book_search_cab);
        mPickLocation = findViewById(R.id.activity_book_pick_location);
        mDropLocation = findViewById(R.id.activity_book_drop_location);
        mCurrentLocation = findViewById(R.id.activity_book_current_location);

        clickListener();
    }

    private void initDialog(String title, ArrayList<SearchListItem> mData, final TextView view) {

        SearchableDialog searchableDialog = new SearchableDialog(this, mData, title);
        searchableDialog.show();
        searchableDialog.setOnItemSelected(new OnSearchItemSelected() {
            @Override
            public void onClick(int i, SearchListItem searchListItem) {
                view.setText(searchListItem.getTitle());
                view.setTag(searchListItem.getId());
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
        mCurrentLocation.setOnClickListener(this);
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
            case R.id.activity_book_current_location:
//                initPlacePicker();
                break;
            case R.id.activity_book_search_cab:
                validate();
                break;
        }

    }

    private void initPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void validate() {
        if (mPickLocation.getTag().equals("-1")) {
            AppDialogs.okAction(mContext, "Pick location should not be empty");
        } else if (mDropLocation.getTag().equals("-1")) {
            AppDialogs.okAction(mContext, "Drop location should not be empty");
        } else searchRoutes();
    }

    private void searchRoutes() {
        if (checkInternet()) {
            AppDialogs.showProgressDialog(mContext);
            AppServices.searchRoutes(mContext, mPickLocation.getTag().toString(),
                    mDropLocation.getTag().toString());
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
                                SearchListItem data = new SearchListItem(stops.data.get(i).id, stops.data.get(i).name);
                                mData.add(data);
                            }
                            initDialog("Choose Pick Location", mData, mPickLocation);
                        }
                    } else AppDialogs.okAction(mContext, response.message);
                } else if (response.requestType == AppServices.API.stops.hashCode()) {
                    if (response.isSuccess()) {
                        Stops stops = ((Stops) response);
                        if (stops.data.size() > 0) {
                            mData = new ArrayList<>();
                            for (int i = 0; i < stops.data.size(); i++) {
                                SearchListItem data = new SearchListItem(stops.data.get(i).id, stops.data.get(i).stopName);
                                mData.add(data);
                            }
                            initDialog("Choose Drop Location", mData, mDropLocation);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Place place = PlacePicker.getPlace(mContext, data);
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                } else AppDialogs.okAction(mContext, "Failed to get current location!");
            }
        }
    }
}
