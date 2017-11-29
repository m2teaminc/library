package com.m2team.library.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EMailHelper {

    public static boolean isValidEmail(final String email) {
        if (TextUtils.isEmpty(email))
            return false;
        // https://en.wikipedia.org/wiki/Domain_Name_System
        // a label may contain zero to 63 characters
        // subdivisions may have up to 127 levels
        // but total domain name 253 chars max
        final String expression = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,63}";
        final Pattern p = Pattern.compile(expression);
        final Matcher m = p.matcher(email);
        if (m.matches()) {
            final String domainPart = expression.substring(expression.indexOf("@") + 1);
            final String[] domainParts = domainPart.split(".");
            if (domainParts.length < 127) {
                // check labels
                for (String domainLabel : domainParts) {
                    if (domainLabel.length() < 1 || domainLabel.length() > 63)
                        return false;
                }
                return domainPart.length() < 254;
            } else
                return false;
        } else
            return false;
    }

    private final static String[] knownPackages = new String[]{
            "com.google.android.gm",
            "com.google.android.apps.inbox",
            "com.appple.app.email",
            "ru.mail.mailapp",
            "com.microsoft.office.outlook",
            "com.my.mail",
            "com.yahoo.mobile.client.android.mail",
            "com.cloudmagic.mail",
            "cz.seznam.email",
            "com.email.email",
            "com.trtf.blue",
            "me.bluemail.mail",
            "com.fsck.k9",
            "com.mailboxapp",
            "com.syntomo.email",
            "org.kman.aquamail",
            "com.mobincube.android.sc_15ibz",
            "ru.yandex.mail",
            "net.daum.android.solmail",
            "com.boxer.email",
            "com.aol.mobile.aolapp",
            "com.mobincube.android.sc_gaz7l",
            "com.xiaomistudio.tools.finalmail",
            "com.mail.emails",
            "co.itspace.emailproviders",
            "de.gmx.mobile.android.mail",
            "com.yahoo.mobile.client.android.im",
            "com.mail.mobile.android.mail",
            "com.asus.email",
            "com.maildroid",
            "com.wemail",
            "de.web.mobile.android.mail",
            "com.onegravity.k10.free",
            "com.dicklucifer.email",
            "de.freenet.mail",
            "com.qs.enhancedemail",
            "com.feistapps.anonymousemail",
            "com.onegravity.k10.pro2",
            "com.gloxandro.birdmail",
            "com.kaitenmail",
            "com.android.email",
            "com.sec.android.email",
            "com.htc.android.mail"};

    private static boolean applyKnownPackage(final Context context, final Intent emailIntent) {
        final List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(emailIntent, 0);
        if (resInfo.size() == 0)
            return false;
        if (resInfo.size() == 1) {
            emailIntent.setPackage(resInfo.get(0).activityInfo.packageName);
            return true;
        }
        int count = 0;
        String targetPackage = null;
        for (ResolveInfo info : resInfo) {
            for (String sharePackage : knownPackages) {
                if (info.activityInfo.packageName.toLowerCase(Locale.US).contains(sharePackage)
                        || info.activityInfo.name.toLowerCase(Locale.US).contains(sharePackage)) {
                    targetPackage = info.activityInfo.packageName;
                    count++;
                }
            }
        }
        if (count == 1)
            emailIntent.setPackage(targetPackage);
        return count == 1;
    }


    private static String EMH_DEFAULT_PICKER_TITLE = "Send EMail using:";
    private static String EMH_DEFAULT_SECURITY_EXCEPTION_ERROR_MESSAGE = "Sending EMail has been forbidden by permissions";
    private static String EMH_DEFAULT_NO_ASSOCIATED_APP_ERROR_MESSAGE = "No EMail app has been found";

    public static boolean sendEmail(Context context,
                                    final String receiver,
                                    final String subject,
                                    final String text) {
        return sendEmail(context,
                new String[]{receiver},
                subject,
                text,
                null,
                null,
                null);
    }

    public static boolean sendEmail(Context context,
                                    final String[] receivers,
                                    final String subject,
                                    final String text) {
        return sendEmail(context,
                receivers,
                subject,
                text,
                null,
                null,
                null);
    }

    public static boolean sendEmail(final Context context,
                                    final String receiver,
                                    final String subject,
                                    final String text,
                                    final String pickerTitle,
                                    final String securityExceptionMessage,
                                    final String noAssociatedAppErrorMessage) {
        return sendEmail(context,
                !TextUtils.isEmpty(receiver) ? new String[]{receiver} : null,
                subject,
                text,
                pickerTitle,
                securityExceptionMessage,
                noAssociatedAppErrorMessage);
    }


    public static boolean sendEmail(final Context context,
                                    final String[] receivers,
                                    final String subject,
                                    final String text,
                                    String pickerTitle,
                                    String securityExceptionMessage,
                                    String noAssociatedAppErrorMessage) {
        if (TextUtils.isEmpty(pickerTitle))
            pickerTitle = EMH_DEFAULT_PICKER_TITLE;
        if (TextUtils.isEmpty(securityExceptionMessage))
            securityExceptionMessage = EMH_DEFAULT_SECURITY_EXCEPTION_ERROR_MESSAGE;
        if (TextUtils.isEmpty(noAssociatedAppErrorMessage))
            noAssociatedAppErrorMessage = EMH_DEFAULT_NO_ASSOCIATED_APP_ERROR_MESSAGE;


        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        if (receivers != null && receivers.length > 0)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers);
        if (!TextUtils.isEmpty(subject))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (!TextUtils.isEmpty(text))
            emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        emailIntent.setData(Uri.parse("mailto:"));
//        emailIntent.setType("text/plain");
        emailIntent.setType("message/rfc822");

        boolean isIntentSent = false;
        if (applyKnownPackage(context, emailIntent)) {
            //Toast.makeText(context, noAssociatedAppErrorMessage, Toast.LENGTH_LONG).show();
            try {
                context.startActivity(Intent.createChooser(emailIntent, pickerTitle));
                isIntentSent = true;
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, noAssociatedAppErrorMessage, Toast.LENGTH_LONG).show();
            } catch (SecurityException e) {
                Toast.makeText(context, securityExceptionMessage, Toast.LENGTH_LONG).show();
            }
        } else if (emailIntent.resolveActivity(context.getPackageManager()) != null)
            try {
                context.startActivity(emailIntent);
                isIntentSent = true;
            } catch (SecurityException e) {
                Toast.makeText(context, securityExceptionMessage, Toast.LENGTH_LONG).show();
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, noAssociatedAppErrorMessage, Toast.LENGTH_LONG).show();
            }
        else
            try {
                context.startActivity(Intent.createChooser(emailIntent, pickerTitle));
                isIntentSent = true;
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, noAssociatedAppErrorMessage, Toast.LENGTH_LONG).show();
            } catch (SecurityException e) {
                Toast.makeText(context, securityExceptionMessage, Toast.LENGTH_LONG).show();
            }
        return isIntentSent;
    }


    //
    // mail with attachments section
    //

    public static boolean sendEmail(Context context,
                                    final String receiver,
                                    final String subject,
                                    final String text,
                                    final File... files) {
        if (files == null || files.length == 0) {
            return sendEmail(context,
                    new String[]{receiver},
                    subject,
                    text,
                    null,
                    null,
                    null);
        } else {
            return sendEmail(context,
                    new String[]{receiver},
                    subject,
                    text,
                    null,
                    null,
                    null,
                    files);
        }
    }

    public static boolean sendEmail(Context context,
                                    final String[] receivers,
                                    final String subject,
                                    final String text,
                                    final File... files) {
        if (files == null || files.length == 0) {
            return sendEmail(context,
                    receivers,
                    subject,
                    text,
                    null,
                    null,
                    null);
        } else {
            return sendEmail(context,
                    receivers,
                    subject,
                    text,
                    null,
                    null,
                    null,
                    files);
        }
    }

    public static boolean sendEmail(Context context,
                                    final String receiver,
                                    final String subject,
                                    final String text,
                                    final String pickerTitle,
                                    final String securityExceptionMessage,
                                    final String noAssociatedAppErrorMessage,
                                    final File... files) {
        if (files == null || files.length == 0) {
            return sendEmail(context,
                    new String[]{receiver},
                    subject,
                    text,
                    pickerTitle,
                    securityExceptionMessage,
                    noAssociatedAppErrorMessage);
        } else {
            return sendEmail(context,
                    new String[]{receiver},
                    subject,
                    text,
                    pickerTitle,
                    securityExceptionMessage,
                    noAssociatedAppErrorMessage,
                    files);
        }
    }

    public static boolean sendEmail(Context context,
                                    final String[] receivers,
                                    final String subject,
                                    final String text,
                                    String pickerTitle,
                                    String securityExceptionMessage,
                                    String noAssociatedAppErrorMessage,
                                    final File... files) {
        if (files == null || files.length == 0) {
            return sendEmail(context,
                    receivers,
                    subject,
                    text,
                    pickerTitle,
                    securityExceptionMessage,
                    noAssociatedAppErrorMessage);
        }
        if (TextUtils.isEmpty(pickerTitle))
            pickerTitle = EMH_DEFAULT_PICKER_TITLE;
        if (TextUtils.isEmpty(securityExceptionMessage))
            securityExceptionMessage = EMH_DEFAULT_SECURITY_EXCEPTION_ERROR_MESSAGE;
        if (TextUtils.isEmpty(noAssociatedAppErrorMessage))
            noAssociatedAppErrorMessage = EMH_DEFAULT_NO_ASSOCIATED_APP_ERROR_MESSAGE;

        final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        final ArrayList<Uri> uris = new ArrayList<>();
        //convert from files to Android friendly Parcelable Uri's
        for (File file : files)
            uris.add(Uri.fromFile(file));
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        if (receivers != null && receivers.length > 0)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers);
        if (!TextUtils.isEmpty(subject))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (!TextUtils.isEmpty(text))
            emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        emailIntent.setData(Uri.parse("mailto:"));
//        emailIntent.setType("text/plain");
        emailIntent.setType("message/rfc822");

        boolean isIntentSent = false;

        if (applyKnownPackage(context, emailIntent)) {
            //Toast.makeText(context, noAssociatedAppErrorMessage, Toast.LENGTH_LONG).show();
            try {
                context.startActivity(Intent.createChooser(emailIntent, pickerTitle));
                isIntentSent = true;
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, noAssociatedAppErrorMessage, Toast.LENGTH_LONG).show();
            } catch (SecurityException e) {
                Toast.makeText(context, securityExceptionMessage, Toast.LENGTH_LONG).show();
            }
        } else if (emailIntent.resolveActivity(context.getPackageManager()) != null)
            try {
                context.startActivity(emailIntent);
                isIntentSent = true;
            } catch (SecurityException e) {
                Toast.makeText(context, securityExceptionMessage, Toast.LENGTH_LONG).show();
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, noAssociatedAppErrorMessage, Toast.LENGTH_LONG).show();
            }
        else
            try {
                context.startActivity(Intent.createChooser(emailIntent, pickerTitle));
                isIntentSent = true;
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, noAssociatedAppErrorMessage, Toast.LENGTH_LONG).show();
            } catch (SecurityException e) {
                Toast.makeText(context, securityExceptionMessage, Toast.LENGTH_LONG).show();
            }

//        if (!applyKnownPackage(context, emailIntent)) {
//            Toast.makeText(context, noAssociatedAppErrorMessage, Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        try {
//            context.startActivity(Intent.createChooser(emailIntent, pickerTitle));
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(context, noAssociatedAppErrorMessage, Toast.LENGTH_LONG).show();
//        } catch (SecurityException e) {
//            Toast.makeText(context, securityErrorMessage, Toast.LENGTH_LONG).show();
//        }
        return isIntentSent;
    }


}
