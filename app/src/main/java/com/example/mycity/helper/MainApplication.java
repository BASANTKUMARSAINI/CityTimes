package com.example.mycity.helper;

import android.app.Application;
import android.content.Context;

import java.util.Locale;

public class MainApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base,"en"));
    }
}
