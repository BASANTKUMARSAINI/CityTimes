package users.different_shops;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.mycity.R;

import model.ApplicationClass;

public class DifferentShopActivity extends AppCompatActivity {
    Fragment fragment;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(this);
        setContentView(R.layout.activity_different_shop);
        transaction=getSupportFragmentManager().beginTransaction();
        //fragment=new PetrolFragment();
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();
    }
}
