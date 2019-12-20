package com.panghu.appcommon.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * PhAESUtil
 * AES加密操作类
 *
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 */
public class PhAESUtil {

    public static final String CHARSET_NAME = "UTF-8";
    private static Cipher cipher;
    private static SecretKeySpec key;
    private static AlgorithmParameterSpec spec;

    /**
     * 初始化加密操作
     * @param securtyKey 密钥
     */
    private static void initAES(String securtyKey){
        try {
            if(cipher == null){
                MessageDigest digest = MessageDigest.getInstance("SHA-384");
                digest.update(securtyKey.getBytes(CHARSET_NAME));
                byte[] keyBytes = new byte[32];
                byte[] tmpDigest = digest.digest();
                System.arraycopy(tmpDigest, 0, keyBytes, 0, keyBytes.length);
                cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                key = new SecretKeySpec(keyBytes, "AES");
                spec = getIV(tmpDigest);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();

    }

    public static AlgorithmParameterSpec getIV(byte[] digest) {
        byte[] iv = new byte[16];
        System.arraycopy(digest, 32, iv, 0, iv.length);
        IvParameterSpec ivParameterSpec;
        ivParameterSpec = new IvParameterSpec(iv);
        return ivParameterSpec;
    }

    public static String encrypt(String plainText, String securtyKey) throws Exception {
        byte[] encrypted = encrypt(plainText.getBytes(CHARSET_NAME), securtyKey);
        String encryptedText = new String(encrypted, CHARSET_NAME);
        return encryptedText;
    }

    public static String decrypt(String cryptedText, String securtyKey) throws Exception {
        byte[] decrypted = decrypt(cryptedText.getBytes(CHARSET_NAME), securtyKey);
        String decryptedText = new String(decrypted, CHARSET_NAME);
        return decryptedText;
    }


    public static byte[] encrypt(byte[] plainText, String securtyKey) throws Exception {
        initAES(securtyKey);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encrypted = cipher.doFinal(plainText);
        encrypted = android.util.Base64.encode(encrypted,
                android.util.Base64.NO_WRAP);
        return encrypted;
    }

    public static byte[] decrypt(byte[] cryptedText, String securtyKey) throws Exception {
        initAES(securtyKey);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] bytes = android.util.Base64.decode(cryptedText, android.util.Base64.NO_WRAP);
        byte[] decrypted = cipher.doFinal(bytes);
        return decrypted;
    }

    /**
     * Here is Both function for encrypt and decrypt file in Sdcard folder. we
     * can not lock folder but we can encrypt file using AES in Android, it may
     * help you.
     *
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */

    public static File encrypt(File srcFile, File encodeFile, String decodeKey) throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {
        // Here you read the cleartext.
        if(srcFile==null || encodeFile==null || !srcFile.exists() || !encodeFile.exists() || decodeKey==null){
            return null;
        }

        FileInputStream fis = new FileInputStream(srcFile.getAbsolutePath());
        // This stream write the encrypted text. This stream will be wrapped by
        // another stream.
        FileOutputStream fos = new FileOutputStream(encodeFile.getAbsolutePath());

        // Length is 16 byte
        SecretKeySpec sks = new SecretKeySpec(decodeKey.getBytes(),
                "AES");
        // Create cipher
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        // Write bytes
        int b;
        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        // Flush and close streams.
        cos.flush();
        cos.close();
        fis.close();
        return encodeFile;
    }

    public static File decrypt(File encodeFile, File decodeFile, String decodeKey) throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {
        try{
            if(decodeFile==null || encodeFile==null || !decodeFile.exists() || !encodeFile.exists() || decodeKey==null){
                return null;
            }
            FileInputStream fis = new FileInputStream(encodeFile.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(decodeFile.getAbsolutePath());
            SecretKeySpec sks = new SecretKeySpec(decodeKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, sks);
            CipherInputStream cis = new CipherInputStream(fis, cipher);
            int b;
            byte[] d = new byte[8];
            while ((b = cis.read(d)) != -1) {
                fos.write(d, 0, b);
            }
            fos.flush();
            fos.close();
            cis.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return decodeFile;
    }


}
