package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;

import model.ApplicationClass;
import model.SubCategoryClass;
import view_holder.SubCategoryViewHolder;

public class SearchCategoriesActivity extends AppCompatActivity {
SearchView searchView;
FirebaseFirestore db;
FirebaseAuth mAuth;
FirestoreRecyclerAdapter<SubCategoryClass, SubCategoryViewHolder>adapter;
RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(this);
        setContentView(R.layout.activity_search_categories);
        searchView=findViewById(R.id.search_view);
        ImageView imageView =(ImageView) searchView.findViewById(R.id.search_button);
        imageView.performClick();
        db= FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchData(null);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchData(newText);
                return true;
            }
        });
    }
public void searchData(String text)
{
    Query query=db.collection("categories");
    if(text!=null) {
        SharedPreferences sharedPreferences=getSharedPreferences("MyData",MODE_PRIVATE);

        query = db.collection("categories")
                .orderBy(sharedPreferences.getString("categorySearchBy","subCategoryName"))
                .startAt(text).endAt(text + "\uf8ff");
    }


    FirestoreRecyclerOptions<SubCategoryClass>options=new FirestoreRecyclerOptions.Builder<SubCategoryClass>()
            .setQuery(query,SubCategoryClass.class)
            .build();
    adapter=new FirestoreRecyclerAdapter<SubCategoryClass, SubCategoryViewHolder>(options) {
        @Override
        protected void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position, @NonNull final SubCategoryClass model) {

            holder.tvCategoryName.setText(model.getCategoryName());
           holder.tvSubCategoryName.setText(model.getSubCategoryName());
           if(ApplicationClass.LANGUAGE_MODE.equals("hi"))
           {
               holder.tvCategoryName.setText(model.getHicategoryName());
               holder.tvSubCategoryName.setText(model.getHisubCategoryName());
           }
            //ApplicationClass.setTranslatedText(holder.tvCategoryName,model.getCategoryName());
            //ApplicationClass.setTranslatedText(holder.tvSubCategoryName,model.getSubCategoryName());
            holder.categoryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("en"+"users").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists())
                            {
                                Intent intent=new Intent(SearchCategoriesActivity.this,StoresActivity.class);
                                intent.putExtra("subCategory",model.getSubCategoryName());//in english

                                intent.putExtra("category",model.getCategoryName());//in english
                                startActivity(intent);
                            }
                            else{
                                Intent intent=new Intent(SearchCategoriesActivity.this,EnterDetailsActivity.class);
                                intent.putExtra("subCategory",model.getSubCategoryName());
                                intent.putExtra("category",model.getCategoryName());
                                startActivity(intent);

                            }
                        }
                    });

                }
            });
        }

        @NonNull
        @Override
        public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_search_layout,parent,false);
            return new SubCategoryViewHolder(view);
        }
    };
    recyclerView.setAdapter(adapter);
    adapter.startListening();
    adapter.notifyDataSetChanged();

}
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Query query=db.collection("categories");
//        FirestoreRecyclerOptions<SubCategoryClass>options=new FirestoreRecyclerOptions.Builder<SubCategoryClass>()
//                                                        .setQuery(query,SubCategoryClass.class)
//                                                        .build();
//        adapter=new FirestoreRecyclerAdapter<SubCategoryClass, SubCategoryViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position, @NonNull SubCategoryClass model) {
//                holder.tvCategoryName.setText(model.getCategoryName());
//                holder.tvSubCategoryName.setText(model.getSubCategoryName());
//            }
//
//            @NonNull
//            @Override
//            public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_search_layout,parent,false);
//                return new SubCategoryViewHolder(view);
//            }
//        };
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//       adapter.notifyDataSetChanged();
//
//
//    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }
}
