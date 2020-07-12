package seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.StrongBoxUnavailableException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dialog.CustumProgressDialog;
import interfaces.UpdateInterface;
import model.Seller;
import update_dialog.PhoneNumberUpdateDialog;
import update_dialog.UpdateDays;
import update_dialog.UpdateHomeDelivery;
import update_dialog.UpdateOwnerName;
import update_dialog.UpdateStoreAddress;
import update_dialog.UpdateStoreName;
import update_dialog.UpdateStoreTime;
import update_dialog.UpdateWorkerRequired;
import users.HomeActivity;

public class StoreActivity extends AppCompatActivity implements UpdateInterface {
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    private  ImageView imgBackground;
   private CircleImageView imgProfile;
   private TextView tvStoreName,tvOwnerName,tvPhone1,tvPhone2,tvStoreAddress,tvStoreTime;
    TextView tvSunday,tvMonday,tvTuseday,tvWednesday,tvThrusday,tvFriday,tvSaturday;
    TextView tvHomeDelivery,tvWorkers,tvSeeAllPhotos;
    ImageView updateAll;
    TextView tvRatings;

    CircleImageView imgUpdateAll;

    ImageView updateStoreName,updateOwnerName,updatePhone,updateStoreAddress,
            updateStoreTime,updateDays,updateHomeDelivery,updateWorkers,updatePhotos;

    Button btnApply,btnCancel;

    LinearLayout ratingLayout,photosLayout;

    Seller store=null;

    List<String>newFromTime,newToTime;
    HashMap<String,Boolean>newDays;
    boolean newHomeDeliveryStatus;
    private boolean newWorkers;
    Double newStoreLatitude,newStoreLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
       final ProgressDialog dialog=new ProgressDialog(StoreActivity.this);





                mAuth=FirebaseAuth.getInstance();
                db=FirebaseFirestore.getInstance();

                imgBackground=findViewById(R.id.img_background);
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

                tvRatings=findViewById(R.id.tv_rating_bar);

                updateAll=findViewById(R.id.img_change);

                updateStoreName=findViewById(R.id.update_store_name);
                updateOwnerName=findViewById(R.id.update_owner_name);
                updateStoreAddress=findViewById(R.id.update_store_address);
                updateStoreTime=findViewById(R.id.update_store_time);
                updatePhone=findViewById(R.id.update_phone);
                updateDays=findViewById(R.id.update_days);
                updateHomeDelivery=findViewById(R.id.update_delivery_status);
                updateWorkers=findViewById(R.id.update_workers);
                updatePhotos=findViewById(R.id.update_all_photos);

                btnApply=findViewById(R.id.btn_apply);
                btnCancel=findViewById(R.id.btn_cancel);

                imgUpdateAll=findViewById(R.id.img_change);
                ratingLayout=findViewById(R.id.rating_layout);
                photosLayout=findViewById(R.id.photos_layout);

                newToTime=new ArrayList<>();
                newFromTime=new ArrayList<>();
                newDays=new HashMap<>();

                setAllData();






    }
    private void setAllData() {
        db.collection("sellers").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    Seller seller=documentSnapshot.toObject(Seller.class);
                    store=seller;
                    if(seller.getBackgroundImage()!=null)
                    {
                        Picasso.get().load(seller.getBackgroundImage()).into(imgBackground);
                        imgBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    if(seller.getOwnerImage()!=null)
                        Picasso.get().load(seller.getOwnerImage()).into(imgProfile);
                    tvStoreName.setText(seller.getStoreName());
                    tvOwnerName.setText(seller.getOwnerName());
                    tvPhone1.setText(seller.getPhone1());
                    tvPhone2.setText(seller.getPhone2());
                    tvStoreAddress.setText(seller.getStoreAddress());

                    List<String> timeListFrom=seller.getTimeFrom();
                    List<String> timeListTo=seller.getTimeTo();

                    newFromTime=timeListFrom;
                    newToTime=timeListTo;

                    String storeTiming=timeListFrom.get(0)+":"+timeListFrom.get(1)+timeListFrom.get(2)+"-"
                            +timeListTo.get(0)+":"+timeListTo.get(1)+timeListTo.get(2);
                    tvStoreTime.setText(storeTiming);

                    HashMap<String,Boolean>days=seller.getDays();
                    setDays(days);
                    newDays=days;

                    setDeleveryStatus(seller.isDeliveryStatus());
                    newHomeDeliveryStatus=seller.isDeliveryStatus();

                    setWorkerRequiredStatus(seller.isWorkersRequred());
                    newWorkers=seller.isWorkersRequred();

                    Integer totalStar=seller.getTotalStar();
                    Integer totalRatings=seller.getNoOfRatings();
                    if(totalRatings==0)
                        tvRatings.setText("0.0");
                    else {
                        float ratings=(float)totalStar/totalRatings;
                        tvRatings.setText(ratings+"");
                    }

                    newStoreLatitude=store.getStoreLatitude();
                    newStoreLongitude=store.getStoreLongitude();

                }

            }


        });
    }
    private void setWorkerRequiredStatus(boolean workersRequred) {

        if(workersRequred)
        {
            tvWorkers.setText("Yes, workers required");
        }
        else
        {
            tvWorkers.setText("No, workers required");
        }
    }
    private void setDeleveryStatus(boolean deliveryStatus) {
        if(deliveryStatus)
        {
            tvHomeDelivery.setText("Home delivery Avaliable");
        }
        else
        {
            tvHomeDelivery.setText("Home delivery not Avaliable");
        }

    }

    private void setDays(HashMap<String, Boolean> days) {
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
        if(!days.get("tuseday"))
            tvTuseday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("wednesday"))
            tvWednesday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("thrusday"))
            tvThrusday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("friday"))
            tvFriday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("saturday"))
            tvSaturday.setTextColor(getResources().getColor(R.color.red));
    }

    public void goToBack(View view)
    {
        Intent intent=new Intent(StoreActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(StoreActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void applyAllChanges(View view)
    {
        final CustumProgressDialog dialog=new CustumProgressDialog(this);
        dialog.startProgressBar("upating...");
        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("storeName",tvStoreName.getText().toString());
        hashMap.put("storeAddress",tvStoreAddress.getText().toString());
        hashMap.put("ownerName",tvOwnerName.getText().toString());
        hashMap.put("phone1",tvPhone1.getText().toString());
        hashMap.put("phone2",tvPhone2.getText().toString());
        hashMap.put("timeFrom",newFromTime);
        hashMap.put("timeTo",newToTime);
        hashMap.put("days",newDays);
        hashMap.put("deliveryStatus",newHomeDeliveryStatus);
        hashMap.put("workersRequred",newWorkers);
        hashMap.put("storeLongitude",newStoreLongitude);
        hashMap.put("storeLatitude",newStoreLatitude);
        db.collection("sellers").document(mAuth.getUid()).update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Log.v("TAG","SUcc");
                    recreate();
                    dialog.stopProgressBar();
                }
                else
                {
                    dialog.stopProgressBar();
                    recreate();
                    Toast.makeText(StoreActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void seeAllPhotos(View view)
    {
        Intent intent=new Intent(StoreActivity.this,HandleProductPhotosActivity.class);
        startActivity(intent);
    }

    public void setVisiblity(View view)
    {

        updateStoreName.setVisibility(View.VISIBLE);
        updateStoreAddress.setVisibility(View.VISIBLE);
        updateStoreTime.setVisibility(View.VISIBLE);
        updateOwnerName.setVisibility(View.VISIBLE);
        updatePhone.setVisibility(View.VISIBLE);
        updateDays.setVisibility(View.VISIBLE);
        updateHomeDelivery.setVisibility(View.VISIBLE);
        updateWorkers.setVisibility(View.VISIBLE);
        btnApply.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);

        ratingLayout.setVisibility(View.INVISIBLE);
        imgUpdateAll.setVisibility(View.INVISIBLE);
        photosLayout.setVisibility(View.INVISIBLE);



    }
    public void updatePhoneNumber(View view)
    {
        if(store==null)
            Log.v("TAG","yes null");
        PhoneNumberUpdateDialog dialog=new PhoneNumberUpdateDialog(StoreActivity.this,store.getPhone1(),store.getPhone2(),this);
        dialog.show();
    }
    public void updateStoreName(View view)
    {
     UpdateStoreName updateStoreName=new UpdateStoreName(StoreActivity.this,store.getStoreName(),this);
     updateStoreName.show();

    }
    public void updateStoreAddress(View view)
    {
        UpdateStoreAddress updateStoreAddress=new UpdateStoreAddress(StoreActivity.this,store.getStoreAddress(),this);
        updateStoreAddress.show();
    }
    public  void updateStoreTime(View view)
    {
        UpdateStoreTime updateStoreTime=new UpdateStoreTime(StoreActivity.this,newFromTime,newToTime,this);
        updateStoreTime.show();
    }
    public void updateOwnerName(View view)
    {
        UpdateOwnerName updateOwnerName=new UpdateOwnerName(StoreActivity.this,store.getOwnerName(),this);
        updateOwnerName.show();
    }
    public void updateDays(View view)
    {

        UpdateDays updateDays=new UpdateDays(StoreActivity.this,store.getDays(),this);
        updateDays.show();
    }
    public void updateHomeDeliveryStatus(View view)
    {

        UpdateHomeDelivery updateHomeDelivery=new UpdateHomeDelivery(StoreActivity.this,store.isDeliveryStatus(),this);
        updateHomeDelivery.show();
    }
public void updateWorkers(View view)
{
    UpdateWorkerRequired updateWorkerRequired=new UpdateWorkerRequired(StoreActivity.this,newWorkers,this);
    updateWorkerRequired.show();
}
public  void setVisibilityGone(View view)
{
    updateStoreName.setVisibility(View.INVISIBLE);
    updateStoreAddress.setVisibility(View.INVISIBLE);
    updateStoreTime.setVisibility(View.INVISIBLE);
    updateOwnerName.setVisibility(View.INVISIBLE);
    updatePhone.setVisibility(View.INVISIBLE);
    updateDays.setVisibility(View.INVISIBLE);
    updateHomeDelivery.setVisibility(View.INVISIBLE);
    updateWorkers.setVisibility(View.INVISIBLE);
    btnApply.setVisibility(View.VISIBLE);
    imgUpdateAll.setVisibility(View.INVISIBLE);
    recreate();

}
    @Override
    public void update(String place, List<String> value) {
        switch (place)
        {
            case "PHONE":
                tvPhone1.setText(value.get(0));
                tvPhone2.setText(value.get(1));
                tvPhone1.setTextColor(getResources().getColor(R.color.red));
                tvPhone2.setTextColor(getResources().getColor(R.color.red));
                break;
            case "STORE_NAME":
                tvStoreName.setText(value.get(0));
                tvStoreName.setTextColor(getResources().getColor(R.color.red));
                break;
            case "OWNER_NAME":
                tvOwnerName.setText(value.get(0));
                tvOwnerName.setTextColor(getResources().getColor(R.color.red));
                break;

        }

    }

    @Override
    public void update(String place, List<String> value1, List<String> value2) {
        switch (place)
        {
            case "STORE_TIMING":
                newFromTime=value1;
                newToTime=value2;
                String time=value1.get(0)+":"+value1.get(1)+value1.get(2)+"-"+value2.get(0)+":"+value2.get(1)+value2.get(2);
                tvStoreTime.setText(time);
                tvStoreTime.setTextColor(getResources().getColor(R.color.red));
        }


    }

    @Override
    public void update(String place, HashMap<String, Boolean> value) {
        switch (place)
        {
            case "DAYS":
                newDays=value;
                setDays(value);
                break;


        }

    }

    @Override
    public void update(String place, boolean value) {
        switch (place)
        {
            case "HOME_DELIVERY":
                newHomeDeliveryStatus=value;
                tvHomeDelivery.setTextColor(getResources().getColor(R.color.red));
                setDeleveryStatus(value);
                break;
            case "WORKERS":
                newWorkers=value;
                setWorkerRequiredStatus(value);
                tvWorkers.setTextColor(getResources().getColor(R.color.red));
                break;
        }
    }

    @Override
    public void update(String place, List<String> value, Double logitude, Double latitude) {
        switch (place)
        {
            case "STORE_ADDRESS":
                tvStoreAddress.setText(value.get(0));
                tvStoreAddress.setTextColor(getResources().getColor(R.color.red));
                newStoreLatitude=latitude;
                newStoreLongitude=logitude;
                break;
        }
    }
}
