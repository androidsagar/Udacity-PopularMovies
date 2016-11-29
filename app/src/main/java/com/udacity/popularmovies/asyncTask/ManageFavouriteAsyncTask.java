package com.udacity.popularmovies.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

import com.udacity.popularmovies.db.DBUtil;
import com.udacity.popularmovies.models.Movie;
import com.udacity.popularmovies.R;

/**
 * Created by sagar on 25/11/16.
 */

public class ManageFavouriteAsyncTask extends AsyncTask<Void,Void,Boolean>{

    private Context mContext;
    private Boolean action;
    private Movie movie;
    private Button favButton;
    public ManageFavouriteAsyncTask(Context mContext, Boolean action, Movie movie, Button favButton) {
        this.mContext=mContext;
        this.action=action;
        this.movie=movie;
        this.favButton=favButton;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return DBUtil.isFavourite(mContext,String.valueOf(movie.getId()));
    }

    @Override
    protected void onPostExecute(Boolean isFavourite) {
        if (action){
            new UpdateFavouriteAsyncTask(mContext,isFavourite,movie,favButton).execute();
        }else {
            if (isFavourite){
                favButton.setText(mContext.getString(R.string.mark_unfavorite));
            }else {
                favButton.setText(mContext.getString(R.string.mark_favorite));
            }
        }
    }

}
