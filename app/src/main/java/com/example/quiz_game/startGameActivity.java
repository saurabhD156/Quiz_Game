package com.example.quiz_game;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class startGameActivity extends AppCompatActivity {

    private TextView time, correct, wrong;
    private TextView question, ans_A, ans_B, ans_C, ans_D;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser user = auth.getCurrentUser();
    private final DatabaseReference databaseReference = database.getReference().child("Questions");
    private final DatabaseReference reference = database.getReference();

    String quizQuestion;
    String quizAnswerA;
    String quizAnswerB;
    String quizAnswerC;
    String quizAnswerD;
    String quizCorrectAnswer;
    String userAnswer;

    int questionCount;
    int questionNumber = 1;
    int userCorrect = 0;
    int userWrong = 0;

    CountDownTimer countDownTimer;
    private static final long TOTAL_TIME = 30000;
    Boolean timerContinue;
    long leftTime = TOTAL_TIME;


    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        time = findViewById(R.id.time);
        correct = findViewById(R.id.correctAnswer);
        wrong = findViewById(R.id.wrongAnswer);

        question = findViewById(R.id.textQuestion);
        ans_A = findViewById(R.id.answerA);
        ans_B = findViewById(R.id.answerB);
        ans_C = findViewById(R.id.answerC);
        ans_D = findViewById(R.id.answerD);

        Button finishQue = findViewById(R.id.btnFinish);
        Button next = findViewById(R.id.btnNextQue);

        resetTimer();
        game();

        next.setOnClickListener(v -> {
            resetTimer();
            game();
        });

        finishQue.setOnClickListener(v -> {
            sendScore();
            Intent i = new Intent(startGameActivity.this, resultActivity.class);
            startActivity(i);
            finish();
        });

        ans_A.setOnClickListener(v -> {
            pauseTimer();
            userAnswer = "a";
            if(quizCorrectAnswer.equals(userAnswer))
            {
                ans_A.setBackgroundColor(Color.GREEN);
                userCorrect++;
                correct.setText("" + userCorrect);
            }
            else
            {
                ans_A.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("" + userWrong);
                findAnswer();
            }
        });

        ans_B.setOnClickListener(v -> {
            pauseTimer();
            userAnswer = "b";
            if(quizCorrectAnswer.equals(userAnswer))
            {
                ans_B.setBackgroundColor(Color.GREEN);
                userCorrect++;
                correct.setText("" + userCorrect);
            }
            else
            {
                ans_B.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("" + userWrong);
                findAnswer();
            }
        });

        ans_C.setOnClickListener(v -> {
            pauseTimer();
            userAnswer = "c";
            if(quizCorrectAnswer.equals(userAnswer))
            {
                ans_C.setBackgroundColor(Color.GREEN);
                userCorrect++;
                correct.setText("" + userCorrect);
            }
            else
            {
                ans_C.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("" + userWrong);
                findAnswer();
            }
        });

        ans_D.setOnClickListener(v -> {
            pauseTimer();
            userAnswer = "d";
            if(quizCorrectAnswer.equals(userAnswer))
            {
                ans_D.setBackgroundColor(Color.GREEN);
                userCorrect++;
                correct.setText("" + userCorrect);
            }
            else
            {
                ans_D.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("" + userWrong);
                findAnswer();
            }
        });
    }
    public void game()
    {
    startTimer();
        ans_A.setBackgroundColor(Color.WHITE);
        ans_B.setBackgroundColor(Color.WHITE);
        ans_C.setBackgroundColor(Color.WHITE);
        ans_D.setBackgroundColor(Color.WHITE);

        databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.

            questionCount = (int) dataSnapshot.getChildrenCount();

            quizQuestion = dataSnapshot.child(String.valueOf(questionNumber)).child("Q").getValue().toString();
            quizAnswerA = dataSnapshot.child(String.valueOf(questionNumber)).child("a").getValue().toString();
            quizAnswerB = dataSnapshot.child(String.valueOf(questionNumber)).child("b").getValue().toString();
            quizAnswerC = dataSnapshot.child(String.valueOf(questionNumber)).child("c").getValue().toString();
            quizAnswerD = dataSnapshot.child(String.valueOf(questionNumber)).child("d").getValue().toString();
            quizCorrectAnswer = dataSnapshot.child(String.valueOf(questionNumber)).child("Answer").getValue().toString();

            question.setText(quizQuestion);
            ans_A.setText(quizAnswerA);
            ans_B.setText(quizAnswerB);
            ans_C.setText(quizAnswerC);
            ans_D.setText(quizAnswerD);

            if(questionNumber < questionCount)
            {
                questionNumber++;
            }
            else
            {
                Toast.makeText(startGameActivity.this, "You answered all questions", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            Toast.makeText(startGameActivity.this, "Sorry, there is a problem", Toast.LENGTH_LONG).show();

        }
    });
}

    public void findAnswer() {
        switch (quizCorrectAnswer) {
            case "a":
                ans_A.setBackgroundColor(Color.GREEN);
                break;
            case "b":
                ans_B.setBackgroundColor(Color.GREEN);
                break;
            case "c":
                ans_C.setBackgroundColor(Color.GREEN);
                break;
            case "d":
                ans_D.setBackgroundColor(Color.GREEN);
                break;
        }
    }

    public void startTimer()
    {
        countDownTimer = new CountDownTimer(leftTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                leftTime = millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                timerContinue = false;
                pauseTimer();
                question.setText("Sorry, time is up");
            }
        }.start();

        timerContinue = true;
    }
    public void resetTimer()
    {
        leftTime = TOTAL_TIME;
        updateCountDownText();
    }
    public void updateCountDownText()
    {
        int second = (int)(leftTime / 1000) % 60;
        time.setText("" + second);
    }

    public void pauseTimer()
    {
        countDownTimer.cancel();
        timerContinue = false;
    }

    public void sendScore()
    {
        String userUID = user.getUid();
        reference.child("scores").child(userUID).child("correct").setValue(userCorrect)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(startGameActivity.this, "scores sent successfully", Toast.LENGTH_LONG).show();
                    }
                });
        reference.child("scores").child(userUID).child("wrong").setValue(userWrong);
    }
}