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

public class UpdateServiceRate extends Dialog {
    public Context context;
    private EditText etServiceRate;
    private Button btnCancel,btnUpdate;
    UpdateInterface updateInterface;

    String  SERVICE_RATE="";
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public UpdateServiceRate(@NonNull Context context, String SERVICE_RATE, UpdateInterface updateInterface) {
        super(context);
        this.context=context;
        this.SERVICE_RATE=SERVICE_RATE;
        this.updateInterface=updateInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_service_rate);

        etServiceRate=findViewById(R.id.et_service_rate);
        btnCancel=findViewById(R.id.btn_cancel);
        btnUpdate=findViewById(R.id.btn_update);


        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        if(!SERVICE_RATE.equals(""))
           etServiceRate.setText(SERVICE_RATE);
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

        String storeName=etServiceRate.getText().toString();
        if(TextUtils.isEmpty(storeName))
            Toast.makeText(context,"Rate can't be empty",Toast.LENGTH_LONG).show();
        else{
            List<String>list=new ArrayList<>();
            list.add(storeName);
          updateInterface.update("SERVICE_RATE",list);
           dismiss();

        }

    }
}
