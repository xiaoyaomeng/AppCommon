package com.panghu.uikit.utils;


import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The file util functions.
 * <p/>
 */
public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final int BUFF_SIZE = 1024 * 1024;
    private static final int WRITE_FILE_BUFF_SIZE = 16 * 1024;

    private FileUtil() {

    }

    public static File getLogDir(Context context) {
        File logDir = new File(context.getFilesDir(), "log");
        if (!logDir.exists()) {
            if (!logDir.mkdirs()) {
                Log.e(TAG, "Error in create directory");
            }
        }
        return logDir;
    }

    public static File getCrashLogFile(Context context, String fileName) {
        File dir = getLogDir(context);
        if (dir != null) {
            return new File(dir, fileName);
        } else {
            return null;
        }

    }

    public static String formatSizeString(long size) {
        if (size <= 0) {
            return "";
        }
        int multiplyFactor = 0;
        String result;
        String[] tokens = new String[]{"Byte", "KB", "MB", "GB", "TB"};
        double temp = size;
        while (temp > 1024) {
            temp /= 1024;
            multiplyFactor += 1;
        }
        boolean isInteger = multiplyFactor < 2 || (Math.round(temp) - temp == 0);
        String checkSumString = isInteger ? String.format("%.0f", temp) : String.format("%.1f", temp);
        result = String.format("%s %s%s", checkSumString, tokens[multiplyFactor], (multiplyFactor == 0 && size != 1) ? "s" : "");
        return result;
    }

    public static String readFile(String fileName) {
        String res = "";
        FileInputStream fin = null;
        try {

            File file = new File(fileName);
            boolean isExist = file.exists();
            if (isExist) {
                fin = new FileInputStream(file);
                int length = fin.available();
                int ch;
                byte[] buffer = new byte[1024];
                StringBuffer source = new StringBuffer();
                while ((ch = fin.read(buffer)) != -1) {
                    source.append(new String(buffer, 0, ch));
                }

                res = source.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;

    }

    //Zip File or Directory
    public static void zipFile(@NonNull List<File> filesToZip, @NonNull File zipFile,
                               @Nullable List<String> filteredDirectories,
                               @Nullable List<String> filteredFiles) {
        ZipOutputStream zipOut = null;
        try {
            OutputStream fileOut = new FileOutputStream(zipFile);
            zipOut = new ZipOutputStream(new BufferedOutputStream(fileOut, BUFF_SIZE));
            for (File fileToZip : filesToZip) {
                zipFile(fileToZip, zipOut, filteredDirectories, filteredFiles, "");
            }
        } catch (IOException e) {
            Log.e(TAG, "Error in zip file: "+ zipFile.getPath(), e);
        } finally{
            if (zipOut != null) {
                try {
                    zipOut.close();
                } catch (IOException e) {
                    Log.e(TAG, "close: ", e);
                }
            }
        }
    }

    private static void zipFile(@NonNull File fileToZip, @NonNull ZipOutputStream zipOut,
                                @Nullable List<String> filteredDirectories, @Nullable List<String> filteredFiles, @NonNull String rootPath) {

        rootPath = rootPath + (rootPath.trim().length() == 0 ? "" : File.separator) + fileToZip.getName();
        if (fileToZip.isDirectory()) {
            if (filteredDirectories != null && filteredDirectories.contains(fileToZip.getName())) {
                Log.v(TAG, "Ignore " + fileToZip.getPath());
                return;
            }
            for (File file : fileToZip.listFiles()) {
                zipFile(file, zipOut, filteredDirectories, filteredFiles, rootPath);
            }
        } else {
            if (filteredFiles != null && filteredFiles.contains(fileToZip.getName())) {
                Log.v(TAG, "Ignore file: " + fileToZip.getPath());
                return;
            }
            byte buffer[] = new byte[BUFF_SIZE];
            BufferedInputStream inputStream = null;
            try {
                inputStream = new BufferedInputStream(new FileInputStream(fileToZip),
                        BUFF_SIZE);
                zipOut.putNextEntry(new ZipEntry(rootPath));
                int realLength;
                while ((realLength = inputStream.read(buffer)) != -1) {
                    zipOut.write(buffer, 0, realLength);
                }
                zipOut.flush();
                zipOut.closeEntry();
            } catch (IOException ex) {
                Log.e(TAG, "Error in zip file: " + fileToZip.getPath(), ex);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ex) {
                        Log.e(TAG, "Error in close inputStream", ex);
                    }
                }

            }
        }
    }

    public static File getAlbumStorageDir(String albumName) {
        File file;
        if (albumName.isEmpty()) {
            file = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
        } else {
            file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), albumName);
        }
        Log.v(TAG, "File: " + file);
        if (file.exists() && file.isDirectory()) {
            return file;
        }
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
            return null;
        }
        return file;
    }

    public static void writeFile(InputStream inputStream, File file) throws IOException {
        OutputStream output;
        try {
            output = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[WRITE_FILE_BUFF_SIZE]; // or other buffer size
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            } finally {
                output.close();
            }

        } catch (IOException e) {
            throw e;
        }
    }

    public static boolean writeFile(String filePath, String data) {
        boolean written = false;
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(filePath));
            bufferedWriter.write(data);
            written = true;
        } catch (IOException e) {
            Log.e(TAG, "Error in writeFile", e);
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error in close BufferedWriter", e);
            }
        }
        return written;
    }

    public static String getFileExtensionFromPath(String path) {
        if (path == null) {
            return null;
        }
        int index = path.lastIndexOf(".");
        if (index == -1) {
            return null;
        }
        return path.substring(index + 1);
    }

    public static String getFileExtensionFromUri(Context context, Uri uri) {
        if (uri == null) {
            return "";
        }
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public static String getMimeTypeFromExtension(Uri uri) {
        if (uri == null) {
            return "";
        }
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        extension = extension.toLowerCase();
        if (extension.contains(".")) {
            extension = extension.substring(extension.lastIndexOf("."));
        }
        String mimeType = mimeTypeMap.getMimeTypeFromExtension(extension);
        // sometimes this works (as with the commit content api)
        if (mimeType == null) {
            mimeType = uri.getQueryParameter("mimeType");
        }
        return mimeType;
    }


    public static String getFileMimeTypeFromUri(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.getType(uri);
    }

    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                size = inputStream.available();
            }  catch (IOException e) {
                Log.e(TAG, "IOException: ", e);
            }
        }
        return size;
    }

    public static String getFileNameWithoutExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        int start = filePath.lastIndexOf(File.separatorChar) + 1;
        int end = filePath.lastIndexOf('.');
        if (end < 0) {
            end = filePath.length();
        }
        return filePath.substring(start, end);
    }

    public static boolean isExists(String path) {
        try {
            return new File(path).exists();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean copyFile(Uri sourceUri, String destPath, Context context) throws Exception {
        File destFile = createDestFile(destPath);
        if (destFile == null) {
            return false;
        }
        InputStream srcStream = context.getContentResolver().openInputStream(sourceUri);
        if (srcStream == null) {
            Log.w(TAG, "sourceUri " + sourceUri + " can not get inputStream.");
            return false;
        }
        OutputStream destStream = new FileOutputStream(destPath);
        boolean isSucceed = false;
        try {
            isSucceed = copyFile(srcStream, destStream);
        } catch (Exception e) {
            destFile.delete();
        } finally {
            srcStream.close();
            destStream.close();
        }
        return isSucceed;
    }

    public static boolean copyFile(String sourcePath, String destPath) throws Exception {
        File srcFile = new File(sourcePath);
        if (!srcFile.exists()) {
            Log.w(TAG, "Source file " + srcFile + " is not exist.");
            return false;
        }
        File destFile = createDestFile(destPath);
        if (destFile == null) {
            return false;
        }
        boolean isSucceed = false;
        try (InputStream srcStream = new FileInputStream(srcFile);
             OutputStream destStream = new FileOutputStream(destPath)) {
            isSucceed = copyFile(srcStream, destStream);
        } catch (Exception e) {
            destFile.delete();
        }
        return isSucceed;
    }

    private static File createDestFile(String destPath) throws Exception {
        File file = new File(destPath);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            Log.w(TAG, "mkdirs " + file.getParentFile() + " failed");
            return null;
        }
        if (!file.exists() && !file.createNewFile()) {
            Log.w(TAG, "create new file failed");
            return null;
        }
        return file;
    }

    public static boolean copyFile(InputStream in, OutputStream out) throws IOException {
        // Copy the bits from input stream to output stream
        byte[] buf = new byte[WRITE_FILE_BUFF_SIZE];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        return true;
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
