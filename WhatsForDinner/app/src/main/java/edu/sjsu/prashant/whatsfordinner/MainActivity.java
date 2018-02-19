package edu.sjsu.prashant.whatsfordinner;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void display_info(View view){
        Toast.makeText(this,"Banner Clicked!",Toast.LENGTH_LONG).show();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_app_info_popup,null));
        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            //Do Nothing Special
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void displayMealsScreen(View view){
        Intent intent = new Intent(this,MealsActivity.class);
        startActivity(intent);
    }

    public void displayRecipesScreen(View view){
        Intent intent = new Intent(this,RecipesActivity.class);
        startActivity(intent);
    }

    public void displayGroceriesScreen(View view){
        Intent intent = new Intent(this,GroceriesActivity.class);
        startActivity(intent);
    }

    public void displayNewDishScreen(View view){
        Intent intent = new Intent(this,NewDishActivity.class);
        startActivity(intent);
    }
}
