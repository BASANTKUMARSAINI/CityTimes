package authantication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.util.LocaleData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import adapter.AddressAdapter;
import adapter.SubCategoryAdapter;
import dialog.CustumProgressDialog;
import dialog.EmailVerificationDialog;
import dialog.PrivacyPolicyDialog;
import dialog.TermsDialog;
import interfaces.RecyclerViewInterface;
import model.ApplicationClass;
import seller.RegisterStoreActivity;
import users.HomeActivity;

public class UserRegisterActivity extends AppCompatActivity implements RecyclerViewInterface {
    RecyclerView recyclerView;
    TextView tvUserCountry,tvUserState,tvUserCity;
    EditText etUserName,etUserEmail,etUserPassword;
    BottomSheetDialog bottomSheetDialog;

    CustumProgressDialog custumProgressDialog;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String userName,userEmail;
    public static String userCountry="",userState="",userCity="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(UserRegisterActivity.this);
        setContentView(R.layout.activity_user_register);

        tvUserCountry=findViewById(R.id.tv_user_country);
        tvUserState=findViewById(R.id.tv_user_state);
        tvUserCity=findViewById(R.id.tv_user_city);
        etUserName=findViewById(R.id.et_user_name);
        etUserEmail=findViewById(R.id.et_user_email);
        etUserPassword=findViewById(R.id.et_user_password);

        custumProgressDialog=new CustumProgressDialog(UserRegisterActivity.this);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();


    }

    public void userRegistration(View view)
    {
          userName=etUserName.getText().toString();
         userEmail=etUserEmail.getText().toString();
        String userPassword=etUserPassword.getText().toString();

        if(TextUtils.isEmpty(userName))
        {
            Toast.makeText(UserRegisterActivity.this,"Name can't be empty",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(userEmail))
        {
            Toast.makeText(UserRegisterActivity.this,"Email can't be empty",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(userPassword))
        {
            Toast.makeText(UserRegisterActivity.this,"Password can't be empty",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(userCountry)||userCountry.equals("Select Country"))
        {
            Toast.makeText(UserRegisterActivity.this,"Please select your country",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(userState)||userState.equals("Select State"))
        {
            Toast.makeText(UserRegisterActivity.this,"Please select your state",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(userCity)||userCity.equals("Select City"))
        {
            Toast.makeText(UserRegisterActivity.this,"Please select your city",Toast.LENGTH_LONG).show();
        }
        else
        {

            custumProgressDialog.startProgressBar(getString(R.string.creating_account_please_wait));
            mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    subscribe();
                                    HashMap<String,Object>hashMap=new HashMap<>();
                                    hashMap.put("uname",userName);
                                    hashMap.put("uemail",userEmail);
                                    hashMap.put("ucity",userCity);
                                    hashMap.put("ucountry",userCountry);
                                    hashMap.put("ustate",userState);

                                    db.collection("enusers").document(mAuth.getUid()).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                ApplicationClass.translatedData=new HashMap<>();
                                                ApplicationClass.translatedData.put("uemail",userEmail);
                                                ApplicationClass.setTranslatedDataToMap("ucountry",userCountry);
                                                ApplicationClass.setTranslatedDataToMap("ustate",userState);
                                                ApplicationClass.setTranslatedDataToMap("ucity",userCity);
                                                ApplicationClass.setTranslatedDataToMap("uname",userName);

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Log.v("RUN","size"+ApplicationClass.translatedData.size());
                                                        db.collection("hiusers").document(mAuth.getUid()).set(ApplicationClass.translatedData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    Log.v("SIZ",ApplicationClass.translatedData.size()+"size");
                                                                    custumProgressDialog.stopProgressBar();
                                                                    EmailVerificationDialog dialog=new EmailVerificationDialog(UserRegisterActivity.this);
                                                                    dialog.startProgressBar(getString(R.string.verification_email)+userEmail);
                                                                }
                                                                else
                                                                {
                                                                    mAuth.getCurrentUser().delete();
                                                                    custumProgressDialog.stopProgressBar();
                                                                    Log.v("Error",task.getException().getMessage());
                                                                    Toast.makeText(UserRegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                },2000);


                                            }
                                            else
                                            {
                                                mAuth.getCurrentUser().delete();
                                                custumProgressDialog.stopProgressBar();
                                                Log.v("Error",task.getException().getMessage());
                                                Toast.makeText(UserRegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });


                                }
                                else
                                {
                                    mAuth.getCurrentUser().delete();
                                    custumProgressDialog.stopProgressBar();
                                    Toast.makeText(UserRegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        custumProgressDialog.stopProgressBar();
                        Toast.makeText(UserRegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void subscribe() {
        FirebaseMessaging.getInstance().subscribeToTopic("users").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG","subcribed");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","sub"+e.getMessage());
            }
        });
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
    public void showBottomSheet(final String type)
    {
         bottomSheetDialog=new BottomSheetDialog(
                UserRegisterActivity.this,R.style.AppBottomSheetDialogTheme
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
         else
             tvTypeName.setText(R.string.city);
        recyclerView=bottomSheetView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setData(type);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
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
        }
        AddressAdapter adapter=new AddressAdapter( UserRegisterActivity.this,this,list,type);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(String place,int index,String where) {
        switch (where)
        {
            case "Country":
                tvUserCountry.setText(place);
                tvUserCountry.setTextColor(getResources().getColor(R.color.selver));
                userCountry= ApplicationClass.getEnglishSubCategory("country_"+index, UserRegisterActivity.this);


                break;
            case "State":
                tvUserState.setText(place);
                tvUserState.setTextColor(getResources().getColor(R.color.selver));
                userState= ApplicationClass.getEnglishSubCategory("state_"+index, UserRegisterActivity.this);;


                break;
            case "City":
                tvUserCity.setText(place);
                tvUserCity.setTextColor(getResources().getColor(R.color.selver));
                userCity= ApplicationClass.getEnglishSubCategory("city_"+index, UserRegisterActivity.this);;

                break;
        }
        bottomSheetDialog.dismiss();

    }
    public void goToBack(View view)
    {
        finish();
    }
    public void seeTerms(View view)
    {
        TermsDialog dialog=new TermsDialog(this);
        dialog.setCancelable(false);
        dialog.show();
    }
    public void seePrivacyPolicy(View view)
    {
        PrivacyPolicyDialog dialog=new PrivacyPolicyDialog(this);
        dialog.setCancelable(false);
        dialog.show();
    }
}
