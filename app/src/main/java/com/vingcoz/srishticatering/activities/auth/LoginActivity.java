package com.vingcoz.srishticatering.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.vingcoz.srishticatering.DashBoard;
import com.vingcoz.srishticatering.R;
import com.vingcoz.srishticatering.models.api.login.LoginResponse;
import com.vingcoz.srishticatering.utils.ApiClient;
import com.vingcoz.srishticatering.utils.ApiInterface;
import com.vingcoz.srishticatering.utils.CommonUtils;
import com.vingcoz.srishticatering.utils.GlobalConstants;
import com.vingcoz.srishticatering.utils.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    CommonUtils mUtils;
    PrefManager mPref;
    String strName, strPass;
    EditText txtUserName, txtPassword;
    TextView txtNewAccount,textView_Forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mUtils = new CommonUtils(LoginActivity.this);
        mPref = new PrefManager(LoginActivity.this);

        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        txtNewAccount = findViewById(R.id.textView_signup);
        textView_Forgot=findViewById(R.id.txtForgot);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {

            if (Validate()) {
                getLogin();

         /*       mPref.putBoolean(GlobalConstants.IS_LOGGED_IN, true);
                mPref.putLong(GlobalConstants.USER_ID, 1);
                mPref.putString(GlobalConstants.USER_NAME, "test");
                mPref.putString(GlobalConstants.USER_MOBILE, "0000000000");
                mPref.putString(GlobalConstants.USER_PASSWORD, "85258");

                mUtils.showSuccess("Hai " + "test");
                startActivity(new Intent(LoginActivity.this, DashBoard.class));
                finish();*/

            }
        });

        txtNewAccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        textView_Forgot.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
        });

    }

    boolean Validate() {
        strName = txtUserName.getText().toString().trim();
        strPass = txtPassword.getText().toString().trim();
        return true;
    }

    void getLogin() {

        mUtils.progress("Validating user..Please wait..", true);
        if (mUtils.isNetworkAvailable()) {
            try {

                Call<LoginResponse> call;
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                call = apiService.getLogin(strName, strPass);

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.raw().isSuccessful()) {
                            assert response.body() != null;
                            if (!response.body().isError()) {

                                mPref.putBoolean(GlobalConstants.IS_LOGGED_IN, true);
                                mPref.putLong(GlobalConstants.USER_ID, response.body().getLogindata().get(0).getID());
                                mPref.putString(GlobalConstants.USER_NAME, response.body().getLogindata().get(0).getName());
                                mPref.putString(GlobalConstants.USER_MOBILE, response.body().getLogindata().get(0).getMobileNo());
                                mPref.putString(GlobalConstants.USER_PASSWORD, strPass);

                                mUtils.showSuccess("Hai " + response.body().getLogindata().get(0).getName());
                                startActivity(new Intent(LoginActivity.this, DashBoard.class));
                                finish();

                            } else {
                                mUtils.progress(" ", false);
                                new AlertDialog.Builder(LoginActivity.this)
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
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
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