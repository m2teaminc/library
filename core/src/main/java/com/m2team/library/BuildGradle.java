package com.m2team.library;

/**
 * @author Min Hoang
 * @since 11/30/2017.
 */

public class BuildGradle {
    //Android support libraries
//        implementation "com.android.support:support-v4:$supportLibraryVersion"
//        implementation "com.android.support:appcompat-v7:$supportLibraryVersion"
//        implementation "com.android.support:recyclerview-v7:$supportLibraryVersion"
//        implementation "com.android.support:cardview-v7:$supportLibraryVersion"
//        implementation "com.android.support:design:$supportLibraryVersion"
//        implementation "com.android.support:support-vector-drawable:$supportLibraryVersion"
//        implementation "com.android.support:animated-vector-drawable:$supportLibraryVersion"
    //implementation 'com.android.support.constraint:constraint-layout:1.0.2'

    //Nine old android
    //implementation 'com.nineoldandroids:library:2.4.0'

    //Kotlin
    //implementation "org.jetbrains.kotlin:kotlin-stdlib-jre7:$kotlin_version"

    //ButterKnife
        /*implementation "com.jakewharton:butterknife:$butterKnifeVersion"
        annotationProcessor "com.jakewharton:butterknife-compiler:$butterKnifeVersion"*/

    //Json parser
//        implementation "com.google.code.gson:gson:$gsonVersion"

    //Image loader
//        implementation "com.github.bumptech.glide:glide:$glideVersion"

    //Font
//        implementation "uk.co.chrisjenx:calligraphy:$calligraphyVersion"

    //Logger
//        implementation "com.jakewharton.timber:timber:$timberVersion"
//        implementation "com.orhanobut:logger:$loggerVersion"

    //Custom toast
//        implementation "com.sdsmdg.tastytoast:tastytoast:$tastyToastVersion"
    //implementation 'com.github.GrenderG:Toasty:1.2.5'

    //Jsoup
//        implementation "org.jsoup:jsoup:$jsoupVersion"

    //Secure prefs
//        implementation 'com.scottyab:secure-preferences-lib:0.1.4'

    //SpongeCastle (adds Security algorithms)
//        implementation 'com.madgag.spongycastle:core:1.54.0.0'
//        implementation 'com.madgag.spongycastle:prov:1.54.0.0'

    //Joda time
//        implementation "joda-time:joda-time:$jodaTimeVersion"
//        implementation 'com.fatboyindustrial.gson-jodatime-serialisers:gson-jodatime-serialisers:1.6.0'

    //Eventbus
//        implementation "org.greenrobot:eventbus:$evenBusVersion"

    //Admob Adapter
    //implementation 'com.github.clockbyte:admobadapter:1.4.1'

    //Mosby (MVP)
        /*implementation "com.hannesdorfmann.mosby3:mvp:$mosbyMvpVersion" // Plain MVP
        implementation "com.hannesdorfmann.mosby3:mvp-lce:$mosbyMvpVersion" // MVP + ViewState + LCE Views*/

    //Thirty inch (MVP Lib)
        /*implementation "net.grandcentrix.thirtyinch:thirtyinch:$thirtyinchVersion"
        implementation "net.grandcentrix.thirtyinch:thirtyinch-rx:$thirtyinchVersion"*/

    //Fabric
        /*implementation('com.crashlytics.sdk.android:crashlytics:2.6.8') {
                transitive = true;
        }*/

    //Fresco
    //implementation "com.facebook.fresco:fresco:$frescoVersion"

    //Firebase
        /*implementation "com.google.firebase:firebase-messaging:$googleServicesVersion"
        implementation "com.google.firebase:firebase-core:$googleServicesVersion"
        implementation "com.google.firebase:firebase-config:$googleServicesVersion"
        implementation "com.google.firebase:firebase-ads:$googleServicesVersion"
        implementation "com.google.firebase:firebase-storage:$googleServicesVersion"*/

    // Google
        /*implementation "com.google.maps.android:android-maps-utils:$googleMapUtilsVersion"
        implementation "com.google.android.gms:play-services-ads:$googleServicesVersion"
        implementation "com.google.android.gms:play-services-analytics:$googleServicesVersion"
        implementation "com.google.android.gms:play-services-maps:$googleServicesVersion"
        implementation "com.google.android.gms:play-services-location:$googleServicesVersion"
        implementation "com.google.android.gms:play-services-places:$googleServicesVersion"
        */

    //Multidex
    //implementation "com.android.support:multidex:$multidexVersion"

    //Retrofit
        /*implementation "com.squareup.retrofit2:retrofit:$retrofitVersion"
        implementation "com.squareup.retrofit2:converter-gson:$retrofitVersion"
        implementation 'com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0'
        implementation 'com.squareup.okhttp3:logging-interceptor:3.4.1'*/

    //Facebook Stetho for intercept network
        /*implementation "com.facebook.stetho:stetho:$facebookStethoVersion"
        implementation "com.facebook.stetho:stetho-okhttp3:$facebookStethoVersion"*/

    //RX Android
        /*implementation "io.reactivex.rxjava2:rxjava:$rxjava2Version"
        implementation "io.reactivex.rxjava2:rxandroid:$rxandroidVersion"*/

    //Dependency injection
        /*implementation "com.google.dagger:dagger:$dagger2Version"
        annotationProcessor "com.google.dagger:dagger-compiler:$dagger2Version"
        provided 'javax.annotation:jsr250-api:1.0'*/

    //Room ORM DB
        /*implementation "android.arch.persistence.room:runtime:$roomDbVersion"
        annotationProcessor "android.arch.persistence.room:compiler:$roomDbVersion"
        implementation "android.arch.persistence.room:rxjava2:$roomDbVersion"*/

    //Leak canary
        /*debugCompile "com.squareup.leakcanary:leakcanary-android:$leakCanaryVersion"
        releaseCompile "com.squareup.leakcanary:leakcanary-android-no-op:$leakCanaryVersion"
        testCompile "com.squareup.leakcanary:leakcanary-android-no-op:$leakCanaryVersion"*/

    //Ice pick - saving and restoring BundleSaveInstance
    //implementation "frankiesardo:icepick:$icePickVersion"
    //provided "frankiesardo:icepick-processor:$icePickVersion"

    //Parcelable - maybe conflict with Icepick, Auto-Parcel is alternative lib
    //implementation "org.parceler:parceler-api:$parcelableVersion"
    //annotationProcessor "org.parceler:parceler:$parcelableVersion"

    //Auto Parcel
    //apt 'frankiesardo:auto-parcel:1.0.3'

    //Gesture detector
    //implementation 'com.github.nisrulz:sensey:1.7.0'

    //Swipe view
    //implementation "com.mindorks:placeholderview:$placeholderviewVersion"

    //Round, circle image view
    //implementation 'com.makeramen:roundedimageview:2.3.0'

    //Material UI Kit
    //implementation 'com.github.rey5137:material:1.2.2'

    //Bootstrap UI Kit
    //implementation 'com.beardedhen:androidbootstrap:2.3.1'

    //Material EditText
    //implementation "com.rengwuxian.materialedittext:library:$materialEditTextVersion"

    //Flower material drawer
    //implementation 'com.mxn.soul:flowingdrawer-core:2.0.0'

    //Material navigation tab bar
    //implementation 'devlight.io:navigationtabbar:1.2.5'

    //Date time picker
    //implementation 'com.wdullaer:materialdatetimepicker:3.3.0'

    //Tag Group
    //implementation 'me.gujun.android.taggroup:library:1.4@aar'

    //Icon
        /*implementation "com.joanzapata.iconify:android-iconify-fontawesome:$iconifyVersion" // (v4.5)
        implementation "com.joanzapata.iconify:android-iconify-entypo:$iconifyVersion" // (v3,2015)
        implementation "com.joanzapata.iconify:android-iconify-typicons:$iconifyVersion" // (v2.0.7)
        implementation "com.joanzapata.iconify:android-iconify-material:$iconifyVersion" // (v2.0.0)
        implementation "com.joanzapata.iconify:android-iconify-material-community:$iconifyVersion" // (v1.4.57)
        implementation "com.joanzapata.iconify:android-iconify-meteocons:$iconifyVersion" // (latest)
        implementation "com.joanzapata.iconify:android-iconify-weathericons:$iconifyVersion" // (v2.0)
        implementation "com.joanzapata.iconify:android-iconify-simplelineicons:$iconifyVersion" // (v1.0.0)
        implementation "com.joanzapata.iconify:android-iconify-ionicons:$iconifyVersion" // (v2.0.1)*/

    //Dependencies for local unit tests
        /*testCompile "junit:junit:$ext.junitVersion"
        testCompile "org.mockito:mockito-core:$mockitoVersion"
        testAnnotationProcessor "com.google.dagger:dagger-compiler:$dagger2Version"*/

    //UI Testing
        /*androidTestCompile "com.android.support.test.espresso:espresso-core:$espressoVersion"
        androidTestCompile "com.android.support.test.espresso:espresso-intents:$espressoVersion"
        androidTestCompile "org.mockito:mockito-core:$mockitoVersion"
        androidTestAnnotationProcessor "com.google.dagger:dagger-compiler:$dagger2Version"*/
}
