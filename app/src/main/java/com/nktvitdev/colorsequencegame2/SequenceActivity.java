package com.nktvitdev.colorsequencegame2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.nktvitdev.colorsequencegame2.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SequenceActivity extends AppCompatActivity {
    private Button redButton, blueButton, greenButton, yellowButton;
    private List<Integer> sequence;
    private int currentRound = 1;
    private Handler handler = new Handler();
    private static final int SEQUENCE_DELAY = 1500; // 1.5 second between colors

    // Color constants
    private static final int RED = 0;
    private static final int BLUE = 1;
    private static final int GREEN = 2;
    private static final int YELLOW = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        currentRound = getIntent().getIntExtra("round", 1);
        int currentScore = getIntent().getIntExtra("currentScore", 0);

        initializeButtons();
        startSequence();
    }

    private void initializeButtons() {
        redButton = findViewById(R.id.redButton);
        blueButton = findViewById(R.id.blueButton);
        greenButton = findViewById(R.id.greenButton);
        yellowButton = findViewById(R.id.yellowButton);
    }

    private void startSequence() {
        // Calculate sequence length (4 initially, +2 each round)
        int sequenceLength = 4 + ((currentRound - 1) * 2);

        // Generate random sequence
        sequence = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < sequenceLength; i++) {
            sequence.add(random.nextInt(4));
        }

        // Show sequence to user
        showSequence();
    }




    private void showSequence() {
        for (int i = 0; i < sequence.size(); i++) {
            final int position = i;
            handler.postDelayed(() -> {
                highlightButton(sequence.get(position));
            }, SEQUENCE_DELAY * i);
        }

        // After showing sequence, move to play screen
        handler.postDelayed(() -> {
            Intent intent = new Intent(SequenceActivity.this, PlayActivity.class);
            intent.putIntegerArrayListExtra("sequence", (ArrayList<Integer>) sequence);
            intent.putExtra("round", currentRound);
            intent.putExtra("currentScore", getIntent().getIntExtra("currentScore", 0));
            startActivity(intent);
            finish();
        }, SEQUENCE_DELAY * (sequence.size() + 1));
    }

    private void highlightButton(int color) {
        // Reset all buttons
        redButton.setAlpha(1.0f);
        blueButton.setAlpha(1.0f);
        greenButton.setAlpha(1.0f);
        yellowButton.setAlpha(1.0f);

        // Highlight selected button
        switch (color) {
            case RED:
                redButton.setAlpha(0.5f);
                break;
            case BLUE:
                blueButton.setAlpha(0.5f);
                break;
            case GREEN:
                greenButton.setAlpha(0.5f);
                break;
            case YELLOW:
                yellowButton.setAlpha(0.5f);
                break;
        }

        // Reset after brief delay
        handler.postDelayed(() -> {
            redButton.setAlpha(1.0f);
            blueButton.setAlpha(1.0f);
            greenButton.setAlpha(1.0f);
            yellowButton.setAlpha(1.0f);
        }, SEQUENCE_DELAY / 2);
    }
}