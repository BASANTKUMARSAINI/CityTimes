package update_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mycity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import interfaces.UpdateInterface;

public class UpdateWorkerRequired extends Dialog {
    public Context context;
    Switch btnSwitch;
    private Button btnCancel,btnUpdate;
    UpdateInterface updateInterface;

    boolean isRequired;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public UpdateWorkerRequired(@NonNull Context context, boolean isRequired, UpdateInterface updateInterface) {
        super(context);
        this.context=context;
        this.isRequired=isRequired;
        this.updateInterface=updateInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_workers_required);

        btnSwitch=findViewById(R.id.switch_workers);
        btnCancel=findViewById(R.id.btn_cancel);
        btnUpdate=findViewById(R.id.btn_update);


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



            updateInterface.update("WORKERS",btnSwitch.isChecked());
            dismiss();



    }
}
