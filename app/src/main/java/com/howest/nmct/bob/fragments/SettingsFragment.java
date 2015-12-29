package com.howest.nmct.bob.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.howest.nmct.bob.R;

/**
 * illyism
 * 20/12/15
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
