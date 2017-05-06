package com.example.android.quakereport;

import android.app.Activity;
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
        super(context,resource,earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Earthquake currentEarthquake = (Earthquake) getItem(position);

        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitude);
        magnitudeTextView.setText(
                formatMagnitude(Double.parseDouble(currentEarthquake.getMagnitude())));

        TextView locationTextView = (TextView) listItemView.findViewById(R.id.location);
        locationTextView.setText(currentEarthquake.getLocation());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        dateTextView.setText(simpleDateFormat.format(currentEarthquake.getTime()));

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time);
        timeTextView.setText(simpleTimeFormat.format(currentEarthquake.getTime()));

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
}
