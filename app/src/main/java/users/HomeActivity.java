package users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycity.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import adapter.SliderAdapterExample;
import authantication.UserLoginActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import model.Seller;
import model.SliderItem;
import model.User;
import seller.RegisterStoreActivity;
import seller.SetupProfileActivity;
import seller.StoreActivity;
import users.fragments.FavouritesFragment;
import users.fragments.HomeFragment;

public class HomeActivity extends AppCompatActivity {

    TextView tvNavStoreName,tvCityName;
    CircleImageView imgNavUserImage;

    ImageView imgDrawerOpen;
    String userCity="",userCountry="",userState="";


    FragmentTransaction transaction;
    Fragment fragment;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DatabaseReference mDataRef;
    boolean TAG=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       tvCityName=findViewById(R.id.tv_city_name);
       imgDrawerOpen=findViewById(R.id.btn_three_line);



        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        tvNavStoreName=headerView.findViewById(R.id.nav_store_name);


        imgNavUserImage=headerView.findViewById(R.id.nav_img_user);


        imgDrawerOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mDataRef= FirebaseDatabase.getInstance().getReference();

        setUserDataInNavHeader();
        getUserDatas();

        transaction=getSupportFragmentManager().beginTransaction();
        fragment=new HomeFragment(userCountry,userState,userState);
        transaction.replace(R.id.fragment,fragment);

        transaction.commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                fragment=new HomeFragment(userCountry,userState,userCity);
                switch (item.getItemId())
                {
                    case R.id.nav_home:
                    fragment=new HomeFragment(userCountry,userState,userCity);
                    break;
                    case R.id.nav_store:
                        Intent intent=new Intent(HomeActivity.this, StoreActivity.class);
                        startActivity(intent);

                        break;
                   case  R.id.nav_about:
                       Intent intent3=new Intent(HomeActivity.this, AboutActivity.class);
                       startActivity(intent3);
                    break;
                    case R.id.nav_favorites:
                    Intent intent4=new Intent(HomeActivity.this,FavouriteStoreActivity.class);
                    startActivity(intent4);
                    break;
                    case R.id.nav_settings:
                         Intent intent2=new Intent(HomeActivity.this,SettingsActivity.class);
                         startActivity(intent2);
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        if(LoginManager.getInstance()!=null)
                            LoginManager.getInstance().logOut();
                        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
                        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(HomeActivity.this,gso);
                        if(googleSignInClient!=null)
                            googleSignInClient.signOut();
                        Intent intent1=new Intent(HomeActivity.this, UserLoginActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                    break;

                }
                transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment,fragment);

                transaction.commit();
               drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


navigationView.getHeaderView(0).findViewById(R.id.header_view).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        TAG=true;
        mDataRef.child("sellers").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {if(TAG) {
                TAG = false;

                if (snapshot.exists()) {
                    Intent intent = new Intent(HomeActivity.this, SetupProfileActivity.class);
                    startActivity(intent);
                } else {
                    db.collection("sellers").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Intent intent = new Intent(HomeActivity.this, StoreActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(HomeActivity.this, RegisterStoreActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

                }
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
});



    }



    public void getUserDatas()
{
    db.collection("users").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            if(documentSnapshot.exists())
            {
                User user=documentSnapshot.toObject(User.class);
                userCountry=user.getUcountry();
                userState=user.getUstate();
                userCity=user.getUcity();
                tvCityName.setText(userCity);
            }
        }
    });
}


    private void setUserDataInNavHeader() {

          db.collection("sellers").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                  if(documentSnapshot.exists())
                  {
                      Seller seller=documentSnapshot.toObject(Seller.class);
                      if(seller.getOwnerImage()!=null)
                          Picasso.get().load(seller.getOwnerImage()).into(imgNavUserImage);
                      tvNavStoreName.setText(seller.getStoreName());
                      navigationView.getMenu().findItem(R.id.nav_store).setVisible(true);
                  }
              }
          });

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }


    public  void openDrawer()
    {
        toggle=new ActionBarDrawerToggle(HomeActivity.this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.openDrawer(GravityCompat.START);
        toggle.onDrawerOpened(drawerLayout);
        toggle.syncState();
    }


}
