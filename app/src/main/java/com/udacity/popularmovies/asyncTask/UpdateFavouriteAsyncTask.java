package com.udacity.popularmovies.asyncTask;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.Toast;

import com.udacity.popularmovies.db.DBUtil;
import com.udacity.popularmovies.db.MovieContract;
import com.udacity.popularmovies.models.Movie;
import com.udacity.popularmovies.R;

/**
 * Created by sagar on 25/11/16.
 */

class UpdateFavouriteAsyncTask extends AsyncTask<Void ,Void ,Void> {
     private Context mContext;
     private Boolean isAlreadyFavourite;
     private Movie movie;
     private Button favButton;

    public UpdateFavouriteAsyncTask(Context mContext, Boolean action, Movie movie, Button favButton) {
        this.mContext=mContext;
        this.isAlreadyFavourite=action;
        this.movie=movie;
        this.favButton=favButton;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (!isAlreadyFavourite){
            ContentValues values= DBUtil.makeContentValues(movie);
            mContext.getContentResolver().insert(MovieContract.MovieTable.CONTENT_URI,values);
        }else {
            mContext.getContentResolver().delete(
                    MovieContract.MovieTable.CONTENT_URI,
                    MovieContract.MovieTable._ID+" =?",
                    new String[]{String.valueOf(movie.getId())}
                                );
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        int toastMsgRes;
        if (isAlreadyFavourite){
            toastMsgRes= R.string.removed_From_Favourite;
            favButton.setText(mContext.getString(R.string.mark_favorite));
        }else {
            toastMsgRes=R.string.added_To_Favourite;
            favButton.setText(mContext.getString(R.string.mark_unfavorite));
        }
        Toast.makeText(mContext,mContext.getString(toastMsgRes),Toast.LENGTH_SHORT).show();
    }
}
