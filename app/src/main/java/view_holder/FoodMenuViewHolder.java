package view_holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;

public class FoodMenuViewHolder extends RecyclerView.ViewHolder {

    public TextView tvItemPrice,tvItemName;
    public ImageView  btnDelete;
    public FoodMenuViewHolder(@NonNull View itemView) {
        super(itemView);

        tvItemName=itemView.findViewById(R.id.tv_item_name);
        tvItemPrice=itemView.findViewById(R.id.tv_item_price);
        btnDelete=itemView.findViewById(R.id.img_delete);
    }
}
