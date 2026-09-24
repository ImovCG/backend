package com.imovcg.back.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Protege a carga em lote do scraper com uma chave de servico.
 *
 * <p>Enquanto {@code scraper.api-key} estiver vazio o endpoint segue aberto, para nao quebrar o
 * scraper ja em producao. Basta definir a variavel nos dois lados para fechar a porta.
 */
public class ScraperApiKeyFilter extends OncePerRequestFilter {

    private static final String ROTA_LOTE = "/api/imoveis/lote";
    private static final String CABECALHO = "X-Api-Key";

    private final String apiKey;

    public ScraperApiKeyFilter(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return apiKey.isBlank() || !ROTA_LOTE.equals(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws ServletException, IOException {

        if (!apiKey.equals(request.getHeader(CABECALHO))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Chave de servico invalida");
            return;
        }

        chain.doFilter(request, response);
    }
}
