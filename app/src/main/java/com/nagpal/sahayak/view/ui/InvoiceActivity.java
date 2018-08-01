package com.nagpal.sahayak.view.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nagpal.sahayak.R;
import com.nagpal.sahayak.databinding.ActivityInvoiceBinding;

public class InvoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityInvoiceBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_invoice);

        binding.textViewName.setText("DD Enterprises");
        binding.textViewAddress.setText("983, Karol Bagh, New  Delhi Road, near Metro Station- 110023");

    }
}
