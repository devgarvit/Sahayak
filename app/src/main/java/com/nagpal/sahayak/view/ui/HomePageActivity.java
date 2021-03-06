package com.nagpal.sahayak.view.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nagpal.sahayak.R;
import com.nagpal.sahayak.view.adapter.HomePageAdapter;

public class HomePageActivity extends AppCompatActivity {
    private String gridItems[] = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        gridItems = new String[]{getString(R.string.add_expense),
                getString(R.string.add_invoice),
                getString(R.string.edit_expense),
                getString(R.string.edit_invoice)};

        initViews();
    }


    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        HomePageAdapter adapter = new HomePageAdapter(this, gridItems);
        recyclerView.setAdapter(adapter);
    }
}
