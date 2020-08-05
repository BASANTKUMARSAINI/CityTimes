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
//                ApplicationClass.setTranslatedText(holder.tvSellerCity,model.getScity());
//                ApplicationClass.setTranslatedText(holder.tvProductPrice,"Rs:"+model.getPprice());
//                ApplicationClass.setTranslatedText(holder.tvProductType,model.getPtype());
//                ApplicationClass.setTranslatedText(holder.tvProductTitle,model.getPtitle());
                //holder.tvSellerPhone.setText(model.getSphone());
              holder.tvSellerCity.setText(model.getScity());
               holder.tvProductPrice.setText(getString(R.string.rs)+model.getPprice());
                //holder.tvProductDes.setText(model.getPdescription());
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
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("pid",model.getPid());
                         startActivity(intent);
                    }
                });
//                holder.morePhotos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if(isChecked)
//                        {
//                            setImages(holder,imgList);
//                        }
//                        else
//                        {
//                            holder.photosLayoutLastTwo.setVisibility(GONE);
//                            holder.photosLayoutFirsTwo.setVisibility(GONE);
//                        }
//                    }
//                });
//
//                holder.call.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        makeCall(model.getSphone());
//                    }
//                });
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

//    private void setImages(ProductViewHolder holder, List<String>imgList) {
//        int siz=imgList.size();
//        if(siz==5)
//        {
//            holder.photosLayoutLastTwo.setVisibility(View.VISIBLE);
//            holder.photosLayoutFirsTwo.setVisibility(View.VISIBLE);
//            Picasso.get().load(imgList.get(1)).into(holder.productImage2);
//            Picasso.get().load(imgList.get(2)).into(holder.productImage3);
//            Picasso.get().load(imgList.get(3)).into(holder.productImage4);
//            Picasso.get().load(imgList.get(4)).into(holder.productImage5);
//        }
//        else if(siz==4)
//        {
//            holder.photosLayoutLastTwo.setVisibility(View.VISIBLE);
//            holder.photosLayoutFirsTwo.setVisibility(View.VISIBLE);
//            Picasso.get().load(imgList.get(1)).into(holder.productImage2);
//            Picasso.get().load(imgList.get(2)).into(holder.productImage3);
//            Picasso.get().load(imgList.get(3)).into(holder.productImage4);
//            holder.productImage5.setVisibility(GONE);
//        }
//        else if(siz==3)
//        {
//
//            holder.photosLayoutFirsTwo.setVisibility(View.VISIBLE);
//            Picasso.get().load(imgList.get(1)).into(holder.productImage2);
//            Picasso.get().load(imgList.get(2)).into(holder.productImage3);
//            holder.photosLayoutLastTwo.setVisibility(GONE);
//
//        }
//        else if(siz==2)
//        {
//
//            holder.photosLayoutFirsTwo.setVisibility(View.VISIBLE);
//            Picasso.get().load(imgList.get(1)).into(holder.productImage2);
//            holder.productImage3.setVisibility(GONE);
//            holder.photosLayoutLastTwo.setVisibility(GONE);
//
//
//        }
//        else
//        {
//            holder.photosLayoutLastTwo.setVisibility(GONE);
//            holder.photosLayoutFirsTwo.setVisibility(GONE);
//            Toast.makeText(getContext(),"no more photos",Toast.LENGTH_LONG).show();
//        }
//
//
//    }

//    private void makeCall(String sphone) {
//    }

}
