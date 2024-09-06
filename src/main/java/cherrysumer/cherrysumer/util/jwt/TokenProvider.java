package cherrysumer.cherrysumer.util.jwt;


import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.exception.handler.CertificationHandler;
import cherrysumer.cherrysumer.web.dto.UserRequestDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class TokenProvider implements InitializingBean {

    private final String secretkey;
    private Key key;
    private final long accessTokenExptime;

    public TokenProvider(@Value("${jwt.secret}") String key, @Value("${jwt.expiration_time}") long accessTokenExptime) {
        this.secretkey = key;
        this.accessTokenExptime = accessTokenExptime;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(UserRequestDTO.userInfoDTO user) {
        return createJwtToken(user);
    }

    private String createJwtToken(UserRequestDTO.userInfoDTO user) {

        long exp = (new Date()).getTime() + Long.valueOf(this.accessTokenExptime*1000); // unix time 설정
        Date expirtime = new Date(exp); // 만료시간 30일 설정

        Claims claims = Jwts.claims().setSubject(user.getId().toString());

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(claims)
                .setIssuer("cherrysumer")
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expirtime)
                .compact();
    }

    // header create
    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());

        return header;
    }

    // claims create
    private Map<String, Object> createClaims(UserRequestDTO.userInfoDTO user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", user.getId());
        return claims;
    }

    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException(ErrorCode._TOKEN_EXPIRATION.getMessage());
            //throw new CertificationHandler(ErrorCode._TOKEN_EXPIRATION);
        } catch (Exception e) {
            throw new JwtException(ErrorCode._TOKEN_UNAUTHORIZED.getMessage());
            //throw new CertificationHandler(ErrorCode._TOKEN_UNAUTHORIZED);
        }
    }


    // 메일 인증 코드
    public String generatedMailCode(){
        try {
            Random random = SecureRandom.getInstanceStrong();
            String code = "";
            for(int i=0; i<6; i++) {
                int n = random.nextInt(10);
                code += Integer.toString(n);
            }
            return code;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
