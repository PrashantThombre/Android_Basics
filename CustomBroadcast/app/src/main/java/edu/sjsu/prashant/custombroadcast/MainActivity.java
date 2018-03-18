package edu.sjsu.prashant.custombroadcast;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String CUSTOM_INTENT = "edu.sjsu.prashant.CUSTOM_BROADCAST";
    static int  val = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.TIME_TICK");
        CustomReceiver customReceiver = new CustomReceiver();
        this.registerReceiver(customReceiver,intentFilter);
    }

    public void button_click(View view){
    val++;
    }
}
