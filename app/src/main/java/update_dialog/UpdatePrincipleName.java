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

import java.util.ArrayList;
import java.util.List;

import interfaces.UpdateInterface;
import model.ApplicationClass;

public class UpdatePrincipleName extends Dialog {
    public Context context;
    private EditText etPrincipleName;
    private Button btnCancel,btnUpdate;
    UpdateInterface updateInterface;

    String   PRINCIPLE_NAME="";


    public UpdatePrincipleName(@NonNull Context context, String PRINCIPLE_NAME, UpdateInterface updateInterface) {
        super(context);
        this.context=context;
        this.PRINCIPLE_NAME=PRINCIPLE_NAME;
        this.updateInterface=updateInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_principle_name);

        etPrincipleName=findViewById(R.id.et_principle_name);
        btnCancel=findViewById(R.id.btn_cancel);
        btnUpdate=findViewById(R.id.btn_update);



        if(!PRINCIPLE_NAME.equals(""))
            etPrincipleName.setText( PRINCIPLE_NAME);
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

        String ownerName=etPrincipleName.getText().toString();
        if(TextUtils.isEmpty(ownerName))
            Toast.makeText(context,"Principle name can't be empty",Toast.LENGTH_LONG).show();
        else{
            List<String> list=new ArrayList<>();
            list.add(ownerName);
            updateInterface.update("PRINCIPLE_NAME",list);
            dismiss();

        }

    }
}
