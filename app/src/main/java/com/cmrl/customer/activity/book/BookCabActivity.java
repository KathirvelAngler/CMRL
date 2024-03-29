package com.cmrl.customer.activity.book;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.cmrl.customer.helper.AppHelper;
import com.cmrl.customer.http.Response;
import com.cmrl.customer.http.ResponseListener;
import com.cmrl.customer.model.Routes;
import com.cmrl.customer.model.Stops;
import com.cmrl.customer.utils.PermissionChecker;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

/**
 * Created by Mathan on 12-07-2019.
 */

public class BookCabActivity extends BaseActivity implements View.OnClickListener, ResponseListener {

    Button mSearchCab;
    Context mContext;
    ArrayList<SearchListItem> mData;
    TextView mPickLocation, mDropLocation, mMetro, mHome;
    ImageView mBack, mCurrentLocation;
    int PLACE_PICKER_REQUEST = 99;
    boolean isDropMetro = true, isCurrentLocation = false;
    Location mLocation;
    int mSelectedId = 0;

    String[] mPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    PermissionChecker mChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_cab);

        mContext = this;
        mChecker = new PermissionChecker();
        mBack = findViewById(R.id.header_app_back);

        mMetro = findViewById(R.id.activity_book_metro_section);
        mHome = findViewById(R.id.activity_book_home_section);

        mSearchCab = findViewById(R.id.activity_book_search_cab);
        mPickLocation = findViewById(R.id.activity_book_pick_location);
        mDropLocation = findViewById(R.id.activity_book_drop_location);
        mCurrentLocation = findViewById(R.id.activity_book_current_location);

        mChecker.askAllPermissions(mContext, mPermissions);

        clickListener();
    }

    private void initDialog(String title, ArrayList<SearchListItem> mData, final TextView view) {
        if (view.getId() == R.id.activity_book_pick_location) {
            reset(mPickLocation);
        }
        SearchableDialog searchableDialog = new SearchableDialog(this, mData, title);
        searchableDialog.show();
        searchableDialog.setOnItemSelected(new OnSearchItemSelected() {
            @Override
            public void onClick(int i, SearchListItem searchListItem) {
                view.setText(searchListItem.getTitle());
                view.setTag(searchListItem.getId());

                if (mSelectedId == 0 && !isCurrentLocation)
                    mSelectedId = searchListItem.getId();

                if (view.getId() == R.id.activity_book_pick_location) {
                    isCurrentLocation = false;
                    reset(mDropLocation);
                }

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

        mHome.setOnClickListener(this);
        mMetro.setOnClickListener(this);

        return true;
    }

    private void reset(TextView view) {
        view.setText(null);
        view.setTag(String.valueOf(-1));
    }

    private void resetAll(boolean metro) {
        reset(mPickLocation);
        reset(mDropLocation);
        isDropMetro = metro;
        mSelectedId = 0;

        if (metro) {
            mCurrentLocation.setVisibility(View.VISIBLE);
            mHome.setBackgroundColor(mContext.getResources().getColor(R.color.app_blue));
            mMetro.setBackgroundColor(mContext.getResources().getColor(R.color.app_light_blue));
        } else {
            mCurrentLocation.setVisibility(View.INVISIBLE);
            mMetro.setBackgroundColor(mContext.getResources().getColor(R.color.app_blue));
            mHome.setBackgroundColor(mContext.getResources().getColor(R.color.app_light_blue));
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.header_app_back) {
            onBackPressed();
        } else if (i == R.id.activity_book_pick_location) {
            mSelectedId = 0;
            if (isDropMetro)
                getStops();
            else getStations();
        } else if (i == R.id.activity_book_drop_location) {
            if (mPickLocation.getTag().equals("-1")) {
                AppDialogs.okAction(mContext, "Pick location should not be empty");
                return;
            }

            if (isDropMetro)
                getStations();
            else getStops();
        } else if (i == R.id.activity_book_current_location) {//                AppDialogs.okAction(mContext, "NYI");
            if (mChecker.checkAllPermission(mContext, mPermissions))
                initPlacePicker();
        } else if (i == R.id.activity_book_search_cab) {
            validate();
        } else if (i == R.id.activity_book_metro_section) {
            resetAll(true);
        } else if (i == R.id.activity_book_home_section) {
            resetAll(false);
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
        } else {
            if (isCurrentLocation)
                searchRoutes(mLocation, true);
            else getCurrentLocation();
        }

    }

    private void getCurrentLocation() {
        AppHelper.INSTANCE.getCurrentLocation(mContext, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                AppDialogs.hideProgressDialog();
                if (location != null)
                    searchRoutes(location, isCurrentLocation);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AppDialogs.hideProgressDialog();
                AppDialogs.okAction(mContext, e.getMessage());
            }
        });
    }

    private void searchRoutes(Location location, boolean isCurrentLocation) {
        if (checkInternet()) {
            AppDialogs.showProgressDialog(mContext);
            AppServices.searchRoutes(mContext, mPickLocation.getTag().toString(),
                    mDropLocation.getTag().toString(), location, isCurrentLocation);
        } else AppDialogs.okAction(mContext, getString(R.string.no_internet));
    }

    /**
     * Get Pickup Stops
     */

    private void getStops() {
        if (checkInternet()) {
            AppDialogs.showProgressDialog(mContext);
            AppServices.getStops(mContext, mSelectedId);
        } else AppDialogs.okAction(mContext, getString(R.string.no_internet));
    }

    /**
     * Get Metro Stations
     */
    private void getStations() {
        if (checkInternet()) {
            AppDialogs.showProgressDialog(mContext);
            AppServices.getStations(mContext, mSelectedId);
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
                                if (stops.data.get(i).stopName != null) {
                                    SearchListItem data = new SearchListItem(stops.data.get(i).id, stops.data.get(i).stopName);
                                    mData.add(data);
                                }
                            }
                            String dialog = String.format("Choose %s Location", isDropMetro ? "Drop" : "Pick");
                            initDialog(dialog, mData, isDropMetro ? mDropLocation : mPickLocation);
                        }
                    } else AppDialogs.okAction(mContext, response.message);
                } else if (response.requestType == AppServices.API.stops.hashCode()) {
                    if (response.isSuccess()) {
                        Stops stops = ((Stops) response);
                        if (stops.data.size() > 0) {
                            mData = new ArrayList<>();
                            for (int i = 0; i < stops.data.size(); i++) {
                                if (stops.data.get(i).stopName != null) {
                                    SearchListItem data = new SearchListItem(stops.data.get(i).id, stops.data.get(i).stopName);
                                    mData.add(data);
                                }
                            }
                            String dialog = String.format("Choose %s Location", isDropMetro ? "Pick" : "Drop");
                            initDialog(dialog, mData, isDropMetro ? mPickLocation : mDropLocation);
                        }
                    } else AppDialogs.okAction(mContext, response.message);
                } else if (response.requestType == AppServices.API.routes.hashCode()) {
                    if (response.isSuccess()) {
                        Routes routes = ((Routes) response);
                        if (routes.data.size() > 0) {
                            Intent intent = new Intent(mContext, RouteActivity.class);
                            intent.putExtra("routes", routes);
                            startActivity(intent);
                            isCurrentLocation = false;
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
                    mPickLocation.setText(place.getName());
                    mPickLocation.setTag("1");
                    reset(mDropLocation);
                    isCurrentLocation = true;
                    mLocation = new Location("");
                    mLocation.setLatitude(place.getLatLng().latitude);
                    mLocation.setLongitude(place.getLatLng().longitude);
                    searchRoutes(mLocation, true);
                } else AppDialogs.okAction(mContext, "Failed to get current location!");
            }
        }
    }
}
