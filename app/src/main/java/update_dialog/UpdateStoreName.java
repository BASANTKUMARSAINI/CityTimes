package update_dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dialog.CustumProgressDialog;
import interfaces.UpdateInterface;
import model.ApplicationClass;

public class UpdateStoreName extends Dialog {
    public Context context;
    private EditText etStoreName;
    private Button btnCancel,btnUpdate;
    UpdateInterface updateInterface;

    String  STORE_NAME="";
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public UpdateStoreName(@NonNull Context context,String STORE_NAME,UpdateInterface updateInterface) {
        super(context);
        this.context=context;
        this.STORE_NAME=STORE_NAME;
        this.updateInterface=updateInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_store_name);

        etStoreName=findViewById(R.id.et_store_name);
        btnCancel=findViewById(R.id.btn_cancel);
        btnUpdate=findViewById(R.id.btn_update);


        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        if(!STORE_NAME.equals(""))
           etStoreName.setText(STORE_NAME);
           // ApplicationClass.setTranslatedText(etStoreName,STORE_NAME);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStoreName();
            }


        });



    }
    private void updateStoreName() {

        String storeName=etStoreName.getText().toString();
        if(TextUtils.isEmpty(storeName))
            Toast.makeText(context,"Store name can't be empty",Toast.LENGTH_LONG).show();
        else{
            List<String>list=new ArrayList<>();
            list.add(storeName);
          updateInterface.update("STORE_NAME",list);
           dismiss();

        }

    }
}
