package edu.sjsu.prashant.whatsfordinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import edu.sjsu.prashant.whatsfordinner.model.NewDish;
import edu.sjsu.prashant.whatsfordinner.util.DishUtils;

public class GroceriesActivity extends AppCompatActivity {
    private static final String TAG = GroceriesActivity.class.getSimpleName();

    List<NewDish> newDishList = new ArrayList<>();
    final static String new_dish_filename = "/new_dish_entry.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries);


        DishUtils duObj = new DishUtils();
        newDishList = duObj.readDishList(this, new_dish_filename);

        for(NewDish dish : newDishList){
            for (String ingredient: dish.getIngredient_list()){
                Log.d(TAG, ingredient);
            }
        }
    }
}
