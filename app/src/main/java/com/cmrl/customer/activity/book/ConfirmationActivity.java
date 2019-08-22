package com.cmrl.customer.activity.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmrl.customer.R;
import com.cmrl.customer.base.BaseActivity;
import com.cmrl.customer.helper.AppHelper;
import com.cmrl.customer.http.Response;
import com.cmrl.customer.model.Booking;
import com.google.gson.Gson;

import static com.cmrl.customer.preference.CMRLConstants.DD_MMM_yy_HH_MM;
import static com.cmrl.customer.preference.CMRLConstants.DD_MM_YY_ZONE;

/**
 * Created by Mathan on 16-07-2019.
 */

public class ConfirmationActivity extends BaseActivity implements View.OnClickListener {

    ImageView mBack;
    Context mContext;
    TextView mBookedMsg;
    EditText mBookedCab, mBookedId, mBookedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        mContext = this;
        mBack = findViewById(R.id.header_app_back);

        mBookedMsg = findViewById(R.id.activity_booked_message);
        mBookedId = findViewById(R.id.activity_booked_id);
        mBookedCab = findViewById(R.id.activity_booked_cab);
        mBookedDate = findViewById(R.id.activity_booked_date);

        clickListener();

        initBundle();
    }

    @Override
    public boolean initBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            initDetails(new Gson().fromJson(intent.getExtras().getString("booking"), Booking.class));
        }
        return true;
    }

    private void initDetails(Response response) {
        try {
            mBookedMsg.setText(response.message);
            mBookedId.setText(((Booking) response).details.bookingNo);
            mBookedCab.setText(((Booking) response).details.cabNumber);
            String date = AppHelper.INSTANCE.convertDateFormat(((Booking) response).details.bookedDate,
                    DD_MM_YY_ZONE, DD_MMM_yy_HH_MM);
            mBookedDate.setText(date);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigateHome();
                }
            }, 3000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHeader();
    }

    @Override
    public boolean initHeader() {
        ((TextView) findViewById(R.id.header_app_header)).setText(getString(R.string.confirmed));
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
            navigateHome();
        }
    }

    private void navigateHome() {
        Intent intent = new Intent(mContext, RideHistoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateHome();
    }
}
