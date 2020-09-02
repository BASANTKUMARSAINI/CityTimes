package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import model.ApplicationClass;
import model.Item;
import model.MoveShow;

public class ItemDialog extends Dialog  {
    public Context context;
    private Button btnCancel,btnAdd;
    EditText etPrice,etName;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public ItemDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_item_dialog);

        btnCancel=findViewById(R.id.btn_cancel);
        btnAdd=findViewById(R.id.btn_add_item);

        etName=findViewById(R.id.et_item_name);
        etPrice=findViewById(R.id.et_item_price);

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

                if(TextUtils.isEmpty(name))
                {
                    Toast.makeText(context,"enter item name",Toast.LENGTH_LONG).show();
                }
                else  if(TextUtils.isEmpty(price))
                {
                    Toast.makeText(context,"enter item price",Toast.LENGTH_LONG).show();
                }
                else
                {
                    ApplicationClass.translatedData=new HashMap<>();
                    ApplicationClass.setTranslatedDataToMap("hname",name);
                    final CustumProgressDialog dialog=new CustumProgressDialog((Activity) context);
                    dialog.startProgressBar(context.getString(R.string.item_adding));

                    final Item item=new Item();
                    item.setName(name);
                    item.setUid(mAuth.getUid());
                    item.setPrice(price);

                    //calculate document id
                    String currentTime=new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    String currentDate=new SimpleDateFormat("dd:MM:yy", Locale.getDefault()).format(new Date());
                    final String itemId="Item:"+currentDate+currentTime;

                    item.setId(itemId);
                    item.setHname((String)ApplicationClass.translatedData.get("hname"));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            db.collection("items").document(itemId)
                                    .set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    },5000);


                }

            }


        });



    }



}
