package com.cmrl.customer.base;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.cmrl.customer.utils.APPNetworkUtil;

/**
 * Created by Mathan on 10-07-2019.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public boolean onResumeActivity() {
        onResume();
        return true;
    }

    public abstract boolean initBundle();

    public abstract boolean initHeader();

    public abstract boolean clickListener();

    public boolean checkInternet() {
        return APPNetworkUtil.isInternetOn(getApplicationContext());
    }

    public String getETValue(EditText aEditText) {
        return aEditText.getText().toString().trim();
    }

    public String getTXTValue(TextView aTextText) {
        return aTextText.getText().toString().trim();
    }
}
