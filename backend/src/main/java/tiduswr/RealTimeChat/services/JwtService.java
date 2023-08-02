package tiduswr.RealTimeChat.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tiduswr.RealTimeChat.exceptions.WeakSecretJWT;
import tiduswr.RealTimeChat.model.security.JwtToken;
import tiduswr.RealTimeChat.model.security.JwtTokenType;

import java.security.InvalidKeyException;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET;
    private final int EXPIRATION;

    @Autowired
    public JwtService(@Value("${jwt.secret}") String secret) {
        this.SECRET = secret;
        this.EXPIRATION = 10;
    }

    public String extractUsername(String token) {
        if(token.startsWith("Bearer "))
            token = token.replace("Bearer ", "");
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails, JwtTokenType expectedTokenType) {
        try {
            final String username = extractUsername(token);
            final String tokenType = extractClaim(token, claims -> claims.get("tokenType").toString());
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) &&
                            tokenType.equalsIgnoreCase(expectedTokenType.name()));
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public JwtToken generateToken(String userName) throws WeakSecretJWT{
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenType", JwtTokenType.ACCESS);
        return createToken(claims, userName, EXPIRATION);
    }

    public JwtToken generateRefreshToken(String userName) throws WeakSecretJWT {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenType", JwtTokenType.REFRESH);
        return createToken(claims, userName, 60 * 24);
    }

    private JwtToken createToken(Map<String, Object> claims, String userName, int expiration) throws WeakSecretJWT {
        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        ZonedDateTime futureDateTime = now.plusMinutes(expiration).atZone(zoneId);

        String token =  Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(futureDateTime.toInstant()))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

        return new JwtToken(userName, token, Date.from(futureDateTime.toInstant()));
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
