package com.nagpal.sahayak.view.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.nagpal.sahayak.R;
import com.nagpal.sahayak.databinding.ActivityExpenseListBinding;
import com.nagpal.sahayak.service.model.Callbacks.ExpenseCategoryInfo;
import com.nagpal.sahayak.service.model.Callbacks.ExpenseListInfo;
import com.nagpal.sahayak.service.model.Entities.Category;
import com.nagpal.sahayak.service.model.Entities.Expense;
import com.nagpal.sahayak.service.model.Entities.ExpenseList;
import com.nagpal.sahayak.service.model.Events.EventExpenseCategoryResponse;
import com.nagpal.sahayak.service.model.Events.EventExpenseListResponse;
import com.nagpal.sahayak.service.repository.ApiManager;
import com.nagpal.sahayak.utils.TinyDB;
import com.nagpal.sahayak.view.adapter.ExpenseRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExpenseListActivity extends AppCompatActivity {

    private ActivityExpenseListBinding binding;
    private ExpenseRecyclerViewAdapter adapter;
    private Context context;
    private ProgressDialog asyncDialog;
    private List<Expense> expensesList;

    private Calendar calendar;
    private int year, month, day;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expense_list);
        context = this;
        asyncDialog = new ProgressDialog(this);

        binding.llNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expensesList != null && expensesList.size() > 0) {
                    adapter = new ExpenseRecyclerViewAdapter(context, expensesList);
                    binding.recyclerView.setAdapter(adapter);

                    binding.llNoData.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
        initRecyclerView();

        new ApiManager().getExpenseCategories(new TinyDB(getApplicationContext()).getString("access_token"), new ExpenseCategoryInfo());

        this.asyncDialog.setMessage("Getting Data...");
        this.asyncDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ApiManager().getExpenseDetails(new TinyDB(getApplicationContext()).getString("access_token"), new ExpenseListInfo());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_expense_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.show_all:
                if (expensesList != null && expensesList.size() > 0) {
                    binding.llNoData.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);

                    adapter = new ExpenseRecyclerViewAdapter(context, expensesList);
                    binding.recyclerView.setAdapter(adapter);
                } else {
                    binding.llNoData.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
                }
                break;
            case R.id.by_amount:
                showCustomDialog(1);
                break;
            case R.id.by_category:
                if (categoryList != null && categoryList.size() > 0) {
                    showCategoryDialog();
                }
                break;
            case R.id.by_date:
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                showDialog(999);
                break;
            case R.id.by_name:
                showCustomDialog(0);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);
        binding.recyclerView.addItemDecoration(itemDecoration);
    }

    @Subscribe
    public void onEvent(EventExpenseCategoryResponse event) {
        this.asyncDialog.dismiss();
        if (event != null && event.getLoginInfo() != null && event.getLoginInfo().getData() != null
                && event.getLoginInfo().getData().getCategories() != null) {
            categoryList = event.getLoginInfo().getData().getCategories();
        }
    }

    @Subscribe
    public void onEvent(EventExpenseListResponse event) {
        this.asyncDialog.dismiss();
        if (event != null && event.getInfo() != null) {
            ExpenseList expenseList = event.getInfo();
            if (expenseList != null && expenseList.getData() != null && expenseList.getData().getExpense() != null
                    && expenseList.getData().getExpense().size() > 0) {
                expensesList = expenseList.getData().getExpense();
                adapter = new ExpenseRecyclerViewAdapter(context, expensesList);
                binding.recyclerView.setAdapter(adapter);

                binding.llNoData.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);

            } else {
                Toast.makeText(getApplicationContext(), "No expenses available", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong. Please reach us on sahayak1010@gmail.com", Toast.LENGTH_LONG).show();
        }
    }

    public void showCustomDialog(final int searchType) {//0- by name, 1- by amount
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.edittext_dialog_layout);

        Button btnSearch = dialog.findViewById(R.id.btn_search);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        final EditText editText = dialog.findViewById(R.id.edit_text);

        if (searchType == 0) {
            editText.setHint("Enter Name to search");
        } else {
            editText.setHint("Enter Amount to search");
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.length() > 0) {
                    String filter = editText.getText().toString();
                    List<Expense> filteredExpenses = new ArrayList<>();

                    Iterator<Expense> iterator = expensesList.iterator();
                    while (iterator.hasNext()) {
                        Expense expense = iterator.next();
                        if (searchType == 0) {
                            if (expense.getPartyName() != null) {
                                if (expense.getPartyName().toLowerCase().contains(filter.toLowerCase())) {
                                    filteredExpenses.add(expense);
                                }
                            }
                        } else if (searchType == 1) {
                            if (expense.getAmount() != null) {
                                if (expense.getAmount().equalsIgnoreCase(filter)) {
                                    filteredExpenses.add(expense);
                                }
                            }
                        }
                    }

                    if (filteredExpenses != null && filteredExpenses.size() > 0) {
                        binding.llNoData.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);

                        adapter = new ExpenseRecyclerViewAdapter(context, filteredExpenses);
                        binding.recyclerView.setAdapter(adapter);
                    } else {
                        binding.llNoData.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(context, "Please enter data to search", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    // Create a Date variable/object with user chosen date
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(0);
                    cal.set(arg1, arg2, arg3, 0, 0, 0);
                    Date chosenDate = cal.getTime();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    showDate(format.format(chosenDate));
//                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(String selectedDate) {
        List<Expense> filteredExpenses = new ArrayList<>();

        Iterator<Expense> iterator = expensesList.iterator();
        while (iterator.hasNext()) {
            Expense expense = iterator.next();

            //2018-04-08T15:45:34.263+05:30
            String date = expense.getCreatedAt().substring(0, expense.getCreatedAt().indexOf("T"));

            if (date.equalsIgnoreCase(selectedDate)) {
                filteredExpenses.add(expense);
            }
        }

        if (filteredExpenses != null && filteredExpenses.size() > 0) {
            binding.llNoData.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);

            adapter = new ExpenseRecyclerViewAdapter(context, filteredExpenses);
            binding.recyclerView.setAdapter(adapter);
        } else {
            binding.llNoData.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        }

    }

    private void showCategoryDialog() {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_for_category);
        final List<String> list = new ArrayList<>();
        for (Category category : categoryList) {
            list.add(category.getName());
        }
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, R.layout.single_textview_layout, list);

        ListView listView = dialog.findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                Category selectedCategory = categoryList.get(i);
                List<Expense> filteredExpenses = new ArrayList<>();

                Iterator<Expense> iterator = expensesList.iterator();
                while (iterator.hasNext()) {
                    Expense expense = iterator.next();

                    if (selectedCategory.getId() == expense.getCategoryId()) {
                        filteredExpenses.add(expense);
                    }
                }

                if (filteredExpenses != null && filteredExpenses.size() > 0) {
                    binding.llNoData.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);

                    adapter = new ExpenseRecyclerViewAdapter(context, filteredExpenses);
                    binding.recyclerView.setAdapter(adapter);
                } else {
                    binding.llNoData.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
                }
            }
        });
        dialog.show();
    }
}
