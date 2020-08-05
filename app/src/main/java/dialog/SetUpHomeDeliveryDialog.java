package dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycity.R;

import authantication.UserLoginActivity;
import model.ApplicationClass;
import seller.SetupProfileActivity;

public class SetUpHomeDeliveryDialog {
    Activity activity;
    AlertDialog dialog;
    public ImageView imgYes,imgNo;
    public Button btnNext;
   public boolean isYes=true,isNo=false,deliveryStatus=true;
    public  SetUpHomeDeliveryDialog(Activity activity)
    {
        this.activity=activity;
    }
    public  void startProgressBar()
    {
        ApplicationClass.loadLocale(activity);
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setCancelable(false);
        final View view=inflater.inflate(R.layout.setup_delivery_service,null);
        imgNo=view.findViewById(R.id.img_no);
        imgYes=view.findViewById(R.id.img_yes);
        btnNext=view.findViewById(R.id.btn_next);

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
         btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNo)
                    deliveryStatus=false;
                else if(isYes)
                    deliveryStatus=true;
                SetupProfileActivity object=new SetupProfileActivity();
                object.setDelivery(deliveryStatus);
                dialog.dismiss();
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
