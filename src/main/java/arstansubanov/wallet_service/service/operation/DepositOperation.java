package arstansubanov.wallet_service.service.operation;

import arstansubanov.wallet_service.enums.OperationType;
import arstansubanov.wallet_service.model.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DepositOperation implements Operation {

    @Override
    public Wallet calculateBalance(Wallet wallet, BigDecimal amount) {
        BigDecimal balance = wallet.getBalance().add(amount);
        wallet.setBalance(balance);
        return wallet;
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.DEPOSIT;
    }
}
