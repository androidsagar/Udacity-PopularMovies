package com.udacity.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.udacity.popularmovies.db.MovieContract.*;

/**
 * Created by sagar on 25/11/16.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "popularmovie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_MOVIE_TABLE="CREATE TABLE "+ MovieTable.TABLE_NAME+"("
                +MovieTable._ID+" INTEGER PRIMARY KEY, "
                +MovieTable.COLUMN_TITLE+" TEXT NOT NULL, "
                +MovieTable.COLUMN_OVERVIEW+" TEXT NOT NULL, "
                +MovieTable.COLUMN_POSTER_IMAGE+" TEXT NOT NULL, "
                +MovieTable.COLUMN_RATING+" TEXT NOT NULL, "
                +MovieTable.COLUMN_RELEASE_DATE+" TEXT NOT NULL"+")";
        sqLiteDatabase.execSQL(CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+MovieTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
