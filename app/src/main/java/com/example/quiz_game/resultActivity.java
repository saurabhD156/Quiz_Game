package com.example.quiz_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class resultActivity extends AppCompatActivity {

    private TextView scoreCorrect, scoreWrong;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference().child("scores");
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    String userCorrect, userWrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        scoreCorrect = findViewById(R.id.textCorrect);
        scoreWrong = findViewById(R.id.textWrong);
        Button playAgain = findViewById(R.id.btnPlayAgain);
        Button exit = findViewById(R.id.btnExit);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userUid = user.getUid();
                userCorrect = snapshot.child(userUid).child("Correct").getValue().toString();
                userWrong = snapshot.child(userUid).child("Wrong").getValue().toString();

                scoreCorrect.setText(userCorrect);
                scoreWrong.setText(userWrong);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        playAgain.setOnClickListener(View->{
            Intent i = new Intent(resultActivity.this, startGameActivity.class);
            startActivity(i);
            finish();

        });

        exit.setOnClickListener(View->{
            Intent i = new Intent(resultActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }
}