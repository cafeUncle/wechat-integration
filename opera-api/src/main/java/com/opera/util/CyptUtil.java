package com.opera.util;

import java.security.MessageDigest;

public class CyptUtil {

    public static String getSha1(byte[] input) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(input);
            StringBuffer sb = new StringBuffer();
            for (byte b : result) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }

    }
}
