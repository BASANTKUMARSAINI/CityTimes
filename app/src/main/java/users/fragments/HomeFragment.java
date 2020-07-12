package users.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import adapter.SliderAdapterExample;
import model.SliderItem;
import users.HomeActivity;
import users.fragments.CategoryFragment;
import users.fragments.GovernmentFragment;

public class HomeFragment extends Fragment {
    String userCity="",userCountry="",userState="";
    Fragment fragment;
    FragmentTransaction transaction;
    Context context;
    TextView tvAllServices,tvGovernment;
    View allServicesView,governmentServices;

    SliderView sliderView,sliderViewBottom;
    FirebaseFirestore db;
    public  HomeFragment()
    {

    }
    public HomeFragment(String userCountry,String userState,String userCity) {
        this.userCountry=userCountry;
        this.userState=userState;
        this.userCity=userCity;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_home, container, false);
       tvAllServices=view.findViewById(R.id.tv_all_services);
       tvGovernment=view.findViewById(R.id.government);
       allServicesView=view.findViewById(R.id.all_services_view);
       db=FirebaseFirestore.getInstance();
       sliderView= view.findViewById(R.id.imageSlider);
       sliderViewBottom=view.findViewById(R.id.image_slider_bottom);

        setSliderImage();
        setSliderImageBottom();
       governmentServices=view.findViewById(R.id.government_view);


       tvGovernment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               allServicesView.setVisibility(View.GONE);
               governmentServices.setVisibility(View.VISIBLE);
               tvAllServices.setTextColor(getResources().getColor(R.color.light_text_color));
               tvGovernment.setTextColor(getResources().getColor(R.color.black));

               setGovernmentServices();


           }
       });
       tvAllServices.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               allServicesView.setVisibility(View.VISIBLE);
               governmentServices.setVisibility(View.GONE);
               tvGovernment.setTextColor(getResources().getColor(R.color.light_text_color));
               tvAllServices.setTextColor(getResources().getColor(R.color.black));
               setAllServics();


           }
       });

        transaction=getChildFragmentManager().beginTransaction();
        fragment=new CategoryFragment("kkk","kkl","aaa");
        transaction.replace(R.id.fragment_category,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        return view;
    }

    private void setSliderImageBottom() {
        db.collection("images").whereEqualTo("imageType","general").limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<SliderItem> list=new ArrayList<>();
                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                {
                    list.add(documentSnapshot.toObject(SliderItem.class));
                }

                SliderAdapterExample adapter = new SliderAdapterExample(getContext(),list);

                sliderViewBottom.setSliderAdapter(adapter);

                //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                sliderViewBottom.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                sliderViewBottom.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                sliderViewBottom.setIndicatorSelectedColor(Color.WHITE);
                sliderViewBottom.setIndicatorUnselectedColor(Color.GRAY);
                sliderViewBottom.setScrollTimeInSec(4); //set scroll delay in seconds :
                sliderViewBottom.startAutoCycle();

            }
        });
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

                SliderAdapterExample adapter = new SliderAdapterExample(getContext(),list);

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
    private void setGovernmentServices() {
        transaction=getChildFragmentManager().beginTransaction();
        fragment=new GovernmentFragment();
        transaction.replace(R.id.fragment_category,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void setAllServics()
    {
        transaction=getChildFragmentManager().beginTransaction();
        fragment=new CategoryFragment();
        transaction.replace(R.id.fragment_category,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
