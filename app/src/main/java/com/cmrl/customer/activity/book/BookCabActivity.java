package com.cmrl.customer.activity.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajithvgiri.searchdialog.OnSearchItemSelected;
import com.ajithvgiri.searchdialog.SearchListItem;
import com.ajithvgiri.searchdialog.SearchableDialog;
import com.cmrl.customer.R;
import com.cmrl.customer.activity.book.route.RouteActivity;
import com.cmrl.customer.base.BaseActivity;

import java.util.ArrayList;

/**
 * Created by Mathan on 12-07-2019.
 */

public class BookCabActivity extends BaseActivity implements View.OnClickListener {

    Button mSearchCab;
    Context mContext;
    ArrayList<SearchListItem> mData = new ArrayList<>();
    TextView mPickLocation, mDropLocation;
    ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_cab);

        mContext = this;
        mBack = findViewById(R.id.header_app_back);

        mSearchCab = findViewById(R.id.activity_book_search_cab);
        mPickLocation = findViewById(R.id.activity_book_pick_location);
        mDropLocation = findViewById(R.id.activity_book_drop_location);

        clickListener();
    }

    private void initDialog(String title, final TextView view) {

        SearchableDialog searchableDialog = new SearchableDialog(this, mData, title);
        for (int i = 0; i < 30; i++) {
            SearchListItem data = new SearchListItem(i, String.format("Route %s", i + 1));
            mData.add(data);
        }
        searchableDialog.show();
        searchableDialog.setOnItemSelected(new OnSearchItemSelected() {
            @Override
            public void onClick(int i, SearchListItem searchListItem) {
                view.setText(searchListItem.getTitle());
            }
        });
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
        return true;
    }

    @Override
    public boolean clickListener() {
        mSearchCab.setOnClickListener(this);
        mPickLocation.setOnClickListener(this);
        mDropLocation.setOnClickListener(this);
        mBack.setOnClickListener(this);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_app_back:
                onBackPressed();
                break;
            case R.id.activity_book_pick_location:
                initDialog("Pick", mPickLocation);
                break;
            case R.id.activity_book_drop_location:
                initDialog("Drop", mDropLocation);
                break;
            case R.id.activity_book_search_cab:
                startActivity(new Intent(mContext, RouteActivity.class));
                break;
        }

    }
}
