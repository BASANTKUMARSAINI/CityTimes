package update_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mycity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import interfaces.UpdateInterface;
import model.ApplicationClass;

public class UpdateWorkerRequired extends Dialog {
    public Context context;
    Switch btnSwitch;
    private Button btnCancel,btnUpdate;
    UpdateInterface updateInterface;
    LinearLayout workersQualificationLinearLayout;
    EditText etWorkersQualification;
    boolean isRequired;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String SUB_CATEGORY="",QUALIFICATION=null;

    public UpdateWorkerRequired(@NonNull Context context, boolean isRequired,
                                UpdateInterface updateInterface,String  SUB_CATEGORY,String QUALIFICATION ) {
        super(context);
        this.context=context;
        this.isRequired=isRequired;
        this.updateInterface=updateInterface;
        this.QUALIFICATION=QUALIFICATION;
        this.SUB_CATEGORY=SUB_CATEGORY;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_workers_required);

        btnSwitch=findViewById(R.id.switch_workers);
        btnCancel=findViewById(R.id.btn_cancel);
        btnUpdate=findViewById(R.id.btn_update);
        etWorkersQualification=findViewById(R.id.et_workers_qualification);
        workersQualificationLinearLayout=findViewById(R.id.workers_qualification_linear_layout);

        if(SUB_CATEGORY.equals("Academy")||SUB_CATEGORY.equals("Hospitals")||SUB_CATEGORY.equals("Health Clinics")||SUB_CATEGORY.equals("School"))
        {
            workersQualificationLinearLayout.setVisibility(View.VISIBLE);
        }
        if(QUALIFICATION!=null)
        {
            etWorkersQualification.setText(QUALIFICATION);
        }


        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        if(isRequired)
            btnSwitch.setChecked(true);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRequiredWorkers();
            }


        });



    }
    private void updateRequiredWorkers() {
            String qualification=etWorkersQualification.getText().toString();
            if(TextUtils.isEmpty(qualification))
                    qualification="";
            updateInterface.update("WORKERS",btnSwitch.isChecked(),qualification);
            dismiss();



    }
}
