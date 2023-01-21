package com.example.quiz_game;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.quiz_game.loginForm.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = findViewById(R.id.btnStart);
        Button signOut = findViewById(R.id.signOut);

        start.setOnClickListener(View->{

            Intent i = new Intent(MainActivity.this, startGameActivity.class);
            startActivity(i);

        });

        signOut.setOnClickListener(View->{
            auth.signOut();
            Intent i = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(i);

        });
    }
}