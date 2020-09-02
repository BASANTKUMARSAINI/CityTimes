package users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycity.NotificationActivity;
import com.example.mycity.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import dialog.AboutUsDialog;
import dialog.CategorySearchDefine;
import dialog.VersionDialog;
import model.ApplicationClass;
import services.LocationService;
import model.Seller;
import model.User;
import seller.RegisterStoreActivity;
import seller.SetupProfileActivity;
import seller.StoreActivity;
import sell_and_buy.SellAndBuyActivity;
import services.TrackingService;
import update_dialog.UpdateShopStatus;
import users.fragments.CategoryFragment;
import users.fragments.GovernmentFragment;


public class HomeActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST =1;
    TextView tvNavStoreName,tvCityName;
    CircleImageView imgNavUserImage;

    ImageView imgDrawerOpen;

    Fragment fragment;
    FragmentTransaction transaction;
    TextView tvAllServices;//,tvGovernment;
    View allServicesView;//,governmentServices;
    SliderView sliderView,sliderViewBottom;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DatabaseReference mDataRef;
    boolean TAG=true;
    SearchView searchView;

    //store status
    LinearLayout shopStatusLayout;
    ImageView shopStatusImage;
    TextView tvShopStatus;

    public static boolean SHOP_STATUS=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(HomeActivity.this);
        setContentView(R.layout.activity_home);
        textFunction();
        startService(new Intent(HomeActivity.this,TrackingService.class));


        tvCityName=findViewById(R.id.tv_city_name);
        imgDrawerOpen=findViewById(R.id.btn_three_line);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);

        View headerView=navigationView.getHeaderView(0);
        tvNavStoreName=headerView.findViewById(R.id.nav_store_name);
        imgNavUserImage=headerView.findViewById(R.id.nav_img_user);
        shopStatusImage=headerView.findViewById(R.id.img_shop_status);
        shopStatusLayout=headerView.findViewById(R.id.change_shop_status);
        tvShopStatus=headerView.findViewById(R.id.tv_shop_status);

        imgDrawerOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mDataRef= FirebaseDatabase.getInstance().getReference();

        //setUserDataInNavHeader();
        Log.d("TAG","uid:"+mAuth.getUid());
        getUserDatas();

        shopStatusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeShopStatus();
            }
        });

        searchView=findViewById(R.id.search_view);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableSearchView(searchView,true);
                Intent intent=new Intent(HomeActivity.this,SearchCategoriesActivity.class);
                EditText editText=(EditText)searchView.findViewById(R.id.search_src_text);
                editText.setEnabled(true);
                startActivityForResult(intent,1);

            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableSearchView(searchView,true);
                Intent intent=new Intent(HomeActivity.this,SearchCategoriesActivity.class);
                EditText editText=(EditText)searchView.findViewById(R.id.search_src_text);
                editText.setEnabled(true);
                startActivityForResult(intent,1);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.nav_home:
                        recreate();
                        break;
                    case R.id.nav_store:
                        Intent intent=new Intent(HomeActivity.this, StoreActivity.class);
                        startActivity(intent);
                        break;
                    case  R.id.nav_about:
                        Intent intent3=new Intent(HomeActivity.this, AboutActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_favorites:
                        Intent intent4=new Intent(HomeActivity.this,FavouriteStoreActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_settings:
                        Intent intent2=new Intent(HomeActivity.this,SettingsActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_logout:
                        ApplicationClass.logout(HomeActivity.this);
                        break;

                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        LinearLayout navHeaderLayout=navigationView.getHeaderView(0).findViewById(R.id.nav_header_layout);
        navHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog=new ProgressDialog(HomeActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Loading...");
                dialog.show();
                mDataRef.child("sellers").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (snapshot.exists()) {
                            String category=snapshot.child("storeCategory").getValue(String.class);
                            String subCategory=snapshot.child("storeSubCategory").getValue(String.class);
                            Intent intent = new Intent(HomeActivity.this, SetupProfileActivity.class);
                            intent.putExtra("category",category);
                            intent.putExtra("subCategory",subCategory);
                            dialog.dismiss();
                            startActivity(intent);
                        } else {
                            db.collection(ApplicationClass.LANGUAGE_MODE+"sellers").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        Intent intent = new Intent(HomeActivity.this, StoreActivity.class);
                                        dialog.dismiss();
                                        startActivity(intent);
                                    } else {
                                        dialog.dismiss();
                                        Intent intent = new Intent(HomeActivity.this, RegisterStoreActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dialog.dismiss();

                    }
                });

            }
        });

        tvAllServices=findViewById(R.id.tv_all_services);
        //tvGovernment=findViewById(R.id.government);
        allServicesView=findViewById(R.id.all_services_view);
        sliderView= findViewById(R.id.imageSlider);
        sliderViewBottom=findViewById(R.id.image_slider_bottom);

        setSliderImage();
        setSliderImageBottom();
//        governmentServices=findViewById(R.id.government_view);
//
//        tvGovernment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                allServicesView.setVisibility(View.GONE);
//                governmentServices.setVisibility(View.VISIBLE);
//                tvAllServices.setTextColor(getResources().getColor(R.color.text_light));
//                tvGovernment.setTextColor(getResources().getColor(R.color.text_dark));
//
//                setGovernmentServices();
//
//
//            }
//        });
        tvAllServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allServicesView.setVisibility(View.VISIBLE);
                //governmentServices.setVisibility(View.GONE);
                //tvGovernment.setTextColor(getResources().getColor(R.color.text_light));
                tvAllServices.setTextColor(getResources().getColor(R.color.text_dark));
                setAllServics();


            }
        });

        //layout refresh

        transaction=getSupportFragmentManager().beginTransaction();
        fragment=new CategoryFragment();
        transaction.replace(R.id.fragment_category,fragment);
        if(getSupportFragmentManager().getFragments().size()>1)
        {
            transaction.addToBackStack(null);
        }
        transaction.commit();

    }


    private void changeShopStatus() {

        UpdateShopStatus dialog=new UpdateShopStatus(HomeActivity.this,SHOP_STATUS);
        dialog.setCancelable(false);
        setShopStatus();
        dialog.show();
        Log.d("TAG","change");

    }

    private void setSliderImageBottom() {
        ApplicationClass.setSliderImage(HomeActivity.this,sliderViewBottom,"general");

    }
    private void setSliderImage() {
        ApplicationClass.setSliderImage(HomeActivity.this,sliderView,"general");

    }
//    //private void setGovernmentServices() {
//        transaction=getSupportFragmentManager().beginTransaction();
//        fragment=new GovernmentFragment();
//        transaction.replace(R.id.fragment_category,fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//
//    }
    public void setAllServics()
    {
        transaction= getSupportFragmentManager().beginTransaction();
        fragment=new CategoryFragment();
        transaction.replace(R.id.fragment_category,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void getUserDatas()
    {
        String  path=ApplicationClass.LANGUAGE_MODE+"users";
        db.collection(path).document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    User user=documentSnapshot.toObject(User.class);
                    tvCityName.setText(user.getUcity());
                }

            }
        });
    }
    private void enableSearchView(View view,boolean enabled)
    {
        view.setEnabled(enabled);
        if(view instanceof ViewGroup)
        {
            ViewGroup viewGroup=(ViewGroup)view;
            for(int i=0;i<viewGroup.getChildCount();i++)
            {
                View child=viewGroup.getChildAt(i);
                enableSearchView(child,enabled);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    public  void openDrawer()
    {
        toggle=new ActionBarDrawerToggle(HomeActivity.this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.openDrawer(GravityCompat.START);
        toggle.onDrawerOpened(drawerLayout);
        toggle.syncState();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("TAG","llk");
        EditText editText=(EditText)searchView.findViewById(R.id.search_src_text);
        ImageView closeButton=(ImageView)searchView.findViewById(R.id.search_close_btn) ;
        closeButton.performClick();
        if(resultCode==RESULT_OK&&requestCode==1)
        {
            Log.v("TAG","ok");
            enableSearchView(searchView,false);
        }
    }
    private void onGps() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(R.string.opne_gps).setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
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
    public void logout(View view)
    {
        ApplicationClass.logout(HomeActivity.this);
    }
    public void seeAboutUs(View view)
    {
        AboutUsDialog dialog=new AboutUsDialog(this);
        dialog.setCancelable(false);
        dialog.show();
    }
    public void contactUs(View view)
    {
        Toast.makeText(HomeActivity.this,"kya krega contact ker ke",Toast.LENGTH_LONG).show();
    }
    public void seeVersion(View view)
    {
        VersionDialog dialog=new VersionDialog(this);
        dialog.setCancelable(false);
        dialog.show();
    }
    public void setSearch(View view)
    {
        CategorySearchDefine dialog=new CategorySearchDefine(this);
        dialog.setCancelable(false);
        dialog.show();
    }
    public void sellBuyProduct(View view)
    {
        Intent intent=new Intent(HomeActivity.this, SellAndBuyActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAG","onStart");
        setShopStatus();
    }

    public void setShopStatus() {

        db.collection(ApplicationClass.LANGUAGE_MODE+"sellers").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    Seller seller=documentSnapshot.toObject(Seller.class);
                    if(seller.getOwnerImage()!=null)
                        Picasso.get().load(seller.getOwnerImage()).into(imgNavUserImage);
                    //ApplicationClass.setTranslatedText(tvNavStoreName,seller.getStoreName());
                    tvNavStoreName.setText(seller.getStoreName());
                    navigationView.getMenu().findItem(R.id.nav_store).setVisible(true);

                    //shop status
                    SHOP_STATUS=seller.isShopStatus();

                    if(!seller.getStoreSubCategory().equals("NGO")) {
                        shopStatusLayout.setVisibility(View.VISIBLE);
                        boolean freeBookedCondition=seller.getStoreCategory().equals("Travel")||seller.getStoreSubCategory().equals("Sound box(DJ)")
                                ||seller.getStoreSubCategory().equals("JCB and Crane");
                        boolean availableNotAvailableCondition=seller.getStoreCategory().equals("Rentals")||seller.getStoreSubCategory().equals("Thekedar");

                        if(seller.isShopStatus())
                        {
                            shopStatusImage.setImageResource(R.drawable.ic_right_green);
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
                            shopStatusImage.setImageResource(R.drawable.ic_cross_red);
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

                }
            }
        });

    }
    private void textFunction() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("ADD","gps");

        }
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            Log.d("ADD","start service");
            startTrackerService();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("ADD","permission");
            startTrackerService();
        } else {
            onGps();
            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }
    private void startTrackerService() {
        startService(new Intent(this, LocationService.class));

        //Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();
    }
    public void openNotification(View view)
    {
        startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
    }

}
