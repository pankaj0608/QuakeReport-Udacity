package com.example.android.quakereport;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by pankaj on 5/4/2017.
 */
public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");

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

        TextView intensityTextView = (TextView) listItemView.findViewById(R.id.earthquake_intensity);
        intensityTextView.setText(currentEarthquake.getIntensity());

        TextView locationTextView = (TextView) listItemView.findViewById(R.id.earthquake_location);
        locationTextView.setText(currentEarthquake.getLocation());

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.earthquake_time);
        timeTextView.setText(simpleDateFormat.format(currentEarthquake.getTime()));

        return listItemView;
    }
}
