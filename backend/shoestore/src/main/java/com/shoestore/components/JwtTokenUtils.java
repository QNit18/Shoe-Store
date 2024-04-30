package com.shoestore.components;

import com.shoestore.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    @Value("${jwt.expiration}")
    private int expiration; // save to an environment variable

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user){
        // properties => claims
        Map<String, Object> claims = new HashMap<>();


        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("userId",user.getId());
//        String secrkey = this.generateSecretKey(); Using when generate new Key
        try {
            String token = Jwts.builder()
                    .setClaims(claims) // how to extract claims from this
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        }catch (Exception e ){
            // Can 'inject' Logger instead of System.out.println
            throw new InvalidParameterException("Cannot create JWT Token, error: " + e.getMessage());
        }
    }

    public Key getSignKey(){
        byte[] bytes = Decoders.BASE64.decode(secretKey); //TcK1IwzCcPVosaMaS0yRUUDDjtPUSl8DnjeJNXWPugg=
        return Keys.hmacShaKeyFor(bytes);
    }

    private String generateSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        return Encoders.BASE64.encode(keyBytes);
    }

    // Extracting all claims in section
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Return one claim
    public  <T> T extractClaim(String token, Function<Claims, T> claimsTFunction){
        Claims claims = this.extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // Checking expiration
    public boolean isTokenExpired(String token){
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date()); // Does not exceed the current date
    }

    public String extractPhoneNumber(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String phoneNumber = extractPhoneNumber(token);
        return phoneNumber.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
