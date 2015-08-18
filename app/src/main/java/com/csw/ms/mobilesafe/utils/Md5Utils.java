package com.csw.ms.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5密码加密
 * Created by chensiwen on 15/8/15.
 */
public class Md5Utils {

    /**
     * md5密码加密
     */
    public static String md5(String passwd) {
        try {
            //得到一个信息摘要器
            MessageDigest digest = null;
            digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(passwd.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : result) {
                int number = b & 0xff;//加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
            }
            //标准md5结果
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

}
