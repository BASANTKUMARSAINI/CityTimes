package sell_and_buy.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.ApplicationClass;
import model.Product;
import sell_and_buy.ProductActivity;
import sell_and_buy.ProductMainActivity;
import view_holder.ProductViewHolder;

import static android.view.View.GONE;


public class SpecificSearchFragment extends Fragment {

    String TYPE;
    RecyclerView recyclerView;
    String searchText="";
    TextView tvProductType;
    FirebaseFirestore db;
    FirestoreRecyclerAdapter<Product, ProductViewHolder>adapter;

    public SpecificSearchFragment() {

    }
    public SpecificSearchFragment(String TYPE) {
        this.TYPE=TYPE;

    }
    public SpecificSearchFragment(String TYPE,String searchText) {
        this.TYPE=TYPE;
        this.searchText=searchText;

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_specific_search, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvProductType=view.findViewById(R.id.tv_selling_product);
        tvProductType.setText(TYPE+getContext().getString(R.string.products_for_selling));
        db=FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if((!TextUtils.isEmpty(TYPE))&&TYPE.equals("Other"))
        {
            String path= ApplicationClass.LANGUAGE_MODE+"sellProducts";
            Query query=db.collection(path).whereEqualTo("others",true);
            if(!TextUtils.isEmpty(searchText))
            {
                List<String>list=new ArrayList<>();

                list.add(searchText.toLowerCase());

                int siz=searchText.length();
                 for(int i=1;i<siz&&i<=9;i++)
                 {
                     list.add(searchText.substring(i).toLowerCase());
                 }
                query=db.collection(path).whereEqualTo("others",true).whereIn("sortTitle",list);
            }

            FirestoreRecyclerOptions<Product> options=new FirestoreRecyclerOptions.Builder<Product>()
                    .setQuery(query,Product.class)
                    .build();
            adapter=new FirestoreRecyclerAdapter<Product, ProductViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, final @NonNull Product model) {
                    holder.tvSellerCity.setText(model.getScity());
                    holder.tvProductPrice.setText(getString(R.string.Rs)+model.getPprice());
                    holder.tvProductType.setText(model.getPtype());
                    holder.tvProductTitle.setText(model.getPtitle());

                    final List<String> imgList = model.getPimage();
                    if (imgList != null) {
                        final int siz = imgList.size();
                        if (siz > 0 && imgList.get(0) != null)
                            Picasso.get().load(imgList.get(0)).into(holder.productImage1);

                    }
                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(getContext(), ProductMainActivity.class);
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
                    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_buy_layout,parent,false);
                    return new ProductViewHolder(view);
                }
            };
        }
        else
        {
            String path=ApplicationClass.LANGUAGE_MODE+"sellProducts";
            Query query=db.collection(path).whereEqualTo("sortType",TYPE.toLowerCase());
            if(!TextUtils.isEmpty(searchText))
            {
                query=db.collection(path).whereEqualTo("sortType",TYPE.toLowerCase()).orderBy("sortTitle").startAt(searchText.toLowerCase());
            }

            FirestoreRecyclerOptions<Product>options=new FirestoreRecyclerOptions.Builder<Product>()
                    .setQuery(query,Product.class)
                    .build();
            adapter=new FirestoreRecyclerAdapter<Product, ProductViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, final @NonNull Product model) {
                    holder.tvSellerCity.setText(model.getScity());
                    holder.tvProductPrice.setText(getString(R.string.Rs)+model.getPprice());
                    holder.tvProductType.setVisibility(GONE);
                    holder.tvProductTitle.setText(model.getPtitle());
                    final List<String>imgList=model.getPimage();
                    if(imgList!=null) {
                        final int siz = imgList.size();
                        if (siz > 0 && imgList.get(0) != null)
                            Picasso.get().load(imgList.get(0)).into(holder.productImage1);
                        holder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getContext(), ProductMainActivity.class);
                                intent.putExtra("user_type","buyer");
                                intent.putExtra("pid",model.getPid());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });

                    }
                }

                @NonNull
                @Override
                public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_buy_layout,parent,false);
                    return new ProductViewHolder(view);
                }
            };
        }
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }
}