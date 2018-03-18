package edu.sjsu.prashant.whatsfordinner;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.widget.ArrayAdapter;

import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.sjsu.prashant.whatsfordinner.model.NewDish;
import edu.sjsu.prashant.whatsfordinner.util.DishUtils;

public class GroceriesActivity extends AppCompatActivity {
    private static final String TAG = GroceriesActivity.class.getSimpleName();

    private static final String new_dish_filename = "/new_dish_entry.dat";
    final static String groceries_filename = "/groceries.dat";

    private static final String SEPERATOR = "::";

    SwipeMenuListView groceryListView;

    List<NewDish> newDishList = new ArrayList<>();
    HashMap<String, Integer> listIngredientMap = new HashMap<>();
    List<String> groceriesList = new ArrayList<>();
    private ArrayAdapter<String> groceriesAdapter;
    List<String> tempList;

    DishUtils duObj = new DishUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries);
        groceryListView = findViewById(R.id.lv_groceries);

//        File file = new File(this.getFilesDir().getPath() + groceries_filename);
//        if (file.exists() && !file.isDirectory()) {
            listIngredientMap = duObj.readGroceriesMap(this, groceries_filename);
//        } else {
//            newDishList = duObj.readDishList(this, new_dish_filename);
//            for (NewDish dish : newDishList) {
//                Toast.makeText(this, dish.getDish_name(), Toast.LENGTH_SHORT).show();
//                for (String ingredient : dish.getIngredient_list()) {
//                    if (listIngredientMap.get(ingredient) != null) {
//                        listIngredientMap.put(ingredient, listIngredientMap.get(ingredient) + 1);
//                    } else {
//                        listIngredientMap.put(ingredient, 1);
//                    }
//                }
//            }
//        }
        for (String key : listIngredientMap.keySet()) {
            groceriesList.add(listIngredientMap.get(key) + " " + key.split(SEPERATOR)[1] + "-" + key.split(SEPERATOR)[0]);
        }
        groceriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, groceriesList);
        groceryListView.setAdapter(groceriesAdapter);


        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem plusItem = new SwipeMenuItem(getApplicationContext());
                plusItem.setBackground(new ColorDrawable(Color.rgb(0, 255, 0)));
                plusItem.setWidth(150);
                plusItem.setTitle("Add");
                plusItem.setIcon(R.mipmap.ic_add);

                SwipeMenuItem minusItem = new SwipeMenuItem(getApplicationContext());
                minusItem.setBackground(new ColorDrawable(Color.rgb(255, 0, 0)));
                minusItem.setWidth(150);
                minusItem.setTitle("Remove");
                minusItem.setIcon(R.mipmap.ic_remove);

                menu.addMenuItem(minusItem);
                menu.addMenuItem(plusItem);

            }
        };

        groceryListView.setMenuCreator(creator);

        groceryListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                String currentText = groceriesAdapter.getItem(position);
                String[] newTextSplit = getUpdatedText(currentText, index);

                String newText = newTextSplit[0] + " " + newTextSplit[1];
                if (Integer.valueOf(newTextSplit[0]) != 0) {
                    //Do Nothing
                } else {
                    //Strike-through Text
                    SpannableString str =str = new SpannableString(newText);
                    str.setSpan(new StrikethroughSpan(), 0 , newText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                Toast.makeText(getApplicationContext(), "position1- " + newTextSplit[0] + ", index- " + index, Toast.LENGTH_SHORT).show();
                groceriesList.remove(position);
                groceriesList.add(position, newText);
                tempList = new ArrayList<>();
                tempList.addAll(groceriesList);
                Log.d(TAG, String.valueOf(groceriesList.size()));

                groceriesAdapter.clear();

                groceriesAdapter.addAll(tempList);
                Log.d(TAG, tempList.get(position));
                groceriesAdapter.notifyDataSetChanged();


                Toast.makeText(getApplicationContext(), "position2- " + newTextSplit[0] + ", index- " + index, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HashMap<String, Integer> groceriesMap = new HashMap<>();

        if (tempList != null) {
            for (String text : tempList) {
                String[] temp = text.split("-");
                String key = temp[1] + SEPERATOR + temp[0].split(" ")[1];
                Integer value = Integer.valueOf(temp[0].split(" ")[0]);
                groceriesMap.put(key, value);
                duObj.writeGroceriesMap(this, groceries_filename, groceriesMap);
            }
        }

    }

    public String[] getUpdatedText(String currentText, Integer buttonIndex) {
        String[] textSplit = currentText.split(" ");
        Integer currentItemCount = Integer.valueOf(textSplit[0]);
        switch (buttonIndex) {
            case 0:
                Log.d(TAG, String.valueOf(currentItemCount));
                if (currentItemCount > 0)
                    currentItemCount--;
                break;
            case 1:
                Log.d(TAG, String.valueOf(currentItemCount));

                currentItemCount++;
                break;
            default:
                Log.e(TAG, "Incorrect Option Encountered in getUpdatedText Method");
        }
        textSplit[0] = currentItemCount.toString();
        return textSplit;
    }
}
