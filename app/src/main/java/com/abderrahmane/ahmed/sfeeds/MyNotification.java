package com.abderrahmane.ahmed.sfeeds;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.*;

import static android.provider.Settings.Global.getString;

/**
 * Created by Ahmed on 2/17/2018.
 */

    //Notification class
    public class MyNotification  {
        String title;
        String message;
        Context c;
        int id;

        public MyNotification(Context c, int position,String message,String link) {
            try {
                if(link != "" ){
                title = "أهم الأخبار" ;
               // title = r.title0;
               // message = m;

              //      UpdateNews.writeDataToFile2(r.title);
                id = (int) System.currentTimeMillis();
                if (position >= 0) {
                    Intent intent = new Intent(c, WebActivity.class);
                    intent.putExtra("yourkey", link);
                    PendingIntent pIntent = PendingIntent.getActivity(c,
                            (int) System.currentTimeMillis(), intent, 0);
                    NotificationCompat.Builder mBuilder =
                            (NotificationCompat.Builder) new NotificationCompat.Builder(c)
                                    .setSmallIcon(R.drawable.ic_stat_name)
                                    .setContentTitle(title)
                                    .setContentText(message)
                                    .setContentIntent(pIntent);
                    NotificationManager notificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
                    mBuilder.setAutoCancel(true);
                    notificationManager.notify(id, mBuilder.build());
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone rr = RingtoneManager.getRingtone(c.getApplicationContext(), notification);
                    rr.play();
                }
              }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

}

