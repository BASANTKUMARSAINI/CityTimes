package users.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import model.Seller;
import users.StoresActivity;
import view_holder.SellerViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoresFragment extends Fragment {
    RecyclerView recyclerView;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String userCity="";
    String subCategory="";
    String userState="";
    String userCountry="";
    FirestoreRecyclerAdapter<Seller, SellerViewHolder> adapter;
    int CALL_TYPY=-1;


    public StoresFragment() {
        // Required empty public constructor
    }
    public StoresFragment(int CALL_TYPY) {
        // Required empty public constructor
        this.CALL_TYPY=CALL_TYPY;
    }
    public StoresFragment(int CALL_TYPY,String userCountry,String userState,String userCity,String subCategory) {
       this.userCity=userCity;
       this.userCountry=userCountry;
       this.userState=userState;
       this.subCategory=subCategory;
       this.CALL_TYPY=CALL_TYPY;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_stores, container, false);

        recyclerView= view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager( getContext()));

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query query=query=db.collection("sellers");;
        if(CALL_TYPY==1)
         query=db.collection("sellers");
        else if(CALL_TYPY==2)
            query=db.collection("users").document(mAuth.getUid()).collection("favoriteStores");
//        .whereEqualTo("storeCity",userCity)
//                .whereEqualTo("storeCountry",userCountry)
//                .whereEqualTo("storeState",userState).whereEqualTo("storeSubCategory",subCategory);
        FirestoreRecyclerOptions<Seller> options=new FirestoreRecyclerOptions.Builder<Seller>()
                .setQuery(query,Seller.class)
                .build();
        adapter=new FirestoreRecyclerAdapter<Seller, SellerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final SellerViewHolder holder, int position, final @NonNull Seller model) {
                holder.setDeliveryStatus(model.isDeliveryStatus());
                holder.setShopStatus(model.isStoreStatus());
                holder.setDistance();
                holder.setRatings(model.getNoOfRatings());
                holder.setStar(model.getTotalStar(),model.getNoOfRatings());
                holder.setShopName(model.getStoreName());
                holder.setShopAddress(model.getStoreAddress());
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.setItemOnClicked(model.getsUid());
                    }
                });

            }

            @NonNull
            @Override
            public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sellers_layout,parent,false);

                return new SellerViewHolder(view,  getContext());
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null)
        adapter.stopListening();
    }
}
