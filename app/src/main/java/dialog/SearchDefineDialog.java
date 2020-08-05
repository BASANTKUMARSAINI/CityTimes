package dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import model.ApplicationClass;
import model.Feedback;

public class SearchDefineDialog extends Dialog {
    public Context context;
   CheckBox ownerNameCheckbox,storeNameCheckbox,storeAddressCheckbox;
    SharedPreferences sharedPreferences;
    ImageView imgCross;

    Button btnApply;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String SEARCH_BY="sortOwnerName";
    public  static String PREF_NAME="MyData";

    public SearchDefineDialog(@NonNull Context context) {
        super(context);
        this.context=context;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.define_search_layout);



        imgCross=findViewById(R.id.img_cross);
        ownerNameCheckbox=findViewById(R.id.owner_checkbox);
        storeAddressCheckbox=findViewById(R.id.store_address_checkbox);
        storeNameCheckbox=findViewById(R.id.store_name_checkbox);
        btnApply=findViewById(R.id.btn_apply);
        sharedPreferences=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);

        String searchBy=sharedPreferences.getString("searchBy","sortOwnerName");
        if(searchBy.equals("sortOwnerName"))
            ownerNameCheckbox.setChecked(true);
        else if(searchBy.equals("sortStoreAddress"))
            storeAddressCheckbox.setChecked(true);
        else if(searchBy.equals("sortStoreName"))
            storeNameCheckbox.setChecked(true);

        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        storeNameCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    storeAddressCheckbox.setChecked(false);
                    ownerNameCheckbox.setChecked(false);
                    SEARCH_BY="sortStoreName";
                }

            }
        });

        storeAddressCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    storeNameCheckbox.setChecked(false);
                    ownerNameCheckbox.setChecked(false);
                    SEARCH_BY="sortStoreAddress";
                }

            }
        });
        ownerNameCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    storeNameCheckbox.setChecked(false);
                    storeAddressCheckbox.setChecked(false);
                    SEARCH_BY = "sortOwnerName";
                }
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("searchBy",SEARCH_BY);
                editor.apply();
                dismiss();
            }
        });
    }





}
