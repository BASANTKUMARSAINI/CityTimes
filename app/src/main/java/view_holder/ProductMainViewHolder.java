package view_holder;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;

import java.util.List;

public class ProductMainViewHolder extends RecyclerView.ViewHolder {
   public TextView tvProductType;

   public RecyclerView recyclerView_sub;

    Context context;
    public ProductMainViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context=context;
        tvProductType=itemView.findViewById(R.id.tv_product_type);
        recyclerView_sub=itemView.findViewById(R.id.recycler_view);
        recyclerView_sub.setLayoutManager(new LinearLayoutManager(context));
    }
    public void setCollectionType(String type)
    {
        tvProductType.setText(type);
    }


}
