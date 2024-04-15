package avia.cloud.message.functions;

import avia.cloud.message.dto.SimpleMailMessageDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class MessageFunctions {
    private final JavaMailSender mailSender;
    @Bean
    public Consumer<SimpleMailMessageDTO> sendSimpleMailMessage() {
        return message -> {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(message.recipient());
            simpleMailMessage.setSubject(message.subject());
            simpleMailMessage.setText(message.text());
            mailSender.send(simpleMailMessage);
        };
    }
    @Bean
    public Consumer<SimpleMailMessageDTO> sendHtmlMailMessage() {
        return message -> {
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,  MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                        StandardCharsets.UTF_8.name());

                helper.setTo(message.recipient());
                helper.setSubject(message.subject());
                helper.setText(message.text(), true);
                mailSender.send(mimeMessage);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
