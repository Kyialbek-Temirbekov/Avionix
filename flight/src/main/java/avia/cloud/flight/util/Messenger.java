package avia.cloud.flight.util;

import avia.cloud.flight.dto.SimpleMailMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class Messenger {
    private final StreamBridge streamBridge;
    public void sendAttachmentMessage(SimpleMailMessageDTO message) {
        var result = streamBridge.send("sendHtmlMessage-out-0", message);
        log.info("Is the Communication request successfully triggered ? : {}", result);
    }
}
