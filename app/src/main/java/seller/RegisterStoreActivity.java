package seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import adapter.AddressAdapter;
import authantication.UserRegisterActivity;
import dialog.CustumProgressDialog;
import dialog.EmailVerificationDialog;
import dialog.SetupProfileDialog;
import interfaces.RecyclerViewInterface;
import model.Seller;

public class RegisterStoreActivity extends AppCompatActivity implements RecyclerViewInterface {
    RecyclerView recyclerView;
    TextView tvStoreCountry,tvStoreState,tvStoreCity;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        hashMap=new HashMap<>();
        hashMap.put("Shopping", R.array.shopping);
        hashMap.put("Food",R.array.food);
        hashMap.put("Gym-Sports", R.array.gym_sport);
        hashMap.put("Beauty",R.array.beauty);
        hashMap.put("Electronics", R.array.electronics);
        hashMap.put("Health",R.array.health);
        hashMap.put("Movies", R.array.movies);
        hashMap.put("Education",R.array.education);
        hashMap.put("Catering", R.array.catering);
        hashMap.put("Rentals", R.array.rentals);
        hashMap.put("Agriculture", R.array.agriculture);
        hashMap.put("Machanice", R.array.machanice);
        hashMap.put("Other", R.array.other);

        custumProgressDialog=new CustumProgressDialog( RegisterStoreActivity.this);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        mDataRef= FirebaseDatabase.getInstance().getReference();
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

        if(!temp.equals("Store Category"))
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
        tvTypeName.setText(type);
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
                list = getResources().getStringArray(hashMap.get(tvStoreCategory.getText().toString()));
                break;
        }
        AddressAdapter adapter=new AddressAdapter( RegisterStoreActivity.this,this,list,type);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(String place,String where) {
        switch (where)
        {
            case "Country":
                tvStoreCountry.setText(place);
                tvStoreCountry.setTextColor(getResources().getColor(R.color.selver));

                break;
            case "State":
                tvStoreState.setText(place);
                tvStoreState.setTextColor(getResources().getColor(R.color.selver));
                break;
            case "City":
                tvStoreCity.setText(place);
                tvStoreCity.setTextColor(getResources().getColor(R.color.selver));
                break;
            case "Category":
                tvStoreCategory.setText(place);
                tvStoreCategory.setTextColor(getResources().getColor(R.color.selver));
                break;
            case "SubCategory":
                tvStoreSubCategory.setText(place);
                tvStoreSubCategory.setTextColor(getResources().getColor(R.color.selver));
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

        final String storeCountry=tvStoreCountry.getText().toString();
        final String storeState=tvStoreState.getText().toString();
        final String storeCity=tvStoreCity.getText().toString();
        final String storeCategory=tvStoreCategory.getText().toString();
        final String storeSubCategory=tvStoreSubCategory.getText().toString();

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

            if(!getAddress(storeAddress))
            {
                custumProgressDialog.stopProgressBar();
                Toast.makeText(RegisterStoreActivity.this,"Invalid address...use city name",Toast.LENGTH_LONG).show();
                return;
            }




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
            mDataRef.child("sellers").child(mAuth.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        custumProgressDialog.stopProgressBar();

                        Log.v("TAG","su");
                                                custumProgressDialog.stopProgressBar();
                    SetupProfileDialog dialog=new SetupProfileDialog(RegisterStoreActivity.this);

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
}
