package seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dialog.CustumProgressDialog;
import dialog.SetUpHomeDeliveryDialog;
import dialog.TimeSetupDialog;
import model.Seller;

public class SetupProfileActivity extends AppCompatActivity {
int BACKGROUND=1,PROFILE=2;
Uri backgroundUri=null,profileUri=null;
ImageView imgBackground;
CircleImageView imgProfile;

public  static  TextView tvStoreTimings,tvDeliveryServices;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            tvDeliveryServices.setText("Available");
        else
            tvDeliveryServices.setText("Not Available");
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
        else  if(timeFrom.size()==0||timeTo.size()==0)
        {
            Toast.makeText(SetupProfileActivity.this,"select time ",Toast.LENGTH_LONG).show();
        }
        else
        {
            tag=true;
            final CustumProgressDialog dialog=new CustumProgressDialog(SetupProfileActivity.this);
            dialog.startProgressBar("setup store...");


//            mStoreRef.child("imges").putFile(backgroundUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                }
//            });



            final StorageReference childRef=mStoreRef.child("images").child("sellers").child(mAuth.getUid()).child("background");
            childRef.putFile(backgroundUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.v("TAG","onSuccess");

                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String backgroundURL=uri.toString();//backuri
                            mStoreRef.child("images").child("sellers").child(mAuth.getUid()).child("profile");

                            childRef.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.v("TAG","onSuccess2");

                                   childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                           final String profileURL=uri.toString();//mmmm

                                           mDataRef.child("sellers").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                   if(tag) {
                                                       tag=false;
                                                       Log.v("TAG", "listion");
                                                       if (snapshot.exists()) {
                                                           Log.v("TAG", "listen");
                                                           Seller seller = snapshot.getValue(Seller.class);
                                                           seller.setBackgroundImage(backgroundURL);
                                                           seller.setOwnerImage(profileURL);
                                                           seller.setTimeFrom(timeFrom);
                                                           seller.setTimeTo(timeTo);
                                                           seller.setDays(days);
                                                           seller.setDeliveryStatus(deliveryStatus);
                                                           seller.setTotalStar(0);
                                                           seller.setNoOfRatings(0);
                                                           seller.setsUid(mAuth.getUid());
                                                           seller.setWorkersRequred(false);
                                                           db.collection("sellers").document(mAuth.getUid()).set(seller).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                               @Override
                                                               public void onComplete(@NonNull Task<Void> task) {
                                                                   if (task.isSuccessful()) {
                                                                       Log.v("TAG", "successful");
                                                                       mDataRef.child("sellers").child(mAuth.getUid()).removeValue();
                                                                       Log.v("TAG", "pp");
                                                                       Intent intent = new Intent(SetupProfileActivity.this, StoreActivity.class);
                                                                       startActivity(intent);
                                                                       finish();

                                                                   } else {
                                                                       dialog.stopProgressBar();
                                                                       Toast.makeText(SetupProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                   }
                                                               }
                                                           });
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
                                    Toast.makeText(SetupProfileActivity.this,"try again",Toast.LENGTH_LONG).show();
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
}
