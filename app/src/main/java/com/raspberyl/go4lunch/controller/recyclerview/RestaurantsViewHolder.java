package com.raspberyl.go4lunch.controller.recyclerview;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.raspberyl.go4lunch.R;

public class RestaurantsViewHolder extends RecyclerView.ViewHolder {

    // Variables
    public TextView name, address, distance, workmateNumber, openingTimes;
    public ImageView picture, workmatePicture, star1, star2, star3;

    // Constructor
    public RestaurantsViewHolder(View view) {
        super(view);

        name = view.findViewById(R.id.restaurant_view_name);
        address = view.findViewById(R.id.restaurant_view_address);
        distance = view.findViewById(R.id.restaurant_view_distance);
        openingTimes = view.findViewById(R.id.restaurant_view_opening_times);
        workmateNumber = view.findViewById(R.id.restaurant_view_workmates_number);
        picture = view.findViewById(R.id.restaurant_view_picture);

        workmatePicture = view.findViewById(R.id.restaurant_view_workmates);
        star1 = view.findViewById(R.id.restaurant_view_stars_1);
        star2 = view.findViewById(R.id.restaurant_view_stars_2);
        star3 = view.findViewById(R.id.restaurant_view_stars_3);

    }
}
