package com.nagpal.sahayak.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nagpal.sahayak.R;
import com.nagpal.sahayak.view.ui.ExpenseActivity;
import com.nagpal.sahayak.view.ui.InvoiceActivity;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {
    private String[] gridList;
    private Activity activity;

    public HomePageAdapter(Activity activity, String[] gridList) {
        this.gridList = gridList;
        this.activity = activity;
    }


    @Override
    public HomePageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_home_page, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomePageAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.txtGridItems.setText(gridList[i]);
        viewHolder.llGridItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (gridList[i].equalsIgnoreCase("Add Expense")
//                        || gridList[i].equalsIgnoreCase("Edit Expense")) {
//                    activity.startActivity(new Intent(activity, ExpenseActivity.class));
//                } else if (gridList[i].equalsIgnoreCase("Add Invoice")
//                        || gridList[i].equalsIgnoreCase("Edit Invoice")) {
//                    activity.startActivity(new Intent(activity, InvoiceActivity.class));
//                }

                if (gridList[i].equalsIgnoreCase(activity.getString(R.string.add_expense)))
                    activity.startActivity(new Intent(activity, ExpenseActivity.class));
                else if (gridList[i].equalsIgnoreCase(activity.getString(R.string.add_invoice)))
                    activity.startActivity(new Intent(activity, InvoiceActivity.class));
                else
                    Toast.makeText(activity.getApplicationContext(), "Coming Soon!!", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return gridList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llGridItems;
        private TextView txtGridItems;

        public ViewHolder(View view) {
            super(view);

            llGridItems = (LinearLayout) view.findViewById(R.id.ll_grid_items);
            txtGridItems = (TextView) view.findViewById(R.id.txt_grid_items);
        }
    }

}
