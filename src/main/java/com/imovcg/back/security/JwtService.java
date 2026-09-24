package com.imovcg.back.security;

import com.imovcg.back.model.Anunciante;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;

/**
 * Emite e valida os tokens de sessao do anunciante.
 */
public class JwtService {

    private final SecretKey chave;
    private final long validadeSegundos;

    public JwtService(String segredo, long validadeSegundos) {
        this.chave = Keys.hmacShaKeyFor(segredo.getBytes(StandardCharsets.UTF_8));
        this.validadeSegundos = validadeSegundos;
    }

    public String gerarToken(Anunciante anunciante) {
        Instant agora = Instant.now();

        return Jwts.builder()
                .subject(String.valueOf(anunciante.getId()))
                .claim("email", anunciante.getEmail())
                .claim("nome", anunciante.getNome())
                .issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plusSeconds(validadeSegundos)))
                .signWith(chave)
                .compact();
    }

    /**
     * Devolve o id do anunciante dono do token, ou null se o token for invalido ou estiver expirado.
     */
    public Long extrairAnuncianteId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(chave)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return Long.valueOf(claims.getSubject());
        } catch (JwtException | NumberFormatException e) {
            return null;
        }
    }

    public long getValidadeSegundos() {
        return validadeSegundos;
    }
}
