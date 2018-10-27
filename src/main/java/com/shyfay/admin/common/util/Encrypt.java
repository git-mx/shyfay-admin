package com.shyfay.admin.common.util;
import com.shyfay.admin.common.constant.Constant;
import org.apache.commons.codec.digest.DigestUtils;

public class Encrypt {
    public static String md5(String text) throws Exception {
        String encodeStr= DigestUtils.md5Hex(text + Constant.MD5_KEY);
        return encodeStr;
    }

    public static boolean verify(String text, String md5) throws Exception {
        String md5Text = md5(text);
        return md5Text.equalsIgnoreCase(md5);
    }

    public static void main(String[] args){
        try{
            System.out.println(md5("string"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
