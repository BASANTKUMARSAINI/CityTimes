package view_holder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import model.ApplicationClass;
import users.ShopActivity;

import static android.view.View.GONE;

public class SellerViewHolder extends RecyclerView.ViewHolder {
    public TextView tvShopName,tvShopAddress,tvStar,tvRatings,tvDistance,tvShopStatus,tvDeliveryStatus;
    Context context;

    public CardView cardView;
    public SellerViewHolder(@NonNull View itemView,Context context) {
        super(itemView);
        this.context=context;
        tvDeliveryStatus=itemView.findViewById(R.id.tv_delivery_status);
        tvShopName=itemView.findViewById(R.id.tv_shop_name);
        tvShopStatus=itemView.findViewById(R.id.tv_shop_status);
        tvShopAddress=itemView.findViewById(R.id.tv_shop_address);
        tvStar=itemView.findViewById(R.id.tv_shop_rating);
        tvRatings=itemView.findViewById(R.id.tv_number_of_ratings);
        tvDistance=itemView.findViewById(R.id.tv_distance);
        cardView=itemView.findViewById(R.id.card_view);


    }
    public double setDistance(Double storeLatitude,Double storeLongitude,String category)
    {
            if(ApplicationClass.USER_LOGITUDE==0.0||ApplicationClass.USER_LATITUDE==0.0)
            {
                tvDistance.setText("");
                return 0.0;
            }
            double lon1=Math.toRadians(ApplicationClass.USER_LOGITUDE);
            double lon2=Math.toRadians(storeLongitude);
            double lat1=Math.toRadians(ApplicationClass.USER_LATITUDE);
            double lat2=Math.toRadians(storeLatitude);
            double dlon=lon2-lon1;
            double dlat=lat2-lat1;

            double a=Math.pow(Math.sin(dlat/2),2)+Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin(dlon/2),2);
            double c=2*Math.asin(Math.sqrt(a));
            double r=6371;
            double distance=c*r;
        Log.v("TAG","dis"+distance);
            String dis=String.format("%.2f",distance);
            tvDistance.setText(dis+context.getString(R.string.km));
            return distance;


    }

    public void setShopName(String shopName,String ownerName,String category)
    {
        if(shopName!=null)
        {

            //ApplicationClass.setTranslatedText(tvShopName,shopName);
tvShopName.setText(shopName);
        }
        else
        {


            //ApplicationClass.setTranslatedText(tvShopName,ownerName);
tvShopName.setText(ownerName);
        }
    }
    public void setShopAddress(String shopAddress,String category)
    {
        tvShopAddress.setText(shopAddress);
    }
    public void setStar(double star,Integer ratings,String category)
    {
        double rate=0;
        if(ratings!=0)
            rate=star/ratings;

        tvStar.setText(rate+"");
    }
    public void setRatings(Integer ratings,String category)
    {
        tvRatings.setText(ratings+context.getString(R.string.rating));
    }
    public void setShopStatus(boolean isOpen) {
//        Calendar calendar=Calendar.getInstance();
//        String today=calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.getDefault()).toLowerCase();
//        Log.v("DAY",today);
//        boolean shopStatus=days.get(today);
        if(isOpen)
        {
            tvShopStatus.setBackgroundResource(R.drawable.small_green_background);
            tvShopStatus.setTextColor(context.getResources().getColor(R.color.green));
            tvShopStatus.setText(R.string.open);
        }
        else
        {
            tvShopStatus.setBackgroundResource(R.drawable.small_red_background);
            tvShopStatus.setTextColor(context.getResources().getColor(R.color.red));
            tvShopStatus.setText(R.string.close);
        }


    }
    public void setDeliveryStatus(boolean isDelivery,String category) {
        if(category.equals("Shopping")||category.equals("Food"))
        {
            if (isDelivery) {
                tvDeliveryStatus.setBackgroundResource(R.drawable.small_green_background);
                tvDeliveryStatus.setTextColor(context.getResources().getColor(R.color.green));
                tvDeliveryStatus.setText(R.string.delivery);

            } else {

                tvDeliveryStatus.setBackgroundResource(R.drawable.small_red_background);
                tvDeliveryStatus.setTextColor(context.getResources().getColor(R.color.red));
                tvDeliveryStatus.setText(R.string.no_delivery);


            }
        }
        else
        {
            tvDeliveryStatus.setVisibility(GONE);
        }
    }
    public void setItemOnClicked(String sUid,String category)
    {
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("sUid",sUid);
        context.startActivity(intent);
    }

}
