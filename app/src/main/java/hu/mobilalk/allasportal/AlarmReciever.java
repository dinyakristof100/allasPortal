package hu.mobilalk.allasportal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
            new NotificationHandler(context).send("It's time to get a job!");
    }
}