package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by pankaj on 5/4/2017.
 */
public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
    private SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("h:m a");

    public EarthquakeAdapter(Activity context, int resource, ArrayList<Earthquake> earthquakes) {
        super(context, resource, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View listItemView = convertView;

        try {
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_item, parent, false);
            }

            Earthquake currentEarthquake = (Earthquake) getItem(position);

            TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitude);
            magnitudeTextView.setText(
                    formatMagnitude(Double.parseDouble(currentEarthquake.getMagnitude())));

            // Set the proper background color on the magnitude circle.
            // Fetch the background from the TextView, which is a GradientDrawable.
            GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();

            // Get the appropriate background color based on the current earthquake magnitude
            int magnitudeColor = getMagnitudeColor(
                    Double.parseDouble(currentEarthquake.getMagnitude()));

            // Set the color on the magnitude circle
            magnitudeCircle.setColor(magnitudeColor);

            TextView locationOffsetTextView = (TextView) listItemView.findViewById(R.id.location_offset);
            locationOffsetTextView.setText(currentEarthquake.getLocation().
                    substring(0, currentEarthquake.getLocation().indexOf("of") + 2));

            TextView primaryLocationTextView = (TextView) listItemView.findViewById(R.id.primary_location);

            if (currentEarthquake.getLocation().length() > 12) {
                primaryLocationTextView.setText(
                        currentEarthquake.getLocation().substring(12, currentEarthquake.getLocation().length()));
            } else {
                primaryLocationTextView.setText(
                        currentEarthquake.getLocation());
            }


            TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
            dateTextView.setText(simpleDateFormat.format(currentEarthquake.getTime()));

            TextView timeTextView = (TextView) listItemView.findViewById(R.id.time);
            timeTextView.setText(simpleTimeFormat.format(currentEarthquake.getTime()));

        } catch (Exception e) {
            Log.e("getView ", e.getMessage());
        }

        return listItemView;
    }

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
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
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

}
