package com.example.mycity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import model.Notification;
import view_holder.NotificationViewHolder;

public class NotificationActivity extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter<Notification, NotificationViewHolder>adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        db=FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query=db.collection("notifications");
        FirestoreRecyclerOptions<Notification>options=new FirestoreRecyclerOptions.Builder<Notification>()
                                                        .setQuery(query,Notification.class)
                                                        .build();
        adapter = new FirestoreRecyclerAdapter<Notification, NotificationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull Notification model) {
                holder.tvTitle.setText(model.getTitle());
                holder.tvMessage.setText(model.getMessage());

            }

            @NonNull
            @Override
            public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout,parent,false);
                return new NotificationViewHolder(view);
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
    public void goToBack(View view)
    {
        finish();
    }
}