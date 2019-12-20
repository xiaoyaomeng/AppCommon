package com.panghu.appcommon.utils;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * PhMD5Util
 *
 * @desc MD5操作类
 * @autor lijiangping
 * @wechat ljphhj
 * @date 2019年12月19日
 *
 **/
public class PhMD5Util {

    /**
     * 转换成md5值
     *
     * @param src
     * @return
     */
    public final static String convertMd5(String src) {

        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();  //md5 32bit
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 分析md5缓冲区大小
     */
    private static final int BUFFER_SIZE = 2048;

    /**
     * 分析文件md5
     * @param file
     * @return
     *
     * 米3 android 4.4 分析孕期apk(27M) 缓冲区大小2048 用时 600~800毫秒不等
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[BUFFER_SIZE];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {

            }
        }
        return parseByte2HexStr(digest.digest());
    }

    public static String getMD5(byte[] buffer) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return parseByte2HexStr(digest.digest());
    }


    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return (new StringBuilder(String.valueOf(hexDigits[d1]))).append(hexDigits[d2]).toString();
    }

    public static String Encode(String origin) {
        String resultString = origin;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
        } catch (Exception exception) {
        }
        return resultString;
    }

    public static String MD5Encode(String key) {
        return Encode(Encode(key));
    }

    public static String MD5Encode16(String key) {
        String result =  Encode(Encode(key));
        result = result.substring(8,24);
        return result;
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b
     *            字节数组
     * @return 字符串
     */
    public static String byteArrayToHexStringOfSha256(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * sha256_HMAC加密
     *
     * @param message
     *            消息
     * @param secret
     *            秘钥
     * @return 加密后字符串
     */
    public static String hmacSha256(String message, String secret) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            hash = Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (Exception e) {
        }
        return hash;
    }
}
