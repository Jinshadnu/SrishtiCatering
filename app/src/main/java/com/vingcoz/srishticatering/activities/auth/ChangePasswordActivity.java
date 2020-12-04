package com.vingcoz.srishticatering.activities.auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.vingcoz.srishticatering.R;
import com.vingcoz.srishticatering.models.api.CommonResponse;
import com.vingcoz.srishticatering.models.api.ComonResponse;
import com.vingcoz.srishticatering.utils.ApiClient;
import com.vingcoz.srishticatering.utils.ApiInterface;
import com.vingcoz.srishticatering.utils.CommonUtils;
import com.vingcoz.srishticatering.utils.GlobalConstants;
import com.vingcoz.srishticatering.utils.MyHelper;
import com.vingcoz.srishticatering.utils.PrefManager;

import java.util.Calendar;

import static android.text.TextUtils.isEmpty;
import static java.util.Objects.requireNonNull;

public class ChangePasswordActivity extends AppCompatActivity {

    public String oldPassword,newPassword,confirmPassword;
    public EditText old_Password,new_Password,confirm_Password;
    public Button button_submit;
    CommonUtils mUtils;
    PrefManager mPref;
    public long UserID = 0;
    public String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        old_Password=findViewById(R.id.editTextoldPassword);
        new_Password=findViewById(R.id.editTextnewPassword);
        confirm_Password=findViewById(R.id.editTextconfirmPassword);
        button_submit=findViewById(R.id.button_submit);

        mUtils = new CommonUtils(ChangePasswordActivity.this);

        user_id=getIntent().getStringExtra(GlobalConstants.USER_ID);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Change Password");
        }


        button_submit.setOnClickListener(v -> {
            if (validatefield()){
              changePassword();
            }
        });






    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public boolean validatefield(){
        oldPassword= requireNonNull(old_Password.getText().toString());
        newPassword= requireNonNull(new_Password.getText().toString());
        confirmPassword=requireNonNull(confirm_Password.getText().toString());

        if (isEmpty(oldPassword)){
            old_Password.setError("please enter your password");
            return false;
        }

        if (isEmpty(newPassword)){
            new_Password.setError("please enter your new password");
            return false;
        }

        if (isEmpty(confirmPassword)){
            confirm_Password.setError("please confirm your password");
            return false;
        }


        if (newPassword.length() < 6){
            new_Password.setError("Password must be atleast 6 characters");
            return false;
        }

        if(!newPassword.equals(confirmPassword)){
            confirm_Password.setError("Password not matching");
            return false;
        }

        return true;
    }

    void resetFields() {

        old_Password.setText("");
        new_Password.setText("");
        confirm_Password.setText("");

    }

    public void changePassword(){
        mUtils.progress("Please wait..", true);
        if (mUtils.isNetworkAvailable()) {
            try {


                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<ComonResponse> responseCall=apiService.changePassword(user_id, oldPassword, confirmPassword);

                responseCall.enqueue(new Callback<ComonResponse>() {
                    @Override
                    public void onResponse(Call<ComonResponse> call, Response<ComonResponse> response) {
                       ComonResponse comonResponse=response.body();
                       if (comonResponse != null && comonResponse.getStatus().equals("success")){
                           mUtils.progress(" ", false);
                           mUtils.showSuccess(comonResponse.getMessage());
                           finish();
                       }
                       else {
                           mUtils.progress(" ", false);
                           mUtils.showSuccess(comonResponse.getMessage());
                       }
                    }

                    @Override
                    public void onFailure(Call<ComonResponse> call, Throwable t) {
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

}