package update_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class UpdateStoreTime extends Dialog {
    public Context context;

    private Button btnCancel,btnUpdate;
    UpdateInterface updateInterface;
    public EditText fromHour,fromMin,toHour,toMin;
    private ToggleButton fromAM,fromPM,toAM,toPM;

     List<String>from,to;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public UpdateStoreTime(@NonNull Context context, List<String>from,List<String>to, UpdateInterface updateInterface) {
        super(context);
        this.context=context;
       this.from=from;
        this.to=to;
        this.updateInterface=updateInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_store_time);


        btnCancel=findViewById(R.id.btn_cancel);
        btnUpdate=findViewById(R.id.btn_update);

        fromAM=findViewById(R.id.t_from_am);
        fromPM=findViewById(R.id.t_from_pm);
        toAM=findViewById(R.id.t_to_am);
        toPM=findViewById(R.id.t_to_pm);


        fromHour=findViewById(R.id.et_from_h);
        fromMin=findViewById(R.id.et_from_min);
        toHour=findViewById(R.id.et_to_h);
        toMin=findViewById(R.id.et_to_min);



        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();


        setAmPmFrom();
        setAmPmTo();
        setStoreTime();

        fromAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fromAM.isChecked())
                    fromPM.setChecked(false);

            }
        });
        fromPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fromPM.isChecked())
                    fromAM.setChecked(false);
            }
        });

        toAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toAM.isChecked())
                    toPM.setChecked(false);
            }
        });
        toPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toPM.isChecked())
                    toAM.setChecked(false);
            }
        });


        fromHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!fromHour.getText().toString().equals("")) {
                    Integer h = Integer.parseInt(s.toString());
                    if (h >= 12 || h < 0) {

                        fromHour.setText("");
                        Toast.makeText(context, "wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        toHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!toHour.getText().toString().equals("")) {
                    Integer h = Integer.parseInt(s.toString());
                    if (h >= 12 || h < 0) {

                        toHour.setText("");
                        Toast.makeText(context, "wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        toMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!toMin.getText().toString().equals("")) {
                    Integer min = Integer.parseInt(s.toString());
                    if (min >= 60 || min < 0) {

                        toMin.setText("");
                        Toast.makeText(context, "wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fromMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!fromMin.getText().toString().equals("")) {
                    Integer min = Integer.parseInt(s.toString());
                    if (min >= 60 || min < 0) {

                        fromMin.setText("");
                        Toast.makeText(context, "wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStoreTiming();
            }


        });



    }

    private void setStoreTime() {
        fromHour.setText(from.get(0));
        fromMin.setText(from.get(1));
        toHour.setText(to.get(0));
        toMin.setText(to.get(1));
    }

    private void setAmPmTo() {
        if(to.get(2).equals("PM"))
        {
            toPM.setChecked(true);
            toAM.setChecked(false);
        }
        else
        {
            toPM.setChecked(false);
            toAM.setChecked(true);
        }
    }

    private void setAmPmFrom() {
        if(from.get(2).equals("PM"))
        {
            fromPM.setChecked(true);
            fromAM.setChecked(false);
        }
        else
        {
            fromPM.setChecked(false);
            fromAM.setChecked(true);
        }

    }

    private void updateStoreTiming() {
        List<String> timeFrom,timeTo;

        timeFrom=new ArrayList<>();
        timeTo=new ArrayList<>();

        String fromh=fromHour.getText().toString();
        String toh=toHour.getText().toString();
        String fromM=fromMin.getText().toString();
        String toM=toMin.getText().toString();
        if(TextUtils.isEmpty(fromh)||TextUtils.isEmpty(fromM)||TextUtils.isEmpty(toh)||TextUtils.isEmpty(toM))
        {
            Toast.makeText(context,"time can't be empty",Toast.LENGTH_LONG).show();
            return;
        }

        timeFrom.add(fromh);
        timeFrom.add(fromM);
        if (fromPM.isChecked())
            timeFrom.add("PM");
        else
            timeFrom.add("AM");

        timeTo.add(toh);
        timeTo.add(toM);
        if (toPM.isChecked())
            timeTo.add("PM");
        else
            timeTo.add("AM");
        updateInterface.update("STORE_TIMING",timeFrom,timeTo);
        dismiss();

    }
}
