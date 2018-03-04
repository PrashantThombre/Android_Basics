package edu.sjsu.prashant.custombroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CustomReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Received Your Broadcast!"+MainActivity.val,Toast.LENGTH_LONG).show();
        MainActivity.val = 0;
    }
}
