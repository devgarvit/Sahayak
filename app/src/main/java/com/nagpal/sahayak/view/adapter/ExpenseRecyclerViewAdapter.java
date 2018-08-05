package com.nagpal.sahayak.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nagpal.sahayak.R;
import com.nagpal.sahayak.databinding.ExpenseRowItemBinding;

public class ExpenseRecyclerViewAdapter extends RecyclerView.Adapter<ExpenseRecyclerViewAdapter.ExpenseItemRowHolder> {

    private Context context;

    public ExpenseRecyclerViewAdapter(Context context) {
        this.context = context;
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
        holder.binding.tvPartyName.setText("HIIIIIIIIIIIIIIIIIII");
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class ExpenseItemRowHolder extends RecyclerView.ViewHolder {
        ExpenseRowItemBinding binding;

        ExpenseItemRowHolder(ExpenseRowItemBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }
}
