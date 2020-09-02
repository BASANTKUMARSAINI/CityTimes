package seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dialog.AddCollectionDialog;
import dialog.CustumProgressDialog;
import dialog.RenameCollectionDialog;
import model.ApplicationClass;
import model.CollectionType;
import model.ProductImage;
import users.HomeActivity;
import users.SeeFullImageActivity;
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
    Uri uri=null;
    ProductCollectionViewHolder HOLDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(this);
        setContentView(R.layout.activity_handle_product_photos);

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mStoreRef= FirebaseStorage.getInstance().getReference();
        imageView=findViewById(R.id.img_add_collection);
        recyclerView_main=findViewById(R.id.recycler_view_main);
        recyclerView_main.setLayoutManager(new LinearLayoutManager(this));
        Log.v("TAG","oncreate");
        if(uri!=null)
        {
            Log.v("TAG","not null");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("TAG","onstart");
        final Query query=db.collection("productCollections").whereEqualTo("uid",mAuth.getUid());
        FirestoreRecyclerOptions<CollectionType>options=new FirestoreRecyclerOptions.Builder<CollectionType>()
                                                        .setQuery(query,CollectionType.class)
                                                        .build();
        adapter=new FirestoreRecyclerAdapter<CollectionType, ProductCollectionViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ProductCollectionViewHolder holder, int position, @NonNull final CollectionType model) {
                String collectionName=model.getCollectionName();
                holder.setCollectionType(collectionName);
                if(ApplicationClass.LANGUAGE_MODE.equals("hi"))
                    holder.tvCollectionType.setText(model.getHicollectionName());
                Query query1= db.collection("productImages").whereEqualTo("collectionName",model.getCollectionName())
                        .whereEqualTo("uid",mAuth.getUid());


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
                        holder.tvProductPrice.setText(getString(R.string.rs)+model1.getPrice());
                        if(model1.getPrice()==null)
                        {
                            holder.tvProductPrice.setVisibility(View.GONE);
                        }
                        holder.imgProduct.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(HandleProductPhotosActivity.this, SeeFullImageActivity.class);
                                intent.putExtra("uri",model1.getImage());
                                startActivity(intent);
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
                        String collectionName=model.getCollectionName();

                        String hcollectionName=model.getHicollectionName();
                        RenameCollectionDialog dialog=new RenameCollectionDialog(HandleProductPhotosActivity.this,
                                collectionName,hcollectionName,model.getId());
                        dialog.show();
                    }
                });

                if(uri!=null&&model.getCollectionName().equals(PERSENT_COLLECTION_NAME))
                {
                    Log.d("TAG","Yes");
                    holder.uploadLayout.setVisibility(View.VISIBLE);
                    holder.newImageCard.setVisibility(View.VISIBLE);
                    holder.newImageView.setImageURI(uri);
                }
                     holder.addPhotoLayout.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      PERSENT_COLLECTION_NAME=model.getCollectionName();
                      holder.uploadLayout.setVisibility(View.VISIBLE);
                      HOLDER=holder;
                      addPhoto();
                  }
              });
              holder.tvUpload.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      String price=holder.etProductPrice.getText().toString();
                      if(uri==null)
                      {
                          Toast.makeText(HandleProductPhotosActivity.this,"select an image",Toast.LENGTH_LONG).show();

                      }
                      else
                      {
                          upload(price,holder);
                      }

                  }
              });
              holder.imgDeleteCollection.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      final AlertDialog.Builder builder=new AlertDialog.Builder(HandleProductPhotosActivity.this);
                      builder.setTitle("Delete Collection");
                      builder.setMessage("Do you want delete this?");
                      builder.setCancelable(false);
                      builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              deleteCollection(model.getCollectionName(),model.getId());
                          }
                      }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              dialogInterface.dismiss();
                          }
                      });
                      AlertDialog dialog=builder.create();
                      dialog.show();

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

    private void deleteCollection(String collectionName, String id) {
         db.collection("productImages").whereEqualTo("collectionName",collectionName)
                .whereEqualTo("uid",mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                 if(!queryDocumentSnapshots.isEmpty())
                 {
                     for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots)
                     {
                         ProductImage image=snapshot.toObject(ProductImage.class);
                         db.collection("productImages").document(image.getImageId()).delete();

                     }
                 }
             }
         });
         db.collection("productCollections").document(id).delete();

    }

    private void deletePhotos(String collectionName, final String imageId)
    {
        final CustumProgressDialog dialog=new CustumProgressDialog(HandleProductPhotosActivity.this);
        dialog.startProgressBar(getString(R.string.deleting));
        db.collection("productImages").document(imageId).
                delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    Toast.makeText(HandleProductPhotosActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
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
    public void upload(final String price, final ProductCollectionViewHolder holder)
    {
        String currentTime=new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String currentDate=new SimpleDateFormat("dd:MM:yy", Locale.getDefault()).format(new Date());
        final String imageId="images"+uri.getLastPathSegment()+currentTime+currentDate;


        final CustumProgressDialog dialog=new CustumProgressDialog(HandleProductPhotosActivity.this);
        dialog.startProgressBar(getString(R.string.photo_uploading));
        final  StorageReference childRef=mStoreRef.child("images").child("sellers").child(mAuth.getUid()).child("products").child(imageId);
        childRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri URI) {
                        ProductImage productImage=new ProductImage();
                        productImage.setImage(URI.toString());
                        productImage.setImageId(imageId);
                        if(!TextUtils.isEmpty(price))
                        productImage.setPrice(price);
                        productImage.setCollectionName(PERSENT_COLLECTION_NAME);
                        productImage.setUid(mAuth.getUid());

                      db.collection("productImages")
                               .document(imageId).set(productImage).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              if(task.isSuccessful())
                              {
                                  holder.uploadLayout.setVisibility(View.GONE);
                                  holder.newImageCard.setVisibility(View.GONE);
                                  uri=null;
                                  dialog.stopProgressBar();
                                  Toast.makeText(HandleProductPhotosActivity.this,"success",Toast.LENGTH_LONG).show();
                              }
                              else
                              {
                                  dialog.stopProgressBar();
                                  Toast.makeText(HandleProductPhotosActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                              }
                          }
                      });
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                dialog.stopProgressBar();
                                Toast.makeText(HandleProductPhotosActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
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
//            Log.d("TAG","uri");
//            HOLDER.uploadLayout.setVisibility(View.VISIBLE);
//            HOLDER.newImageCard.setVisibility(View.VISIBLE);
//            HOLDER.newImageView.setImageURI(uri);

        }

    }
}
