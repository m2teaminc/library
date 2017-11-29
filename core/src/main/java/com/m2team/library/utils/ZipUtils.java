package com.m2team.library.utils;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    private static final int KB = 1024;

    private ZipUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean zipFiles(final Collection<File> resFiles, final String zipFilePath)
            throws IOException {
        return zipFiles(resFiles, zipFilePath, null);
    }

    public static boolean zipFiles(final Collection<File> resFiles, final String zipFilePath, final String comment)
            throws IOException {
        return zipFiles(resFiles, FileUtils.getFileByPath(zipFilePath), comment);
    }

    public static boolean zipFiles(final Collection<File> resFiles, final File zipFile)
            throws IOException {
        return zipFiles(resFiles, zipFile, null);
    }

    public static boolean zipFiles(final Collection<File> resFiles, final File zipFile, final String comment)
            throws IOException {
        if (resFiles == null || zipFile == null) return false;
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            for (File resFile : resFiles) {
                if (!zipFile(resFile, "", zos, comment)) return false;
            }
            return true;
        } finally {
            if (zos != null) {
                zos.finish();
                CloseUtils.closeIO(zos);
            }
        }
    }

    public static boolean zipFile(final String resFilePath, final String zipFilePath)
            throws IOException {
        return zipFile(resFilePath, zipFilePath, null);
    }

    public static boolean zipFile(final String resFilePath, final String zipFilePath, final String comment)
            throws IOException {
        return zipFile(FileUtils.getFileByPath(resFilePath), FileUtils.getFileByPath(zipFilePath), comment);
    }

    public static boolean zipFile(final File resFile, final File zipFile)
            throws IOException {
        return zipFile(resFile, zipFile, null);
    }

    public static boolean zipFile(final File resFile, final File zipFile, final String comment)
            throws IOException {
        if (resFile == null || zipFile == null) return false;
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            return zipFile(resFile, "", zos, comment);
        } finally {
            if (zos != null) {
                CloseUtils.closeIO(zos);
            }
        }
    }

    private static boolean zipFile(final File resFile, String rootPath, final ZipOutputStream zos, final String comment)
            throws IOException {
        rootPath = rootPath + (isSpace(rootPath) ? "" : File.separator) + resFile.getName();
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            // ???????????????'/'??File.separator???????eggPain
            if (fileList == null || fileList.length <= 0) {
                ZipEntry entry = new ZipEntry(rootPath + '/');
                if (!StringUtils.isEmpty(comment)) entry.setComment(comment);
                zos.putNextEntry(entry);
                zos.closeEntry();
            } else {
                for (File file : fileList) {
                    // ??????false???false
                    if (!zipFile(file, rootPath, zos, comment)) return false;
                }
            }
        } else {
            InputStream is = null;
            try {
                is = new BufferedInputStream(new FileInputStream(resFile));
                ZipEntry entry = new ZipEntry(rootPath);
                if (!StringUtils.isEmpty(comment)) entry.setComment(comment);
                zos.putNextEntry(entry);
                byte buffer[] = new byte[KB];
                int len;
                while ((len = is.read(buffer, 0, KB)) != -1) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
            } finally {
                CloseUtils.closeIO(is);
            }
        }
        return true;
    }

    public static boolean unzipFiles(final Collection<File> zipFiles, final String destDirPath)
            throws IOException {
        return unzipFiles(zipFiles, FileUtils.getFileByPath(destDirPath));
    }

    public static boolean unzipFiles(final Collection<File> zipFiles, final File destDir)
            throws IOException {
        if (zipFiles == null || destDir == null) return false;
        for (File zipFile : zipFiles) {
            if (!unzipFile(zipFile, destDir)) return false;
        }
        return true;
    }

    public static boolean unzipFile(final String zipFilePath, final String destDirPath)
            throws IOException {
        return unzipFile(FileUtils.getFileByPath(zipFilePath), FileUtils.getFileByPath(destDirPath));
    }

    public static boolean unzipFile(final File zipFile, final File destDir)
            throws IOException {
        return unzipFileByKeyword(zipFile, destDir, null) != null;
    }

    public static List<File> unzipFileByKeyword(final String zipFilePath, final String destDirPath, final String keyword)
            throws IOException {
        return unzipFileByKeyword(FileUtils.getFileByPath(zipFilePath),
                FileUtils.getFileByPath(destDirPath), keyword);
    }

    public static List<File> unzipFileByKeyword(final File zipFile, final File destDir, final String keyword)
            throws IOException {
        if (zipFile == null || destDir == null) return null;
        List<File> files = new ArrayList<>();
        ZipFile zf = new ZipFile(zipFile);
        Enumeration<?> entries = zf.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            String entryName = entry.getName();
            if (StringUtils.isEmpty(keyword) || FileUtils.getFileName(entryName).toLowerCase().contains(keyword.toLowerCase())) {
                String filePath = destDir + File.separator + entryName;
                File file = new File(filePath);
                files.add(file);
                if (entry.isDirectory()) {
                    if (!FileUtils.createOrExistsDir(file)) return null;
                } else {
                    if (!FileUtils.createOrExistsFile(file)) return null;
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        in = new BufferedInputStream(zf.getInputStream(entry));
                        out = new BufferedOutputStream(new FileOutputStream(file));
                        byte buffer[] = new byte[KB];
                        int len;
                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                    } finally {
                        CloseUtils.closeIO(in, out);
                    }
                }
            }
        }
        return files;
    }

    public static List<String> getFilesPath(final String zipFilePath)
            throws IOException {
        return getFilesPath(FileUtils.getFileByPath(zipFilePath));
    }

    public static List<String> getFilesPath(final File zipFile)
            throws IOException {
        if (zipFile == null) return null;
        List<String> paths = new ArrayList<>();
        Enumeration<?> entries = getEntries(zipFile);
        while (entries.hasMoreElements()) {
            paths.add(((ZipEntry) entries.nextElement()).getName());
        }
        return paths;
    }

    public static List<String> getComments(final String zipFilePath)
            throws IOException {
        return getComments(FileUtils.getFileByPath(zipFilePath));
    }

    public static List<String> getComments(final File zipFile)
            throws IOException {
        if (zipFile == null) return null;
        List<String> comments = new ArrayList<>();
        Enumeration<?> entries = getEntries(zipFile);
        while (entries.hasMoreElements()) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            comments.add(entry.getComment());
        }
        return comments;
    }

    public static Enumeration<?> getEntries(final String zipFilePath)
            throws IOException {
        return getEntries(FileUtils.getFileByPath(zipFilePath));
    }

    public static Enumeration<?> getEntries(final File zipFile)
            throws IOException {
        if (zipFile == null) return null;
        return new ZipFile(zipFile).entries();
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
    public static byte[] compress(byte[] data) {
        byte[] output = new byte[0];

        Deflater compresser = new Deflater();

        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        compresser.end();
        return output;
    }

    public static void compress(byte[] data, OutputStream os) {
        DeflaterOutputStream dos = new DeflaterOutputStream(os);
        try {
            dos.write(data, 0, data.length);
            dos.finish();
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] decompress(byte[] data) {
        byte[] output = new byte[0];

        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);

        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!decompresser.finished()) {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        decompresser.end();
        return output;
    }

    public static byte[] decompressEx(byte[] data,int offset,int len) {
        byte[] output = new byte[0];

        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data,offset,len);

        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!decompresser.finished()) {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        decompresser.end();
        return output;
    }

    public static byte[] decompress(InputStream is) {
        InflaterInputStream iis = new InflaterInputStream(is);
        ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
        try {
            int i = 1024;
            byte[] buf = new byte[i];
            while ((i = iis.read(buf, 0, i)) > 0) {
                o.write(buf, 0, i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return o.toByteArray();
    }


    private final static String LOG_TAG = ZipUtils.class.getSimpleName();

    public static final int BUFFER = 1024;

    public static void zip(File[] files, File zipFileName) {
        FileOutputStream dest = null;
        ZipOutputStream zipOutputStream = null;
        try {
            dest = new FileOutputStream(zipFileName);
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(dest));
            final byte data[] = new byte[BUFFER];
            for (File file : files) {
                add(file, zipOutputStream, data);
            }
            FileUtils.sync(dest);
            dest.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zipOutputStream != null)
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (dest != null)
                try {
                    dest.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static void zip(String[] files, String zipFileName) {
        FileOutputStream dest = null;
        ZipOutputStream zipOutputStream = null;
        try {
            dest = new FileOutputStream(zipFileName);
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(dest));
            final byte data[] = new byte[BUFFER];
            for (String file : files) {
                LogUtils.d(LOG_TAG, "Adding: " + file);
                add(new File(file), zipOutputStream, data);
            }

            FileUtils.sync(dest);
            dest.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zipOutputStream != null)
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (dest != null)
                try {
                    dest.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private static void add(final File file, final ZipOutputStream zipOutputStream, final byte[] data) {
        LogUtils.d(LOG_TAG, "Adding: " + file);
        BufferedInputStream origin = null;
        try {
            FileInputStream fi = new FileInputStream(file);
            origin = new BufferedInputStream(fi, BUFFER);

            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(zipEntry);
            int count;

            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                zipOutputStream.write(data, 0, count);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (origin != null)
                try {
                    origin.close();
                } catch (IOException ignored) {
                }

        }
    }

    public static void zip(final String fileToAddToZip, String targetZipFile) {
        zip(new File(fileToAddToZip), new File(targetZipFile));
    }

    public static void zip(final File fileToAddToZip, File targetZipFile) {
        try {
            LogUtils.d(LOG_TAG, "Adding: " + fileToAddToZip);
            final FileOutputStream dest = new FileOutputStream(targetZipFile);
            final ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            final byte data[] = new byte[BUFFER];
            final FileInputStream fi = new FileInputStream(fileToAddToZip);
            final BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);

            final ZipEntry entry = new ZipEntry(fileToAddToZip.getName());
            out.putNextEntry(entry);
            int count;

            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
            FileUtils.sync(dest);
            out.close();
            dest.flush();
            dest.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void unzip(final String zipFile, final String targetLocation) {
        unzip(new File(zipFile), new File(targetLocation));
    }

    public void unzip(final File zipFile, final File targetLocation) {
        if (!FileUtils.isReadable(zipFile)) {
            new Exception("unzip(): Invalid zipFile: " + zipFile).printStackTrace();
            return;
        }
        //create target location folder if not exist
        targetLocation.mkdirs();
        if (!targetLocation.exists()) {
            new Exception("unzip(): Can't create path (targetLocation): " + targetLocation).printStackTrace();
            return;
        }
        try {
            final FileInputStream fileInputStream = new FileInputStream(zipFile);
            final ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                //create dir if required while unzipping
                if (zipEntry.isDirectory()) {
                    new File(targetLocation, zipEntry.getName()).mkdirs();
                } else {
                    final FileOutputStream fileOutputStream = new FileOutputStream(targetLocation + zipEntry.getName());
                    for (int c = zipInputStream.read(); c != -1; c = zipInputStream.read()) {
                        fileOutputStream.write(c);
                    }
                    FileUtils.sync(fileOutputStream);
                    zipInputStream.closeEntry();
                    fileOutputStream.close();
                }
            }
            zipInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unzip(final File zipFile, final String targetFile, final File targetLocation) {
        if (!FileUtils.isReadable(zipFile)) {
            new Exception("unzip(): Invalid zipFile: " + zipFile).printStackTrace();
            return;
        }
        //create target location folder if not exist
        targetLocation.mkdirs();
        if (!targetLocation.exists()) {
            new Exception("unzip(): Can't create path (targetLocation): " + targetLocation).printStackTrace();
            return;
        }
        try {
            final FileInputStream fileInputStream = new FileInputStream(zipFile);
            final ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
            ZipEntry zipEntry;
            String zipEntryName;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                zipEntryName = zipEntry.getName();
                //create dir if required while unzipping
                if (zipEntry.isDirectory()) {
                    new File(targetLocation, zipEntryName).mkdirs();
                } else {
                    if (TextUtils.equals(targetFile, zipEntryName)) {
                        final FileOutputStream fileOutputStream = new FileOutputStream(targetLocation + zipEntryName);
                        for (int c = zipInputStream.read(); c != -1; c = zipInputStream.read()) {
                            fileOutputStream.write(c);
                        }
                        FileUtils.sync(fileOutputStream);
                        zipInputStream.closeEntry();
                        fileOutputStream.close();
                    } else {
                        zipInputStream.closeEntry();
                    }
                }
            }
            zipInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
