package adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;

import users.StoresActivity;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

Context context;
String []list;
String userCity="",userState="",userCountry="";

public SubCategoryAdapter(Context context,String []list,String userCity,String userState,String userCountry)
{
    this.list=list;
    this.context=context;
    this.userCity=userCity;
    this.userState=userState;
    this.userCountry=userCountry;

}
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.store_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tvSubCategory.setText(list[position]);
        holder.tvSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userCity.equals(""))
                {
                Toast.makeText(context,"anter city first",Toast.LENGTH_LONG).show();
                }
               else {
                   Log.v("UCITY",userCity+"baba");
                   Toast.makeText(context,userCity+"baba",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(context, StoresActivity.class);
                    intent.putExtra("ucity",userCity);
                    intent.putExtra("subCategory",list[position]);
                    intent.putExtra("ucountry",userCountry);
                    intent.putExtra("ustate",userState);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
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
