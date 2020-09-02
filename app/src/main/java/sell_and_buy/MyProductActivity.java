package sell_and_buy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.mycity.R;
import com.facebook.login.LoginManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import adapter.SliderAdapterExample;
import authantication.UserLoginActivity;
import model.ApplicationClass;
import model.Product;
import model.SliderItem;
import view_holder.ProductViewHolder;

import static android.view.View.GONE;

public class MyProductActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    StorageReference mStoreRef;
    SliderView sliderView;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter<Product, ProductViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(MyProductActivity.this);
        setContentView(R.layout.activity_my_product);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mStoreRef= FirebaseStorage.getInstance().getReference();
        sliderView= findViewById(R.id.imageSlider);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyProductActivity.this));
        setSliderImage();


    }
    @Override
    public void onStart() {
        super.onStart();


        String path=ApplicationClass.LANGUAGE_MODE+"sellProducts";
        Query query=db.collection(path).whereEqualTo("uid",mAuth.getUid());

        FirestoreRecyclerOptions<Product> options=new FirestoreRecyclerOptions.Builder<Product>()
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
                        Intent intent=new Intent(MyProductActivity.this, ProductMainActivity.class);
                        intent.putExtra("user_type","seller");
                        intent.putExtra("pid",model.getPid());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

//                holder.productDelete.setVisibility(View.VISIBLE);
//                holder.productDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        deleteProduct(model.getPid(),siz);
//                    }
//                });

            }


            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_buy_layout,parent,false);
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
    public void goToBack(View view)
    {
        finish();
    }
    private void setSliderImage() {

        ApplicationClass.setSliderImage(MyProductActivity.this,sliderView,"general");
    }
}
