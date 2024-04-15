package avia.cloud.message.dto;

public record AttachmentMailMessageDTO(String recipient, String subject, byte[] attachment) {
}
