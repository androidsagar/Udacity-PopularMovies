package com.udacity.popularmovies.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by sagar on 25/11/16.
 */

public class MovieProvider extends ContentProvider {

    private static UriMatcher uriMatcher=buildUriMatcher();
    private MovieDbHelper dbHelper;

    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 101;

    static UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        String authority=MovieContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority,MovieContract.MovieTable.TABLE_NAME,MOVIE);
        uriMatcher.addURI(authority, MovieContract.MovieTable.TABLE_NAME+"/#",MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper=new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri,String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        int match=uriMatcher.match(uri);
        switch (match){
            case MOVIE:
                cursor=dbHelper.getReadableDatabase().query(
                        MovieContract.MovieTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                        );
                break;
            case MOVIE_WITH_ID:
                String movieIdSelection= MovieContract.MovieTable.TABLE_NAME+"."+ MovieContract.MovieTable.TABLE_NAME+" = ?";
                String[] movieSelectionArgs=new String[]{MovieContract.MovieTable.getMovieIdFromUri(uri)};
                cursor=dbHelper.getReadableDatabase().query(
                        MovieContract.MovieTable.TABLE_NAME,
                        projection,
                        movieIdSelection,
                        movieSelectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException();
        }
        if (getContext()!=null && getContext().getContentResolver()!=null){
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match=uriMatcher.match(uri);
        String type;
        switch (match){
            case MOVIE:
                type= MovieContract.MovieTable.CONTENT_TYPE;
                break;
            case MOVIE_WITH_ID:
                type= MovieContract.MovieTable.CONTENT_ITEM_TYPE;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return type;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri returnUri;
        int match=uriMatcher.match(uri);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        switch (match){
            case MOVIE:
                long movieId=db.insert(MovieContract.MovieTable.TABLE_NAME,null,contentValues);
                if (movieId!=-1){
                    returnUri= MovieContract.MovieTable.buildMovieUri(movieId);
                }else {
                    returnUri=null;
                }
                break;
            default:
                throw  new UnsupportedOperationException("Invalid uri: " + uri);
        }
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;
        int match=uriMatcher.match(uri);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if (selection==null){
            selection="1";
        }
        switch (match){
            case MOVIE:
                rowsDeleted=db.delete(MovieContract.MovieTable.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Invalid uri: " + uri);
        }
        if (rowsDeleted!=0){
            if (getContext() != null && getContext().getContentResolver() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        db.close();
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int rowsUpdated;
        int match=uriMatcher.match(uri);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        switch (match){
            case MOVIE:
                rowsUpdated=db.update(MovieContract.MovieTable.TABLE_NAME,contentValues,s,strings);
                break;
            default:
                throw new UnsupportedOperationException("Invalid Uri "+uri);
        }
        if (rowsUpdated != 0) {
            if (getContext() != null && getContext().getContentResolver() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        return rowsUpdated;
    }
}
