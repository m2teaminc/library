package com.m2team.library.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.SequenceInputStream;
import java.io.SyncFailedException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    private static final String TAG = FileUtils.class.getSimpleName();

    private static final String LINE_SEP = System.getProperty("line.separator");

    public static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    public static boolean isFileExists(final String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    public static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }

    public static boolean rename(final String filePath, final String newName) {
        return rename(getFileByPath(filePath), newName);
    }

    public static boolean rename(final File file, final String newName) {
        // ??????false
        if (file == null) return false;
        // ???????false
        if (!file.exists()) return false;
        // ?????????false
        if (isSpace(newName)) return false;
        // ???????????true
        if (newName.equals(file.getName())) return true;
        File newFile = new File(file.getParent() + File.separator + newName);
        // ?????????????false
        return !newFile.exists()
                && file.renameTo(newFile);
    }

    public static boolean isDir(final String dirPath) {
        return isDir(getFileByPath(dirPath));
    }

    public static boolean isDir(final File file) {
        return isFileExists(file) && file.isDirectory();
    }

    public static boolean isFile(final String filePath) {
        return isFile(getFileByPath(filePath));
    }

    public static boolean isFile(final File file) {
        return isFileExists(file) && file.isFile();
    }

    public static boolean createOrExistsDir(final String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    public static boolean createOrExistsDir(final File file) {
        // ???????????true???????false?????????????
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    public static boolean createOrExistsFile(final String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    public static boolean createOrExistsFile(final File file) {
        if (file == null) return false;
        // ???????????true???????false
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createFileByDeleteOldFile(final File file) {
        if (file == null) return false;
        // ????????????false
        if (file.exists() && !file.delete()) return false;
        // ????????false
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean copyOrMoveDir(final String srcDirPath, final String destDirPath, final boolean isMove) {
        return copyOrMoveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath), isMove);
    }

    private static boolean copyOrMoveDir(final File srcDir, final File destDir, final boolean isMove) {
        if (srcDir == null || destDir == null) return false;
        // ??????????????false????????????????
        // srcPath : F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res
        // destPath: F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res1
        // ?????????????????????????????
        String srcPath = srcDir.getPath() + File.separator;
        String destPath = destDir.getPath() + File.separator;
        if (destPath.contains(srcPath)) return false;
        // ???????????????false
        if (!srcDir.exists() || !srcDir.isDirectory()) return false;
        // ?????????false
        if (!createOrExistsDir(destDir)) return false;
        File[] files = srcDir.listFiles();
        for (File file : files) {
            File oneDestFile = new File(destPath + file.getName());
            if (file.isFile()) {
                // ????????false
                if (!copyOrMoveFile(file, oneDestFile, isMove)) return false;
            } else if (file.isDirectory()) {
                // ????????false
                if (!copyOrMoveDir(file, oneDestFile, isMove)) return false;
            }
        }
        return !isMove || deleteDir(srcDir);
    }

    private static boolean copyOrMoveFile(final String srcFilePath, final String destFilePath, final boolean isMove) {
        return copyOrMoveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath), isMove);
    }

    private static boolean copyOrMoveFile(final File srcFile, final File destFile, final boolean isMove) {
        if (srcFile == null || destFile == null) return false;
        // ???????????????false
        if (!srcFile.exists() || !srcFile.isFile()) return false;
        // ?????????????false
        if (destFile.exists() && destFile.isFile()) return false;
        // ?????????false
        if (!createOrExistsDir(destFile.getParentFile())) return false;
        try {
            return FileIOUtils.writeFileFromIS(destFile, new FileInputStream(srcFile), false)
                    && !(isMove && !deleteFile(srcFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyDir(final String srcDirPath, final String destDirPath) {
        return copyDir(getFileByPath(srcDirPath), getFileByPath(destDirPath));
    }

    public static boolean copyDir(final File srcDir, final File destDir) {
        return copyOrMoveDir(srcDir, destDir, false);
    }

    public static boolean copyFile(final String srcFilePath, final String destFilePath) {
        return copyFile(getFileByPath(srcFilePath), getFileByPath(destFilePath));
    }

    public static boolean copyFile(final File srcFile, final File destFile) {
        return copyOrMoveFile(srcFile, destFile, false);
    }

    public static boolean moveDir(final String srcDirPath, final String destDirPath) {
        return moveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath));
    }

    public static boolean moveDir(final File srcDir, final File destDir) {
        return copyOrMoveDir(srcDir, destDir, true);
    }

    public static boolean moveFile(final String srcFilePath, final String destFilePath) {
        return moveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath));
    }

    public static boolean moveFile(final File srcFile, final File destFile) {
        return copyOrMoveFile(srcFile, destFile, true);
    }

    public static boolean deleteDir(final String dirPath) {
        return deleteDir(getFileByPath(dirPath));
    }

    public static boolean deleteDir(final File dir) {
        if (dir == null) return false;
        // ???????true
        if (!dir.exists()) return true;
        // ??????false
        if (!dir.isDirectory()) return false;
        // ???????????
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

    public static boolean deleteFile(final String srcFilePath) {
        return deleteFile(getFileByPath(srcFilePath));
    }

    public static boolean deleteFile(final File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    public static boolean deleteFilesInDir(final String dirPath) {
        return deleteFilesInDir(getFileByPath(dirPath));
    }

    public static boolean deleteFilesInDir(final File dir) {
        if (dir == null) return false;
        // ???????true
        if (!dir.exists()) return true;
        // ??????false
        if (!dir.isDirectory()) return false;
        // ???????????
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return true;
    }

    public static List<File> listFilesInDir(final String dirPath, final boolean isRecursive) {
        return listFilesInDir(getFileByPath(dirPath), isRecursive);
    }

    public static List<File> listFilesInDir(final File dir, final boolean isRecursive) {
        if (!isDir(dir)) return null;
        if (isRecursive) return listFilesInDir(dir);
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            Collections.addAll(list, files);
        }
        return list;
    }

    public static List<File> listFilesInDir(final String dirPath) {
        return listFilesInDir(getFileByPath(dirPath));
    }

    public static List<File> listFilesInDir(final File dir) {
        if (!isDir(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                list.add(file);
                if (file.isDirectory()) {
                    List<File> fileList = listFilesInDir(file);
                    if (fileList != null) {
                        list.addAll(fileList);
                    }
                }
            }
        }
        return list;
    }

    public static List<File> listFilesInDirWithFilter(final String dirPath, final String suffix, final boolean isRecursive) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), suffix, isRecursive);
    }

    public static List<File> listFilesInDirWithFilter(final File dir, final String suffix, final boolean isRecursive) {
        if (isRecursive) return listFilesInDirWithFilter(dir, suffix);
        if (dir == null || !isDir(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    public static List<File> listFilesInDirWithFilter(final String dirPath, final String suffix) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), suffix);
    }

    public static List<File> listFilesInDirWithFilter(final File dir, final String suffix) {
        if (dir == null || !isDir(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                    list.add(file);
                }
                if (file.isDirectory()) {
                    list.addAll(listFilesInDirWithFilter(file, suffix));
                }
            }
        }
        return list;
    }

    public static List<File> listFilesInDirWithFilter(final String dirPath, final FilenameFilter filter, final boolean isRecursive) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, isRecursive);
    }

    public static List<File> listFilesInDirWithFilter(final File dir, final FilenameFilter filter, final boolean isRecursive) {
        if (isRecursive) return listFilesInDirWithFilter(dir, filter);
        if (dir == null || !isDir(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (filter.accept(file.getParentFile(), file.getName())) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    public static List<File> listFilesInDirWithFilter(final String dirPath, final FilenameFilter filter) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter);
    }

    public static List<File> listFilesInDirWithFilter(final File dir, final FilenameFilter filter) {
        if (dir == null || !isDir(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (filter.accept(file.getParentFile(), file.getName())) {
                    list.add(file);
                }
                if (file.isDirectory()) {
                    list.addAll(listFilesInDirWithFilter(file, filter));
                }
            }
        }
        return list;
    }

    public static List<File> searchFileInDir(final String dirPath, final String fileName) {
        return searchFileInDir(getFileByPath(dirPath), fileName);
    }

    public static List<File> searchFileInDir(final File dir, final String fileName) {
        if (dir == null || !isDir(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.getName().toUpperCase().equals(fileName.toUpperCase())) {
                    list.add(file);
                }
                if (file.isDirectory()) {
                    list.addAll(searchFileInDir(file, fileName));
                }
            }
        }
        return list;
    }

    public static long getFileLastModified(final String filePath) {
        return getFileLastModified(getFileByPath(filePath));
    }

    public static long getFileLastModified(final File file) {
        if (file == null) return -1;
        return file.lastModified();
    }

    public static String getFileCharsetSimple(final String filePath) {
        return getFileCharsetSimple(getFileByPath(filePath));
    }

    public static String getFileCharsetSimple(final File file) {
        int p = 0;
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            p = (is.read() << 8) + is.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(is);
        }
        switch (p) {
            case 0xefbb:
                return "UTF-8";
            case 0xfffe:
                return "Unicode";
            case 0xfeff:
                return "UTF-16BE";
            default:
                return "GBK";
        }
    }

    public static int getFileLines(final String filePath) {
        return getFileLines(getFileByPath(filePath));
    }

    public static int getFileLines(final File file) {
        int count = 1;
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            int readChars;
            if (LINE_SEP.endsWith("\n")) {
                while ((readChars = is.read(buffer, 0, 1024)) != -1) {
                    for (int i = 0; i < readChars; ++i) {
                        if (buffer[i] == '\n') ++count;
                    }
                }
            } else {
                while ((readChars = is.read(buffer, 0, 1024)) != -1) {
                    for (int i = 0; i < readChars; ++i) {
                        if (buffer[i] == '\r') ++count;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(is);
        }
        return count;
    }

    public static String getDirSize(final String dirPath) {
        return getDirSize(getFileByPath(dirPath));
    }

    public static String getDirSize(final File dir) {
        long len = getDirLength(dir);
        return len == -1 ? "" : byte2FitMemorySize(len);
    }

    public static String getFileSize(final String filePath) {
        return getFileSize(getFileByPath(filePath));
    }

    public static String getFileSize(final File file) {
        long len = getFileLength(file);
        return len == -1 ? "" : byte2FitMemorySize(len);
    }
    public static long getDirLength(final String dirPath) {
        return getDirLength(getFileByPath(dirPath));
    }

    public static long getDirLength(final File dir) {
        if (!isDir(dir)) return -1;
        long len = 0;
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    len += getDirLength(file);
                } else {
                    len += file.length();
                }
            }
        }
        return len;
    }

    public static long getFileLength(final String filePath) {
        return getFileLength(getFileByPath(filePath));
    }

    public static long getFileLength(final File file) {
        if (!isFile(file)) return -1;
        return file.length();
    }

    public static String getFileMD5ToString(final String filePath) {
        File file = isSpace(filePath) ? null : new File(filePath);
        return getFileMD5ToString(file);
    }

    public static byte[] getFileMD5(final String filePath) {
        File file = isSpace(filePath) ? null : new File(filePath);
        return getFileMD5(file);
    }

    public static String getFileMD5ToString(final File file) {
        return bytes2HexString(getFileMD5(file));
    }

    public static byte[] getFileMD5(final File file) {
        if (file == null) return null;
        DigestInputStream dis = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            dis = new DigestInputStream(fis, md);
            byte[] buffer = new byte[1024 * 256];
            while (true) {
                if (!(dis.read(buffer) > 0)) break;
            }
            md = dis.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(dis);
        }
        return null;
    }

    public static String getDirName(final File file) {
        if (file == null) return null;
        return getDirName(file.getPath());
    }

    public static String getDirName(final String filePath) {
        if (isSpace(filePath)) return filePath;
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? "" : filePath.substring(0, lastSep + 1);
    }

    public static String getFileName(final File file) {
        if (file == null) return null;
        return getFileName(file.getPath());
    }

    public static String getFileName(final String filePath) {
        if (isSpace(filePath)) return filePath;
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
    }

    public static String getFileNameNoExtension(final File file) {
        if (file == null) return null;
        return getFileNameNoExtension(file.getPath());
    }

    public static String getFileNameNoExtension(final String filePath) {
        if (isSpace(filePath)) return filePath;
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastSep == -1) {
            return (lastPoi == -1 ? filePath : filePath.substring(0, lastPoi));
        }
        if (lastPoi == -1 || lastSep > lastPoi) {
            return filePath.substring(lastSep + 1);
        }
        return filePath.substring(lastSep + 1, lastPoi);
    }

    public static String getFileExtension(final File file) {
        if (file == null) return null;
        return getFileExtension(file.getPath());
    }

    public static String getFileExtension(final String filePath) {
        if (isSpace(filePath)) return filePath;
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastPoi == -1 || lastSep >= lastPoi) return "";
        return filePath.substring(lastPoi + 1);
    }

    ///////////////////////////////////////////////////////////////////////////
    // copy from ConvertUtils
    ///////////////////////////////////////////////////////////////////////////

    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    @SuppressLint("DefaultLocale")
    private static String byte2FitMemorySize(final long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < 1024) {
            return String.format("%.3fB", (double) byteNum + 0.0005);
        } else if (byteNum < 1048576) {
            return String.format("%.3fKB", (double) byteNum / 1024 + 0.0005);
        } else if (byteNum < 1073741824) {
            return String.format("%.3fMB", (double) byteNum / 1048576 + 0.0005);
        } else {
            return String.format("%.3fGB", (double) byteNum / 1073741824 + 0.0005);
        }
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    public static File createFileDir(Context context, String dirName) {
        String filePath;
        // ?SD?????????????data???
        if (isMountedSDCard()) {
            // SD???
            filePath = Environment.getExternalStorageDirectory() + File.separator + dirName;
        } else {
            filePath = context.getCacheDir().getPath() + File.separator + dirName;
        }
        File destDir = new File(filePath);
        if (!destDir.exists()) {
            boolean isCreate = destDir.mkdirs();
            LogUtils.i("FileUtils", filePath + " has created. " + isCreate);
        }
        return destDir;
    }

    public static void delFile(File file, boolean delThisPath) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null) {
                int num = subFiles.length;
                // ????????
                for (int i = 0; i < num; i++) {
                    delFile(subFiles[i], true);
                }
            }
        }
        if (delThisPath) {
            file.delete();
        }
    }

    public static long getFileSizeLong(File file) {
        long size = 0;
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles != null) {
                    int num = subFiles.length;
                    for (int i = 0; i < num; i++) {
                        size += getFileSizeLong(subFiles[i]);
                    }
                }
            } else {
                size += file.length();
            }
        }
        return size;
    }

    public static void saveBitmap(File dir, String fileName, Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        File file = new File(dir, fileName);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isFileExists(File dir, String fileName) {
        return new File(dir, fileName).exists();
    }



    public static boolean isMountedSDCard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
            .getExternalStorageState())) {
            return true;
        } else {
            LogUtils.w(TAG, "SDCARD is not MOUNTED !");
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    public static long gainSDFreeSize() {
        if (isMountedSDCard()) {
            // ??SD?????
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());
            // ??????????(Byte)
            long blockSize = sf.getBlockSize();
            // ?????????
            long freeBlocks = sf.getAvailableBlocks();

            // ??SD?????
            return freeBlocks * blockSize; // ??Byte
        } else {
            return 0;
        }
    }

    @SuppressWarnings("deprecation")
    public static long gainSDAllSize() {
        if (isMountedSDCard()) {
            // ??SD?????
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());
            // ??????????(Byte)
            long blockSize = sf.getBlockSize();
            // ????????
            long allBlocks = sf.getBlockCount();
            // ??SD????Byte?
            return allBlocks * blockSize;
        } else {
            return 0;
        }
    }

    public static String gainSDCardPath() {
        if (isMountedSDCard()) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            if (!sdcardDir.canWrite()) {
                LogUtils.w(TAG, "SDCARD can not write !");
            }
            return sdcardDir.getPath();
        }
        return "";
    }

    public static String readFileByLines(String filePath) throws IOException {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath),
                System.getProperty("file.encoding")));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
                sb.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return sb.toString();

    }

    public static String readFileByLines(String filePath, String encoding)
        throws IOException {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), encoding));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
                sb.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return sb.toString();
    }

    public static void saveToFile(String filePath, String content)
        throws IOException {
        saveToFile(filePath, content, System.getProperty("file.encoding"));
    }

    public static void saveToFile(String filePath, String content,
        String encoding) throws IOException {
        BufferedWriter writer = null;
        File file = new File(filePath);
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, false), encoding));
            writer.write(content);

        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void appendToFile(String content, File file)
        throws IOException {
        appendToFile(content, file, System.getProperty("file.encoding"));
    }

    public static void appendToFile(String content, File file, String encoding)
        throws IOException {
        BufferedWriter writer = null;
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, true), encoding));
            writer.write(content);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static Boolean isExsit(String filePath) {
        Boolean flag = false;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                flag = true;
            }
        } catch (Exception e) {
            LogUtils.e("??????-->" + e.getMessage());
        }

        return flag;
    }

    public static String read(Context context, String filename)
        throws IOException {
        FileInputStream inStream = context.openFileInput(filename);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        return new String(data);
    }

    @SuppressWarnings("resource")
    public static String read(String fileName) throws IOException {
        FileInputStream inStream = new FileInputStream(fileName);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        return new String(data);
    }

    public static String read(String fileName, String encoding)
        throws IOException {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileName), encoding));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return sb.toString();
    }

    public static String readRawValue(Context context, int rawFileId) {
        String result = "";
        try {
            InputStream is = context.getResources().openRawResource(rawFileId);
            int len = is.available();
            byte[] buffer = new byte[len];
            is.read(buffer);
            result = new String(buffer, "UTF-8");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String readAssetsValue(Context context, String fileName) {
        String result = "";
        try {
            InputStream is = context.getResources().getAssets().open(fileName);
            int len = is.available();
            byte[] buffer = new byte[len];
            is.read(buffer);
            result = new String(buffer, "UTF-8");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<String> readAssetsListValue(Context context,
        String fileName) {
        List<String> list = new ArrayList<String>();
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Map<String, ?> readShrePerface(Context context,
        String fileNameNoExt) {
        SharedPreferences preferences = context.getSharedPreferences(
            fileNameNoExt, Context.MODE_PRIVATE);
        return preferences.getAll();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void writeShrePerface(Context context, String fileNameNoExt,
        Map<String, ?> values) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(
                fileNameNoExt, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            for (Iterator iterator = values.entrySet().iterator(); iterator
                .hasNext();) {
                Map.Entry<String, ?> entry = (Map.Entry<String, ?>) iterator
                    .next();
                if (entry.getValue() instanceof String) {
                    editor.putString(entry.getKey(), (String) entry.getValue());
                } else if (entry.getValue() instanceof Boolean) {
                    editor.putBoolean(entry.getKey(),
                        (Boolean) entry.getValue());
                } else if (entry.getValue() instanceof Float) {
                    editor.putFloat(entry.getKey(), (Float) entry.getValue());
                } else if (entry.getValue() instanceof Long) {
                    editor.putLong(entry.getKey(), (Long) entry.getValue());
                } else if (entry.getValue() instanceof Integer) {
                    editor.putInt(entry.getKey(), (Integer) entry.getValue());
                }
            }
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(Context context, String fileName, String content) {
        try {

            FileOutputStream outStream = context.openFileOutput(fileName,
                Context.MODE_PRIVATE);
            outStream.write(content.getBytes());
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(Context context, String fileName, byte[] content) {
        try {

            FileOutputStream outStream = context.openFileOutput(fileName,
                Context.MODE_PRIVATE);
            outStream.write(content);
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(Context context, String fileName, byte[] content,
        int modeType) {
        try {

            FileOutputStream outStream = context.openFileOutput(fileName,
                modeType);
            outStream.write(content);
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(File target, String content, String encoding)
        throws IOException {
        BufferedWriter writer = null;
        try {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
            }
            writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(target, false), encoding));
            writer.write(content);

        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void write(String filePath, byte[] content)
        throws IOException {
        FileOutputStream fos = null;

        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            fos.write(content);
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    public static File write(InputStream inputStream, String filePath)
        throws IOException {
        OutputStream outputStream = null;
        // ???????????????????
        File mFile = new File(filePath);
        if (!mFile.getParentFile().exists())
            mFile.getParentFile().mkdirs();
        try {
            outputStream = new FileOutputStream(mFile);
            byte buffer[] = new byte[4 * 1024];
            int lenght = 0;
            while ((lenght = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, lenght);
            }
            outputStream.flush();
            return mFile;
        } catch (IOException e) {
            LogUtils.e(TAG, "??????????" + e.getMessage());
            throw e;
        } finally {
            try {
                inputStream.close();
                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;
                }

            } catch (IOException e) {
            }
        }
    }

    public static void saveAsJPEG(Bitmap bitmap, String filePath)
        throws IOException {
        FileOutputStream fos = null;

        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    public static void saveAsPNG(Bitmap bitmap, String filePath)
        throws IOException {
        FileOutputStream fos = null;

        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }


    public static void copy(final String src, final String dst) throws IOException {
        if (TextUtils.isEmpty(src) || TextUtils.isEmpty(dst))
            throw new IOException("IOException (copy file): source file does not exists or not readable");
        copy(new File(src), new File(dst));
    }

    public static void copy(final File src, final File dst) throws IOException {
        if (src == null || !src.exists() || !src.canRead())
            throw new IOException("IOException (copy file): source file does not exists or not readable");

//		if ( !makeDirsForFile(dst) )
//			throw new IOException("IOException (copy file): cant create dirs for destination file");

        if (!isWritable(dst, true))
            throw new IOException("IOException (copy file): destination file not writable");

        LogUtils.w("FileUtils", "copying: src: " + src + " to dst: " + dst);

        final FileInputStream fis = new FileInputStream(src);
        final FileOutputStream fos = new FileOutputStream(dst);
        final InputStream in = new BufferedInputStream(fis);
        final BufferedOutputStream out = new BufferedOutputStream(fos);

        // Transfer bytes from in to out
        final byte[] buf = new byte[1024];
        int len;
        IOException e = null;

        try {
            while ((len = in.read(buf)) > 0)
                out.write(buf, 0, len);
        } catch (IOException e1) {
            e = e1;
        }

        // close InputStream in
        try {
            in.close();
        } catch (IOException ignored) {
        }
        // close FileInputStream fis
        try {
            fis.close();
        } catch (IOException ignored) {
        }
        // close BufferedOutputStream out
        try {
            out.flush();
            out.close();
        } catch (IOException ignored) {
        }
        // close FileOutputStream fos
        try {
            fos.flush();
            fos.close();
        } catch (IOException ignored) {
        }

        sync(fos);

        if (e != null)
            throw e;

    }

    public static synchronized void copySynchronized(final File src, final File dst) throws IOException {
        if (src == null || !src.exists() || !src.canRead())
            throw new IOException("IOException (copy file): source file does not exists or not readable");

//		if (!makeDirsForFile(dst) )
//			throw new IOException("IOException (copy file): cant create dirs for destination file");

        if (!isWritable(dst, true) /*dst==null || dst.exists() && !dst.canWrite()*/)
            throw new IOException("IOException (copy file): destination file not writable");

        final FileInputStream fis = new FileInputStream(src);
        final FileOutputStream fos = new FileOutputStream(dst);
        final InputStream in = new BufferedInputStream(fis);
        final BufferedOutputStream out = new BufferedOutputStream(fos);

        // Transfer bytes from in to out
        final byte[] buf = new byte[1024];
        int len;
        IOException e = null;
        try {
            while ((len = in.read(buf)) > 0)
                out.write(buf, 0, len);
        } catch (IOException e1) {
            e = e1;
        }
        // close InputStream in
        try {
            in.close();
        } catch (IOException ignored) {
        }
        // close FileInputStream fis
        try {
            fis.close();
        } catch (IOException ignored) {
        }
        // close BufferedOutputStream out
        try {
            out.flush();
            out.close();
        } catch (IOException ignored) {
        }
        // close FileOutputStream fos
        try {
            fos.flush();
            fos.close();
        } catch (IOException ignored) {
        }

        sync(fos);

        if (e != null)
            throw e;
    }

    public static boolean stringToFile(final String data, final File targetFile) {
        return stringToFile(data, targetFile, false);
    }

    public static boolean stringToFile(final String data, final File targetFile, final boolean doAppend) {
        if (data == null) {
            LogUtils.e("String data is null!");
            return false;
        }
        if (targetFile == null) {
            LogUtils.e("File is null!");
            return false;
        }
        if (!isWritable(targetFile, true)) {
            LogUtils.e("File is null or " + targetFile + " is not writable");
            //new IOException("File is null or cant make path dirs").printStackTrace();
            return false;
        }
        boolean isSucceed = true;
        try {
            final FileWriter out = new FileWriter(targetFile, doAppend);
            out.write(data);
            out.flush();
            out.close();
        } catch (IOException e) {
            //Logger.logError(TAG, e);
            isSucceed = false;
            LogUtils.e(e.getMessage());
        }
        return isSucceed;
    }

    public static boolean streamToFile(final InputStream inputStream, final File targetFile) {
        return streamToFile(inputStream, targetFile, false);
    }

    public static boolean streamToFile(final InputStream inputStream, final File targetFile, final boolean doAppend) {
        if (inputStream == null || !isWritable(targetFile, true)) {
            LogUtils.e("streamToFile(): Null parameter or can't make path dirs");
            //new IOException("Null parameter or can't make path dirs").printStackTrace();
            return false;
        }

        boolean isSucceed = false;
        final int buffer_size = 1024;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(targetFile, doAppend);
            byte[] bytes = new byte[buffer_size];
            int count;
            while ((count = inputStream.read(bytes, 0, buffer_size)) > 0)
                outputStream.write(bytes, 0, count);

            isSucceed = true;
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null)
                try {
                    sync(outputStream);
                    outputStream.close();
                } catch (IOException e) {
                }
            //inputStream wasn't opened here so it would not be closed here
        }
        return isSucceed;
    }

    public static boolean byteArrayOutputStreamToFile(final ByteArrayOutputStream bos, final File targetFile) {
        return byteArrayOutputStreamToFile(bos, targetFile, false);
    }

    public static boolean byteArrayOutputStreamToFile(final ByteArrayOutputStream bos, final File targetFile, final boolean doAppend) {
        if (bos == null || targetFile == null) {
            LogUtils.e("byteArrayOutputStreamToFile(): Null parameters given");
            //new IOException("Null parameters given").printStackTrace();
            return false;
        }
        return byteArrayToFile(bos.toByteArray(), targetFile, doAppend);
    }

    public static boolean byteArrayToFile(final byte[] array, final File targetFile) {
        return byteArrayToFile(array, targetFile, false);
    }

    public static boolean byteArrayToFile(final byte[] array, final File targetFile, final boolean doAppend) {

        if (array == null || array.length == 0 || !isWritable(targetFile, true)) {
            LogUtils.e("byteArrayToFile(): Null parameter or can't make path dirs");
            //new IOException("Null parameter or can't make path dirs").printStackTrace();
            return false;
        }

        if (targetFile.exists() && !targetFile.delete())
            return false;

//		if (targetFile==null || !makeDirsForFile(targetFile)){
//			new IOException("File is null or cant make path dirs").printStackTrace();
//			return false;
//		}

        boolean isSucceed = false;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(targetFile, doAppend);
            outputStream.write(array);
            isSucceed = true;
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null)
                try {
                    sync(outputStream);
                    outputStream.close();
                } catch (IOException e) {
                }
        }

        return isSucceed;
    }


    public static boolean intentDataToFile(final Context context, final Uri uri, final String targetFile) {
        return intentDataToFile(context, uri, new File(targetFile));
    }

    public static boolean intentDataToFile(final Context context, final Uri uri, final File targetFile) {

        if (context == null || uri == null || uri.toString().length() == 0 || !isWritable(targetFile, true)) {
            LogUtils.e("intentDataToFile(): Null parameter or can't make path dirs");
            //new IOException("Null parameter or can't make path dirs").printStackTrace();
            return false;
        }
//    	if (targetFile.exists() &&  !targetFile.delete())
//    		return false;
//		if (targetFile==null || !makeDirsForFile(targetFile)){
//			new IOException("File is null or cant make path dirs").printStackTrace();
//			return false;
//		}

        boolean isSucceed = false;
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            isSucceed = streamToFile(inputStream, targetFile, false);
        } catch (IOException e) {
            LogUtils.e("FleUtils.intentDataToFile()", e.getMessage());
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
        }

        return isSucceed;
    }

    public static boolean makeDirsForFile(final String file) {
        if (TextUtils.isEmpty(file)) {
            LogUtils.e("makeDirsForFile(): Null or Empty parameter given");
            //new Exception("Empty parameter").printStackTrace();
            return false;
        }
        return makeDirsForFile(new File(file));
    }

    public static boolean makeDirsForFile(final File file) {

        if (file == null)
            return false;

        LogUtils.d("FileUtils", "file: " + file + " isDirectory(): " + file.isDirectory());

        // ??? ????? ???? ????? parent, ????? ? ???? ????? ?????? ?? ??????!
        // ????? ????, ?????????????? ???? ?????? false ? isDirectory() ? ? isFile()
        //final File fileDir = file.isDirectory() ? file : new File(file.getParent());

        final String fileParentDir = file.getParent();
        final File fileDir = TextUtils.isEmpty(fileParentDir) ? null : new File(file.getParent());

        boolean isSucceed = fileDir == null || fileDir.exists();
        if (isSucceed)
            LogUtils.d("FileUtils", "fileDir: " + fileDir + " already exists(): " + isSucceed);

        if (!isSucceed && fileDir != null) {
            isSucceed = fileDir.mkdirs();
            LogUtils.d("FileUtils", "fileDir: " + fileDir + " mkdirs(): " + isSucceed);
        }

        return isSucceed;
    }

    public static boolean isWritable(final String file) {
        if (TextUtils.isEmpty(file)) {
            LogUtils.e("isWritable(): Null or Empty parameter given");
            //new Exception("Empty parameter").printStackTrace();
            return false;
        }
        return isWritable(new File(file));
    }

    public static boolean isWritable(final File file) {

        if (file == null) {
            LogUtils.e("isWritable(): Null or Empty parameter given");
            return false;
        }

        return isWritable(file, false);
//		final File fileDir = new File(file.getParent());
//		
//		if (!fileDir.exists())
//			return false;
//
//		boolean isWritable = true;
//    	
//		if (file.exists())
//			isWritable = file.canWrite();
//		else{
//			try{
//				isWritable = file.createNewFile();
//			} catch (IOException e) {}
//			
//			if (isWritable)
//				isWritable = file.delete();
//		}	
//		
//		return isWritable;
    }

    public static boolean isWritable(final String file, final boolean makeDirs) {
        if (TextUtils.isEmpty(file)) {
            LogUtils.e("isWritable(): Null or Empty parameter given");
            //new Exception("Empty parameter").printStackTrace();
            return false;
        }
        return isWritable(new File(file), makeDirs);
    }

    public static boolean isWritable(final File file, final boolean makeDirs) {

        if (file == null) {
            LogUtils.e("isWritable(): Null or Empty parameter given");
            return false;
        }

        final String fileParentDir = file.getParent();
        if (!TextUtils.isEmpty(fileParentDir)) {
            final File fileDir = new File(fileParentDir);
            if (!fileDir.exists()) {
                return makeDirs && makeDirsForFile(file);
            }
        }

        boolean isWritable = true;

        if (file.exists())
            isWritable = file.canWrite();
        else {
            try {
                isWritable = file.createNewFile();
            } catch (IOException e) {
            }

            if (isWritable)
                isWritable = file.delete();
        }

        return isWritable;
    }

    public static boolean isReadable(final String file) {
        if (TextUtils.isEmpty(file)) {
            LogUtils.e("isReadable(): Null or Empty parameter given");
            //new Exception("Empty or null parameter").printStackTrace();
            return false;
        }
        return isReadable(new File(file));
    }

    public static boolean isReadable(final File file) {

        if (file == null || !file.isFile()) {
            LogUtils.e("isReadable(): Null parameter given or not a File");
            return false;
        }

//		final File fileDir = new File(file.getParent());
//		if (!fileDir.exists())
//			return false;

        return file.exists() && file.canRead();
    }

    public static boolean sync(final OutputStream stream) {
        if (stream == null) {
            LogUtils.e("sync(): Null parameter given");
            return false;
        }

        return stream instanceof FileOutputStream && sync((FileOutputStream) stream);
    }

    public static boolean sync(final FileOutputStream stream) {
        if (stream == null) {
            LogUtils.e("sync(): Null parameter given");
            return false;
        }
        try {
            stream.getFD().sync();
            return true;
        } catch (SyncFailedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean syncAndClose(final FileOutputStream stream) {
        if (stream == null) {
            LogUtils.e("sync(): Null parameter given");
            return false;
        }
        boolean result = false;
        try {
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            stream.getFD().sync();
            result = true;
        } catch (SyncFailedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    public static boolean deleteFilesAndDirsRecursive(final String file) {
        if (TextUtils.isEmpty(file)) {
            LogUtils.e("deleteFilesAndDirsRecursive(): Null or Empty parameter given");
            return false;
        }
        return deleteFilesAndDirsRecursive(new File(file));
    }

    public static boolean deleteFilesAndDirsRecursive(final File directory) {
        if (directory == null || !directory.isDirectory()) {
            LogUtils.e("deleteFilesAndDirsRecursive(): Null parameter given or not a Directory");
            return false;
        }

        return deleteFilesAndDirs(directory);
    }

    // recursively called method
    // Returns true if all files deleted false if at least one doesn't
    private static boolean deleteFilesAndDirs(final File fileOrDirectory) {
        boolean isDeleted = true;
        if (fileOrDirectory.isDirectory()) {
            final File[] filesList = fileOrDirectory.listFiles();
            for (File child : filesList)
                isDeleted &= deleteFilesAndDirs(child);
        }

        return isDeleted & fileOrDirectory.delete();
    }

    public static boolean deleteFiles(final File targetDir) {
        if (targetDir == null || !targetDir.isDirectory()) {
            LogUtils.e("deleteFiles(): Null parameter given or not a Directory");
            return false;
        }

        boolean isDeleted = true;
        final File[] filesList = targetDir.listFiles();
        if (filesList == null) {
            LogUtils.e("deleteFiles(): targetDir (" + targetDir + ") is not a Directory");
        } else {
            for (File file2Delete : filesList)
                if (file2Delete.isFile()) // excluding dirs!
                    isDeleted &= file2Delete.delete();
        }
        return isDeleted;
    }


    public static long getAvailableSpace(final File file) {
        final String mFileRootPath = file.getAbsolutePath();
        return getAvailableSpace(mFileRootPath);
    }

    public static long getAvailableSpace(final Uri uri) {
        final String mFileRootPath = uri.getPath();
        return getAvailableSpace(mFileRootPath);
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static long getAvailableSpace(String mFileRootPath) {
        final File file = new File(mFileRootPath);
        if (!file.exists())
            mFileRootPath = file.getParent();

        long availableBytez;
        try {
            final StatFs stat = new StatFs(mFileRootPath);
            stat.restat(mFileRootPath);
            if (AppUtils.hasAPI(18))
                availableBytez = (stat.getAvailableBlocksLong() * stat.getBlockSizeLong());
            else
                availableBytez = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());

//				File path = Environment.getExternalStorageDirectory();
//	            StatFs stat = new StatFs(path.getPath());
//	            long blockSize = stat.getBlockSize();
//	            long totalBlocks = stat.getBlockCount();
//	            long availableBlocks = stat.getAvailableBlocks();
//
//	            mSdSize.setSummary(formatSize(totalBlocks * blockSize));
//	            mSdAvail.setSummary(formatSize(availableBlocks * blockSize) + readOnly);
//
//	            mSdMountToggle.setEnabled(true);
//	            mSdMountToggle.setTitle(mRes.getString(R.string.sd_eject));
//	            mSdMountToggle.setSummary(mRes.getString(R.string.sd_eject_summary));

        } catch (Exception e) {//IllegalArgumentException, NPE
            e.printStackTrace();
            // this can occur if the SD card is removed, but we haven't received the 
            // ACTION_MEDIA_REMOVED Intent yet.
            // status = Environment.MEDIA_REMOVED;
            availableBytez = -1;
        }

        return availableBytez;
    }


    public static String[] getFilenamesByExtension(final File directory, final String extension) {
        if (directory == null || !directory.isDirectory() || TextUtils.isEmpty(extension)) {
            LogUtils.e("getFilenamesByExtension(): Null, not a directory or empty extension");
            //new IOException("Null not a directory or empty extension").printStackTrace();
            return null;
        }

        final ExtensionFilter filter = new ExtensionFilter(extension.startsWith(".") ? extension : "." + extension);

        return directory.list(filter);
        //return null;
    }

    public static File[] getFilesByExtension(final File directory, final String extension) {
        if (directory == null || !directory.isDirectory() || TextUtils.isEmpty(extension)) {
            LogUtils.e("getFilesByExtension(): Null not a directory or empty extension");
            //new IOException("Null not a directory or empty extension").printStackTrace();
            return null;
        }

//    	String [] listOfFileNames = getFilenamesByExtension(directory, extension);
//    	if (listOfFileNames == null)
//    		return null;

        final ExtensionFilter filter = new ExtensionFilter(extension.startsWith(".") ? extension : "." + extension);
        return directory.listFiles(filter);
//    	
//    	if (filesList.length==0 )
//    		return new File[]{};
//
////    	File [] filesList = new File[listOfFileNames.length];
//    	for (int i = 0; i < filesList.length; i++) {
//    		filesList[i] = new File(directory.getAbsolutePath()+File.separator+listOfFileNames[i]);
//		}
//    	
//    	return filesList;
    }

//    public static String[] getFilesByExtension(File directory, String ... extensions){
//    	if (directory==null || !directory.isDirectory() || extensions==null || extensions.length==0)
//    		return null;
//    	
//    	
//    	for (String ext : extensions){
//    		ExtensionFilter filter = new ExtensionFilter(".png");
//    	}
//
//    	return directory.list(filter);
//    	//return null;
//    }

    public static File getFileFromUri(final Context context, final Uri uri) {
        final String path = getPathFromUri(context, uri);
        if (TextUtils.isEmpty(path))
            return null;
        else
            return new File(path);
    }

    @SuppressLint("NewApi")
    public static String getPathFromUri(final Context context, final Uri uri) {
//    	if (ctx==null || uri==null)
//    		return null;
//
//    	final Context context = ctx.getApplicationContext();

        //final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (AppUtils.hasAPI(19) && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(final Context context, final Uri uri, final String selection, final String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(final Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(final Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(final Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void mergeFiles(final File file1, final File file2, final File outputFile) {
        if (!isReadable(file1) || !isReadable(file2) || !outputFile.exists() && !makeDirsForFile(outputFile)) {
            return;
        }
        try {
            FileInputStream fis1 = new FileInputStream(file1);
            FileInputStream fis2 = new FileInputStream(file2);
            SequenceInputStream sis = new SequenceInputStream(fis1, fis2);
            FileOutputStream fos = new FileOutputStream(outputFile);
            int count;
            byte[] temp = new byte[4096];
            while ((count = sis.read(temp)) != -1) {
                fos.write(temp, 0, count);
            }
            FileUtils.sync(fos);
            fos.close();
            sis.close();
            fis1.close();
            fis2.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendFileToFile(final File file1, final File file2) {
        if (!isReadable(file1) || !isReadable(file2) || !isWritable(file1)) {
            return;
        }
        try {
            FileInputStream inputStream = new FileInputStream(file2);
            FileOutputStream outputStream = new FileOutputStream(file1, true);
            int count;
            final int buffer_size = 4096;
            byte[] bytes = new byte[buffer_size];
            while ((count = inputStream.read(bytes, 0, buffer_size)) > 0)
                outputStream.write(bytes, 0, count);
            FileUtils.sync(outputStream);
            outputStream.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean appendStringToFile(final String textToAppend, final File targetFile) {
        if (!isWritable(targetFile)) {
            LogUtils.e("File " + targetFile + " is not writable!");
            return false;
        }
        return stringToFile(textToAppend, targetFile, true);
    }

    public static String getBase64EncodedFile(final File fileToEncode) {
        if (!isReadable(fileToEncode)) {
            new Exception("File: " + fileToEncode + " is not readable!").printStackTrace();
            return null;
        }
        String dataString = null;
        try {
            // Reading a Image file from file system
            final FileInputStream imageInFile = new FileInputStream(fileToEncode);
            byte imageData[] = new byte[(int) fileToEncode.length()];
            imageInFile.read(imageData);
            // Converting Image byte array into Base64 String
            dataString = Base64.encodeToString(imageData, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return dataString;
    }

    public static class ExtensionFilter implements FilenameFilter {

        private final String ext;

        public ExtensionFilter(String ext) {
            this.ext = ext;
        }

        @Override
        public boolean accept(File dir, String filename) {
            return (filename.endsWith(ext));
        }
    }
}
