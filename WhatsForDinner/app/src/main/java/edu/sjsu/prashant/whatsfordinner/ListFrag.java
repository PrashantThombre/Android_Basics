package edu.sjsu.prashant.whatsfordinner;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.prashant.whatsfordinner.model.NewDish;
import edu.sjsu.prashant.whatsfordinner.util.DishUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFrag extends ListFragment {
    private final static String new_dish_filename = "/new_dish_entry.dat";
    List<NewDish> newDishList = new ArrayList<>();

    private RecipeSelectedListener recipeSelectedListener;
    public ListFrag() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DishUtils duObj = new DishUtils();
        newDishList = duObj.readDishList(this.getActivity(), new_dish_filename);

        ArrayList<String> dishNameList = new ArrayList<>();
        for(NewDish dish : newDishList){
            dishNameList.add(dish.getDish_name());
        }
        setListAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1,dishNameList ) );
        //If the phone is in landscape mode --> i.e. layout_default is null, then click on the first item
        if(this.getActivity().findViewById(R.id.recipes_layout_default) == null){
        recipeSelectedListener.onRecipeSelected(0);
        }
    }

    public interface RecipeSelectedListener{
        public void onRecipeSelected(int index);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            recipeSelectedListener = (RecipeSelectedListener) context;
        }catch (ClassCastException ex){
            throw new ClassCastException(context.toString() + " Must Implement The Interface Called - recipeSelectedListener.!");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        recipeSelectedListener.onRecipeSelected(position);
    }
}
