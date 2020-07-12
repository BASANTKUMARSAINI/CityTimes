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
import seller.SetupProfileActivity;

public class SetupProfileDialog extends Dialog {
  Context context;
    public  SetupProfileDialog(Context context)
    {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setup_profile_layout);
        Log.v("TAG","inininin");
        Button btnLetGo=findViewById(R.id.btn_lets_go);
        btnLetGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SetupProfileActivity.class);
                context.startActivity(intent);
                Activity activity=(Activity)context;
                activity.finish();

            }
        });
    }



}
