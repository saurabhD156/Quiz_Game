package com.example.quiz_game;

import androidx.annotation.NonNull;
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
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private final DatabaseReference databaseReference = database.getReference().child("Questions");
    private  DatabaseReference reference = database.getReference();

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

        next.setOnClickListener(View -> {
            game();
            startTimer();
        });

        finishQue.setOnClickListener(View -> {
            sendScore();
            Intent i = new Intent(startGameActivity.this, resultActivity.class);
            startActivity(i);
            finish();

        });

        ans_A.setOnClickListener(View -> {
            userAnswer = "a";

            if (quizCorrectAnswer.equals(userAnswer)) {
                ans_A.setBackgroundColor(Color.GREEN);
                userCorrect++;
                correct.setText("" + userCorrect);

            } else {
                ans_A.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("" + userWrong);
                findAnswer();
            }
            pauseTimer();
        });

        ans_B.setOnClickListener(View -> {
            userAnswer = "b";

            if (quizCorrectAnswer.equals(userAnswer)) {
                ans_B.setBackgroundColor(Color.GREEN);
                userCorrect++;
                correct.setText("" + userCorrect);

            } else {
                ans_B.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("" + userWrong);
                findAnswer();
            }
            pauseTimer();
        });

        ans_C.setOnClickListener(View -> {
            userAnswer = "c";

            if (quizCorrectAnswer.equals(userAnswer)) {
                ans_C.setBackgroundColor(Color.GREEN);
                userCorrect++;
                correct.setText("" + userCorrect);

            } else {
                ans_C.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("" + userWrong);
                findAnswer();
            }
            pauseTimer();
        });

        ans_D.setOnClickListener(View -> {
            userAnswer = "d";

            if (quizCorrectAnswer.equals(userAnswer)) {
                ans_D.setBackgroundColor(Color.GREEN);
                userCorrect++;
                correct.setText("" + userCorrect);

            } else {
                ans_D.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("" + userWrong);
                findAnswer();
            }
            pauseTimer();

        });
    }

    public void game() {

        startTimer();

        ans_A.setBackgroundColor(Color.WHITE);
        ans_B.setBackgroundColor(Color.WHITE);
        ans_C.setBackgroundColor(Color.WHITE);
        ans_D.setBackgroundColor(Color.WHITE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

                if (questionNumber < questionCount) {
                    questionNumber++;
                } else {
                    Toast.makeText(startGameActivity.this, "You Answered all questions"
                            , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Toast.makeText(startGameActivity.this, "There is an error"
                        , Toast.LENGTH_SHORT).show();
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

    public void startTimer() {

        countDownTimer = new CountDownTimer(leftTime, 1000) {
            @Override
            public void onTick(long l) {

                leftTime = l;
                updateCountDownText();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                timerContinue = false;
                pauseTimer();
                question.setText("Sorry Time's Up!");
            }
        }.start();
        timerContinue = true;
    }

    public void resetTimer() {
        leftTime = TOTAL_TIME;
        updateCountDownText();
    }

    @SuppressLint("SetTextI18n")
    private void updateCountDownText() {
        int second = (int) ((leftTime / 1000) % 60);
        time.setText("" + second);
    }

    public void pauseTimer() {
        countDownTimer.cancel();
        timerContinue = false;
    }

    public void sendScore()
    {
        String userUid = user.getUid();
        reference.child("scores").child(userUid).child("Correct")
                .setValue(userCorrect)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(startGameActivity.this, "Score send Successfully", Toast.LENGTH_SHORT).show();

                            }
                        });

        reference.child("scores").child(userUid).child("Wrong")
                .setValue(userWrong);
    }

}