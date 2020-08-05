package view_holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;

import org.w3c.dom.Text;

import java.security.PublicKey;

public class ProductCategoryViewHolder extends RecyclerView.ViewHolder {
    public TextView tvProductCategory;
    public ProductCategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        tvProductCategory=itemView.findViewById(R.id.tv_sub_category_name);
    }
}
