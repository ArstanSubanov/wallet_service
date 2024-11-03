package arstansubanov.wallet_service.dto;

import arstansubanov.wallet_service.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;


import java.math.BigDecimal;
import java.util.UUID;

public record WalletUpdateRequest(

        @NotNull
        UUID walletId,

        @NonNull
        OperationType operationType,

        @Positive
        BigDecimal amount
) {
}
