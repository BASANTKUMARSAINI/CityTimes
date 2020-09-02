package useless;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mycity.R;

public class RoughActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rough);
    }
    //HomeActvity
///////1
    //        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
//        {
//            onGps();
//        }
//
//
//        Intent intent=new Intent(this, LocationService.class);
//        startService(intent);
//
//        if(ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&
//                ActivityCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
//
//        }

   ///////2

    //        ProgressDialog dialog=new ProgressDialog(HomeActivity.this);
//        dialog.setCancelable(false);
//        dialog.setMessage("Updating...");
//        dialog.show();
//        db.collection("")

    ///////3

    //        db.collection("images").whereEqualTo("imageType","general").limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                List<SliderItem> list=new ArrayList<>();
//                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
//                {
//                    list.add(documentSnapshot.toObject(SliderItem.class));
//                }
//
//                SliderAdapterExample adapter = new SliderAdapterExample(HomeActivity.this,list);
//
//                sliderViewBottom.setSliderAdapter(adapter);
//
//                //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//                sliderViewBottom.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//                sliderViewBottom.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//                sliderViewBottom.setIndicatorSelectedColor(Color.WHITE);
//                sliderViewBottom.setIndicatorUnselectedColor(Color.GRAY);
//                sliderViewBottom.setScrollTimeInSec(4); //set scroll delay in seconds :
//                sliderViewBottom.startAutoCycle();
//
//            }
//        });

    //////4
    //        db.collection("images").whereEqualTo("imageType","general").limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                List<SliderItem> list=new ArrayList<>();
//                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
//                {
//                    list.add(documentSnapshot.toObject(SliderItem.class));
//                }
//
//                SliderAdapterExample adapter = new SliderAdapterExample(HomeActivity.this,list);
//
//                sliderView.setSliderAdapter(adapter);
//
//                //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//                sliderView.setIndicatorSelectedColor(Color.WHITE);
//                sliderView.setIndicatorUnselectedColor(Color.GRAY);
//                sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
//                sliderView.startAutoCycle();
//
//            }
//        });

    /////5
    //    private void setUserDataInNavHeader() {
//
//        db.collection(ApplicationClass.LANGUAGE_MODE+"sellers").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists())
//                {
//                    Seller seller=documentSnapshot.toObject(Seller.class);
//                    if(seller.getOwnerImage()!=null)
//                        Picasso.get().load(seller.getOwnerImage()).into(imgNavUserImage);
//                    //ApplicationClass.setTranslatedText(tvNavStoreName,seller.getStoreName());
//                    tvNavStoreName.setText(seller.getStoreName());
//                    navigationView.getMenu().findItem(R.id.nav_store).setVisible(true);
//
//                    //shop status
//                    SHOP_STATUS=seller.isShopStatus();
//                    shopStatusLayout.setVisibility(View.VISIBLE);
//                    if(seller.isShopStatus())
//                    {
//                        tvShopStatus.setText(R.string.open);
//
//                        tvShopStatus.setTextColor(getResources().getColor(R.color.green));
//                        shopStatusImage.setImageResource(R.drawable.ic_right_green);
//                    }
//                    else
//                    {
//                        tvShopStatus.setText(R.string.close);
//                        tvShopStatus.setTextColor(getResources().getColor(R.color.red));
//                        shopStatusImage.setImageResource(R.drawable.ic_cross_red);
//                    }
//                }
//            }
//        });
//
//    }

    //ShopActiviy
    ////1
    //        if(ActivityCompat.checkSelfPermission(ShopActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&
//                ActivityCompat.checkSelfPermission(ShopActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_LOCATION);
//            //Toast.makeText(ShopActivity.this,"Give Location permission",Toast.LENGTH_LONG).show();
//            return;
//        }
//        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
//        {
//            onGps();
//        }
    //Sell and Buy
    ///////1
    //    private void enableSearchView(View view, boolean enabled)
//    {
//        view.setEnabled(enabled);
//        if(view instanceof ViewGroup)
//        {
//            ViewGroup viewGroup=(ViewGroup)view;
//            for(int i=0;i<viewGroup.getChildCount();i++)
//            {
//                View child=viewGroup.getChildAt(i);
//                enableSearchView(child,enabled);
//            }
//        }
//    }
    //        db.collection("images").whereEqualTo("imageType","general").limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                List<SliderItem> list=new ArrayList<>();
//                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
//                {
//                    list.add(documentSnapshot.toObject(SliderItem.class));
//                }
//
//                SliderAdapterExample adapter = new SliderAdapterExample(SellAndBuyActivity.this,list);
//
//                sliderView.setSliderAdapter(adapter);
//
//                //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//                sliderView.setIndicatorSelectedColor(Color.WHITE);
//                sliderView.setIndicatorUnselectedColor(Color.GRAY);
//                sliderView.setScrollTimeInSec(2); //set scroll delay in seconds :
//                sliderView.startAutoCycle();
//
//            }
//        });
    //        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                enableSearchView(searchView,true);
//               // Intent intent=new Intent(SellAndBuyActivity.this, SearchCategoriesActivity.class);
//                EditText editText=(EditText)searchView.findViewById(R.id.search_src_text);
//                editText.setEnabled(true);
//                //startActivityForResult(intent,1);
//
//            }
//        });
//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                enableSearchView(searchView,true);
////                Intent intent=new Intent(SellAndBuyActivity.this,SearchCategoriesActivity.class);
//                EditText editText=(EditText)searchView.findViewById(R.id.search_src_text);
//                editText.setEnabled(true);
//                //startActivityForResult(intent,1);
//            }
//        });
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager manager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                EditText editText=(EditText)searchView.findViewById(R.id.search_src_text);
//                manager.showSoftInput(editText,0);
//
//            }
//        });
//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                transaction=getSupportFragmentManager().beginTransaction();
//                fragment=new SearchProductFragment();
//                transaction.replace(R.id.sub_fragment,fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });
//////////////////////2
    //    private void setGovernmentData(String pid) {
//        //pid
//        String path=ApplicationClass.LANGUAGE_MODE+"government";
//        db.collection(path).document("ll").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {
//                    Office office = documentSnapshot.toObject(Office.class);
//                    daysLayout.setVisibility(View.VISIBLE);
//                    timeLayout.setVisibility(View.VISIBLE);
//                    navigationImage.setVisibility(View.VISIBLE);
//
//                    List<String> timeListFrom = office.getTimeFrom();
//                    List<String> timeListTo = office.getTimeTo();
//
//                    String storeTiming = timeListFrom.get(0) + ":" + timeListFrom.get(1) + timeListFrom.get(2) + "-"
//                            + timeListTo.get(0) + ":" + timeListTo.get(1) + timeListTo.get(2);
//                    tvTime.setText(storeTiming);
//
//
//                    HashMap<String, Boolean> days = office.getDays();
//                    setDays(days);
//                    tvTitle.setText(office.getOfficeType());
//                    tvCity.setText(office.getAddress());
//                    tvDes.setText(office.getDescription());
//                    tvType.setText(office.getHeadName());
//                    priceLayout.setVisibility(GONE);
//tvPhone.setText(office.getPhone());
//                    productText = office.getOfficeType() + "\n"
//                            + "Head:" + office.getHeadName() + "\n"
//                            + "Phone:" + office.getPhone() + "\n" +
//                            office.getDescription();
//
//
//                    if (office.getImage() != null)
//                        Picasso.get().load(office.getImage()).into(productImage1);
//                }
//            }
//
//
//        });
//    }
//    private void setDays(HashMap<String, Boolean> days) {
//
//        tvSunday.setTextColor(getResources().getColor(R.color.green));
//        tvMonday.setTextColor(getResources().getColor(R.color.green));
//        tvTuseday.setTextColor(getResources().getColor(R.color.green));
//        tvWednesday.setTextColor(getResources().getColor(R.color.green));
//        tvThrusday.setTextColor(getResources().getColor(R.color.green));
//        tvFriday.setTextColor(getResources().getColor(R.color.green));
//        tvSaturday.setTextColor(getResources().getColor(R.color.green));
//
//        if(!days.get("sunday"))
//            tvSunday.setTextColor(getResources().getColor(R.color.red));
//        if(!days.get("monday"))
//            tvMonday.setTextColor(getResources().getColor(R.color.red));
//        if(!days.get("tuesday"))
//            tvTuseday.setTextColor(getResources().getColor(R.color.red));
//        if(!days.get("wednesday"))
//            tvWednesday.setTextColor(getResources().getColor(R.color.red));
//        if(!days.get("thursday"))
//            tvThrusday.setTextColor(getResources().getColor(R.color.red));
//        if(!days.get("friday"))
//            tvFriday.setTextColor(getResources().getColor(R.color.red));
//        if(!days.get("saturday"))
//            tvSaturday.setTextColor(getResources().getColor(R.color.red));
//    }


    //office
//
//        tvSunday=findViewById(R.id.tv_day_sunday);
//        tvMonday=findViewById(R.id.tv_day_monday);
//        tvTuseday=findViewById(R.id.tv_day_tuesday);
//        tvWednesday=findViewById(R.id.tv_day_wednesday);
//        tvThrusday=findViewById(R.id.tv_day_thursday);
//        tvFriday=findViewById(R.id.tv_day_friday);
//        tvSaturday=findViewById(R.id.tv_day_saturday);

//        navigationImage=findViewById(R.id.img_navigation);
//        daysLayout=findViewById(R.id.day_layout);
//        timeLayout=findViewById(R.id.time_layout);
//        tvTime=findViewById(R.id.tv_time);
//        priceLayout=findViewById(R.id.price_layout);

    //
//        if(getIntent().getStringExtra("category").equals("government"))
//        {
//            String pid=getIntent().getStringExtra("pid");
//            Log.v("TAG",pid+"kk");
//           setGovernmentData(pid);
//        }

    //    //office
//    LinearLayout daysLayout,timeLayout,priceLayout;
//    CircleImageView navigationImage;
//    TextView tvSunday,tvMonday,tvTuseday,tvWednesday,tvThrusday,tvFriday,tvSaturday;
//    TextView tvTime;
//  private void setImages(ProductViewHolder holder, List<String>imgList) {
//        int siz=imgList.size();
//        if(siz==5)
//        {
//            holder.photosLayoutLastTwo.setVisibility(View.VISIBLE);
//            holder.photosLayoutFirsTwo.setVisibility(View.VISIBLE);
//            Picasso.get().load(imgList.get(1)).into(holder.productImage2);
//            Picasso.get().load(imgList.get(2)).into(holder.productImage3);
//            Picasso.get().load(imgList.get(3)).into(holder.productImage4);
//            Picasso.get().load(imgList.get(4)).into(holder.productImage5);
//        }
//        else if(siz==4)
//        {
//            holder.photosLayoutLastTwo.setVisibility(View.VISIBLE);
//            holder.photosLayoutFirsTwo.setVisibility(View.VISIBLE);
//            Picasso.get().load(imgList.get(1)).into(holder.productImage2);
//            Picasso.get().load(imgList.get(2)).into(holder.productImage3);
//            Picasso.get().load(imgList.get(3)).into(holder.productImage4);
//            holder.productImage5.setVisibility(GONE);
//        }
//        else if(siz==3)
//        {
//
//            holder.photosLayoutFirsTwo.setVisibility(View.VISIBLE);
//            Picasso.get().load(imgList.get(1)).into(holder.productImage2);
//            Picasso.get().load(imgList.get(2)).into(holder.productImage3);
//            holder.photosLayoutLastTwo.setVisibility(GONE);
//
//        }
//        else if(siz==2)
//        {
//
//            holder.photosLayoutFirsTwo.setVisibility(View.VISIBLE);
//            Picasso.get().load(imgList.get(1)).into(holder.productImage2);
//            holder.productImage3.setVisibility(GONE);
//            holder.photosLayoutLastTwo.setVisibility(GONE);
//
//
//        }
//        else
//        {
//            holder.photosLayoutLastTwo.setVisibility(GONE);
//            holder.photosLayoutFirsTwo.setVisibility(GONE);
//            Toast.makeText(getContext(),"no more photos",Toast.LENGTH_LONG).show();
//        }
//
//
//    }

//    private void makeCall(String sphone) {
//    }
    //}
    //                holder.productShare.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String text="Tittle:"+model.getPtitle()+"\n"
//                                +"Price:"+model.getPprice()+"\n"
//                                +"Phone:"+model.getSphone()+"\n"+
//                                model.getPdescription();
//                        ApplicationClass.shareImage(imgList.get(0),text,MyProductActivity.this);
//                    }
//                });

    //                holder.morePhotos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if(isChecked)
//                        {
//                            setImages(holder,imgList);
//                        }
//                        else
//                        {
//                            holder.photosLayoutLastTwo.setVisibility(GONE);
//                            holder.photosLayoutFirsTwo.setVisibility(GONE);
//                        }
//                    }
//                });
//
//                holder.call.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        makeCall(model.getSphone());
//                    }
//                });
}