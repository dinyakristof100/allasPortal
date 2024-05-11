package hu.mobilalk.allasportal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {

    private static final String CHANNEL_ID = "jobs_notfication_channel";
    private NotificationManager myManager;
    private final Context myContext;

    private final int NOTIFICATION_ID = 0;

    public NotificationHandler(Context context) {
        this.myContext = context;
        this.myManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationChannel channel =
                new NotificationChannel(
                    CHANNEL_ID,"Job Notification",NotificationManager.IMPORTANCE_DEFAULT
        );

        channel.enableLights(true);
        channel.setLightColor(Color.WHITE);
        channel.setDescription("Hello! Take a look at the current jobs!");
        this.myManager.createNotificationChannel(channel);
    }

    public void send(String messeage){
        Intent intent = new Intent(myContext, JobsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(myContext, 0, intent, PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(myContext, CHANNEL_ID)
                .setContentTitle("Jobs application")
                .setContentText(messeage)
                .setSmallIcon(R.drawable.assignment_turned_in)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        this.myManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancel(){
        this.myManager.cancel(NOTIFICATION_ID);
    }
}
