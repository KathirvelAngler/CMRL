package com.cmrl.customer.activity.track;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmrl.customer.R;
import com.cmrl.customer.base.BaseActivity;
import com.cmrl.customer.utils.PermissionChecker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by Mathan on 23-07-2019.
 */

public class TrackMapActivity extends BaseActivity implements View.OnClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnSuccessListener<Location> {

    ImageView mBack;
    Context mContext;
    View mView;
    SupportMapFragment mMapFragment;
    String[] mPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    GoogleApiClient mGoogleClient;
    Location mLocation;
    Marker mMarker;
    LocationRequest mLocationRequest;
    GoogleMap mMap;
    PermissionChecker mChecker;
    FusedLocationProviderClient mFusedClient;
    int PLACE_PICKER_REQUEST = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_cab_map);

        mView = getWindow().getDecorView().getRootView();
        mContext = this;
        mBack = findViewById(R.id.header_app_back);
        mChecker = new PermissionChecker();

        mChecker.askAllPermissions(mContext, mPermissions);

        mFusedClient = LocationServices.getFusedLocationProviderClient(this);

        initMap();

        clickListener();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = null;
                if (data != null) {
                    place = PlacePicker.getPlace(mContext, data);
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Initialize Google Map
     */
    private void initMap() {
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.activity_track_map);
        if (mMapFragment != null) {
            mMapFragment.getMapAsync(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHeader();
//        initPlacePicker();
    }

    @Override
    public boolean initBundle() {
        return false;
    }

    @Override
    public boolean initHeader() {
        ((TextView) findViewById(R.id.header_app_header)).setText(getString(R.string.track_cab));
        return false;
    }

    @Override
    public boolean clickListener() {
        mBack.setOnClickListener(this);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.header_app_back) {
            onBackPressed();
        }
    }

    // Google Map Setup
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        /*mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);*/
        if (mChecker.checkAllPermission(mContext, mPermissions)) {
            mMap.setMyLocationEnabled(true);
        } else mChecker.askAllPermissions(mContext, mPermissions);

        buildGoogleApiClient();
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleClient.connect();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (mChecker.checkAllPermission(mContext, mPermissions)) {
            mFusedClient.getLastLocation().addOnSuccessListener(this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSuccess(Location location) {
        mLocation = location;
        if (mMarker != null)
            mMarker.remove();

        // Showing Current Location Marker on Map
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d("Location LatLng --->>> ", String.valueOf(latLng));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        /*if (mGoogleClient != null) {
            if (mChecker.checkAllPermission(mContext, mPermissions)) {
                mFusedClient.getLastLocation().addOnSuccessListener(this);
            }
        }*/
    }
}
