package seller.deffrent_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import dialog.ItemDialog;
import model.ApplicationClass;
import model.Item;
import model.MoveShow;
import view_holder.FoodMenuViewHolder;
import view_holder.ShowViewHolder;

public class FoodMenuActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirestoreRecyclerAdapter<Item, FoodMenuViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(this);
        setContentView(R.layout.activity_food_menu);
        recyclerView=findViewById(R.id.recycler_view);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public void goToBack(View view)
    {
        finish();
    }
    public void addMenuItem(View view)
    {
        ItemDialog dialog=new ItemDialog(this);
        dialog.setCancelable(false);
        dialog.show();
    }
    @Override
    protected void onStart() {
        super.onStart();
//        String path=ApplicationClass.LANGUAGE_MODE+"sellers";

        Query query=db.collection("items").whereEqualTo("uid",mAuth.getUid());
        FirestoreRecyclerOptions<Item> options=new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query,Item.class)
                .build();
        adapter=new FirestoreRecyclerAdapter<Item, FoodMenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodMenuViewHolder holder, int position, @NonNull final Item model) {
                 holder.tvItemPrice.setText(model.getPrice());
                 holder.tvItemName.setText(model.getName());
                 if(ApplicationClass.LANGUAGE_MODE.equals("hi"))
                        holder.tvItemName.setText(model.getHname());
                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteShow(model.getId());
                    }
                });

            }

            @NonNull
            @Override
            public FoodMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
                return new FoodMenuViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

    private void deleteShow(String id) {
        db.collection("items").document(id).delete();

    }

}