package com.nktvitdev.colorsequencegame2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {
    private TextView scoreText;
    private Button playAgainButton;
    private Button highScoresButton;
    private DatabaseHelper dbHelper;
    private int finalScore;
    private EditText nameInput;
    private Button submitNameButton;
    private LinearLayout nameInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        dbHelper = new DatabaseHelper(this);
        finalScore = getIntent().getIntExtra("score", 0);

        initializeViews();
        setupButtons();
        checkHighScore();
    }

    private void initializeViews() {
        scoreText = findViewById(R.id.scoreText);
        playAgainButton = findViewById(R.id.playAgainButton);
        highScoresButton = findViewById(R.id.highScoresButton);
        nameInputLayout = findViewById(R.id.nameInputLayout);
        nameInput = findViewById(R.id.nameInput);
        submitNameButton = findViewById(R.id.submitNameButton);

        scoreText.setText("Your score was " + finalScore);
    }

    private void setupButtons() {
        playAgainButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        highScoresButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HighScoresActivity.class);
            startActivity(intent);
        });

        submitNameButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if (!name.isEmpty()) {
                dbHelper.addScore(name, finalScore);
                nameInputLayout.setVisibility(View.GONE);
                Toast.makeText(this, "Score saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkHighScore() {
        if (dbHelper.isHighScore(finalScore)) {
            nameInputLayout.setVisibility(View.VISIBLE);
        } else {
            nameInputLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}