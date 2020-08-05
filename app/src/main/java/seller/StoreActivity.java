package seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
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
import update_dialog.PhoneNumberUpdateDialog;
import update_dialog.UpdateDays;
import update_dialog.UpdateHomeDelivery;
import update_dialog.UpdateOwnerName;
import update_dialog.UpdateShopStatus;
import update_dialog.UpdateStoreAddress;
import update_dialog.UpdateStoreName;
import update_dialog.UpdateStoreTime;
import update_dialog.UpdateWorkerRequired;
import users.HomeActivity;
import users.SeeFullImageActivity;
import users.StoresActivity;

import static android.view.View.GONE;

public class StoreActivity extends AppCompatActivity implements UpdateInterface {
    //
    LinearLayout homeDeliveryView,timeLayout,dayLayout;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ImageView imageView;
    TextView textView;

    private  ImageView imgBackground;
    private CircleImageView imgProfile;
    private TextView tvStoreName,tvOwnerName,tvPhone1,tvPhone2,tvStoreAddress,tvStoreTime,tvHostel,tvTransport,tvBoard;
    TextView tvSunday,tvMonday,tvTuseday,tvWednesday,tvThrusday,tvFriday,tvSaturday;
    TextView tvHomeDelivery,tvWorkers,tvSeeAllPhotos,tvStoreDescription;
    ImageView updateAll;
    TextView tvRatings;

    public  static int BACKGROUND=1,PROFILE=2;
    CircleImageView imgUpdateAll;
    EditText etStoreDescription;

    ImageView updateStoreName,updateOwnerName,updatePhone,updateStoreAddress,
            updateStoreTime,updateDays,updateHomeDelivery,updateWorkers,updatePhotos,updateHostel,updateTransport;

    Button btnApply,btnCancel;

    LinearLayout ratingLayout,photosLayout,hostelLayout,transportLayout,showsLayout;

    Seller store=null;

    List<String>currentFromTime,currentToTime;
//    HashMap<String,Boolean>newDays;
//    boolean newHomeDeliveryStatus;
//    private boolean newWorkers,newHOSTEL,newTRANSPORT;
//    Double newStoreLatitude,newStoreLongitude;
    String CATEGORY="";

//    String newBackgroundImageUri=null,newProfileImageuri=null;
    boolean SHOP_STATUS=true;
    boolean isEdit=false;
    //
    public static String PHONE_1=null,PHONE_2=null,STORE_NAME=null,STORE_ADDRESS=null,OWNER_NAME=null,PRINCIPAL_NAME=null,BACKGROUND_URL=null,OWNER_IMAGE_URL=null;
    public static boolean HOSTEL=false,DELIVERY_STATUS=false,WORK_REQUIRED=false,TRANSPORT=false;
    public static Map<String ,Boolean>DAYS=null;
    public  static List<String>TIME_FROM=null,TIME_TO=null;
    public static double STORE_LONITUDE=0.0,STORE_LATITUDE=0.0;
    StorageReference mStoreRef;
    public  static HashMap<String,Object>updatedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(this);
        setContentView(R.layout.activity_store);

        isEdit=false;
        homeDeliveryView=findViewById(R.id.home_delivery_view);
        final ProgressDialog dialog=new ProgressDialog(StoreActivity.this);

    updatedData=new HashMap<>();

        imageView=findViewById(R.id.img_shop_status);
        textView=findViewById(R.id.tv_shop_status);

        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        mStoreRef= FirebaseStorage.getInstance().getReference();

        imgBackground=findViewById(R.id.img_background);
        imgProfile=findViewById(R.id.img_profile);

        tvStoreName=findViewById(R.id.tv_store_name);
        tvOwnerName=findViewById(R.id.tv_owner_name);
        tvPhone1=findViewById(R.id.tv_phone_1);
        tvPhone2=findViewById(R.id.tv_phone_2);
        tvStoreAddress=findViewById(R.id.tv_store_address);
        tvStoreTime=findViewById(R.id.tv_store_timing);
        tvBoard=findViewById(R.id.tv_board_name);

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
        tvHostel=findViewById(R.id.tv_hostel);
        tvTransport=findViewById(R.id.tv_transport);

        tvRatings=findViewById(R.id.tv_rating_bar);

        updateAll=findViewById(R.id.img_change);

        updateStoreName=findViewById(R.id.update_store_name);
        updateOwnerName=findViewById(R.id.update_owner_name);
        updateStoreAddress=findViewById(R.id.update_store_address);
        updateStoreTime=findViewById(R.id.update_store_time);
        updatePhone=findViewById(R.id.update_phone);
        updateDays=findViewById(R.id.update_days);
        updateHomeDelivery=findViewById(R.id.update_delivery_status);
        updateWorkers=findViewById(R.id.update_workers);
        updatePhotos=findViewById(R.id.update_all_photos);
        updateHostel=findViewById(R.id.update_hostel);
        updateTransport=findViewById(R.id.update_transport);


        btnApply=findViewById(R.id.btn_apply);
        btnCancel=findViewById(R.id.btn_cancel);

        imgUpdateAll=findViewById(R.id.img_change);
        ratingLayout=findViewById(R.id.rating_layout);
        photosLayout=findViewById(R.id.photos_layout);
        hostelLayout=findViewById(R.id.hostel_layout);
        transportLayout=findViewById(R.id.transprt_layout);
        showsLayout=findViewById(R.id.show_layout);

        timeLayout=findViewById(R.id.time_layout);
        // dayLayout=findViewById(R.id.layout_day);

        etStoreDescription=findViewById(R.id.et_shop_description);
        tvStoreDescription=findViewById(R.id.tv_shop_description);


        currentFromTime=new ArrayList<>();
        currentToTime=new ArrayList<>();
//        newDays=new HashMap<>();

        setAllData();

    }
    private void setAllData() {
        String path=ApplicationClass.LANGUAGE_MODE+"sellers";
        db.collection(path).document(mAuth.getUid())

                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    Seller seller=documentSnapshot.toObject(Seller.class);
                    store=seller;
                    if(seller.getBackgroundImage()!=null)
                    {
                        Picasso.get().load(seller.getBackgroundImage()).into(imgBackground);
                        imgBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    if(seller.getOwnerImage()!=null)
                        Picasso.get().load(seller.getOwnerImage()).into(imgProfile);


                    tvStoreName.setText(seller.getStoreName());

                    tvOwnerName.setText(seller.getOwnerName());
                    tvPhone1.setText(seller.getPhone1());
                    tvPhone2.setText(seller.getPhone2());
                    tvStoreAddress.setText(seller.getStoreAddress());
                    if(seller.getStoreDescription()!=null)
                        etStoreDescription.setText(seller.getStoreDescription());
                    etStoreDescription.setEnabled(false);

                    List<String> timeListFrom=seller.getTimeFrom();
                    List<String> timeListTo=seller.getTimeTo();

                    currentFromTime=timeListFrom;
                    currentToTime=timeListTo;

                    CATEGORY=seller.getStoreCategory();
                    String storeTiming=timeListFrom.get(0)+":"+timeListFrom.get(1)+timeListFrom.get(2)+"-"
                            +timeListTo.get(0)+":"+timeListTo.get(1)+timeListTo.get(2);
                    tvStoreTime.setText(storeTiming);

                    HashMap<String,Boolean>days=seller.getDays();
                    setDays(days);


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



                    //image
                BACKGROUND_URL=seller.getBackgroundImage();
                  OWNER_IMAGE_URL=seller.getOwnerImage();

                    //
                    if(seller.getBoardName()!=null)
                        tvBoard.setText(seller.getBoardName());
                  //  ApplicationClass.setTranslatedText(tvBoard,seller.getBoardName());
                    if(seller.getPrincipalName()!=null)
                        //ApplicationClass.setTranslatedText(tvOwnerName,seller.getPrincipalName());
                        tvOwnerName.setText(seller.getPrincipalName());
                    sethostelStatus(seller.isHostel());
                    setTransportStatus(seller.isTransport());
                    HOSTEL=seller.isHostel();
                    TRANSPORT=seller.isTransport();

                    setDataVisiblity(seller.getStoreCategory());
                    SHOP_STATUS=seller.isShopStatus();
                    setShopStatus(seller.isShopStatus());

                }

            }


        });
    }
    private void setShopStatus(boolean isOpen) {

        if(isOpen)
        {

            imageView.setImageResource(R.drawable.ic_right_green);
            textView.setText(R.string.open);
            textView.setTextColor(getResources().getColor(R.color.green));
        }
        else
        {
            imageView.setImageResource(R.drawable.ic_cross_red);
            textView.setText(R.string.close);
            textView.setTextColor(getResources().getColor(R.color.red));
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
                homeDeliveryView.setVisibility(View.VISIBLE);
                break;
            case "Food":
                homeDeliveryView.setVisibility(View.VISIBLE);
            case "Gym Sports":
                homeDeliveryView.setVisibility(View.GONE);
                break;
            case "Shows and Cinema":
                homeDeliveryView.setVisibility(View.GONE);
                showsLayout.setVisibility(View.VISIBLE);
                break;
            case "Cyber":
                homeDeliveryView.setVisibility(View.GONE);
                break;
            case "Vehicles and Workshop":
                homeDeliveryView.setVisibility(View.GONE);
                break;
            case "Buliding Material":
                homeDeliveryView.setVisibility(GONE);
                break;
            case "Rentals":
                homeDeliveryView.setVisibility(GONE);
                break;
            case "Factories":
                homeDeliveryView.setVisibility(GONE);
                timeLayout.setVisibility(GONE);
                dayLayout.setVisibility(GONE);
                break;
            case "Bank and Atm":
                homeDeliveryView.setVisibility(GONE);
                break;
            case "Pentrol Pump":
                homeDeliveryView.setVisibility(GONE);
                break;
            case "NGO and Club":
                homeDeliveryView.setVisibility(GONE);
                break;
            case "Parlourl Saloon":
                homeDeliveryView.setVisibility(GONE);
                break;
            case "Electronics":
                homeDeliveryView.setVisibility(GONE);
                break;
            case "Health":
                homeDeliveryView.setVisibility(GONE);
                break;
            case "Education":
                homeDeliveryView.setVisibility(GONE);
                transportLayout.setVisibility(View.VISIBLE);
                hostelLayout.setVisibility(View.VISIBLE);
                tvBoard.setVisibility(View.VISIBLE);
                break;
            case "Booking":
                homeDeliveryView.setVisibility(GONE);
                break;
            case "Agriculture":
                homeDeliveryView.setVisibility(GONE);
                break;
            case "Stay":
                homeDeliveryView.setVisibility(GONE);
                break;



        }
    }
    public  void openShowActivity(View view)
    {
        Intent intent=new Intent(StoreActivity.this, CinemaShowsActivity.class);
        startActivity(intent);
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
    private void setDeleveryStatus(boolean deliveryStatus) {
        if(deliveryStatus)
        {
            tvHomeDelivery.setText(R.string.home_delivery);
        }
        else
        {
            tvHomeDelivery.setText(R.string.no_home_delivery);
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
        Map.put("deliveryStatus",DELIVERY_STATUS);
        Map.put("workersRequred",WORK_REQUIRED);
        if(!(STORE_LATITUDE!=0.0)&&!(STORE_LONITUDE!=0.0)) {
            Map.put("storeLongitude", STORE_LONITUDE);
            Map.put("storeLatitude", STORE_LATITUDE);
            GeoPoint geoPoint = new GeoPoint(STORE_LATITUDE, STORE_LONITUDE);
            Map.put("geoPoint", geoPoint);
        }
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

        if(CATEGORY.equals("Education"))
        {
            hashMap.put("hostel",HOSTEL);
            hashMap.put("transport",TRANSPORT);
            if(PRINCIPAL_NAME!=null)
            hashMap.put("principalName",PRINCIPAL_NAME);
            ApplicationClass.setTranslatedDataToMap("principalName",PRINCIPAL_NAME);
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                db.collection("ensellers")
                        .document(mAuth.getUid()).update(updatedData).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        updateStoreName.setVisibility(View.VISIBLE);
        updateStoreAddress.setVisibility(View.VISIBLE);
        updateStoreTime.setVisibility(View.VISIBLE);
        updateOwnerName.setVisibility(View.VISIBLE);
        updatePhone.setVisibility(View.VISIBLE);
        updateDays.setVisibility(View.VISIBLE);
        updateHomeDelivery.setVisibility(View.VISIBLE);
        updateWorkers.setVisibility(View.VISIBLE);
        btnApply.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);

        etStoreDescription.setEnabled(true);

        ratingLayout.setVisibility(GONE);
        imgUpdateAll.setVisibility(GONE);
        photosLayout.setVisibility(GONE);
        showsLayout.setVisibility(GONE);
        updateTransport.setVisibility(GONE);
        updateHostel.setVisibility(GONE);

        isEdit=true;

    }
    public void updatePhoneNumber(View view)
    {
        if(store==null)
            Log.v("TAG","yes null");
        PhoneNumberUpdateDialog dialog=new PhoneNumberUpdateDialog(StoreActivity.this,store.getPhone1(),store.getPhone2(),this);
        dialog.show();
    }
    public void updateStoreName(View view)
    {
        UpdateStoreName updateStoreName=new UpdateStoreName(StoreActivity.this,store.getStoreName(),this);
        updateStoreName.show();
    }
    public void updateStoreAddress(View view)
    {
        UpdateStoreAddress updateStoreAddress=new UpdateStoreAddress(StoreActivity.this,store.getStoreAddress(),this);
        updateStoreAddress.show();
    }
    public  void updateStoreTime(View view)
    {
        UpdateStoreTime updateStoreTime=new UpdateStoreTime(StoreActivity.this,currentFromTime,currentToTime,this);
        updateStoreTime.show();
    }
    public void updateOwnerName(View view)
    {
        UpdateOwnerName updateOwnerName=new UpdateOwnerName(StoreActivity.this,store.getOwnerName(),this);
        updateOwnerName.show();
    }
    public void updateDays(View view)
    {

        UpdateDays updateDays=new UpdateDays(StoreActivity.this,store.getDays(),this);
        updateDays.show();
    }
    public void updateHomeDeliveryStatus(View view)
    {

        UpdateHomeDelivery updateHomeDelivery=new UpdateHomeDelivery(StoreActivity.this,store.isDeliveryStatus(),this);
        updateHomeDelivery.show();
    }
    public void updateWorkers(View view)
    {
        UpdateWorkerRequired updateWorkerRequired=new UpdateWorkerRequired(StoreActivity.this,WORK_REQUIRED,this);
        updateWorkerRequired.show();
    }
    public void backgroundClick(View view)
    {
        if(!isEdit)
        {
            Intent intent=new Intent(StoreActivity.this, SeeFullImageActivity.class);
            intent.putExtra("uri",BACKGROUND_URL);
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
            intent.putExtra("uri",OWNER_IMAGE_URL);
            startActivity(intent);
        }
        else
        {
            openGellery(PROFILE);
        }
    }
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
                             //updatedData.put("backgroundImage",URL);

                            BACKGROUND_URL=URL;
                         }
                         else if(TYPE==PROFILE)
                         { // updatedData.put("ownerImage",URL);
                             OWNER_IMAGE_URL=URL;


                         }

                    }
                });
            }
        });
    }

    public  void setVisibilityGone(View view)
    {
        updateStoreName.setVisibility(GONE);
        updateStoreAddress.setVisibility(GONE);
        updateStoreTime.setVisibility(GONE);
        updateOwnerName.setVisibility(GONE);
        updatePhone.setVisibility(GONE);
        updateDays.setVisibility(GONE);
        updateHomeDelivery.setVisibility(GONE);
        updateWorkers.setVisibility(GONE);
        btnApply.setVisibility(View.VISIBLE);
        imgUpdateAll.setVisibility(GONE);

        etStoreDescription.setEnabled(false);
        isEdit=false;
        recreate();

    }
    public void updateShopStatus(View view)
    {
        UpdateShopStatus dialog=new UpdateShopStatus(StoreActivity.this,SHOP_STATUS);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void update(String place, List<String> value) {
        switch (place)
        {
            case "PHONE":
                tvPhone1.setText(value.get(0));
                tvPhone2.setText(value.get(1));
                tvPhone1.setTextColor(getResources().getColor(R.color.red));
                tvPhone2.setTextColor(getResources().getColor(R.color.red));
//                updatedData.put("phone1",value.get(0));
//                updatedData.put("phone2",value.get(1));
                PHONE_1=value.get(0);
                PHONE_2=value.get(1);
                break;
            case "STORE_NAME":
                tvStoreName.setText(value.get(0));
                tvStoreName.setTextColor(getResources().getColor(R.color.red));
//                updatedData.put("storeName",value.get(0));
//                updatedData.put("sortStoreName", value.get(0).toLowerCase())
//
                STORE_NAME=value.get(0);
                break;
            case "OWNER_NAME":
                tvOwnerName.setText(value.get(0));
                tvOwnerName.setTextColor(getResources().getColor(R.color.red));
                if(CATEGORY.equals("Education"))
                {

//                    updatedData.put("principalName",value.get(0));
                    PRINCIPAL_NAME=value.get(0);

                }
                else {
//                    updatedData.put("ownerName", value.get(0));
                    OWNER_NAME=value.get(0);
//                    updatedData.put("sortOwnerName",value.get(0).toLowerCase());
                }
                break;

        }

    }

    @Override
    public void update(String place, List<String> value1, List<String> value2) {
        switch (place)
        {
            case "STORE_TIMING":
//                newFromTime=value1;
//                newToTime=value2;
                String time=value1.get(0)+":"+value1.get(1)+value1.get(2)+"-"+value2.get(0)+":"+value2.get(1)+value2.get(2);
                tvStoreTime.setText(time);
                tvStoreTime.setTextColor(getResources().getColor(R.color.red));
//                updatedData.put("timeFrom",value1);
//                updatedData.put("timeTo",value2);
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
//                newDays=value;
                setDays(value);
//                updatedData.put("days",value);
                DAYS=value;
                break;


        }

    }

    @Override
    public void update(String place, boolean value) {
        switch (place)
        {
            case "HOME_DELIVERY":
//                newHomeDeliveryStatus=value;
                tvHomeDelivery.setTextColor(getResources().getColor(R.color.red));
               // updatedData.put("deliveryStatus",value);
                setDeleveryStatus(value);
                DELIVERY_STATUS=value;
                break;
            case "WORKERS":
                //updatedData.put("workersRequred",value);
                WORK_REQUIRED=value;
//                newWorkers=value;
                setWorkerRequiredStatus(value);
                tvWorkers.setTextColor(getResources().getColor(R.color.red));
                break;
            case "HOSTEL":
                HOSTEL=value;
               // updatedData.put("hostel",value);
//                newHOSTEL=value;
                sethostelStatus(value);
                tvHostel.setTextColor(getResources().getColor(R.color.red));
                break;
            case "TRANSPORT":
                //updatedData.put("transport",value);
                TRANSPORT=value;
//                newTRANSPORT=value;
                setTransportStatus(value);
                tvTransport.setTextColor(getResources().getColor(R.color.red));
                break;

        }
    }

    @Override
    public void update(String place, List<String> value, Double logitude, Double latitude) {
        switch (place)
        {
            case "STORE_ADDRESS":
                //updatedData.put("storeAddress",value.get(0));
                tvStoreAddress.setText(value.get(0));
                tvStoreAddress.setTextColor(getResources().getColor(R.color.red));
//                newStoreLatitude=latitude;
//                newStoreLongitude=logitude;
//                updatedData.put("storeLongitude",value);
//                updatedData.put("storeLatitude",value);

//                updatedData.put("geoPoint",geoPoint);
//                updatedData.put("sortStoreAddress",value.get(0).toLowerCase());
                STORE_ADDRESS=value.get(0);
                STORE_LONITUDE=logitude;
                STORE_LATITUDE=latitude;

                break;
        }
    }
}
