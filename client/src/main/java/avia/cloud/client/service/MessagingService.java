package avia.cloud.client.service;

public interface MessagingService {
    void sendMessage(String to, String subject, String text);
}
