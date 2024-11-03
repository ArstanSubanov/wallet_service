package arstansubanov.wallet_service.controller;

import arstansubanov.wallet_service.dto.WalletDto;
import arstansubanov.wallet_service.dto.WalletUpdateRequest;
import arstansubanov.wallet_service.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public WalletDto updateWallet(@RequestBody @Valid WalletUpdateRequest request) {
        return walletService.updateWallet(request);
    }

    @GetMapping("/{id}")
    public WalletDto getWallet(@PathVariable UUID id) {
        return walletService.getWalletById(id);
    }
}
