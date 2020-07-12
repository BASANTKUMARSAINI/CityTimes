package users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.example.mycity.R;

import users.fragments.HomeFragment;
import users.fragments.StoresFragment;

public class FavouriteStoreActivity extends AppCompatActivity {
    Fragment fragment;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_store);
        fragment=new StoresFragment(2);
        transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
     public void goToBack(View view)
     {
         finish();
     }
}
