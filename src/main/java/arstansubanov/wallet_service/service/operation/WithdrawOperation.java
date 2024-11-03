package arstansubanov.wallet_service.service.operation;

import arstansubanov.wallet_service.enums.OperationType;
import arstansubanov.wallet_service.exception.InsufficientFundsException;
import arstansubanov.wallet_service.model.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WithdrawOperation implements Operation {

    @Override
    public Wallet calculateBalance(Wallet wallet, BigDecimal amount) {
        BigDecimal balance = wallet.getBalance();
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds to withdraw, balance: " + balance +
                    ", withdraw amount: " + amount);
        }
        wallet.setBalance(balance.subtract(amount));
        return wallet;
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.WITHDRAWAL;
    }
}
