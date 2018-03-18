package edu.sjsu.prashant.whatsfordinner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.prashant.whatsfordinner.model.NewDish;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFrag extends Fragment {
    private final static String new_dish_filename = "/new_dish_entry.dat";
    List<NewDish> newDishList = new ArrayList<>();


    public DetailsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

}
