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
                COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades here
    }

    public void addSearchQuery(NotesModel recentSearchModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the search query already exists
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NOTES_HISTORY + " WHERE " + COLUMN_TITLE + " = ?",
                new String[]{recentSearchModel.getTitle()}
        );

        if (cursor.getCount() > 0) {
            // If the query exists, do not insert it again
            cursor.close();
            db.close();
            return;
        }

        cursor.close();

        //If does not exist insert the new search query
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, recentSearchModel.getTitle());
        values.put(COLUMN_DESCRIPTION, recentSearchModel.getDescription());
        db.insert(TABLE_NOTES_HISTORY, null, values);
        db.close();
    }

    public ArrayList<NotesModel> getAllSearchQueries() {
        ArrayList<NotesModel> searchQueries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES_HISTORY, null);
        if (cursor.moveToFirst()) {
            do {

                NotesModel recentSearchModel = new NotesModel();

                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));

                recentSearchModel.setTitle(title);
                recentSearchModel.setDescription(description);

                searchQueries.add(recentSearchModel);


            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return searchQueries;
    }

    public void deleteSearchQuery(String searchQuery) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTES_HISTORY + " WHERE " + COLUMN_TITLE + "='" + searchQuery + "'");
        db.close();
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Drop existing tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES_HISTORY);
        // Recreate tables
        onCreate(db);
        db.close();
    }
}
