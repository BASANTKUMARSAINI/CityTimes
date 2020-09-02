package services;



import com.example.mycity.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.app.LauncherActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.Manifest;
import android.location.Location;
import android.app.Notification;
import android.content.pm.PackageManager;
import android.app.PendingIntent;
import android.app.Service;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import model.Seller;


public class TrackingService extends Service {

    private static final String TAG = TrackingService.class.getSimpleName();
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String CHANNEL_ID="my_channel_id";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        if(mAuth.getCurrentUser()!=null) {
            db.collection("ensellers").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()) {
                        Seller seller = documentSnapshot.toObject(Seller.class);
                        if (seller.getStoreSubCategory().equals("Auto")) {
                            Log.v("TAG", "auto");
                            buildNotification();
                            requestLocationUpdates();

                        }
                    }
                }
            });

        }
    }

    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        createChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.tracking_enabled_notif))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.ic_notifications);
        startForeground(1, builder.build());
    }

    private void createChannel() {
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
           notificationChannel=new NotificationChannel(CHANNEL_ID, "My_Channel", NotificationManager.IMPORTANCE_LOW);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
            notificationManager.createNotificationChannel(notificationChannel);
        }
        else
        {
            Log.d("ADD","less");
        }

    }
    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };
    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    String currentTime=new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    String currentDate=new SimpleDateFormat("dd:MM:yy", Locale.getDefault()).format(new Date());
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                            .child("current_location").child(mAuth.getUid());
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        ref.child("location").setValue(location);
                        ref.child("time").setValue(currentTime);
                        ref.child("date").setValue(currentDate);
                    }
                }
            }, null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // Toast.makeText(TrackingService.this, "GPS Tracking have stopped", Toast.LENGTH_SHORT).show();
    }
}
