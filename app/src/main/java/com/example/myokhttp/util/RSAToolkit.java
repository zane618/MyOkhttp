package com.example.myokhttp.util;


import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;


/**
 * RSA签名和验签工具类
 *
 * @author 钱多多--付广才
 */
public class RSAToolkit {

    public static final String RSA = "RSA";

    static {
        System.loadLibrary("save-key");
    }

    // 私钥 false 线上 true 测试
    public static native String getPrivateKey(boolean type);

    private RSAToolkit() {
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        getString();

    }

    public static String getString() {
        String data = null;
        try {
            byte[] testByte = RSAToolkit.encryptByPrivateKey("feng".getBytes("utf-8"));
            data = Base64UtilsToHttp.encode(testByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("data>>" + data);
        return data;
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data 源数据
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64UtilsToHttp.decode(getPrivateKey(true)));
        //注意
        KeyFactory keyFactory = KeyFactory.getInstance(RSA, "BC");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        //加密填充方式
        //注意android环境与Java不同，此处不能使用keyFactory
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        return cipher.doFinal(data);
    }

    /**
     * SHA-1加密
     *
     * @param info
     * @return
     */
    public static String encryptToSHA(String info) {
        byte[] digesta = null;
        try {
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            try {
                alga.update(info.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String rs = bytes2Hex(digesta);
        return rs;
    }

    public static String bytes2Hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    /**
     * RSA解密
     *
     * @param data
     * @param privateKey
     * @return
     */
    public String decryptData(String data, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA", "BC");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] descryptData = Base64UtilsToHttp.decode(data);
            byte[] descryptedData = cipher.doFinal(descryptData);
            String srcData = new String(descryptedData, "utf-8");
            return srcData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
