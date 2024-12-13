package com.nktvitdev.colorsequencegame2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.os.Handler;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Force landscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(v -> startGame());
    }

    private void startGame() {
        Intent intent = new Intent(this, SequenceActivity.class);
        startActivity(intent);
    }
}
