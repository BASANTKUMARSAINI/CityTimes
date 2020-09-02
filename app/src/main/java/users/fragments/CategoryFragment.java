package users.fragments;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycity.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import adapter.CategoryAdapter;
import adapter.SubCategoryAdapter;
import model.ApplicationClass;
import model.ExpandableHeightGridView;
import sell_and_buy.SellAndBuyActivity;
import users.HomeActivity;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryFragment extends Fragment {
    private int []imageList={R.mipmap.ic_shopping,R.mipmap.ic_food,
            R.mipmap.ic_stay,R.mipmap.beauty,R.mipmap.ic_sports,R.mipmap.movies,R.mipmap.education,R.mipmap.ic_health,
            R.drawable.ic_cyber,R.mipmap.electronics,R.mipmap.travel,R.drawable.ic_workshop,
            R.mipmap.catering,R.mipmap.irentals,R.mipmap.sell_buy,R.mipmap.ic_mistary,R.mipmap.agriculture,
            R.mipmap.ic_factories,R.drawable.ic_ngo,R.drawable.ic_bank,R.drawable.ic_petrol

    };
    private  String[]categorylist;
    ExpandableHeightGridView mAppsGrid;
    RecyclerView recyclerView;
    List<String>list;
    public CategoryFragment() {
    }

    SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ApplicationClass.loadLocale(getContext());
    View view=inflater.inflate(R.layout.fragment_category, container, false);
    categorylist=getResources().getStringArray(R.array.Category);
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
                //Handle click on any category
                if(categorylist[position].equals(getString(R.string.sell_and_buy)))
                {
                   Intent intent=new Intent(getApplicationContext(), SellAndBuyActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent);
                   return;
                }

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
                TextView tvBack=bottomSheetView.findViewById(R.id.tv_back);
                tvBack.setText(R.string.back);
                //ApplicationClass.setTranslatedText(tvCategoryName,categorylist[position]);
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                recyclerView=bottomSheetView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                setSubCategory(position);
                bottomSheetView.setBackground(getContext().getDrawable(R.drawable.bottom_sheet_background));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

    }
    private void setSubCategory(int position) {
        String CATEGEORY=getString(R.string.shopping);
        String []list=getResources().getStringArray(R.array.shopping);
//        switch (categorylist[position])
//        {
//            case  "Shopping":
//                CATEGEORY="Shopping";
//                list=getResources().getStringArray(R.array.shopping);
//                break;
//            case "Parlourl-Saloon":
//                CATEGEORY="Parlourl-Saloon";
//                list=getResources().getStringArray(R.array.parlour_saloon);
//                break;
//            case "Gym-Sports":
//                CATEGEORY="Gym-Sports";
//                list=getResources().getStringArray(R.array.gym_sport);
//                break;
//            case "Food":
//                CATEGEORY="Food";
//                list=getResources().getStringArray(R.array.food);
//                break;
//            case "Electronics":
//                CATEGEORY="Electronics";
//                list=getResources().getStringArray(R.array.electronics);
//                break;
//            case "Health":
//                CATEGEORY="Health";
//                list=getResources().getStringArray(R.array.health);
//                break;
//            case "Travel":
//                CATEGEORY="Shopping";
//                list=getResources().getStringArray(R.array.travel);
//                break;
//            case "Shows and Cinema":
//                CATEGEORY="Shows and Cinema";
//                list=getResources().getStringArray(R.array.shows_cinema);
//                break;
//            case "Education":
//                CATEGEORY="Education";
//                list=getResources().getStringArray(R.array.education);
//                break;
//            case "Rentals":
//                CATEGEORY="Rentals";
//                list=getResources().getStringArray(R.array.rentals);
//                break;
//            case "Sell and Buy":
//                CATEGEORY="Sell and Buy";
//                list=getResources().getStringArray(R.array.sell_buy);
//                break;
//            case "Agriculture":
//                CATEGEORY="Agriculture";
//                list=getResources().getStringArray(R.array.agriculture);
//                break;
//            case "Buliding Material":
//                CATEGEORY="Buliding Material";
//                list=getResources().getStringArray(R.array.bulding);
//                break;
//            case "Cyber":
//                CATEGEORY="Cyber";
//                list=getResources().getStringArray(R.array.cyber);
//                break;
//            case "Stay":
//                CATEGEORY="Stay";
//                list=getResources().getStringArray(R.array.stay);
//                break;
//            case "Booking":
//                CATEGEORY="Booking";
//                list=getResources().getStringArray(R.array.booking);
//                break;
//            case "Factories":
//                CATEGEORY="Factories";
//                list=getResources().getStringArray(R.array.factories);
//                break;
//            case "NGO and Club":
//                CATEGEORY="NGO and Club";
//                list=getResources().getStringArray(R.array.ngo_club);
//                break;
//            case "Bank and Atm":
//                CATEGEORY="Bank and Atm";
//                list=getResources().getStringArray(R.array.ngo_club);
//                break;
//            case "Pentrol Pump":
//                CATEGEORY="Pentrol Pump";
//                list=getResources().getStringArray(R.array.petrol_pump);
//                break;
//        }
        switch (position)
        {
            case  0:
                CATEGEORY="Shopping";
                list=getResources().getStringArray(R.array.shopping);
                break;
            case 1:
                CATEGEORY="Food";
                list=getResources().getStringArray(R.array.food);
break;
            case 2:
                CATEGEORY="Stay";
                list=getResources().getStringArray(R.array.stay);

                break;
            case 3:
                CATEGEORY="Parlourl Saloon";
                list=getResources().getStringArray(R.array.parlour_saloon);
                break;
            case 4:
                CATEGEORY="Gym Sports";
                list=getResources().getStringArray(R.array.gym_sport);

                break;
            case 5:
                CATEGEORY="Shows and Cinema";
                list=getResources().getStringArray(R.array.shows_cinema);

                break;
            case 6:
                CATEGEORY="Education";
                list=getResources().getStringArray(R.array.education);
                break;
            case 7:
                CATEGEORY="Health";
                list=getResources().getStringArray(R.array.health);
                break;
            case 8:
                CATEGEORY="Cyber";
                list=getResources().getStringArray(R.array.cyber);
                break;
            case 9:
                CATEGEORY="Electronics";
                list=getResources().getStringArray(R.array.electronics);
                break;
            case 10:
                CATEGEORY="Travel";
                list=getResources().getStringArray(R.array.travel);

                break;
            case 11:
                CATEGEORY="Vehicles and Workshop";
                list=getResources().getStringArray(R.array.vehicle_workshop);
                break;
            case 12:
                CATEGEORY="Booking";
                list=getResources().getStringArray(R.array.booking);
                break;
            case 13:
                CATEGEORY="Rentals";
                list=getResources().getStringArray(R.array.rentals);

                break;
            case 14:
                CATEGEORY="Sell and Buy";
                list=getResources().getStringArray(R.array.sell_buy);
                break;
            case 15:
                CATEGEORY="Buliding Material";
                list=getResources().getStringArray(R.array.bulding);
                break;
            case 16:
                CATEGEORY="Agriculture";
                list=getResources().getStringArray(R.array.agriculture);
                break;
            case 17:
                CATEGEORY="Factories";
                list=getResources().getStringArray(R.array.factories);
                break;
            case 18:
                CATEGEORY="NGO and Club";
                list=getResources().getStringArray(R.array.ngo_club);
                break;
            case 19:
                CATEGEORY="Banking";
                list=getResources().getStringArray(R.array.banking);
                break;
            case 20:
                CATEGEORY="Pentrol Pump";
                list=getResources().getStringArray(R.array.petrol_pump);
                break;
        }
        SubCategoryAdapter adapter=new SubCategoryAdapter( getApplicationContext(),list,CATEGEORY);
        recyclerView.setAdapter(adapter);
    }

}
