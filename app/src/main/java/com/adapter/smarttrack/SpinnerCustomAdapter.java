package com.adapter.smarttrack;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tracking.smarttrack.R;

import java.util.ArrayList;

public class SpinnerCustomAdapter extends ArrayAdapter<String> {

    public SpinnerCustomAdapter(Context context, ArrayList<String> vehList)
    {
        super(context, 0, vehList);
    }


    @Override
    public View getView(int position, @Nullable
            View convertView,  ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent)
    {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_textview, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.spinnerTxtView);

        // It is used the name to the TextView when the
        // current item is not null.
            textViewName.setText(getItem(position));

        // Setting Special atrributes for 1st element
        if (position == 0) {
            // Setting the size of the text
            textViewName.setTextSize(18f);
            // Setting the text Color
            textViewName.setTextColor(Color.parseColor("#284C7B"));

        }

        return convertView;
    }
}



   /* public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

// Inflating the layout for the custom Spinner
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.item_spinner_textview, parent, false);

// Declaring and Typecasting the textview in the inflated layout
        TextView tvLanguage = (TextView) layout.findViewById(R.id.spinnerTxtView);

// Setting the text using the array
        tvLanguage.setText(Languages[position]);


// Setting Special atrributes for 1st element
        if (position == 0) {
            // Setting the size of the text
            tvLanguage.setTextSize(20f);
            // Setting the text Color
            tvLanguage.setTextColor(Color.parseColor("#284C7B"));

        }

        return layout;
    }

    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // It gets a View that displays the data at the specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}*/