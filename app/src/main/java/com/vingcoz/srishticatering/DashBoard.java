package com.vingcoz.srishticatering;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.vingcoz.srishticatering.activities.AboutActivity;
import com.vingcoz.srishticatering.activities.Items;
import com.vingcoz.srishticatering.activities.adapter.ItemAdapter;
import com.vingcoz.srishticatering.activities.auth.ChangePasswordActivity;
import com.vingcoz.srishticatering.activities.auth.LoginActivity;
import com.vingcoz.srishticatering.models.api.CommonResponse;
import com.vingcoz.srishticatering.models.api.categ.CategoryDataItem;
import com.vingcoz.srishticatering.models.api.categ.CategoryResponse;
import com.vingcoz.srishticatering.utils.ApiClient;
import com.vingcoz.srishticatering.utils.ApiInterface;
import com.vingcoz.srishticatering.utils.CommonUtils;
import com.vingcoz.srishticatering.utils.GlobalConstants;
import com.vingcoz.srishticatering.utils.MyHelper;
import com.vingcoz.srishticatering.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ArrayAdapter<String> SpinnerAdapterCards;
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    Calendar mCalendarDt = Calendar.getInstance();
    int mYear1, mMonth1, mDay1;
    int mHour, mMinute;
    EditText txtName, txtPhone, txtDishNames, txtSecondPhone, txtPlace, txtAddress,txtQuantity;
    TextView txtDate, txtTime;
    int[] arrCardId;
    int ProductID;
    CommonUtils mUtils;
    PrefManager mPref;
    Spinner spCategories;
    long UserID = 0;
    String strDbDateFormat = "";
    String strDateTime;
    String strName, strPhone, strDate, strTime, strDishNames, strSecondPhone, strPlace, strAddress,strQuantity;
    public RecyclerView recyclerView_items;
    public String user_id;
    public ItemAdapter itemAdapter;
    public ArrayList<Items> modelData;
    public int position;
    public String item_name,quantity;
    public String itemsList;
    public ArrayList<String> stringList;
    public StringBuffer stringBuffer;
    public String itemString;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        mUtils = new CommonUtils(DashBoard.this);
        mPref = new PrefManager(DashBoard.this);

        UserID = mPref.getLong(GlobalConstants.USER_ID);
        user_id=String.valueOf(UserID);

        modelData=new ArrayList<Items>();
        stringList=new ArrayList<>();

        if (UserID == 0) {
            mPref.putBoolean(GlobalConstants.IS_LOGGED_IN, false);
            mPref.putLong(GlobalConstants.USER_ID, 0);
            mPref.putString(GlobalConstants.USER_NAME, "");
            mPref.putString(GlobalConstants.USER_MOBILE, "");
            mPref.putString(GlobalConstants.USER_PASSWORD, "");
            mUtils.showInfo("Logout Success please login to continue..");
            startActivity(new Intent(DashBoard.this, LoginActivity.class));
            finish();
        }

        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtDishNames = findViewById(R.id.txtDishNames);
        txtSecondPhone = findViewById(R.id.txtSecondPhone);
        txtPlace = findViewById(R.id.txtPlace);
        txtAddress = findViewById(R.id.txtAddress);
        recyclerView_items=(RecyclerView)findViewById(R.id.recyclerItems);
        txtQuantity=findViewById(R.id.txtQauntity);

        spCategories = findViewById(R.id.spCategories);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        TextView navUser = hView.findViewById(R.id.txtUserName);
        navUser.setText(mPref.getString(GlobalConstants.USER_NAME));

        getCategories();

        txtDate.setText(MyHelper.getCurrentDateForView());
        txtDate.setOnClickListener(v -> ShowInvoiceDate());

        txtTime.setText(MyHelper.getCurrentTimeForView());
        txtTime.setOnClickListener(v -> ShowTime());


        recyclerView_items.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        recyclerView_items.setHasFixedSize(true);
        recyclerView_items.addItemDecoration(dividerItemDecoration);

        Button btnAdd = findViewById(R.id.button_add);
        btnAdd.setOnClickListener(v -> {
          item_name=txtDishNames.getText().toString();
          quantity=txtQuantity.getText().toString();

          if (validateItems()){

              String items=item_name + " : " + quantity;

              insertMethod(items);
              txtDishNames.setText("");
              txtQuantity.setText("");
          }

          stringList.clear();
          for (int i=0;i<itemAdapter.itemsList.size();i++){
              stringList.add(itemAdapter.itemsList.get(i).getItem_name());
          }

          System.out.println(stringList);
          stringBuffer=new StringBuffer();



          for (String stringItems: stringList){
              stringBuffer.append(stringItems);
              stringBuffer.append(",");

          }

          itemString=stringBuffer.toString();
          System.out.println(itemString);








        });



        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {
            if (Validate()) {
                getSubmit();
            }
        });

        strDbDateFormat = MyHelper.getDateForDatabase(mCalendarDt);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            startActivity(new Intent(DashBoard.this, AboutActivity.class));
        }
        else if(id == R.id.nav_changePassword){
            Intent intent=new Intent(DashBoard.this,ChangePasswordActivity.class);
            intent.putExtra(GlobalConstants.USER_ID,user_id);
            startActivity(intent);
        }
        else if (id == R.id.nav_exit) {
            finish();
        } else if (id == R.id.nav_logout) {

            mPref.putBoolean(GlobalConstants.IS_LOGGED_IN, false);
            mPref.putLong(GlobalConstants.USER_ID, 0);
            mPref.putString(GlobalConstants.USER_NAME, "");
            mPref.putString(GlobalConstants.USER_MOBILE, "");
            mPref.putString(GlobalConstants.USER_PASSWORD, "");
            mUtils.showInfo("Logout Success please login to continue..");
            startActivity(new Intent(DashBoard.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    void ShowInvoiceDate() {

        mYear1 = mCalendarDt.get(Calendar.YEAR);
        mMonth1 = mCalendarDt.get(Calendar.MONTH);
        mDay1 = mCalendarDt.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    mCalendarDt.set(year, monthOfYear, dayOfMonth);

                    txtDate.setText(MyHelper.getDateForViewCalendar(mCalendarDt));
                    strDbDateFormat = MyHelper.getDateForDatabase(mCalendarDt);
                }, mYear1, mMonth1, mDay1);
        datePickerDialog.show();
    }

    void ShowTime() {

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {

                    mCalendarDt.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mCalendarDt.set(Calendar.MINUTE, minute);
                    mCalendarDt.set(Calendar.SECOND, 0);

                    txtTime.setText(MyHelper.getCalendarToTime(mCalendarDt));
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    boolean Validate() {

        strName = txtName.getText().toString().trim();
        strPhone = txtPhone.getText().toString().trim();
        //strDishNames = txtDishNames.getText().toString().trim();
        strSecondPhone = txtSecondPhone.getText().toString().trim();
        strPlace = txtPlace.getText().toString().trim();
        strAddress = txtAddress.getText().toString().trim();
//        strQuantity= txtQuantity.getText().toString().trim();



        if (strName.length() < 2) {
            mUtils.showError("Invalid name");
            return false;
        }

//        if (strQuantity.length() < 1){
//            mUtils.showError("Invalid Quantity");
//            return false;
//        }

        if (strPhone.length() != 10) {
            mUtils.showError("Invalid phone - 10 digit needed");
            return false;
        }
//        if (strDishNames.length() < 2) {
//            mUtils.showError("Invalid dish");
//            return false;
//        }
        if (strSecondPhone.length() < 1) {
            strSecondPhone = "";
        }
        if (strPlace.length() < 1) {
            mUtils.showError("Invalid place");
            return false;
        }
        if (strAddress.length() < 1) {
            mUtils.showError("Invalid address");
            return false;
        }

        if (SpinnerAdapterCards.getCount() > 0) {
            int intArrPosition = spCategories.getSelectedItemPosition();
            ProductID = arrCardId[intArrPosition];
        } else {
            mUtils.showError("No categories found");
            return false;
        }

        if (modelData.isEmpty()){
            mUtils.showError("Please Enter Items");
            return false;
        }

        return true;
    }

    public boolean validateItems(){
        item_name=txtDishNames.getText().toString().trim();
        quantity=txtQuantity.getText().toString().trim();
        if (item_name.length() < 2) {
            mUtils.showError("Invalid dish");
            return false;
        }

        if (quantity.length() < 1){
            mUtils.showError("Please Enter Quantity");
            return false;
        }

        return true;
    }

    void ResetFields() {

        txtName.setText("");
        txtPhone.setText("");
        txtDate.setText(MyHelper.getCurrentDateForView());
        txtTime.setText(MyHelper.getCurrentTimeForView());
        txtDishNames.setText("");
        txtSecondPhone.setText("");
        txtPlace.setText("");
        txtAddress.setText("");
        //txtQuantity.setText("");
        mCalendarDt = Calendar.getInstance();
    }

    void getSubmit() {

        mUtils.progress("Submitting order..Please wait..", true);
        if (mUtils.isNetworkAvailable()) {
            try {
                Call<CommonResponse> call;
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                strName = txtName.getText().toString().trim();
                strPhone = txtPhone.getText().toString().trim();
                strDate = txtDate.getText().toString().trim();
                strTime = txtTime.getText().toString().trim();
                strDishNames = txtDishNames.getText().toString().trim();
                strSecondPhone = txtSecondPhone.getText().toString().trim();
                strPlace = txtPlace.getText().toString().trim();
                strDateTime = MyHelper.getCalendarToDateTime(mCalendarDt);
                strAddress = txtAddress.getText().toString().trim();
                //strQuantity=txtQuantity.getText().toString().trim();

                call = apiService.getOrderInsert(
                        UserID,
                        strName,
                        strPhone,
                        ProductID,
                        strDbDateFormat,
                        strTime,
                        itemString,
                        strSecondPhone,
                        strPlace, strDateTime, strAddress);

                call.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (response.raw().isSuccessful()) {
                            assert response.body() != null;
                            if (!response.body().isError()) {
                                mUtils.progress(" ", false);
                                mUtils.showSuccess("Order successfully submitted");
                                ResetFields();
                            } else {
                                mUtils.progress(" ", false);
                                new AlertDialog.Builder(DashBoard.this)
                                        .setTitle("Error")
                                        .setMessage(response.body().getMessage())
                                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                                            dialogInterface.dismiss();
                                        })
                                        .show();
                            }
                        } else {
                            mUtils.showError(response.raw().message());
                            mUtils.progress(" ", false);
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        mUtils.progress(" ", false);
                        mUtils.showError(t.getMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                mUtils.showError(e.getMessage());
                mUtils.progress(" ", false);
            }
        } else {
            mUtils.showError("No network connection");
        }
    }

    void getCategories() {

        mUtils.progress("Fetching categories..Please wait..", true);
        if (mUtils.isNetworkAvailable()) {
            try {
                Call<CategoryResponse> call;
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                call = apiService.getCategories();

                call.enqueue(new Callback<CategoryResponse>() {
                    @Override
                    public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                        if (response.raw().isSuccessful()) {
                            assert response.body() != null;
                            if (!response.body().isError()) {

                                if (response.body().getCategoryData().size() != 0) {
                                    int ListSize = response.body().getCategoryData().size();
                                    String[] items = new String[ListSize];
                                    arrCardId = new int[ListSize];
                                    for (int i = 0; i < ListSize; i++) {
                                        CategoryDataItem rowCards = response.body().getCategoryData().get(i);
                                        arrCardId[i] = (int) rowCards.getId();
                                        items[i] = String.valueOf(rowCards.getName());
                                    }
                                    SpinnerAdapterCards = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item_textview, items);
                                    spCategories.setAdapter(SpinnerAdapterCards);
                                    mUtils.progress(" ", false);
                                } else {
                                    mUtils.showError("No categories found in server,Please add it from admin app");
                                }

                            } else {
                                mUtils.progress(" ", false);
                                new AlertDialog.Builder(DashBoard.this)
                                        .setTitle("Error")
                                        .setMessage(response.body().getMessage())
                                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                                            dialogInterface.dismiss();
                                        })
                                        .show();
                            }
                        } else {
                            mUtils.showError(response.raw().message());
                            mUtils.progress(" ", false);
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryResponse> call, Throwable t) {
                        mUtils.progress(" ", false);
                        mUtils.showError(t.getMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                mUtils.showError(e.getMessage());
                mUtils.progress(" ", false);
            }
        } else {
            mUtils.showError("No network connection");
        }
    }
    private void insertMethod(String name) {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            Items model = gson.fromJson(String.valueOf(jsonObject), Items.class);
            modelData.add(new Items(name));

            itemAdapter=new ItemAdapter(this, modelData, new ItemAdapter.Onclick() {
                @Override
                public void onEvent(Items model, int pos) {
                    position = pos;
                }
            });
            recyclerView_items.setAdapter(itemAdapter);
            itemAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
