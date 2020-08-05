package sell_and_buy.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.mycity.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import dialog.CustumProgressDialog;
import model.ApplicationClass;
import model.Product;
import model.ProductCategory;
import sell_and_buy.ProductActivity;
import seller.RegisterStoreActivity;
import view_holder.ProductCategoryViewHolder;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class SellProductFragment extends Fragment {
    EditText etProductTitle,etProductPrice,etSellerCity,etProductDes,etSellerPhone,etOtherType;
    TextView tvProductType;
    Button btnCancel,btnSell;
    LinearLayout photosLayout;
    FirebaseAuth mAuth;
    ToggleButton morePhotos;
    FirebaseFirestore db;
    StorageReference mStoreRef;
    CircleImageView productImage1;
    ImageView productImage2, productImage3, productImage4, productImage5;
    String productImageUrl1=null,productImageUrl2=null,productImageUrl3=null,productImageUrl4=null,productImageUrl5=null;
    public final static int IMAGE_VIEW1=1,IMAGE_VIEW2=2,IMAGE_VIEW3=3,IMAGE_VIEW4=4,IMAGE_VIEW5=5;
   public String PRODUCT_ID="";
   RecyclerView recyclerView;
   public static String productType="";
View view;
    BottomSheetDialog bottomSheetDialog;
    public SellProductFragment() {
        String currentTime=new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String currentDate=new SimpleDateFormat("dd:MM:yy", Locale.getDefault()).format(new Date());
        PRODUCT_ID="ProductId_"+currentTime+currentDate;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ApplicationClass.loadLocale(getContext());
         View view=inflater.inflate(R.layout.fragment_sell_product, container, false);
        this.view=view;

        etProductTitle=view.findViewById(R.id.et_product_title);
        etProductDes=view.findViewById(R.id.et_product_des);
        etProductPrice=view.findViewById(R.id.et_product_price);
        etSellerCity=view.findViewById(R.id.et_seller_city);
        etSellerPhone=view.findViewById(R.id.et_seller_phone);
        etOtherType=view.findViewById(R.id.et_other_name);

        tvProductType=view.findViewById(R.id.tv_product_type);
        btnCancel=view.findViewById(R.id.btn_cancel);
        btnSell=view.findViewById(R.id.btn_apply);

        photosLayout=view.findViewById(R.id.image_view);
        morePhotos=view.findViewById(R.id.tv_add_more);

        productImage1=view.findViewById(R.id.img_product);
        productImage2=view.findViewById(R.id.img_product1);
        productImage3=view.findViewById(R.id.img_product2);
        productImage4=view.findViewById(R.id.img_product3);
        productImage5=view.findViewById(R.id.img_product4);



        productImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGellery(IMAGE_VIEW1);
            }
        });
        productImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGellery(IMAGE_VIEW2);
            }
        });
        productImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGellery(IMAGE_VIEW3);
            }
        });
        productImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGellery(IMAGE_VIEW4);
            }
        });
        productImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGellery(IMAGE_VIEW5);
            }
        });
        morePhotos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    photosLayout.setVisibility(View.VISIBLE);
                }
                else {
                    photosLayout.setVisibility(View.GONE);
                }
            }
        });
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mStoreRef= FirebaseStorage.getInstance().getReference();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellProduct();
            }
        });
        tvProductType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductTypeList();
            }
        });
        return view;
    }

    private void openGellery(int IMAGE_VIEW) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_VIEW);
    }
    public void openProductTypeList()
    {
        bottomSheetDialog=new BottomSheetDialog(
                getContext(),R.style.AppBottomSheetDialogTheme
        );
        View bottomSheetView= LayoutInflater.from(getContext())
                .inflate(
                        R.layout.layout_bottom_sheet,
                        (LinearLayout)view.findViewById(R.id.bottom_sheet_contanier)
                );
        TextView tvTypeName=bottomSheetView.findViewById(R.id.tv_category_name);
        ImageView btnBack=bottomSheetView.findViewById(R.id.img_downword);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        tvTypeName.setText(R.string.product_type);
        recyclerView=bottomSheetView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setData();

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void setData() {
        FirestoreRecyclerAdapter<ProductCategory, ProductCategoryViewHolder>adapter;
        Query query=db.collection("productCategory");
        FirestoreRecyclerOptions<ProductCategory> options=new FirestoreRecyclerOptions.Builder<ProductCategory>()
                .setQuery(query,ProductCategory.class)
                .build();
        adapter=new FirestoreRecyclerAdapter<ProductCategory, ProductCategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductCategoryViewHolder holder, int position, @NonNull final ProductCategory model) {
                //ApplicationClass.setTranslatedText(holder.tvProductCategory,model.getCategoryName());

                holder.tvProductCategory.setText(model.getCategoryName());

                holder.tvProductCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvProductType.setText(model.getCategoryName());
                        //ApplicationClass.setTranslatedText(tvProductType,model.getCategoryName());
                        productType=model.getCategoryName();
                    tvProductType.setTextColor(getResources().getColor(R.color.selver));
                         if(model.getCategoryName().equals("Other"))
                         {
                             etOtherType.setVisibility(View.VISIBLE);
                         }
                         else
                         {
                             etOtherType.setVisibility(View.GONE);
                         }
                         bottomSheetDialog.dismiss();
                    }
                });
            }

            @NonNull
            @Override
            public ProductCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.store_layout,parent,false);
                return new ProductCategoryViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    //    private void openProductTypeList() {
//        PopupMenu popupMenu=new PopupMenu(getContext(),tvProductType);
//        popupMenu.getMenuInflater().inflate(R.menu.product_type_menu,popupMenu.getMenu());
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                tvProductType.setText(item.getTitle());
//                if(item.getItemId()==R.id.other)
//                {
//                    etOtherType.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    etOtherType.setVisibility(View.GONE);
//                }
//                return true;
//            }
//        });
//        popupMenu.show();
//
//    }
    private void sellProduct()
    {
        String productTitle=etProductTitle.getText().toString();
        String productPrice=etProductPrice.getText().toString();
        String productDes=etProductDes.getText().toString();
        String sellerPhone=etSellerPhone.getText().toString();
        String sellerCity=etSellerCity.getText().toString();
//        String productType=tvProductType.getText().toString();
        boolean other=false;
        if(productImageUrl1==null)
        {
            Toast.makeText(getContext(),"At least one image requried ",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(productTitle))
        {
            Toast.makeText(getContext(),"Product title can't be empty ",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(productPrice))
        {
            Toast.makeText(getContext(),"Product price can't be empty ",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(sellerPhone))
        {
            Toast.makeText(getContext(),"Phone number compalsury ",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(sellerCity))
        {
            Toast.makeText(getContext(),"City can't be empty ",Toast.LENGTH_LONG).show();
        }
        else if(productType.equals("select product type"))
        {
            Toast.makeText(getContext(),"select any product type",Toast.LENGTH_LONG).show();
        }
        else if(productType.equals("Other")&&TextUtils.isEmpty(etOtherType.getText().toString()))
        {

            Toast.makeText(getContext(),"enter a product type",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(productDes))
        {
            Toast.makeText(getContext(),"Enter product description",Toast.LENGTH_LONG).show();
        }
        else
        {
            if(productType.equals("Other")) {
                other = true;
                productType = etOtherType.getText().toString();
            }

            final CustumProgressDialog dialog=new CustumProgressDialog(getActivity());
            dialog.startProgressBar(getString(R.string.ready_for_sell));
           final Product product=new Product();

            product.setPdescription(productDes);
            product.setPprice(productPrice);
            product.setPtitle(productTitle);
            product.setPtype(productType);
            product.setScity(sellerCity);
            product.setSphone(sellerPhone);
            product.setOthers(other);
            product.setUid(mAuth.getUid());
            product.setPid(PRODUCT_ID);

            //For searching
            product.setSortCity(sellerCity.toLowerCase());
            product.setSortTitle(productTitle.toLowerCase());
            product.setSortType(productType.toLowerCase());

            //for hindi
            ApplicationClass.translatedData=new HashMap<>();
            ApplicationClass.translatedData.put("pprice",productPrice);
            ApplicationClass.translatedData.put("sphone",sellerPhone);
            ApplicationClass.translatedData.put("pid",PRODUCT_ID);
            ApplicationClass.translatedData.put("uid",mAuth.getUid());
            ApplicationClass.translatedData.put("others",other);
            ApplicationClass.setTranslatedDataToMap("pdescription",productDes);
            ApplicationClass.setTranslatedDataToMap("ptitle",productTitle);
            ApplicationClass.setTranslatedDataToMap("ptype",productType);
            ApplicationClass.setTranslatedDataToMap("scity",sellerCity);
            ApplicationClass.translatedData.put("sortCity",sellerCity.toLowerCase());
            ApplicationClass.translatedData.put("sortTitle",productTitle.toLowerCase());
            ApplicationClass.translatedData.put("sortType",productType.toLowerCase());



            List<String>list=new ArrayList<>();

            list.add(productImageUrl1);

            if(productImageUrl2!=null)
            {
                list.add(productImageUrl2);

            }
            if(productImageUrl3!=null)
            {
                list.add(productImageUrl3);

            }
            if(productImageUrl4!=null)
            {
                list.add(productImageUrl4);

            }
            if(productImageUrl5!=null)
            {
                list.add(productImageUrl5);

            }
            product.setPimage(list);
            ApplicationClass.translatedData.put("pimage",list);
new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
        db.collection("ensellProducts").document(PRODUCT_ID)
                .set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                db.collection("hisellProducts")
                        .document(PRODUCT_ID).set(ApplicationClass.translatedData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            dialog.stopProgressBar();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                        else
                        {
                            dialog.stopProgressBar();
                            Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.stopProgressBar();
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }
}, 3000);

        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED)
        {
            Toast.makeText(getContext(),"try again",Toast.LENGTH_LONG).show();
            return;
        }
        Uri uri=data.getData();
        String imageNumber="image1.jpg";
        switch (requestCode)
        {
            case IMAGE_VIEW1:
                productImage1.setImageURI(uri);
                imageNumber="image1.jpg";
                break;
            case IMAGE_VIEW2:
                productImage2.setImageURI(uri);
                imageNumber="image2.jpg";
                break;
            case IMAGE_VIEW3:
                productImage3.setImageURI(uri);
                imageNumber="image3.jpg";
                break;
            case IMAGE_VIEW4:
                productImage4.setImageURI(uri);
                imageNumber="image4.jpg";
                break;
            case IMAGE_VIEW5:
                productImage5.setImageURI(uri);
                imageNumber="image5.jpg";
                break;

        }
        uploadImage(uri,imageNumber);
    }

    private void uploadImage(Uri uri, final String imageNumber) {
        final CustumProgressDialog dialog=new CustumProgressDialog(getActivity());
        dialog.startProgressBar(getContext().getString(R.string.photo_uploading));
        final StorageReference childRef=mStoreRef.child("images").child("sellProduct").child(PRODUCT_ID).child(imageNumber);
        childRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                       // productImageUrl1=uri.toString();
                        switch (imageNumber)
                        {
                            case "image1.jpg":
                                productImageUrl1=uri.toString();
                                break;
                            case "image2.jpg":
                                productImageUrl2=uri.toString();
                                break;
                            case "image3.jpg":
                                productImageUrl3=uri.toString();
                                break;
                            case "image4.jpg":
                                productImageUrl4=uri.toString();
                                break;
                            case "image5.jpg":
                                productImageUrl5=uri.toString();
                                break;

                        }
                        dialog.stopProgressBar();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.stopProgressBar();
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.stopProgressBar();
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
