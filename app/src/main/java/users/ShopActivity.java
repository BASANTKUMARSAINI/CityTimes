package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycity.MainActivity;
import com.example.mycity.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Seller;

public class ShopActivity extends AppCompatActivity {
    String sUid;



private static final int REQUEST_LOCATION=1;
LocationManager locationManager;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    private  ImageView imgBackground;
    private CircleImageView imgProfile;
    private TextView tvStoreName,tvOwnerName,tvPhone1,tvPhone2,tvStoreAddress,tvStoreTime;
    TextView tvSunday,tvMonday,tvTuseday,tvWednesday,tvThrusday,tvFriday,tvSaturday;
    TextView tvHomeDelivery,tvWorkers,tvSeeAllPhotos;
    ImageView imgSave;

    TextView tvRatings;
    boolean isStoreSaved=false;

     Seller favoriteStore=null;
     Double STORE_LONGITUDE=1.0,STORE_LATITUDE=1.0;
     Double USER_LOGITUDE=-1.0,USER_LATITUDE=-1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        sUid=getIntent().getStringExtra("sUid");

//        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        imgBackground=findViewById(R.id.background_image);
        imgProfile=findViewById(R.id.img_profile);

        tvStoreName=findViewById(R.id.tv_store_name);
        tvOwnerName=findViewById(R.id.tv_owner_name);
        tvPhone1=findViewById(R.id.tv_phone_1);
        tvPhone2=findViewById(R.id.tv_phone_2);
        tvStoreAddress=findViewById(R.id.tv_store_address);
        tvStoreTime=findViewById(R.id.tv_store_timing);

        tvSunday=findViewById(R.id.tv_day_sunday);
        tvMonday=findViewById(R.id.tv_day_monday);
        tvTuseday=findViewById(R.id.tv_day_tuesday);
        tvWednesday=findViewById(R.id.tv_day_wednesday);
        tvThrusday=findViewById(R.id.tv_day_thursday);
        tvFriday=findViewById(R.id.tv_day_friday);
        tvSaturday=findViewById(R.id.tv_day_saturday);

        tvHomeDelivery=findViewById(R.id.tv_delivery_status);
        tvWorkers=findViewById(R.id.tv_workers);
        tvSeeAllPhotos=findViewById(R.id.tv_see_all_photos);

        imgSave=findViewById(R.id.img_bookmarks);

        setBookMarks();

        tvSeeAllPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ShopActivity.this,StorePhotosActivity.class);
                intent.putExtra("sUid",sUid);
                startActivity(intent);
            }
        });

        tvRatings=findViewById(R.id.tv_rating_bar);
        setAllData();
    }

    private void setBookMarks() {
        db.collection("users").document(mAuth.getUid()).collection("favoriteStores").document(sUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    isStoreSaved=true;
                    imgSave.setImageResource(R.drawable.bookmark);
                }
                else {
                    isStoreSaved=false;
                    imgSave.setImageResource(R.drawable.bookmark_border);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isStoreSaved=false;
                imgSave.setImageResource(R.drawable.bookmark_border);
            }
        });
    }

    private void setAllData() {
        db.collection("sellers").document(sUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Seller seller = documentSnapshot.toObject(Seller.class);
                    favoriteStore=seller;

                    if (seller.getBackgroundImage() != null) {
                        Picasso.get().load(seller.getBackgroundImage()).into(imgBackground);
                        imgBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    if (seller.getOwnerImage() != null)
                        Picasso.get().load(seller.getOwnerImage()).into(imgProfile);
                    tvStoreName.setText(seller.getStoreName());
                    tvOwnerName.setText(seller.getOwnerName());
                    tvPhone1.setText(seller.getPhone1());
                    tvPhone2.setText(seller.getPhone2());
                    tvStoreAddress.setText(seller.getStoreAddress());

                    List<String> timeListFrom=seller.getTimeFrom();
                    List<String> timeListTo=seller.getTimeTo();

                    String storeTiming = timeListFrom.get(0) + ":" + timeListFrom.get(1) + timeListFrom.get(2) + "-"
                            + timeListTo.get(0) + ":" + timeListTo.get(1) + timeListTo.get(2);
                    tvStoreTime.setText(storeTiming);

                    HashMap<String, Boolean> days = seller.getDays();
                    setDays(days);


                    setDeleveryStatus(seller.isDeliveryStatus());

                    setWorkerRequiredStatus(seller.isWorkersRequred());

                    STORE_LATITUDE=seller.getStoreLatitude();
                    STORE_LONGITUDE=seller.getStoreLongitude();
                    Integer totalStar = seller.getTotalStar();
                    Integer totalRatings = seller.getNoOfRatings();
                    if (totalRatings == 0)
                        tvRatings.setText("0.0");
                    else {
                        float ratings = (float) totalStar / totalRatings;
                        tvRatings.setText(ratings + "");
                    }


                }

            }


        });
    }
    private void setWorkerRequiredStatus(boolean workersRequred) {

        if(workersRequred)
        {
            tvWorkers.setText("Yes, workers required");
        }
        else
        {
            tvWorkers.setText("No, workers required");
        }
    }
    private void setDeleveryStatus(boolean deliveryStatus) {
        if(deliveryStatus)
        {
            tvHomeDelivery.setText("Home delivery Avaliable");
        }
        else
        {
            tvHomeDelivery.setText("Home delivery not Avaliable");
        }

    }

    private void setDays(HashMap<String, Boolean> days) {
        tvSunday.setTextColor(getResources().getColor(R.color.green));
        tvMonday.setTextColor(getResources().getColor(R.color.green));
        tvTuseday.setTextColor(getResources().getColor(R.color.green));
        tvWednesday.setTextColor(getResources().getColor(R.color.green));
        tvThrusday.setTextColor(getResources().getColor(R.color.green));
        tvFriday.setTextColor(getResources().getColor(R.color.green));
        tvSaturday.setTextColor(getResources().getColor(R.color.green));

        if(!days.get("sunday"))
            tvSunday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("monday"))
            tvMonday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("tuseday"))
            tvTuseday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("wednesday"))
            tvWednesday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("thrusday"))
            tvThrusday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("friday"))
            tvFriday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("saturday"))
            tvSaturday.setTextColor(getResources().getColor(R.color.red));
    }
    public void goToBack(View view)
    {
        finish();
    }
    public void saveStore(View view)
    {
        isStoreSaved=!isStoreSaved;
        if(isStoreSaved)
        {
            imgSave.setImageResource(R.drawable.bookmark);
             if(favoriteStore!=null)
             {
                 db.collection("users").document(mAuth.getUid())
                         .collection("favoriteStores").document(sUid).set(favoriteStore).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         Toast.makeText(ShopActivity.this,"saved",Toast.LENGTH_LONG).show();

                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         imgSave.setImageResource(R.drawable.bookmark_border);
                         Toast.makeText(ShopActivity.this,"you can't save this store",Toast.LENGTH_LONG).show();
                     }
                 });
             }
             else
             {
                 imgSave.setImageResource(R.drawable.bookmark_border);
                 Toast.makeText(ShopActivity.this,"no such store exist",Toast.LENGTH_LONG).show();
             }
        }
        else
        {

            imgSave.setImageResource(R.drawable.bookmark_border);
            db.collection("users").document(mAuth.getUid()).collection("favoriteStores")
                    .document(sUid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ShopActivity.this,"deleted",Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    imgSave.setImageResource(R.drawable.bookmark);
                    Toast.makeText(ShopActivity.this,"you can't remove it",Toast.LENGTH_LONG).show();
                }
            });
        }

    }
    public void goToMap(View view)
    {
        Log.v("TAG","a");
        if(ActivityCompat.checkSelfPermission(ShopActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(ShopActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            return;

        }
       if(!getCurrentLocation())
       {

           return;
       }
        Log.v("TAG","aaa");
        String uri;
        if(USER_LATITUDE==-1.0||USER_LOGITUDE==-1.0)
        {
            uri=String.format(Locale.ENGLISH,"geo:%f,%f",STORE_LATITUDE,STORE_LONGITUDE);
            Log.v("TAG","b");
        }
        else
        {
            Log.v("TAG","bbbb");
            uri="http://maps.google.com/maps?saddr="+USER_LATITUDE+","+USER_LOGITUDE+"&daddr="+STORE_LATITUDE+","+STORE_LONGITUDE;
        }
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    private boolean getCurrentLocation() {

        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            onGps();
            return false;
        }
        else
        {
            getLocations();
            return  true;
        }

    }

    private void getLocations() {
        if(ActivityCompat.checkSelfPermission(ShopActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(ShopActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);

        }
        else {
            Log.v("TAG", "aa");
            Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location locationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (locationGps != null) {
                USER_LOGITUDE = locationGps.getLongitude();
                USER_LATITUDE = locationGps.getLatitude();

            } else if (locationNetwork != null) {
                USER_LOGITUDE = locationNetwork.getLongitude();
                USER_LATITUDE = locationNetwork.getLatitude();

            } else if (locationPassive != null) {
                USER_LOGITUDE = locationPassive.getLongitude();
                USER_LATITUDE = locationPassive.getLatitude();

            }

        }
    }

    private void onGps() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Enable Gps").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog dialog=builder.create();
        dialog.show();

    }
}
