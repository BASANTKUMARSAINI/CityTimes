package com.example.mycity.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocaleHelper
{
    private static  final String SELECTED_LANGUAGE="Locale.Helper.Selected.Language";
    public static Context onAttach(Context context)
    {
        String lang=getPresistedData(context, Locale.getDefault().getLanguage());
        return  setLocale(context,lang);

    }
    public static Context onAttach(Context context,String defalutLanguage)
    {
        String lang=getPresistedData(context, defalutLanguage);
        return  setLocale(context,lang);

    }

    public static Context setLocale(Context context, String lang) {
        presist(context,lang);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            return updateResources(context,lang);
        }
        return updateResourcesLegacy(context,lang);
    }
@TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String lang) {
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
//    Configuration configuration=context.getResources().getConfiguration();
//    configuration.setLocale(locale);
//    configuration.setLayoutDirection(locale);
    Configuration configuration=new Configuration();
    configuration.locale=locale;
    context.getResources().updateConfiguration(configuration,context.getResources().getDisplayMetrics());
    return context.createConfigurationContext(configuration);

    }
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResourcesLegacy(Context context, String lang) {
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);

        Resources resources=context.getResources();
        Configuration configuration=resources.getConfiguration();
        configuration.locale=locale;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1)
            configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        return context;
    }

    private static void presist(Context context, String lang) {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(SELECTED_LANGUAGE,lang);
        editor.apply();
    }

    private static String getPresistedData(Context context, String language) {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(SELECTED_LANGUAGE,language);
    }

}
