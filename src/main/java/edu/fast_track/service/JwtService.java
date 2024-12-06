package edu.fast_track.service;

import edu.fast_track.repository.CustomerRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final CustomerRepository repository;

    public String extractEmail(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes()))
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public void validateToken(String token) throws Exception {
        Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

}
