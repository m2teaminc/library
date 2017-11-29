package com.m2team.library;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import com.m2team.library.utils.Initializer;
import com.m2team.library.utils.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import java.util.Locale;
import timber.log.Timber;

public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public abstract void init();

    public void initFresco() {
        //Fresco.initialize(this);
    }

    public void initLeakCanary() {
       /* if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
    }

    public void initCalligraphy() {
        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/" + Constant.DEFAULT_FONT + ".ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/
    }

    public void initFabric() {
        /*Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();

        // Initialize Fabric with the debug-disabled crashlytics.
        Fabric.with(this, crashlyticsKit);*/
    }

    public void initLocale(Context ctx, Locale newLocale) {
        Configuration cfg = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            cfg.setLocale(newLocale);
        } else {
            cfg.locale = newLocale;
        }
        Locale.setDefault(newLocale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getApplicationContext().createConfigurationContext(cfg);
        } else {
            ctx.getResources().updateConfiguration(cfg, getResources().getDisplayMetrics());
        }
    }

    public void initActiveAndroidDB(String dbName) {
        /*Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName(dbName +".db").create();
        ActiveAndroid.initialize(dbConfiguration);*/
    }

    public void initBootstrap() {
        //TypefaceProvider.registerDefaultIconSets();
    }

    public void initIconify() {
        /*Iconify
                .with(new FontAwesomeModule())
                .with(new EntypoModule())
                .with(new TypiconsModule())
                .with(new MaterialModule())
                .with(new MaterialCommunityModule())
                .with(new MeteoconsModule())
                .with(new WeathericonsModule())
                .with(new SimpleLineIconsModule())
                .with(new IoniconsModule());*/
    }

    public void initUtils() {
        Initializer.init(this);
        Utils.initialize(this);
    }

    public void initTimber(String TAG) {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                //.methodOffset(5)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag(TAG)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        Timber.plant(new Timber.DebugTree());
    }

    public void initMultiDex() {
        //MultiDex.install(this);
    }

    public void initAndroidNetworking() {
        /*AndroidNetworking.initialize(this);
        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        }*/
    }
}
