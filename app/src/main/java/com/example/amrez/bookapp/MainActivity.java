package com.example.amrez.bookapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<B_object>> {

    private String requestURL = "https://www.googleapis.com/books/v1/volumes?q=";

    private LoaderManager loaderManager;
    private static int BOOK_LOADER_ID = 1;
    private myAdapter Adapter;
    private ProgressBar progressBar;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // find view for widgets
        final Button b_search = (Button) findViewById(R.id.b_search);
        final ListView bookListView = (ListView) findViewById(R.id.book_list);
        final EditText search = (EditText) findViewById(R.id.editText);
        //progress Visibility
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        //at empty state
        mEmptyStateTextView = (TextView) findViewById(R.id.emptyState);
        bookListView.setEmptyView(mEmptyStateTextView);

        //add Books to list
        Adapter = new myAdapter(this, new ArrayList<B_object>());
        bookListView.setAdapter(Adapter);

        //for network connectivity
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //check network connectivity
        if (networkInfo != null && networkInfo.isConnected()) {
            //give any data to list
            requestURL = requestURL + "Android";
            loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
        } else {
            mEmptyStateTextView.setText(R.string.networkState);
        }

        // Set onClickListener on the b_search button
        b_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userQuery = search.getText().toString();
                //if EditText empty
                if (search.length() == 0) {
                    search.setHint("enter data to search");
                }
                //if EditText not empty
                else {
                    BOOK_LOADER_ID += 1;
                    requestURL = "https://www.googleapis.com/books/v1/volumes?q=";
                    //if network connection
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {

                        //to hide bookListView while searching
                        bookListView.setVisibility(View.GONE);
                        // Remove  spaces from userQuery
                        String userQueryMain = userQuery.replaceAll("\\s+", "");
                        // Update Url request
                        requestURL = requestURL + userQueryMain;
                        // Fetch QueryUtils data from loaderManager
                        loaderManager = getLoaderManager();
                        // Init loader with eny with BOOK_LOADER_ID counter
                        loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    }
                    //if No connection
                    else {
                        Adapter.clear();
                        // hide progressBar
                        progressBar.setVisibility(View.GONE);
                        //tell user No internet connection
                        mEmptyStateTextView.setText(R.string.networkState);
                    }

                }
            }
        });

        //onclick on any data on list
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                B_object currentBook = Adapter.getItem(i);
                Uri bookUri = Uri.parse(currentBook.getPreviewLInk());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }

    //LoaderManager Override functions
    @Override
    public Loader<List<B_object>> onCreateLoader(int i, Bundle bundle) {
        progressBar.setVisibility(View.VISIBLE);
        Uri baseUri = Uri.parse(requestURL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        return new asynceTask(this, uriBuilder.toString());
    }

    @Override
    public void onLoaderReset(Loader<List<B_object>> loader) {
        Adapter.clear();
    }

    @Override
    public void onLoadFinished(Loader<List<B_object>> loader, List<B_object> books) {
        progressBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText("No Bookes founded");
        Adapter.clear();

        //if have data update list
        if (books != null && !books.isEmpty()) {
            Adapter.addAll(books);
        }

    }
}
