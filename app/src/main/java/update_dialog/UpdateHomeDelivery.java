package update_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mycity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import interfaces.UpdateInterface;
import model.ApplicationClass;

public class UpdateHomeDelivery extends Dialog {
    public Context context;
    public ImageView imgYes,imgNo;
     public boolean isYes=true,isNo=false,deliveryStatus=true;
    private Button btnCancel,btnUpdate;
    UpdateInterface updateInterface;

    boolean  DELIVERY_STATUS;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public UpdateHomeDelivery(@NonNull Context context, boolean DELIVERY_STATUS, UpdateInterface updateInterface) {
        super(context);
        this.context=context;
        this.DELIVERY_STATUS=DELIVERY_STATUS;
        this.updateInterface=updateInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_home_delivery);
        imgNo=findViewById(R.id.img_no);
        imgYes=findViewById(R.id.img_yes);


        btnCancel=findViewById(R.id.btn_cancel);
        btnUpdate=findViewById(R.id.btn_update);


        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        setImageInit();

        imgNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNo=!isNo;
                if(isNo)
                {
                    isYes=false;
                    imgYes.setImageResource(R.drawable.ic_right);
                    imgNo.setImageResource(R.drawable.ic_cross_red);
                }
                else
                {
                    isYes=true;
                    imgYes.setImageResource(R.drawable.ic_right_green);
                    imgNo.setImageResource(R.drawable.ic_cross);
                }
            }
        });

        imgYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isYes=!isYes;
                if(isYes)
                {
                    isNo=false;
                    imgYes.setImageResource(R.drawable.ic_right_green);
                    imgNo.setImageResource(R.drawable.ic_cross);
                }
                else
                {
                    isNo=true;
                    imgYes.setImageResource(R.drawable.ic_right);
                    imgNo.setImageResource(R.drawable.ic_cross_red);
                }

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
                updateStoreName();
            }


        });



    }

    private void setImageInit() {
        if(DELIVERY_STATUS)
        {
            imgYes.setImageResource(R.drawable.ic_right_green);
            isYes=true;
            isNo=false;
        }
        else
        {
            imgNo.setImageResource(R.drawable.ic_cross_red);
            isYes=false;
            isNo=true;
        }
    }

    private void updateStoreName() {
        if(isNo)
            deliveryStatus=false;
        else if(isYes)
            deliveryStatus=true;

          updateInterface.update("HOME_DELIVERY",deliveryStatus);
           dismiss();

        }

    }

