package com.cmrl.customer.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmrl.customer.R;
import com.cmrl.customer.base.BaseFragment;
import com.cmrl.customer.activity.book.BookCabActivity;

import java.util.ArrayList;

/**
 * Created by Mathan on 09-07-2019.
 */

public class HomeFragment extends BaseFragment implements HomeAdapter.Callback {

    public static String TAG = HomeFragment.class.getSimpleName();
    private FragmentActivity mContext;
    private RecyclerView mMenuRecycler;
    private HomeAdapter mMenuAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        return view;
    }

    @Override
    public boolean init(View view) {
        mContext = getActivity();
        mMenuRecycler = view.findViewById(R.id.fragment_home_menu);

        initRecycler();
        return false;
    }

    private void initRecycler() {
        LinearLayoutManager myLayoutManager = new GridLayoutManager(mContext, 2);
        mMenuRecycler.setLayoutManager(myLayoutManager);
        mMenuRecycler.setHasFixedSize(true);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Book");
        arrayList.add("Track");
        arrayList.add("History");
        arrayList.add("Help");
        mMenuAdapter = new HomeAdapter(mContext, arrayList, this);
        mMenuRecycler.setAdapter(mMenuAdapter);

    }

    @Override
    public boolean initBundle() {
        return false;
    }

    @Override
    protected void onBackPressed() {

    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public boolean clickListener() {
        return false;
    }

    @Override
    public void loadMenu(int aPosition) {
        switch (aPosition) {
            case 0:
                startActivity(new Intent(mContext, BookCabActivity.class));
                break;
        }
    }
}
