package dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.mycity.R;

import authantication.UserLoginActivity;
import model.ApplicationClass;
import seller.SetupProfileActivity;

public class SetupProfileDialog extends Dialog {
  Context context;
  String CATEGORY,SUB_CATEGORY;
    Button btnLetGo;
    public  SetupProfileDialog(Context context,String CATEGORY,String SUB_CATEGORY)
    {
        super(context);
        this.context=context;
        this.CATEGORY=CATEGORY;
        this.SUB_CATEGORY=SUB_CATEGORY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setup_profile_layout);
        Log.v("TAG","inininin");
        btnLetGo=findViewById(R.id.btn_lets_go);
        btnLetGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle where we should go
                Intent intent=new Intent(context, SetupProfileActivity.class);
                Activity activity=(Activity)context;
                switch (CATEGORY)
                {
                    case "Education":
                         intent=new Intent(context, SetupProfileActivity.class);
                        intent.putExtra("category",CATEGORY);
                        intent.putExtra("subCategory",SUB_CATEGORY);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        activity.finish();
                        break;
                    default:
                        intent=new Intent(context, SetupProfileActivity.class);
                        intent.putExtra("category",CATEGORY);
                        intent.putExtra("subCategory",SUB_CATEGORY);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        activity.finish();

                }



            }
        });
    }



}
