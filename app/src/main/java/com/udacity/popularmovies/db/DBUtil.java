package com.udacity.popularmovies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.udacity.popularmovies.models.Movie;
import com.udacity.popularmovies.db.MovieContract.*;



/**
 * Created by sagar on 25/11/16.
 */

public class DBUtil {

    public static ContentValues makeContentValues(Movie movie){
        ContentValues values=new ContentValues();
        values.put(MovieTable._ID,movie.getId());
        values.put(MovieTable.COLUMN_TITLE,movie.getTitle());
        values.put(MovieTable.COLUMN_OVERVIEW,movie.getOverview());
        values.put(MovieTable.COLUMN_POSTER_IMAGE,movie.getMoviePoster());
        values.put(MovieTable.COLUMN_RATING,String.valueOf(movie.getRating()));
        values.put(MovieTable.COLUMN_RELEASE_DATE,movie.getReleaseDate());
        return values;
    }

    public static boolean isFavourite(Context context,String id){
        Cursor cursor=context.getContentResolver().query(
                MovieTable.CONTENT_URI,
                null,
                MovieTable._ID+" = ?",
                new String[]{id},
                null);
          if (cursor!=null){
              int count=cursor.getCount();
              cursor.close();
              return (count>0);
          }
          return false;
    }
}
