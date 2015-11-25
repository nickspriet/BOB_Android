package com.howest.nmct.bob.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.models.Event;

/**
 * illyism
 * 16/11/15
 */
public class CreateRideDialogFragment extends DialogFragment {
    private RideOptionSelectedListener mListener;
    private Event mEvent;

    public static CreateRideDialogFragment newInstance(RideOptionSelectedListener listener, Event event) {
        CreateRideDialogFragment fragment = new CreateRideDialogFragment();
        fragment.mListener = listener;
        fragment.mEvent = event;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.are_you_bob)
                .setItems(R.array.bob_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                mListener.onDialogBobClick(mEvent);
                            case 1:
                                mListener.onDialogNotBobClick(mEvent);
                        }
                    }
                });
        return builder.create();
    }

    public interface RideOptionSelectedListener {
        void onDialogBobClick(Event mEvent);
        void onDialogNotBobClick(Event mEvent);
    }
}
