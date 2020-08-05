package update_dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import dialog.CustumProgressDialog;
import interfaces.UpdateInterface;
import model.ApplicationClass;

public class UpdateShopStatus extends Dialog {
    public Context context;
    public ImageView imgYes,imgNo;
     public boolean isYes=true,isNo=false,shopStatus=true;
    private Button btnCancel,btnUpdate;

    boolean  SHOP_STATUS;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public UpdateShopStatus(@NonNull Context context, boolean SHOP_STATUS) {
        super(context);
        this.context=context;
        this.SHOP_STATUS=SHOP_STATUS;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_shop_status);
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
        if(SHOP_STATUS)
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
            shopStatus=false;
        else if(isYes)
            shopStatus=true;

        final HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("shopStatus",shopStatus);
        final CustumProgressDialog dialog=new CustumProgressDialog((Activity)context);
        dialog.startProgressBar(context.getString(R.string.update));
        db.collection("ensellers").document(mAuth.getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                db.collection("hisellers").document(mAuth.getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.stopProgressBar();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
           dismiss();

        }

    }

