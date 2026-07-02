package com.imovcg.back.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.imovcg.back.model.Imovel;

@Service
public class ImovelEmailService {

    private final JavaMailSender mailSender;

    @Value("${app.confirmation-base-url:http://localhost:8080}")
    private String confirmationBaseUrl;

    public ImovelEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(Imovel imovel) {
        String confirmationUrl = confirmationBaseUrl
            + "/api/imoveis/confirmar?token="
            + imovel.getVerificationToken();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(imovel.getEmail());
        message.setSubject("Confirme o cadastro do imóvel");
        message.setText(
            "Recebemos o cadastro do imóvel \""
                + imovel.getTitulo()
                + "\".\n\n"
                + "Para confirmar o e-mail e liberar o imóvel, acesse:\n"
                + confirmationUrl
        );

        mailSender.send(message);
    }
}