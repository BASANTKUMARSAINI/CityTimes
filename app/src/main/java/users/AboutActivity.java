package users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mycity.R;

import users.fragments.GeneralFragment;
import users.fragments.HistoryFragment;
import users.fragments.HomeFragment;
import users.fragments.TourismFragment;

public class AboutActivity extends AppCompatActivity {
    TextView tvGeneral,tvHistory,tvTourism;
    View viewGeneral,viewHistory,viewTourism;
    Fragment fragment;
    FragmentTransaction transaction;
    public final static int GENERAL=1,HISTORY=2,TOURISM=3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        tvGeneral=findViewById(R.id.tv_general);
        tvHistory=findViewById(R.id.tv_history);
        tvTourism=findViewById(R.id.tv_tourism);
        viewGeneral=findViewById(R.id.view_general);
        viewHistory=findViewById(R.id.view_history);
        viewTourism=findViewById(R.id.view_tourism);


        fragment=new GeneralFragment();
        transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        tvGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvGeneral.setTextColor(getResources().getColor(R.color.black));
                viewGeneral.setVisibility(View.VISIBLE);
                tvHistory.setTextColor(getResources().getColor(R.color.light_text_color));
                viewHistory.setVisibility(View.GONE);
                tvTourism.setTextColor(getResources().getColor(R.color.light_text_color));
                viewTourism.setVisibility(View.GONE);
                setFragment(GENERAL);

            }


        });
        tvTourism.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTourism.setTextColor(getResources().getColor(R.color.black));
                viewTourism.setVisibility(View.VISIBLE);
                tvHistory.setTextColor(getResources().getColor(R.color.light_text_color));
                viewHistory.setVisibility(View.GONE);
                tvGeneral.setTextColor(getResources().getColor(R.color.light_text_color));
                viewGeneral.setVisibility(View.GONE);
                setFragment(TOURISM);

            }
        });
        tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvHistory.setTextColor(getResources().getColor(R.color.black));
                viewHistory.setVisibility(View.VISIBLE);
                tvGeneral.setTextColor(getResources().getColor(R.color.light_text_color));
                viewGeneral.setVisibility(View.GONE);
                tvTourism.setTextColor(getResources().getColor(R.color.light_text_color));
                viewTourism.setVisibility(View.GONE);
                setFragment(HISTORY);

            }
        });
    }


    private void setFragment(int index) {
        transaction=getSupportFragmentManager().beginTransaction();
        fragment=new GeneralFragment();
        switch (index)
        {
            case GENERAL:
                fragment=new GeneralFragment();
                break;
            case HISTORY:
                fragment=new HistoryFragment();
                break;
            case TOURISM:
                fragment=new TourismFragment();
                break;


        }
        transaction.replace(R.id.fragment,fragment);
        transaction.addToBackStack(null);
        transaction.commit();


    }
    public void goToBack(View view)
    {
        finish();
    }
}
