package arstansubanov.wallet_service.service.operation;

import arstansubanov.wallet_service.enums.OperationType;
import arstansubanov.wallet_service.model.Wallet;

import java.math.BigDecimal;

public interface Operation {

    Wallet calculateBalance(Wallet wallet, BigDecimal amount);

    OperationType getOperationType();
}
