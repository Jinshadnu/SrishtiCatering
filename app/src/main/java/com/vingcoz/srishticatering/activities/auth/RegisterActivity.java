package com.vingcoz.srishticatering.activities.auth;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.vingcoz.srishticatering.R;
import com.vingcoz.srishticatering.models.api.CommonResponse;
import com.vingcoz.srishticatering.utils.ApiClient;
import com.vingcoz.srishticatering.utils.ApiInterface;
import com.vingcoz.srishticatering.utils.CommonUtils;
import com.vingcoz.srishticatering.utils.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class RegisterActivity extends AppCompatActivity {

    CommonUtils mUtils;
    PrefManager mPref;
    String strName, strPass, strPhone,strEmail,strPass2;
    EditText txtPhone, txtPassword2, txtName, txtPassword1,textEmail;
    TextView txtNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);

        mUtils = new CommonUtils(RegisterActivity.this);
        mPref = new PrefManager(RegisterActivity.this);

        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtPassword1 = findViewById(R.id.txtPassword1);
        txtPassword2 = findViewById(R.id.txtPassword2);
        txtNewAccount = findViewById(R.id.lnkSignIn);
        textEmail=findViewById(R.id.txtEmail);

        Button btnLogin = findViewById(R.id.btnSignUp);
        btnLogin.setOnClickListener(v -> {

            if (Validate()) {
                getLogin();
            }
        });

        txtNewAccount.setOnClickListener(v -> {
            finish();
        });

    }

    boolean Validate() {
        strName = txtName.getText().toString().trim();
        strPass = txtPassword1.getText().toString().trim();
        strPhone = txtPhone.getText().toString().trim();
        strEmail=textEmail.getText().toString().trim();
        strPass2=txtPassword2.getText().toString().trim();

        if (isEmpty(strName)){
         txtName.setError("Please enter your name");
         return false;
        }

        if (isEmpty(strPhone)){
            txtPhone.setError("Please enter your phone number");
            return false;
        }

        if(strPhone.length() < 10){
            txtPhone.setError("Invalid phone number");
            return false;
        }

        if (isEmpty(strPass)){
            txtPassword1.setError("Please enter your password");
            return false;
        }


        if (isEmpty(strPass2)){
            txtPassword2.setError("Please confirm your password");
            return false;
        }
        if (isEmpty(strEmail)){
            textEmail.setError("Please enter your email");
            return false;
        }

        if (strPass.length() < 6){
            txtPassword1.setError("Password Must be atleast 6 characters");
            return false;
        }


        if(!strPass.equals(strPass2)){
            txtPassword2.setError("Password not matching");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches() && strEmail.length() < 5){
            textEmail.setError("Invalid email address");
            return false;
        }





        return true;
    }

    void getLogin() {

        mUtils.progress("Registering user..Please wait..", true);
        if (mUtils.isNetworkAvailable()) {
            try {

                Call<CommonResponse> call;
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                call = apiService.getRegister(strName, strPhone, strPass,strEmail);

                call.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (response.raw().isSuccessful()) {
                            assert response.body() != null;
                            if (!response.body().isError()) {
                                mUtils.showSuccess("User registered successfully registered, please login to continue ");
                                finish();
                            } else {
                                mUtils.progress(" ", false);
                                new AlertDialog.Builder(RegisterActivity.this)
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
}