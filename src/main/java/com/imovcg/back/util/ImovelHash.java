package com.imovcg.back.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.Locale;

import com.imovcg.back.dto.ImovelPostDTO;

public class ImovelHash {

    public static String gerarHash(ImovelPostDTO dto) {
        String conteudo = String.join("|",
                normalizar(dto.getTitulo()),
                normalizarNumero(dto.getPreco()),
                normalizar(dto.getBairro()),
                normalizar(dto.getEndereco()),
                normalizar(dto.getTipoImovel()),
                normalizarInteiro(dto.getQuartos()),
                normalizarInteiro(dto.getBanheiros()),
                normalizarNumero(dto.getAreaM2()),
                normalizarNumero(dto.getCondominio()),
                normalizarNumero(dto.getIptu()),
                normalizarInteiro(dto.getVagas())
        );

        return sha256(conteudo);
    }

    private static String normalizar(String valor) {
        if (valor == null) return "";
        String semAcento = Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcento.trim().toLowerCase(Locale.ROOT);
    }

    private static String normalizarNumero(Number valor) {
        return valor == null ? "" : String.valueOf(valor.doubleValue());
    }

    private static String normalizarInteiro(Integer valor) {
        return valor == null ? "" : String.valueOf(valor);
    }

    private static String sha256(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(texto.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash SHA-256", e);
        }
    }
}