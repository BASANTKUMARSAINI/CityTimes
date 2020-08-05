package adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.proto.ConfigPersistence;


import java.util.Locale;

import io.opencensus.resource.Resource;
import model.ApplicationClass;
import users.EnterDetailsActivity;
import users.StoresActivity;
import sell_and_buy.SellAndBuyActivity;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

Context context;
String []list;
FirebaseAuth mAuth;
FirebaseFirestore db;
String CATEGORY;

public SubCategoryAdapter(Context context,String []list,String CATEGORY)
{
    this.list=list;
    this.context=context;
    this.CATEGORY=CATEGORY;

}
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        ApplicationClass.loadLocale(context);
        View view=inflater.inflate(R.layout.store_layout,parent,false);
        mAuth=FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tvSubCategory.setText(list[position]);
        int index=Math.min(CATEGORY.indexOf(' '),CATEGORY.length()-1);
        String firstWord=CATEGORY;
        if(index!=-1)
           firstWord =CATEGORY.substring(0,index);

        final String subCategory= ApplicationClass.getEnglishSubCategory(firstWord.toLowerCase()+"_"+position,context);
        holder.tvSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   db.collection("en"+"users").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                       @Override
                       public void onSuccess(DocumentSnapshot documentSnapshot) {
                           if(documentSnapshot.exists())
                           {
                               Intent intent=new Intent(context, StoresActivity.class);
                               //handle click
                               Log.v("CAT",CATEGORY);
                               Log.v("CAT","k"+subCategory);
                               switch (CATEGORY)
                               {
                                   case "Sell and Buy":
                                       intent=new Intent(context, SellAndBuyActivity.class);
                                       intent.putExtra("subCategory", subCategory);
                                       intent.putExtra("category",CATEGORY);
                                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                       context.startActivity(intent);
                                       break;
                                   default:
                                       intent=new Intent(context, StoresActivity.class);
                                       intent.putExtra("subCategory",subCategory);
                                       intent.putExtra("category",CATEGORY);
                                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                       context.startActivity(intent);

                               }

                           }
                           else
                           {

                               Intent intent=new Intent(context,EnterDetailsActivity.class);
                               intent.putExtra("subCategory",subCategory);
                               intent.putExtra("category",CATEGORY);
                               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                               context.startActivity(intent);
                           }
                       }
                   });


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
