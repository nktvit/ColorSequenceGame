package com.nktvitdev.colorsequencegame2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HighScoresActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        dbHelper = new DatabaseHelper(this);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button playAgainButton = findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        displayHighScores();
    }

    private void displayHighScores() {
        LinearLayout scoresLayout = findViewById(R.id.scoresLayout);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query top 5 scores
        String query = "SELECT name, score FROM high_scores ORDER BY score DESC LIMIT 5";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") int score = cursor.getInt(cursor.getColumnIndex("score"));

            // Create text view for each score entry
            TextView scoreEntry = new TextView(this);
            scoreEntry.setText(String.format("%s: %d", name, score));
            scoreEntry.setTextColor(Color.WHITE);
            scoreEntry.setTextSize(24);
            scoreEntry.setPadding(0, 8, 0, 8);

            scoresLayout.addView(scoreEntry);
        }

        cursor.close();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}