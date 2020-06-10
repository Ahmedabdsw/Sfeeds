package com.abderrahmane.ahmed.sfeeds;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.abderrahmane.ahmed.visits.Visitor;

import java.util.Calendar;
import java.util.Random;

import db.dbHelper;

public class UpdateService extends Service {
 public Context c;
 public static int update = 0;
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */

  //  dbHelper db;
    public class LocalBinder extends Binder {
        UpdateService getService() {
            // Return this instance of LocalService so clients can call public methods
            return UpdateService.this;
        }
    }

    public UpdateService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.ed");
//        Toast.makeText(this,"1 2 noti"  , Toast.LENGTH_LONG).sho

        Calendar cld = Calendar.getInstance();
        Intent inte = new Intent(getApplicationContext(),UpdateNews.class);
        PendingIntent pendingIntent  = PendingIntent.getBroadcast(this,0,inte,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, cld.getTimeInMillis(), 400000   , pendingIntent);


        Calendar cld1 = Calendar.getInstance();
        Intent inte1 = new Intent(getApplicationContext(),UpdateNews1.class);
        PendingIntent pendingIntent1  = PendingIntent.getBroadcast(this,0,inte1,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am1.setRepeating(AlarmManager.RTC_WAKEUP, cld1.getTimeInMillis(), 2000000   , pendingIntent1);

        Calendar cld2 = Calendar.getInstance();
        Intent inte2 = new Intent(getApplicationContext(),UpdateNews2.class);
        PendingIntent pendingIntent2  = PendingIntent.getBroadcast(this,0,inte2,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am2.setRepeating(AlarmManager.RTC_WAKEUP, cld2.getTimeInMillis(), 8000000   , pendingIntent2);

        return  mBinder;
    }

    @Override
    public void onCreate() {
        // The service is being created

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
       c = getApplicationContext();
//        Toast.makeText(this, "Service starting", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }


}
