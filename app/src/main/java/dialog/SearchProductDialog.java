package dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.mycity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import model.ApplicationClass;

public class SearchProductDialog extends Dialog {
    public Context context;
   CheckBox titleCheckbox,typeCheckbox,cityCheckbox,phoneCheckbox;
    SharedPreferences sharedPreferences;
    ImageView imgCross;

    Button btnApply;

    String SEARCH_BY="sortTitle";
    public  static String PREF_NAME="MyProduct";

    public SearchProductDialog(@NonNull Context context) {
        super(context);
        this.context=context;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.define_product_search);



        imgCross=findViewById(R.id.img_cross);
        titleCheckbox=findViewById(R.id.title_checkbox);
        typeCheckbox=findViewById(R.id.type_checkbox);
        cityCheckbox=findViewById(R.id.city_checkbox);
        phoneCheckbox=findViewById(R.id.phone_checkbox);
        btnApply=findViewById(R.id.btn_apply);
        sharedPreferences=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);

        String searchBy=sharedPreferences.getString("searchBy","sortTitle");
        if(searchBy.equals("sortTitle"))
            titleCheckbox.setChecked(true);
        else if(searchBy.equals("sortType"))
            typeCheckbox.setChecked(true);
        else if(searchBy.equals("sortCity"))
            cityCheckbox.setChecked(true);
        else if(searchBy.equals("sphone"))
            phoneCheckbox.setChecked(true);

        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        titleCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    cityCheckbox.setChecked(false);
                    typeCheckbox.setChecked(false);
                    phoneCheckbox.setChecked(false);

                    SEARCH_BY="sortTitle";
                }

            }
        });

        typeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    cityCheckbox.setChecked(false);

                    phoneCheckbox.setChecked(false);
                    titleCheckbox.setChecked(false);
                    SEARCH_BY="sortType";
                }

            }
        });
        cityCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    typeCheckbox.setChecked(false);
                    phoneCheckbox.setChecked(false);
                    titleCheckbox.setChecked(false);
                    SEARCH_BY="sortCity";
                }

            }
        });

        phoneCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cityCheckbox.setChecked(false);
                    typeCheckbox.setChecked(false);
                    titleCheckbox.setChecked(false);
                    SEARCH_BY="sphone";
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
