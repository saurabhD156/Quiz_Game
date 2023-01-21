package com.example.quiz_game.loginForm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.quiz_game.MainActivity;
import com.example.quiz_game.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText email, password;
    private ProgressBar progressBar;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.signUpPassword);
        Button signUp = findViewById(R.id.btnSignUp);
        progressBar = findViewById(R.id.progressBar);

        signUp.setOnClickListener(View -> {

            String userEmail = Objects.requireNonNull(email.getText()).toString();
            String userPassword = Objects.requireNonNull(password.getText()).toString();

            if (!userEmail.equals("") && !userPassword.equals("")) {
                firebaseSignUp(userEmail, userPassword);
            } else {
                Toast.makeText(this, "Please Fill correctly", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void firebaseSignUp(String userEmail, String userPassword) {
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "SignUp successful", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "SignUp Unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}