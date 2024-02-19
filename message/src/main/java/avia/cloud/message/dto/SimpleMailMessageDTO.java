package avia.cloud.message.dto;

public record SimpleMailMessageDTO(String recipient, String subject, String text) {
}
