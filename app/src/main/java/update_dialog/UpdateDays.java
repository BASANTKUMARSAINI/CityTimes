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
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import com.example.mycity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import interfaces.UpdateInterface;
import model.ApplicationClass;

public class UpdateDays extends Dialog {
    public Context context;
    public ToggleButton tMonday,tSunday,tTuseday,tWednesday,tThrusday,tFriday,tSaturday;
    private Button btnCancel,btnUpdate;
    UpdateInterface updateInterface;

    HashMap<String,Boolean>days;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public UpdateDays(@NonNull Context context,HashMap<String,Boolean>days, UpdateInterface updateInterface) {
        super(context);
        this.context=context;
        this.days=days;
        this.updateInterface=updateInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_days);


        btnCancel=findViewById(R.id.btn_cancel);
        btnUpdate=findViewById(R.id.btn_update);

        tMonday=findViewById(R.id.tMonday);
        tSunday=findViewById(R.id.tSunday);
        tTuseday=findViewById(R.id.tTuseday);
        tWednesday=findViewById(R.id.tWednesday);
        tThrusday=findViewById(R.id.tThursday);
        tFriday=findViewById(R.id.tFriday);
        tSaturday=findViewById(R.id.tSaturday);


        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        setAllChecked();


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

    private void setAllChecked() {
        if(days.get("monday"))
            tMonday.setChecked(true);
        if(days.get("tuesday"))
            tTuseday.setChecked(true);
        if(days.get("wednesday"))
            tWednesday.setChecked(true);
        if(days.get("thursday"))
            tThrusday.setChecked(true);
        if(days.get("friday"))
            tFriday.setChecked(true);
        if(days.get("saturday"))
            tSaturday.setChecked(true);
        if(days.get("sunday"))
            tSunday.setChecked(true);

    }

    private void updateStoreName() {
HashMap<String,Boolean>days=new HashMap<>();
        days.put("sunday",false);
        days.put("monday",false);
        days.put("tuesday",false);
        days.put("wednesday",false);
        days.put("thursday",false);
        days.put("friday",false);
        days.put("saturday",false);

        if(tSunday.isChecked())
            days.put("sunday",true);
        if(tMonday.isChecked())
            days.put("monday",true);
        if(tTuseday.isChecked())
            days.put("tuesday",true);
        if(tWednesday.isChecked())
            days.put("wednesday",true);
        if(tThrusday.isChecked())
            days.put("thursday",true);
        if(tFriday.isChecked())
            days.put("friday",true);
        if(tSaturday.isChecked())
            days.put("saturday",true);


            updateInterface.update("DAYS",days);
            dismiss();



    }
}
