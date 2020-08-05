package users.fragments;

import android.content.Intent;
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
import model.ApplicationClass;
import model.ExpandableHeightGridView;
import model.Office;
import users.government.OfficeActivity;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */

public class GovernmentFragment extends Fragment {

    private int []imageList={R.mipmap.ic_police_station,R.mipmap.ic_fire_brigade,
            R.mipmap.ic_hospital,R.mipmap.ic_college,R.mipmap.ic_court,R.mipmap.ic_bdo_office,R.mipmap.ic_post_office,
            R.mipmap.ic_beo_office,R.mipmap.ic_deo_office,R.mipmap.ic_panchayat_bhawan,R.mipmap.ic_beo_office
    };
//    private int []categorylist={R.string.shopping,R.string.food,
//            R.string.gym_sports,R.string.beauty,R.string.electronics,R.string.health,
//            R.string.travel,R.string.movies,R.string.education,R.string.catering,R.string.rentals,
//            R.string.sell_buy,R.string.agriculture,R.string.machanice,R.string.other
//    };
    private  String []categorylist;
    ExpandableHeightGridView mAppsGrid;
    RecyclerView recyclerView;


    FirebaseAuth mAuth;
    FirebaseFirestore db;

    public GovernmentFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ApplicationClass.loadLocale(getContext());
        View view=inflater.inflate(R.layout.fragment_government, container, false);
        categorylist=getResources().getStringArray(R.array.government);
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
                Intent intent=new Intent(getContext(), OfficeActivity.class);
                String CATEGORY=ApplicationClass.getEnglishSubCategory("government_"+position,getContext());
                intent.putExtra("SORT_BY",CATEGORY);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(
//                        getContext(),R.style.AppBottomSheetDialogTheme
//                );
//                View bottomSheetView=LayoutInflater.from(getApplicationContext())
//                        .inflate(
//                                R.layout.layout_bottom_sheet,
//                                (LinearLayout)view.findViewById(R.id.bottom_sheet_contanier)
//                        );
//                TextView tvCategoryName=bottomSheetView.findViewById(R.id.tv_category_name);
//                ImageView btnBack=bottomSheetView.findViewById(R.id.img_downword);
//                tvCategoryName.setText(categorylist[position]);
//                btnBack.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        bottomSheetDialog.dismiss();
//                    }
//                });
//                recyclerView=bottomSheetView.findViewById(R.id.recycler_view);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                setSubCategory(position);
//
//                bottomSheetDialog.setContentView(bottomSheetView);
//                bottomSheetDialog.show();
//            }

            }
        });


    }
//    private void setSubCategory(int position) {
//        String []list=getResources().getStringArray(R.array.police_station);
//        switch (categorylist[position])
//        {
//            case "Police Station":
//                list=getResources().getStringArray(R.array.police_station);
//                break;
//            case "Fire Brigade":
//                list=getResources().getStringArray(R.array.fire_brigade);
//                break;
//            case "Hospital":
//                list=getResources().getStringArray(R.array.hospital);
//                break;
//            case "College":
//                list=getResources().getStringArray(R.array.college);
//                break;
//            case "Court":
//                list=getResources().getStringArray(R.array.court);
//                break;
//            case "BDO office":
//                list=getResources().getStringArray(R.array.bdo_office);
//                break;
//            case "Post Office":
//                list=getResources().getStringArray(R.array.post_office);
//                break;
//            case "BEO office":
//                list=getResources().getStringArray(R.array.beo_office);
//                break;
//            case "DEO office":
//                list=getResources().getStringArray(R.array.deo_office);
//                break;
//            case "Panchayat Bhawan":
//                list=getResources().getStringArray(R.array.panchayat_bhawan);
//                break;
//        }
//        SubCategoryAdapter adapter=new SubCategoryAdapter( getApplicationContext(),list,"Shopping");
//        recyclerView.setAdapter(adapter);
//    }
}
