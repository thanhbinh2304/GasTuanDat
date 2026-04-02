package com.example.GasTuanDat.auth;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.example.GasTuanDat.account.AccountEntity;
import com.example.GasTuanDat.account.AccountRepository;
import com.example.GasTuanDat.auth.dtos.LoginRequest;
import com.example.GasTuanDat.auth.dtos.LoginResponse;
import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.expiration:3600}")
    protected long accessExpirationSeconds;

    @NonFinal
    @Value("${jwt.refreshExpiration:604800}")
    protected long refreshExpirationSeconds;

    public LoginResponse login(LoginRequest request) {
        AccountEntity account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ApiException(ErrorCode.UNAUTHORIZED));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), account.getPassword());
        if (!passwordMatches) {
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }

        long now = System.currentTimeMillis();
        long accessExpiresAt = now + accessExpirationSeconds * 1000;
        long refreshExpiresAt = now + refreshExpirationSeconds * 1000;

        SecretKey key = Keys.hmacShaKeyFor(SIGNER_KEY.getBytes(StandardCharsets.UTF_8));
        String accessToken = Jwts.builder()
                .setSubject(account.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(accessExpiresAt))
                .claim("role", (account.getRole() != null ? account.getRole().getRoleName() : "EMPLOYEE")
                        .toUpperCase(Locale.ROOT))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(account.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(refreshExpiresAt))
                .claim("type", "refresh")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        TokenEntity tokenEntity = tokenRepository.findByAccountId(account.getAccountId())
                .orElseGet(() -> TokenEntity.builder()
                        .accountId(account.getAccountId())
                        .createdAt(Timestamp.from(Instant.ofEpochMilli(now)))
                        .build());

        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setExpiresAt(Timestamp.from(Instant.ofEpochMilli(refreshExpiresAt)));
        tokenRepository.save(tokenEntity);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessExpirationSeconds)
                .build();
    }
}
