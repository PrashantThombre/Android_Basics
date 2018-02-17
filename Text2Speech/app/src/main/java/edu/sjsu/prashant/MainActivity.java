package edu.sjsu.prashant;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "edu.sjsu.prashant.FirstMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "OnCreate", Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_main);
    }


    public void sendMessage(View view){
        //1. Creating an intent to connect the 1st and 2nd activity
        Intent intent = new Intent(this, Main2Activity.class);
        //2. Retrieve a handle to the edit text object using ID
        EditText editText = (EditText) findViewById(R.id.editText);
        //3. Retrieve and convert the Editable Text to String
        String message = editText.getText().toString();
        //4. Put the message on to the intent that will be carried to the next activity by android
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

}
