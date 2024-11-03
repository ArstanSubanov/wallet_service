package arstansubanov.wallet_service.mapper;

import arstansubanov.wallet_service.dto.WalletDto;
import arstansubanov.wallet_service.model.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletMapper {

    @Mapping(target = "id", source = "wallet.id")
    @Mapping(target = "balance", source = "wallet.balance")
    WalletDto toWalletDto(Wallet wallet);
}
