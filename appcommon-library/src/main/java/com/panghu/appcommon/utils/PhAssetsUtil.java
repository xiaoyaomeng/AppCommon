package com.panghu.appcommon.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * PhAssetsUtil
 *
 * @desc 操作app中assets目录文件的类
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhAssetsUtil {

    private static final String ENCODING = "UTF-8";

    /**
     * 从assets目录中读文件
     *
     * @param context
     * @param fileName
     * @return
     * @throws IOException
     */
    private static InputStream getFileFromAssets(Context context, String fileName) throws IOException {
        AssetManager am = context.getAssets();
        return am.open(fileName);
    }

    /**
     * 方式一：从assets目录中读取文件内容(by InputStream)
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getTextFromAssets(Context context, String fileName) {
        String result = null;
        try {
            InputStream in = getFileFromAssets(context, fileName);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            result = new String(buffer, Charset.forName(ENCODING));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 方式二：从assets目录中读取文件内容(by InputStreamReader)
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getTextFromAssets2(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getFileFromAssets(context, fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            StringBuilder result = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
