package com.howest.nmct.bob.fragments;

<<<<<<< HEAD
import android.net.Uri;
=======
>>>>>>> origin/master
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.models.Ride;
import com.poliveira.apps.parallaxlistview.ParallaxScrollView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * illyism
 * 22/10/15
 */
public class RideDetailsFragment extends Fragment {
//    @Bind(R.id.text)
//    TextView textView;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.ride_image)
    ImageView imageView;
    private Ride ride;

    public RideDetailsFragment() {
    }

    public static RideDetailsFragment newInstance(Ride ride) {
        RideDetailsFragment fragment = new RideDetailsFragment();
        fragment.setRide(ride);
        return fragment;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_ride_details, container, false);

        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        if (ride == null) return;

        //textView.setText(ride.getTitle());
        address.setText(ride.getAddress());
        date.setText(ride.getDate());


//        Picasso p = Picasso.with(getActivity());
//        p.setIndicatorsEnabled(true);
//        p.load(ride.getImage())
//                .fit()
//                .centerCrop()
//                .into(imageView);

    }
}
