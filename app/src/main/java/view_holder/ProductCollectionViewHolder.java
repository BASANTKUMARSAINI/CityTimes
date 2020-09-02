package view_holder;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;

import java.util.List;

public class ProductCollectionViewHolder extends RecyclerView.ViewHolder {
   public TextView tvCollectionType;
    public LinearLayout renameLayout;
   public RecyclerView recyclerView_sub;
    public LinearLayout addPhotoLayout,uploadLayout;
    public EditText etProductPrice;
    public TextView tvUpload;
    Context context;
    public CardView newImageCard;
    public ImageView newImageView;
    public LinearLayout mainLayout;
    public ImageView imgDeleteCollection;
    public ProductCollectionViewHolder(@NonNull View itemView,Context context) {
        super(itemView);
        this.context=context;
        tvCollectionType=itemView.findViewById(R.id.tv_img_type);
        renameLayout=itemView.findViewById(R.id.rename_layout);
        uploadLayout=itemView.findViewById(R.id.upload_layout);
        newImageCard=itemView.findViewById(R.id.new_image_card_view);
        newImageView=itemView.findViewById(R.id.new_product_image);
        imgDeleteCollection=itemView.findViewById(R.id.img_delete_collection);

        etProductPrice=itemView.findViewById(R.id.et_product_price);
        tvUpload=itemView.findViewById(R.id.tv_upload);
        recyclerView_sub=itemView.findViewById(R.id.recycler_view);
        recyclerView_sub.setLayoutManager(new GridLayoutManager(context,3));

        mainLayout=itemView.findViewById(R.id.main_layout);
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
