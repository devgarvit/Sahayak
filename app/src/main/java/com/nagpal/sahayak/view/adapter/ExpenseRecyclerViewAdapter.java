package com.nagpal.sahayak.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nagpal.sahayak.R;
import com.nagpal.sahayak.databinding.ExpenseRowItemBinding;
import com.nagpal.sahayak.service.model.Entities.Expense;
import com.nagpal.sahayak.view.ui.ExpenseActivity;

import java.util.List;

public class ExpenseRecyclerViewAdapter extends RecyclerView.Adapter<ExpenseRecyclerViewAdapter.ExpenseItemRowHolder> {

    private Context context;
    private List<Expense> expenseList;

    public ExpenseRecyclerViewAdapter(Context context, List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ExpenseRowItemBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.expense_row_item,
                        parent, false);
        return new ExpenseItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseItemRowHolder holder, int position) {
        final Expense expense = expenseList.get(position);
        holder.binding.tvPartyName.setText(expense.getPartyName());
//        holder.binding.tvPaymentType.setText(context.getString(R.string.payment_type) + ": " +expense.getPaymentType());
//        holder.binding.tvAmount.setText(context.getString(R.string.rupee_sign) + " " + expense.getAmount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ExpenseActivity.class);
                intent.putExtra("expense",expense);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList != null ? expenseList.size() : 0;
    }

    class ExpenseItemRowHolder extends RecyclerView.ViewHolder {
        ExpenseRowItemBinding binding;

        ExpenseItemRowHolder(ExpenseRowItemBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }
}
