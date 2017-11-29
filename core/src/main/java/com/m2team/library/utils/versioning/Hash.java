package com.m2team.library.utils.versioning;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Base64;

import com.m2team.library.utils.FileUtils;
import com.m2team.library.utils.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

    private final static MessageDigest sMD5digest;
    private final static MessageDigest sSHAdigest;

    static {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        sMD5digest = digest;
        digest = null;
        try {
            digest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        sSHAdigest = digest;
    }

    public static String getMD5(final byte[] bytes) {
        if (bytes == null)
            return null;

        if (sMD5digest == null) {
            new NoSuchAlgorithmException().printStackTrace();
            return null;
        } else {
            sMD5digest.reset();
        }

        // calc MD5 Hash
        sMD5digest.update(bytes);
        final byte[] md5sum = sMD5digest.digest();
        final BigInteger bigInt = new BigInteger(1, md5sum);
        final String hash = bigInt.toString(16);
        // Fill to 32 chars
        return String.format("%32s", hash).replace(' ', '0');
    }

    public static String getMD5(final String s) {
        if (s == null)
            return null;
        return getMD5(s.getBytes());
    }

    public static boolean checkMD5(final String md5Hash, final File file) {
        if (TextUtils.isEmpty(md5Hash) || file == null || !FileUtils.isReadable(file)) {
            Log.e("Given String is empty or File is NULL or File is not readable");
            return false;
        }

        final String hash = getMD5(file);
        if (hash == null) {
            Log.e("calculated hash is NULL");
            return false;
        }

        return hash.equalsIgnoreCase(md5Hash);
    }

    public static String getMD5(final File file) {
        if (!FileUtils.isReadable(file))
            return null;

        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.e("Exception while getting FileInputStream", e);
            return null;
        }

        final String hash = getMD5(inputStream);

        try {
            inputStream.close();
        } catch (IOException e) {
            Log.e("Exception on closing input stream", e);
        }

        return hash;

    }

    public static String getMD5(final InputStream inputStream) {
        if (inputStream == null)
            return null;

        if (sMD5digest == null) {
            new NoSuchAlgorithmException().printStackTrace();
            return null;
        } else {
            sMD5digest.reset();
        }

        final byte[] buffer = new byte[8192];
        int read;
        String hash = null;
        try {
            while ((read = inputStream.read(buffer)) > 0)
                sMD5digest.update(buffer, 0, read);
            final byte[] md5sum = sMD5digest.digest();
            final BigInteger bigInt = new BigInteger(1, md5sum);
            hash = bigInt.toString(16);
            // Fill to 32 chars
            hash = String.format("%32s", hash).replace(' ', '0');
        } catch (IOException e) {
            Log.e("Exception on closing MD5 input stream", e);
        }
        return hash;
    }

    public static String getMD5AndResetStream(final InputStream inputStream) {
        if (inputStream == null)
            return null;

        final String hash = getMD5(inputStream);
        try {
            inputStream.reset();
        } catch (IOException e) {
            Log.e("Exception on resetting input stream", e);
        }
        return hash;
    }

    public static String getSHA(final byte[] bytes) {
        if (bytes == null)
            return null;

        if (sSHAdigest == null) {
            new NoSuchAlgorithmException().printStackTrace();
            return null;
        } else {
            sSHAdigest.reset();
        }
        // Create SHA Hash
        sSHAdigest.update(bytes);
        return Base64.encodeToString(sSHAdigest.digest(), Base64.DEFAULT);
    }

    public static String getSHA(final String s) {
        if (s == null)
            return null;
        return getSHA(s.getBytes());
    }

    public static boolean checkSHA(final String shaHash, final File file) {
        if (TextUtils.isEmpty(shaHash) || file == null || !FileUtils.isReadable(file)) {
            Log.e("Given String is NULL or File is NULL or File is not readable");
            return false;
        }

        final String hash = getSHA(file);
        if (hash == null) {
            Log.e("calculated hash is NULL");
            return false;
        }

        return hash.equalsIgnoreCase(shaHash);
    }

    public static String getSHA(final File file) {
        if (!FileUtils.isReadable(file))
            return null;

        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.e("Exception while getting FileInputStream", e);
            return null;
        }

        final String hash = getSHA(inputStream);

        try {
            inputStream.close();
        } catch (IOException e) {
            Log.e("Exception on closing input stream", e);
        }

        return hash;
    }

    public static String getSHA(final InputStream inputStream) {
        if (inputStream == null)
            return null;

        if (sSHAdigest == null) {
            new NoSuchAlgorithmException().printStackTrace();
            return null;
        } else {
            sSHAdigest.reset();
        }

        final byte[] buffer = new byte[8192];
        int read;
        String hash = null;
        try {
            while ((read = inputStream.read(buffer)) > 0)
                sSHAdigest.update(buffer, 0, read);
            hash = Base64.encodeToString(sSHAdigest.digest(), Base64.DEFAULT);
        } catch (IOException e) {
            Log.e("Exception on closing MD5 input stream", e);
        }
        return hash;
    }

    public static String getSHAAndResetStream(final InputStream inputStream) {
        if (inputStream == null)
            return null;

        final String hash = getSHA(inputStream);
        try {
            inputStream.reset();
        } catch (IOException e) {
            Log.e("Exception on resetting input stream", e);
        }
        return hash;
    }

    public static String getAppKeyHash(final Context context, final String packageName) {
        if (context==null || TextUtils.isEmpty(packageName))
            return null;
        if (sSHAdigest == null) {
            new NoSuchAlgorithmException().printStackTrace();
            return null;
        } else {
            sSHAdigest.reset();
        }
        String keyHash = null;
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                sSHAdigest.update(signature.toByteArray());
                keyHash = Base64.encodeToString(sSHAdigest.digest(), Base64.DEFAULT);
                Log.i("Application key SHA hash: " + keyHash);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return keyHash;
    }

    public static boolean isValidSHA1(String s) {
        return s.matches("[a-fA-F0-9]{40}");
    }

    public static boolean isValidMD5(String s) {
        return s.matches("[a-fA-F0-9]{32}");
    }
}
