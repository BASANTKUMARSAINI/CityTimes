package view_holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import users.government.OfficeActivity;

public class OfficeViewHolder extends RecyclerView.ViewHolder {
    public TextView  tvHeadName,tvOfficeType,tvAddress,tvTime,tvDescription,tvPhone;
   // TextView tvSunday,tvMonday,tvTuseday,tvWednesday,tvThrusday,tvFriday,tvSaturday;

   public LinearLayout call;
   public ImageView officeShare,imgeNavigation;
   CircleImageView imgType;
   Context context;
   public CardView cardView;

    public OfficeViewHolder(Context context,@NonNull View view) {
        super(view);
        this.context=context;
        //tvHeadName=view.findViewById(R.id.tv_head_name);
        tvOfficeType=view.findViewById(R.id.tv_office_type);
        tvAddress=view.findViewById(R.id.tv_address);
        tvTime=view.findViewById(R.id.tv_time);
        //tvDescription=view.findViewById(R.id.tv_description);
        //tvPhone=view.findViewById(R.id.tv_phone);
       // call=itemView.findViewById(R.id.call);

//        tvSunday=view.findViewById(R.id.tv_day_sunday);
//        tvMonday=view.findViewById(R.id.tv_day_monday);
//        tvTuseday=view.findViewById(R.id.tv_day_tuesday);
//        tvWednesday=view.findViewById(R.id.tv_day_wednesday);
//        tvThrusday=view.findViewById(R.id.tv_day_thursday);
//        tvFriday=view.findViewById(R.id.tv_day_friday);
//        tvSaturday=view.findViewById(R.id.tv_day_saturday);
//
//        officeShare=view.findViewById(R.id.img_share);
//        imgeNavigation=view.findViewById(R.id.img_navigation);
        imgType=view.findViewById(R.id.img_office_type);
        cardView=view.findViewById(R.id.card_view);

    }
//    public void setDays(HashMap<String, Boolean> days) {
//        tvSunday.setTextColor(context.getResources().getColor(R.color.green));
//        tvMonday.setTextColor(context.getResources().getColor(R.color.green));
//        tvTuseday.setTextColor(context.getResources().getColor(R.color.green));
//        tvWednesday.setTextColor(context.getResources().getColor(R.color.green));
//        tvThrusday.setTextColor(context.getResources().getColor(R.color.green));
//        tvFriday.setTextColor(context.getResources().getColor(R.color.green));
//        tvSaturday.setTextColor(context.getResources().getColor(R.color.green));
//
//        if(!days.get("sunday"))
//            tvSunday.setTextColor(context.getResources().getColor(R.color.red));
//        if(!days.get("monday"))
//            tvMonday.setTextColor(context.getResources().getColor(R.color.red));
//        if(!days.get("tuesday"))
//            tvTuseday.setTextColor(context.getResources().getColor(R.color.red));
//        if(!days.get("wednesday"))
//            tvWednesday.setTextColor(context.getResources().getColor(R.color.red));
//        if(!days.get("thursday"))
//            tvThrusday.setTextColor(context.getResources().getColor(R.color.red));
//        if(!days.get("friday"))
//            tvFriday.setTextColor(context.getResources().getColor(R.color.red));
//        if(!days.get("saturday"))
//            tvSaturday.setTextColor(context.getResources().getColor(R.color.red));
//    }

    public void setIcon(OfficeActivity officeActivity, String officeType) {
        switch (officeType)
        {
            case "Police Station":
                imgType.setImageResource(R.mipmap.ic_police_station);
                break;
            case "Fire Brigade":
                imgType.setImageResource(R.mipmap.ic_fire_brigade);
                break;
            case "Hospital":
                imgType.setImageResource(R.mipmap.ic_hospital);
                break;
            case "College":
                imgType.setImageResource(R.mipmap.ic_college);
                break;
            case "BDO office":
                imgType.setImageResource(R.mipmap.ic_bdo_office);
                break;
            case "Post Office":
                imgType.setImageResource(R.mipmap.ic_post_office);
                break;
            case "BEO office":
                imgType.setImageResource(R.mipmap.ic_beo_office);
                break;
            case "DEO office":
                imgType.setImageResource(R.mipmap.ic_deo_office);
                break;
            case "Panchayat Bhawan":
                imgType.setImageResource(R.mipmap.ic_panchayat_bhawan);
                break;


        }
    }

}
