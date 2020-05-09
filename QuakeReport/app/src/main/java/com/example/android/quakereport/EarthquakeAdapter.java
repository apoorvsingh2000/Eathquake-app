package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter {

    //constructor for the custom array adapter
    public EarthquakeAdapter(Activity context , ArrayList<Earthquake> earthquakes) {
        super(context,0,earthquakes);
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd,yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    //get he color for the magnitude circle
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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //check if the existing view is being reused, otherwise inflate the view
        View listItemview = convertView;
        if (listItemview == null) {
            listItemview = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        //Get the earthquake object located at this position in the list
        Earthquake currentEarthquake = (Earthquake) getItem(position);

        //magnitude as a decimal value using DecimalFormat
        DecimalFormat formatter = new DecimalFormat("0.0");
        String output = formatter.format(currentEarthquake.getMagnitude());

        //setting the magnitude of the earthquake in the text view
        TextView magnitudeteTextView = (TextView) listItemview.findViewById(R.id.magnitude);
        magnitudeteTextView.setText(output);

        /**
         * Strings of location and time
         */
        String directionString="";
        String locationString="";

        //splitting the string
        if (currentEarthquake.getLocation().contains("of")) {
            directionString = currentEarthquake.getLocation().substring(0, currentEarthquake.getLocation().indexOf("of") + 2);
            locationString = currentEarthquake.getLocation().substring(currentEarthquake.getLocation().indexOf("of") + 3);
        }
        else {
            directionString = getContext().getString(R.string.near_the);
            locationString = currentEarthquake.getLocation();
        }

        //setting the direction of the earthquake in the text view
        TextView directionTextView = (TextView) listItemview.findViewById(R.id.direction);
        directionTextView.setText(directionString);

        //setting the location of the earthquake in the text view
        TextView locationTextView = (TextView) listItemview.findViewById(R.id.location);
        locationTextView.setText(locationString);

        /**
         * Date object
         */
        Date dateObject = new Date(currentEarthquake.getTimeInMilliseconds());
        String formattedDate = formatDate(dateObject);
        String formattedTime = formatTime(dateObject);

        //setting the date of the earthquake in the text view
        TextView dateTextView = (TextView) listItemview.findViewById(R.id.date);
        dateTextView.setText(formattedDate);

        //setting the time of the earthquake in the text view
        TextView timeTextView = (TextView) listItemview.findViewById(R.id.time);
        timeTextView.setText(formattedTime);

        /**
         * Setting up the respective color values for the magnitude circles
         */
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeteTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return listItemview;
    }
}
