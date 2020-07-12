package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;

import interfaces.RecyclerViewInterface;

public class AddressAdapter  extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    Context context;
    String []list;
    RecyclerViewInterface recyclerViewInterface;
    String where="";

    public AddressAdapter(Context context,RecyclerViewInterface recyclerViewInterface,String []list,String  where)
    {
        this.recyclerViewInterface=recyclerViewInterface;
        this.list=list;
        this.context=context;
        this.where=where;

    }
    @NonNull
    @Override
    public AddressAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.store_layout,parent,false);
        return new AddressAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.MyViewHolder holder, final int position) {
        holder.tvSubCategory.setText(list[position]);
        holder.tvSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewInterface.onItemClicked(list[position],where);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvSubCategory;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubCategory=itemView.findViewById(R.id.tv_sub_category_name);

        }
    }
}
