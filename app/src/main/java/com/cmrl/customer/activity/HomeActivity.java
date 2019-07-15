package com.cmrl.customer.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.cmrl.customer.R;
import com.cmrl.customer.fragment.home.HomeFragment;

/**
 * Created by Mathan on 08-07-2019.
 */

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();

    }

    private void initFragment() {
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frame_container, new HomeFragment(), HomeFragment.TAG);
        transaction.addToBackStack(HomeFragment.TAG);
        transaction.commit();
    }
}
