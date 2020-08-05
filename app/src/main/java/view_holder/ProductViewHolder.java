package view_holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public TextView tvProductTitle,tvProductType,tvSellerCity,tvProductPrice;

  public CardView cardView;

   // public LinearLayout photosLayoutFirsTwo,photosLayoutLastTwo;

   public CircleImageView productImage1;
 //  public ImageView productImage2, productImage3, productImage4, productImage5;
 //  public ImageView productShare;
  // public ImageView productDelete;
    public ProductViewHolder(@NonNull View view) {
        super(view);
        //tvProductDes=view.findViewById(R.id.tv_product_des);
        tvProductTitle=view.findViewById(R.id.tv_product_name);
        tvProductType=view.findViewById(R.id.tv_product_type);
        tvSellerCity=view.findViewById(R.id.tv_seller_city);
        tvProductPrice=view.findViewById(R.id.tv_product_price);
        cardView=view.findViewById(R.id.card_view);
       // tvSellerPhone=view.findViewById(R.id.tv_seller_phone);

//
//        photosLayoutFirsTwo=view.findViewById(R.id.first_two);
//        photosLayoutLastTwo=view.findViewById(R.id.last_two);
//
//        morePhotos=view.findViewById(R.id.tv_more_photos);

        productImage1=view.findViewById(R.id.img_product);
//        productImage2=view.findViewById(R.id.img_product1);
//        productImage3=view.findViewById(R.id.img_product2);
//        productImage4=view.findViewById(R.id.img_product3);
//        productImage5=view.findViewById(R.id.img_product4);
//        productShare=view.findViewById(R.id.img_share);
       // productDelete=view.findViewById(R.id.img_delete_product);
        //call=itemView.findViewById(R.id.call);

    }

}
