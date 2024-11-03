package arstansubanov.wallet_service.service.impl;

import arstansubanov.wallet_service.dto.WalletDto;
import arstansubanov.wallet_service.dto.WalletUpdateRequest;
import arstansubanov.wallet_service.mapper.WalletMapper;
import arstansubanov.wallet_service.model.Wallet;
import arstansubanov.wallet_service.repository.WalletRepository;
import arstansubanov.wallet_service.service.WalletService;
import arstansubanov.wallet_service.service.operation.Operation;
import arstansubanov.wallet_service.service.operation.OperationRegistry;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final OperationRegistry operationRegistry;

    @Override
    @Transactional
    @Retryable(retryFor = OptimisticLockException.class,
            maxAttemptsExpression = "${retryable.max-attempts}",
            backoff = @Backoff(delayExpression = "${retryable.delay}"))
    public WalletDto updateWallet(WalletUpdateRequest request) {
        Wallet wallet = findWalletById(request.walletId());
        Operation operation = operationRegistry.getOperation(request.operationType());
        wallet = operation.calculateBalance(wallet, request.amount());
        walletRepository.save(wallet);
        log.debug("Updated wallet {}", wallet);
        return walletMapper.toWalletDto(wallet);
    }

    @Override
    public WalletDto getWalletById(UUID id) {
        return walletMapper.toWalletDto(findWalletById(id));
    }

    private Wallet findWalletById(UUID id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet with id " + id + " not found"));
    }
}