package seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.StrongBoxUnavailableException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dialog.CustumProgressDialog;
import interfaces.UpdateInterface;
import model.ApplicationClass;
import model.Seller;
import seller.deffrent_services.CinemaShowsActivity;
import seller.deffrent_services.FoodMenuActivity;
import services.TrackingService;
import update_dialog.PhoneNumberUpdateDialog;
import update_dialog.UpdateCompanyName;
import update_dialog.UpdateDays;
import update_dialog.UpdateHomeDelivery;
import update_dialog.UpdateHostelFacility;
import update_dialog.UpdateOwnerName;
import update_dialog.UpdatePrincipleName;
import update_dialog.UpdateServiceRate;
import update_dialog.UpdateShopStatus;
import update_dialog.UpdateStoreAddress;
import update_dialog.UpdateStoreName;
import update_dialog.UpdateStoreTime;
import update_dialog.UpdateTransportFacility;
import update_dialog.UpdateWorkerRequired;
import users.HomeActivity;
import users.SeeFullImageActivity;
import users.StoresActivity;

import static android.view.View.GONE;

public class StoreActivity extends AppCompatActivity implements UpdateInterface {

    private static final int PERMISSIONS_REQUEST =1 ;
    //web services
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference mStoreRef;


    //Layouts
    private LinearLayout rateLinearLayout, menuLinearLayout,ratingLinearLayout,photosLinearLayout,hostelLinearLayout
            ,transportLinearLayout,showsLinearLayout,homeDeliveryLinearLayout,timeLinearLayout,dayLinearLayout
            ,boardLinearLayout,workersRequiredLinearLayout,changeShopStatusLinearLayout,contactLinearLayout;
    private RelativeLayout principleRelativeLayout,companyNameRelativeLayout;

   //TetViews
    private TextView tvStoreName,tvOwnerName,tvPhone1,tvPhone2,tvStoreAddress,tvStoreTime,tvHostel,tvTransport,tvBoard,
                    tvShopStatus,tvComma,tvRate,tvPrincipleName,tvLastUpdate,tvCompanyName;
    private TextView tvSunday,tvMonday,tvTuseday,tvWednesday,tvThrusday,tvFriday,tvSaturday;
    private TextView tvHomeDelivery,tvWorkers;
    private TextView tvRatings;

    //ImageViews
    private ImageView imgBackground,imgShopStatus,updateAll;
    private CircleImageView imgProfile;
    private  ImageView updateStoreName,updateOwnerName,updatePhone,updateStoreAddress,
            updateStoreTime,updateDays,updateHomeDelivery,updateWorkers,updateHostel,updateTransport,updateRate,
            updatePrincipleName,updateCompanyName;

    //EditText
    private EditText etStoreDescription;

    //Constate
    private   static int BACKGROUND=1,PROFILE=2;

   //Button
    private Button btnApply,btnCancel;

    //Java Class
    private Seller store=null;

    private String CATEGORY="",SUB_CATEGORY="";


//    private boolean SHOP_STATUS=true;
    private boolean isEdit=false;

    //store updated details
    public static String PHONE_1=null,PHONE_2=null,STORE_NAME=null,STORE_ADDRESS=null,
            OWNER_NAME=null,PRINCIPAL_NAME=null,BACKGROUND_URL=null,OWNER_IMAGE_URL=null,COMPANY_NAME=null;
    public static boolean HOSTEL=false,DELIVERY_STATUS=false,WORK_REQUIRED=false,TRANSPORT=false;
    public static Map<String ,Boolean>DAYS=null;
    public  static List<String>TIME_FROM=null,TIME_TO=null;
    public  static String SERVICE_RATE=null;
    public static String QUALIFICATION=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(this);
        setContentView(R.layout.activity_store);
        initializeViews();
        textFunction();
    }

    private void textFunction() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("ADD","gps");
            //finish();
        }

        //Check whether this app has access to the location permission//


        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

//If the location permission has been granted, then start the TrackerService//

        if (permission == PackageManager.PERMISSION_GRANTED) {
            Log.d("ADD","start service");
            startTrackerService();
        } else {

//If the app doesn’t currently have access to the user’s location, then request access//

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {

//If the permission has been granted...//

        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            //...then start the GPS tracking service//
Log.d("ADD","permission");
            startTrackerService();
        } else {

//If the user denies the permission request, then display a toast with some more information//

            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }




    private void startTrackerService() {
        startService(new Intent(this, TrackingService.class));

//Notify the user that tracking has been enabled//

        //Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();

//Close MainActivity//

        //finish();
    }

    private void initializeViews() {
        //web services
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        mStoreRef= FirebaseStorage.getInstance().getReference();

        //layouts
        menuLinearLayout=findViewById(R.id.menu_linear_layout);
        rateLinearLayout=findViewById(R.id.rate_linear_layout);
        ratingLinearLayout=findViewById(R.id.rating_linear_layout);
        photosLinearLayout=findViewById(R.id.photos_linear_layout);
        hostelLinearLayout=findViewById(R.id.hostel_linear_layout);
        transportLinearLayout=findViewById(R.id.transprot_linear_layout);
        showsLinearLayout=findViewById(R.id.show_linear_layout);
        timeLinearLayout=findViewById(R.id.time_linear_layout);
        homeDeliveryLinearLayout=findViewById(R.id.home_delivery_linear_layout);
        dayLinearLayout=findViewById(R.id.day_linear_layout);
        boardLinearLayout=findViewById(R.id.board_linear_layout);
        workersRequiredLinearLayout=findViewById(R.id.workers_linear_layout);
        principleRelativeLayout=findViewById(R.id.principle_relative_layout);
        companyNameRelativeLayout=findViewById(R.id.company_name_relative_layout);
        changeShopStatusLinearLayout=findViewById(R.id.change_shop_status);
        contactLinearLayout=findViewById(R.id.contact_linear_layout);

        //TextViews
        tvRatings=findViewById(R.id.tv_rating_bar);
        tvRate=findViewById(R.id.tv_rate);
        tvShopStatus=findViewById(R.id.tv_shop_status);
        tvComma=findViewById(R.id.tv_comma);
        tvStoreName=findViewById(R.id.tv_store_name);
        tvOwnerName=findViewById(R.id.tv_owner_name);
        tvPhone1=findViewById(R.id.tv_phone_1);
        tvPhone2=findViewById(R.id.tv_phone_2);
        tvStoreAddress=findViewById(R.id.tv_store_address);
        tvStoreTime=findViewById(R.id.tv_store_timing);
        tvBoard=findViewById(R.id.tv_board_name);
        tvPrincipleName=findViewById(R.id.tv_principle_name);
        tvLastUpdate=findViewById(R.id.tv_last_update);
        tvCompanyName=findViewById(R.id.tv_company_name);

        tvSunday=findViewById(R.id.tv_day_sunday);
        tvMonday=findViewById(R.id.tv_day_monday);
        tvTuseday=findViewById(R.id.tv_day_tuesday);
        tvWednesday=findViewById(R.id.tv_day_wednesday);
        tvThrusday=findViewById(R.id.tv_day_thursday);
        tvFriday=findViewById(R.id.tv_day_friday);
        tvSaturday=findViewById(R.id.tv_day_saturday);

        tvHomeDelivery=findViewById(R.id.tv_delivery_status);
        tvWorkers=findViewById(R.id.tv_workers);
        tvHostel=findViewById(R.id.tv_hostel);
        tvTransport=findViewById(R.id.tv_transport);


        //ImageViews
        imgShopStatus=findViewById(R.id.img_shop_status);
        imgBackground=findViewById(R.id.img_background);
        imgProfile=findViewById(R.id.img_profile);

        updateAll=findViewById(R.id.img_change);
        updateStoreName=findViewById(R.id.update_store_name);
        updateOwnerName=findViewById(R.id.update_owner_name);
        updateStoreAddress=findViewById(R.id.update_store_address);
        updateStoreTime=findViewById(R.id.update_store_time);
        updatePhone=findViewById(R.id.update_phone);
        updateDays=findViewById(R.id.update_days);
        updateHomeDelivery=findViewById(R.id.update_delivery_status);
        updateWorkers=findViewById(R.id.update_workers);
        updateHostel=findViewById(R.id.update_hostel);
        updateTransport=findViewById(R.id.update_transport);
        updateRate=findViewById(R.id.update_rate);
        updatePrincipleName=findViewById(R.id.update_principle_name);
        updateCompanyName=findViewById(R.id.update_company_name);

        //Buttons
        btnApply=findViewById(R.id.btn_apply);
        btnCancel=findViewById(R.id.btn_cancel);

        //EditText
        etStoreDescription=findViewById(R.id.et_shop_description);

        //extras
        isEdit=false;
    }

    //set all details
    @Override
    protected void onStart() {
    super.onStart();
    String path=ApplicationClass.LANGUAGE_MODE+"sellers";
    db.collection(path).document(mAuth.getUid())
            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            if(documentSnapshot.exists())
            {
                Seller seller=documentSnapshot.toObject(Seller.class);
                //Assign value
                store=seller;
                CATEGORY=seller.getStoreCategory();
                SUB_CATEGORY=seller.getStoreSubCategory();

                //common setup
                if(seller.getBackgroundImage()!=null)
                {
                    Picasso.get().load(seller.getBackgroundImage()).into(imgBackground);
                    imgBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                if(seller.getOwnerImage()!=null)
                {
                    Picasso.get().load(seller.getOwnerImage()).into(imgProfile);
                }

                tvStoreName.setText(seller.getStoreName());
                tvOwnerName.setText(seller.getOwnerName());

                tvPhone1.setText(seller.getPhone1());
                if(TextUtils.isEmpty(seller.getPhone1()))
                {
                    contactLinearLayout.setVisibility(GONE);
                }
                if(!TextUtils.isEmpty(seller.getPhone2()))
                {
                    tvComma.setVisibility(View.VISIBLE);
                    tvPhone2.setText(seller.getPhone2());
                }
                tvStoreAddress.setText(seller.getStoreAddress());
                if(seller.getStoreDescription()!=null)
                    etStoreDescription.setText(seller.getStoreDescription());
                etStoreDescription.setEnabled(false);

                List<String> timeListFrom=seller.getTimeFrom();
                List<String> timeListTo=seller.getTimeTo();
                String storeTiming=timeListFrom.get(0)+":"+timeListFrom.get(1)+timeListFrom.get(2)+"-"
                        +timeListTo.get(0)+":"+timeListTo.get(1)+timeListTo.get(2);
                tvStoreTime.setText(storeTiming);

                //call functions
                setDays(seller.getDays());

                setDeleveryStatus(seller.isDeliveryStatus());
                DELIVERY_STATUS=seller.isDeliveryStatus();

                setWorkerRequiredStatus(seller.isWorkersRequred());
                WORK_REQUIRED=seller.isWorkersRequred();

                double totalStar=seller.getTotalStar();
                Integer totalRatings=seller.getNoOfRatings();
                if(totalRatings==0)
                    tvRatings.setText("0.0");
                else {
                    float ratings=(float)totalStar/totalRatings;
                    tvRatings.setText(ratings+"");
                }



                setShopStatus(seller.isShopStatus());

                setDataVisiblity(seller.getStoreCategory());

                if(BACKGROUND_URL!=null)
                    Picasso.get().load(BACKGROUND_URL).into(imgBackground);
                if(OWNER_IMAGE_URL!=null)
                    Picasso.get().load(OWNER_IMAGE_URL).into(imgProfile);

                setupForEducation();
                setUpForStay();
                setUpForTravel();
                setUpForVehical();
                setUpForBooking();
                setUpForNGO();
                setUpforBank();
                setUpForRentals();
                setUpForBuilding();


            }
        }


    });
}

    private void setUpForBuilding() {
        if(store.getStoreSubCategory().equals("Thekedar"))
        {

        }
    }

    private void setUpForRentals() {
        if(store.getStoreCategory().equals("Rentals"))
        {
            workersRequiredLinearLayout.setVisibility(GONE);
//            changeShopStatusLinearLayout.setVisibility(GONE);
            rateLinearLayout.setVisibility(View.VISIBLE);
            if(store.getRate()!=null)
            {
                tvRate.setText(store.getRate()+"/month");
            }
        }
    }

    private void setUpforBank() {
        if(store.getStoreCategory().equals("Banking"))
        {
            imgProfile.setVisibility(GONE);
            if(store.getStoreSubCategory().equals("ATM"))
            {
                workersRequiredLinearLayout.setVisibility(GONE);
            }
        }

    }

    private void setUpForNGO() {
        if(store.getStoreSubCategory().equals("NGO"))
        {
            imgProfile.setVisibility(GONE);
            workersRequiredLinearLayout.setVisibility(GONE);
            changeShopStatusLinearLayout.setVisibility(GONE);

        }
    }

    private void setUpForBooking() {
        if(store.getStoreCategory().equals("Booking"))
        {
            if(store.getRate()!=null)
            {
                rateLinearLayout.setVisibility(View.VISIBLE);
                tvRate.setText(store.getRate());
            }
            if(store.getStoreSubCategory().equals("Sound box(DJ)"))
            {
                workersRequiredLinearLayout.setVisibility(GONE);
            }
        }
    }


    private void setUpForVehical() {
        if(store.getStoreSubCategory().equals("Showroom"))
        {
            if((store.getCompanyName()!=null))
            {
                if(!store.getCompanyName().equals("")) {
                    companyNameRelativeLayout.setVisibility(View.VISIBLE);
                    tvCompanyName.setText(store.getCompanyName());
                }
            }
        }
        if(store.getStoreSubCategory().equals("Two wheeler misthri")||store.getStoreSubCategory().equals("Four wheeler misthri"))
        {
            homeDeliveryLinearLayout.setVisibility(View.VISIBLE);
            setDeleveryStatus(store.isDeliveryStatus());
            workersRequiredLinearLayout.setVisibility(GONE);
        }
        if(store.getStoreSubCategory().equals("JCB and Crane"))
        {
            workersRequiredLinearLayout.setVisibility(GONE);

        }
    }

    private void setUpForTravel() {
        if(store.getStoreSubCategory().equals("Taxi Car"))
        {
            if(store.getRate()!=null)
            {
                tvRate.setText(store.getRate()+"/km");
            }
            else {
                rateLinearLayout.setVisibility(GONE);
            }
        }
        if(store.getStoreSubCategory().equals("Auto"))
        {
            tvLastUpdate.setVisibility(View.VISIBLE);

            DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
            ref=ref.child("current_location").child(mAuth.getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String date=snapshot.child("date").getValue(String.class);
                    String time=snapshot.child("time").getValue(String.class);
                    String text="Last update:"+time+","+date;
                    tvLastUpdate.setText(text);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    tvLastUpdate.setVisibility(GONE);

                }
            });

        }
    }
    private void setUpForStay() {
        if(store.getStoreCategory().equals("Stay"))
        {

            if(store.getRate()!=null)
                tvRate.setText(store.getRate()+"");
        }
    }

    private void setupForEducation() {
        if(store.getStoreCategory().equals("Education")) {
            if (store.getBoardName() != null)
                tvBoard.setText(store.getBoardName());
            if (store.getPrincipalName() != null) {

                String fullName = store.getPrincipalName() + "(" + getString(R.string.principle) + ")";
                if(!store.getStoreSubCategory().equals("School"))
                    fullName=store.getPrincipalName()+"("+getString(R.string.head_name)+")";
                tvPrincipleName.setText(fullName);
            }
            if (store.getOwnerName()!=null) {
                String fullName = store.getOwnerName() + "(" +getString(R.string.chairman) + ")";
                tvOwnerName.setText(fullName);
            }
            setTransportStatus(store.isTransport());
            sethostelStatus(store.isHostel());
             HOSTEL=store.isHostel();
             TRANSPORT=store.isTransport();

        }
    }

    //set methods
    private void setShopStatus(boolean isOpen) {
        boolean freeBookedCondition=store.getStoreCategory().equals("Travel")||store.getStoreSubCategory().equals("Sound box(DJ)")
                ||store.getStoreSubCategory().equals("JCB and Crane");
        boolean availableNotAvailableCondition=store.getStoreCategory().equals("Rentals")||store.getStoreSubCategory().equals("Thekedar");

        if(isOpen)
        {
            imgShopStatus.setImageResource(R.drawable.ic_right_green);
            tvShopStatus.setText(R.string.open);

            if(freeBookedCondition)
            {
                tvShopStatus.setText(R.string.free);
            }
            else if(availableNotAvailableCondition)
            {
                tvShopStatus.setText(R.string.available);
            }

            tvShopStatus.setTextColor(getResources().getColor(R.color.green));
        }
        else
        {
            imgShopStatus.setImageResource(R.drawable.ic_cross_red);
            tvShopStatus.setText(R.string.close);
            if (freeBookedCondition)
            {
                tvShopStatus.setText(R.string.booked);
            }
            else if(availableNotAvailableCondition)
            {
                tvShopStatus.setText(R.string.not_available);
            }
            tvShopStatus.setTextColor(getResources().getColor(R.color.red));
        }

    }

    private void setTransportStatus(boolean transport) {
        tvTransport.setText(R.string.transport_available);
        if(!transport)
            tvTransport.setText(R.string.transport_not_available);
    }

    private void sethostelStatus(boolean hostel) {
        tvHostel.setText(R.string.hostel_available);
        if(!hostel)
            tvHostel.setText(R.string.hostel_not_available);
    }

    public void setDataVisiblity(String CATEGORY)
    {
        switch (CATEGORY)
        {
            case "Shopping":
                 homeDeliveryLinearLayout.setVisibility(View.VISIBLE);
                 menuLinearLayout.setVisibility(GONE);
                 rateLinearLayout.setVisibility(GONE);
                 hostelLinearLayout.setVisibility(GONE);
                 transportLinearLayout.setVisibility(GONE);
                 showsLinearLayout.setVisibility(GONE);
                 principleRelativeLayout.setVisibility(GONE);
                break;
            case "Food":
                homeDeliveryLinearLayout.setVisibility(View.VISIBLE);
                menuLinearLayout.setVisibility(View.VISIBLE);
                rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                showsLinearLayout.setVisibility(GONE);
                principleRelativeLayout.setVisibility(GONE);
            case "Gym Sports":
                homeDeliveryLinearLayout.setVisibility(View.GONE);
                menuLinearLayout.setVisibility(GONE);
                rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                showsLinearLayout.setVisibility(GONE);
                break;
            case "Shows and Cinema":
                homeDeliveryLinearLayout.setVisibility(View.GONE);
                showsLinearLayout.setVisibility(View.VISIBLE);
                menuLinearLayout.setVisibility(GONE);
                rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                break;
            case "Cyber":
                homeDeliveryLinearLayout.setVisibility(View.GONE);
                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);
                rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                principleRelativeLayout.setVisibility(GONE);
                break;
            case "Vehicles and Workshop":
//                homeDeliveryLinearLayout.setVisibility(View.GONE);
                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);
//                rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                principleRelativeLayout.setVisibility(GONE);
                break;
            case "Buliding Material":
                homeDeliveryLinearLayout.setVisibility(GONE);
                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);
                rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                break;
            case "Rentals":
                homeDeliveryLinearLayout.setVisibility(GONE);
                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);
                rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                principleRelativeLayout.setVisibility(GONE);
                break;
            case "Factories":
                homeDeliveryLinearLayout.setVisibility(GONE);
                timeLinearLayout.setVisibility(GONE);
                dayLinearLayout.setVisibility(GONE);
                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);
                rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                principleRelativeLayout.setVisibility(GONE);
                break;
            case "Banking":
                homeDeliveryLinearLayout.setVisibility(GONE);
                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);
                rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                principleRelativeLayout.setVisibility(GONE);
                break;
            case "Pentrol Pump":
                homeDeliveryLinearLayout.setVisibility(GONE);
                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);
                rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                break;
            case "NGO and Club":
                homeDeliveryLinearLayout.setVisibility(GONE);
                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);
                rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                principleRelativeLayout.setVisibility(GONE);
                break;
            case "Parlourl Saloon":
                homeDeliveryLinearLayout.setVisibility(GONE);
                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);
                rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                principleRelativeLayout.setVisibility(GONE);
                break;
            case "Electronics":
                homeDeliveryLinearLayout.setVisibility(GONE);
                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);
                rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                principleRelativeLayout.setVisibility(GONE);
                break;
            case "Health":
                homeDeliveryLinearLayout.setVisibility(GONE);
                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);
                rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                principleRelativeLayout.setVisibility(GONE);
                break;
            case "Education":
                homeDeliveryLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(View.VISIBLE);
                hostelLinearLayout.setVisibility(View.VISIBLE);


                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);
                rateLinearLayout.setVisibility(GONE);
                principleRelativeLayout.setVisibility(View.VISIBLE);
                if(store.getStoreSubCategory().equals("School"))
                {
                    boardLinearLayout.setVisibility(View.VISIBLE);
                }
                if(store.getStoreSubCategory().equals("Book Store"))
                {
                    principleRelativeLayout.setVisibility(GONE);
                    homeDeliveryLinearLayout.setVisibility(View.VISIBLE);
                    transportLinearLayout.setVisibility(GONE);
                    hostelLinearLayout.setVisibility(GONE);
                }
                break;
            case "Booking":
                homeDeliveryLinearLayout.setVisibility(GONE);
                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);
                //rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                principleRelativeLayout.setVisibility(GONE);
                break;
            case "Agriculture":
                homeDeliveryLinearLayout.setVisibility(GONE);
                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);
                rateLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                principleRelativeLayout.setVisibility(GONE);
                break;
            case "Stay":
                homeDeliveryLinearLayout.setVisibility(GONE);
                rateLinearLayout.setVisibility(View.VISIBLE);
                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);

                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                principleRelativeLayout.setVisibility(GONE);
                break;
            case "Travel":
                homeDeliveryLinearLayout.setVisibility(GONE);
                if(store.getStoreSubCategory().equals("Taxi Car"))
                {
                    rateLinearLayout.setVisibility(View.VISIBLE);
                }
                showsLinearLayout.setVisibility(GONE);
                menuLinearLayout.setVisibility(GONE);
                hostelLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(GONE);
                principleRelativeLayout.setVisibility(GONE);
                workersRequiredLinearLayout.setVisibility(GONE);
                ;

        }
    }

    private void setDeleveryStatus(boolean deliveryStatus) {
        if(deliveryStatus)
        {
            tvHomeDelivery.setText(R.string.home_delivery);
            if(store.getStoreSubCategory().equals("Two wheeler misthri")||store.getStoreSubCategory().equals("Four wheeler misthri"))
                tvHomeDelivery.setText(R.string.point_delivery_avilable);
        }
        else
        {
            tvHomeDelivery.setText(R.string.no_home_delivery);
            if(store.getStoreSubCategory().equals("Two wheeler misthri")||store.getStoreSubCategory().equals("Four wheeler misthri"))
                tvHomeDelivery.setText(R.string.point_delivery_not_avialable);
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
        if(!days.get("tuesday"))
            tvTuseday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("wednesday"))
            tvWednesday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("thursday"))
            tvThrusday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("friday"))
            tvFriday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("saturday"))
            tvSaturday.setTextColor(getResources().getColor(R.color.red));
    }

    private void setWorkerRequiredStatus(boolean workersRequred) {

        if(workersRequred)
        {
            tvWorkers.setText(R.string.worker_required);
        }
        else
        {
            tvWorkers.setText(R.string.worker_not_required);
        }
    }

    public  void workerQualification(View view)
    {
        if(SUB_CATEGORY.equals("Academy")||SUB_CATEGORY.equals("Hospitals")||SUB_CATEGORY.equals("Health Clinics")||SUB_CATEGORY.equals("School")) {
            String massege = getString(R.string.not_added);
            if (store.getWqualification() != null)
                massege = store.getWqualification();
            AlertDialog.Builder builder = new AlertDialog.Builder(StoreActivity.this);
            builder.setTitle("Workers Qualifications");
            builder.setMessage(massege);

            AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.show();
        }


    }

    //start another activity
    public  void openShowActivity(View view)
    {
        Intent intent=new Intent(StoreActivity.this, CinemaShowsActivity.class);
        startActivity(intent);
    }

    public void addMenuItem(View view)
    {
        Intent intent=new Intent(StoreActivity.this, FoodMenuActivity.class);
        startActivity(intent);
    }

    public void goToBack(View view)
    {
        Intent intent=new Intent(StoreActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void applyAllChanges(View view)
    {
        final CustumProgressDialog dialog=new CustumProgressDialog(this);
        dialog.startProgressBar(getString(R.string.update));
        final HashMap<String,Object>hashMap=new HashMap<>();
        ApplicationClass.translatedData=new HashMap<>();
        if(STORE_NAME!=null)
        {
            hashMap.put("storeName",STORE_NAME);
            ApplicationClass.setTranslatedDataToMap("storeName",STORE_NAME);
            ApplicationClass.translatedData.put("sortStoreName",STORE_NAME.toLowerCase());
            hashMap.put("sortStoreName",STORE_NAME.toLowerCase());
        }
        if(STORE_ADDRESS!=null)
        {
            hashMap.put("storeAddress",STORE_ADDRESS);
            ApplicationClass.setTranslatedDataToMap("storeAddress",STORE_ADDRESS);
            ApplicationClass.translatedData.put("sortStoreAddress",STORE_ADDRESS.toLowerCase());
            hashMap.put("sortStoreAddress",STORE_ADDRESS.toLowerCase());
        }
         HashMap<String ,Object>Map=new HashMap<>();
        if(PHONE_1!=null)
        {
            Map.put("phone1",PHONE_1);
        }
        if(PHONE_2!=null) {
            Map.put("phone2", PHONE_2);
        }
        if(TIME_FROM!=null)
        {
            Map.put("timeFrom",TIME_FROM);
        }
        if(TIME_TO!=null)
        {
            Map.put("timeTo",TIME_TO);
        }
        if(DAYS!=null)
        {
            Map.put("days",DAYS);
        }
        if(SERVICE_RATE!=null)
        {
            Map.put("rate",SERVICE_RATE);
        }
        Map.put("deliveryStatus",DELIVERY_STATUS);
        Map.put("workersRequred",WORK_REQUIRED);
        if(BACKGROUND_URL!=null)
        {
            Map.put("backgroundImage",BACKGROUND_URL);
        }
       if(OWNER_IMAGE_URL!=null)
       {
           Map.put("ownerImage",OWNER_IMAGE_URL);
       }

        hashMap.putAll(Map);
        ApplicationClass.translatedData.putAll(Map);
        String des=etStoreDescription.getText().toString();
        if(!TextUtils.isEmpty(des))
        {
            hashMap.put("storeDescription",des);
            ApplicationClass.setTranslatedDataToMap("storeDescription",des);
        }
        if(SUB_CATEGORY.equals("Academy"))
        {
            if(QUALIFICATION!=null)
            {


                if(QUALIFICATION.equals(""))
                {
                    hashMap.put("wqualification",null);
                    ApplicationClass.translatedData.put("wqualification",null);
                }
                else{
                    ApplicationClass.setTranslatedDataToMap("wqualification",QUALIFICATION);
                    hashMap.put("wqualification",QUALIFICATION);

                }

            }

        }
        if(CATEGORY.equals("Education"))
        {
            hashMap.put("hostel",HOSTEL);
            hashMap.put("transport",TRANSPORT);
            ApplicationClass.translatedData.put("hostel",HOSTEL);
            ApplicationClass.translatedData.put("transport",TRANSPORT);
            if(PRINCIPAL_NAME!=null) {
                hashMap.put("principalName", PRINCIPAL_NAME);
                ApplicationClass.setTranslatedDataToMap("principalName", PRINCIPAL_NAME);
            }
            if(OWNER_NAME!=null)
            {
                hashMap.put("ownerName",OWNER_NAME);
                hashMap.put("sortOwnerName",OWNER_NAME.toLowerCase());
                ApplicationClass.setTranslatedDataToMap("ownerName",OWNER_NAME);
                ApplicationClass.translatedData.put("sortOwnerName",OWNER_NAME.toLowerCase());
            }
        }
        else
        {
            if(OWNER_NAME!=null) {
                hashMap.put("ownerName",OWNER_NAME);
                hashMap.put("sortOwnerName",OWNER_NAME.toLowerCase());
                ApplicationClass.setTranslatedDataToMap("ownerName",OWNER_NAME);
                ApplicationClass.translatedData.put("sortOwnerName",OWNER_NAME.toLowerCase());
            }
        }
        if(COMPANY_NAME!=null)
        {
            hashMap.put("companyName",COMPANY_NAME);
            ApplicationClass.setTranslatedDataToMap("companyName",COMPANY_NAME);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                db.collection("ensellers")
                        .document(mAuth.getUid()).update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            db.collection("hisellers")
                                    .document(mAuth.getUid()).update(ApplicationClass.translatedData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        recreate();
                                        dialog.stopProgressBar();
                                    }
                                    else
                                    {
                                        dialog.stopProgressBar();
                                        recreate();
                                        Toast.makeText(StoreActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                        else
                        {
                            dialog.stopProgressBar();
                            recreate();
                            Toast.makeText(StoreActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        },5000);

    }

    public void seeAllPhotos(View view)
    {
        Intent intent=new Intent(StoreActivity.this,HandleProductPhotosActivity.class);
        startActivity(intent);
    }

    public void setVisiblity(View view)
    {
        //pen icon
        updateStoreName.setVisibility(View.VISIBLE);
        updateStoreAddress.setVisibility(View.VISIBLE);
        updateStoreTime.setVisibility(View.VISIBLE);
        updateOwnerName.setVisibility(View.VISIBLE);
        updatePhone.setVisibility(View.VISIBLE);
        updateDays.setVisibility(View.VISIBLE);
        updateHomeDelivery.setVisibility(View.VISIBLE);
        updateWorkers.setVisibility(View.VISIBLE);
        updateRate.setVisibility(View.VISIBLE);
        updatePrincipleName.setVisibility(View.VISIBLE);
        updateTransport.setVisibility(View.VISIBLE);
        updateHostel.setVisibility(View.VISIBLE);
        updateCompanyName.setVisibility(View.VISIBLE);

        //Buttons
        btnApply.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);

        //ImageViews
        updateAll.setVisibility(GONE);

        //Layouts
        ratingLinearLayout.setVisibility(GONE);
        photosLinearLayout.setVisibility(GONE);
        showsLinearLayout.setVisibility(GONE);
        menuLinearLayout.setVisibility(GONE);
        boardLinearLayout.setVisibility(GONE);

        //extra
        etStoreDescription.setEnabled(true);
        isEdit=true;

    }

    public  void setVisibilityGone(View view)
    {
        etStoreDescription.setText(getString(R.string.more));
        recreate();
    }

    //update details
    public void updateCompanyName(View view)
    {
        UpdateCompanyName updateCompanyName=new UpdateCompanyName(StoreActivity.this,store.getCompanyName(),this);
        updateCompanyName.setCancelable(false);
        updateCompanyName.show();
    }
    public void updateRate(View view)
    {
        UpdateServiceRate updateServiceRate=new UpdateServiceRate(StoreActivity.this,store.getRate(),this);
        updateServiceRate.setCancelable(false);
        updateServiceRate.show();
    }
    public void updatePhoneNumber(View view)
    {
        if(store==null)
            Log.v("TAG","yes null");
        PhoneNumberUpdateDialog dialog=new PhoneNumberUpdateDialog(StoreActivity.this,store.getPhone1(),store.getPhone2(),this);
        dialog.setCancelable(false);
        dialog.show();
    }
    public void updateStoreName(View view)
    {
        UpdateStoreName updateStoreName=new UpdateStoreName(StoreActivity.this,store.getStoreName(),this);
        updateStoreName.setCancelable(false);
        updateStoreName.show();
    }
    public void updateTransport(View view)
    {
        UpdateTransportFacility updateTransportFacility=new UpdateTransportFacility(StoreActivity.this,store.isTransport(),this);
        updateTransportFacility.setCancelable(false);
        updateTransportFacility.show();
    }
    public void updateStoreAddress(View view)
    {
        UpdateStoreAddress updateStoreAddress=new UpdateStoreAddress(StoreActivity.this,store.getStoreAddress(),this);
        updateStoreAddress.setCancelable(false);
        updateStoreAddress.show();
    }
    public  void updateStoreTime(View view)
    {
        UpdateStoreTime updateStoreTime=new UpdateStoreTime(StoreActivity.this,store.getTimeFrom(),store.getTimeTo(),this);
        updateStoreTime.setCancelable(false);
        updateStoreTime.show();
    }
    public void updateOwnerName(View view)
    {
        UpdateOwnerName updateOwnerName=new UpdateOwnerName(StoreActivity.this,store.getOwnerName(),this);
        updateOwnerName.setCancelable(false);
        updateOwnerName.show();
    }
    public void updateDays(View view)
    {

        UpdateDays updateDays=new UpdateDays(StoreActivity.this,store.getDays(),this);
        updateDays.setCancelable(false);
        updateDays.show();
    }
    public void updateHomeDeliveryStatus(View view)
    {

        UpdateHomeDelivery updateHomeDelivery=new UpdateHomeDelivery(StoreActivity.this,store.isDeliveryStatus(),this);
       updateHomeDelivery.setCancelable(false);
        updateHomeDelivery.show();
    }
    public void updateWorkers(View view)
    {
        UpdateWorkerRequired updateWorkerRequired=new UpdateWorkerRequired(StoreActivity.this,
                store.isWorkersRequred(),this,store.getStoreSubCategory(),store.getWqualification());
        updateWorkerRequired.setCancelable(false);
        updateWorkerRequired.show();
    }
    public void backgroundClick(View view)
    {
        if(!isEdit)
        {
            Intent intent=new Intent(StoreActivity.this, SeeFullImageActivity.class);
            intent.putExtra("uri",store.getBackgroundImage());
            startActivity(intent);
        }
        else
        {
            openGellery(BACKGROUND);
        }
    }
    public void profileClick(View view)
    {
        if(!isEdit)
        {
            Intent intent=new Intent(StoreActivity.this, SeeFullImageActivity.class);
            intent.putExtra("uri",store.getOwnerImage());
            startActivity(intent);
        }
        else
        {
            openGellery(PROFILE);
        }
    }
    public void updatePrincipleName(View view)
    {
        UpdatePrincipleName updatePrincipleName=new UpdatePrincipleName(StoreActivity.this,store.getPrincipalName(),this);
        updatePrincipleName.setCancelable(false);
        updatePrincipleName.show();

    }
    public  void updateHostelService(View view)
    {
        UpdateHostelFacility updateHostelFacility=new UpdateHostelFacility(StoreActivity.this,store.isHostel(),this);
        updateHostelFacility.setCancelable(false);
        updateHostelFacility.show();
    }
    public void updateShopStatus(View view)
    {
        UpdateShopStatus dialog=new UpdateShopStatus(StoreActivity.this,store.isShopStatus());
        dialog.setCancelable(false);
        dialog.show();
    }



    //helpers
    public void openGellery(int type)
    {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,type);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==BACKGROUND)
            {
                Uri uri=data.getData();
                Log.d("TAG","uri"+uri);

                imgBackground.setImageURI(uri);
                upload(uri,requestCode);
            }
            else if(requestCode==PROFILE)
            {
                Uri uri=data.getData();
                imgProfile.setImageURI(uri);
                upload(uri,requestCode);
            }
        }
    }

    private void upload(final Uri uri, final int TYPE) {
        final CustumProgressDialog dialog = new CustumProgressDialog(StoreActivity.this);
        dialog.startProgressBar("uploading...");
        String childName = "extra";
        if (TYPE == BACKGROUND)
            childName = "background";
        else if (TYPE == PROFILE)
            childName = "profile";
        final StorageReference childRef = mStoreRef.child("images").child("sellers").child(mAuth.getUid()).child(childName);
        childRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String  URL = uri.toString();
                         if(TYPE==BACKGROUND)
                         {
                             Log.d("TAG","url  "+URL);
                             dialog.stopProgressBar();
                             Picasso.get().load(URL).into(imgBackground);
                            BACKGROUND_URL=URL;
                         }
                         else if(TYPE==PROFILE)
                         {
                             OWNER_IMAGE_URL=URL;
                             Picasso.get().load(URL).into(imgProfile);
                             dialog.stopProgressBar();
                         }
                    }
                });
            }
        });
    }



    //Interface mathodes
    @Override
    public void update(String place, List<String> value) {
        switch (place)
        {
            case "SERVICE_RATE":
                tvRate.setText(value.get(0));
                tvRate.setTextColor(getResources().getColor(R.color.red));
                SERVICE_RATE=value.get(0);
                break;
            case "PHONE":
                tvPhone1.setText(value.get(0));
                tvPhone2.setText(value.get(1));
                tvPhone1.setTextColor(getResources().getColor(R.color.red));
                tvPhone2.setTextColor(getResources().getColor(R.color.red));
                PHONE_1=value.get(0);
                PHONE_2=value.get(1);
                break;
            case "STORE_NAME":
                tvStoreName.setText(value.get(0));
                tvStoreName.setTextColor(getResources().getColor(R.color.red));
                STORE_NAME=value.get(0);
                break;
            case "OWNER_NAME":
                tvOwnerName.setText(value.get(0));
                tvOwnerName.setTextColor(getResources().getColor(R.color.red));
                OWNER_NAME=value.get(0);
                break;
            case "STORE_ADDRESS":
                tvStoreAddress.setText(value.get(0));
                tvStoreAddress.setTextColor(getResources().getColor(R.color.red));
                STORE_ADDRESS=value.get(0);
                break;
            case "PRINCIPLE_NAME":
                String fullName=value.get(0)+"("+getString(R.string.principle)+")";
                tvPrincipleName.setText(fullName);
                tvPrincipleName.setTextColor(getResources().getColor(R.color.red));
                PRINCIPAL_NAME=value.get(0);
                break;
            case "COMPANY_NAME":
                COMPANY_NAME=value.get(0);
                tvCompanyName.setText(COMPANY_NAME);
                tvCompanyName.setTextColor(getResources().getColor(R.color.red));
                break;
        }

    }
    @Override
    public void update(String place, List<String> value1, List<String> value2) {
        switch (place)
        {
            case "STORE_TIMING":
                String time=value1.get(0)+":"+value1.get(1)+value1.get(2)+"-"+value2.get(0)+":"+value2.get(1)+value2.get(2);
                tvStoreTime.setText(time);
                tvStoreTime.setTextColor(getResources().getColor(R.color.red));
                TIME_FROM=value1;
                TIME_TO=value2;
                break;
        }


    }

    @Override
    public void update(String place, HashMap<String, Boolean> value) {
        switch (place)
        {
            case "DAYS":
                setDays(value);
                DAYS=value;
                break;
        }

    }

    @Override
    public void update(String place, boolean value) {
        switch (place)
        {
            case "HOME_DELIVERY":
                tvHomeDelivery.setTextColor(getResources().getColor(R.color.red));
                setDeleveryStatus(value);
                DELIVERY_STATUS=value;
                break;
            case "HOSTEL":
                HOSTEL=value;
                sethostelStatus(value);
                tvHostel.setTextColor(getResources().getColor(R.color.red));
                break;
            case "TRANSPORT":
                TRANSPORT=value;
                setTransportStatus(value);
                tvTransport.setTextColor(getResources().getColor(R.color.red));
                break;

        }
    }

    @Override
    public void update(String place, boolean value1, String value2) {
        switch (place) {
            case "WORKERS":
                WORK_REQUIRED = value1;
                setWorkerRequiredStatus(value1);
                tvWorkers.setTextColor(getResources().getColor(R.color.red));
                QUALIFICATION=value2;
                break;
        }

    }


}
