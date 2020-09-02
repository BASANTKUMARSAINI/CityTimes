package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import dialog.RenameCollectionDialog;
import dialog.SearchDefineDialog;
import model.ApplicationClass;
import model.CollectionType;
import model.ProductImage;
import sell_and_buy.SellAndBuyActivity;
import seller.HandleProductPhotosActivity;
import view_holder.ProductCollectionViewHolder;
import view_holder.ProductImageViewHolder;

public class StorePhotosActivity extends AppCompatActivity {
    String sUid="";
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter<CollectionType, ProductCollectionViewHolder>adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(this);
        setContentView(R.layout.activity_store_photos);
        sUid=getIntent().getStringExtra("sUid");
        recyclerView=findViewById(R.id.recycler_view_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db=FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Query query=db.collection("productCollections").whereEqualTo("uid",sUid);
        FirestoreRecyclerOptions<CollectionType> options=new FirestoreRecyclerOptions.Builder<CollectionType>()
                .setQuery(query,CollectionType.class)
                .build();
        adapter=new FirestoreRecyclerAdapter<CollectionType, ProductCollectionViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductCollectionViewHolder holder, int position, @NonNull final CollectionType model) {
                String collectionName=model.getCollectionName();
                holder.setCollectionType(collectionName);
                if(ApplicationClass.LANGUAGE_MODE.equals("hi"))
                    holder.setCollectionType(model.getHicollectionName());
//ApplicationClass.setTranslatedText(holder.tvCollectionType,model.getCollectionName());

                Query query1=db.collection("productImages").whereEqualTo("uid",sUid)
                        .whereEqualTo("collectionName",collectionName);
                FirestoreRecyclerOptions<ProductImage>options1=new FirestoreRecyclerOptions.Builder<ProductImage>()
                        .setQuery(query1,ProductImage.class)
                        .build();
                FirestoreRecyclerAdapter<ProductImage, ProductImageViewHolder>adapter1=new FirestoreRecyclerAdapter<ProductImage,ProductImageViewHolder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductImageViewHolder holder, int position, @NonNull final ProductImage model1) {
                        holder.setProductImage(model1.getImage());
                        if(model1.getPrice()!=null)
                        holder.tvProductPrice.setText(getString(R.string.rs)+model1.getPrice());
                        holder.btnDelete.setImageResource(R.drawable.share);

                        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String text="Price:"+model1.getPrice()+"$";
                                ApplicationClass.shareImage(model1.getImage(),text,StorePhotosActivity.this);
                            }
                        });

                        holder.imgProduct.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(StorePhotosActivity.this, SeeFullImageActivity.class);
                                intent.putExtra("uri",model1.getImage());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_layout,parent,false);

                        return new ProductImageViewHolder(view);
                    }
                };
                holder.recyclerView_sub.setAdapter(adapter1);
                adapter1.startListening();
                adapter1.notifyDataSetChanged();



                holder.renameLayout.setVisibility(View.GONE);
                holder.addPhotoLayout.setVisibility(View.GONE);


            }

            @NonNull
            @Override
            public ProductCollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_image_layout,parent,false);
                return new ProductCollectionViewHolder(view,StorePhotosActivity.this);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public void goToBack(View view)
    {
        finish();
    }
}
