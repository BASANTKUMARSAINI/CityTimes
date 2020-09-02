package services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.ApplicationClass;

public class LocationService extends Service {
//    public static final String BROADCAST_ACTION = "Hello World";
//    private static final int TWO_MINUTES = 1000 * 60 * 2;
//    public LocationManager locationManager;
//    public MyLocationListener listener;
//    public Location previousBestLocation = null;
//
//    Intent intent;
//    int counter = 0;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.v("tag", "kk"+"lati");
//        intent = new Intent(BROADCAST_ACTION);
//    }
//
//    @Override
//    public void onStart(Intent intent, int startId) {
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        listener = new MyLocationListener();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, (LocationListener) listener);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
//        if (currentBestLocation == null) {
//            // A new location is always better than no location
//            return true;
//        }
//
//        // Check whether the new location fix is newer or older
//        long timeDelta = location.getTime() - currentBestLocation.getTime();
//        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
//        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
//        boolean isNewer = timeDelta > 0;
//
//        // If it's been more than two minutes since the current location, use the new location
//        // because the user has likely moved
//        if (isSignificantlyNewer) {
//            return true;
//            // If the new location is more than two minutes older, it must be worse
//        } else if (isSignificantlyOlder) {
//            return false;
//        }
//
//        // Check whether the new location fix is more or less accurate
//        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
//        boolean isLessAccurate = accuracyDelta > 0;
//        boolean isMoreAccurate = accuracyDelta < 0;
//        boolean isSignificantlyLessAccurate = accuracyDelta > 200;
//
//        // Check if the old and new location are from the same provider
//        boolean isFromSameProvider = isSameProvider(location.getProvider(),
//                currentBestLocation.getProvider());
//
//        // Determine location quality using a combination of timeliness and accuracy
//        if (isMoreAccurate) {
//            return true;
//        } else if (isNewer && !isLessAccurate) {
//            return true;
//        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
//            return true;
//        }
//        return false;
//    }
//
//
//    /**
//     * Checks whether two providers are the same
//     */
//    private boolean isSameProvider(String provider1, String provider2) {
//        if (provider1 == null) {
//            return provider2 == null;
//        }
//        return provider1.equals(provider2);
//    }
//
//
//    @Override
//    public void onDestroy() {
//        // handler.removeCallbacks(sendUpdatesToUI);
//        super.onDestroy();
//        Log.v("STOP_SERVICE", "DONE");
//        locationManager.removeUpdates(listener);
//    }
//
//    public static Thread performOnBackgroundThread(final Runnable runnable) {
//        final Thread t = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    runnable.run();
//                } finally {
//
//                }
//            }
//        };
//        t.start();
//        return t;
//    }
//
//    public class MyLocationListener implements LocationListener {
//
//        public void onLocationChanged(final Location loc) {
//            Log.i("*****", "Location changed");
//            if (isBetterLocation(loc, previousBestLocation)) {
//                ApplicationClass.USER_LATITUDE=loc.getLatitude();
//                ApplicationClass.USER_LOGITUDE=loc.getLongitude();
//                Log.v("tag",loc.getLatitude()+"lati");
//                Log.v("tag",loc.getLongitude()+"longi");
//                intent.putExtra("Latitude", loc.getLatitude());
//                intent.putExtra("Longitude", loc.getLongitude());
//                intent.putExtra("Provider", loc.getProvider());
//                sendBroadcast(intent);
//
//            }
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        public void onProviderDisabled(String provider) {
//            //Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
//        }
//
//
//        public void onProviderEnabled(String provider) {
//           // Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
//        }
//    }
FirebaseAuth mAuth;
    @Override
    public void onCreate() {
        super.onCreate();
        mAuth=FirebaseAuth.getInstance();


        LocationRequest request = new LocationRequest();

//Specify how often your app should request the device’s location//

        request.setInterval(1000);

//Get the most accurate location data available//

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        // final String path = getString(R.string.firebase_path);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

//If the app currently has access to the location permission...//

        if (permission == PackageManager.PERMISSION_GRANTED) {

//...then request location updates//

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

//Get a reference to the database, so your app can perform read and write operations//



                    Location location = locationResult.getLastLocation();
                    if (location != null) {

//Save the location data to the database//
                        Log.d("ADD", location.getLatitude() + "kaka" + location.getLongitude());

                       ApplicationClass.USER_LATITUDE=location.getLatitude();
                       ApplicationClass.USER_LOGITUDE=location.getLongitude();
                    }
                }
            }, null);
        }
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
