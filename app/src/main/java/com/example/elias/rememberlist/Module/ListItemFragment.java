package com.example.elias.rememberlist.Module;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elias.rememberlist.R;

/**
 * Created by elias on 01-12-2016.
 */

public class ListItemFragment extends Fragment {


    onButtonClickedListener mCallback;

    public interface onButtonClickedListener{
        public void onAddClick();
        public void onDeleteClick();
    }

    public ListItemFragment(){
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (onButtonClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onButtonClickListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return  inflater.inflate(R.layout.shopping_list_items_fragment, container, false);
    }

}
