package com.example.amrez.bookapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by amrez on 10/14/2017.
 */

public class asynceTask extends AsyncTaskLoader<List<B_object>> {

    private String mUrl;

    public asynceTask(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<B_object> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // extract list books
        List<B_object> books = QueryUtils.fetchEarthquakeData(mUrl);
        return books;
    }

}
