package com.howest.nmct.bob.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.howest.nmct.bob.MainActivity;
import com.howest.nmct.bob.R;
import com.howest.nmct.bob.models.User;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * illyism
 * 21/10/15
 */
public class ProfileFragment extends Fragment {
    @Bind(R.id.imgProfile) ImageView imgProfile;
    @Bind(R.id.tvProfileName) TextView tvProfileName;
    @Bind(R.id.tvProfilePhone) TextView tvProfilePhone;
    @Bind(R.id.tvProfileEmail) TextView tvProfileEmail;
    @Bind(R.id.tvProfileAddress) TextView tvProfileAddress;
    @Bind(R.id.tvProfileAboutMe) TextView tvProfileAboutMe;
    @Bind(R.id.tvProfileCarType) TextView tvProfileCarType;
    @Bind(R.id.tvProfileCarLicense) TextView tvProfileCarLicense;
    private MainActivity mActivity;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_profile, container, false);
        ButterKnife.bind(this, view);
        initViews();
        mActivity = (MainActivity) getActivity();
        return view;
    }

    private void initViews() {
        Picasso p = Picasso.with(getActivity());
        p.setIndicatorsEnabled(true);
        p.load(mActivity.mUser.getPicture())
                .fit()
                .centerCrop()
                .into(imgProfile);
    }

}
