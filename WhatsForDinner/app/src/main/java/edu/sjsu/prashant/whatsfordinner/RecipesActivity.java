package edu.sjsu.prashant.whatsfordinner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.sjsu.prashant.whatsfordinner.model.NewDish;
import edu.sjsu.prashant.whatsfordinner.util.DishUtils;

public class RecipesActivity extends AppCompatActivity implements ListFrag.RecipeSelectedListener {
    private static final String TAG = RecipesActivity.class.getSimpleName();

    private final static String new_dish_filename = "/new_dish_entry.dat";
    private final static String new_groceries_filename = "/new_groceries.dat";
    private final static String meals_filename = "/meals.dat";

    private static final String SEPERATOR = "::";

    List<NewDish> newDishList = new ArrayList<>();
    HashMap<String, Integer> listIngredientMap;
    HashMap<String, Integer> mealsMap;

    TextView tv_recipeName;
    ListView lv_ingredientList;
    TextView tv_recipeDescription;
    ImageView iv_foodIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        tv_recipeName = findViewById(R.id.tv_recipes_recipeName);
        lv_ingredientList = findViewById(R.id.lv_recipes_ingredientList);
        tv_recipeDescription = findViewById(R.id.tv_recipes_recipeDescription);
        iv_foodIcon = findViewById(R.id.iv_recipes_foodIcon);

        DishUtils duObj = new DishUtils();
        newDishList = duObj.readDishList(this, new_dish_filename);

        if (findViewById(R.id.recipes_layout_default) != null) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .hide(fragmentManager.findFragmentById(R.id.detailsFrag))
                    .show(fragmentManager.findFragmentById(R.id.listFrag))
                    .commit();

            listIngredientMap = duObj.readGroceriesMap(this, new_groceries_filename);
            mealsMap = duObj.readMealsMap(this, meals_filename);
        }

        if (findViewById(R.id.recipes_layout_land) != null) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .show(fragmentManager.findFragmentById(R.id.detailsFrag))
                    .show(fragmentManager.findFragmentById(R.id.listFrag))
                    .commit();
        }
    }

    @Override
    public void onRecipeSelected(int index) {
        ArrayList<String> dish_descriptions = new ArrayList<>();
        ArrayList<String> dish_name = new ArrayList<>();
        ArrayList<String> dish_foodIcon = new ArrayList<>();
        ArrayList<List<String>> dish_ingredients = new ArrayList<>();


        for (NewDish dish : newDishList) {
            dish_name.add(dish.getDish_name());
            dish_descriptions.add(dish.getDish_description());
            dish_foodIcon.add(dish.getDish_image_path());
            List<String> tempList = new ArrayList<>();
            for(String ingredientName : dish.getIngredient_list()){
                tempList.add(ingredientName.split(SEPERATOR)[0]);
            }
            dish_ingredients.add(tempList);
        }

        if (findViewById(R.id.recipes_layout_default) != null) {
// --> Show the recipe details onClick
//            FragmentManager fragmentManager = this.getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .hide(fragmentManager.findFragmentById(R.id.listFrag))
//                    .show(fragmentManager.findFragmentById(R.id.detailsFrag))
//                    .addToBackStack(null)
//                    .commit();


            for (String ingredient : newDishList.get(index).getIngredient_list()) {
                if (listIngredientMap.get(ingredient) != null) {
                    listIngredientMap.put(ingredient, listIngredientMap.get(ingredient) + 1);
                } else {
                    listIngredientMap.put(ingredient, 1);
                }
            }

            DishUtils duObj = new DishUtils();
            duObj.writeGroceriesMap(this, new_groceries_filename, listIngredientMap);


            if (mealsMap.get(newDishList.get(index).getDish_name()) != null) {
                mealsMap.put(newDishList.get(index).getDish_name(), mealsMap.get(newDishList.get(index).getDish_name()) + 1);
            } else {
                mealsMap.put(newDishList.get(index).getDish_name(), 1);
            }

            duObj.writeMealsMap(this, meals_filename, mealsMap);
        }
        String[] dish_descriptions_array = new String[dish_descriptions.size()];
        dish_descriptions_array = dish_descriptions.toArray(dish_descriptions_array);

        String[] dish_name_array = new String[dish_name.size()];
        dish_name_array = dish_name.toArray(dish_name_array);

        String[] dish_foodIcon_array = new String[dish_foodIcon.size()];
        dish_foodIcon_array = dish_foodIcon.toArray(dish_foodIcon_array);

//        List<String>[] dish_ingredients_array = new List[dish_ingredients.size()];
//        dish_ingredients_array =  dish_ingredients.toArray(dish_ingredients_array);

        tv_recipeName.setText(dish_name_array[index]);
        tv_recipeDescription.setText(dish_descriptions_array[index]);
        Toast.makeText(this, dish_foodIcon_array[index], Toast.LENGTH_LONG).show();
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        5551);
            } else {
                if (!dish_foodIcon_array[index].contains("drawable:")) {
                    iv_foodIcon.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(dish_foodIcon_array[index]))));
                } else {
                    Toast.makeText(this, Integer.valueOf(dish_foodIcon_array[index].split("://")[1]), Toast.LENGTH_LONG).show();
                    iv_foodIcon.setImageDrawable(getResources().getDrawable(Integer.valueOf(dish_foodIcon_array[index].split("://")[1])));
                }
            }
        } catch (FileNotFoundException ex) {
            Log.d(TAG, "File Not Found!");
        }

        lv_ingredientList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dish_ingredients.get(index)));
    }
}
