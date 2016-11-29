package com.udacity.popularmovies.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.popularmovies.Constant;
import com.udacity.popularmovies.fragment.DetailActivityFragment;
import com.udacity.popularmovies.fragment.MainActivityFragment;
import com.udacity.popularmovies.models.Movie;
import com.udacity.popularmovies.R;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.MyCallback{

    private static final String DETAIL_FRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.movie_detail_container)!=null){
            mTwoPane=true;
            if (savedInstanceState==null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container,new DetailActivityFragment(),DETAIL_FRAGMENT_TAG).commit();
            }
        }else {
            mTwoPane=false;
        }



    }

    @Override
    public void onItemSelected(Movie movie) {

        if(mTwoPane){
            Bundle bundle=new Bundle();
            bundle.putParcelable(Constant.MOVIE_TAG,movie);

            DetailActivityFragment detailActivityFragment=new DetailActivityFragment();
            detailActivityFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container,detailActivityFragment,DETAIL_FRAGMENT_TAG).commit();
        }else {
            Intent intent=new Intent(this,DetailActivity.class);
            intent.putExtra(Constant.MOVIE_TAG,movie);
            startActivity(intent);
        }


    }
}
