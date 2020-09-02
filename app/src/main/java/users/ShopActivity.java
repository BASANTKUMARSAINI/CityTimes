package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.MediaRouteButton;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.protobuf.DescriptorProtos;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import dialog.RatingDialog;
import model.ApplicationClass;
import model.CollectionType;
import model.Item;
import model.MoveShow;
import model.ProductImage;
import model.Seller;

import services.LocationService;
import view_holder.FoodMenuViewHolder;
import view_holder.ProductCollectionViewHolder;
import view_holder.ProductImageViewHolder;
import view_holder.ShowViewHolder;

import static android.view.View.GONE;

public class ShopActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST = 1;
    //web services
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    //Layouts
    private LinearLayout rateLinearLayout,hostelLinearLayout
            ,transportLinearLayout,homeDeliveryLinearLayout,timeLinearLayout,dayLinearLayout,boardLinearLayout,
            descriptionLinerLayout,workersQualificationLinearLayout,workersRequiredLinearLayout, changeShopStatusLinearLayout,contactLinearLayout;
    private RelativeLayout principleRelativeLayout,itemsRelativeLayout,showRelativeLayout,photosRelativeLayout,companyNameRelativeLayout;

    //TetViews
    private TextView tvStoreName,tvOwnerName,tvPhone1,tvPhone2,tvStoreAddress,tvStoreTime,tvHostel,tvTransport,tvBoard,
             tvComma,tvRate,tvPrincipleName,tvWorkersQualification,tvLastUpdate,tvCompanyName;
    private TextView tvSunday,tvMonday,tvTuseday,tvWednesday,tvThrusday,tvFriday,tvSaturday;
    private TextView tvHomeDelivery,tvWorkers;
    private TextView tvRatings;

    //ImageViews
    private ImageView imgBackground,imgSave;
    private CircleImageView imgProfile;


    //recycler view
    private RecyclerView recyclerViewItems,recyclerView,recyclerViewShow;

    //adapter
    FirestoreRecyclerAdapter<CollectionType, ProductCollectionViewHolder>adapter;
    FirestoreRecyclerAdapter<Item,FoodMenuViewHolder> adapterItem;

    //extras
    private String sUid;
    private String ownerImageUrl=null;
    private static final int REQUEST_LOCATION=1;
    LocationManager locationManager;
    private boolean isStoreSaved=false;

    private String favoriteStore=null;
    private Double STORE_LONGITUDE=1.0,STORE_LATITUDE=1.0;
    private  TextView tvShopDescription;
    private  Seller store=null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(this);
        setContentView(R.layout.activity_shop);
        initializeViews();
        setBookMarks();

        setShows();
        setItems();
        setAllData();

    }

    private void setAutoData() {

        if(store.getStoreSubCategory().equals("Auto"))
        {

            setUpForTravel();
            final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("current_location").child(sUid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Double latitude=snapshot.child("location").child("latitude").getValue(Double.class);
                    Double longitude=snapshot.child("location").child("longitude").getValue(Double.class);

                            STORE_LATITUDE=latitude;
                            STORE_LONGITUDE=longitude;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void initializeViews() {
        //web services
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        //layouts
        itemsRelativeLayout=findViewById(R.id.items_relative_layout);
        rateLinearLayout=findViewById(R.id.rate_linear_layout);
        photosRelativeLayout=findViewById(R.id.photos_relative_layout);
        hostelLinearLayout=findViewById(R.id.hostel_linear_layout);
        transportLinearLayout=findViewById(R.id.transprot_linear_layout);
        showRelativeLayout=findViewById(R.id.show_relative_layout);
        timeLinearLayout=findViewById(R.id.time_linear_layout);
        homeDeliveryLinearLayout=findViewById(R.id.home_delivery_linear_layout);
        dayLinearLayout=findViewById(R.id.day_linear_layout);
        boardLinearLayout=findViewById(R.id.board_linear_layout);
        descriptionLinerLayout=findViewById(R.id.description_linear_layout);
        workersQualificationLinearLayout=findViewById(R.id.workers_qualification_linear_layout);
        workersRequiredLinearLayout=findViewById(R.id.workers_linear_layout);
        companyNameRelativeLayout=findViewById(R.id.company_name_relative_layout);
        contactLinearLayout=findViewById(R.id.contact_linear_layout);

        principleRelativeLayout=findViewById(R.id.principle_relative_layout);
        changeShopStatusLinearLayout=findViewById(R.id.change_shop_status);

        //TextViews
        tvRatings=findViewById(R.id.tv_rating_bar);
        tvRate=findViewById(R.id.tv_rate);
        tvComma=findViewById(R.id.tv_comma);
        tvStoreName=findViewById(R.id.tv_store_name);
        tvOwnerName=findViewById(R.id.tv_owner_name);
        tvPhone1=findViewById(R.id.tv_phone_1);
        tvPhone2=findViewById(R.id.tv_phone_2);
        tvStoreAddress=findViewById(R.id.tv_store_address);
        tvStoreTime=findViewById(R.id.tv_store_timing);
        tvBoard=findViewById(R.id.tv_board_name);
        tvPrincipleName=findViewById(R.id.tv_principle_name);
        tvShopDescription=findViewById(R.id.tv_shop_description);
        tvWorkersQualification=findViewById(R.id.tv_workers_qualifications);
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
        imgBackground=findViewById(R.id.background_image);
        imgProfile=findViewById(R.id.img_profile);

        //extras
        sUid=getIntent().getStringExtra("sUid");

        recyclerView=findViewById(R.id.recycler_view_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewShow=findViewById(R.id.recycler_view_show);
        recyclerViewShow.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewItems=findViewById(R.id.recycler_view_items);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));

        imgSave=findViewById(R.id.img_bookmarks);


    }
    private void setItems() {
        Query query=db.collection("items").whereEqualTo("uid",sUid);
            FirestoreRecyclerOptions<Item> options=new FirestoreRecyclerOptions.Builder<Item>()
                    .setQuery(query,Item.class)
                    .build();
            adapterItem=new FirestoreRecyclerAdapter<Item, FoodMenuViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull FoodMenuViewHolder holder, int position, @NonNull final Item model) {

                    holder.tvItemPrice.setText(model.getPrice());
                    holder.tvItemName.setText(model.getName());
                    if(ApplicationClass.LANGUAGE_MODE.equals("hi"))
                        holder.tvItemName.setText(model.getHname());
                    holder.btnDelete.setVisibility(GONE);
                }

                @NonNull
                @Override
                public FoodMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
                    return new FoodMenuViewHolder(view);
                }
            };
            recyclerViewItems.setAdapter(adapterItem);
            adapterItem.startListening();


    }

    private void setBookMarks() {
        db.collection("en"+"users").document(mAuth.getUid()).collection("favoriteStores").document(sUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
        String path=ApplicationClass.LANGUAGE_MODE+"sellers";
        db.collection(path).document(sUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    final Seller seller = documentSnapshot.toObject(Seller.class);
                    store=seller;
                    favoriteStore=seller.getsUid();

                    if (seller.getBackgroundImage() != null) {
                        Picasso.get().load(seller.getBackgroundImage()).into(imgBackground);
                        imgBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        ownerImageUrl = seller.getBackgroundImage();
                        imgBackground.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ShopActivity.this, SeeFullImageActivity.class);
                                intent.putExtra("uri", seller.getBackgroundImage());
                                startActivity(intent);
                            }
                        });
                    }


                    if (seller.getOwnerImage() != null)
                    {
                        Picasso.get().load(seller.getOwnerImage()).into(imgProfile);
                        ownerImageUrl=seller.getOwnerImage();
                        imgProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(ShopActivity.this, SeeFullImageActivity.class);
                                intent.putExtra("uri",seller.getOwnerImage());
                                startActivity(intent);
                            }
                        });
                    }

                    tvStoreName.setText(seller.getStoreName());
                    tvOwnerName.setText(seller.getOwnerName());
                    tvPhone1.setText(seller.getPhone1());
                    if(TextUtils.isEmpty(seller.getPhone1()))
                    {
                        contactLinearLayout.setVisibility(GONE);
                    }
                    if(seller.getPhone2()!=null)
                    {
                        tvComma.setVisibility(View.VISIBLE);
                        tvPhone2.setText(seller.getPhone2());
                    }
                    tvStoreAddress.setText(seller.getStoreAddress());

                    if(seller.getStoreDescription()!=null)
                    {
                        descriptionLinerLayout.setVisibility(View.VISIBLE);
                        tvShopDescription.setText(seller.getStoreDescription());
                    }
                    String SUB_CATEGORY=seller.getStoreSubCategory();
                    if((seller.getWqualification()!=null)&&
                            (SUB_CATEGORY.equals("Academy")||SUB_CATEGORY.equals("Hospitals")||SUB_CATEGORY.equals("Health Clinics")
                                    ||SUB_CATEGORY.equals("School"))&&(seller.isWorkersRequred()))
                    {
                        workersQualificationLinearLayout.setVisibility(View.VISIBLE);
                        tvWorkersQualification.setText(seller.getWqualification());
                    }


                        List<String> timeListFrom = seller.getTimeFrom();
                        List<String> timeListTo = seller.getTimeTo();

                        String storeTiming = timeListFrom.get(0) + ":" + timeListFrom.get(1) + timeListFrom.get(2) + "-"
                                + timeListTo.get(0) + ":" + timeListTo.get(1) + timeListTo.get(2);
                        tvStoreTime.setText(storeTiming);

                    setDays(seller.getDays());


                    setDeleveryStatus(seller.isDeliveryStatus());

                    setWorkerRequiredStatus(seller.isWorkersRequred());
                    setAutoData();


                    STORE_LATITUDE=seller.getStoreLatitude();
                    STORE_LONGITUDE=seller.getStoreLongitude();
                    Double totalStar = seller.getTotalStar();
                    Integer totalRatings = seller.getNoOfRatings();
                    if (totalRatings == 0)
                        tvRatings.setText("0.0");
                    else {
                        double ratings =  totalStar / totalRatings;

                        tvRatings.setText(ratings + "");
                    }


                    setDataVisiblity(seller.getStoreCategory());
                    setShopStatus(seller.isShopStatus());
                    if(seller.getRate()!=null)
                    {
                        tvRate.setText(seller.getRate()+"");
                    }
                    setupForEducation(seller);
                    setUpForFood(seller);
                    setUpForTravel();
                    setUpForVehical();
                    setUpForBooking();
                    setUpForNGO();
                    setUpforBank();

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
    private void setUpForFood(Seller store) {
        if(store.getStoreCategory().equals("Stay"))
        {
            if(store.getRate()!=null)
                tvRate.setText(store.getRate()+"");
        }
    }
    private void setupForEducation(Seller store) {
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
        }
    }

    private void setShopStatus(boolean isOpen) {
        ImageView imgShopStatus=findViewById(R.id.img_shop_status);
        TextView tvShopStatus=findViewById(R.id.tv_shop_status);
//        boolean condition=store.getStoreCategory().equals("Travel")||store.getStoreSubCategory().equals("Sound box(DJ)")
//                ||store.getStoreSubCategory().equals("JCB and Crane");
//        if(isOpen)
//        {
//
//            imageView.setImageResource(R.drawable.ic_right_green);
//            textView.setText(R.string.open);
//            if(condition)
//            {
//                textView.setText(R.string.free);
//            }
//            textView.setTextColor(getResources().getColor(R.color.green));
//        }
//        else
//        {
//            imageView.setImageResource(R.drawable.ic_cross_red);
//            textView.setText(R.string.close);
//            if(condition)
//            {
//                textView.setText(R.string.booked);
//            }
//            textView.setTextColor(getResources().getColor(R.color.red));
//        }
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
        if(days!=null) {
            tvSunday.setTextColor(getResources().getColor(R.color.green));
            tvMonday.setTextColor(getResources().getColor(R.color.green));
            tvTuseday.setTextColor(getResources().getColor(R.color.green));
            tvWednesday.setTextColor(getResources().getColor(R.color.green));
            tvThrusday.setTextColor(getResources().getColor(R.color.green));
            tvFriday.setTextColor(getResources().getColor(R.color.green));
            tvSaturday.setTextColor(getResources().getColor(R.color.green));

            if (!days.get("sunday"))
                tvSunday.setTextColor(getResources().getColor(R.color.red));
            if (!days.get("monday"))
                tvMonday.setTextColor(getResources().getColor(R.color.red));
            if (!days.get("tuesday"))
                tvTuseday.setTextColor(getResources().getColor(R.color.red));
            if (!days.get("wednesday"))
                tvWednesday.setTextColor(getResources().getColor(R.color.red));
            if (!days.get("thursday"))
                tvThrusday.setTextColor(getResources().getColor(R.color.red));
            if (!days.get("friday"))
                tvFriday.setTextColor(getResources().getColor(R.color.red));
            if (!days.get("saturday"))
                tvSaturday.setTextColor(getResources().getColor(R.color.red));
        }
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
                final HashMap<String,Object>hashMap=new HashMap<>();
                hashMap.put("sUid",favoriteStore);
                db.collection("en"+"users").document(mAuth.getUid())
                        .collection("favoriteStores").document(sUid).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection("hi"+"users").document(mAuth.getUid())
                                .collection("favoriteStores").document(sUid).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ShopActivity.this,"saved",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                imgSave.setImageResource(R.drawable.bookmark_border);
                                Toast.makeText(ShopActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        imgSave.setImageResource(R.drawable.bookmark_border);
                        Toast.makeText(ShopActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
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
            db.collection("en"+"users").document(mAuth.getUid()).collection("favoriteStores")
                    .document(sUid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    db.collection("hi"+"users").document(mAuth.getUid())
                            .collection("favoriteStores").document(sUid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ShopActivity.this,"deleted",Toast.LENGTH_LONG).show();
                        }
                    });


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

    private void onGps() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(R.string.enable_gps).setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog dialog=builder.create();
        dialog.show();

    }
    public void shareStore(View view)
    {
        ApplicationClass.shareImage(ownerImageUrl,"kkkkk",ShopActivity.this);

    }
    public void setStoreRatings(View view)
    {
        RatingDialog dialog=new RatingDialog(ShopActivity.this,sUid);
        dialog.show();
    }
    public void makePhoneCall(View view)
    {
        Intent intent=new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+tvPhone1.getText().toString()));
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        db.collection("productCollections").whereEqualTo("uid",sUid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty())
                {
                    photosRelativeLayout.setVisibility(View.VISIBLE);
                    Log.v("TAG","inini"+sUid);//productCollections
                    final Query query=db.collection("productCollections").whereEqualTo("uid",sUid);
                    FirestoreRecyclerOptions<CollectionType> options=new FirestoreRecyclerOptions.Builder<CollectionType>()
                            .setQuery(query,CollectionType.class)
                            .build();
                    adapter=new FirestoreRecyclerAdapter<CollectionType, ProductCollectionViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ProductCollectionViewHolder holder, int position, @NonNull final CollectionType model) {

                            String collectionName=model.getCollectionName();
                            Log.v("TAG","inini"+collectionName+"name");
                            holder.tvCollectionType.setText(collectionName);
                            if(ApplicationClass.LANGUAGE_MODE.equals("hi"))
                                holder.tvCollectionType.setText(model.getHicollectionName());
                            //  holder.setCollectionType(collectionName);
                            holder.mainLayout.setBackgroundColor(getResources().getColor(R.color.other_background));
                            Query query1=db.collection("productImages").whereEqualTo("uid",sUid)
                                    .whereEqualTo("collectionName",collectionName);
                            FirestoreRecyclerOptions<ProductImage>options1=new FirestoreRecyclerOptions.Builder<ProductImage>()
                                    .setQuery(query1,ProductImage.class)
                                    .build();
                            FirestoreRecyclerAdapter<ProductImage, ProductImageViewHolder>adapter1=new FirestoreRecyclerAdapter<ProductImage,ProductImageViewHolder>(options1) {
                                @Override
                                protected void onBindViewHolder(@NonNull ProductImageViewHolder holder, int position, @NonNull final ProductImage model1) {
                                    holder.setProductImage(model1.getImage());

                                    holder.tvProductPrice.setText(model1.getPrice()+"$");
                                    if(model1.getPrice()==null)
                                        holder.tvProductPrice.setVisibility(GONE);
                                    holder.btnDelete.setImageResource(R.drawable.share);
                                    holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String text="Price:"+model1.getPrice()+"$";
                                            ApplicationClass.shareImage(model1.getImage(),text,ShopActivity.this);
                                        }
                                    });
                                    holder.imgProduct.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent=new Intent(ShopActivity.this, SeeFullImageActivity.class);
                                            intent.putExtra("uri",model1.getImage());
                                            startActivity(intent);
                                        }
                                    });

                                }
                                @NonNull
                                @Override
                                public ProductImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_layout,parent,false);

                                    return new ProductImageViewHolder(view);
                                }
                            };
                            holder.recyclerView_sub.setAdapter(adapter1);
                            adapter1.startListening();
                            adapter1.notifyDataSetChanged();
                            holder.renameLayout.setVisibility(View.GONE);
                            holder.addPhotoLayout.setVisibility(View.GONE);
                        }
                        @NonNull
                        @Override
                        public ProductCollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_image_layout,parent,false);
                            return new ProductCollectionViewHolder(view, ShopActivity.this);
                        }
                    };
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }
    private void setShows() {
        Query query=db.collection("shows").whereEqualTo("uid",sUid);
            FirestoreRecyclerOptions<MoveShow>options=new FirestoreRecyclerOptions.Builder<MoveShow>()
                    .setQuery(query,MoveShow.class)
                    .build();
            FirestoreRecyclerAdapter<MoveShow, ShowViewHolder>adapter=new FirestoreRecyclerAdapter<MoveShow, ShowViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull ShowViewHolder holder, int position, @NonNull final MoveShow model) {

                    holder.tvPrice.setText(getString(R.string.rs)+model.getPrice());
                    holder.tvMovieName.setText(model.getName());
                    if(ApplicationClass.LANGUAGE_MODE.equals("hi"))
                        holder.tvMovieName.setText(model.getHname());
                    List<String> timeListFrom=model.getTimeFrom();
                    List<String> timeListTo=model.getTimeTo();

                    final String timeFrom="From: "+timeListFrom.get(0)+":"+timeListFrom.get(1)+timeListFrom.get(2);
                    final String timeTo="To: "+timeListTo.get(0)+":"+timeListTo.get(1)+timeListTo.get(2);
                    holder.tvTimeTo.setText(timeTo);
                    holder.tvTimeFrom.setText(timeFrom);
                    holder.imgShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(Intent.ACTION_SEND);
                            String text="Movie:"+model.getName()+"\n"+
                                    "Price:"+model.getPrice()+"\n"+
                                    timeFrom+"\n"+timeTo;
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT,text);
                            startActivity(intent);

                        }
                    });

                    holder.btnDelete.setVisibility(GONE);
                }
                @NonNull
                @Override
                public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.show_layout,parent,false);
                    return new ShowViewHolder(view);
                }
            };
            recyclerViewShow.setAdapter(adapter);
            adapter.startListening();
            adapter.notifyDataSetChanged();
        }


    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
        if(adapterItem!=null)
            adapterItem.stopListening();
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
            case "Gym Sports":
                homeDeliveryLinearLayout.setVisibility(View.GONE);
                break;
            case "Shows and Cinema":
                homeDeliveryLinearLayout.setVisibility(View.GONE);
                setShowLayoutVisisblity();
                break;
            case "Food":
                setItemLayout();
                break;
            case "Cyber":
                homeDeliveryLinearLayout.setVisibility(View.GONE);
                break;
            case "Vehicles and Workshop":
                homeDeliveryLinearLayout.setVisibility(View.GONE);
                break;
            case "Buliding Material":
                homeDeliveryLinearLayout.setVisibility(GONE);
                break;
            case "Rentals":
                homeDeliveryLinearLayout.setVisibility(GONE);
                break;
            case "Factories":
                homeDeliveryLinearLayout.setVisibility(GONE);
                timeLinearLayout.setVisibility(GONE);
                dayLinearLayout.setVisibility(GONE);
                break;
            case "Bank and Atm":
                homeDeliveryLinearLayout.setVisibility(GONE);
                break;
            case "Pentrol Pump":
                homeDeliveryLinearLayout.setVisibility(GONE);
                break;
            case "NGO and Club":
                homeDeliveryLinearLayout.setVisibility(GONE);
                timeLinearLayout.setVisibility(GONE);
                dayLinearLayout.setVisibility(GONE);
                break;
            case "Parlourl Saloon":
                homeDeliveryLinearLayout.setVisibility(GONE);
                break;
            case "Electronics":
                homeDeliveryLinearLayout.setVisibility(GONE);
                break;
            case "Health":
                homeDeliveryLinearLayout.setVisibility(GONE);
                break;
            case "Education":
                homeDeliveryLinearLayout.setVisibility(GONE);
                transportLinearLayout.setVisibility(View.VISIBLE);
                hostelLinearLayout.setVisibility(View.VISIBLE);

                principleRelativeLayout.setVisibility(View.VISIBLE);

                if(store.equals("School"))
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
                break;
            case "Agriculture":
                homeDeliveryLinearLayout.setVisibility(GONE);
                break;
            case "Stay":
                rateLinearLayout.setVisibility(View.VISIBLE);
                homeDeliveryLinearLayout.setVisibility(GONE);
                break;
            case "Travel":
                homeDeliveryLinearLayout.setVisibility(GONE);
                if(store.getStoreSubCategory().equals("Taxi Car"))
                {
                    rateLinearLayout.setVisibility(View.VISIBLE);
                }
                workersRequiredLinearLayout.setVisibility(GONE);
        }
    }

    private void setItemLayout() {
        db.collection("items").whereEqualTo("uid",sUid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty())
                {
                    itemsRelativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setShowLayoutVisisblity() {
        db.collection("shows").whereEqualTo("uid",sUid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty())
                {
                    showRelativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public void seeAllPhotos(View view)
    {
        Intent intent=new Intent(ShopActivity.this,StorePhotosActivity.class);
        intent.putExtra("sUid",sUid);
        startActivity(intent);
    }
    private void textFunction() {


    }
    public  void goToMap(View view)
    {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("ADD","gps");
        }
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {

            startMapService();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }

    private void startMapService() {
        final String uri;
        if (ApplicationClass.USER_LATITUDE ==0.0 || ApplicationClass.USER_LOGITUDE == 0.0) {
            uri = String.format(Locale.ENGLISH, "geo:%f,%f", STORE_LATITUDE, STORE_LONGITUDE);

        } else {

            Log.v("TAG", "bbbb");
            uri = "http://maps.google.com/maps?saddr=" + ApplicationClass.USER_LATITUDE + "," + ApplicationClass.USER_LOGITUDE + "&daddr=" + STORE_LATITUDE + "," + STORE_LONGITUDE;

        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startMapService();
        } else {
            onGps();
            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }

}
