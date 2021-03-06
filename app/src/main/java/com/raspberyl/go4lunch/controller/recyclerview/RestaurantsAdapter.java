package com.raspberyl.go4lunch.controller.recyclerview;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.GsonBuilder;
import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.controller.activities.MainActivity;
import com.raspberyl.go4lunch.model.firebase.User;
import com.raspberyl.go4lunch.model.googleplaces.Result;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsViewHolder> {

    // List of Restaurants
    private List<Result> mRestaurantList;
    private Context mContext;
    private double longitude;
    private double latitude;


    // Constructors
    public RestaurantsAdapter(List<Result> mRestaurantList, Context mContext) {
        this.mRestaurantList = mRestaurantList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.restaurant_view, parent, false);
        RestaurantsViewHolder vHolder = new RestaurantsViewHolder(itemView);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder holder, int position) {

        final Result result = mRestaurantList.get(position);

        longitude = MainActivity.longitude;
        latitude = MainActivity.latitude;

        //Bind here

        // Restaurant name
        holder.name.setText(result.getName());

        // Restaurant address
        String vicinity = result.getVicinity();
        String displayedAddress = vicinity.split(",")[0];
        holder.address.setText(displayedAddress);

        // Restaurant opening/closing times

        try {
            boolean isOpenNow = result.getOpeningHours().getOpenNow();
            if (isOpenNow) {
                holder.openingTimes.setText("Open Now");
            } else {
                holder.openingTimes.setText("Closed Now");
            }

        } catch (NullPointerException e) {
            holder.openingTimes.setText("Error?");
        }

        // Restaurant distance (m)
        // Set current lat/long location
        Location current_location = new Location("locationA");
        current_location.setLatitude(latitude);
        current_location.setLongitude(longitude);
        // Set targeted lat/long location
        Location restaurant_location = new Location("locationB");
        restaurant_location.setLatitude(result.getGeometry().getLocation().getLat());
        restaurant_location.setLongitude(result.getGeometry().getLocation().getLng());
        // Calculate distance between A and B locations (m)
        double distance = current_location.distanceTo(restaurant_location);
        // Round value to int (m)
        distance = Math.round(distance * 1);
        String roundedDistance = String.valueOf((int) distance);
        String displayedDistance = roundedDistance + "m";
        // Bind to holder
        holder.distance.setText(displayedDistance);


        // Restaurant workmates
        String numberOfWorkmates = String.valueOf(result.getNumberOfPeopleJoining());
        String displayedNumberOfWorkmates = "(" + numberOfWorkmates + ")";

        if (result.getNumberOfPeopleJoining() != 0) {
            holder.workmateNumber.setText(displayedNumberOfWorkmates);
        } else {
            holder.workmateNumber.setVisibility(View.INVISIBLE);
            holder.workmatePicture.setVisibility(View.INVISIBLE);
        }

        // Restaurant likes
        // 1 Star: > 1 && <=5
        // 2 Stars: >5 && <=10
        // 3 Stars: >10
        if (result.getNumberOfLikes() == 0) {
            holder.star1.setVisibility(View.INVISIBLE);
            holder.star2.setVisibility(View.INVISIBLE);
            holder.star3.setVisibility(View.INVISIBLE);
        }
        if (result.getNumberOfLikes() >= 1 && result.getNumberOfLikes() <= 5) {
            holder.star1.setVisibility(View.INVISIBLE);
            holder.star2.setVisibility(View.INVISIBLE);
        }
        if (result.getNumberOfLikes() > 5 && result.getNumberOfLikes() <= 10) {
            holder.star1.setVisibility(View.INVISIBLE);
        }
        if (result.getNumberOfLikes() > 10) {

        }

        // Restaurant picture
        if (result.getPhotos().get(0).getPhotoReference() != null) {
            if (!result.getPhotos().get(0).getPhotoReference().isEmpty()) {
                String restaurantPictureUrl = "https://maps.googleapis.com/maps/api/place/photo" +
                        "?maxwidth=100" + "&maxheight=100" +
                        "&photoreference=" + result.getPhotos().get(0).getPhotoReference() +
                        "&key=AIzaSyDqefrTQHVLLodQoTiQWHpIWRUofSV1SUw";

                Glide.with(mContext)
                        .load(restaurantPictureUrl)
                        .into(holder.picture);
            }else{
                Glide.with(mContext)
                        .load(R.drawable.unknown_userpicture)
                        .into(holder.picture);
            }
        }
    }

    @Override
    public int getItemCount() {

        return mRestaurantList.size();
    }

    // Itemclick?
    public Result getRestaurantPosition(int position){
        return this.mRestaurantList.get(position);
    }


}

