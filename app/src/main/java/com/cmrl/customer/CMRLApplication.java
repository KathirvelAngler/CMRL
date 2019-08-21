package com.cmrl.customer;

import android.app.Application;
import android.content.ContextWrapper;

import com.cmrl.customer.preference.CMRLPrefs;

/**
 * Created by Mathan on 21-08-2019.
 */


public class CMRLApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initPref();
    }

    private void initPref() {
        new CMRLPrefs.Builder().setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true).build();
    }
}
