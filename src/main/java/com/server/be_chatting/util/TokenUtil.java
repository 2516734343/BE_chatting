package com.server.be_chatting.util;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;


import org.apache.maven.surefire.shade.org.apache.commons.lang3.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenUtil {

    private static final String SECRET = "systemApiKey20200205";
    private static final int EXPIRE_DAYS = 36500;

    public static String getToken(String biz) {

        Date nowDate = new Date();
        JwtBuilder builder = Jwts.builder().setSubject(biz)
                .setIssuedAt(nowDate)
                .setExpiration(new Date(DateUtil.plusDays(nowDate.getTime(), EXPIRE_DAYS)))
                .signWith(SignatureAlgorithm.HS256, getSecretKey());
        return builder.compact();
    }

    public static boolean tokenValid(String header) {
        if (StringUtils.isBlank(header)) {
            return false;
        }
        try {
            String[] s = header.trim().split("\\s+");
            if (s.length < 2) {
                return false;
            }
            String biz = s[0].trim();
            String token = s[1].trim();
            Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
            return biz.equals(jws.getBody().get("sub"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public static String getBiz(String header) {
        if (header == null) {
            return StringUtils.EMPTY;
        }
        String[] s = header.trim().split("\\s+");
        if (s.length < 2) {
            return StringUtils.EMPTY;
        }
        return s[0].trim();
    }

    private static Key getSecretKey() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] bytes = Base64.getEncoder().encode(Base64.getEncoder().encode(SECRET.getBytes()));
        return new SecretKeySpec(bytes, signatureAlgorithm.getJcaName());
    }

    public static void main(String[] args) {
        System.out.println(getToken("jiangliang"));
    }

}
