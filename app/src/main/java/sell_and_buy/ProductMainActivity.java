package sell_and_buy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycity.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.ApplicationClass;
import model.Office;
import model.Product;
import users.SeeFullImageActivity;

import static android.view.View.GONE;

public class ProductMainActivity extends AppCompatActivity {
     CircleImageView productImage1,imgCall;
     private  LinearLayout mainPhotoLinearLayout,descriptionLinearLayout;
     public ImageView productImage2, productImage3, productImage4, productImage5,imgDelete;
     public ImageView productShare;
     TextView tvTitle,tvPrice,tvCity,tvType,tvDes,tvPhone;
     FirebaseFirestore db;
     String productText="",PHONE="";
     String USER_TYPE="";
     String PID="";
     StorageReference mStoreRef;
     String image_URL_1=null,image_URL_2=null,image_URL_3=null,image_URL_4=null,image_URL_5=null;


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

        imgCall=findViewById(R.id.imgCall);
        mainPhotoLinearLayout=findViewById(R.id.photo_main_linear_layout);
        descriptionLinearLayout=findViewById(R.id.description_layout);
        imgDelete=findViewById(R.id.btn_delete);

        db=FirebaseFirestore.getInstance();
        mStoreRef= FirebaseStorage.getInstance().getReference();
        USER_TYPE=getIntent().getStringExtra("user_type");
        if(USER_TYPE.equals("seller"))
        {
            imgDelete.setVisibility(View.VISIBLE);
            imgCall.setVisibility(GONE);
        }
        PID=getIntent().getStringExtra("pid");
        setAllData(getIntent().getStringExtra("pid"));


    }


    private void setAllData(String pid) {
        String path=ApplicationClass.LANGUAGE_MODE+"sellProducts";
        db.collection(path).document(pid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Product product = documentSnapshot.toObject(Product.class);
                    tvPrice.setText(getString(R.string.Rs)+product.getPprice());
                    tvPhone.setText(product.getSphone());


                        tvTitle.setText(product.getPtitle());
                        tvCity.setText(product.getScity());
                        if(product.getPdescription()!=null)
                        {
                            tvDes.setVisibility(View.VISIBLE);
                            descriptionLinearLayout.setVisibility(View.VISIBLE);
                            tvDes.setText(product.getPdescription());
                        }
                        tvType.setText(product.getPtype());

                    productText = "Tittle:" + product.getPtitle() + "\n"
                            + "Price:" + product.getPprice() + "\n"
                            + "Phone:" + product.getSphone() + "\n" +
                            product.getPdescription();

                    List<String> imageUrl = product.getPimage();
                    PHONE = product.getSphone();
                    image_URL_1= imageUrl.get(0);

                    Picasso.get().load(imageUrl.get(0)).into(productImage1);
                    int siz = imageUrl.size();
                    if(siz>1) {
                        mainPhotoLinearLayout.setVisibility(View.VISIBLE);
                        for (int i = 1; i < siz; i++) {
                            if (i == 1) {
                                productImage2.setVisibility(View.VISIBLE);
                                image_URL_2=imageUrl.get(i);
                                Picasso.get().load(imageUrl.get(i)).into(productImage2);
                            } else if (i == 2) {
                                productImage3.setVisibility(View.VISIBLE);
                                image_URL_3=imageUrl.get(i);
                                Picasso.get().load(imageUrl.get(i)).into(productImage3);
                            } else if (i == 3) {
                                productImage4.setVisibility(View.VISIBLE);
                                image_URL_4=imageUrl.get(i);
                                Picasso.get().load(imageUrl.get(i)).into(productImage4);
                            } else if (i == 4) {
                                productImage5.setVisibility(View.VISIBLE);
                                image_URL_5=imageUrl.get(i);
                                Picasso.get().load(imageUrl.get(i)).into(productImage5);
                            }
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
        ApplicationClass.shareImage(image_URL_1,productText,ProductMainActivity.this);
    }
    public void  makePhoneCall(View view)
    {
        ApplicationClass.makeCall(ProductMainActivity.this,PHONE);
    }

    public void deleteProduct(View view) {
        deleteProduct();
    }

    private void deleteProduct() {
        db.collection("en"+"sellProducts").document(PID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    Product product=documentSnapshot.toObject(Product.class);
                    List<String>list=product.getPimage();
                    final int siz=list.size();
                    AlertDialog.Builder builder=new AlertDialog.Builder(ProductMainActivity.this);
                    builder.setMessage(R.string.do_you_want_delete_this_product);
                    builder.setTitle(R.string.delete_product);
                    builder.setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final String path="en"+"sellProducts";
                                    db.collection(path).document(PID).delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    db.collection("hisellProducts").document(PID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            List<String>imgIdList=new ArrayList<>();
                                                            imgIdList.add("image1.jpg");
                                                            imgIdList.add("image2.jpg");
                                                            imgIdList.add("image3.jpg");
                                                            imgIdList.add("image4.jpg");
                                                            imgIdList.add("image5.jpg");
                                                            int k=0;
                                                            for(int i=0;i<siz;i++)
                                                            {
                                                                k++;
                                                                mStoreRef.child("images").child("sellProduct").child(PID).child(imgIdList.get(i)).delete();
                                                            }
                                                            if(k==siz)
                                                                finish();

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(ProductMainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ProductMainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog=builder.create();
                    dialog.show();
                }
            }
        });


    }
    public void  fullImage1(View view)
    {
        if(image_URL_1!=null) {
            Intent intent = new Intent(ProductMainActivity.this, SeeFullImageActivity.class);
            intent.putExtra("uri", image_URL_1);
            startActivity(intent);
        }
    }
    public void  fullImage2(View view)
    {
        if(image_URL_2!=null) {
            Intent intent = new Intent(ProductMainActivity.this, SeeFullImageActivity.class);
            intent.putExtra("uri", image_URL_2);
            startActivity(intent);
        }
    }
    public void  fullImage3(View view)
    {
        if(image_URL_3!=null) {
            Intent intent = new Intent(ProductMainActivity.this, SeeFullImageActivity.class);
            intent.putExtra("uri", image_URL_3);
            startActivity(intent);
        }
    }
    public void  fullImage4(View view)
    {
        if(image_URL_4!=null) {

            Intent intent = new Intent(ProductMainActivity.this, SeeFullImageActivity.class);
            intent.putExtra("uri", image_URL_4);
            startActivity(intent);
        }
    }
    public void  fullImage5(View view)
    {
        if(image_URL_5!=null) {

            Intent intent = new Intent(ProductMainActivity.this, SeeFullImageActivity.class);
            intent.putExtra("uri", image_URL_5);
            startActivity(intent);
        }
    }
}
