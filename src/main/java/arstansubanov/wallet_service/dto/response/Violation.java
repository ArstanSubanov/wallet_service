package arstansubanov.wallet_service.dto.response;

public record Violation(
        String fieldName,
        String message) {
}
