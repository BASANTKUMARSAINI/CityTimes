package update_dialog;

import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
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
import java.util.Locale;

import interfaces.UpdateInterface;
import model.ApplicationClass;
import seller.RegisterStoreActivity;

public class UpdateStoreAddress extends Dialog {
    public Context context;
    private EditText etStoreAddress;
    private Button btnCancel,btnUpdate;
    UpdateInterface updateInterface;

    String  STORE_ADDRESS="";
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Double STORE_LONGITUDE=1.0,STORE_LATITUDE=1.0;

    public UpdateStoreAddress(@NonNull Context context, String STORE_ADDRESS, UpdateInterface updateInterface) {
        super(context);
        this.context=context;
        this.STORE_ADDRESS=STORE_ADDRESS;
        this.updateInterface=updateInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_store_address);

        etStoreAddress=findViewById(R.id.et_store_address);
        btnCancel=findViewById(R.id.btn_cancel);
        btnUpdate=findViewById(R.id.btn_update);


        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        if(!STORE_ADDRESS.equals(""))
           etStoreAddress.setText(STORE_ADDRESS);
            //ApplicationClass.setTranslatedText(etStoreAddress,STORE_ADDRESS);
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

        String storeAddress=etStoreAddress.getText().toString();
        if(TextUtils.isEmpty(storeAddress))
            Toast.makeText(context,"Store Address can't be empty",Toast.LENGTH_LONG).show();
        else{
            List<String>list=new ArrayList<>();
            list.add(storeAddress);
            if(!getAddress(storeAddress))
            {
                Toast.makeText(context,"invalid new address",Toast.LENGTH_LONG).show();
                return;
            }
                 updateInterface.update("STORE_ADDRESS",list,STORE_LONGITUDE,STORE_LATITUDE);
           dismiss();

        }

    }
    public boolean getAddress(String address)
    {

        Geocoder geocoder=new Geocoder(context, Locale.getDefault());
        try {

            List list=geocoder.getFromLocationName(address,1);
            if(list!=null&&list.size()>0)
            {
                Address ADDRESS=(Address) list.get(0);
                STORE_LATITUDE=ADDRESS.getLatitude();
                STORE_LONGITUDE=ADDRESS.getLongitude();
                return  true;

            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
