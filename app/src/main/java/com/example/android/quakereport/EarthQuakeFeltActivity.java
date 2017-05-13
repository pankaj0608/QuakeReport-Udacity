package com.example.android.quakereport;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) textPerceivedMag.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(
                Double.parseDouble(perceived_magnitude));

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);
    }


    /**
     * Return the color as an app int
     *
     * @param magnitude
     * @return
     */
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(this, magnitudeColorResourceId);
    }
}
