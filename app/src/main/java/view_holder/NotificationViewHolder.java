package view_holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTitle,tvMessage;
    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
         tvMessage=itemView.findViewById(R.id.tv_message);
         tvTitle=itemView.findViewById(R.id.tv_notification_title);
    }
}
