package view_holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;

import users.ShopActivity;

public class SellerViewHolder extends RecyclerView.ViewHolder {
    private TextView tvShopName,tvShopAddress,tvStar,tvRatings,tvDistance,tvShopStatus,tvDeliveryStatus;
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
    public void setDistance()
    {

    }
    public void setShopName(String shopName)
    {
        tvShopName.setText(shopName);
    }
    public void setShopAddress(String shopAddress)
    {
        tvShopAddress.setText(shopAddress);
    }
    public void setStar(Integer star,Integer ratings)
    {
        double rate=0;
        if(ratings!=0)
            rate=star/ratings;

        tvStar.setText(rate+"");
    }
    public void setRatings(Integer ratings)
    {
        tvRatings.setText(ratings+"Ratings");
    }
    public void setShopStatus(boolean isOpen) {

        if (isOpen) {
            tvShopStatus.setBackgroundResource(R.drawable.small_green_background);
            tvShopStatus.setTextColor(context.getResources().getColor(R.color.green));
            tvShopStatus.setText("open");

        } else {

            tvShopStatus.setBackgroundResource(R.drawable.small_red_background);
            tvShopStatus.setTextColor(context.getResources().getColor(R.color.red));
            tvShopStatus.setText("closed");


        }
    }
    public void setDeliveryStatus(boolean isDelivery) {

        if (isDelivery) {
            tvDeliveryStatus.setBackgroundResource(R.drawable.small_green_background);
            tvDeliveryStatus.setTextColor(context.getResources().getColor(R.color.green));
            tvDeliveryStatus.setText("delivery");

        } else {

            tvDeliveryStatus.setBackgroundResource(R.drawable.small_red_background);
            tvDeliveryStatus.setTextColor(context.getResources().getColor(R.color.red));
            tvDeliveryStatus.setText("no delivery");


        }
    }
    public void setItemOnClicked(String sUid)
    {
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("sUid",sUid);
        context.startActivity(intent);
    }

}
