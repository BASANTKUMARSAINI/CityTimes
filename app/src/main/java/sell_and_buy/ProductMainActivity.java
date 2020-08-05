package sell_and_buy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.ApplicationClass;
import model.Office;
import model.Product;

import static android.view.View.GONE;

public class ProductMainActivity extends AppCompatActivity {
    CircleImageView productImage1;
     public ImageView productImage2, productImage3, productImage4, productImage5;
     public ImageView productShare;
     TextView tvTitle,tvPrice,tvCity,tvType,tvDes,tvPhone;
     FirebaseFirestore db;
     String productText="",IMAGE_URL="",PHONE="";

    //office
    LinearLayout daysLayout,timeLayout,priceLayout;
    CircleImageView navigationImage;
    TextView tvSunday,tvMonday,tvTuseday,tvWednesday,tvThrusday,tvFriday,tvSaturday;
    TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(ProductMainActivity.this);
        setContentView(R.layout.activity_product_main);
tvPhone=findViewById(R.id.tv_phone_1);
        productImage2=findViewById(R.id.img_product1);
        productImage3=findViewById(R.id.img_product2);
        productImage4=findViewById(R.id.img_product3);
        productImage5=findViewById(R.id.img_product4);
        productShare=findViewById(R.id.img_share);
        productImage1=findViewById(R.id.image1);


        tvCity=findViewById(R.id.tv_product_address);
        tvDes=findViewById(R.id.tv_product_description);
        tvPrice=findViewById(R.id.tv_product_price);
        tvTitle=findViewById(R.id.tv_product_title);
        tvType=findViewById(R.id.tv_product_type);

        //office

        tvSunday=findViewById(R.id.tv_day_sunday);
        tvMonday=findViewById(R.id.tv_day_monday);
        tvTuseday=findViewById(R.id.tv_day_tuesday);
        tvWednesday=findViewById(R.id.tv_day_wednesday);
        tvThrusday=findViewById(R.id.tv_day_thursday);
        tvFriday=findViewById(R.id.tv_day_friday);
        tvSaturday=findViewById(R.id.tv_day_saturday);

        navigationImage=findViewById(R.id.img_navigation);
        daysLayout=findViewById(R.id.day_layout);
        timeLayout=findViewById(R.id.time_layout);
        tvTime=findViewById(R.id.tv_time);
        priceLayout=findViewById(R.id.price_layout);

        db=FirebaseFirestore.getInstance();

        if(getIntent().getStringExtra("category").equals("government"))
        {
            String pid=getIntent().getStringExtra("pid");
            Log.v("TAG",pid+"kk");
           setGovernmentData(pid);
        }
else
        {
            setAllData(getIntent().getStringExtra("pid"));
        }

    }

    private void setGovernmentData(String pid) {
        //pid
        String path=ApplicationClass.LANGUAGE_MODE+"government";
        db.collection(path).document("ll").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Office office = documentSnapshot.toObject(Office.class);
                    daysLayout.setVisibility(View.VISIBLE);
                    timeLayout.setVisibility(View.VISIBLE);
                    navigationImage.setVisibility(View.VISIBLE);

                    List<String> timeListFrom = office.getTimeFrom();
                    List<String> timeListTo = office.getTimeTo();

                    String storeTiming = timeListFrom.get(0) + ":" + timeListFrom.get(1) + timeListFrom.get(2) + "-"
                            + timeListTo.get(0) + ":" + timeListTo.get(1) + timeListTo.get(2);
                    tvTime.setText(storeTiming);


                    HashMap<String, Boolean> days = office.getDays();
                    setDays(days);

                    //
//                    ApplicationClass.setTranslatedText(tvTitle,office.getOfficeType() );
//                    ApplicationClass.setTranslatedText( tvCity,office.getAddress());
//                    ApplicationClass.setTranslatedText(tvDes,office.getDescription() );
//                    ApplicationClass.setTranslatedText(tvType,office.getHeadName());
                    tvTitle.setText(office.getOfficeType());
                    tvCity.setText(office.getAddress());
                    tvDes.setText(office.getDescription());
                    tvType.setText(office.getHeadName());
                    priceLayout.setVisibility(GONE);
tvPhone.setText(office.getPhone());
                    productText = office.getOfficeType() + "\n"
                            + "Head:" + office.getHeadName() + "\n"
                            + "Phone:" + office.getPhone() + "\n" +
                            office.getDescription();


                    if (office.getImage() != null)
                        Picasso.get().load(office.getImage()).into(productImage1);
                }
            }


        });
    }
    private void setDays(HashMap<String, Boolean> days) {

        tvSunday.setTextColor(getResources().getColor(R.color.green));
        tvMonday.setTextColor(getResources().getColor(R.color.green));
        tvTuseday.setTextColor(getResources().getColor(R.color.green));
        tvWednesday.setTextColor(getResources().getColor(R.color.green));
        tvThrusday.setTextColor(getResources().getColor(R.color.green));
        tvFriday.setTextColor(getResources().getColor(R.color.green));
        tvSaturday.setTextColor(getResources().getColor(R.color.green));

        if(!days.get("sunday"))
            tvSunday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("monday"))
            tvMonday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("tuesday"))
            tvTuseday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("wednesday"))
            tvWednesday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("thursday"))
            tvThrusday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("friday"))
            tvFriday.setTextColor(getResources().getColor(R.color.red));
        if(!days.get("saturday"))
            tvSaturday.setTextColor(getResources().getColor(R.color.red));
    }
    private void setAllData(String pid) {
        String path=ApplicationClass.LANGUAGE_MODE+"sellProducts";
        db.collection(path).document(pid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Product product = documentSnapshot.toObject(Product.class);

//                    ApplicationClass.setTranslatedText(tvTitle,product.getPtitle() );
//                    ApplicationClass.setTranslatedText( tvCity,product.getScity());
//                    ApplicationClass.setTranslatedText(tvDes,product.getPdescription() );
//                    ApplicationClass.setTranslatedText(tvType,product.getPtype());
//                    ApplicationClass.setTranslatedText(tvPrice,product.getPprice());

                    tvTitle.setText(product.getPtitle());
                    tvCity.setText(product.getScity());
                    tvDes.setText(product.getPdescription());
                    tvType.setText(product.getPtype());
                    tvPrice.setText(getString(R.string.rs)+product.getPprice());


                    productText = "Tittle:" + product.getPtitle() + "\n"
                            + "Price:" + product.getPprice() + "\n"
                            + "Phone:" + product.getSphone() + "\n" +
                            product.getPdescription();

                    List<String> imageUrl = product.getPimage();
                    PHONE = product.getSphone();
                    IMAGE_URL = imageUrl.get(0);
                    Picasso.get().load(imageUrl.get(0)).into(productImage1);
                    int siz = imageUrl.size();
tvPhone.setText(product.getSphone());
                    for (int i = 1; i < siz; i++) {
                        if (i == 1) {
                            productImage2.setVisibility(View.VISIBLE);
                            Picasso.get().load(imageUrl.get(i)).into(productImage2);
                        } else if (i == 2) {
                            productImage3.setVisibility(View.VISIBLE);
                            Picasso.get().load(imageUrl.get(i)).into(productImage3);
                        } else if (i == 3) {
                            productImage4.setVisibility(View.VISIBLE);
                            Picasso.get().load(imageUrl.get(i)).into(productImage3);
                        } else if (i == 4) {
                            productImage5.setVisibility(View.VISIBLE);
                            Picasso.get().load(imageUrl.get(i)).into(productImage4);
                        }
                    }
                }
            }
        });

    }
    public void goToBack(View view)
    {
        finish();
    }
    public void shareProduct(View view)
    {
        ApplicationClass.shareImage(IMAGE_URL,productText,ProductMainActivity.this);
    }
    public void  makePhoneCall(View view)
    {
        ApplicationClass.makeCall(ProductMainActivity.this,PHONE);
    }

    public void goToMap(View view) {
    }
}
