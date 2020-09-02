package sell_and_buy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.ApplicationClass;
import model.Product;
import sell_and_buy.fragments.SearchProductFragment;
import sell_and_buy.fragments.SpecificSearchFragment;
import users.ShopActivity;
import view_holder.ProductViewHolder;

import static android.view.View.GONE;

public class ProductActivity extends AppCompatActivity {
    TextView tvProductType;//,tvSellingProduct;

    String TYPE="";
    RelativeLayout relativeLayout;

    androidx.appcompat.widget.SearchView searchView;
    Fragment fragment;
    FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(ProductActivity.this);
        setContentView(R.layout.activity_product);
        tvProductType=findViewById(R.id.tv_product_type);
        //tvSellingProduct=findViewById(R.id.tv_selling_product);
        searchView=findViewById(R.id.search_view);

        TYPE=getIntent().getStringExtra("type");
        tvProductType.setText(TYPE);
        relativeLayout=findViewById(R.id.relative_layout);
        //tvSellingProduct.setText(TYPE+getString(R.string.for_selling));
relativeLayout.setVisibility(View.VISIBLE);
        fragment=new SpecificSearchFragment(TYPE);
        transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        if(!getSupportFragmentManager().getFragments().isEmpty())
            transaction.addToBackStack(null);
        transaction.commit();
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(GONE);

                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s)) {

                    transaction = getSupportFragmentManager().beginTransaction();
                    fragment = new SpecificSearchFragment(TYPE,s);
                    transaction.replace(R.id.fragment, fragment);
                    transaction.commit();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s)) {
                    transaction = getSupportFragmentManager().beginTransaction();
                    fragment = new SpecificSearchFragment(TYPE,s);
                    transaction.replace(R.id.fragment, fragment);
                    transaction.commit();
                    return true;
                }
                return false;
            }
        });

    }


    public  void goToBack(View view)
    {
        finish();
    }
}
