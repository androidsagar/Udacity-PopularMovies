package com.udacity.popularmovies;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by sagar on 21/11/16.
 */

public class MyApplication extends Application {
    public static final String TAG="MyApplication";
    private static MyApplication mInstance;
    private RequestQueue requestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }
    public static synchronized MyApplication getInstance(){
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request){

        request.setTag(TAG);
        getRequestQueue().add(request);
    }


}
