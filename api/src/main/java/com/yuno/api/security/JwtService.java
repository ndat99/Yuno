package com.yuno.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service //khai báo đây là một service
public class JwtService {

    //đọc con dấu từ file application.properties
    @Value("${jwt.secret.key}")
    private String jwtSecretKey;    

    //đọc thời hạn
    @Value("${jwt.expiration.ms}")
    private long jwtExpirationMs;

    //tạo token
    public String generateToken(String username){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username) //Ghi tên chủ token
                .setIssuedAt(now) //Ghi ngày phát hành
                .setExpiration(expiryDate) //Ghi ngày hết hạn
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) //Ký tên bằng con dấu
                .compact();
    }

    //Lấy tên từ token (dùng để xác thực)
    public String getUsernameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }

    //Kiểm tra token
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token);
            return true; //hợp lệ
        } catch (Exception e){
            return false; //ko hợp lệ
        }
    }

    //decode con dấu (biến chuỗi text thành con dấu thật)
    private SecretKey getSignInKey(){
        byte[] keyBytes = jwtSecretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //đọc một thông tin cụ thể (claim) từ token
    private <T> T getClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = Jwts.parserBuilder()
                                .setSigningKey(getSignInKey())
                                .build()
                                .parseClaimsJws(token)
                                .getBody();
        return claimsResolver.apply(claims);
    }
}
