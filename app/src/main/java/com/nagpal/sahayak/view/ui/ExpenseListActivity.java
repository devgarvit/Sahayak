package com.nagpal.sahayak.view.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.nagpal.sahayak.R;
import com.nagpal.sahayak.databinding.ActivityExpenseListBinding;
import com.nagpal.sahayak.service.model.Callbacks.ExpenseDetailsInfo;
import com.nagpal.sahayak.service.model.Callbacks.ExpenseListInfo;
import com.nagpal.sahayak.service.model.Entities.Expense;
import com.nagpal.sahayak.service.model.Entities.ExpenseList;
import com.nagpal.sahayak.service.model.Entities.ExpenseRequest;
import com.nagpal.sahayak.service.model.Events.EventExpenseDetailsResponse;
import com.nagpal.sahayak.service.model.Events.EventExpenseListResponse;
import com.nagpal.sahayak.service.repository.ApiManager;
import com.nagpal.sahayak.utils.TinyDB;
import com.nagpal.sahayak.view.adapter.ExpenseRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class ExpenseListActivity extends AppCompatActivity {

    private ActivityExpenseListBinding binding;
    private ExpenseRecyclerViewAdapter adapter;
    private Context context;
    private ProgressDialog asyncDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expense_list);
        context = this;
        asyncDialog = new ProgressDialog(this);

        initRecyclerView();

        new ApiManager().getExpenseDetails(new TinyDB(getApplicationContext()).getString("access_token"), new ExpenseListInfo());
        this.asyncDialog.setMessage("Getting Data...");
        this.asyncDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);
        binding.recyclerView.addItemDecoration(itemDecoration);
    }

    @Subscribe
    public void onEvent(EventExpenseListResponse event) {
        this.asyncDialog.dismiss();
        if (event != null && event.getInfo() != null) {
            ExpenseList expenseList = event.getInfo();
            if (expenseList != null && expenseList.getData() != null && expenseList.getData().getExpense() != null
                    && expenseList.getData().getExpense().size() > 0) {
                adapter = new ExpenseRecyclerViewAdapter(context, expenseList.getData().getExpense());
                binding.recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "No expenses available", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong. Please reach us on sahayak1010@gmail.com", Toast.LENGTH_LONG).show();
        }
    }
}
