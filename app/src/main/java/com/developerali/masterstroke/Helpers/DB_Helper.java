package com.developerali.masterstroke.Helpers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.developerali.masterstroke.Models.NotesModel;

import java.util.ArrayList;

public class DB_Helper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes_history.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NOTES_HISTORY = "notes_history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATETIME = "date";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public DB_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NOTES_HISTORY + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_DATETIME + " TEXT, " +
                COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop and recreate table if upgrading
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES_HISTORY);
        onCreate(db);
    }

    // Insert if not duplicate
    public void addSearchQuery(NotesModel recentSearchModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NOTES_HISTORY + " WHERE " + COLUMN_TITLE + " = ?",
                new String[]{recentSearchModel.getTitle()}
        );

        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, recentSearchModel.getTitle());
        values.put(COLUMN_DESCRIPTION, recentSearchModel.getDescription());
        values.put(COLUMN_DATETIME, recentSearchModel.getDate());

        db.insert(TABLE_NOTES_HISTORY, null, values);
        db.close();
    }

    // Fetch all notes
    public ArrayList<NotesModel> getAllSearchQueries() {
        ArrayList<NotesModel> searchQueries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES_HISTORY + " ORDER BY " + COLUMN_TIMESTAMP + " DESC", null);
        if (cursor.moveToFirst()) {
            do {
                NotesModel recentSearchModel = new NotesModel();
                recentSearchModel.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                recentSearchModel.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                recentSearchModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATETIME)));
                searchQueries.add(recentSearchModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return searchQueries;
    }

    // Delete by title safely
    public void deleteSearchQuery(String searchQuery) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES_HISTORY, COLUMN_TITLE + "=?", new String[]{searchQuery});
        db.close();
    }

    // Reset entire table
    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES_HISTORY);
        onCreate(db);
        db.close();
    }

    // Optional: Get note count
    public int getSearchQueryCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NOTES_HISTORY, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }
}
