package com.howest.nmct.bob.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.activities.EditProfileActivity;
import com.howest.nmct.bob.models.User;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * illyism
 * 09/12/15
 */
public class EditProfileFragment extends Fragment {
    @Bind(R.id.tvProfileAboutMe) EditText etProfileAboutMe;
    @Bind(R.id.etMobile) EditText etMobile;
    @Bind(R.id.etCarModel) AutoCompleteTextView etCarModel;
    @Bind(R.id.etCarNo) EditText etCarNo;

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

    public void initViews() {
        EditProfileActivity parentActivity = (EditProfileActivity) getActivity();
        User user = parentActivity.getUser();
        if (user == null) return;

        if (!user.getAboutMe().isEmpty()) {
            etProfileAboutMe.setText(user.getAboutMe());
        }

        if (!user.getMobile().isEmpty()) {
            etMobile.setText(user.getMobile());
        }

        ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.car_makes));
        etCarModel.setAdapter(modelAdapter);

        if (!user.getCarModel().isEmpty()) {
            etCarModel.setText(user.getCarModel());
        }

        if (!user.getCarNo().isEmpty()) {
            etCarNo.setText(user.getCarNo());
        }
    }

    /**
     * Get the User from the parent activity and add the values from the EditTexts
     * @return User The user with new values
     */
    public User getUser() {
        EditProfileActivity parentActivity = (EditProfileActivity) getActivity();
        User user = parentActivity.getUser();

        user.setAboutMe(etProfileAboutMe.getText().toString());
        user.setMobile(etMobile.getText().toString());
        user.setCarModel(etCarModel.getText().toString());
        user.setCarNo(etCarNo.getText().toString());

        return user;
    }
}
