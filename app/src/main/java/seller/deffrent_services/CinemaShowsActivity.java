package seller.deffrent_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import dialog.ShowDialog;
import model.ApplicationClass;
import model.MoveShow;
import view_holder.ShowViewHolder;


public class CinemaShowsActivity extends AppCompatActivity {
RecyclerView recyclerView;
FirebaseAuth mAuth;
FirebaseFirestore db;
FirestoreRecyclerAdapter<MoveShow, ShowViewHolder>adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(this);
        setContentView(R.layout.activity_cinema_shows);
        recyclerView=findViewById(R.id.recycler_view);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
//        String path=ApplicationClass.LANGUAGE_MODE+"sellers";

        Query query=db.collection("shows").whereEqualTo("uid",mAuth.getUid());
        FirestoreRecyclerOptions<MoveShow>options=new FirestoreRecyclerOptions.Builder<MoveShow>()
                .setQuery(query,MoveShow.class)
                .build();
        adapter=new FirestoreRecyclerAdapter<MoveShow, ShowViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ShowViewHolder holder, int position, @NonNull final MoveShow model) {
                holder.tvPrice.setText(getString(R.string.rs)+model.getPrice());
                //ApplicationClass.setTranslatedText(holder.tvMovieName,model.getName());

                holder.tvMovieName.setText(model.getName());
                if(ApplicationClass.LANGUAGE_MODE.equals("hi"))
                    holder.tvMovieName.setText(model.getHname());
                List<String> timeListFrom=model.getTimeFrom();
                List<String> timeListTo=model.getTimeTo();

                final String timeFrom=getString(R.string.from)+":"+timeListFrom.get(0)+":"+timeListFrom.get(1)+timeListFrom.get(2);
                final String timeTo=getString(R.string.to)+":"+timeListTo.get(0)+":"+timeListTo.get(1)+timeListTo.get(2);
                holder.tvTimeTo.setText(timeTo);
                holder.tvTimeFrom.setText(timeFrom);
                holder.imgShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Intent.ACTION_SEND);
                        String text="Movie:"+model.getName()+"\n"+
                                    "Price:"+model.getPrice()+"\n"+
                                    timeFrom+"\n"+timeTo;
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT,text);
                        startActivity(intent);

                    }
                });
                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteShow(model.getId());
                    }
                });

            }

            @NonNull
            @Override
            public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.show_layout,parent,false);
                return new ShowViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    private void deleteShow(final String id) {
        AlertDialog.Builder builder=new AlertDialog.Builder(CinemaShowsActivity.this);
        builder.setMessage(R.string.do_you_want_delete_this_show);
        builder.setTitle(R.string.delete_show);
        builder.setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db.collection("shows").document(id)
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
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
    public void addShow(View view)
    {
        ShowDialog dialog=new ShowDialog(this);
        dialog.setCancelable(false);
        dialog.show();
    }
}
