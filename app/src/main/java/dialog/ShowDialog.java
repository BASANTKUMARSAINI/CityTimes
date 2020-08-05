package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import interfaces.UpdateInterface;
import model.ApplicationClass;
import model.MoveShow;

public class ShowDialog extends Dialog  {
    public Context context;

    private Button btnCancel,btnAdd;
   // UpdateInterface updateInterface;
    private TextView tvFromhh,tvfrommm,tvTohh,tvTomm;
    ToggleButton toggleFrom,toggleTo;
    EditText etPrice,etName;


    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public ShowDialog(@NonNull Context context) {
        super(context);
        this.context=context;

        //this.updateInterface=updateInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_show_dialog);


        btnCancel=findViewById(R.id.btn_cancel);
        btnAdd=findViewById(R.id.btn_add_show);
        tvFromhh=findViewById(R.id.tv_time_from_hh);
        tvfrommm=findViewById(R.id.tv_time_from_mm);
        tvTohh=findViewById(R.id.tv_time_to_hh);
        tvTomm=findViewById(R.id.tv_time_to_mm);

        toggleFrom=findViewById(R.id.from_am);
        toggleTo=findViewById(R.id.to_am);

        etName=findViewById(R.id.et_movie_name);
        etPrice=findViewById(R.id.et_movie_price);

        tvFromhh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu=new PopupMenu(context,tvFromhh);
                menu.getMenuInflater().inflate(R.menu.hour_menu,menu.getMenu());;
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        tvFromhh.setText(item.getTitle());
                        tvFromhh.setTextColor(context.getResources().getColor(R.color.button_color));
                        return true;
                    }
                });
                menu.show();

            }
        });
        tvfrommm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu=new PopupMenu(context,tvfrommm);
                menu.getMenuInflater().inflate(R.menu.minute_menu,menu.getMenu());;
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        tvfrommm.setText(item.getTitle());
                        tvfrommm.setTextColor(context.getResources().getColor(R.color.button_color));
                        return true;
                    }
                });
                menu.show();

            }
        });
        tvTohh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu=new PopupMenu(context,tvTohh);
                menu.getMenuInflater().inflate(R.menu.hour_menu,menu.getMenu());;
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        tvTohh.setText(item.getTitle());
                        tvTohh.setTextColor(context.getResources().getColor(R.color.button_color));
                        return true;
                    }
                });
                menu.show();

            }
        });
        tvTomm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu=new PopupMenu(context,tvTomm);
                menu.getMenuInflater().inflate(R.menu.minute_menu,menu.getMenu());;
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        tvTomm.setText(item.getTitle());
                        tvTomm.setTextColor(context.getResources().getColor(R.color.button_color));
                        return true;
                    }
                });
                menu.show();

            }
        });






        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=etName.getText().toString();
                String price=etPrice.getText().toString();
                String fromhh=tvFromhh.getText().toString();
                String frommm=tvfrommm.getText().toString();
                String tohh=tvTohh.getText().toString();
                String tomm=tvTomm.getText().toString();
                if(TextUtils.isEmpty(name))
                {
                    Toast.makeText(context,"enter movie name",Toast.LENGTH_LONG).show();
                }
                else  if(TextUtils.isEmpty(price))
                {
                    Toast.makeText(context,"enter movie price",Toast.LENGTH_LONG).show();
                }
                else  if(TextUtils.isEmpty(fromhh)||fromhh.equals("hh")||TextUtils.isEmpty(frommm)||frommm.equals("mm")
                ||TextUtils.isEmpty(tohh)||tohh.equals("hh")||TextUtils.isEmpty(tomm)||tomm.equals("mm"))
                {
                    Toast.makeText(context,"enter time",Toast.LENGTH_LONG).show();
                }
                else
                {
                    ApplicationClass.translatedData=new HashMap<>();
                    ApplicationClass.setTranslatedDataToMap("hname",name);
                    final CustumProgressDialog dialog=new CustumProgressDialog((Activity) context);
                    dialog.startProgressBar(context.getString(R.string.show_adding));
                    List<String>from=new ArrayList<>();
                    List<String>to=new ArrayList<>();
                    from.add(fromhh);
                    from.add(frommm);
                    from.add(toggleFrom.getText().toString());

                    to.add(tohh);
                    to.add(tomm);
                    to.add(toggleTo.getText().toString());

                    MoveShow moveShow=new MoveShow();
                    moveShow.setName(name);
                    moveShow.setPrice(price);
                    moveShow.setTimeFrom(from);
                    moveShow.setTimeTo(to);



                    //calculate document id
                    String currentTime=new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    String currentDate=new SimpleDateFormat("dd:MM:yy", Locale.getDefault()).format(new Date());
                    final String showId="Movie Show"+currentDate+currentTime;

                    moveShow.setId(showId);
                    moveShow.setUid(mAuth.getUid());
                    moveShow.setHname((String)ApplicationClass.translatedData.get("hname"));

                    db.collection("shows").document(showId)
                            .set(moveShow).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.stopProgressBar();
                            dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.stopProgressBar();
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    });

                }

            }


        });



    }



}
