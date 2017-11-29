package com.m2team.library.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.m2team.library.utils.versioning.DefaultArtifactVersion;
import com.m2team.library.utils.versioning.NewVersionAvailableEvent;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/*
 * Authors:
 * Stan Koshutsky {Stan.Koshutsky@gmail.com}
 */

public class AppUpdateChecker implements Runnable {

    private final Context mContext;

    private final String mPackageName;
    private final String mCurrentVersion;
    private final int mCurrentVersionCode;
    private final long TIME_TO_RETRY_CHECK;

    public static final long SEC = 1000;
    public static final long MIN = 60 * SEC;
    public static final long HOUR = 60 * MIN;
    public static final long DAY = 24 * HOUR;

    private final Object mEventToPost;

    private static boolean isUpdateAvailable = false;

    public AppUpdateChecker(final Context context, final long timeToRetry) {
        this(context, timeToRetry, new NewVersionAvailableEvent());
    }

    public AppUpdateChecker(final Context context, final long timeToRetry, final Object eventToPost) {
        mContext = context.getApplicationContext();
        mPackageName = context.getPackageName();
        String currentVersion = null;
        int currentVersionCode = 0;
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersion = packageInfo.versionName;
            currentVersionCode = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
        }
        this.mCurrentVersion = currentVersion;
        mCurrentVersionCode = currentVersionCode;
        this.TIME_TO_RETRY_CHECK = timeToRetry;
        mEventToPost = eventToPost;
    }

    public AppUpdateChecker(final Context context) {
        this(context, DAY);
    }


    public AppUpdateChecker(final Context context, final Object eventToPost) {
        this(context, DAY, eventToPost);
    }

//    /**
//     * If true it means that the user has explicitly asked to verify if a new update exists so the runnable will show a dialog even if
//     * the app is already updated (otherwise the user couldn't have a feedback from his explicit request).
//     * If false it means that verification is started automatically (the user don't asked explicitly to verify), so a dialog is shown to the user
//     * only if an update really exists.
//     *
//     * @param force if true it forces a dialog visualization (even if already updated)
//     * @return an updater
//     */

    public void check() {
        if (isUpdateAvailable) {
            Log.i(AppUpdateChecker.class.getSimpleName(), "check() isUpdateAvailable: " + isUpdateAvailable);
            EventBus.getDefault().postSticky(mEventToPost);
        } else {
            new Thread(this).start();
        }
    }

    @Override
    public void run() {
        synchronized (mEventToPost) {
            // Extract from the Internet if an update is needed or not
            if (!isUpdateAvailable)
                isUpdateAvailable = isUpdateAvailable();
        }
        if (isUpdateAvailable) {
            EventBus.getDefault().postSticky(mEventToPost);
        }
    }

    private boolean isUpdateAvailable() {

        // Check if there is really an update on the Google Play Store
        final Boolean isUpdateAvailableWeb = isUpdateAvailableWeb();
        if (isUpdateAvailableWeb != null) {
            // if successfully checked the update
            setLastTimeUpdateChecked(mContext);

            return isUpdateAvailableWeb;
        } else
            return false;
    }

    private Boolean isUpdateAvailableWeb() {
        String webVersion = null, webVersionInt = null;
        try {
            final Document doc =
                    Jsoup.connect("https://play.google.com/store/apps/details?id=" + mPackageName + "&hl=en")
                            .timeout(30000)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .referrer("http://www.google.com")
                            .get();

            if (doc != null) {
                webVersion = doc
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();

                //<button class="dropdown-child" data-dropdown-value="83" tabindex="0">Latest Version</button>
                webVersionInt = doc
                        .select("button.dropdown-child")
                        .select("[data-dropdown-value]")
                        .select(":contains(Latest Version)")
                        .first()
                        .attr("data-dropdown-value");
//                if (TextUtils.isDigitsOnly(webVersionInt)){
                Log.i(AppUpdateChecker.class.getSimpleName(), "Version Code Check, web: " + webVersionInt + " vs current: " + mCurrentVersionCode);
//                }
//                for (Element element : buttons) {
//                    Log.i("AppUpdateChecker", "data-dropdown-value: " + element.attr("data-dropdown-value"));
//                }


//            webVersion  = Jsoup.connect("https://play.google.com/store/apps/details?id=" + mPackageName + "&hl=en")
//                    .timeout(30000)
//                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                    .referrer("http://www.google.com")
//                    .get()
//                    .select("div[itemprop=softwareVersion]")
//                    .first()
//                    .ownText();
                Log.i(AppUpdateChecker.class.getSimpleName(), "isUpdateAvailableWeb() webVersion: " + webVersion);
            }
        } catch (Exception e) {
            Log.i(AppUpdateChecker.class.getSimpleName(), "isUpdateAvailableWeb() Exception: " + e.getMessage());
            return null;
        }
        if (TextUtils.isEmpty(webVersionInt) && TextUtils.isEmpty(webVersion))
            return null;
        else {
            // store last update time
            setLastTimeUpdateCheckedVersion(mContext, webVersion);
            if (!TextUtils.isEmpty(webVersionInt) && TextUtils.isDigitsOnly(webVersionInt)) {
                int weVersionCode;
                try {
                    weVersionCode = Integer.valueOf(webVersionInt);
                    return weVersionCode > mCurrentVersionCode;
                } catch (NumberFormatException e) {
                }
            }
            return isNewerVersionAvailable(mCurrentVersion, webVersion);
        }
    }

    public static AlertDialog.Builder showUpdateAvailableDialog(final Activity activity,
                                                                int titleResId,
                                                                int messageResId,
                                                                int labelYesResId,
                                                                int labelNoResId) {

        return showUpdateAvailableDialog(activity,
                activity.getString(titleResId),
                activity.getString(messageResId),
                activity.getString(labelYesResId),
                activity.getString(labelNoResId));
    }

    public static AlertDialog.Builder showUpdateAvailableDialog(final Activity activity,
                                                                final String title,
                                                                final String message,
                                                                final String labelYes,
                                                                final String labelNo) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
//        alertDialogBuilder.setIcon(getCloudDrawable());
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNegativeButton(labelNo, (dialog, id) -> {
            dialog.cancel();
            DisplayUtils.unlockOrientation(activity);
        });
        alertDialogBuilder.setPositiveButton(labelYes, (dialog, id) -> {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));
            dialog.cancel();
            DisplayUtils.unlockOrientation(activity);
        });
        alertDialogBuilder.show();
        return alertDialogBuilder;
    }


    private static boolean isNewerVersionAvailable(final String localVersion, final String webVersion) {
        final DefaultArtifactVersion dafLocalVersion = new DefaultArtifactVersion(localVersion);
        final DefaultArtifactVersion dafWebVersion = new DefaultArtifactVersion(webVersion);
        return !TextUtils.isEmpty(localVersion)
                && !TextUtils.isEmpty(webVersion)
                && dafLocalVersion.compareTo(dafWebVersion) == -1;
    }

    private static long getLastTimeUpdateChecked(Context context) {
        return SharedPrefsHelper.getLong(context, getLastUpdateKey(context), 0L);
    }

    private static void setLastTimeUpdateChecked(Context context) {
        SharedPrefsHelper.put(context, getLastUpdateKey(context), System.currentTimeMillis());
    }

    // version
    private static String getLastTimeUpdateCheckedVersion(Context context) {
        return SharedPrefsHelper.getString(context, getLastUpdateVersionKey(context), null);
    }

    private static void setLastTimeUpdateCheckedVersion(Context context, final String version) {
        SharedPrefsHelper.put(context, getLastUpdateVersionKey(context), version);
    }

    private static String getLastUpdateKey(Context context) {
        return "LastTimeUpdateChecked_" + context.getPackageName();
    }

    private static String getLastUpdateVersionKey(Context context) {
        return "LastTimeUpdateCheckedVersion_" + context.getPackageName();
    }

}