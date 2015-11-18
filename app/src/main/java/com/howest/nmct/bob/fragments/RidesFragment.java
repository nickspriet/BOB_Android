package com.howest.nmct.bob.fragments;

import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.howest.nmct.bob.MainActivity;
import com.howest.nmct.bob.R;
import com.howest.nmct.bob.adapters.RideAdapter;
import com.howest.nmct.bob.collections.Rides;
import com.howest.nmct.bob.models.Ride;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * illyism
 * 21/10/15
 */
public class RidesFragment extends Fragment {
    @Bind(R.id.list)
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Nullable
    @Bind(R.id.ride_image)
    ImageView ride_image;


    public RidesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_rides, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    /**
     * Populates the Ride ArrayList
     */
    private void initData() {
        Rides.fetchData();
    }

    /**
     * Sets up the RecyclerView;
     */
    private void initViews() {
        if (mLayoutManager == null)
            mLayoutManager = new LinearLayoutManager(getActivity());

        if (mAdapter == null)
            mAdapter = new RideAdapter(this, Rides.getRides());

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
        }
    }

    public void onRideSelected(Ride ride, ImageView rideImage) {
        View v = getView();
        if (v == null) return;
        navigatetoRideDetails(ride, rideImage);


//        FrameLayout frameLayout = (FrameLayout) v.findViewById(R.id.frameLayout);
//        if (frameLayout != null) {
//            ((MainActivity) getActivity()).navigatetoRideDetails(R.id.frameLayout, ride);
//            Display display = getActivity().getWindowManager().getDefaultDisplay();
//            Point size = new Point();
//            display.getSize(size);
//            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(size.x / 3, RecyclerView.LayoutParams.MATCH_PARENT));
//        } else {
        // ((MainActivity) getActivity()).navigatetoRideDetails(ride);
//}
    }

    public void navigatetoRideDetails(Ride ride, ImageView rideImage) {
        navigateToFragment(RideDetailsFragment.newInstance(ride), true, rideImage);
        getActivity().setTitle(ride.getTitle());
    }


    public void navigateToFragment(Fragment fragment, Boolean addToManager, ImageView rideImage) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionInflater transActivity = TransitionInflater.from(getActivity());


            //Transition transition = new AutoTransition().setDuration(6000);
            //fragment.setSharedElementEnterTransition(transition);
            //rideImage.setTransitionName("MyTransition");


            setSharedElementReturnTransition(transActivity.inflateTransition(R.transition.change_image_transform));
            setExitTransition(transActivity.inflateTransition(android.R.transition.explode));

            //Fragment rideDetailsFragment = new RideDetailsFragment();
            fragment.setSharedElementEnterTransition(transActivity.inflateTransition(R.transition.change_image_transform));
            fragment.setEnterTransition(transActivity.inflateTransition(android.R.transition.explode));
        }

        //getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .addSharedElement(rideImage, rideImage.getTransitionName())
                .addToBackStack(fragment.getClass().toString())
                .commit();
    }


    public void onRideMapClick(Ride ride) {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%s", ride.getAddress());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        getActivity().startActivity(intent);
    }
}
