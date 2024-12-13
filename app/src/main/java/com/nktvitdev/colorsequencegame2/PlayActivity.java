package com.nktvitdev.colorsequencegame2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends AppCompatActivity {
    private Button redButton, blueButton, greenButton, yellowButton;
    private List<Integer> gameSequence;
    private List<Integer> playerSequence;
    private int currentRound;

    // Color constants
    private static final int RED = 0;
    private static final int BLUE = 1;
    private static final int GREEN = 2;
    private static final int YELLOW = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Get sequence from previous activity
        gameSequence = getIntent().getIntegerArrayListExtra("sequence");
        currentRound = getIntent().getIntExtra("round", 1);
        playerSequence = new ArrayList<>();

        initializeButtons();
        setupButtonListeners();
    }

    private void initializeButtons() {
        redButton = findViewById(R.id.redButton);
        blueButton = findViewById(R.id.blueButton);
        greenButton = findViewById(R.id.greenButton);
        yellowButton = findViewById(R.id.yellowButton);
    }

    private void setupButtonListeners() {
        redButton.setOnClickListener(v -> handleButtonClick(RED));
        blueButton.setOnClickListener(v -> handleButtonClick(BLUE));
        greenButton.setOnClickListener(v -> handleButtonClick(GREEN));
        yellowButton.setOnClickListener(v -> handleButtonClick(YELLOW));
    }

    private void handleButtonClick(int color) {
        // Add player's choice to their sequence
        playerSequence.add(color);

        // Highlight the pressed button briefly
        highlightButton(color);

        // Check if the move was correct
        int currentMove = playerSequence.size() - 1;
        if (gameSequence.get(currentMove) != color) {
            // Wrong move - game over
            gameOver();
            return;
        }

        // Check if the sequence is complete
        if (playerSequence.size() == gameSequence.size()) {
            // Round complete - success!
            roundComplete();
        }
    }

    private void highlightButton(int color) {
        Button button = null;
        switch (color) {
            case RED:
                button = redButton;
                break;
            case BLUE:
                button = blueButton;
                break;
            case GREEN:
                button = greenButton;
                break;
            case YELLOW:
                button = yellowButton;
                break;
        }

        if (button != null) {
            button.setAlpha(0.5f);
            Button finalButton = button;
            new Handler().postDelayed(() -> finalButton.setAlpha(1.0f), 200);
        }
    }

    private void roundComplete() {
        // Calculate current total score
        int score = calculateScore();

        // Start next round
        Intent intent = new Intent(this, SequenceActivity.class);
        intent.putExtra("round", currentRound + 1);
        intent.putExtra("currentScore", score);
        startActivity(intent);
        finish();
    }

    private void gameOver() {
        int finalScore = calculateScore();
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra("score", finalScore);
        startActivity(intent);
        finish();
    }

    private int calculateScore() {
        // Get the current score from intent (passed from previous rounds)
        int previousScore = getIntent().getIntExtra("currentScore", 0);

        // Add points for current round
        // First round: 4 points (4 colors)
        // Second round: 6 points (6 colors)
        // Third round: 8 points (8 colors), etc.
        int currentRoundPoints = 4 + ((currentRound - 1) * 2);

        return previousScore + currentRoundPoints;
    }

    @Override
    public void onBackPressed() {
        // Disable back button during play
        super.onBackPressed();
    }
}