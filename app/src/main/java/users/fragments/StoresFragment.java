package users.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.protobuf.ApiProto;

import ch.hsr.geohash.GeoHash;
import model.ApplicationClass;
import model.Seller;
import model.User;
import users.HomeActivity;
import users.StoresActivity;
import view_holder.SellerViewHolder;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.view.View.GONE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoresFragment extends Fragment {
    public static  RecyclerView recyclerView;
    public static FirebaseFirestore db;
    public static FirebaseAuth mAuth;
    public static String userCity="";
    public static String subCategory="";
    public static String userState="";
    public static  String userCountry="";
    public  static String category="";
    //FusedLocationProviderClient fusedLocationProviderClient;
    public static  FirestoreRecyclerAdapter<Seller, SellerViewHolder> adapter;
    public  static TextView tvNoExist;

    public static Double USER_LOGITUDE=1.0,USER_LATITUDE=1.0;
    public static Context context;
    public  static SharedPreferences sharedPreferences;
    SwipeRefreshLayout refreshLayout;


    public StoresFragment() {
        // Required empty public constructor
    }

    public StoresFragment(String subCategory1,String category1) {

        subCategory=subCategory1;
       category=category1;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ApplicationClass.loadLocale(getContext());
        View view=inflater.inflate(R.layout.fragment_stores, container, false);
        recyclerView= view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager( getContext()));
        context=getContext();
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        sharedPreferences=context.getSharedPreferences("MyData",Context.MODE_PRIVATE);
        tvNoExist=view.findViewById(R.id.tv_no_exist);
        tvNoExist.setVisibility(View.VISIBLE);

        setData("");

        return view;
    }
    public static void setData(final String newText) {
      //  Log.v("TAG","onStart")
        String  path=ApplicationClass.LANGUAGE_MODE+"users";
        db.collection(path).document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user=documentSnapshot.toObject(User.class);
                userCity=user.getUcity();
                userState=user.getUstate();
                userCountry=user.getUcountry();
                final String  path=ApplicationClass.LANGUAGE_MODE+"sellers";
                Query query=db.collection(path)
                        .whereEqualTo("storeCity",userCity)
                        .whereEqualTo("storeState",userState).whereEqualTo("storeCountry",userCountry)
                        .whereEqualTo("sortStoreSubCategory",subCategory.toLowerCase());
                if(newText.equals(""))
                {
                    if(sharedPreferences.getBoolean("checkbox",true))
                    {
                        query=db.collection(path).whereEqualTo("storeCity",userCity)
                                .whereEqualTo("storeState",userState).whereEqualTo("storeCountry",userCountry)
                                .whereEqualTo("sortStoreSubCategory",subCategory.toLowerCase());
                    }
                    else
                    {
                        if(ApplicationClass.USER_LATITUDE!=0.0&&ApplicationClass.USER_LOGITUDE!=0.0) {
                            double lat = 0.0144927536231884;
                            double lon = 0.0181818181818182;
                            double distance = sharedPreferences.getInt("distance", 50);
                            double lowerLat = ApplicationClass.USER_LATITUDE - lat * distance;
                            double lowerLog = ApplicationClass.USER_LOGITUDE - lon * distance;
                            double greaterLat = ApplicationClass.USER_LATITUDE + lat * distance;
                            double greaterLog = ApplicationClass.USER_LOGITUDE + lon * distance;
                            GeoPoint leserGeoPoint = new GeoPoint(lowerLat, lowerLog);
                            GeoPoint greaterGeoPoint = new GeoPoint(greaterLat, greaterLog);
                            query = db.collection(path).whereEqualTo("sortStoreSubCategory", subCategory.toLowerCase())
                                    .whereEqualTo("sortRating", sharedPreferences.getFloat("rating", 4))
                                    .whereGreaterThanOrEqualTo("geoPoint", leserGeoPoint)
                                    .whereLessThanOrEqualTo("geoPoint", greaterGeoPoint);
                        }
                        else
                        {
                            Toast.makeText(context,"first open gps",Toast.LENGTH_LONG).show();
                        }

                    }

                }
                else
                {

                  query=db.collection(path).whereEqualTo("sortStoreSubCategory",subCategory.toLowerCase())
                           .whereEqualTo("storeCity",userCity)
                           .orderBy(sharedPreferences.getString("searchBy","sortOwnerName"))
                            .startAt(newText.toLowerCase());


                }
                FirestoreRecyclerOptions<Seller> options=new FirestoreRecyclerOptions.Builder<Seller>()
                        .setQuery(query,Seller.class)
                        .build();
                adapter=new FirestoreRecyclerAdapter<Seller, SellerViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final SellerViewHolder holder, int position, final @NonNull Seller model) {
                        holder.setDeliveryStatus(model.isDeliveryStatus(),category);
                        holder.setShopStatus(model.isShopStatus());
                        holder.setDistance(model.getStoreLatitude(),model.getStoreLongitude(),category);
                        holder.setRatings(model.getNoOfRatings(),category);
                        holder.setStar(model.getTotalStar(),model.getNoOfRatings(),category);
                        holder.setShopName(model.getStoreName(),model.getOwnerName(),category);
                        // ApplicationClass.setTranslatedText(holder.tvShopAddress,model.getStoreAddress());
                        holder.tvShopAddress.setText(model.getStoreAddress());
                        tvNoExist.setVisibility(GONE);
                        holder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.setItemOnClicked(model.getsUid(),category);
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sellers_layout,parent,false);
                        return new SellerViewHolder(view,  context);
                    }
                };
                recyclerView.setAdapter(adapter);
                adapter.startListening();
                adapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.v("TAG","failer");
            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null)
        {
            adapter.startListening();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null)
        adapter.stopListening();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        return networkInfo!=null;
    }


}
