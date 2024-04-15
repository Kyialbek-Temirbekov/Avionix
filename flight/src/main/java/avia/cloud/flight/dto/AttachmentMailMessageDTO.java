package avia.cloud.flight.dto;

public record AttachmentMailMessageDTO(String recipient, String subject, byte[] attachment) {
}
