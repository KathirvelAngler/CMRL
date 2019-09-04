package com.cmrl.customer.activity.book;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmrl.customer.R;
import com.cmrl.customer.api.AppServices;
import com.cmrl.customer.base.BaseActivity;
import com.cmrl.customer.helper.AppDialogs;
import com.cmrl.customer.model.Booking;
import com.cmrl.customer.model.User;
import com.cmrl.customer.preference.LocalStorageSP;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Mathan on 12-07-2019.
 */

public class PaymentActivity extends BaseActivity implements View.OnClickListener {

    ImageView mBack;
    Context mContext;
    View mView;
    Booking mDetails;
    WebView mWebView;
//    boolean isRedirected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mView = getWindow().getDecorView().getRootView();
        mContext = this;
        mBack = findViewById(R.id.header_app_back);
        mWebView = findViewById(R.id.activity_payment_webview);

        clickListener();

        initBundle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHeader();
    }

    @Override
    public boolean initBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            mDetails = new Gson().fromJson(intent.getStringExtra("booking_data"), Booking.class);

            initWebView();
        }
        return true;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        try {
            if (checkInternet()) {

                final String url = AppServices.constructUrl(AppServices.API.billDesk);
                User user = LocalStorageSP.INSTANCE.getLoginUser(mContext);
                final String postData = String.format("tripId=%s&customerMobile=%s&customerName=%s&customerToken=%s&cabAssigned=%s&" +
                                "noOfSeats=%s&amount=%s&pickupLat=%s&pickupLng=%s&pickUpLocation=%s&pickupStopId=%s&" +
                                "dropLat=%s&dropLng=%s&dropLocation=%s&dropStopId=%s&paymentMode=%s&routeSlotId=%s"
                        , encodeData(String.valueOf(mDetails.tripId))
                        , encodeData(user.mobile)
                        , encodeData(user.fullName)
                        , encodeData(user.token)
                        , encodeData(mDetails.cabNumber)
                        , encodeData(String.valueOf(mDetails.totalTickets))
                        , encodeData(String.valueOf(2))
//                        , encodeData(String.valueOf(mDetails.totalTickets * mDetails.fare))
                        , encodeData(String.valueOf(mDetails.pickup.lat))
                        , encodeData(String.valueOf(mDetails.pickup.lng))
                        , encodeData(mDetails.pickup.location)
                        , encodeData(String.valueOf(mDetails.pickup.stopId))
                        , encodeData(String.valueOf(mDetails.drop.lat))
                        , encodeData(String.valueOf(mDetails.drop.lng))
                        , encodeData(mDetails.drop.location)
                        , encodeData(String.valueOf(mDetails.drop.stopId))
                        , encodeData("Cash")
                        , encodeData(String.valueOf(mDetails.routeId)));

                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.getSettings().setSupportZoom(true);
                mWebView.getSettings().setBuiltInZoomControls(true);
                mWebView.getSettings().setAllowFileAccess(true);
                mWebView.postUrl(url, postData.getBytes("UTF-8"));
                mWebView.requestFocus(View.FOCUS_DOWN);

                mWebView.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        Log.i("Payment URL -->> ", view.getUrl());
                        Log.i("Payment Param -->> ", postData);
                        AppDialogs.showProgressDialog(mContext);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        Log.d("onPageFinished -->> ", view.getUrl());
                        AppDialogs.hideProgressDialog();
                    }

                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        AppDialogs.hideProgressDialog();
                        AppDialogs.okAction(mContext, getString(R.string.something));
                    }
                });
            } else AppDialogs.okAction(mContext, getString(R.string.no_internet));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String encodeData(String data) {
        try {
            return URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean initHeader() {
        ((TextView) findViewById(R.id.header_app_header)).setText(getString(R.string.booking));
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
}
