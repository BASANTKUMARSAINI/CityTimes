package update_dialog;

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

import com.example.mycity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import interfaces.UpdateInterface;
import model.ApplicationClass;

public class UpdateCompanyName extends Dialog {
    public Context context;
    private EditText etCompanyName;
    private Button btnCancel,btnUpdate;
    UpdateInterface updateInterface;

    String  COMPANY_NAME="";
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public UpdateCompanyName(@NonNull Context context, String COMPANY_NAME, UpdateInterface updateInterface) {
        super(context);
        this.context=context;
        this.COMPANY_NAME=COMPANY_NAME;
        this.updateInterface=updateInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_company_name);

        etCompanyName=findViewById(R.id.et_company_name);
        btnCancel=findViewById(R.id.btn_cancel);
        btnUpdate=findViewById(R.id.btn_update);


        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        if(COMPANY_NAME!=null&&(!COMPANY_NAME.equals("")))
            etCompanyName.setText(COMPANY_NAME);
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

        String storeName=etCompanyName.getText().toString();

            List<String>list=new ArrayList<>();
            list.add(storeName);
            updateInterface.update("COMPANY_NAME",list);
             dismiss();

        }


}
