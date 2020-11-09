package com.bing.lan.http.auth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lb on 2020/11/5.
 */

public class RegexTest {

  public static void main(String[] args) {
    //String regex = "([a-z]+)=\"([a-z]+)\"";
    String regex = "[\\s](.+?)=\"(.+?)\",?";
    //String regex = "[\\s]?([^\\s.]+?)=\"(.+?)\",?";
    Pattern pattern = Pattern.compile(regex);
    //Matcher matcher = pattern.matcher("username=\"admin\"");
    Matcher matcher = pattern.matcher( "Digest username=\"admin\", realm=\"realm\", nonce=\"222763e0-e037-4b8d-ab48-d6715d430496\", uri=\"/digest\", algorithm=\"MD5\", qop=auth, nc=00000001, cnonce=\"dP6Zvhhe\", response=\"facb40e92049c24b19863187a3a2f0a0\"");
    while (matcher.find()) {
      System.out.println(">>>" + matcher.group(1) + "<<< >>>" + matcher.group(2) + "<<<" + "\n");
      //System.out.println("main(): \n");
      //System.out.println("main(): \n");
      //System.out.println("main(): \n");
    }
  }
}
