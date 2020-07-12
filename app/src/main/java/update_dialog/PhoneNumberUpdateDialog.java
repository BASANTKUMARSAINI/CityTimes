package update_dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import authantication.UserLoginActivity;
import dialog.CustumProgressDialog;
import interfaces.UpdateInterface;

public class PhoneNumberUpdateDialog extends Dialog {
    public Context context;
   private EditText etPhone1,etPhone2;
   private Button btnCancel,btnUpdate;
   UpdateInterface updateInterface;

    String PHONE1="",PHONE2="";
    FirebaseFirestore db;
    FirebaseAuth mAuth;


    public PhoneNumberUpdateDialog(@NonNull Context context,String phone1,String phone2,UpdateInterface updateInterface) {
        super(context);
        this.context=context;
        PHONE1=phone1;
        PHONE2=phone2;
        this.updateInterface=updateInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);










        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_phone_number);
        etPhone1=findViewById(R.id.et_phone_1);
        etPhone2=findViewById(R.id.et_phone_2);
        btnCancel=findViewById(R.id.btn_cancel);
        btnUpdate=findViewById(R.id.btn_update);


        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        if(!PHONE1.equals(""))
            etPhone1.setText(PHONE1);
        if(!PHONE2.equals(""))
            etPhone2.setText(PHONE2);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePhoneNumber();
            }


        });



    }
    private void updatePhoneNumber() {
        String phone1=etPhone1.getText().toString();
        String phone2=etPhone2.getText().toString();
        if(TextUtils.isEmpty(phone1))
            Toast.makeText(context,"At least one phone is required",Toast.LENGTH_LONG).show();
        else
        {

            List<String>list=new ArrayList<>();
            list.add(phone1);
            list.add(phone2);
            updateInterface.update("PHONE",list);
            dismiss();
        }

    }
}
