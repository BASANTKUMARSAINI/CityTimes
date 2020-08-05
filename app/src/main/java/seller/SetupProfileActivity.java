package seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.hsr.geohash.GeoHash;
import de.hdodenhof.circleimageview.CircleImageView;
import dialog.CustumProgressDialog;
import dialog.EducationSetupDialog;
import dialog.SetUpHomeDeliveryDialog;
import dialog.TimeSetupDialog;
import interfaces.EducationInterface;
import model.ApplicationClass;
import model.Seller;

import static android.view.View.GONE;

public class SetupProfileActivity extends AppCompatActivity implements EducationInterface {
int BACKGROUND=1,PROFILE=2;
Uri backgroundUri=null,profileUri=null;
ImageView imgBackground;
CircleImageView imgProfile;

public  static  TextView tvStoreTimings,tvDeliveryServices,tvOtherDetails;
public static  int SELVER_COLOR_CODE;

public static boolean deliveryStatus=true;
public  static HashMap<String,Boolean>days;
public  static List<String>timeFrom,timeTo;

DatabaseReference mDataRef;
FirebaseFirestore db;
StorageReference mStoreRef;
FirebaseAuth mAuth;
    boolean tag=true;

    TimeSetupDialog dialog;
    String CATEGORY="";

    //education
    boolean HOSTEL=false,TRANSPORT=false;
    String principalName="";
    String board="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ApplicationClass.setLocale(SetupProfileActivity.this,"en");
        ApplicationClass.loadLocale(SetupProfileActivity.this);
        setContentView(R.layout.setup_profile);

        imgBackground=findViewById(R.id.img_background);

        imgProfile=findViewById(R.id.img_owner);
        days=new HashMap<>();
        timeFrom=new ArrayList<>();
        timeTo=new ArrayList<>();
        SELVER_COLOR_CODE=getResources().getColor(R.color.selver);


        mDataRef=FirebaseDatabase.getInstance().getReference();
        db=FirebaseFirestore.getInstance();
        mStoreRef= FirebaseStorage.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();


        tvStoreTimings=findViewById(R.id.tv_store_timing);
        tvDeliveryServices=findViewById(R.id.tv_delivery);
        tvOtherDetails=findViewById(R.id.tv_important_details);

        CATEGORY=getIntent().getStringExtra("category");
        setUpLayout();


    }

    private void setUpLayout()
    {
        switch (CATEGORY)
        {
            case "Gym Sports":
                tvDeliveryServices.setVisibility(GONE);
                break;
            case "Shows and Cinema":
                tvDeliveryServices.setVisibility(GONE);
                break;
            case "Cyber":
                tvDeliveryServices.setVisibility(GONE);
                break;
            case "Vehicles and Workshop":
                tvDeliveryServices.setVisibility(GONE);
                break;
            case "Buliding Material":
                tvDeliveryServices.setVisibility(GONE);
                break;
            case "Rentals":
                tvDeliveryServices.setVisibility(GONE);
                break;
            case "Factories":
                tvDeliveryServices.setVisibility(GONE);
                tvStoreTimings.setVisibility(GONE);
                break;
            case "Bank and Atm":
                tvDeliveryServices.setVisibility(GONE);
                break;
            case "Pentrol Pump":
                tvDeliveryServices.setVisibility(GONE);
                break;
            case "NGO and Club":
                tvDeliveryServices.setVisibility(GONE);
                break;
            case "Parlourl Saloon":
                tvDeliveryServices.setVisibility(GONE);
                break;
            case "Electronics":
                tvDeliveryServices.setVisibility(GONE);
                break;
            case "Health":
                tvDeliveryServices.setVisibility(GONE);
                break;
            case "Education":
                tvDeliveryServices.setVisibility(GONE);
                tvOtherDetails.setVisibility(View.VISIBLE);
                break;
            case "Booking":
                tvDeliveryServices.setVisibility(GONE);
                break;
            case "Agriculture":
                tvDeliveryServices.setVisibility(GONE);
                break;
            case "Stay":
                tvDeliveryServices.setVisibility(GONE);
                break;


        }
    }

    public void goToBack(View view)
    {
        finish();
    }

    public void openGelleryBackground(View view)
    {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,BACKGROUND);
    }

    public void openGelleryProfile(View view)
    {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,PROFILE);
    }

    public void setDeliveryService(View view)
    {
        final SetUpHomeDeliveryDialog deliveryDialog=new SetUpHomeDeliveryDialog(SetupProfileActivity.this);
        deliveryDialog.startProgressBar();

    }

    public  void setDelivery(boolean status)
    {
        deliveryStatus=status;
       tvDeliveryServices.setTextColor(SELVER_COLOR_CODE);
        if(status)
            tvDeliveryServices.setText(R.string.avaliable);
        else
            tvDeliveryServices.setText(R.string.not_available);
    }

    public void setTiming(View view)
    {
         dialog=new TimeSetupDialog(SetupProfileActivity.this);
         dialog.startProgressBar();

    }

    public void setAllData(HashMap<String,Boolean>hashMap,List<String>from,List<String>to)
    {
        days=hashMap;
        timeTo=to;
        timeFrom=from;
        String time=timeFrom.get(0)+":"+timeFrom.get(1)+timeFrom.get(2)+"-"+timeTo.get(0)+":"+timeTo.get(1)+timeTo.get(2);
        Log.v("TAG",timeFrom.size()+"kk");
        Log.v("TAG",timeTo.size()+"kk");

        tvStoreTimings.setText(time);
       tvStoreTimings.setTextColor(SELVER_COLOR_CODE);


    }

    public void procced(View view)
    {
        Log.v("TAG",timeFrom.size()+"pp");
        Log.v("TAG",timeTo.size()+"pp");
        if(backgroundUri==null)
        {
            Toast.makeText(SetupProfileActivity.this,"select background photo",Toast.LENGTH_LONG).show();
        }
        else if(profileUri==null)
        {
            Toast.makeText(SetupProfileActivity.this,"select profile photo",Toast.LENGTH_LONG).show();
        }
        else  if(((!CATEGORY.equals("NGO and Club"))||(!CATEGORY.equals("Factories")))&&timeFrom.size()==0||timeTo.size()==0)
        {
            Toast.makeText(SetupProfileActivity.this,"select time ",Toast.LENGTH_LONG).show();
        }
        else if(CATEGORY.equals("Education")&&board.equals(""))
        {
            Toast.makeText(SetupProfileActivity.this,"set some other details also",Toast.LENGTH_LONG).show();
        }
        else
        {
            tag=true;
            final CustumProgressDialog dialog=new CustumProgressDialog(SetupProfileActivity.this);
            dialog.startProgressBar(getString(R.string.set_up_store));

            final StorageReference childRef=mStoreRef.child("images").child("sellers").child(mAuth.getUid()).child("background");
            childRef.putFile(backgroundUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String backgroundURL=uri.toString();//backuri
                            mStoreRef.child("images").child("sellers").child(mAuth.getUid()).child("profile");

                            childRef.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                   childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                           final String profileURL=uri.toString();//mmmm

                                           mDataRef.child("sellers").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                   if(tag) {
                                                       tag=false;

                                                       if (snapshot.exists()) {


                                                           //
                                                           final Seller seller = snapshot.getValue(Seller.class);
                                                           final HashMap<String,Object>hashMap=new HashMap<>();
                                                           hashMap.put("phone1",seller.getPhone1());
                                                           hashMap.put("phone2",seller.getPhone2());
                                                           hashMap.put("storeCategory",seller.getStoreCategory());
                                                           hashMap.put("storeSubCategory",seller.getStoreSubCategory());
                                                           hashMap.put("storeEmail",mAuth.getCurrentUser().getEmail());
                                                           hashMap.put("storeLongitude",seller.getStoreLongitude());
                                                           hashMap.put("storeLatitude",seller.getStoreLongitude());
                                                           hashMap.put("sortStoreName",seller.getSortStoreName());
                                                           hashMap.put("sortOwnerName",seller.getSortOwnerName());
                                                           hashMap.put("sortStoreAddress",seller.getSortStoreAddress());
                                                           hashMap.put("sortRating",seller.getSortRating());
                                                           hashMap.put("sortStoreSubCategory",seller.getSortStoreSubCategory());
                                                           hashMap.put("rating",seller.getRating());
                                                           hashMap.put("geohash",seller.getGeohash());
                                                           GeoPoint geoPoint=new GeoPoint(seller.getStoreLatitude(),seller.getStoreLongitude());
                                                           hashMap.put("geoPoint",geoPoint);
                                                           hashMap.put("backgroundImage",backgroundURL);
                                                           hashMap.put("ownerImage",profileURL);
                                                           hashMap.put("noOfRatings",0);
                                                           hashMap.put("totalStar",0.0);
                                                           hashMap.put("sUid",mAuth.getUid());
                                                           hashMap.put("workersRequred",false);
                                                           hashMap.put("isShopStatus",true);
                                                           if((!CATEGORY.equals("Factories"))||(!CATEGORY.equals("NGO and Club"))) {
                                                               hashMap.put("timeFrom", timeFrom);
                                                               hashMap.put("timeTo", timeTo);
                                                               hashMap.put("days", days);
                                                           }
                                                           String CATEGORY=seller.getStoreCategory();
                                                           if(CATEGORY.equals("Shopping")||CATEGORY.equals("Food"))
                                                               hashMap.put("deliveryStatus",deliveryStatus);
                                                           ApplicationClass.translatedData=new HashMap<>();
                                                           ApplicationClass.translatedData.putAll(hashMap);
                                                           if(CATEGORY.equals("Education"))
                                                           {
                                                               hashMap.put("principalName",principalName);
                                                               hashMap.put("boardName",board);
                                                               ApplicationClass.setTranslatedDataToMap("hiBoardName",board);
                                                               ApplicationClass.setTranslatedDataToMap("hiPrincipalName",principalName);
                                                               hashMap.put("hostel",HOSTEL);
                                                               hashMap.put("transport",TRANSPORT);
                                                               ApplicationClass.translatedData.put("hostel",HOSTEL);
                                                               ApplicationClass.translatedData.put("transport",TRANSPORT);
                                                           }

                                                           hashMap.put("storeName",seller.getStoreName());
                                                           hashMap.put("ownerName",seller.getOwnerName());
                                                           hashMap.put("storeAddress",seller.getStoreAddress());
                                                           hashMap.put("storeCity",seller.getStoreCity());
                                                           hashMap.put("storeCountry",seller.getStoreCountry());
                                                           hashMap.put("storeState",seller.getStoreState());

                                                           //

                                                           ApplicationClass.setTranslatedDataToMap("storeName",seller.getStoreName());
                                                           ApplicationClass.setTranslatedDataToMap("ownerName",seller.getOwnerName());
                                                           ApplicationClass.setTranslatedDataToMap("storeAddress",seller.getStoreAddress());
                                                           ApplicationClass.setTranslatedDataToMap("storeCity",seller.getStoreCity());
                                                           ApplicationClass.setTranslatedDataToMap("storeCountry",seller.getStoreCountry());
                                                           ApplicationClass.setTranslatedDataToMap("storeState",seller.getStoreState());
                                                           hashMap.putAll(ApplicationClass.translatedData);
                                                           new Handler().postDelayed(new Runnable() {
                                                               @Override
                                                               public void run() {
                                                                   final DocumentReference documentReference= db.collection("ensellers").document(mAuth.getUid());

                                                                  documentReference.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                       @Override
                                                                       public void onComplete(@NonNull Task<Void> task) {
                                                                           if (task.isSuccessful()) {
                                                                                db.collection("hisellers").document(mAuth.getUid()).set(ApplicationClass.translatedData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                   @Override
                                                                                   public void onComplete(@NonNull Task<Void> task) {
                                                                                       if(task.isSuccessful())
                                                                                       {
                                                                                           mDataRef.child("sellers").child(mAuth.getUid()).removeValue();
                                                                                           Intent intent = new Intent(SetupProfileActivity.this, StoreActivity.class);
                                                                                           startActivity(intent);
                                                                                           finish();
                                                                                       }
                                                                                       else
                                                                                       {
                                                                                           dialog.stopProgressBar();
                                                                                           Toast.makeText(SetupProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                                       }
                                                                                   }
                                                                               });

                                                                           } else {
                                                                               dialog.stopProgressBar();
                                                                               Toast.makeText(SetupProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                           }
                                                                       }
                                                                   });
                                                               }
                                                           }, 3000);


                                                       } else {
                                                           dialog.stopProgressBar();
                                                           Toast.makeText(SetupProfileActivity.this, "first complete registration", Toast.LENGTH_LONG).show();

                                                       }
                                                   }
                                                   else {
                                                       finish();
                                                       Log.v("TAG","else");
                                                   }
                                               }
                                                   @Override
                                                   public void onCancelled(@NonNull DatabaseError error) {
                                                       dialog.stopProgressBar();
                                                       Toast.makeText(SetupProfileActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

                                                   }
                                               });

                                           }
                                       }).addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               dialog.stopProgressBar();
                                               Toast.makeText(SetupProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                           }
                                       });
                                   }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.stopProgressBar();
                                        Toast.makeText(SetupProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SetupProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });

                }


                                   })

                    .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SetupProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });

        }
    }



    public  void setUpOthersDetails(View view)
    {
        EducationSetupDialog dialog=new EducationSetupDialog(SetupProfileActivity.this,this);
        dialog.setCancelable(false);
        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED)
        {
            Toast.makeText(SetupProfileActivity.this,"try again",Toast.LENGTH_LONG).show();
            return;
        }
        else if(resultCode==RESULT_OK)
        {
            if(requestCode==BACKGROUND)
            {
               backgroundUri=data.getData();
               if(backgroundUri!=null)
               {
                   imgBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
                   imgBackground.setImageURI(backgroundUri);
               }

            }
            if(requestCode==PROFILE)
            {
                profileUri=data.getData();
                if(profileUri!=null)
                {
                    //imgProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imgProfile.setImageURI(profileUri);
                }

            }
        }

    }

    @Override
    public void educationData(String principalName, String board, boolean hostel, boolean transport) {
        tvOtherDetails.setTextColor(getResources().getColor(R.color.selver));
        tvOtherDetails.setText(R.string.set_up);
        this.principalName=principalName;
        this.board=board;
        HOSTEL=hostel;
        TRANSPORT=transport;
    }
}
