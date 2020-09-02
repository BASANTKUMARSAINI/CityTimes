package sell_and_buy.fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.ApplicationClass;
import model.Product;
import sell_and_buy.ProductMainActivity;
import view_holder.ProductViewHolder;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchProductFragment extends Fragment {
String textSearch="";
FirebaseFirestore db;
FirestoreRecyclerAdapter<Product, ProductViewHolder>adapter;

RecyclerView recyclerView;
    public SearchProductFragment() {

    }
    public SearchProductFragment(String textSearch) {
        this.textSearch=textSearch;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ApplicationClass.loadLocale(getContext());
        View view=inflater.inflate(R.layout.fragment_search_product, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db=FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //decide search
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("MyProduct", Context.MODE_PRIVATE);
        String SEARCH_BY=sharedPreferences.getString("searchBy","sortTitle");

        Query query=db.collection("sellProducts");
        String path=ApplicationClass.LANGUAGE_MODE+"sellProducts";
        if(textSearch.equals(""))
        {
            Log.v("SER","null");
            query=db.collection( path).orderBy(SEARCH_BY);
        }
        else{
            Log.v("SER",textSearch+"  "+SEARCH_BY);
            query=db.collection( path).orderBy(SEARCH_BY).startAt(textSearch.toLowerCase());
        }
        FirestoreRecyclerOptions<Product>options=new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query,Product.class)
                .build();
        adapter=new FirestoreRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Product model) {

              holder.tvSellerCity.setText(model.getScity());
               holder.tvProductPrice.setText(getString(R.string.Rs)+model.getPprice());
                holder.tvProductType. setText(model.getPtype());
               holder.tvProductTitle.setText(model.getPtitle());
                final List<String> imgList=model.getPimage();
                final int siz=imgList.size();
                if(siz>0&&imgList.get(0)!=null)
                    Picasso.get().load(imgList.get(0)).into(holder.productImage1);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent( getContext(), ProductMainActivity.class);
                        intent.putExtra("user_type","buyer");
                        intent.putExtra("pid",model.getPid());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                         startActivity(intent);
                    }
                });
            }


            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_buy_layout,parent,false);
                return new ProductViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter!=null)
            adapter.stopListening();
    }

}
