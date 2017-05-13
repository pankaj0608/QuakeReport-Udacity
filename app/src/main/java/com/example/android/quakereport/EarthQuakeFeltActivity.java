package com.example.android.quakereport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class EarthQuakeFeltActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_quake_felt);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String number_of_people = intent.getStringExtra("number_of_people");
        String perceived_magnitude = intent.getStringExtra("perceived_magnitude");

        // Capture the layout's TextView and set the string as its text
        TextView textTitle = (TextView) findViewById(R.id.title);
        textTitle.setText(title);

        TextView textNumberOfPeople = (TextView) findViewById(R.id.number_of_people);
        textNumberOfPeople.setText(getString(R.string.num_people_felt_it, number_of_people));

        TextView textPerceivedMag = (TextView) findViewById(R.id.perceived_magnitude);
        textPerceivedMag.setText(perceived_magnitude);
    }
}
