package com.cmrl.customer.activity.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.cmrl.customer.R;
import com.cmrl.customer.helper.AppHelper;
import com.cmrl.customer.model.User;
import com.cmrl.customer.preference.LocalStorageSP;

/**
 * Created by Mathan on 08-07-2019.
 */

public class HomeActivity extends AppCompatActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        initBundle();
    }

    private void initBundle() {
        try {
            /*Intent intent = getIntent();
            if (intent != null && intent.getExtras() != null) {
                User user = AppHelper.INSTANCE.decodeToken(mContext, intent.getExtras().getString("token"));
                if (user != null) {
                    LocalStorageSP.INSTANCE.storeLoginUser(mContext, user);
                    initFragment();
                } else AppDialogs.okAction(mContext, "Failed! Invalid User Token!");
            } else AppDialogs.okAction(mContext, getString(R.string.something));*/


            User user = AppHelper.INSTANCE.decodeToken(mContext, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2N0TmJyIjoiOCIsImZpcnN0TmFtZSI6ImphaSIsImxhc3ROYW1lIjoiIiwibW9iaWxlIjoiODE0ODUzNTA3NiIsImRvYiI6IjE4IiwiZ2VuZGVyIjoiTSIsImV4cCI6MTU2NTkzMzY1OH0.t5klPW09Vji6DQi5ptLpt7Dd2mT1-h9UJryVeOkF2J8");
            LocalStorageSP.INSTANCE.storeLoginUser(mContext, user);
            initFragment();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFragment() {
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frame_container, new HomeFragment(), HomeFragment.TAG);
        transaction.addToBackStack(HomeFragment.TAG);
        transaction.commit();
    }


    // TODO CMRL Connect Process
    /*
    ###############################################################################################

    Intent intent = new Intent(mContext, HomeActivity.class);
    intent.putExtra("token", "your_token_here");
    startActivity(intent);


    ###############################################################################################
     */

//    eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2N0TmJyIjoiOCIsImZpcnN0TmFtZSI6ImphaSIsImxhc3ROYW1lIjoiIiwibW9iaWxlIjoiODE0ODUzNTA3NiIsImRvYiI6IjE4IiwiZ2VuZGVyIjoiTSIsImV4cCI6MTU2NTkzMzY1OH0.t5klPW09Vji6DQi5ptLpt7Dd2mT1-h9UJryVeOkF2J8
}
