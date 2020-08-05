package sell_and_buy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import adapter.SliderAdapterExample;
import dialog.SearchProductDialog;
import model.ApplicationClass;
import model.SliderItem;
import sell_and_buy.fragments.BuyProductFragment;
import sell_and_buy.fragments.SearchProductFragment;
import sell_and_buy.fragments.SellProductFragment;
import users.SearchCategoriesActivity;
import users.StoresActivity;

public class SellAndBuyActivity extends AppCompatActivity {
FirebaseFirestore db;
FirebaseAuth mAuth;
Fragment fragment;
FragmentTransaction transaction;
androidx.appcompat.widget.SearchView searchView;
SliderView sliderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(SellAndBuyActivity.this);
        setContentView(R.layout.activity_sell_and_buy);
        db=FirebaseFirestore.getInstance();
        sliderView= findViewById(R.id.imageSlider);
        setSliderImage();
        transaction=getSupportFragmentManager().beginTransaction();
        fragment=new BuyProductFragment();
        transaction.replace(R.id.sub_fragment,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        searchView=findViewById(R.id.search_view);
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                enableSearchView(searchView,true);
//               // Intent intent=new Intent(SellAndBuyActivity.this, SearchCategoriesActivity.class);
//                EditText editText=(EditText)searchView.findViewById(R.id.search_src_text);
//                editText.setEnabled(true);
//                //startActivityForResult(intent,1);
//
//            }
//        });
//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                enableSearchView(searchView,true);
////                Intent intent=new Intent(SellAndBuyActivity.this,SearchCategoriesActivity.class);
//                EditText editText=(EditText)searchView.findViewById(R.id.search_src_text);
//                editText.setEnabled(true);
//                //startActivityForResult(intent,1);
//            }
//        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager manager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                EditText editText=(EditText)searchView.findViewById(R.id.search_src_text);
              // editText.setEnabled(true);
                manager.showSoftInput(editText,0);

            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction=getSupportFragmentManager().beginTransaction();
                fragment=new SearchProductFragment();
                transaction.replace(R.id.sub_fragment,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                transaction=getSupportFragmentManager().beginTransaction();
                fragment=new SearchProductFragment(query);
                transaction.replace(R.id.sub_fragment,fragment);
                transaction.commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                transaction=getSupportFragmentManager().beginTransaction();
                fragment=new SearchProductFragment(newText);
                transaction.replace(R.id.sub_fragment,fragment);
                transaction.commit();
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }




//    private void enableSearchView(View view, boolean enabled)
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
    public void sellProduct(View view)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new SellProductFragment()).addToBackStack(null).commit();
    }
    public void defineSearch(View view)
    {
        SearchProductDialog dialog=new SearchProductDialog(this);
        dialog.setCancelable(false);
        dialog.show();
    }
    public void goToBack(View view)
    {
        finish();
    }
    private void setSliderImage() {
        ApplicationClass.setSliderImage(SellAndBuyActivity.this,sliderView,"general");
//        db.collection("images").whereEqualTo("imageType","general").limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                List<SliderItem> list=new ArrayList<>();
//                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
//                {
//                    list.add(documentSnapshot.toObject(SliderItem.class));
//                }
//
//                SliderAdapterExample adapter = new SliderAdapterExample(SellAndBuyActivity.this,list);
//
//                sliderView.setSliderAdapter(adapter);
//
//                //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//                sliderView.setIndicatorSelectedColor(Color.WHITE);
//                sliderView.setIndicatorUnselectedColor(Color.GRAY);
//                sliderView.setScrollTimeInSec(2); //set scroll delay in seconds :
//                sliderView.startAutoCycle();
//
//            }
//        });
    }
    public void myProduct(View view)
    {
        Intent intent=new Intent(SellAndBuyActivity.this,MyProductActivity.class);
        startActivity(intent);
    }
}
