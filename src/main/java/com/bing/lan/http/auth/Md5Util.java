package com.bing.lan.http.auth;

import java.security.MessageDigest;

/**
 * MD5加密
 */
public class Md5Util {

  private static final char[] hexDigits1 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
  private static final char[] hexDigits2 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  public static void main(String[] args) {
    String md5 = MD5("963852qwe");
    System.out.println("main(): " + md5);
  }

  /**
   * @return 大写
   */
  public static String MD5(String s) {
    return getMd5String(s, hexDigits1);
  }

  /**
   * @return 小写
   */
  public static String md5(String s) {
    return getMd5String(s, hexDigits2);
  }

  private static String getMd5String(String s, char[] hexDigits) {
    try {
      byte[] btInput = s.getBytes();
      // 获得MD5摘要算法的 MessageDigest 对象
      MessageDigest mdInst = MessageDigest.getInstance("MD5");
      // 使用指定的字节更新摘要
      mdInst.update(btInput);
      // 获得密文
      byte[] md = mdInst.digest();
      // 把密文转换成十六进制的字符串形式
      int j = md.length;
      char str[] = new char[j * 2];
      int k = 0;
      for (int i = 0; i < j; i++) {
        byte byte0 = md[i];
        str[k++] = hexDigits[byte0 >>> 4 & 0xf];
        str[k++] = hexDigits[byte0 & 0xf];
      }
      return new String(str);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
