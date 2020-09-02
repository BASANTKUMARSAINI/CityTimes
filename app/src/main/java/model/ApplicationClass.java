package model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.example.mycity.BuildConfig;
import com.example.mycity.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
//import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import adapter.SliderAdapterExample;
import authantication.UserLoginActivity;
import sell_and_buy.SellAndBuyActivity;
import seller.RegisterStoreActivity;
import services.TrackingService;
import users.HomeActivity;
import users.ShopActivity;

import static android.content.Context.MODE_PRIVATE;

public class ApplicationClass
{
    public static FirebaseFirestore db;
    public static Double USER_LOGITUDE=0.0,USER_LATITUDE=0.0;
    public  static  String currentAddress=null;
    private static  String translatedText="tt";
    public  static String LANGUAGE_MODE="en";
    public static int TARGET_LANGUAGE_CODE= FirebaseTranslateLanguage.EN;
    public static HashMap<String,Object>translatedData;
    public static void logout(final Context context)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage(R.string.do_you_want_lagout);
        builder.setTitle(context.getString(R.string.logout)+" CityTimes");
        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.stopService(new Intent(context, TrackingService.class));
                        FirebaseAuth mAuth=FirebaseAuth.getInstance();
                        mAuth.signOut();
                        if(LoginManager.getInstance()!=null)
                            LoginManager.getInstance().logOut();
                        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
                        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(context,gso);
                        if(googleSignInClient!=null)
                            googleSignInClient.signOut();
                        Intent intent1=new Intent(context, UserLoginActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent1);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();

    }
    public static void shareImage(String url,final String text,final Context context) {

        Picasso.get().load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.v("FFF","in");
                Intent i = new Intent(Intent.ACTION_SEND);
                //text
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,text);


                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap,context));
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                context.startActivity(Intent.createChooser(i, "Share Image"));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.v("FFF",e.getMessage());

            }

            @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.v("FFF","intryin");
            }
        });
    }
    public static Uri getLocalBitmapUri(Bitmap bmp,Context context) {
        Uri bmpUri = null;
        try {
            Log.v("FFF","intry");
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri= FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",file);
            //bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            Log.v("FFF","exc"+e.getMessage());
            e.printStackTrace();
        }
        Log.v("URI","uri"+bmpUri);
        return bmpUri;
    }

    public static void setSliderImage(final Context context, final SliderView sliderView, String imageType) {
        db=FirebaseFirestore.getInstance();

        db.collection("images").whereEqualTo("imageType",imageType).limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<SliderItem> list=new ArrayList<>();
                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                {
                    list.add(documentSnapshot.toObject(SliderItem.class));
                }

                SliderAdapterExample adapter = new SliderAdapterExample(context,list);

                sliderView.setSliderAdapter(adapter);

                //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                sliderView.startAutoCycle();
                sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                //sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
               // sliderView.setIndicatorSelectedColor(Color.WHITE);
               // sliderView.setIndicatorUnselectedColor(Color.GRAY);
                //sliderView.setScrollTimeInSec(2); //set scroll delay in seconds :


            }
        });
    }
    public static void makeCall(Context context,String phone)
    {
        Intent intent=new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        context.startActivity(intent);
    }
    public static void onGps(final Context context) {
        final androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setMessage("Enable Gps").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final androidx.appcompat.app.AlertDialog dialog=builder.create();
        dialog.show();

    }
    public static String getAddress(Context context)
    {
        if(ApplicationClass.USER_LATITUDE==0.0||ApplicationClass.USER_LOGITUDE==0.0)
        {
            return null;
        }
        Geocoder geocoder;
        List<Address>addresses;
        geocoder=new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(ApplicationClass.USER_LATITUDE, ApplicationClass.USER_LOGITUDE, 1);

        }
        catch (Exception e)
        {
            return null;
        }
        String address = addresses.get(0).getAddressLine(0);
        if(address!=null)
            return address;
        String city=addresses.get(0).getLocality();
        String state=addresses.get(0).getAdminArea();
        String country=addresses.get(0).getCountryName();
        String postalCode=addresses.get(0).getPostalCode();
        address=city+" "+state+" "+country+" "+postalCode;
        return address;
    }

    public static void setLocale(Context context,String lang) {
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        context.getResources().updateConfiguration(configuration,context.getResources().getDisplayMetrics());
    }
    public static void loadLocale(Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("Setting",MODE_PRIVATE);
        String language=sharedPreferences.getString("language","en");
        setLocale(context,language);
    }

    public static void setTranslatedText(final TextView textView, final String text)
    {
        textView.setText(text);
        Log.v("CITY",text+"kk");
        int langCode=0;
        if(text!=null&&!text.equals("")) {
            identifyLanguage(text);
            Log.v("CODE", "code" + TARGET_LANGUAGE_CODE);
            if (ApplicationClass.LANGUAGE_MODE.equals("hi"))
                langCode = FirebaseTranslateLanguage.HI;
            else {
                langCode = FirebaseTranslateLanguage.EN;
            }
            Log.v("CODE", "code");
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    .setSourceLanguage(TARGET_LANGUAGE_CODE)
                    .setTargetLanguage(langCode)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();
            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(text).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            textView.setText(s);

                            Log.v("CITY", "sss" + s);
                            Log.v("CODE", "code" + TARGET_LANGUAGE_CODE);
                        }
                    });
                }
            });
        }

    }
    public static void setTranslatedDataToMap(final String key,final String text)
    {
        if(text!=null&&!text.equals("")) {
            identifyLanguage(text);
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    .setSourceLanguage(TARGET_LANGUAGE_CODE)
                    .setTargetLanguage(FirebaseTranslateLanguage.HI)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();

            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(text).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            ApplicationClass.translatedData.put(key,s);

                        }
                    });
                }
            });
        }

    }

    private static void identifyLanguage(String text) {
        FirebaseLanguageIdentification languageIdentification= FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
        languageIdentification.identifyLanguage(text).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                if(!s.equals("uid"))
                {
                    getLanguageCode(s);
                }
            }
        });

    }

    private static void getLanguageCode(String s) {
        if(s.equals("hi"))
        {
            TARGET_LANGUAGE_CODE=FirebaseTranslateLanguage.HI;
        }
        else {
            TARGET_LANGUAGE_CODE= FirebaseTranslateLanguage.EN;
        }

    }


    public  static String getLocaleStringResource(Locale requestLocale,int resourceId,Context context)
    {
        String result;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            Configuration configuration=new Configuration(context.getResources().getConfiguration());
            configuration.setLocale(requestLocale);
            result=context.createConfigurationContext(configuration).getText(resourceId).toString();

        }
        else
        {
            Resources resource=context.getResources();
            Configuration configuration=resource.getConfiguration();
            Locale savedLocale=configuration.locale;
            configuration.locale=requestLocale;
            resource.updateConfiguration(configuration,null);
            result=resource.getString(resourceId);
            configuration.locale=savedLocale;
            resource.updateConfiguration(configuration,null);
        }
        return  result;
    }
    public  static String getEnglishSubCategory(String subCategory,Context context) {
        Log.v("ID","id"+subCategory);
        try{
        int resourceId=context.getResources().getIdentifier(subCategory.toLowerCase(),"string",context.getPackageName());
        Log.v("ID","id"+resourceId);
        return  getLocaleStringResource(new Locale("en"),resourceId,context);}
        catch (Exception e)
        {
            Log.v("ID",e.getMessage());
            return "just return";
        }
    }
    public static String getEnglishStringDirect(int resId,Context context)
    {
        return  getLocaleStringResource(new Locale("en"),resId,context);
    }

}
