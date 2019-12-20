package com.panghu.appcommon.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * PhFileUtil
 *
 * @desc 文件操作类
 * @autor lijiangping
 * @wechat ljphhj
 * @date 2019年12月19日
 *
 **/
public class PhFileUtil {

    /**
     * 删除单个文件
     * @param filename
     * @return
     */
    public static boolean deleteFile(String filename) {
        return new File(filename).delete();
    }

    /**
     * 删除文件夹[不是文件夹,无法删除]
     * @param directory
     */
    public static void deleteFileDirectory(File directory) {
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                file.delete();
            }
        }
    }

    /**
     * 删除文件夹[不是文件夹,无法删除]
     * @param directory
     */
    public static void deleteFileDirectory(String directory) {
        File file = new File(directory);
        deleteFileDirectory(file);
    }

    /**
     * 判断文件是否存在
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * 将内容写入文件
     * @param filePath 文件路径
     * @param content 写入内容
     * @param append 是否是追加
     * @return
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        boolean isSuccess = false;
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(filePath, append));
            bufferedWriter.write(content);
            isSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(bufferedWriter);
        }
        return isSuccess;
    }

    /**
     * 读取文件内容
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        File file = new File(filePath);
        BufferedReader bufferedReader = null;
        String str = null;
        try {
            if (file.exists()) {
                bufferedReader = new BufferedReader(new FileReader(filePath));
                str = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(bufferedReader);
        }
        return str;
    }

    /**
     * 读取文件内容
     * @param file
     * @param charsetName 指定字符编码集
     * @return
     */
    public static StringBuilder readFile(File file, String charsetName) {
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            closeIO(reader);
        }
    }

    /**
     * 文件拷贝
     * @param in
     * @param out
     */
    public static void copyFile(InputStream in, OutputStream out) {
        try {
            byte[] b = new byte[2 * 1024 * 1024]; //2M memory
            int len = -1;
            while ((len = in.read(b)) > 0) {
                out.write(b, 0, len);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(in, out);
        }
    }

    /**
     * 快速文件拷贝[使用channel]
     *
     * @param in
     * @param out
     */
    public static void copyFileFast(File in, File out) {
        FileChannel filein = null;
        FileChannel fileout = null;
        try {
            filein = new FileInputStream(in).getChannel();
            fileout = new FileOutputStream(out).getChannel();
            filein.transferTo(0, filein.size(), fileout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(filein, fileout);
        }
    }

    /**
     * 格式化文件大小
     * @param context
     * @param size 单位:byte
     * @return 单位:MB
     */
    public static String formatFileSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }

    /**
     * 写入流数据到文件中
     * @param is
     * @param file
     */
    public static void writeFile(InputStream is, File file) {
        byte[] b = new byte[1024];
        int len;
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(is, os);
        }
    }

    /**
     * 创建文件夹
     * @param filePath
     * @return
     */
    public static boolean createFolder(String filePath) {
        return createFolder(filePath, false);
    }

    /**
     * 创建文件夹
     * @param filePath
     * @param recreate 强制删除之前的文件夹重新双肩
     * @return
     */
    public static boolean createFolder(String filePath, boolean recreate) {
        String folderName = getFileParentPath(filePath);
        if (folderName == null || folderName.length() == 0 || folderName.trim().length() == 0) {
            return false;
        }
        File folder = new File(filePath);
        if (folder.exists()) {
            if (recreate) {
                deleteFileDirectory(filePath);
                return folder.mkdirs();
            } else {
                return true;
            }
        } else {
            return folder.mkdirs();
        }
    }

    /**
     * 获取文件名
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (PhStringUtil.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * 获取文件大小[单位:byte]
     * @param filepath
     * @return
     */
    public static long getFileSize(String filepath) {
        if (TextUtils.isEmpty(filepath)) {
            return -1;
        }
        File file = new File(filepath);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    /**
     * 文件重命名
     * @param filepath
     * @param newfilepath
     * @return
     */
    public static boolean rename(String filepath, String newfilepath) {
        File file = new File(filepath);
        return file.exists() && file.renameTo(new File(newfilepath));
    }

    /**
     * 获取文件夹或者文件的上一个目录路径
     * @param filePath
     * @return
     */
    public static String getFileParentPath(String filePath) {
        if (filePath == null || filePath.length() == 0 || filePath.trim().length() == 0) {
            return filePath;
        }
        int filePos = filePath.lastIndexOf(File.separator);
        return (filePos == -1) ? "" : filePath.substring(0, filePos);
    }

    /**
     * 获取文件夹中的所有文件
     * @param path
     * @return
     */
    public static ArrayList<File> getFilesArray(String path) {
        File file = new File(path);
        File files[] = file.listFiles();
        ArrayList<File> listFile = new ArrayList<File>();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    listFile.add(files[i]);
                }
                if (files[i].isDirectory()) {
                    listFile.addAll(getFilesArray(files[i].toString()));
                }
            }
        }
        return listFile;
    }

    /**
     * 删除整个文件
     * @param folder
     * @return
     */
    public static boolean deleteFiles(String folder) {
        if (folder == null || folder.length() == 0 || folder.trim().length() == 0) {
            return true;
        }
        File file = new File(folder);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    private static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
