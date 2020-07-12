package dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import interfaces.UpdateInterface;

public class AddCollectionDialog extends Dialog {
    public Context context;
    private EditText etCollectionName;
    private Button btnCancel,btnUpdate;



    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public AddCollectionDialog(@NonNull Context context ) {
        super(context);
        this.context=context;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_collection);

      etCollectionName=findViewById(R.id.et_collection_name);
        btnCancel=findViewById(R.id.btn_cancel);
        btnUpdate=findViewById(R.id.btn_update);


        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();


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

        String collectionName=etCollectionName.getText().toString();
        if(TextUtils.isEmpty( collectionName))
            Toast.makeText(context,"Collection name can't be empty",Toast.LENGTH_LONG).show();
        else{
            HashMap<String,Object>hashMap=new HashMap<>();
            hashMap.put("collectionName",collectionName);
           db.collection("sellers").document(mAuth.getUid()).
                   collection("productImages").document(collectionName).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful())
                   {
                       Toast.makeText(context,"added",Toast.LENGTH_LONG).show();
                   }
                   else
                   {
                       Log.v("TAG",task.getException().getMessage());
                       Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                   }
               }
           });
           dismiss();

        }

    }
}