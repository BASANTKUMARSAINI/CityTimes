package users.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mycity.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import adapter.CategoryAdapter;
import adapter.SubCategoryAdapter;
import model.ExpandableHeightGridView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    private int []imageList={R.mipmap.ic_shopping,R.mipmap.ic_food,
            R.mipmap.ic_sports,R.mipmap.beauty,R.mipmap.electronics,R.mipmap.ic_health,R.mipmap.travel,
            R.mipmap.movies,R.mipmap.education,R.mipmap.catering,R.mipmap.irentals,R.mipmap.sell_buy,
            R.mipmap.agriculture,R.mipmap.ic_mistary,R.mipmap.barbar

    };
    private int []categorylist={R.string.shopping,R.string.food,
            R.string.gym_sports,R.string.beauty,R.string.electronics,R.string.health,
            R.string.travel,R.string.movies,R.string.education,R.string.catering,R.string.rentals,
            R.string.sell_buy,R.string.agriculture,R.string.machanice,R.string.other
    };
    ExpandableHeightGridView mAppsGrid;
    RecyclerView recyclerView;
    String userCity="",userCountry="",userState="";

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    public CategoryFragment() {

    }
    public  CategoryFragment(String userCountry,String userState,String userCity) {
        this.userCountry=userCountry;
        this.userState=userState;
        this.userCity=userCity;
        Log.v("TAG","cate");

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view=inflater.inflate(R.layout.fragment_category, container, false);

        mAppsGrid =  view.findViewById(R.id.myId);
        mAppsGrid.setExpanded(true);
        setCategoryInGridView();
        return view;
    }
    private void setCategoryInGridView() {
        mAppsGrid.setVisibility(View.VISIBLE);
        CategoryAdapter adapter=new CategoryAdapter(getApplicationContext(),imageList,categorylist);
        mAppsGrid.setAdapter(adapter);
        mAppsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(
                        getContext(),R.style.AppBottomSheetDialogTheme
                );
                View bottomSheetView=LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.layout_bottom_sheet,
                                (LinearLayout)view.findViewById(R.id.bottom_sheet_contanier)
                        );
                TextView tvCategoryName=bottomSheetView.findViewById(R.id.tv_category_name);
                ImageView btnBack=bottomSheetView.findViewById(R.id.img_downword);
                tvCategoryName.setText(categorylist[position]);
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                recyclerView=bottomSheetView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                setSubCategory(position);

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

    }
    private void setSubCategory(int position) {
        String []list=getResources().getStringArray(R.array.shopping);
        switch (categorylist[position])
        {
            case R.string.shopping:
                list=getResources().getStringArray(R.array.shopping);
                break;
            case R.string.beauty:
                list=getResources().getStringArray(R.array.beauty);
                break;
            case R.string.gym_sports:
                list=getResources().getStringArray(R.array.gym_sport);
                break;
            case R.string.food:
                list=getResources().getStringArray(R.array.food);
                break;
            case R.string.electronics:
                list=getResources().getStringArray(R.array.electronics);
                break;
            case R.string.health:
                list=getResources().getStringArray(R.array.health);
                break;
            case R.string.travel:
                list=getResources().getStringArray(R.array.travel);
                break;
            case R.string.movies:
                list=getResources().getStringArray(R.array.movies);
                break;
            case R.string.education:
                list=getResources().getStringArray(R.array.education);
                break;
            case R.string.catering:
                list=getResources().getStringArray(R.array.catering);
                break;
            case R.string.rentals:
                list=getResources().getStringArray(R.array.rentals);
                break;
            case R.string.sell_buy:
                list=getResources().getStringArray(R.array.sell_buy);
                break;
            case R.string.agriculture:
                list=getResources().getStringArray(R.array.agriculture);
                break;
            case R.string.machanice:
                list=getResources().getStringArray(R.array.machanice);
                break;
            case R.string.other:
                list=getResources().getStringArray(R.array.other);
                break;

        }
        SubCategoryAdapter adapter=new SubCategoryAdapter( getApplicationContext(),list,userCity,userState,userCountry);
        recyclerView.setAdapter(adapter);
    }
}
