package edu.sjsu.prashant.whatsfordinner;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.sjsu.prashant.whatsfordinner.model.NewDish;
import edu.sjsu.prashant.whatsfordinner.util.DishUtils;

public class NewDishActivity extends AppCompatActivity {
    private static final String TAG = NewDishActivity.class.getSimpleName();
    private static final Integer OPEN_GALLERY_ACTIVITY = 1001;

    private static String SEPERATOR = "::";
    static boolean isDuplicate = false;
    private final static String new_dish_filename = "/new_dish_entry.dat";
    private final static String groceries_filename = "/groceries.dat";
    String imageURIpath = "drawable://"+R.drawable.generic_food_icon;
    EditText et_newRecipeName;
    EditText et_newRecipeDescription;
    ImageView iv_dishIcon;
    AutoCompleteTextView acTextView;
    Spinner mSpinner;
    final Integer [] ingredientIDs = {R.id.acTextView_ingredient01, R.id.acTextView_ingredient02, R.id.acTextView_ingredient03,
            R.id.acTextView_ingredient04, R.id.acTextView_ingredient05, R.id.acTextView_ingredient06,
            R.id.acTextView_ingredient07, R.id.acTextView_ingredient08, R.id.acTextView_ingredient09,
            R.id.acTextView_ingredient10};

    final Integer [] dropdownIDs = {R.id.sp_ingredient01, R.id.sp_ingredient02, R.id.sp_ingredient03, R.id.sp_ingredient04,
            R.id.sp_ingredient05,R.id.sp_ingredient06,R.id.sp_ingredient07,R.id.sp_ingredient08,R.id.sp_ingredient09,
            R.id.sp_ingredient10,};

    List<String> ingredientList = new ArrayList<>();
    List<String> completeRecipesList = new ArrayList<>();
    Set<String> tempSet = new HashSet<>();
    List<String> completeIngredientList;
    List<NewDish> newDishList = new ArrayList<>();
    HashMap<String, Integer> listIngredientMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish);

        DishUtils duObj = new DishUtils();
        newDishList = duObj.readDishList(this, new_dish_filename);

        Log.d(TAG, "Before Newdish read griceries if condition");
        listIngredientMap = duObj.readGroceriesMap(this, groceries_filename);
        Log.d(TAG, "After Newdish read griceries if condition");

        for (String key : listIngredientMap.keySet()) {
            Log.d(TAG, listIngredientMap.get(key) + " " + key.split(SEPERATOR)[1] + "-" + key.split(SEPERATOR)[0]);
        }



        ArrayAdapter<CharSequence> mArrayAdapter = ArrayAdapter.createFromResource(this,R.array.quantity_array,android.R.layout.simple_dropdown_item_1line);
        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (Integer id : dropdownIDs) {
            mSpinner = findViewById(id);
            mSpinner.setAdapter(mArrayAdapter);
        }
        for(NewDish newDish : newDishList) {
            Toast.makeText(this, "Hello_Toast from newDish", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, newDish.getDish_name(), Toast.LENGTH_SHORT).show();
            completeRecipesList.add(newDish.getDish_name());

             for(String ingredient : newDish.getIngredient_list()){
                tempSet.add(ingredient.split(SEPERATOR)[0]);
            }
        }
        completeIngredientList = new ArrayList<>(tempSet);
        et_newRecipeName = findViewById(R.id.et_newRecipeName);
        et_newRecipeDescription = findViewById(R.id.et_newRecipeDescription);
        iv_dishIcon = findViewById(R.id.iv_dish_icon);
        ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, completeIngredientList);
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
                for (int i = 0 ; i < ingredientIDs.length ; i++) {
                    acTextView = findViewById(ingredientIDs[i]);
                    mSpinner = findViewById(dropdownIDs[i]);

                    if (acTextView.getText() != null && !acTextView.getText().toString().equals("")){
                        ingredientList.add(acTextView.getText().toString() + SEPERATOR + mSpinner.getSelectedItem().toString());

//                    Toast.makeText(this,"=="+acTextView.getText(),Toast.LENGTH_SHORT).show();
//                    else
//                        ingredientList.add("");
                }
                }

                //1. Serialize The Data
                NewDish newDish = new NewDish(et_newRecipeName.getText().toString(), et_newRecipeDescription.getText().toString(), imageURIpath, ingredientList);
                newDishList.add(newDish);
                DishUtils duObj = new DishUtils();
                duObj.writeDishList(this, new_dish_filename, newDishList);

                        for (String ingredient : newDish.getIngredient_list()) {
                            if (listIngredientMap.get(ingredient) != null) {
                                listIngredientMap.put(ingredient, listIngredientMap.get(ingredient) + 1);
                            } else {
                                listIngredientMap.put(ingredient, 1);
                            }
                        }
                    duObj.writeGroceriesMap(this,groceries_filename,listIngredientMap);


                    //2. Go back to the main screen
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        }
        return super.onOptionsItemSelected(item);
    }

    public void enterNewFoodIcon(View view){

        //Using the ACTION_PICK action to have the user pick gallery
        Intent intent = new Intent(Intent.ACTION_PICK);

        //Getting the path to root of external storage
        String directory = Environment.getExternalStorageDirectory().getPath();
        Uri uri = Uri.parse(directory);

        //Setting DataType to all the images
        intent.setDataAndType(uri,"image/*");

        //Start activity with a result code
        startActivityForResult(intent,OPEN_GALLERY_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Checking if everything went well on the startActivityForResult() call for gallery intent
        if(requestCode == OPEN_GALLERY_ACTIVITY && resultCode == RESULT_OK){

            //Get the URI of the image selected by the user
            Uri selectedImageUri = data.getData();
            if(selectedImageUri != null)
                imageURIpath = selectedImageUri.toString();
            Log.d(TAG, imageURIpath);
            Log.d(TAG, selectedImageUri.toString());
            InputStream inputStream;
            try {
                Log.d(TAG, Uri.parse(imageURIpath).toString());

                inputStream = getContentResolver().openInputStream(Uri.parse(imageURIpath));
                Log.d(TAG, (MediaStore.Images.Media.EXTERNAL_CONTENT_URI).toString());

                //Get the bitmap of selected image from the input stream and set it to the Food Icon ImageView
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                iv_dishIcon.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Unable to open the iamge", Toast.LENGTH_SHORT).show();
            }
            Log.i(TAG, selectedImageUri.getPath());
        }
    }
}
