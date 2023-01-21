package com.example.quiz_game.loginForm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz_game.MainActivity;
import com.example.quiz_game.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    private TextInputEditText email, password;
    private ProgressBar progressBar;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.signInEmail);
        password = findViewById(R.id.signInPassword);
        TextView forgetPassword = findViewById(R.id.textForgetPassword);
        TextView signUp = findViewById(R.id.textSignUp);
        progressBar = findViewById(R.id.progressBar);
        Button signIn = findViewById(R.id.btnSignIn);

        signIn.setOnClickListener(View -> {
            String userEmail = Objects.requireNonNull(email.getText()).toString();
            String userPassword = Objects.requireNonNull(password.getText()).toString();

            if (!userEmail.equals("") && !userPassword.equals("")) {
                firebaseSignIn(userEmail, userPassword);
            } else {
                Toast.makeText(this, "Please Fill correctly", Toast.LENGTH_SHORT).show();
            }
        });

        forgetPassword.setOnClickListener(View -> {
            Intent i = new Intent(SignInActivity.this, ForgetActivity.class);
            startActivity(i);

        });

        signUp.setOnClickListener(View -> {
            Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(i);
        });
    }

    private void firebaseSignIn(String userEmail, String userPassword) {
        progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent i = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "Sign in UnSuccessful", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if(user!=null){
            Intent i = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}