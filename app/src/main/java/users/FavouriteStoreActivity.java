package users;
import androidx.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;


import model.ApplicationClass;
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
        Query query=db.collection(ApplicationClass.LANGUAGE_MODE+"users").document(mAuth.getUid()).collection("favoriteStores");
        FirestoreRecyclerOptions<Seller> options=new FirestoreRecyclerOptions.Builder<Seller>()
                .setQuery(query,Seller.class)
                .build();
        adapter=new FirestoreRecyclerAdapter<Seller, SellerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final SellerViewHolder holder, int position, final @NonNull Seller model) {
                holder.setDeliveryStatus(model.isDeliveryStatus(),model.getStoreCategory());
                holder.setShopStatus(model.isShopStatus());
                holder.setDistance(model.getStoreLatitude(),model.getStoreLongitude(),model.getStoreCategory());
                holder.setRatings(model.getNoOfRatings(),model.getStoreCategory());
                holder.setStar(model.getTotalStar(),model.getNoOfRatings(),model.getStoreCategory());
                holder.setShopName(model.getStoreName(),model.getOwnerName(),model.getStoreCategory());
                holder.setShopAddress(model.getStoreAddress(),model.getStoreCategory());
                tvNoExist.setVisibility(GONE);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.setItemOnClicked(model.getsUid(),model.getStoreCategory());
                    }
                });
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
    public void goToBack(View view)
     {
         finish();
     }
}
