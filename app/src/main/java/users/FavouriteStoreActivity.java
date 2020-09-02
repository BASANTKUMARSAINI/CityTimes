package users;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import model.ApplicationClass;
import model.Favorite;
import model.Seller;
import users.fragments.StoresFragment;
import view_holder.SellerViewHolder;

import static android.view.View.GONE;
public class FavouriteStoreActivity extends AppCompatActivity {
    public static FirestoreRecyclerAdapter<Seller, SellerViewHolder> adapter;
    public static FirebaseFirestore db;
    public static FirebaseAuth mAuth;
    RecyclerView recyclerView;
    TextView tvNoExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(this);
        setContentView(R.layout.activity_favourite_store);
        recyclerView= findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager( this));
        tvNoExist=findViewById(R.id.tv_no_exist);

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
    }
    @Override
    protected void onStart() {
        super.onStart();

        db.collection(ApplicationClass.LANGUAGE_MODE+"users")
                .document(mAuth.getUid()).collection("favoriteStores").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty())
                        {
                            List<Favorite> list=queryDocumentSnapshots.toObjects(Favorite.class);
                            List<String>slist=new ArrayList<>();
                            for(Favorite item:list)
                            {
                                slist.add(item.getsUid());
                            }
//                            for (DocumentSnapshot snapshot:queryDocumentSnapshots)
//                            {
                        //Favorite favorite=snapshot.toObject(Favorite.class);
Log.d("TAG","size"+slist.size());


                        Query query=db.collection(ApplicationClass.LANGUAGE_MODE+"sellers")
                                .whereIn("sUid",slist);
                        FirestoreRecyclerOptions<Seller> options=new FirestoreRecyclerOptions.Builder<Seller>()
                                .setQuery(query,Seller.class)
                                .build();
                        adapter=new FirestoreRecyclerAdapter<Seller, SellerViewHolder>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull final SellerViewHolder holder, int position, final @NonNull Seller model) {
                                holder.setDeliveryStatus(model.isDeliveryStatus(),model.getStoreCategory(),model.getStoreSubCategory());
                                holder.setShopStatus(model.isShopStatus(),model.getStoreCategory(),model.getStoreSubCategory());
                                holder.setDistance(model.getStoreLatitude(),model.getStoreLongitude(),model.getStoreCategory());
                                holder.setRatings(model.getNoOfRatings(),model.getStoreCategory());
                                holder.setStar(model.getTotalStar(),model.getNoOfRatings(),model.getStoreCategory());
                                holder.setShopName(model.getStoreName(),model.getOwnerName(),model.getStoreCategory());
                                holder.setShopAddress(model.getStoreAddress(),model.getStoreCategory());
                                tvNoExist.setVisibility(GONE);
                                if(model.getStoreSubCategory().equals("Auto"))
                                {
                                    FirebaseDatabase.getInstance().getReference().child("current_location")
                                            .child(model.getsUid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Double latitude=snapshot.child("location").child("latitude").getValue(Double.class);
                                            Double longitude=snapshot.child("location").child("longitude").getValue(Double.class);
                                            holder.setDistance(latitude,longitude,model.getStoreCategory());
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            holder.setDistance(model.getStoreLatitude(),model.getStoreLongitude(),model.getStoreCategory());
                                        }
                                    });

                                }else{
                                    holder.setDistance(model.getStoreLatitude(),model.getStoreLongitude(),model.getStoreCategory());
                                }
                                holder.cardView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        holder.setItemOnClicked(model.getsUid(),model.getStoreCategory());
                                    }
                                });
                                holder.setCompanyName(model.getCompanyName(),model.getStoreSubCategory());
                            }
                            @NonNull
                            @Override
                            public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sellers_layout,parent,false);
                                return new SellerViewHolder(view,  FavouriteStoreActivity.this);
                            }
                        };
                        recyclerView.setAdapter(adapter);
                        adapter.startListening();
                        adapter.notifyDataSetChanged();


                }
            }
        });


    }
    public void goToBack(View view)
     {
         finish();
     }
}
