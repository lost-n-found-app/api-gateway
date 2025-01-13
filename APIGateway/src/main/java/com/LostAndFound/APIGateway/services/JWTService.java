package com.LostAndFound.APIGateway.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTService {


    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);
String secretKey="";

public JWTService()
{
    try {
        logger.info("Generating HMAC key for JWTService...");
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk = keyGenerator.generateKey();
        secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        logger.info("HMAC key generated successfully.");
    }
catch (NoSuchAlgorithmException e)
{
    logger.error("Failed to generate HMAC key: {}", e.getMessage(), e);
    throw new RuntimeException("failed to generate HMAC key",e);
}
}

public String generateToken(String phoneNumber){
    logger.info("Generating JWT token for phone number: {}", phoneNumber);
    String token=Jwts.builder()
            .subject(phoneNumber)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis()+(30 * 60 * 1000)))
            .signWith(getKey())
            .compact();
    logger.info("JWT token generated successfully for phone number: {}", phoneNumber);
    return token;
}
    SecretKey getKey() {
        logger.debug("Decoding and retrieving the HMAC secret key...");
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);

    }


}
