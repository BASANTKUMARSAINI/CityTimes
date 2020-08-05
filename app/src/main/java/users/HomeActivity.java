package users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import adapter.SliderAdapterExample;
import de.hdodenhof.circleimageview.CircleImageView;
import dialog.AboutUsDialog;
import dialog.CategorySearchDefine;
import dialog.VersionDialog;
import model.ApplicationClass;
import model.LocationService;
import model.Seller;
import model.SliderItem;
import model.User;
import seller.RegisterStoreActivity;
import seller.SetupProfileActivity;
import seller.StoreActivity;
import sell_and_buy.SellAndBuyActivity;
import update_dialog.UpdateShopStatus;
import users.fragments.CategoryFragment;
import users.fragments.GovernmentFragment;

import static com.facebook.FacebookSdk.getApplicationContext;


public class HomeActivity extends AppCompatActivity {

    TextView tvNavStoreName,tvCityName;
    CircleImageView imgNavUserImage;

    ImageView imgDrawerOpen;
    String userCity="",userCountry="",userState="";

    LocationManager locationManager;
    private static final int REQUEST_LOCATION=1;


    Fragment fragment;
    FragmentTransaction transaction;
    TextView tvAllServices,tvGovernment;
    View allServicesView,governmentServices;
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
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            onGps();
        }


        Intent intent=new Intent(this, LocationService.class);
        startService(intent);

        if(ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);

        }

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

        setUserDataInNavHeader();
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


        navigationView.getHeaderView(0).findViewById(R.id.header_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TAG=true;
                mDataRef.child("sellers").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {if(TAG) {
                        TAG = false;

                        if (snapshot.exists()) {
                            String category=snapshot.child("storeCategory").getValue(String.class);
                            Intent intent = new Intent(HomeActivity.this, SetupProfileActivity.class);
                            intent.putExtra("category",category);
                            startActivity(intent);
                        } else {
                            db.collection(ApplicationClass.LANGUAGE_MODE+"sellers").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        Intent intent = new Intent(HomeActivity.this, StoreActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(HomeActivity.this, RegisterStoreActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });

                        }
                    }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        tvAllServices=findViewById(R.id.tv_all_services);
        tvGovernment=findViewById(R.id.government);
        allServicesView=findViewById(R.id.all_services_view);
        sliderView= findViewById(R.id.imageSlider);
        sliderViewBottom=findViewById(R.id.image_slider_bottom);

        setSliderImage();
        setSliderImageBottom();
        governmentServices=findViewById(R.id.government_view);

        tvGovernment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allServicesView.setVisibility(View.GONE);
                governmentServices.setVisibility(View.VISIBLE);
                tvAllServices.setTextColor(getResources().getColor(R.color.text_light));
                tvGovernment.setTextColor(getResources().getColor(R.color.text_dark));

                setGovernmentServices();


            }
        });
        tvAllServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allServicesView.setVisibility(View.VISIBLE);
                governmentServices.setVisibility(View.GONE);
                tvGovernment.setTextColor(getResources().getColor(R.color.text_light));
                tvAllServices.setTextColor(getResources().getColor(R.color.text_dark));
                setAllServics();


            }
        });

        //layout refresh





        transaction=getSupportFragmentManager().beginTransaction();
        fragment=new CategoryFragment();
        transaction.replace(R.id.fragment_category,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    private void changeShopStatus() {
        UpdateShopStatus dialog=new UpdateShopStatus(HomeActivity.this,SHOP_STATUS);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void setSliderImageBottom() {
        ApplicationClass.setSliderImage(HomeActivity.this,sliderViewBottom,"general");
//        db.collection("images").whereEqualTo("imageType","general").limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                List<SliderItem> list=new ArrayList<>();
//                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
//                {
//                    list.add(documentSnapshot.toObject(SliderItem.class));
//                }
//
//                SliderAdapterExample adapter = new SliderAdapterExample(HomeActivity.this,list);
//
//                sliderViewBottom.setSliderAdapter(adapter);
//
//                //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//                sliderViewBottom.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//                sliderViewBottom.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//                sliderViewBottom.setIndicatorSelectedColor(Color.WHITE);
//                sliderViewBottom.setIndicatorUnselectedColor(Color.GRAY);
//                sliderViewBottom.setScrollTimeInSec(4); //set scroll delay in seconds :
//                sliderViewBottom.startAutoCycle();
//
//            }
//        });
    }
    private void setSliderImage() {
        ApplicationClass.setSliderImage(HomeActivity.this,sliderView,"general");
//        db.collection("images").whereEqualTo("imageType","general").limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                List<SliderItem> list=new ArrayList<>();
//                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
//                {
//                    list.add(documentSnapshot.toObject(SliderItem.class));
//                }
//
//                SliderAdapterExample adapter = new SliderAdapterExample(HomeActivity.this,list);
//
//                sliderView.setSliderAdapter(adapter);
//
//                //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//                sliderView.setIndicatorSelectedColor(Color.WHITE);
//                sliderView.setIndicatorUnselectedColor(Color.GRAY);
//                sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
//                sliderView.startAutoCycle();
//
//            }
//        });
    }
    private void setGovernmentServices() {
        transaction=getSupportFragmentManager().beginTransaction();
        fragment=new GovernmentFragment();
        transaction.replace(R.id.fragment_category,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
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
//                    userCountry=user.getUcountry();
//                    userState=user.getUstate();
                  userCity=user.getUcity();
                    Log.v("VIT",userCity);
                    //ApplicationClass.setTranslatedText(tvCityName,userCity);
                    tvCityName.setText(userCity);
                }
                Log.v("TTT","user city"+userCity);
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

    private void setUserDataInNavHeader() {

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
                    shopStatusLayout.setVisibility(View.VISIBLE);
                    if(seller.isShopStatus())
                    {
                        tvShopStatus.setText(R.string.open);

                        tvShopStatus.setTextColor(getResources().getColor(R.color.green));
                        shopStatusImage.setImageResource(R.drawable.ic_right_green);
                    }
                    else
                    {
                        tvShopStatus.setText(R.string.close);
                        tvShopStatus.setTextColor(getResources().getColor(R.color.red));
                        shopStatusImage.setImageResource(R.drawable.ic_cross_red);
                    }
                }
            }
        });

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


    public void openDrawer(View view) {
    }
}
