package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import adapter.SliderAdapterExample;
import adapter.SubCategoryAdapter;
import model.Seller;
import model.SliderItem;
import users.fragments.StoresFragment;
import view_holder.SellerViewHolder;

public class StoresActivity extends AppCompatActivity {
    TextView tvStoreType;

    FragmentTransaction transaction;
    Fragment fragment;
SliderView sliderView;

//    RecyclerView recyclerView;
    FirebaseFirestore db;
    String userCity="";
    String subCategory="";
    String userState="";
    String userCountry="";
    FirestoreRecyclerAdapter<Seller, SellerViewHolder>adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);

        tvStoreType=findViewById(R.id.tv_category_store);
//        recyclerView= findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(StoresActivity.this));

        db=FirebaseFirestore.getInstance();
        sliderView=findViewById(R.id.imageSlider);
        userCity=getIntent().getStringExtra("ucity");
        subCategory=getIntent().getStringExtra("subCategory");
        userState=getIntent().getStringExtra("ustate");
        userCountry=getIntent().getStringExtra("ucountry");

        tvStoreType.setText(subCategory+" Stores");
        setSliderImage();

        fragment=new StoresFragment(1,userCountry,userState,userCity,subCategory);
        transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment,fragment);

        transaction.commit();




    }
    private void setSliderImage() {
        db.collection("images").whereEqualTo("imageType","general").limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<SliderItem> list=new ArrayList<>();
                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                {
                    list.add(documentSnapshot.toObject(SliderItem.class));
                }

                SliderAdapterExample adapter = new SliderAdapterExample(StoresActivity.this,list);

                sliderView.setSliderAdapter(adapter);

                //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                sliderView.setIndicatorSelectedColor(Color.WHITE);
                sliderView.setIndicatorUnselectedColor(Color.GRAY);
                sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                sliderView.startAutoCycle();

            }
        });
    }

    public void goToBack(View view)
    {
        finish();
    }


}
