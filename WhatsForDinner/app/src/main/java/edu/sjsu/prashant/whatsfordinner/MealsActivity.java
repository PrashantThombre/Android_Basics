package edu.sjsu.prashant.whatsfordinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.sjsu.prashant.whatsfordinner.util.DishUtils;

public class MealsActivity extends AppCompatActivity {

    private final static String meals_filename = "/meals.dat";
    HashMap<String, Integer> mealsMap;
    List<String> mealsList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    Spinner spinner_monday_breakfast;
    Spinner spinner_monday_lunch;
    Spinner spinner_monday_dinner;

    int monday_breakfast_selected;
    int monday_lunch_selected;
    int monday_dinner_selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);

        DishUtils duObj = new DishUtils();
        mealsMap = duObj.readMealsMap(this, meals_filename);

        mealsList.add("Eating Out");
        for (String key : mealsMap.keySet()) {
            mealsList.add(key + " - " + mealsMap.get(key));
        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mealsList);

        spinner_monday_breakfast = findViewById(R.id.sp_monday_breakfast);
        spinner_monday_lunch = findViewById(R.id.sp_monday_lunch);
        spinner_monday_dinner = findViewById(R.id.sp_monday_dinner);

        spinner_monday_breakfast.setAdapter(arrayAdapter);
        spinner_monday_lunch.setAdapter(arrayAdapter);
        spinner_monday_dinner.setAdapter(arrayAdapter);

        monday_breakfast_selected = spinner_monday_breakfast.getSelectedItemPosition();
        monday_lunch_selected = spinner_monday_lunch.getSelectedItemPosition();
        monday_dinner_selected = spinner_monday_dinner.getSelectedItemPosition();

        spinner_monday_breakfast.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("--->", adapterView.getItemAtPosition(position).toString());
                ArrayAdapter<String> tempAdapter = updateArrayAdapter(adapterView, position, monday_breakfast_selected);
                spinner_monday_breakfast.setAdapter(tempAdapter);
                monday_breakfast_selected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_monday_lunch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("--->", adapterView.getItemAtPosition(position).toString());
                ArrayAdapter<String> tempAdapter = updateArrayAdapter(adapterView, position, monday_lunch_selected);
                spinner_monday_lunch.setAdapter(tempAdapter);
                monday_lunch_selected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_monday_dinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("--->", adapterView.getItemAtPosition(position).toString());
                ArrayAdapter<String> tempAdapter = updateArrayAdapter(adapterView, position, monday_dinner_selected);
                spinner_monday_dinner.setAdapter(tempAdapter);
                monday_dinner_selected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public ArrayAdapter<String> updateArrayAdapter(AdapterView<?> adapterView, int position, int previous){
        List<String> tempMealsList2 = new ArrayList<>();
        tempMealsList2.addAll(mealsList);
        if(previous != 0){
            String mapKey = adapterView.getItemAtPosition(previous).toString().split(" ")[0];
            mealsMap.put(mapKey , mealsMap.get(mapKey ) + 1);
            List<String> tempMealsList = new ArrayList<>();
            tempMealsList.add("Eating Out");
            tempMealsList2.clear();
            for (String key : mealsMap.keySet()) {
                tempMealsList.add(key + " - " + mealsMap.get(key));
            }
            tempMealsList2.addAll(tempMealsList);
            arrayAdapter.clear();
            arrayAdapter.addAll(tempMealsList);
            arrayAdapter.notifyDataSetChanged();
        }
        if(position != 0){
            String mapKey = adapterView.getItemAtPosition(position).toString().split(" ")[0];
            mealsMap.put(mapKey , mealsMap.get(mapKey ) - 1);
            List<String> tempMealsList = new ArrayList<>();
            tempMealsList.add("Eating Out");
            tempMealsList2.clear();
            for (String key : mealsMap.keySet()) {
                tempMealsList2.add(key + " - " + mealsMap.get(key));
                if( mealsMap.get(key) != 0)
                    tempMealsList.add(key + " - " + mealsMap.get(key));
            }

            arrayAdapter.clear();
            arrayAdapter.addAll(tempMealsList);
            arrayAdapter.notifyDataSetChanged();
        }
        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,tempMealsList2);
    }

}
