package users.government;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import model.ApplicationClass;
import model.Office;
import model.Product;
import sell_and_buy.MyProductActivity;
import sell_and_buy.ProductMainActivity;
import users.ShopActivity;
import view_holder.OfficeViewHolder;
import view_holder.ProductViewHolder;

import static android.view.View.GONE;

public class OfficeActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    SliderView sliderView;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter<Office, OfficeViewHolder> adapter;
    String SORT_BY=null;
    LinearLayout officeMenu;
    TextView tvSort;
    int REQUEST_LOCATION=1;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(this);
        setContentView(R.layout.activity_office);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        SORT_BY=getIntent().getStringExtra("SORT_BY");
        sliderView= findViewById(R.id.imageSlider);
        officeMenu=findViewById(R.id.sort_by);
        tvSort=findViewById(R.id.tv_sort);
        officeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOfficeType();
            }
        });

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(OfficeActivity.this));
        setSliderImage();


    }

    @Override
    protected void onStart() {
        super.onStart();
        String path=ApplicationClass.LANGUAGE_MODE+"government";
        Query query=db.collection(path);
        if(SORT_BY!=null&&!SORT_BY.equals("All"))
        {
            query=db.collection( path).whereEqualTo("sortOfficeType",SORT_BY.toLowerCase());

        }
        FirestoreRecyclerOptions<Office>options=new FirestoreRecyclerOptions.Builder<Office>()
                                                .setQuery(query,Office.class)
                                                .build();
        adapter=new FirestoreRecyclerAdapter<Office, OfficeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OfficeViewHolder holder, int position, @NonNull final Office model) {
                //timing
                List<String> timeListFrom=model.getTimeFrom();
                List<String> timeListTo=model.getTimeTo();
                String storeTiming = timeListFrom.get(0) + ":" + timeListFrom.get(1) + timeListFrom.get(2) + "-"
                        + timeListTo.get(0) + ":" + timeListTo.get(1) + timeListTo.get(2);
                holder.tvTime.setText(storeTiming);

                //days
               // HashMap<String, Boolean> days = model.getDays();
                //holder.setDays(days);

                //type icon
                holder.setIcon(OfficeActivity.this,model.getOfficeType());

                //office type
              //  holder.tvOfficeType.setText(model.getOfficeType());
//ApplicationClass.setTranslatedText(holder.tvOfficeType,model.getOfficeType());
holder.tvOfficeType.setText(model.getOfficeType());
                //call
//                holder.tvPhone.setText(model.getPhone());
//                holder.call.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        makeCall(model.getPhone());
//                    }
//                });


                //address
                holder.tvAddress.setText(model.getAddress());
                //ApplicationClass.setTranslatedText(holder.tvAddress,model.getAddress());
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(OfficeActivity.this, ProductMainActivity.class);
                        intent.putExtra("category","government");
                        intent.putExtra("pid",model.getId());
                        startActivity(intent);

                    }
                });

                //head name
                //holder.tvHeadName.setText(model.getHeadName());

                //description
                //holder.tvDescription.setText(model.getDescription());

//                holder.imgeNavigation.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        navigation(model.getLocation());
//                    }
//                });

                //share
//                holder.officeShare.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String text="     "+model.getOfficeType()+"\n"
//                                   + "Officer name:"+model.getHeadName()+"\n"
//                                     +"Phone:"+model.getPhone()+"\n"
//                                +model.getDescription();
//                        Intent i = new Intent(Intent.ACTION_SEND);
//                        //text
//                        i.setType("text/plain");
//                        i.putExtra(Intent.EXTRA_TEXT,text);
//
//                        startActivity(i);
//                    }
//                });

            }

            @NonNull
            @Override
            public OfficeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.office_layout,parent,false);
                return new OfficeViewHolder(OfficeActivity.this,view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    private void navigation(GeoPoint location) {
        if(ActivityCompat.checkSelfPermission(OfficeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(OfficeActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            onGps();
        }
        String uri;
        if(ApplicationClass.USER_LATITUDE==0.0||ApplicationClass.USER_LOGITUDE==0.0)
        {
            uri=String.format(Locale.ENGLISH,"geo:%f,%f",location.getLatitude(),location.getLongitude());
            Log.v("TAG","b");
        }
        else
        {
            Log.v("TAG","bbbb");
            uri="http://maps.google.com/maps?saddr="+ApplicationClass.USER_LATITUDE+","+ApplicationClass.USER_LOGITUDE+
                    "&daddr="+location.getLatitude()+","+location.getLongitude();
        }
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
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
    public  void showOfficeType()
{
    PopupMenu popupMenu=new PopupMenu(OfficeActivity.this,officeMenu);
    popupMenu.getMenuInflater().inflate(R.menu.sort_office_menu,popupMenu.getMenu());
    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            tvSort.setText(item.getTitle());
            switch (item.getItemId())
            {
                case R.id.all:
                    SORT_BY="All";
                    break;
                case R.id.police:
                    SORT_BY="Police Station";
                    break;
                case R.id.fire:
                    SORT_BY="Fire Brigade";
                    break;
                   //Police Station  Fire Brigade  Hospital  College Court  BDO office  BEO office  DEO office  Panchayat Bhawan
                case R.id.hospital:
                    SORT_BY="Hospital";
                    break;
                case R.id.college:
                    SORT_BY="College";
                    break;
                case R.id.court:
                    SORT_BY="Court";
                    break;
                case R.id.bdo_office:
                    SORT_BY="BDO office";
                    break;
                case R.id.beo_office:
                    SORT_BY="BEO office";
                    break;
                case R.id.deo_office:
                    SORT_BY="DEO office";
                    break;
                case R.id.panchayat:
                    SORT_BY="Panchayat Bhawan";
                    break;

            }

           // SORT_BY=ApplicationClass.getEnglishStringDirect(item.)
            onStart();
            return true;
        }
    });
    popupMenu.show();
}
    private void makeCall(String sphone) {
        ApplicationClass.makeCall(OfficeActivity.this,sphone);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter!=null)
            adapter.stopListening();
    }

    public void goToBack(View view)
    {
        finish();
    }
    private void setSliderImage() {

        ApplicationClass.setSliderImage(OfficeActivity.this,sliderView,"general");
    }
}
