package edu.sjsu.prashant.whatsfordinner;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import edu.sjsu.prashant.whatsfordinner.model.NewDish;
import edu.sjsu.prashant.whatsfordinner.util.DishUtils;

public class NewDishActivity extends AppCompatActivity {
    private static final String TAG = NewDishActivity.class.getSimpleName();

    static boolean isDuplicate = false;
    final static String new_dish_filename = "/new_dish_entry.dat";
    EditText et_newRecipeName;
    EditText et_newRecipeDescription;
    ImageView iv_dishIcon;
    AutoCompleteTextView acTextView;

    Integer [] ingredientIDs = {R.id.acTextView_ingredient01, R.id.acTextView_ingredient02, R.id.acTextView_ingredient03,
            R.id.acTextView_ingredient04, R.id.acTextView_ingredient05, R.id.acTextView_ingredient06,
            R.id.acTextView_ingredient07, R.id.acTextView_ingredient08, R.id.acTextView_ingredient09,
            R.id.acTextView_ingredient10};

    List<String> ingredientList = new ArrayList<String>();
    List<String> completeRecipesList = new ArrayList<String>();
    List<String> completeIngredientList = new ArrayList<String>();
    List<NewDish> newDishList = new ArrayList<NewDish>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish);

        DishUtils duObj = new DishUtils();
        newDishList = duObj.readDishList(this, new_dish_filename);


        for(NewDish newDish : newDishList) {
            Toast.makeText(this, "Hello_Toast from newDish", Toast.LENGTH_LONG).show();
            Toast.makeText(this, newDish.getDish_name(), Toast.LENGTH_LONG).show();
            completeRecipesList.add(newDish.getDish_name());
            completeIngredientList.addAll(newDish.getIngredient_list());
        }
        et_newRecipeName = findViewById(R.id.et_newRecipeName);
        et_newRecipeDescription = findViewById(R.id.et_newRecipeDescription);
        ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, completeIngredientList);
        for(Integer idVal : ingredientIDs) {
            acTextView = findViewById(idVal);
            acTextView.setAdapter(ingredientAdapter);
        }

        et_newRecipeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                recipeNameHandler(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_newRecipeName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                String currentName = et_newRecipeName.getText().toString();
                if(!hasFocus){
                    recipeNameHandler(currentName);
                }
            }
        });
    }

    public void recipeNameHandler(String currentName){
        isDuplicate = false;
        Log.d(TAG, "EditText Focus Lost: Current Value - " + currentName);
        for(String recipeName : completeRecipesList) {
            if (currentName.equalsIgnoreCase(recipeName)){
                isDuplicate = true;
                et_newRecipeName.setTextColor(getResources().getColor(R.color.RED));
                break;
            }
        }
        if(!isDuplicate){
            et_newRecipeName.setTextColor(getResources().getColor(R.color.BLACK));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_button:
                if(isDuplicate) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.Error_DuplicateRecipe);
                    builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }else{
                for (Integer idVal : ingredientIDs) {
                    acTextView = findViewById(idVal);
                    if (acTextView.getText() != null)
                        ingredientList.add(acTextView.getText().toString());
                    else
                        ingredientList.add("");
                }

                //1. Serialize The Data
                NewDish newDish = new NewDish(et_newRecipeName.getText().toString(), et_newRecipeDescription.getText().toString(), "", ingredientList);
                newDishList.add(newDish);
                DishUtils duObj = new DishUtils();
                duObj.writeDishList(this, new_dish_filename, newDishList);

                //2. Go back to the main screen
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        }
        return super.onOptionsItemSelected(item);
    }
}
