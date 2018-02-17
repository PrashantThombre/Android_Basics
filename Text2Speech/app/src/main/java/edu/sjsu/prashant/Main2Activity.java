package edu.sjsu.prashant;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.widget.Toast;

import java.util.Locale;

public class Main2Activity extends AppCompatActivity {
    TextToSpeech tts;
    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "OnCreate", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main2);

        //1. Get the handle to the intent which created this activity
        Intent intent = getIntent();

        //2. Extract the message from this intent
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

         tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.ENGLISH);
                    tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

    }

    public void onStart(){
        super.onStart();
        Toast.makeText(this, "OnStart", Toast.LENGTH_SHORT).show();
    }
    public void onPause(){
        super.onPause();
        Toast.makeText(this, "OnPause", Toast.LENGTH_SHORT).show();
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
    }
}
