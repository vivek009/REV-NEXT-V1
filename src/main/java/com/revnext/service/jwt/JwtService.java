package com.revnext.service.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.revnext.configuration.security.ApplicationUser;
import com.revnext.domain.user.User;
import com.revnext.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.cert.CertificateException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private Algorithm algorithm;

    @Autowired
    private UserRepository userRepository;

    public String createCustomToken(User user, Map<String, Object> extraClaims) {
        return JWT
                .create()
                .withIssuer("irrigation")
                .withSubject(user.getUserName())
                .withPayload(extraClaims)
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }

    public Map<String, Object> verifyAndGetClaims(String token) throws CertificateException {
        if (token == null) {
            throw new IllegalStateException("Token can not be null");
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer("irrigation").build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);

        // Extract claims properly
        Map<String, Object> claimsMap = new HashMap<>();
        for (Map.Entry<String, Claim> entry : decodedJWT.getClaims().entrySet()) {
            String key = entry.getKey();
            Claim claim = entry.getValue();

            // Handle different data types
            if (claim.isNull()) {
                claimsMap.put(key, null);
            } else if (claim.asBoolean() != null) {
                claimsMap.put(key, claim.asBoolean());
            } else if (claim.asInt() != null) {
                claimsMap.put(key, claim.asInt());
            } else if (claim.asLong() != null) {
                claimsMap.put(key, claim.asLong());
            } else if (claim.asDouble() != null) {
                claimsMap.put(key, claim.asDouble());
            } else if (claim.asString() != null) {
                claimsMap.put(key, claim.asString()); // ✅ Fixes extra double quotes issue
            } else if (claim.asList(String.class) != null) {
                claimsMap.put(key, claim.asList(String.class)); // ✅ Handles arrays correctly
            } else {
                claimsMap.put(key, claim.as(Object.class)); // Fallback for complex objects
            }
        }

        return claimsMap;
    }

    @Override
    @Cacheable(cacheNames = "userDetails", key = "#userId")
    public ApplicationUser loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUserId(UUID.fromString(userId));
        return optionalUser
                .map(user -> ApplicationUser
                        .builder()
                        .id(user.getUserId())
                        .password(user.getPassword())
                        .email(user.getEmail())
                        .username(user.getUserName())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }
}
