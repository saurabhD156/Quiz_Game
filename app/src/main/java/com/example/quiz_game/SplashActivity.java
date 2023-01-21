package com.example.quiz_game;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.quiz_game.loginForm.SignInActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private TextView logo, slogan;
    Animation textAnim;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textAnim = AnimationUtils.loadAnimation(this,R.anim.splash_textanim);

        logo = findViewById(R.id.textLogo);
        slogan = findViewById(R.id.textSlogan);

        logo.setAnimation(textAnim);
        slogan.setAnimation(textAnim);

        Intent i = new Intent(SplashActivity.this, SignInActivity.class);

        new Handler().postDelayed(() -> {
            startActivity(i);
            finish();
        },3500);
    }
}