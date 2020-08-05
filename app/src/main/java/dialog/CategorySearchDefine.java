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

public class CategorySearchDefine extends Dialog {
    public Context context;
   CheckBox  subCategoryCheckbox,categoryCheckbox ;
    SharedPreferences sharedPreferences;
    ImageView imgCross;

    Button btnApply;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String SEARCH_BY="subCategoryName";
    public  static String PREF_NAME="MyData";

    public CategorySearchDefine(@NonNull Context context) {
        super(context);
        this.context=context;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.category_search_define_layout);



        imgCross=findViewById(R.id.img_cross);
        subCategoryCheckbox=findViewById(R.id.sub_category_name_checkbox);
        categoryCheckbox=findViewById(R.id.category_name_checkbox);
        btnApply=findViewById(R.id.btn_apply);
        sharedPreferences=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);

        String searchBy=sharedPreferences.getString("categorySearchBy","subCategoryName");
        if(searchBy.equals("subCategoryName"))
            subCategoryCheckbox.setChecked(true);
        else if(searchBy.equals("categoryName"))
             categoryCheckbox.setChecked(true);


        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

         subCategoryCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    categoryCheckbox.setChecked(false);
                    SEARCH_BY="subCategoryName";
                }

            }
        });

        categoryCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    subCategoryCheckbox.setChecked(false);
                    SEARCH_BY="categoryName";
                }

            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("categorySearchBy",SEARCH_BY);
                editor.apply();
                dismiss();
            }
        });
    }





}
