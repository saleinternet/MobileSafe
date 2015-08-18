package com.csw.ms.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 字符串工具类：
 *      将输入流转换成字符串
 * Created by chensiwen on 15/8/8.
 */
public class StringUtils {

    /**
     * 将输入流转换成字符串
     * @param  is 输入流
     * @return 字符串
     * @throws IOException
     * */
    public static String readFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        is.close();
        String result = baos.toString();
        baos.close();
        return result;
    }
}
