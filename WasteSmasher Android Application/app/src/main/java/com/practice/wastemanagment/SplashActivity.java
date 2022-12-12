package com.practice.wastemanagment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.transition.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

import java.util.logging.LogRecord;

public class SplashActivity extends AppCompatActivity {

    ImageView logo;
    TextView typingt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.splashlogo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());
        logo.startAnimation(rotate);

        typingt = findViewById(R.id.tittle);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                typingt.append(" ");
            }
        }, 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                typingt.append("E");
            }
        }, 400);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                typingt.append("A");
            }
        }, 500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                typingt.append("R");
            }
        }, 500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                typingt.append("T");
            }
        }, 500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                typingt.append("H");
            }
        }, 600);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                typingt.append(".");
            }
        }, 600);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, loginActivity.class);
                    startActivity(i);
                    finish();
                }
        },1800);
    }


}