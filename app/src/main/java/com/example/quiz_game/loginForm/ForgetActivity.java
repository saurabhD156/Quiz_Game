package com.example.quiz_game.loginForm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.quiz_game.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {

    private TextInputEditText email, password;
    private ProgressBar progressBar;
    private Button reset;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        email = findViewById(R.id.forgetEmail);
        password = findViewById(R.id.forgetPassword);
        progressBar = findViewById(R.id.progressBar);
        reset =  findViewById(R.id.btnReset);

        reset.setOnClickListener(View ->{

            String userEmail =  email.getText().toString();

            if(!email.equals("")){
                restPassword(userEmail);
            }

        });
    }

    private void restPassword(String userEmail) {
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(ForgetActivity.this, "We sent an email to reset your password"
                                    , Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent i = new Intent(ForgetActivity.this, SignInActivity.class);
                            startActivity(i);
                            finish();

                        }
                        else {
                            Toast.makeText(ForgetActivity.this, task.getException().getLocalizedMessage()
                                    , Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}