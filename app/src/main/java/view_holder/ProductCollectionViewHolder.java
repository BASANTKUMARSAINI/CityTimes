package view_holder;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;

import java.util.List;

public class ProductCollectionViewHolder extends RecyclerView.ViewHolder {
   public TextView tvCollectionType;
    public LinearLayout renameLayout;
   public RecyclerView recyclerView_sub;
    public LinearLayout addPhotoLayout;
    Context context;
    public ProductCollectionViewHolder(@NonNull View itemView,Context context) {
        super(itemView);
        this.context=context;
        tvCollectionType=itemView.findViewById(R.id.tv_img_type);
        renameLayout=itemView.findViewById(R.id.rename_layout);
        recyclerView_sub=itemView.findViewById(R.id.recycler_view);
        recyclerView_sub.setLayoutManager(new GridLayoutManager(context,3));


        addPhotoLayout=itemView.findViewById(R.id.add_layout);
    }
    public void setCollectionType(String type)
    {
        tvCollectionType.setText(type);
    }

    public void setPhotos(List<String>photosList)
    {

    }

}
