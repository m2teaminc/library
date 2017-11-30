package com.m2team.library.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.widget.Toast;

import com.m2team.library.Constant;
import com.m2team.library.R;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class IntentUtils {

    public static void startActivity(Context context, Class clazz, Bundle extras) {
        try {
            Intent intent = new Intent(context, clazz);
            if (context instanceof AppCompatActivity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }
            if (extras != null)
                intent.putExtras(extras);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void shareApp(Context context, String pkgName) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id=" + pkgName);
            intent.setType("text/plain");

            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_using)));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.no_app_to_handle_intent, Toast.LENGTH_SHORT).show();
        }
    }

    public static void rateApp(Context context, String pkgName) {
        Uri uri = Uri.parse("market://details?id=" + pkgName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        } else {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
            e.printStackTrace();
        }
    }

    public static void goToDeveloperPage(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            intent.setData(Uri.parse("market://search?q=pub:" + Constant.DEVELOPER_NAME));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            intent.setData(Uri.parse("https://play.google.com/store/apps/dev?id=" + Constant.DEVELOPER_ID));
            context.startActivity(intent);
        }
    }

    public static void restartApplication(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        launchIntentForPackage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1234, launchIntentForPackage, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, pendingIntent);
        System.exit(0);
    }

    public static void sendEmail(Context context, String receiverMail, String subject) {

        Intent requestIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", receiverMail, null));
        requestIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        context.startActivity(Intent.createChooser(requestIntent, context.getString(R.string.send_to)));
    }

    public static void shareImage(Context context, Uri uri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent, title));
    }

    public static boolean openUrlInBrowser(final Context context, final String url) {
        boolean isStarted = true;
        final Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url));
        // could be device with no browser installed OR SecurityException
        try {
            context.startActivity(browserIntent);
//        } catch (SecurityException e) {
//            e.printStackTrace();
        } catch (Exception e) {
            isStarted = false;
            e.printStackTrace();
        }
        return isStarted;
    }

    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        final PackageManager pm = context.getPackageManager();
        final List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        final ResolveInfo serviceInfo = resolveInfo.get(0);
        final String packageName = serviceInfo.serviceInfo.packageName;
        final String className = serviceInfo.serviceInfo.name;
        final ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        final Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

    public static void shareByEmail(final Context context,
                                    final String[] receivers,
                                    final String subject,
                                    final String text,
                                    String pickerTitle,
                                    String securityExceptionMessage,
                                    String noAssociatedAppErrorMessage
    ) {
        EMailHelper.sendEmail(context,
                receivers,
                subject,
                text,
                pickerTitle,
                securityExceptionMessage,
                noAssociatedAppErrorMessage);
    }

    public static void shareByEmail(final Context context,
                                    final String subject,
                                    final String text,
                                    String pickerTitle,
                                    String securityExceptionMessage,
                                    String noAssociatedAppErrorMessage
    ) {
        EMailHelper.sendEmail(context,
                new String[]{},
                subject,
                text,
                pickerTitle,
                securityExceptionMessage,
                noAssociatedAppErrorMessage);
    }

    public static void shareByEmail(final Context context,
                                    final String subject,
                                    final String text,
                                    String pickerTitle
    ) {
        EMailHelper.sendEmail(context,
                new String[]{},
                subject,
                text,
                pickerTitle,
                null,
                null);
    }

    public static void shareByEmail(final Context context,
                                    final String subject,
                                    final String text
    ) {
        EMailHelper.sendEmail(context,
                new String[]{},
                subject,
                text,
                null,
                null,
                null);
    }

    public static boolean shareText(final Context context,
                                    final String text) {
        return shareText(context, text, null);
    }

    public static boolean shareText(final Context context,
                                    final String text,
                                    final String pickerTitle) {
        final Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        if (!TextUtils.isEmpty(text))
            sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
        sharingIntent.setType("text/plain");
        return startIntent(context, sharingIntent, pickerTitle);
    }

    public static boolean shareImage(final Context context,
                                     final File imageFile) {
        return shareImage(context, imageFile, null, null);
    }

    public static boolean shareImage(final Context context,
                                     final File imageFile,
                                     final String shareMessage,
                                     final String pickerTitle) {
        final Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
        if (!TextUtils.isEmpty(shareMessage))
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        return startIntent(context, sharingIntent, pickerTitle);
    }

    public static boolean shareAudio(final Context context,
                                     final File messageFileMp3) {
        return shareAudio(context, messageFileMp3, null, null);
    }

    public static boolean shareAudio(final Context context,
                                     final File messageFileMp3,
                                     final String shareMessage,
                                     final String pickerTitle) {
        final Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(messageFileMp3));
        if (!TextUtils.isEmpty(shareMessage))
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        sharingIntent.setType("audio/*");
        return startIntent(context, sharingIntent, pickerTitle);
    }

    private static boolean startIntent(final Context context, final Intent sharingIntent, final String pickerTitle) {
        boolean isSharingIntentStarted = true;
        try {
            if (TextUtils.isEmpty(pickerTitle))
                context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
            else
                context.startActivity(Intent.createChooser(sharingIntent, pickerTitle));
        } catch (ActivityNotFoundException e) {
//                Toast.makeText(context, noAssociatedAppErrorMessage, Toast.LENGTH_LONG).show();
            isSharingIntentStarted = false;
        } catch (SecurityException e) {
//                Toast.makeText(context, securityExceptionMessage, Toast.LENGTH_LONG).show();
            isSharingIntentStarted = false;
        }
        return isSharingIntentStarted;
    }

    public static boolean dialPhoneNumber(final Context context, String phoneNumber) {
        boolean isStarted = true;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        try {
            phoneNumber = PhoneNumberUtils.stripSeparators(phoneNumber);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        intent.setData(Uri.parse("tel:" + phoneNumber));
        // could be device with no browser installed OR SecurityException
        try {
            if (intent.resolveActivity(context.getPackageManager()) != null)
                context.startActivity(intent);
        } catch (Throwable e) {
            isStarted = false;
            e.printStackTrace();
        }
        return isStarted;
    }

    public static boolean showMap(final Context context, final Uri geoLocation) {
        boolean isStarted = true;
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        // could be device with no browser installed OR SecurityException
        try {
            if (intent.resolveActivity(context.getPackageManager()) != null)
                context.startActivity(intent);
//        } catch (SecurityException e) {
//            e.printStackTrace();
        } catch (Exception e) {
            isStarted = false;
            e.printStackTrace();
        }
        return isStarted;
    }

    public static Intent getIntentForPackage(final Context appContext, final String targetPackage) {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> pkgAppsList = appContext.getPackageManager().queryIntentActivities(mainIntent, 0);
//        final List<ResolveInfo> pkgAppsList = appContext.getPackageManager().getInstalledPackages(0);
        int count = 0;
        String intentPackage = null;
        for (ResolveInfo info : pkgAppsList) {
            if (info.activityInfo.packageName.toLowerCase(Locale.US).contains(targetPackage)
                    || info.activityInfo.name.toLowerCase(Locale.US).contains(targetPackage)) {
                intentPackage = info.activityInfo.packageName;
                count++;
            }
        }
        if (count == 1) {
            final Intent intent = new Intent();
            intent.setPackage(intentPackage);
            return intent;
        }
        return null;
    }

}
