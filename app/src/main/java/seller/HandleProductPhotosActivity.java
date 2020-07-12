package seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import dialog.AddCollectionDialog;
import dialog.CustumProgressDialog;
import dialog.RenameCollectionDialog;
import model.CollectionType;
import model.Model;
import model.ProductImage;
import view_holder.ModelViewHolder;
import view_holder.ProductCollectionViewHolder;
import view_holder.ProductImageViewHolder;

public class HandleProductPhotosActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    StorageReference mStoreRef;
    RecyclerView recyclerView_main;
    ImageView imageView;
    FirestoreRecyclerAdapter<CollectionType, ProductCollectionViewHolder>adapter;

    static int  OPEN_GELLERY=1;
    private static String PERSENT_COLLECTION_NAME="store";
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_product_photos);

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mStoreRef= FirebaseStorage.getInstance().getReference();
        imageView=findViewById(R.id.img_add_collection);
        recyclerView_main=findViewById(R.id.recycler_view_main);

       recyclerView_main.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        final Query query=db.collection("sellers").document(mAuth.getUid()).collection("productImages");
        FirestoreRecyclerOptions<CollectionType>options=new FirestoreRecyclerOptions.Builder<CollectionType>()
                                                        .setQuery(query,CollectionType.class)
                                                        .build();
        adapter=new FirestoreRecyclerAdapter<CollectionType, ProductCollectionViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductCollectionViewHolder holder, int position, @NonNull final CollectionType model) {
                String collectionName=model.getCollectionName();
                holder.setCollectionType(collectionName);


                Query query1=db.collection("sellers").document(mAuth.getUid()).collection("productImages")
                     .document(model.getCollectionName()).collection("products");

                FirestoreRecyclerOptions<ProductImage>options1=new FirestoreRecyclerOptions.Builder<ProductImage>()
                                                        .setQuery(query1,ProductImage.class)
                                                        .build();
                FirestoreRecyclerAdapter<ProductImage, ProductImageViewHolder>adapter1=new FirestoreRecyclerAdapter<ProductImage,ProductImageViewHolder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductImageViewHolder holder, int position, @NonNull final ProductImage model1) {
                        holder.setProductImage(model1.getImage());
                        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deletePhotos(model.getCollectionName(),model1.getImageId());
                            }


                        });

                    }

                    @NonNull
                    @Override
                    public ProductImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_layout,parent,false);

                        return new ProductImageViewHolder(view);
                    }
                };
                holder.recyclerView_sub.setAdapter(adapter1);
                adapter1.startListening();
                adapter1.notifyDataSetChanged();



               holder.renameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RenameCollectionDialog dialog=new RenameCollectionDialog(HandleProductPhotosActivity.this,model.getCollectionName());
                        dialog.show();
                    }
                });
              holder.addPhotoLayout.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      PERSENT_COLLECTION_NAME=model.getCollectionName();
                      addPhoto();


                  }
              });


            }

            @NonNull
            @Override
            public ProductCollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_image_layout,parent,false);
                return new ProductCollectionViewHolder(view,HandleProductPhotosActivity.this);
            }
        };
        recyclerView_main.setAdapter(adapter);
        adapter.startListening();

    }
    private void deletePhotos(String collectionName, final String imageId)
    {
        final CustumProgressDialog dialog=new CustumProgressDialog(HandleProductPhotosActivity.this);
        dialog.startProgressBar("deleting...");
        db.collection("sellers").document(mAuth.getUid()).
                collection("productImages").document(collectionName).
                collection("products").document(imageId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    mStoreRef.child("images").child("sellers").child(mAuth.getUid()).child("products").child(imageId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                dialog.stopProgressBar();
                                Toast.makeText(HandleProductPhotosActivity.this,"item deleted",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    dialog.stopProgressBar();
                    Toast.makeText(HandleProductPhotosActivity.this,"try again..",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

    public void addCollection(View view)
    {
       AddCollectionDialog dialog=new AddCollectionDialog(HandleProductPhotosActivity.this);
       dialog.show();

    }
    public void addPhoto( )
    {

        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,OPEN_GELLERY);

    }
    public void upload()
    {
        String currentTime=new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String currentDate=new SimpleDateFormat("dd:MM:yy", Locale.getDefault()).format(new Date());
        final String imageId="images"+uri.getLastPathSegment()+currentTime+currentDate;


        final CustumProgressDialog dialog=new CustumProgressDialog(HandleProductPhotosActivity.this);
        dialog.startProgressBar("photo uploading...");
        final  StorageReference childRef=mStoreRef.child("images").child("sellers").child(mAuth.getUid()).child("products").child(imageId);
        childRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ProductImage productImage=new ProductImage();
                        productImage.setImage(uri.toString());
                        productImage.setImageId(imageId);

                      db.collection("sellers").document(mAuth.getUid()).
                              collection("productImages").document(PERSENT_COLLECTION_NAME)
                              .collection("products").document(imageId).set(productImage).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              if(task.isSuccessful())
                              {
                                  dialog.stopProgressBar();
                                  Toast.makeText(HandleProductPhotosActivity.this,"success",Toast.LENGTH_LONG).show();
                              }
                              else
                              {
                                  dialog.stopProgressBar();
                                  Toast.makeText(HandleProductPhotosActivity.this,"try again...",Toast.LENGTH_LONG).show();
                              }
                          }
                      });
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                dialog.stopProgressBar();
                                Toast.makeText(HandleProductPhotosActivity.this,"error",Toast.LENGTH_LONG).show();
                            }
                        });






            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==OPEN_GELLERY)
        {
            uri=data.getData();
            upload();

        }
    }
}
