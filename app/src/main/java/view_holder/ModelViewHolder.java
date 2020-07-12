package view_holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;

public class ModelViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgUser;
    public ModelViewHolder(@NonNull View itemView) {
        super(itemView);
        imgUser=itemView.findViewById(R.id.img_user);
    }
}
