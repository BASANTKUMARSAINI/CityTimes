package sell_and_buy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

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
Fragment fragment;
TextView tvProductSelling;
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
        tvProductSelling=findViewById(R.id.tv_selling_product);
        tvProductSelling.setText(R.string.products_for_selling);
        setSliderImage();
        transaction=getSupportFragmentManager().beginTransaction();
        fragment=new BuyProductFragment();
        transaction.replace(R.id.sub_fragment,fragment);

        if(!getSupportFragmentManager().getFragments().isEmpty())
            transaction.addToBackStack(null);
        transaction.commit();
        searchView=findViewById(R.id.search_view);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchView.setIconified(false);
                tvProductSelling.setText(R.string.all_products);
                transaction = getSupportFragmentManager().beginTransaction();
                fragment = new SearchProductFragment();
                transaction.replace(R.id.sub_fragment, fragment);
                transaction.commit();
            }
        });
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query)) {

                    transaction = getSupportFragmentManager().beginTransaction();
                    fragment = new SearchProductFragment(query);
                    transaction.replace(R.id.sub_fragment, fragment);
                    transaction.commit();
                    return true;
                }
               return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText)) {
                    transaction = getSupportFragmentManager().beginTransaction();
                    fragment = new SearchProductFragment(newText);
                    transaction.replace(R.id.sub_fragment, fragment);
                    transaction.commit();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

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
    }
    public void myProduct(View view)
    {
        Intent intent=new Intent(SellAndBuyActivity.this,MyProductActivity.class);
        startActivity(intent);
    }
}
