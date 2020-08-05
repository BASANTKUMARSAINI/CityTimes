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
//                ApplicationClass.setTranslatedText(holder.tvSellerCity,model.getScity());
//                ApplicationClass.setTranslatedText(holder.tvProductPrice,"Rs:"+model.getPprice());
//
//                ApplicationClass.setTranslatedText(holder.tvProductTitle,model.getPtitle());
//                ApplicationClass.setTranslatedText(holder.tvProductType,model.getPtype());
               // holder.tvSellerPhone.setText(model.getSphone());
               holder.tvSellerCity.setText(model.getScity());
                holder.tvProductPrice.setText(getString(R.string.rs)+model.getPprice());
               // holder.tvProductDes.setText(model.getPdescription());
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
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
//                holder.productDelete.setVisibility(View.VISIBLE);
//                holder.productDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        deleteProduct(model.getPid(),siz);
//                    }
//                });
//                holder.productShare.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String text="Tittle:"+model.getPtitle()+"\n"
//                                +"Price:"+model.getPprice()+"\n"
//                                +"Phone:"+model.getSphone()+"\n"+
//                                model.getPdescription();
//                        ApplicationClass.shareImage(imgList.get(0),text,MyProductActivity.this);
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

    private void deleteProduct(final String pid,final int siz) {
        AlertDialog.Builder builder=new AlertDialog.Builder(MyProductActivity.this);
        builder.setMessage(R.string.do_you_want_delete_this_product);
        builder.setTitle(R.string.delete_product);
        builder.setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String path="en"+"sellProducts";
                            db.collection(path).document(pid).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    db.collection("hisellProducts").document(pid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            List<String>imgIdList=new ArrayList<>();
                                            imgIdList.add("image1.jpg");
                                            imgIdList.add("image2.jpg");
                                            imgIdList.add("image3.jpg");
                                            imgIdList.add("image4.jpg");
                                            imgIdList.add("image5.jpg");
                                            for(int i=0;i<siz;i++)
                                            {
                                                mStoreRef.child("images").child("sellProduct").child(pid).child(imgIdList.get(i)).delete();
                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MyProductActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MyProductActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });

                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();

    }

    private void makeCall(String sphone) {
       ApplicationClass.makeCall(MyProductActivity.this,sphone);
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
//            Toast.makeText(MyProductActivity.this,"no more photos",Toast.LENGTH_LONG).show();
//        }
//
//
//    }
    public void goToBack(View view)
    {
        finish();
    }
    private void setSliderImage() {

        ApplicationClass.setSliderImage(MyProductActivity.this,sliderView,"general");
    }
}
