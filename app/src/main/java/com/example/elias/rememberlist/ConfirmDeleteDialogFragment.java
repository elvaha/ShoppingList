package com.example.elias.rememberlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by elias on 06-10-2016.
 */

public class ConfirmDeleteDialogFragment extends DialogFragment {

    OnPositiveListener mCallback;

    // Container Activity must implement this interface
    public interface OnPositiveListener {
        public void onPositiveClicked();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnPositiveListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPositiveListener");
        }
    }

    public ConfirmDeleteDialogFragment(){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete");
        builder.setPositiveButton("Yes", pListener);
        builder.setNegativeButton("Cancel", nListener);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    DialogInterface.OnClickListener pListener = new Dialog.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            // FIRE ZE MISSILES!
            positiveClick();
        }
    };

    DialogInterface.OnClickListener nListener = new Dialog.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            // FIRE ZE MISSILES!
            negativeClick();
        }
    };

    protected void positiveClick(){
        mCallback.onPositiveClicked();
    }

    protected void negativeClick(){

    }


}
