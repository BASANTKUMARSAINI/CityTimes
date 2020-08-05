package sell_and_buy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

import model.ApplicationClass;
import model.Product;
import users.ShopActivity;
import view_holder.ProductViewHolder;

import static android.view.View.GONE;

public class ProductActivity extends AppCompatActivity {
    TextView tvProductType,tvSellingProduct;
    RecyclerView recyclerView;
    String TYPE="";
    FirebaseFirestore db;
    FirestoreRecyclerAdapter<Product, ProductViewHolder>adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(ProductActivity.this);
        setContentView(R.layout.activity_product);
        tvProductType=findViewById(R.id.tv_product_type);
        tvSellingProduct=findViewById(R.id.tv_selling_product);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TYPE=getIntent().getStringExtra("type");
        tvProductType.setText(TYPE);
        //ApplicationClass.setTranslatedText(tvProductType,TYPE);
        tvSellingProduct.setText(TYPE+" for Sellings ");
        //ApplicationClass.setTranslatedText(tvSellingProduct,TYPE+" for sellings");
        db=FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(TYPE.equals("Other"))
        {
            String path=ApplicationClass.LANGUAGE_MODE+"sellProducts";
            Query query=db.collection(path).whereEqualTo("others",true);

            FirestoreRecyclerOptions<Product> options=new FirestoreRecyclerOptions.Builder<Product>()
                    .setQuery(query,Product.class)
                    .build();
            adapter=new FirestoreRecyclerAdapter<Product, ProductViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, final @NonNull Product model) {

//                    ApplicationClass.setTranslatedText(holder.tvSellerCity,model.getScity());
//                    ApplicationClass.setTranslatedText(holder.tvProductPrice,"Rs:"+model.getPprice());
//                    ApplicationClass.setTranslatedText(holder.tvProductType,model.getPtype());
//                    ApplicationClass.setTranslatedText(holder.tvProductTitle,model.getPtitle());
                    holder.tvSellerCity.setText(model.getScity());
                    holder.tvProductPrice.setText(getString(R.string.rs)+model.getPprice());
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
                            Intent intent=new Intent(ProductActivity.this, ProductMainActivity.class);
                            intent.putExtra("category","SELLANDBUY");
                            intent.putExtra("pid",model.getPid());
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

            FirestoreRecyclerOptions<Product>options=new FirestoreRecyclerOptions.Builder<Product>()
                    .setQuery(query,Product.class)
                    .build();
            adapter=new FirestoreRecyclerAdapter<Product, ProductViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, final @NonNull Product model) {

                    //holder.tvSellerPhone.setText(model.getSphone());
//                    ApplicationClass.setTranslatedText(holder.tvSellerCity,model.getScity());
//                    ApplicationClass.setTranslatedText(holder.tvProductPrice,"Rs:"+model.getPprice());
//
//                    ApplicationClass.setTranslatedText(holder.tvProductTitle,model.getPtitle());
                   holder.tvSellerCity.setText(model.getScity());
                    holder.tvProductPrice.setText(getString(R.string.rs)+model.getPprice());
                     //older.tvProductDes.setText(model.getPdescription());
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
                                Intent intent=new Intent(ProductActivity.this, ProductMainActivity.class);
                                intent.putExtra("category","SELLANDBUY");
                                intent.putExtra("pid",model.getPid());
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
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

    public  void goToBack(View view)
    {
        finish();
    }
}
