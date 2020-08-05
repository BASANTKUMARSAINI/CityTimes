package update_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mycity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import interfaces.UpdateInterface;
import model.ApplicationClass;

public class UpdateOwnerName extends Dialog {
    public Context context;
    private EditText etOwnerName;
    private Button btnCancel,btnUpdate;
    UpdateInterface updateInterface;

    String   OWNER_NAME="";
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public UpdateOwnerName(@NonNull Context context, String OWNER_NAME, UpdateInterface updateInterface) {
        super(context);
        this.context=context;
        this.OWNER_NAME=OWNER_NAME;
        this.updateInterface=updateInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_owner_name);

        etOwnerName=findViewById(R.id.et_owner_name);
        btnCancel=findViewById(R.id.btn_cancel);
        btnUpdate=findViewById(R.id.btn_update);


        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        if(!OWNER_NAME.equals(""))
            //ApplicationClass.setTranslatedText( etOwnerName,OWNER_NAME);
            etOwnerName.setText(OWNER_NAME);
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

        String ownerName=etOwnerName.getText().toString();
        if(TextUtils.isEmpty(ownerName))
            Toast.makeText(context,"Owner name can't be empty",Toast.LENGTH_LONG).show();
        else{
            List<String> list=new ArrayList<>();
            list.add(ownerName);
            updateInterface.update("OWNER_NAME",list);
            dismiss();

        }

    }
}
