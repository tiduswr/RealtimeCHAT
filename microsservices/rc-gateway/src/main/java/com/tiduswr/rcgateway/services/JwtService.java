package com.tiduswr.rcgateway.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tiduswr.rcgateway.config.security.JwtTokenType;
import com.tiduswr.rcgateway.exceptions.WeakSecretJWT;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.SECRET = secret;
    }

    public Optional<String> extractUsername(String token) {
        try{
            if(token.startsWith("Bearer "))
                token = token.replace("Bearer ", "");
            return Optional.of(extractClaim(token, Claims::getSubject));
        }catch(Exception ex){
            return Optional.empty();
        }
    }

    public Boolean validateToken(String token) {
        try {
            final String tokenType = extractClaim(token, claims -> claims.get("tokenType").toString());
            return (!isTokenExpired(token) && tokenType.equalsIgnoreCase(JwtTokenType.ACCESS.name()));
        } catch (Exception e) {
            return false;
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() throws WeakSecretJWT{
        byte[] keyBytes = SECRET.getBytes();
        try{
            return Keys.hmacShaKeyFor(keyBytes);
        }catch(WeakKeyException ex){
            throw new WeakSecretJWT("A chave JWT é muito fraca, reveja as configurações colocando uma Key válida!");
        }
    }

}
