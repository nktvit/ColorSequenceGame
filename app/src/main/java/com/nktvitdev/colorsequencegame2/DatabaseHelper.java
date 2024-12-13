package com.nktvitdev.colorsequencegame2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HighScores.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "high_scores";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SCORE = "score";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SCORE + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean isHighScore(int score) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME +
                " WHERE " + COLUMN_SCORE + " > " + score;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count < 5;
    }

    public void addScore(String name, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_SCORE, score);
        db.insert(TABLE_NAME, null, values);

        // Keep only top 5 scores
        String deleteQuery = "DELETE FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ID + " NOT IN " +
                "(SELECT " + COLUMN_ID + " FROM " + TABLE_NAME +
                " ORDER BY " + COLUMN_SCORE + " DESC LIMIT 5)";
        db.execSQL(deleteQuery);
    }
}