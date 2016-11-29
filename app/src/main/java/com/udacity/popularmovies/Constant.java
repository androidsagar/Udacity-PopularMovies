package com.udacity.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by sagar on 21/11/16.
 */

public class Constant {

    public final static String MOVIE_TAG = "MOVIE";

    public static final String POPULAR ="popular";
    public static final String SORT_RATING = "top_rated";
    public static final String SORT_FAVOURITE = "favourite";

    private static final String BASE_DATA_URL ="https://api.themoviedb.org/3/movie/";
    private final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    private final static String IMAGE_SMALL_SIZE = "w185/";


    public static String getMoviesUrl(String order){
        return BASE_DATA_URL +order+BuildConfig.THE_MOVIEDB_API_KEY;
    }

    public static String getImageUrl(String imageName){
        return BASE_IMAGE_URL+imageName;
    }

    public static String getTrailerUrl(int movieId){
        return BASE_DATA_URL+movieId+"/videos"+BuildConfig.THE_MOVIEDB_API_KEY;
    }

    public static String getReviewUrl(int movieId){
        return BASE_DATA_URL+movieId+"/reviews"+BuildConfig.THE_MOVIEDB_API_KEY;
    }


    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
