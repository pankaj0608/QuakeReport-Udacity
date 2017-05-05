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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        // Create a fake list of earthquake locations.
        ArrayList<Earthquake> earthquakes = new ArrayList<Earthquake>();
        earthquakes.add(
                new Earthquake("7.0","San Francisco",
                        simpleDateFormat.format(new Date())));

        earthquakes.add(
                new Earthquake("6.0","London",
                        simpleDateFormat.format(new Date())));

        earthquakes.add(
                new Earthquake("9.0","Tokyo",
                        simpleDateFormat.format(new Date())));

        earthquakes.add(
                new Earthquake("7.0","Mexico City",
                        simpleDateFormat.format(new Date())));

        earthquakes.add(
                new Earthquake("4.0","Moscow",
                        simpleDateFormat.format(new Date())));

        earthquakes.add(
                new Earthquake("6.0","Rio de Janeiro",
                        simpleDateFormat.format(new Date())));

        earthquakes.add(
                new Earthquake("7.0","Paris",
                        simpleDateFormat.format(new Date())));


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        EarthquakeAdapter adapter =
                new EarthquakeAdapter(this,
                        R.layout.earthquake_activity, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
    }
}
