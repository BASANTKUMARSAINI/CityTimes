package dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mycity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import interfaces.EducationInterface;
import interfaces.UpdateInterface;
import model.ApplicationClass;

public class EducationSetupDialog extends Dialog   {
    public Context context;

    private Button btnCancel, btnSet;
    EducationInterface educationInterface;
    private TextView  tvBoard;
    private CheckBox checkBoxTransport,checkBoxHostel;
    private EditText etPrincipalName;
    public static String BOARD_NAME=null;
    boolean HOSTEL=false,TRANSPORT=false;
    private  String SUB_CATEGORY;

    public EducationSetupDialog(@NonNull Context context ,String SUB_CATEGORY,EducationInterface updateInterface) {
        super(context);
        this.context = context;
        this.SUB_CATEGORY=SUB_CATEGORY;
        this.educationInterface =updateInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.education_detail_dialog);


        tvBoard=findViewById(R.id.tv_school_board);
        checkBoxHostel=findViewById(R.id.checkbox_hostel);
        checkBoxTransport=findViewById(R.id.checkbox_transport);
        etPrincipalName=findViewById(R.id.et_pricipal_name);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSet = findViewById(R.id.btn_update);

         setVisibility();



        checkBoxTransport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    TRANSPORT=true;
                else
                    TRANSPORT=false;
            }
        });
        checkBoxHostel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    HOSTEL=true;
                else
                    HOSTEL=false;
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();

            }


        });
        tvBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               android.widget.PopupMenu popupMenu=new android.widget.PopupMenu(context,tvBoard);
                popupMenu.getMenuInflater().inflate(R.menu.school_board_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        tvBoard.setText(item.getTitle());
                        tvBoard.setTextColor(context.getResources().getColor(R.color.selver));
                        switch (item.getItemId())
                        {
                            case  R.id.up_board:
                                BOARD_NAME="U.P. Board";
                                break;
                            case  R.id.cbse_board:
                                BOARD_NAME="CBSE Board";
                                break;
                            case  R.id.haryana_board:
                                BOARD_NAME="Haryana Board";
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    private void setVisibility() {
        switch (SUB_CATEGORY)
        {
            case "School":
                tvBoard.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setData() {
        String principalName=etPrincipalName.getText().toString();
//        String board=tvBoard.getText().toString();

        if(TextUtils.isEmpty(principalName))
        {
            Toast.makeText(context,"principal name required",Toast.LENGTH_LONG).show();
        }
        else if((BOARD_NAME==null)&&(SUB_CATEGORY.equals("School")))
        {
            Toast.makeText(context,"select school board",Toast.LENGTH_LONG).show();
        }
        else
        {

            educationInterface.educationData(principalName,BOARD_NAME,HOSTEL,TRANSPORT);
            dismiss();
        }
    }


}
