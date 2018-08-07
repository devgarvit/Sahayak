package com.nagpal.sahayak.view.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nagpal.sahayak.R;
import com.nagpal.sahayak.service.model.Callbacks.ExpenseCategoryInfo;
import com.nagpal.sahayak.service.model.Callbacks.ExpenseDetailsInfo;
import com.nagpal.sahayak.service.model.Entities.Category;
import com.nagpal.sahayak.service.model.Entities.Expense;
import com.nagpal.sahayak.service.model.Entities.ExpenseRequest;
import com.nagpal.sahayak.service.model.Events.EventExpenseCategoryResponse;
import com.nagpal.sahayak.service.model.Events.EventExpenseDetailsResponse;
import com.nagpal.sahayak.service.repository.ApiManager;
import com.nagpal.sahayak.utils.TinyDB;
import com.nagpal.sahayak.view.adapter.CategorySpinnerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    AppCompatSpinner spinnerCategory;
    AppCompatButton btnSubmit;
    AppCompatCheckBox chkBoxCash, chkBoxBank, chkBoxCredit;
    EditText editCash, editBank, editCredit, editPartyName;
    ProgressDialog asyncDialog;
    ImageView imageOne;
    List<Category> categoryList;
    boolean checked = false;
    Bitmap photo = null;
    private static final int CAMERA_REQUEST = 900;
    private static final int GALLERY_REQUEST = 901;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_GALLERY_PERMISSION_CODE = 101;
    private Expense expense = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        if (getIntent().hasExtra("expense")) {
            expense = (Expense) getIntent().getSerializableExtra("expense");
        }
        chkBoxBank = (AppCompatCheckBox) findViewById(R.id.checkbox_bank);
        chkBoxCash = (AppCompatCheckBox) findViewById(R.id.checkbox_cash);
        chkBoxCredit = (AppCompatCheckBox) findViewById(R.id.checkbox_credit);
        editBank = (EditText) findViewById(R.id.edit_bank);
        editCash = (EditText) findViewById(R.id.edit_cash);
        editCredit = (EditText) findViewById(R.id.edit_credit);
        editPartyName = (EditText) findViewById(R.id.edit_party_name);
        spinnerCategory = (AppCompatSpinner) findViewById(R.id.spinner_category);
        btnSubmit = (AppCompatButton) findViewById(R.id.btn_expense_submit);
        imageOne = (ImageView) findViewById(R.id.image_one);
        asyncDialog = new ProgressDialog(this);

        chkBoxCash.setOnCheckedChangeListener(this);
        chkBoxBank.setOnCheckedChangeListener(this);
        chkBoxCredit.setOnCheckedChangeListener(this);
        imageOne.setOnClickListener(this);

        this.asyncDialog.setMessage("Fetching Categories...");
        this.asyncDialog.show();
        new ApiManager().getExpenseCategories(new TinyDB(getApplicationContext()).getString("access_token"), new ExpenseCategoryInfo());

    }


    @Subscribe
    public void onEvent(EventExpenseCategoryResponse event) {
        this.asyncDialog.dismiss();
        if (event != null && event.getLoginInfo() != null) {
            categoryList = event.getLoginInfo().getData().getCategories();
            setViews(event.getLoginInfo().getData().getCategories());
            if (expense != null) {
                setExpenseData();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong. Please reach us on sahayak1010@gmail.com", Toast.LENGTH_LONG).show();
        }
    }

    private void setExpenseData() {
        if (!TextUtils.isEmpty(expense.getPartyName())) {
            editPartyName.setText(expense.getPartyName());
        }
        if (expense.getPaymentType().equalsIgnoreCase("credit")) {
            chkBoxCredit.setChecked(true);
            editCredit.setVisibility(View.VISIBLE);
            editCredit.setText(expense.getAmount());
        } else if (expense.getPaymentType().equalsIgnoreCase("cash")) {
            chkBoxCash.setChecked(true);
            editCash.setVisibility(View.VISIBLE);
            editCash.setText(expense.getAmount());
        } else if (expense.getPaymentType().equalsIgnoreCase("bank")) {
            chkBoxBank.setChecked(true);
            editBank.setVisibility(View.VISIBLE);
            editBank.setText(expense.getAmount());
        }
        if (!TextUtils.isEmpty(expense.getImageUrl())) {
            Glide.with(this).load(expense.getImageUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.icon_upload_file))
                    .into(imageOne);
        }
        if (!TextUtils.isEmpty(expense.getCategory())) {
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getName().equalsIgnoreCase(expense.getCategory())) {
                    spinnerCategory.setSelection(i);
                }
            }
        }
    }

    private void setViews(List<Category> list) {
        Category category = new Category();
        category.setName("Select Category");
        list.add(category);

        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(getApplicationContext(), list);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setSelection(adapter.getCount());
        btnSubmit.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_expense_submit:
                getViews();
                break;

            case R.id.image_one:
                showPictureDialog();
                break;
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Add Photo!!");
        String[] pictureDialogItems = {
                "Choose photo from gallery",
                "Capture photo"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                captureImage();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ExpenseActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, MY_GALLERY_PERMISSION_CODE);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,

                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST);
        }
    }


    private void getViews() {
//        if (photo == null) {
//            Toast.makeText(getApplicationContext(), "Please attach photo!!", Toast.LENGTH_LONG).show();
//            return;
//        }

        String amount = "0";
        ExpenseRequest expenseRequest = new ExpenseRequest();

        if (chkBoxCredit.isChecked()) {
            expenseRequest.setPaymentType("credit");
            amount = editCredit.getText().toString();
            if(editPartyName.length() <= 0){
                Toast.makeText(this,"Enter Party Name",Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (chkBoxCash.isChecked()) {
            expenseRequest.setPaymentType("cash");
            amount = editCash.getText().toString();
        } else if (chkBoxBank.isChecked()) {
            expenseRequest.setPaymentType("bank");
            amount = editBank.getText().toString();
            if(editPartyName.length() <= 0){
                Toast.makeText(this,"Enter Party Name",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (TextUtils.isEmpty(amount) || amount.equalsIgnoreCase("0")) {
            Toast.makeText(getApplicationContext(), "Amount can't be empty!!", Toast.LENGTH_LONG).show();
            return;
        }

        expenseRequest.setAmount(Double.parseDouble(amount));
        expenseRequest.setCategoryId(categoryList.get(spinnerCategory.getSelectedItemPosition()).getId());
        if (photo != null)
            expenseRequest.setImageUrl("data:image/gif;base64," + toBase64(photo));
        expenseRequest.setPartyName(editPartyName.getText().toString());
        if (expense != null && expense.getId() > 0)
            expenseRequest.setParentId(expense.getId());
//        expenseRequest.setUserId(1);

        new ApiManager().setExpenseDetails(new TinyDB(getApplicationContext()).getString("access_token"), expenseRequest, new ExpenseDetailsInfo());
        this.asyncDialog.setMessage("Saving Data...");
        this.asyncDialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        switch (v.getId()) {
            case R.id.checkbox_bank:
                if (!checked) {
                    checked = true;
                    if (isChecked) {
                        chkBoxBank.setChecked(isChecked);
                        chkBoxCash.setChecked(!isChecked);
                        chkBoxCredit.setChecked(!isChecked);
                        editBank.setVisibility(View.VISIBLE);
                        editCash.setVisibility(View.GONE);
                        editCredit.setVisibility(View.GONE);
                    } else {
                        chkBoxBank.setChecked(isChecked);
                        editBank.setVisibility(View.GONE);
                    }
                    checked = false;
                }
                break;

            case R.id.checkbox_cash:
                if (!checked) {
                    checked = true;
                    if (isChecked) {
                        chkBoxCash.setChecked(isChecked);
                        chkBoxBank.setChecked(!isChecked);
                        chkBoxCredit.setChecked(!isChecked);
                        editBank.setVisibility(View.GONE);
                        editCash.setVisibility(View.VISIBLE);
                        editCredit.setVisibility(View.GONE);
                    } else {
                        chkBoxCash.setChecked(isChecked);
                        editCash.setVisibility(View.GONE);
                    }
                    checked = false;

                }
                break;

            case R.id.checkbox_credit:
                if (!checked) {
                    checked = true;
                    if (isChecked) {
                        chkBoxCredit.setChecked(isChecked);
                        chkBoxCash.setChecked(!isChecked);
                        chkBoxBank.setChecked(!isChecked);
                        editBank.setVisibility(View.GONE);
                        editCash.setVisibility(View.GONE);
                        editCredit.setVisibility(View.VISIBLE);
                    } else {
                        chkBoxCredit.setChecked(isChecked);
                        editCredit.setVisibility(View.GONE);
                    }
                    checked = false;
                }
                break;

        }
    }

    private void captureImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ExpenseActivity.this, new String[]{
                    Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                ActivityCompat.requestPermissions(ExpenseActivity.this, new String[]{
                        Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            }

        } else if (requestCode == MY_GALLERY_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            } else {
                ActivityCompat.requestPermissions(ExpenseActivity.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, MY_GALLERY_PERMISSION_CODE);
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            imageOne.setImageBitmap(photo);
        } else if (requestCode == GALLERY_REQUEST) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    imageOne.setImageBitmap(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oops!! Something went wrong..", Toast.LENGTH_SHORT).show();
                }
            }

        }


    }


    @Subscribe
    public void onEvent(EventExpenseDetailsResponse event) {
        this.asyncDialog.dismiss();
        if (event != null && event.getInfo() != null) {
            Toast.makeText(getApplicationContext(), "Saved Successfully!!", Toast.LENGTH_LONG).show();
            resetForm();
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong. Please reach us on sahayak1010@gmail.com", Toast.LENGTH_LONG).show();
        }
    }

    private void resetForm() {
        chkBoxCredit.setChecked(false);
        chkBoxBank.setChecked(false);
        chkBoxCash.setChecked(false);
        editCredit.setText("");
        editBank.setText("");
        editCash.setText("");
        editPartyName.setText("");
        imageOne.setImageResource(R.drawable.icon_upload_file);
    }

    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
