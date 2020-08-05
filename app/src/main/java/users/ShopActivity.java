package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mycity.MainActivity;
import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import dialog.RatingDialog;
import model.ApplicationClass;
import model.CollectionType;
import model.MoveShow;
import model.ProductImage;
import model.Seller;
import view_holder.ProductCollectionViewHolder;
import view_holder.ProductImageViewHolder;
import view_holder.ShowViewHolder;

import static android.view.View.GONE;

public class ShopActivity extends AppCompatActivity {
    //view
    LinearLayout homeDeliveryView,hostelLayout,transportLayout,timeLAyout,dayLayout,shopStatusLayout;
    RelativeLayout boardLayout,showLayout,photoLayout;
    TextView tvHostel,tvTransport,tvBoard;

    String sUid;

    String ownerImageUrl=null;

    private static final int REQUEST_LOCATION=1;
    LocationManager locationManager;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    RecyclerView recyclerView;
    //shows
    RecyclerView recyclerViewShow;

    private  ImageView imgBackground;
    private CircleImageView imgProfile;
    private TextView tvStoreName,tvOwnerName,tvPhone1,tvPhone2,tvStoreAddress,tvStoreTime;
    TextView tvSunday,tvMonday,tvTuseday,tvWednesday,tvThrusday,tvFriday,tvSaturday;
    TextView tvHomeDelivery,tvWorkers,tvSeeAllPhotos,tvShopDescription;
    ImageView imgSave,imgShopDescription;

    TextView tvRatings;
    boolean isStoreSaved=false;

    Seller favoriteStore=null;
    Double STORE_LONGITUDE=1.0,STORE_LATITUDE=1.0;
    Double USER_LOGITUDE=-1.0,USER_LATITUDE=-1.0;
    String CATEGORY="";
    FirestoreRecyclerAdapter<CollectionType, ProductCollectionViewHolder>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(this);
        setContentView(R.layout.activity_shop);
        //view
        homeDeliveryView=findViewById(R.id.view_home_delivery);

        sUid=getIntent().getStringExtra("sUid");

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
        timeLAyout=findViewById(R.id.time_layout);
        dayLayout=findViewById(R.id.day_layout);

        imgShopDescription=findViewById(R.id.img_service_description);
        tvShopDescription=findViewById(R.id.tv_shop_description);

        recyclerView=findViewById(R.id.recycler_view_main);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //cinema
        recyclerViewShow=findViewById(R.id.recycler_view_show);
        recyclerViewShow.setLayoutManager(new LinearLayoutManager(this));

        imgSave=findViewById(R.id.img_bookmarks);
        photoLayout=findViewById(R.id.layout_photo);

        //education
        tvHostel=findViewById(R.id.tv_hostel);
        tvTransport=findViewById(R.id.tv_transport);
        hostelLayout=findViewById(R.id.hostel_layout);
        transportLayout=findViewById(R.id.transport_layout);
        tvBoard=findViewById(R.id.tv_board_name);

        //cinema
        showLayout=findViewById(R.id.layout_show);
        shopStatusLayout=findViewById(R.id.change_shop_status);

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
                    favoriteStore=seller;

                    if (seller.getBackgroundImage() != null) {
                        Picasso.get().load(seller.getBackgroundImage()).into(imgBackground);
                        imgBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ownerImageUrl=seller.getBackgroundImage();
                        imgBackground.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(ShopActivity.this,SeeFullImageActivity.class);
                                intent.putExtra("uri",seller.getBackgroundImage());
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
//                    ApplicationClass.setTranslatedText(tvStoreName,seller.getStoreName());
//                    ApplicationClass.setTranslatedText(tvOwnerName,seller.getOwnerName());
//                    ApplicationClass.setTranslatedText( tvStoreAddress,seller.getStoreAddress());

                  tvStoreName.setText(seller.getStoreName());
                    tvOwnerName.setText(seller.getOwnerName());


                    tvPhone1.setText(seller.getPhone1());
                    tvPhone2.setText(seller.getPhone2());
                   tvStoreAddress.setText(seller.getStoreAddress());

                    if(seller.getStoreDescription()!=null)
                    {
                        tvShopDescription.setVisibility(View.VISIBLE);
                        imgShopDescription.setVisibility(View.VISIBLE);
                       // ApplicationClass.setTranslatedText( tvShopDescription,seller.getStoreDescription());
                        tvShopDescription.setText(seller.getStoreDescription());
                    }
                    if(seller.getStoreCategory().equals("Factories")||seller.getStoreCategory().equals("NGO and Club"))
                    {
                        timeLAyout.setVisibility(GONE);

                    }
                    else {
                        List<String> timeListFrom = seller.getTimeFrom();
                        List<String> timeListTo = seller.getTimeTo();

                        String storeTiming = timeListFrom.get(0) + ":" + timeListFrom.get(1) + timeListFrom.get(2) + "-"
                                + timeListTo.get(0) + ":" + timeListTo.get(1) + timeListTo.get(2);
                        tvStoreTime.setText(storeTiming);
                    }

                    HashMap<String, Boolean> days = seller.getDays();
                    setDays(days,seller.getStoreCategory());


                    setDeleveryStatus(seller.isDeliveryStatus(),seller.getStoreCategory());

                    setWorkerRequiredStatus(seller.isWorkersRequred());

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
                    //education
                    tvBoard.setText(seller.getBoardName());
                    if(seller.getPrincipalName()!=null)
                        tvOwnerName.setText(seller.getPrincipalName());
                    sethostelStatus(seller.isHostel());
                    setTransportStatus(seller.isTransport());
                    setDataVisiblity(seller.getStoreCategory());

                    setShows(seller.getStoreCategory());
                    setShopStatus(seller.isShopStatus());

                }

            }


        });
    }
    private void setShopStatus(boolean isOpen) {
        ImageView imageView=findViewById(R.id.img_shop_status);
        TextView textView=findViewById(R.id.tv_shop_status);
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
    private void setDeleveryStatus(boolean deliveryStatus,String CATEGORY) {


        if(deliveryStatus)
        {
            tvHomeDelivery.setText(R.string.home_delivery);
        }
        else
        {
            tvHomeDelivery.setText(R.string.no_home_delivery);
        }

    }
    private void setDays(HashMap<String, Boolean> days,String category) {
        if(category.equals("Factories")||category.equals("NGO and Club"))
        {
            dayLayout.setVisibility(GONE);
            return;
        }
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
                db.collection("en"+"users").document(mAuth.getUid())
                        .collection("favoriteStores").document(sUid).set(favoriteStore).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection("hi"+"users").document(mAuth.getUid())
                                .collection("favoriteStores").document(sUid).set(favoriteStore).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    db.collection("en"+"users").document(mAuth.getUid())
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

        final Query query=db.collection("sellers").document(sUid).collection("productImages");
        FirestoreRecyclerOptions<CollectionType> options=new FirestoreRecyclerOptions.Builder<CollectionType>()
                .setQuery(query,CollectionType.class)
                .build();
        adapter=new FirestoreRecyclerAdapter<CollectionType, ProductCollectionViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductCollectionViewHolder holder, int position, @NonNull final CollectionType model) {
                String collectionName=model.getCollectionName();
                holder.setCollectionType(collectionName);
                holder.mainLayout.setBackgroundColor(getResources().getColor(R.color.other_background));
                photoLayout.setVisibility(View.VISIBLE);

                Query query1=db.collection("sellers").document(sUid).collection("productImages")
                        .document(model.getCollectionName()).collection("products");

                FirestoreRecyclerOptions<ProductImage>options1=new FirestoreRecyclerOptions.Builder<ProductImage>()
                        .setQuery(query1,ProductImage.class)
                        .build();
                FirestoreRecyclerAdapter<ProductImage, ProductImageViewHolder>adapter1=new FirestoreRecyclerAdapter<ProductImage,ProductImageViewHolder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductImageViewHolder holder, int position, @NonNull final ProductImage model1) {
                        holder.setProductImage(model1.getImage());
                        //ApplicationClass.setTranslatedText(holder.tvProductPrice,"Rs:"+model1.getPrice());
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
    }
    private void setShows(String category) {
        if (category.equals("Shows and Cinema")) {

            Query query=db.collection("shows").whereEqualTo("uid",sUid);
            FirestoreRecyclerOptions<MoveShow>options=new FirestoreRecyclerOptions.Builder<MoveShow>()
                    .setQuery(query,MoveShow.class)
                    .build();
            FirestoreRecyclerAdapter<MoveShow,ShowViewHolder>adapter=new FirestoreRecyclerAdapter<MoveShow, ShowViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull ShowViewHolder holder, int position, @NonNull final MoveShow model) {
//                    ApplicationClass.setTranslatedText(holder.tvPrice,"Rs:"+model.getPrice());
//                    ApplicationClass.setTranslatedText(holder.tvMovieName,model.getName());

                    holder.tvPrice.setText(getString(R.string.rs)+model.getPrice());
                    holder.tvMovieName.setText(model.getName());
                    if(ApplicationClass.LANGUAGE_MODE.equals("hi"))
                        holder.tvMovieName.setText(model.getHname());
                    List<String> timeListFrom=model.getTimeFrom();
                    List<String> timeListTo=model.getTimeTo();

                    Log.v("GG","in");
                    showLayout.setVisibility(View.VISIBLE);
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
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
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
                homeDeliveryView.setVisibility(View.GONE);
                break;
            case "Shows and Cinema":
                homeDeliveryView.setVisibility(View.GONE);
                showLayout.setVisibility(View.VISIBLE);
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
}
