package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import model.ApplicationClass;
import model.CollectionType;
import model.ProductImage;

public class RenameCollectionDialog extends Dialog {
    public Context context;
    private EditText etCollectionName;
    private Button btnCancel,btnUpdate;
    String COLLECTION_NAME=null;
    String COLLECTION_ID=null;
    String HI_COLLECTION_NAME=null;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public  RenameCollectionDialog(@NonNull Context context,String COLLECTION_NAME,String HI_COLLECTION_NAME,String COLLECTION_ID) {
        super(context);
        this.context=context;
        this.COLLECTION_NAME=COLLECTION_NAME;
        this.COLLECTION_ID=COLLECTION_ID;
        this.HI_COLLECTION_NAME=HI_COLLECTION_NAME;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rename_collection_dialog);

        etCollectionName=findViewById(R.id.et_collection_name);
        btnCancel=findViewById(R.id.btn_cancel);
        btnUpdate=findViewById(R.id.btn_update);


        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        etCollectionName.setText(COLLECTION_NAME);
        if(ApplicationClass.LANGUAGE_MODE.equals("hi"))
                etCollectionName.setText(HI_COLLECTION_NAME);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCollection();

            }


        });



    }
    private void  addCollection() {

        final String newCollectionName = etCollectionName.getText().toString();
        if(newCollectionName.equals(COLLECTION_NAME))
        {
            Toast.makeText(context, "New name should be diffrent from previous name", Toast.LENGTH_LONG).show();
        }
       else if (TextUtils.isEmpty(newCollectionName))
            Toast.makeText(context, "Collection name can't be empty", Toast.LENGTH_LONG).show();
        else {
            final CustumProgressDialog dialog=new CustumProgressDialog((Activity)context);
            dialog.startProgressBar(context.getString(R.string.name_editing));
            db.collection("productCollections").whereEqualTo("uid",mAuth.getUid())
                    .whereEqualTo("collectionName",newCollectionName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(queryDocumentSnapshots.isEmpty())
                    {
                        final HashMap<String ,Object>hashMap=new HashMap<>();
                        hashMap.put("collectionName",newCollectionName);
                        ApplicationClass.translatedData=new HashMap<>();
                        ApplicationClass.setTranslatedDataToMap("hicollectionName",newCollectionName);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hashMap.putAll(ApplicationClass.translatedData);
                                db.collection("productCollections").document(COLLECTION_ID).update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            db.collection("productImages").whereEqualTo("uid",mAuth.getUid())
                                                    .whereEqualTo("collectionName",COLLECTION_NAME).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if(!queryDocumentSnapshots.isEmpty())
                                                    {
                                                        for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                                                        {
                                                            ProductImage productImage=documentSnapshot.toObject(ProductImage.class);
                                                            HashMap<String,Object>hashMap1=new HashMap<>();
                                                            hashMap1.put("collectionName",newCollectionName);
                                                            hashMap1.putAll(ApplicationClass.translatedData);
                                                            db.collection("productImages").document(productImage.getImageId()).update(hashMap1);
                                                        }
                                                        dialog.stopProgressBar();
                                                        dismiss();
                                                    }
                                                    else
                                                    {
                                                        dismiss();
                                                        dialog.stopProgressBar();
                                                    }

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    dialog.stopProgressBar();
                                                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                        else
                                        {
                                            dialog.stopProgressBar();
                                            Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }, 1000);
                    }
                    else
                    {
                        dialog.stopProgressBar();
                        Toast.makeText(context,"This name already exist",Toast.LENGTH_LONG).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.stopProgressBar();
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });


//            db.collection("productCollections").whereEqualTo("uid",mAuth.getUid())
//                    .whereEqualTo("collectionName",COLLECTION_NAME)
//                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                @Override
//                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                    if(!queryDocumentSnapshots.isEmpty())
//                    {
//                        for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
//                        {
//                             ProductImage productImage=documentSnapshot.toObject(ProductImage.class);
//
//                            db.collection("sellers").document(mAuth.getUid()).collection("productImages")
//                                    .document(newCollectionName).collection("products").document(productImage.getImageId())
//                                    .set(productImage);
//                            db.collection("sellers").document(mAuth.getUid()).collection("productImages")
//                                    .document(COLLECTION_NAME).collection("products").document(productImage.getImageId())
//                                    .delete();
//
//                        }
//                    }
//                    CollectionType collectionType=new CollectionType();
//                    collectionType.setCollectionName(newCollectionName);
//                    db.collection("sellers").document(mAuth.getUid()).collection("productImages")
//                            .document(newCollectionName).set(collectionType);
//                    db.collection("sellers").document(mAuth.getUid()).collection("productImages")
//                            .document(COLLECTION_NAME).delete();
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    dialog.stopProgressBar();
//                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
//                }
//            });
//            dialog.stopProgressBar();
        }

    }

}
