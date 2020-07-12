package com.example.mycity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import model.Model;
import model.ProductImage;
import view_holder.ModelViewHolder;
import view_holder.ProductImageViewHolder;

public class TextActivity extends AppCompatActivity {
    private static final int PROFILE = 1;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    StorageReference mStoreRef;

    FirestoreRecyclerAdapter<Model,ModelViewHolder>adapter;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        mStoreRef= FirebaseStorage.getInstance().getReference();
        imageView=findViewById(R.id.btn_add);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

    }
    public void show(final View view) {
        Log.v("TAG","in");
        Query query=db.collection("sellers").document(mAuth.getUid()).collection("productImages")
                     .document("store").collection("products");
        FirestoreRecyclerOptions<Model>options=new FirestoreRecyclerOptions.Builder<Model>()
                                                .setQuery(query,Model.class)
                                                .build();
        Log.v("TAG","inout");
        adapter=new FirestoreRecyclerAdapter<Model, ModelViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ModelViewHolder holder, int position, @NonNull Model model) {
                Picasso.get().load(model.getImageUrls()).into(holder.imgUser);
                Log.v("TAG",model.getImageUrls()+"kk");
            }

            @NonNull
            @Override
            public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1=LayoutInflater.from(parent.getContext()).inflate(R.layout.model_layout,parent,false);
                Log.v("TAG","outin");
                return new ModelViewHolder(view1);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        Log.v("TAG","out");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.v("TAG","outout");
    }

    public void addName(View view)
    {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,PROFILE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==PROFILE)
            {
                Uri uri=data.getData();
                imageView.setImageURI(uri);
               final StorageReference childRef= mStoreRef.child("users");
               childRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               String dowanloadUrl=uri.toString();
                               Model model=new Model();
                               model.setImageUrls(dowanloadUrl);
                               db.collection("txt").add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                   @Override
                                   public void onSuccess(DocumentReference documentReference) {
                                       Toast.makeText(TextActivity.this,"success",Toast.LENGTH_LONG).show();
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Log.v("TAG",e.getMessage());
                                   }
                               });
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Log.v("TAG",e.getMessage());
                           }
                       });

                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Log.v("TAG",e.getMessage());
                   }
               });




            }
        }
    }
}
