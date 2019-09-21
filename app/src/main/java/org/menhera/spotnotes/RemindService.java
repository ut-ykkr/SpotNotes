package org.menhera.spotnotes;

import android.Manifest;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RemindService extends Service implements LocationClient.Listener {
    final static String TAG = "RemindService";
    private LocationManager mLocationManager;
    private String bestProvider;
    private LocationClient locationClient;


    public String title;
    public double lat;
    public double lon;
    public String addr;
    public int radius;
    public boolean in;
    public long time;
    public int repeat;
    public String memo;

    final String CHANNEL_ID = "default";
    int notificationCount = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int index = intent.getIntExtra("index", 0);
        Log.d(TAG, "onStartCommand index : " + index);

        SpotNotesApplication app = (SpotNotesApplication)getApplication();
        ReminderItem item = app.get(index);


        in = item.getInOut();

        title = item.getTitle();
        lat = item.getLat();
        lon =  item.getLon();
        addr = item.getLocation();
        time = item.getMilliseconds();
        radius = item.getDistance();
        memo = item.getNotes();

        locationClient = new LocationClient(this, this);

        return START_NOT_STICKY;
    }

    public void showNotification (String title, String description){




        this.createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.btn_dialog)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationCount++;
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationCount, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "通知";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onLocationFetched(Location location) {
        Log.d(TAG,"Lat : " + location.getLatitude() + " Lon : " + location.getLongitude());
        Location dist = new Location("gps");
        dist.setLongitude(lon);
        dist.setLatitude(lat);

        float distance = location.distanceTo(dist);
        Log.d(TAG, "dist : " + distance);

        if((in && distance <= radius ) || (!in && distance > radius)){
            showNotification(title, memo);
        }

        stopSelf();
    }
}
