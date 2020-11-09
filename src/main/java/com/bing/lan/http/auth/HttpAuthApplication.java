package com.bing.lan.http.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HTTP认证之摘要认证
 * <p>
 * https://www.cnblogs.com/xiaoxiaotank/p/11078571.html
 * <p>
 * 摘要认证及实现HTTP digest authentication
 * <p>
 * https://blog.csdn.net/t1269747417/article/details/86038128
 * <p>
 * HTTP摘要认证
 * https://blog.csdn.net/jesse881025/article/details/43669625
 *
 */
@RestController
@SpringBootApplication
public class HttpAuthApplication {

  public static void main(String[] args) {
    SpringApplication.run(HttpAuthApplication.class, args);
  }

  /**
   * 可用postman进行摘要认证
   */
  @RequestMapping("/digest")
  public String login(HttpServletRequest request, HttpServletResponse response) {
    //解析客户端发送过来的请求报头中的Authorization
    String authorization = request.getHeader("Authorization");
    if (authorization != null && !"".equals(authorization)) {
      System.out.println("第二次(): " + authorization);
      Map<String, String> map = new HashMap<>();
      //Digest username="admin", realm="realm", nonce="ca5fa6bf-fadd-42bc-9271-cd54564a1944", uri="/digest", algorithm="MD5",
      // qop=auth, nc=00000001, cnonce="456325", response="d3079a99332c7c11928cfa76d3e6dcb4", opaque="2533c252-5014-47fc-90a5-974746061bb1"
      String regex = "[\\s](.+?)=(.+?),";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(authorization);
      while (matcher.find()) {
        map.put(matcher.group(1), matcher.group(2));
      }
      regex = "[\\s](.+?)=\"(.+?)\",?";
      pattern = Pattern.compile(regex);
      matcher = pattern.matcher(authorization);
      while (matcher.find()) {
        map.put(matcher.group(1), matcher.group(2));
      }

      //其参数不为空，利用参数值，和服务器上存储的口令，进行比对。
      String username = map.get("username");
      String realm = map.get("realm");
      String password = "12345admin";
      String method = request.getMethod();
      String uri = map.get("uri");
      String nonce = map.get("nonce");
      //String nc = map.get("nc");
      //String cnonce = map.get("cnonce").replaceAll("\"","");

      //String qop = map.get("qop");
      //客户端传过来的摘要
      String responseFromClient = map.get("response");

      //MD5计算
      String a1 = username + ":" + realm + ":" + password;
      String ha1 = Md5Util.md5(a1);

      String a2 = method + ":" + uri;
      String ha2 = Md5Util.md5(a2);
      //服务器计算出的摘要
      String responseBefore = ha1 + ":" + nonce + ":" + ha2;
      //String responseBefore = ha1 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + ha2;
      String responseMD5 = Md5Util.md5(responseBefore);
      //两者摘要相同，即验证成功
      if (responseMD5 != null && !responseMD5.equalsIgnoreCase(responseFromClient)) {
        //业务逻辑。。。
        //两者摘要不相同，验证失败
        return "fail";
      } else {
        //业务逻辑。。。支付宝支付h5支付
      }
      //其参数为空，返回参数到客户端，并发起质询。
      return "ok";
    }

     // RFC 2069 标准
     // RFC 2617 标准 ：当 qop 未指定的情况，也就是遵循简化的 RFC 2069 标准

    //拼接AuthorizationHeader,格式如Digestusername="admin",realm="Restrictedarea",nonce="554a3304805fe",qop=auth,opaque="cdce8a5c95a1427d74df7acbf41c9ce0",nc=00000001,response="391bee80324349ea1be02552608c0b10",cnonce="0a4f113b",uri="/MyBlog/home/Response/response_last_modified"
    StringBuilder sb = new StringBuilder();
    sb.append("Digest ");
    sb.append("realm").append("=\"realm\",");
    //sb.append("qop").append("=\"auth\",");
    sb.append("nonce").append("=\"").append(UUID.randomUUID()).append("\",");
    sb.append("opaque").append("=\"").append(UUID.randomUUID()).append("\"");
    String s1 = sb.toString();
    response.setHeader("WWW-Authenticate", s1);
    System.out.println("第一次(): " + s1);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    return "first";
  }
}
