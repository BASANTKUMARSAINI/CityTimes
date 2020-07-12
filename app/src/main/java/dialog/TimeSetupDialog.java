package dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.mycity.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import authantication.UserLoginActivity;
import seller.SetupProfileActivity;

public class TimeSetupDialog  {
    Activity activity;
    AlertDialog dialog;
    public ToggleButton tMonday,tSunday,tTuseday,tWednesday,tThrusday,tFriday,tSaturday,fromAM,fromPM,toAM,toPM;
    public EditText fromHour,fromMin,toHour,toMin;
    public Button btnNext;
    public TimeSetupDialog(Activity activity)
    {
        this.activity=activity;
    }
    public  void startProgressBar()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setCancelable(false);
        final View view=inflater.inflate(R.layout.time_setup_layout,null);
        tMonday=view.findViewById(R.id.tMonday);
        tSunday=view.findViewById(R.id.tSunday);
        tTuseday=view.findViewById(R.id.tTuseday);
        tWednesday=view.findViewById(R.id.tWednesday);
        tThrusday=view.findViewById(R.id.tThursday);
        tFriday=view.findViewById(R.id.tFriday);
        tSaturday=view.findViewById(R.id.tSaturday);



        fromAM=view.findViewById(R.id.t_from_am);
        fromPM=view.findViewById(R.id.t_from_pm);
        toAM=view.findViewById(R.id.t_to_am);
        toPM=view.findViewById(R.id.t_to_pm);


        fromHour=view.findViewById(R.id.et_from_h);
        fromMin=view.findViewById(R.id.et_from_min);
        toHour=view.findViewById(R.id.et_to_h);
        toMin=view.findViewById(R.id.et_to_min);


        fromAM.setChecked(true);
        toPM.setChecked(true);

        tMonday.setChecked(false);
        tSunday.setChecked(true);
        tTuseday.setChecked(true);
        tWednesday.setChecked(true);
        tThrusday.setChecked(true);
        tFriday.setChecked(true);
        tSaturday.setChecked(true);


        btnNext=view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Boolean> days;
                List<String> timeFrom,timeTo;
                days=new HashMap<>();
                timeFrom=new ArrayList<>();
                timeTo=new ArrayList<>();

                String fromh=fromHour.getText().toString();
                String toh=toHour.getText().toString();
                String fromM=fromMin.getText().toString();
                String toM=toMin.getText().toString();
                if(TextUtils.isEmpty(fromh)||TextUtils.isEmpty(fromM)||TextUtils.isEmpty(toh)||TextUtils.isEmpty(toM))
                {
                    Toast.makeText(activity,"time can't be empty",Toast.LENGTH_LONG).show();
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

                days.put("sunday",false);
                days.put("monday",false);
                days.put("tuseday",false);
                days.put("wednesday",false);
                days.put("thrusday",false);
                days.put("friday",false);
                days.put("saturday",false);

                if(tSunday.isChecked())
                    days.put("sunday",true);
                if(tMonday.isChecked())
                    days.put("monday",true);
                if(tTuseday.isChecked())
                    days.put("tuseday",true);
                if(tWednesday.isChecked())
                    days.put("wednesday",true);
                if(tThrusday.isChecked())
                    days.put("thrusday",true);
                if(tFriday.isChecked())
                    days.put("friday",true);
                if(tSaturday.isChecked())
                    days.put("saturday",true);
                SetupProfileActivity object=new SetupProfileActivity();

               object.setAllData(days,timeFrom,timeTo);
                dialog.dismiss();
            }
        });
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
                    Toast.makeText(activity, "wrong", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(activity, "wrong", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(activity, "wrong", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(activity, "wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        builder.setView(view);
        dialog=builder.create();
        dialog.show();
    }


    public void stopProgressBar()
    {
        if(dialog!=null)
            dialog.dismiss();
    }


}
