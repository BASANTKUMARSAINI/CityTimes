package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import adapter.SliderAdapterExample;
import adapter.SubCategoryAdapter;
import dialog.SearchDefineDialog;
import dialog.SortStoreDialog;
import model.ApplicationClass;
import model.Seller;
import model.SliderItem;
import users.fragments.StoresFragment;
import view_holder.SellerViewHolder;

import static com.facebook.FacebookSdk.getApplicationContext;

public class StoresActivity extends AppCompatActivity {
    TextView tvStoreType;
    FragmentTransaction transaction;
    Fragment fragment;
    SliderView sliderView;
    SearchView searchView;
    FirebaseFirestore db;
    String subCategory="";
    String category="";
    FirestoreRecyclerAdapter<Seller, SellerViewHolder>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(this);
        setContentView(R.layout.activity_stores);
        tvStoreType=findViewById(R.id.tv_category_store);
        searchView=findViewById(R.id.search_view);

        db=FirebaseFirestore.getInstance();
        sliderView=findViewById(R.id.imageSlider);
        subCategory=getIntent().getStringExtra("subCategory");
        category=getIntent().getStringExtra("category");
        String text=subCategory+" "+getString(R.string.services);

        tvStoreType.setText(text);
        setSliderImage();

        fragment=new StoresFragment(subCategory,category);
        transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            searchView.setIconified(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                StoresFragment.setData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                StoresFragment.setData(newText);
                return true;
            }
        });




    }
    public void defineSearch(View view)
    {
        SearchDefineDialog dialog=new SearchDefineDialog(this);
        dialog.show();
    }
    private void setSliderImage() {
        ApplicationClass.setSliderImage(StoresActivity.this,sliderView,"general");
//        db.collection("images").whereEqualTo("imageType","general").limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                List<SliderItem> list=new ArrayList<>();
//                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
//                {
//                    list.add(documentSnapshot.toObject(SliderItem.class));
//                }
//
//                SliderAdapterExample adapter = new SliderAdapterExample(StoresActivity.this,list);
//
//                sliderView.setSliderAdapter(adapter);
//
//                //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//                sliderView.setIndicatorSelectedColor(Color.WHITE);
//                sliderView.setIndicatorUnselectedColor(Color.GRAY);
//                sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
//                sliderView.startAutoCycle();
//
//            }
//        });
    }
    public void goToBack(View view)
    {
        finish();
    }
    public void sortData(View view)
    {
    SortStoreDialog dialog=new SortStoreDialog(this);
    dialog.show();
}
//    private void enableSearchView(View view,boolean enabled)
//    {
//        view.setEnabled(enabled);
//        if(view instanceof ViewGroup)
//        {
//            ViewGroup viewGroup=(ViewGroup)view;
//            for(int i=0;i<viewGroup.getChildCount();i++)
//            {
//                View child=viewGroup.getChildAt(i);
//                enableSearchView(child,enabled);
//            }
//        }
//    }

}
