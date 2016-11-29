package com.udacity.popularmovies.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.udacity.popularmovies.asyncTask.FetchFavouriteAsyncTask;
import com.udacity.popularmovies.Constant;
import com.udacity.popularmovies.adapters.CustomAdapter;
import com.udacity.popularmovies.MyApplication;
import com.udacity.popularmovies.models.Movie;
import com.udacity.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment {

    @Bind(R.id.grid)
    GridView gridView;

    private static final String STATE_Movies="MOVIES";
    private static final String STATE_SORT="SORT";
    private MenuItem mMenuItemSortPopular,mMenuItemSortRating,mMenuItemSortFav;
    private String mSortCriteria = Constant.POPULAR;
    private ArrayList<Movie> moviesList;
    private CustomAdapter adapter;

    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }

    public interface MyCallback {
        void onItemSelected(Movie movie);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option,menu);

        mMenuItemSortPopular=menu.findItem(R.id.action_sort_popular);
        mMenuItemSortRating=menu.findItem(R.id.action_sort_rating);
        mMenuItemSortFav=menu.findItem(R.id.action_sort_favourite);

        if (mSortCriteria.contentEquals(Constant.POPULAR)){

            if (!mMenuItemSortPopular.isChecked()){
                mMenuItemSortPopular.setChecked(true);
            }

        }else if(mSortCriteria.contentEquals(Constant.SORT_RATING)){

            if (!mMenuItemSortRating.isChecked()) mMenuItemSortRating.setChecked(true);

        }else if(mSortCriteria.contentEquals(Constant.SORT_FAVOURITE)){

            if (!mMenuItemSortFav.isChecked()) mMenuItemSortFav.setChecked(true);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_sort_popular:
                if (Constant.isOnline(getContext())) {
                    mSortCriteria = Constant.POPULAR;
                    fetchMovies(Constant.getMoviesUrl(Constant.POPULAR));
                    if(!mMenuItemSortPopular.isChecked()) mMenuItemSortPopular.setChecked(true);
                }
                return true;

            case R.id.action_sort_rating:
                if (Constant.isOnline(getContext())) {
                    mSortCriteria = Constant.SORT_RATING;
                    fetchMovies(Constant.getMoviesUrl(Constant.SORT_RATING));
                    if(!mMenuItemSortRating.isChecked()) mMenuItemSortRating.setChecked(true);
                }
                return true;

            case R.id.action_sort_favourite:
                if (Constant.isOnline(getContext())){
                    mSortCriteria=Constant.SORT_FAVOURITE;
                    if (!mMenuItemSortFav.isChecked())mMenuItemSortFav.setChecked(true);
                }
                new FetchFavouriteAsyncTask(getContext(),adapter,moviesList).execute();
                return true;

        }
        return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_main_activity, container, false);
        ButterKnife.bind(this,view);
        moviesList=new ArrayList<>();
        if (savedInstanceState!=null){
            mSortCriteria=savedInstanceState.getString(STATE_SORT);
            ArrayList<Movie> list=savedInstanceState.getParcelableArrayList(STATE_Movies);
            moviesList.addAll(list);
        }
        adapter=new CustomAdapter(getContext(),moviesList);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MyCallback)getActivity()).onItemSelected(moviesList.get(i));
            }
        });

        if(savedInstanceState==null && Constant.isOnline(getContext())){
            fetchMovies(Constant.getMoviesUrl(mSortCriteria));
        }
        return view;
    }

    void fetchMovies(String url){

        JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("response "+response);
                adapter.clear();
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               System.out.println("error "+error);
            }
        });
        objectRequest.setShouldCache(false);
        MyApplication.getInstance().addToRequestQueue(objectRequest);
    }

    void parseJson(JSONObject response){
        List<Movie> list=new ArrayList<>();
        try {
            JSONArray jsonArray=response.getJSONArray("results");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object=jsonArray.getJSONObject(i);
                String posterPath=object.getString("poster_path");
                String title=object.getString("original_title");
                String overview=object.getString("overview");
                Double rating=  object.getDouble("vote_average");
                String releaseDate=object.getString("release_date");
                int id=object.getInt("id");

                adapter.add(new Movie(id,title,overview,posterPath,rating,releaseDate));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_Movies,moviesList);
        outState.putString(STATE_SORT,mSortCriteria);
    }
}
