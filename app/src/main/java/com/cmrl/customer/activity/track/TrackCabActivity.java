package com.cmrl.customer.activity.track;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmrl.customer.R;
import com.cmrl.customer.base.BaseActivity;

/**
 * Created by Mathan on 23-07-2019.
 */

public class TrackCabActivity extends BaseActivity implements View.OnClickListener {

    ImageView mBack;
    Context mContext;
    View mView;
    Button mTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_cab);

        mView = getWindow().getDecorView().getRootView();
        mContext = this;
        mBack = findViewById(R.id.header_app_back);
        mTrack = findViewById(R.id.activity_track_cab);

        clickListener();
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
        ((TextView) findViewById(R.id.header_app_header)).setText(getString(R.string.track_cab));
        return false;
    }

    @Override
    public boolean clickListener() {
        mBack.setOnClickListener(this);
        mTrack.setOnClickListener(this);
        return true;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.header_app_back) {
            onBackPressed();
        } else if (i == R.id.activity_track_cab) {
            startActivity(new Intent(mContext, TrackMapActivity.class));
        }
    }
}
