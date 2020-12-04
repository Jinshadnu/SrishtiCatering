package com.vingcoz.srishticatering.activities.auth;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.vingcoz.srishticatering.R;
import com.vingcoz.srishticatering.models.api.ComonResponse;
import com.vingcoz.srishticatering.utils.ApiClient;
import com.vingcoz.srishticatering.utils.ApiInterface;
import com.vingcoz.srishticatering.utils.CommonUtils;

import static android.text.TextUtils.isEmpty;
import static java.util.Objects.requireNonNull;

public class ForgotActivity extends AppCompatActivity {
    public EditText editText_email;
    public String email;
    public Button button_submit;
    CommonUtils mUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        editText_email=findViewById(R.id.editTextEmail);
        button_submit=findViewById(R.id.button_submit);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Forgot Password");
        }

        mUtils = new CommonUtils(ForgotActivity.this);

        button_submit.setOnClickListener(v -> {
            if (validatefield()){
             forgotPassword();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean validatefield(){
        email=requireNonNull(editText_email.getText().toString().trim());

        if (isEmpty(email)){
            editText_email.setError("please enter your email");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.length() < 5){
            editText_email.setError("Invalid email address");
            return false;
        }
        return true;
    }

    public void forgotPassword(){
        mUtils.progress("Please wait..", true);
        if (mUtils.isNetworkAvailable()) {
            try {


                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<ComonResponse> responseCall=apiService.forgotPassword(email);

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