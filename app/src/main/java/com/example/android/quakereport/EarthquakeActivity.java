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
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Earthquake>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final int EARTHQUAKE_LOADER_ID = 1;

    /**
     * Adapter for the list of earthquakes
     */
    private EarthquakeAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private ProgressBar loadingIndicator;

    /**
     * URL to query the USGS dataset for earthquake information
     */
    private static final String USGS_REQUEST_URL =
            //"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-05";
            // "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-09&minfelt=50&minmagnitude=3";
            "https://earthquake.usgs.gov/fdsnws/event/1/query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Kick off an {@link AsyncTask} to perform the network request
//        TsunamiAsyncTask task = new TsunamiAsyncTask();
//        task.execute(USGS_REQUEST_URL);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.INVISIBLE);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new EarthquakeAdapter(this,
                R.layout.earthquake_activity, new ArrayList<Earthquake>());

        // Obtain a reference to the SharedPreferences file for this app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // And register to be notified of preference changes
        // So we know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.settings_min_magnitude_key)) ||
                key.equals(getString(R.string.settings_order_by_key))
                || key.equals(getString(R.string.settings_limit_results_key))) {
            // Clear the ListView as a new query will be kicked off
            mAdapter.clear();

            // Hide the empty state text view as the loading indicator will be displayed
            mEmptyStateTextView.setVisibility(View.GONE);

            // Show the loading indicator while new data is being fetched
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);

            // Restart the loader to requery the USGS as the query settings have been updated
            getLoaderManager().restartLoader(EARTHQUAKE_LOADER_ID, null, this);
        }

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        loadingIndicator.setVisibility(ProgressBar.VISIBLE);
        loadingIndicator.setIndeterminate(true);

        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(this);

        String limitResults = sharedPrefs.getString(
                getString(R.string.settings_limit_results_key),
                getString(R.string.settings_limit_results_default));

        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", limitResults);
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        System.out.println("uriBuilder.toString() " + uriBuilder.toString());

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {

        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        mEmptyStateTextView.setText(R.string.no_earthquakes);
        loadingIndicator.setVisibility(ProgressBar.GONE);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        mAdapter.addAll(earthquakes);

        updateUi((ArrayList) earthquakes);
    }

    /**
     * Update the UI
     *
     * @param earthquakes
     */
    private void updateUi(ArrayList<Earthquake> earthquakes) {
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        //final EarthquakeAdapter adapter =
        //        new EarthquakeAdapter(this,
        //               R.layout.earthquake_activity, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                // Find the current earthquake that was clicked on
//                Earthquake currentEarthquake = adapter.getItem(position);
//
//                // Convert the String URL into a URI object (to pass into the Intent constructor)
//                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
//
//                // Create a new intent to view the earthquake URI
//                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
//
//                // Send the intent to launch a new activity
//                startActivity(websiteIntent);

                Earthquake currentEarthquake = mAdapter.getItem(position);
                String title = currentEarthquake.getLocation();
                String number_of_people = currentEarthquake.getNumberOfPeople();
                String perceived_magnitude = currentEarthquake.getPerceivedStrength();
                Intent intent = new Intent(EarthquakeActivity.this, EarthQuakeFeltActivity.class);


                intent.putExtra("title", title);
                intent.putExtra("number_of_people", number_of_people);
                intent.putExtra("perceived_magnitude", perceived_magnitude);

                System.out.println(currentEarthquake);

                startActivity(intent);


            }
        });
    }

}


///////////////////////


//    private class TsunamiAsyncTask extends AsyncTask<String, Integer, ArrayList<Earthquake>> {
//
//        @Override
//        protected ArrayList<Earthquake> doInBackground(String... urls) {
//
//            System.out.println("In doInBackground");
//            // Create URL object
//            URL url = createUrl(urls[0]);
//
//            // Perform HTTP request to the URL and receive a JSON response back
//            String jsonResponse = "";
//            try {
//                jsonResponse = makeHttpRequest(url);
////                publishProgress(10);
//            } catch (IOException e) {
//                // TODO Handle the IOException
//            }
//
//            // Extract relevant fields from the JSON response and create an {@link Event} object
//            ArrayList<Earthquake> earthquakes = extractEarthQuakesFromJson(jsonResponse);
//
//            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
//            return earthquakes;
//        }
//
//        /**
//         * Update the screen with the given earthquake (which was the result of the
//         * {@link TsunamiAsyncTask}).
//         */
//        @Override
//        protected void onPostExecute(ArrayList<Earthquake> earthquake) {
//            System.out.println("In onPostExecute");
//
//            if (earthquake == null) {
//                return;
//            }
//
//            updateUi(earthquake);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            System.out.println("In onPreExecute");
//        }
//
//        //        @Override
////        protected void onProgressUpdate(Integer... values) {
////            super.onProgressUpdate(values);
////        }
//
//
//        /**
//         * Make an HTTP request to the given URL and return a String as the response.
//         */
//        private String makeHttpRequest(URL url) throws IOException {
//            String jsonResponse = "";
//            HttpURLConnection urlConnection = null;
//            InputStream inputStream = null;
//            try {
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.setReadTimeout(10000 /* milliseconds */);
//                urlConnection.setConnectTimeout(15000 /* milliseconds */);
//                urlConnection.connect();
//                inputStream = urlConnection.getInputStream();
//                jsonResponse = readFromStream(inputStream);
//            } catch (IOException e) {
//                // TODO: Handle the exception
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (inputStream != null) {
//                    // function must handle java.io.IOException here
//                    inputStream.close();
//                }
//            }
//            return jsonResponse;
//        }
//
//        /**
//         * Convert the {@link InputStream} into a String which contains the
//         * whole JSON response from the server.
//         */
//        private String readFromStream(InputStream inputStream) throws IOException {
//            StringBuilder output = new StringBuilder();
//            if (inputStream != null) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
//                BufferedReader reader = new BufferedReader(inputStreamReader);
//                String line = reader.readLine();
//                while (line != null) {
//                    output.append(line);
//                    line = reader.readLine();
//                }
//            }
//            return output.toString();
//        }
//
//        /**
//         * Returns new URL object from the given string URL.
//         */
//        private URL createUrl(String stringUrl) {
//            URL url = null;
//            try {
//                url = new URL(stringUrl);
//            } catch (MalformedURLException exception) {
//                Log.e(LOG_TAG, "Error with creating URL", exception);
//                return null;
//            }
//            return url;
//        }
//
//        private ArrayList<Earthquake> extractEarthQuakesFromJson(String jsonResponse) {
//
//            JSONObject jsonObject;
//            JSONArray jsonArray;
//            JSONObject jsonEarthquake;
//
//
//            // Create an empty ArrayList that we can start adding earthquakes to
//            ArrayList<Earthquake> earthquakes = new ArrayList<>();
//
//            // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
//            // is formatted, a JSONException exception object will be thrown.
//            // Catch the exception so the app doesn't crash, and print the error message to the logs.
//            try {
//
//                jsonObject = new JSONObject(jsonResponse);
//                jsonArray = jsonObject.getJSONArray("features");
//
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    jsonEarthquake = jsonArray.getJSONObject(i).getJSONObject("properties");
//                    earthquakes.add(new Earthquake(
//                            jsonEarthquake.getString("mag"),
//                            jsonEarthquake.getString("place"),
//                            new Date(Long.parseLong(jsonEarthquake.getString("time"))),
//                            jsonEarthquake.getString("url"),
//                            jsonEarthquake.getString("felt"),
//                            jsonEarthquake.getString("cdi")
//                    ));
//                }
//
//
//            } catch (JSONException e) {
//                // If an error is thrown when executing any of the above statements in the "try" block,
//                // catch the exception here, so the app doesn't crash. Print a log message
//                // with the message from the exception.
//                Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
//            }
//
//            // Return the list of earthquakes
//            return earthquakes;
//        }
//
//    }
//
//    /**
//     * Update the screen to display information from the given {@link ArrayList<Earthquake}.
//     */
//    private void updateUi(ArrayList<Earthquake> earthquakes) {
//        // Find a reference to the {@link ListView} in the layout
//        ListView earthquakeListView = (ListView) findViewById(R.id.list);
//
//        // Create a new {@link ArrayAdapter} of earthquakes
//        final EarthquakeAdapter adapter =
//                new EarthquakeAdapter(this,
//                        R.layout.earthquake_activity, earthquakes);
//
//        // Set the adapter on the {@link ListView}
//        // so the list can be populated in the user interface
//        earthquakeListView.setAdapter(adapter);
//
//        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
////                // Find the current earthquake that was clicked on
////                Earthquake currentEarthquake = adapter.getItem(position);
////
////                // Convert the String URL into a URI object (to pass into the Intent constructor)
////                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
////
////                // Create a new intent to view the earthquake URI
////                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
////
////                // Send the intent to launch a new activity
////                startActivity(websiteIntent);
//
//                Earthquake currentEarthquake = adapter.getItem(position);
//                String title = currentEarthquake.getLocation();
//                String number_of_people = currentEarthquake.getNumberOfPeople();
//                String perceived_magnitude = currentEarthquake.getPerceivedStrength();
//                Intent intent = new Intent(EarthquakeActivity.this, EarthQuakeFeltActivity.class);
//
//
//                intent.putExtra("title", title);
//                intent.putExtra("number_of_people", number_of_people);
//                intent.putExtra("perceived_magnitude", perceived_magnitude);
//
//                System.out.println(currentEarthquake);
//
//                startActivity(intent);
//
//
//            }
//        });
//    }
