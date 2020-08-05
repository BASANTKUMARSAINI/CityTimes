package view_holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;

public class ShowViewHolder extends RecyclerView.ViewHolder {

    public TextView tvPrice,tvTimeFrom,tvTimeTo,tvMovieName;
    public ImageView imgShare,btnDelete;
    public ShowViewHolder(@NonNull View itemView) {
        super(itemView);
        tvMovieName=itemView.findViewById(R.id.tv_movie_name);
        tvPrice=itemView.findViewById(R.id.tv_price);
        tvTimeFrom=itemView.findViewById(R.id.tv_time_from);
        tvTimeTo=itemView.findViewById(R.id.tv_time_to);
        imgShare=itemView.findViewById(R.id.img_share);
        btnDelete=itemView.findViewById(R.id.btn_delete);
    }
}
