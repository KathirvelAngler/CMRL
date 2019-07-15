package com.cmrl.customer.base;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cmrl.customer.utils.APPNetworkUtil;

/**
 * Created by Mathan on 09-07-2019.
 */

public abstract class BaseFragment extends Fragment {

    protected abstract void onBackPressed();

    public abstract void onResumeFragment();

    public abstract boolean init(View view);

    public abstract boolean initBundle();

    public abstract boolean clickListener();

    public boolean checkInternet() {
        return APPNetworkUtil.isInternetOn(getContext());
    }

    public String getETValue(EditText aEditText) {
        return aEditText.getText().toString().trim();
    }

    public String getTXTValue(TextView aTextText) {
        return aTextText.getText().toString().trim();
    }

    @Override

    public void onResume() {
        onResumeFragment();
        super.onResume();
    }
}
