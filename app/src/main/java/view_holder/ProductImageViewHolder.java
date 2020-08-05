package view_holder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;
import com.squareup.picasso.Picasso;

public class ProductImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView btnDelete,imgProduct;
    public TextView tvProductPrice;

    public ProductImageViewHolder(@NonNull View itemView) {
        super(itemView);
        btnDelete=itemView.findViewById(R.id.btn_delete);
        imgProduct=itemView.findViewById(R.id.img_product);
        tvProductPrice=itemView.findViewById(R.id.tv_product_price);
    }

    public void setProductImage(String productUri)
    {
        Picasso.get().load(productUri).into(imgProduct);
        Log.v("TAG","YES");

    }
}
