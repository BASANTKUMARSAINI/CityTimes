package seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycity.MainActivity;
import com.example.mycity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import adapter.AddressAdapter;
import authantication.UserRegisterActivity;
import ch.hsr.geohash.GeoHash;
import dialog.CustumProgressDialog;
import dialog.EmailVerificationDialog;
import dialog.SetupProfileDialog;
import interfaces.RecyclerViewInterface;
import model.ApplicationClass;
import model.LocationService;
import model.Seller;
import users.HomeActivity;

public class RegisterStoreActivity extends AppCompatActivity implements RecyclerViewInterface {
    RecyclerView recyclerView;
    TextView tvStoreCountry,tvStoreState,tvStoreCity,tvCurrentAddress;
    TextView tvStoreCategory,tvStoreSubCategory;
    EditText etStoreName,etOwnerName,etPhone1,etPhone2,etStoreAddress;
    BottomSheetDialog bottomSheetDialog;
    boolean category=false;

    HashMap<String, Integer> hashMap;

    CustumProgressDialog custumProgressDialog;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DatabaseReference mDataRef;

    Double STORE_LONGITUDE=1.0,STORE_LATITUDE=1.0;
    ImageView imgDefineSearch;
    int SELECTED_CATEGORY=-1;
    public  static  String storeCountry="Select Country",storeCity="Select City",storeState="Select State",storeCategory="Store Category",storeSubCategory="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(RegisterStoreActivity.this);
        setContentView(R.layout.activity_register_store);

        tvStoreCountry=findViewById(R.id.tv_store_country);
        tvStoreState=findViewById(R.id.tv_store_state);
        tvStoreCity=findViewById(R.id.tv_store_city);
        tvStoreCategory=findViewById(R.id.tv_store_category);
        tvStoreSubCategory=findViewById(R.id.tv_store_sub_category);
        etStoreName=findViewById(R.id.et_store_name);
        etStoreAddress=findViewById(R.id.et_store_address);
        etOwnerName=findViewById(R.id.et_owner_name);
        etPhone1=findViewById(R.id.et_seller_phone_1);
        etPhone2=findViewById(R.id.et_seller_phone_2);

        tvCurrentAddress=findViewById(R.id.tv_current_location);


        hashMap=new HashMap<>();
        hashMap.put("Shopping", R.array.shopping);
        hashMap.put("Food",R.array.food);
        hashMap.put("Gym Sports", R.array.gym_sport);
        hashMap.put("Parlourl Saloon",R.array.parlour_saloon);
        hashMap.put("Electronics", R.array.electronics);
        hashMap.put("Health",R.array.health);
        hashMap.put("Shows and Cinema", R.array.shows_cinema);
        hashMap.put("Education",R.array.education);
        hashMap.put("Booking", R.array.booking);
        hashMap.put("Rentals", R.array.rentals);
        hashMap.put("Agriculture", R.array.agriculture);
        hashMap.put("Buliding Material", R.array.bulding);
        hashMap.put("Cyber", R.array.cyber);
        hashMap.put("Stay", R.array.stay);
        hashMap.put("Factories", R.array.factories);
        hashMap.put("NGO and Club", R.array.ngo_club);
        hashMap.put("Bank and Atm", R.array.bank_atm);
        hashMap.put("Pentrol Pump", R.array.petrol_pump);
        hashMap.put("Vehicles and Workshop", R.array.vehicle_workshop);
        hashMap.put("Travel",R.array.travel_store);

        custumProgressDialog=new CustumProgressDialog( RegisterStoreActivity.this);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        mDataRef= FirebaseDatabase.getInstance().getReference();

        tvCurrentAddress.setText(R.string.get_current_location);
        if(ActivityCompat.checkSelfPermission(RegisterStoreActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(RegisterStoreActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(RegisterStoreActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            tvCurrentAddress.setText(R.string.opne_gps);
            ApplicationClass.onGps(RegisterStoreActivity.this);
        }
    }

    public  void getCountry(View view)
    {
        showBottomSheet("Country");
    }

    public  void getState(View view)
    {
        showBottomSheet("State");
    }

    public  void getCity(View view)
    {
        showBottomSheet("City");
    }

    public void getCategory(View view)
    {
        showBottomSheet("Category");
        category=true;
    }

    public void getSubCategory(View view)
    {
        String temp=tvStoreCategory.getText().toString();
        Log.v("VVV","cat"+storeCategory);
        if(!storeCategory.equals("Store Category"))
        showBottomSheet("SubCategory");
        else
            Toast.makeText(RegisterStoreActivity.this,"first select a category",Toast.LENGTH_LONG).show();
    }

    public void showBottomSheet(final String type)
    {
        bottomSheetDialog=new BottomSheetDialog(
                RegisterStoreActivity.this,R.style.AppBottomSheetDialogTheme
        );
        View bottomSheetView= LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet,
                        (LinearLayout)findViewById(R.id.bottom_sheet_contanier)
                );
        TextView tvTypeName=bottomSheetView.findViewById(R.id.tv_category_name);
        ImageView btnBack=bottomSheetView.findViewById(R.id.img_downword);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        TextView tvBack=bottomSheetView.findViewById(R.id.tv_back);
        tvBack.setText(R.string.back);
        if(type.equals("Country"))
            tvTypeName.setText(R.string.country);
        else if(type.equals("State"))
            tvTypeName.setText(R.string.state);
        else if(type.equals("City"))
            tvTypeName.setText(R.string.city);
        else if(type.equals("Category"))
            tvTypeName.setText(R.string.categories);
        else
            tvTypeName.setText(R.string.sub_category);
        //ApplicationClass.setTranslatedText(tvTypeName,type);
        recyclerView=bottomSheetView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setData(type);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public void goToBack(View view)
{
    finish();
}

    private void setData(String type) {
        String []list=getResources().getStringArray(R.array.shopping);

        switch (type) {
            case "Country":
                list = getResources().getStringArray(R.array.Country);
                break;
            case "State":
                list = getResources().getStringArray(R.array.State);
                break;
            case "City":
                list = getResources().getStringArray(R.array.City);
                break;
            case "Category":
                list = getResources().getStringArray(R.array.Category);
                break;
            case "SubCategory":
                list = getResources().getStringArray(hashMap.get(storeCategory));
                break;
        }
        AddressAdapter adapter=new AddressAdapter( RegisterStoreActivity.this,this,list,type);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(String value,int index,String  where) {
        switch (where)
        {
            case "Country":
                tvStoreCountry.setText(value);
                tvStoreCountry.setTextColor(getResources().getColor(R.color.selver));
                storeCountry=ApplicationClass.getEnglishSubCategory("country_"+index,RegisterStoreActivity.this);

                Log.v("VVV",storeCountry);
                break;
            case  "State":
                tvStoreState.setText(value);
                tvStoreState.setTextColor(getResources().getColor(R.color.selver));
                storeState=ApplicationClass.getEnglishSubCategory("state_"+index,RegisterStoreActivity.this);
                Log.v("VVV",storeState);
                break;
            case "City":
                tvStoreCity.setText(value);
                tvStoreCity.setTextColor(getResources().getColor(R.color.selver));
                storeCity=ApplicationClass.getEnglishSubCategory("city_"+index,RegisterStoreActivity.this);
                break;
            case  "Category":
                tvStoreCategory.setText(value);
                tvStoreCategory.setTextColor(getResources().getColor(R.color.selver));

                storeCategory=ApplicationClass.getEnglishSubCategory("category_"+index,RegisterStoreActivity.this);
                Log.v("VVV","VV"+storeCategory);
                break;
            case  "SubCategory":
                tvStoreSubCategory.setText(value);
                tvStoreSubCategory.setTextColor(getResources().getColor(R.color.selver));
                Log.v("VVV","xx"+storeCategory);
                        int i=storeCategory.indexOf(' ');
                        String firstWord=storeCategory;
                        if(i!=-1)
                            firstWord=storeCategory.substring(0,i);
                storeSubCategory=ApplicationClass.getEnglishSubCategory(firstWord.toLowerCase()+"_"+index,RegisterStoreActivity.this);
                break;
        }
        bottomSheetDialog.dismiss();

    }

    public  void storeRegistration(View view)
    {
        final String storeName=etStoreName.getText().toString();
        final String ownerName=etOwnerName.getText().toString();
        final String storeAddress=etStoreAddress.getText().toString();
        final String phone1=etPhone1.getText().toString();
        final String phone2=etPhone2.getText().toString();

//        final String storeCountry=tvStoreCountry.getText().toString();
//        final String storeState=tvStoreState.getText().toString();
//        final String storeCity=tvStoreCity.getText().toString();
//        final String storeCategory=tvStoreCategory.getText().toString();
//        final String storeSubCategory=tvStoreSubCategory.getText().toString();
        STORE_LONGITUDE=ApplicationClass.USER_LOGITUDE;
        STORE_LATITUDE=ApplicationClass.USER_LATITUDE;

        if(TextUtils.isEmpty(storeName))
        {
            Toast.makeText(RegisterStoreActivity.this,"Store name can't be empty",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(ownerName))
        {
            Toast.makeText(RegisterStoreActivity.this,"Owner's name can't be empty",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(phone1))
        {
            Toast.makeText(RegisterStoreActivity.this,"Phone number can't be empty",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(storeAddress))
        {
            Toast.makeText(RegisterStoreActivity.this,"Store address can't be empty",Toast.LENGTH_LONG).show();
        }
        else if(STORE_LATITUDE==0.0||STORE_LONGITUDE==0.0)
        {
            Toast.makeText(RegisterStoreActivity.this,"get Current location",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(storeCountry)||storeCountry.equals("Select Country"))
        {
            Toast.makeText(RegisterStoreActivity.this,"Please select store country",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(storeState)||storeState.equals("Select State"))
        {
            Toast.makeText(RegisterStoreActivity.this,"Please select store state",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(storeCity)||storeCity.equals("Select City"))
        {
            Toast.makeText(RegisterStoreActivity.this,"Please select store city",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(storeCategory)||storeState.equals("Store Category"))
        {
            Toast.makeText(RegisterStoreActivity.this,"Please select store category",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(storeSubCategory)||storeCity.equals("Store Sub-Category"))
        {
            Toast.makeText(RegisterStoreActivity.this,"Please select store sub-category",Toast.LENGTH_LONG).show();
        }
        else
        {
            custumProgressDialog.startProgressBar("store setup...please wait...");

//            if(!getAddress(storeAddress))
//            {
//                custumProgressDialog.stopProgressBar();
//                Toast.makeText(RegisterStoreActivity.this,"Invalid address...use city name",Toast.LENGTH_LONG).show();
//                return;
//            }



            Log.v("VVV","kk"+storeCategory);
            HashMap<String,Object>hashMap=new HashMap<>();
            hashMap.put("storeName",storeName);
            hashMap.put("ownerName",ownerName);
            hashMap.put("phone1",phone1);
            hashMap.put("phone2",phone2);
            hashMap.put("storeAddress",storeAddress);
            hashMap.put("storeCategory",storeCategory);
            hashMap.put("storeSubCategory",storeSubCategory);
            hashMap.put("storeCity",storeCity);
            hashMap.put("storeCountry",storeCountry);
            hashMap.put("storeState",storeState);
            hashMap.put("storeEmail",mAuth.getCurrentUser().getEmail());
            hashMap.put("storeLongitude",STORE_LONGITUDE);
            hashMap.put("storeLatitude",STORE_LATITUDE);
            hashMap.put("sortStoreName",storeName.toLowerCase());
            hashMap.put("sortOwnerName",ownerName.toLowerCase());
            hashMap.put("sortStoreAddress",storeAddress.toLowerCase());
            hashMap.put("sortRating",0);
            hashMap.put("sortStoreSubCategory",storeSubCategory.toLowerCase());
            double rating=0;
            hashMap.put("rating",0);
            GeoHash geoHash=GeoHash.withCharacterPrecision(STORE_LATITUDE,STORE_LONGITUDE,12);
            String geoHashString=geoHash.toBase32().substring(0,12);
            hashMap.put("geohash",geoHashString);

            mDataRef.child("sellers").child(mAuth.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        custumProgressDialog.stopProgressBar();
                        SetupProfileDialog dialog=new SetupProfileDialog(RegisterStoreActivity.this,storeCategory,storeSubCategory);
                        dialog.show();
                    }
                    else
                    {
                        custumProgressDialog.stopProgressBar();
                        Toast.makeText(RegisterStoreActivity.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });



        }
    }

    public boolean getAddress(String address)
    {

        Geocoder geocoder=new Geocoder(RegisterStoreActivity.this, Locale.getDefault());
        try {

            List list=geocoder.getFromLocationName(address,1);
            if(list!=null&&list.size()>0)
            {
                Address ADDRESS=(Address) list.get(0);
               STORE_LATITUDE=ADDRESS.getLatitude();
               STORE_LONGITUDE=ADDRESS.getLongitude();
               return  true;

            }
            else
            {
                 return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public void getCurrentLocation(View view)
    {
        if(ActivityCompat.checkSelfPermission(RegisterStoreActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(RegisterStoreActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(RegisterStoreActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            ApplicationClass.onGps(RegisterStoreActivity.this);
            return;
        }
//        Intent intent=new Intent(this, LocationService.class);
//        startService(intent);
        Log.v("TTT","get"+ApplicationClass.USER_LOGITUDE+" "+ApplicationClass.USER_LATITUDE);
        String currentAddress=ApplicationClass.getAddress(RegisterStoreActivity.this);
        if( currentAddress!=null)
        {
            //ApplicationClass.setTranslatedText(tvCurrentAddress,currentAddress);
            tvCurrentAddress.setText(currentAddress);
            tvCurrentAddress.setTextColor(getResources().getColor(R.color.selver));
        }
        else
        {
            tvCurrentAddress.setText(R.string.try_again);
            tvCurrentAddress.setTextColor(getResources().getColor(R.color.red));
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("TTT","onstart");
        Log.v("TTT","onstr"+ApplicationClass.USER_LOGITUDE+" "+ApplicationClass.USER_LATITUDE);


        String currentAddress=ApplicationClass.getAddress(RegisterStoreActivity.this);
        if( currentAddress!=null)
        {
            tvCurrentAddress.setText(currentAddress);
            tvCurrentAddress.setTextColor(getResources().getColor(R.color.selver));
        }
        else
        {
            LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                tvCurrentAddress.setText(R.string.opne_gps);
                return;
            }
            tvCurrentAddress.setText(R.string.get_current_location);
            //tvCurrentAddress.setTextColor(getResources().getColor(R.color.red));
        }
    }
}
