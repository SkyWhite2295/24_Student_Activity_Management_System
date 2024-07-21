package cn.jeefast.system.oauth2;

import java.security.MessageDigest;
import java.util.UUID;

import cn.jeefast.common.exception.RRException;

/**
 * 生成token
 *
 */
public class TokenGenerator {

    //将生成的 UUID 字符串作为参数，生成相应的 token
    public static String generateValue() {
        return generateValue(UUID.randomUUID().toString());
    }

    private static final char[] hexCode = "0123456789abcdef".toCharArray();//将字节数据转换成十六进制表示时所需要的字符集合


    public static String toHexString(byte[] data) {
        if(data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length*2);//用于存储转换后的十六进制字符串
        for ( byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    //根据输入的 param 字符串生成 MD5 加密后的 token
    public static String generateValue(String param) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");//获取一个 MessageDigest 实例，使用 MD5 算法
            algorithm.reset();
            algorithm.update(param.getBytes());//将 param 字符串转换成字节数组，并更新 algorithm 的摘要信息
            byte[] messageDigest = algorithm.digest();//获取生成的摘要字节数组 messageDigest
            return toHexString(messageDigest);//转换成十六进制表示的字符串
        } catch (Exception e) {
            throw new RRException("生成Token失败", e);
        }
    }
}
