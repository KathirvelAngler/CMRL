package com.cmrl.customer;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmrl.customer.base.BaseActivity;

/**
 * Created by Mathan on 12-07-2019.
 */

public class DummyActivity extends BaseActivity implements View.OnClickListener {

    ImageView mBack;
    Context mContext;
    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_cab);

        mView = getWindow().getDecorView().getRootView();
        mContext = this;
        mBack = findViewById(R.id.header_app_back);

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
        ((TextView) findViewById(R.id.header_app_header)).setText(getString(R.string.book_cab));
        return false;
    }

    @Override
    public boolean clickListener() {
        mBack.setOnClickListener(this);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_app_back:
                onBackPressed();
                break;
        }
    }
}
