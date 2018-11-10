package com.zw.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class ZwJWT {
    /**
     * 生成加密后的token
     * @param username 用户名
     * @return 加密后的token
     */
    public static String getToken(final String username,final String password) {
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + 1000L * 3600L * 24L);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("username", username)
                    .withClaim("password", password)
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256("mysecret"));
        } catch (JWTCreationException exception){
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return token;
    }

    public static String getShareToken(String u){
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + 1000L * 3600L * 5L);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("urls", u)
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256("mysecret"));
        } catch (JWTCreationException exception){
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return token;
    }
    public static String getPassToken(String u){
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + 1000L*10);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("pass", u)
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256("mysecret"));
        } catch (JWTCreationException exception){
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return token;
    }
    /**
     * 先验证token是否被伪造，然后解码token。
     * @param token 字符串token
     * @return 解密后的DecodedJWT对象，可以读取token中的数据。
     */
    public static DecodedJWT deToken(final String token) {
        DecodedJWT jwt = null;
        try {
            // 使用了HMAC256加密算法。
            // mysecret是用来加密数字签名的密钥。
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("mysecret"))
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            jwt = verifier.verify(token);
            return jwt;
        } catch (Exception exception) {
            return null;
        }

    }

    /**
     * 验证是否登录
     * @param key
     * @param page
     * @return
     */
    public static String PageVerification(String key,String page){
        if(key==null){
            return "Login";
        }else{
            DecodedJWT decodedJWT=ZwJWT.deToken(key);
            if(decodedJWT==null){
                return "Login";
            }else{
                if(decodedJWT.getClaim("username").asString().equals("zengwei")&&decodedJWT.getClaim("password").asString().equals("123456")){
                    return page;
                }else{
                    return "Login";
                }
            }
        }
    }

    /**
     * 请求验证
     * @param key
     * @return
     */
    public static boolean isAdoptRequest(String key){
        if(key==null){
            return false;
        }else{
            DecodedJWT decodedJWT=ZwJWT.deToken(key);
            if(decodedJWT==null){
                return false;
            }else{
                if(decodedJWT.getClaim("username").asString().equals("zengwei")&&Pass.getPass().getPassWord(decodedJWT.getClaim("password").asString())){
                    return true;
                }else{
                    return false;
                }
            }
        }
    }

    public static void main(String[] s){
//        System.out.println(new ZwJWT().getToken("zengwei","123456"));
//        DecodedJWT decodedJWT= new ZwJWT().deToken("eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoMCIsImV4cCI6MTUzOTc2MzMxMCwidXNlcm5hbWUiOiJ6ZW5nd2VpIn0.Krq8XYPhLPlb8l2SnSZ5FdX4DXnGDsvaNZ51HK3DiGc");
//        System.out.println(decodedJWT.getClaim("username").asString());
    }
}
