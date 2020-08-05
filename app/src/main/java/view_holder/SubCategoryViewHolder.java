package view_holder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;

public class SubCategoryViewHolder extends RecyclerView.ViewHolder
{
    public TextView tvCategoryName,tvSubCategoryName;
    public RelativeLayout categoryView;
    public SubCategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        tvCategoryName=itemView.findViewById(R.id.tv_category);
        tvSubCategoryName=itemView.findViewById(R.id.tv_sub_category);
        categoryView=itemView.findViewById(R.id.category_view);
    }
}
