/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2020-01-01&endtime=2020-04-02&minfelt=50&minmagnitude=5";

    //earthquakeAdapter
    private EarthquakeAdapter mAdapter;

    //empty state text view
    private TextView mEmptyStateTextView;

    //loading spinner
    ProgressBar mLoadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        //spinner
        mLoadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);

        //create a custom array adapter whose source is list_item
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        //finding the list on which adapter is to be set up
        ListView listView = (ListView) findViewById(R.id.list);


        //Empty state text View object
        mEmptyStateTextView = (TextView) findViewById(R.id.empty);
        listView.setEmptyView(mEmptyStateTextView);

        //setting the adapter
        listView.setAdapter(mAdapter);


        //Check Internet connectivity
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            //Loader Manager
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
        }
        else {
            mLoadingSpinner.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        //set a item click listener for the url to open
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //get the earthquake the user clicked on
                Earthquake currentEarthquake = (Earthquake) mAdapter.getItem(position);

                //toast message for opening the web browser
                Toast.makeText(view.getContext(),"Redirecting to the USGS website",Toast.LENGTH_LONG).show();

                //intent to open the web browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentEarthquake.getUrl()));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    public static class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

        /** Tag for log messages */
        private static final String LOG_TAG = EarthquakeLoader.class.getName();

        private String mURL;

        public EarthquakeLoader(Context context,String url) {
            super(context);
            mURL=url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        /**
         * This is on a background thread.
         */
        @Override
        public List<Earthquake> loadInBackground() {

            // Don't perform the request if there are no URLs, or the first URL is null.
            if (mURL == null) {
                return null;
            }
            // Perform the HTTP request for earthquake data and process the response.
            List<Earthquake> result = QueryUtils.fetchEarthquakeData(mURL);
            return result;
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {

        Log.i("TEST: ","onLoadFinished is called");

        mLoadingSpinner.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_earthquakes_found);

        // If there is no result, do nothing.
        if (data == null) {
            return;
        }
        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        mAdapter.clear();
    }
}


