package com.udacity.popularmovies.asyncTask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.udacity.popularmovies.adapters.CustomAdapter;
import com.udacity.popularmovies.db.MovieContract;
import com.udacity.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagar on 25/11/16.
 */

public class FetchFavouriteAsyncTask extends AsyncTask<Void,Void,List<Movie>>{
    private Context mContext;
    private CustomAdapter adapter;
    private List<Movie> movieList;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieTable._ID,
            MovieContract.MovieTable.COLUMN_TITLE,
            MovieContract.MovieTable.COLUMN_OVERVIEW,
            MovieContract.MovieTable.COLUMN_POSTER_IMAGE,
            MovieContract.MovieTable.COLUMN_RATING,
            MovieContract.MovieTable.COLUMN_RELEASE_DATE

               };

    private static final int COLUMN_ID=0;
    private static final int COLUMN_TITLE=1;
    private static final int COLUMN_OVERVIEW=2;
    private static final int COLUMN_POSTER_IMAGE=3;
    private static final int COLUMN_RATING=4;
    private static final int COLUMN_RELEASE_DATE=5;


    public FetchFavouriteAsyncTask(Context mContext, CustomAdapter adapter, List<Movie> movieList) {
        this.mContext=mContext;
        this.adapter=adapter;
        this.movieList=movieList;
    }

    private List<Movie> getFavouriteMoviesFromDB(Cursor cursor){
        List<Movie> list=new ArrayList<>();
        if (cursor!=null && cursor.moveToFirst()){
            do{
                Movie movie=new Movie(
                        cursor.getInt(COLUMN_ID),
                        cursor.getString(COLUMN_TITLE),
                        cursor.getString(COLUMN_OVERVIEW),
                        cursor.getString(COLUMN_POSTER_IMAGE),
                        cursor.getDouble(COLUMN_RATING),
                        cursor.getString(COLUMN_RELEASE_DATE)
                );
                list.add(movie);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }
    @Override
    protected List<Movie> doInBackground(Void... voids) {
        Cursor cursor= mContext.getContentResolver().query(
                MovieContract.MovieTable.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null
              );
        return getFavouriteMoviesFromDB(cursor);
    }

    @Override
    protected void onPostExecute(List<Movie> movieList) {
          if (movieList!=null){
              if (adapter!=null){
                  adapter.clear();
                  for (Movie movie:movieList){
                      adapter.add(movie);
                  }
              }

          }

    }
}
