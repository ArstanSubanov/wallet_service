package arstansubanov.wallet_service.service;

import arstansubanov.wallet_service.dto.WalletDto;
import arstansubanov.wallet_service.dto.WalletUpdateRequest;

import java.util.UUID;

public interface WalletService {

    WalletDto updateWallet(WalletUpdateRequest request);

    WalletDto getWalletById(UUID id);
}
