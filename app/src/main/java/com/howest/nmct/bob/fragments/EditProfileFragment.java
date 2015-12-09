package com.howest.nmct.bob.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.activities.EditProfileActivity;
import com.howest.nmct.bob.models.User;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * illyism
 * 09/12/15
 */
public class EditProfileFragment extends Fragment {
    @Bind(R.id.imgProfile) ImageView imgProfile;
    @Bind(R.id.tvProfileAboutMe) TextView tvProfileAboutMe;

    public EditProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_profile_edit, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        EditProfileActivity parentActivity = (EditProfileActivity) getActivity();
        User user = parentActivity.getUser();

        Picasso p = Picasso.with(getActivity());
        p.setIndicatorsEnabled(true);

        p.load(user.getPicture())
                .fit()
                .centerCrop()
                .into(imgProfile);

        if (!user.getAboutMe().isEmpty()) {
            tvProfileAboutMe.setText(user.getAboutMe());
        }
    }

    /**
     * Get the User from the parent activity and add the values from the EditTexts
     * @return User The user with new values
     */
    public User getUser() {
        EditProfileActivity parentActivity = (EditProfileActivity) getActivity();
        User user = parentActivity.getUser();

        user.setAboutMe(tvProfileAboutMe.getText().toString());

        return user;
    }
}
