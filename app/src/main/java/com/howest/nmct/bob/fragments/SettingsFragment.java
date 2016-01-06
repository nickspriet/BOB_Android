package com.howest.nmct.bob.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.sync.BackendSyncAdapter;

/**
 * illyism
 * 20/12/15
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference button = findPreference(getString(R.string.clear_data));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int count = BackendSyncAdapter.clearData(getActivity());
                Toast.makeText(getActivity(), String.format("%d items cleared.", count), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
