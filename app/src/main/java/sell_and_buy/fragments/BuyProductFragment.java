package sell_and_buy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import model.ApplicationClass;
import model.ProductCategory;
import sell_and_buy.ProductActivity;

public class BuyProductFragment extends Fragment {
    RecyclerView recyclerView;
FirebaseFirestore db;
FirestoreRecyclerAdapter<ProductCategory,ProductMainViewHolder>adapter;


    public BuyProductFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ApplicationClass.loadLocale(getContext());
        View view=inflater.inflate(R.layout.fragment_buy_product, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db=FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query query=db.collection("productCategory").orderBy("priority", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<ProductCategory>options=new FirestoreRecyclerOptions.Builder<ProductCategory>()
                                                        .setQuery(query,ProductCategory.class)
                                                        .build();
        adapter=new FirestoreRecyclerAdapter<ProductCategory, ProductMainViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductMainViewHolder holder, int position, @NonNull final ProductCategory model) {

                if(ApplicationClass.LANGUAGE_MODE.equals("hi"))
                {
                    holder.tvProductType.setText(model.getHicategoryName());
                }
                else
                {
                    holder.tvProductType.setText(model.getCategoryName());
                }

                holder.tvProductType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getContext(), ProductActivity.class);
                        intent.putExtra("type",model.getCategoryName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.product_main_layout,parent,false);
                return new ProductMainViewHolder(view);
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

    public class ProductMainViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductType;
        View viewLine;

        Context context;
        public ProductMainViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductType=itemView.findViewById(R.id.tv_product_type);
            viewLine=itemView.findViewById(R.id.view_line);
        }
    }
}
