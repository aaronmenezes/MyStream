package com.kyser.demosuite.service.messagingservice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.kyser.demosuite.R;
import com.kyser.demosuite.view.ui.Featured;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class DemoNotificationManager {

    private Context mContext;
    private static DemoNotificationManager mInstance;

    private DemoNotificationManager(Context context) {
        mContext = context;
    }

    public static synchronized DemoNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DemoNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String title, String body) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, Constants.CHANNEL_ID)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(title)
                        .setContentText(body);
        Intent resultIntent = new Intent(mContext, Featured.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotifyMgr =  (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        if (mNotifyMgr != null) {
            mNotifyMgr.notify(1, mBuilder.build());
        }
    }
}
