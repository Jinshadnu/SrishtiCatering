package com.vingcoz.srishticatering;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vingcoz.srishticatering.activities.auth.LoginActivity;
import com.vingcoz.srishticatering.utils.GlobalConstants;
import com.vingcoz.srishticatering.utils.PrefManager;

public class SplashScreen extends AppCompatActivity {

    PrefManager mPref;
    public ImageView imageView;
    public TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splashscreen);

        imageView=findViewById(R.id.imageView);
        textView=findViewById(R.id.txtSampleName);


        Animation animation= AnimationUtils.loadAnimation(this,R.anim.left_animation);

        imageView.startAnimation(animation);
        textView.startAnimation(animation);

        mPref = new PrefManager(SplashScreen.this);

        new Handler().postDelayed(() -> {

            if (!mPref.getBoolean(GlobalConstants.IS_LOGGED_IN)) {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashScreen.this, DashBoard.class));
                finish();
            }

        }, 3000);
    }
}
