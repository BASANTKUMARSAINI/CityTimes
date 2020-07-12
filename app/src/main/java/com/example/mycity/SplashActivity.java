package com.example.mycity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.agrawalsuneet.dotsloader.loaders.TrailingCircularDotsLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import authantication.UserLoginActivity;
import users.HomeActivity;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_splash);
//        TrailingCircularDotsLoader trailingCircularDotsLoader = new TrailingCircularDotsLoader(
//                this,
//                24,
//                ContextCompat.getColor(this, android.R.color.holo_green_light),
//                100,
//                5);
//        trailingCircularDotsLoader.setAnimDuration(1200);
//        trailingCircularDotsLoader.setAnimDelay(200);
//
//        containerLL.addView(trailingCircularDotsLoader);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user=mAuth.getCurrentUser();
                if(user!=null&&user.isEmailVerified())
                {
                    Intent intent=new Intent(SplashActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(SplashActivity.this, UserLoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        },3000);
    }
}
