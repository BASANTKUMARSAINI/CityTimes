package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import adapter.AddressAdapter;
import dialog.CustumProgressDialog;
import interfaces.RecyclerViewInterface;
import model.ApplicationClass;
import seller.RegisterStoreActivity;

public class EnterDetailsActivity extends AppCompatActivity implements RecyclerViewInterface {
    RecyclerView recyclerView;
    Button btnProcced;
    ImageView imgCross;
    TextView tvUserCountry,tvUserState,tvUserCity;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    BottomSheetDialog bottomSheetDialog;
    CustumProgressDialog custumProgressDialog;
    public static String userCity,userState,userCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ApplicationClass.setLocale(this,"en");
        ApplicationClass.loadLocale(EnterDetailsActivity.this);
        setContentView(R.layout.activity_enter_details);
        btnProcced=findViewById(R.id.btn_procced);
        imgCross=findViewById(R.id.img_cross);
        tvUserCountry=findViewById(R.id.tv_user_country);
        tvUserState=findViewById(R.id.tv_user_state);
        tvUserCity=findViewById(R.id.tv_user_city);

        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        custumProgressDialog=new CustumProgressDialog(EnterDetailsActivity.this);

        tvUserCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCity();
            }
        });

        tvUserState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getState();
            }
        });
        tvUserCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCountry();
            }
        });
        btnProcced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procced();
            }


        });
    }
    public void procced() {
//        final String userCountry = tvUserCountry.getText().toString();
//        final String userState = tvUserState.getText().toString();
//        final String userCity = tvUserCity.getText().toString();
        if (TextUtils.isEmpty(userCountry) || userCountry.equals("Select Country")) {
            Toast.makeText(getApplicationContext(), "Please select your country", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(userState) || userState.equals("Select State")) {
            Toast.makeText(getApplicationContext(), "Please select your state", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(userCity) || userCity.equals("Select City")) {
            Toast.makeText(getApplicationContext(), "Please select your city", Toast.LENGTH_LONG).show();
        } else {

            custumProgressDialog.startProgressBar(getString(R.string.wait));
            final HashMap<String,Object> hashMap=new HashMap<>();
            if(mAuth.getCurrentUser().getDisplayName()!=null)
                hashMap.put("uname",mAuth.getCurrentUser().getDisplayName());
            hashMap.put("ucity",userCity);
            hashMap.put("ucountry",userCountry);
            hashMap.put("ustate",userState);

            ApplicationClass.translatedData=new HashMap<>();
            ApplicationClass.setTranslatedDataToMap("ucity",userCity);
            ApplicationClass.setTranslatedDataToMap("ucountry",userCountry);
            ApplicationClass.setTranslatedDataToMap("ustate",userState);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    db.collection("enusers").document(mAuth.getUid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            db.collection("hiusers").document(mAuth.getUid()).set(ApplicationClass.translatedData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent=new Intent(getApplicationContext(), StoresActivity.class);
                                    intent.putExtra("subCategory",getIntent().getStringExtra("subCategory"));
                                    intent.putExtra("category",getIntent().getStringExtra("category"));
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    custumProgressDialog.stopProgressBar();
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            custumProgressDialog.stopProgressBar();
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

                }
            },1000);



        }
    }
    public  void getCountry()
    {
        showBottomSheet("Country");
    }
    public  void getState()
    {
        showBottomSheet("State");
    }
    public  void getCity()
    {
        showBottomSheet("City");
    }
    public void showBottomSheet(final String type)
    {
        bottomSheetDialog=new BottomSheetDialog(
                EnterDetailsActivity.this,R.style.AppBottomSheetDialogTheme
        );
        View bottomSheetView= LayoutInflater.from(EnterDetailsActivity.this)
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
        //ApplicationClass.setTranslatedText(tvTypeName,type);
        TextView tvBack=bottomSheetView.findViewById(R.id.tv_back);
        tvBack.setText(R.string.back);
        if(type.equals("Country"))
            tvTypeName.setText(R.string.country);
        else if(type.equals("State"))
            tvTypeName.setText(R.string.state);
        else
            tvTypeName.setText(R.string.city);
        recyclerView=bottomSheetView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(EnterDetailsActivity.this));
        setData(type);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void setData(String type) {
        String []list=getResources().getStringArray(R.array.shopping);

        switch (type) {
            case "Country":
                list =getResources().getStringArray(R.array.Country);
                break;
            case "State":
                list =getResources().getStringArray(R.array.State);
                break;
            case "City":
                list = getResources().getStringArray(R.array.City);
                break;
        }
        AddressAdapter adapter=new AddressAdapter(EnterDetailsActivity.this,this,list,type);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(String place,int index,String where) {
        switch (where)
        {
            case "Country":
                tvUserCountry.setText(place);
                tvUserCountry.setTextColor(getResources().getColor(R.color.selver));
                userCountry=place;//ApplicationClass.getEnglishSubCategory("country_"+index,EnterDetailsActivity.this);
                break;
            case "State":
                tvUserState.setText(place);
                tvUserState.setTextColor(getResources().getColor(R.color.selver));
                userState=place;//ApplicationClass.getEnglishSubCategory("state_"+index,EnterDetailsActivity.this);
                break;
            case "City":
                tvUserCity.setText(place);
                tvUserCity.setTextColor(getResources().getColor(R.color.selver));
                userCity=place;//ApplicationClass.getEnglishSubCategory("city_"+index, EnterDetailsActivity.this);
                break;
        }
        bottomSheetDialog.dismiss();

    }
    public void goToBack(View view)
    {
        finish();
    }
}
