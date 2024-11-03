package arstansubanov.wallet_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDto(
        UUID id,
        BigDecimal balance
) {
}
