package avia.cloud.message.functions;

import avia.cloud.message.dto.SimpleMailMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

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
}
