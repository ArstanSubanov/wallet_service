package arstansubanov.wallet_service.controller;

import arstansubanov.wallet_service.dto.WalletUpdateRequest;
import arstansubanov.wallet_service.enums.OperationType;
import arstansubanov.wallet_service.model.Wallet;
import arstansubanov.wallet_service.repository.WalletRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerIT {

    @Container
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:13.2")
                    .withDatabaseName("test_db")
                    .withUsername("test_user")
                    .withPassword("test_password");

    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = Wallet.builder()
                .balance(BigDecimal.valueOf(100))
                .build();
        wallet = walletRepository.save(wallet);
    }

    @Test
    void updateWalletDepositSuccess() throws Exception {
        WalletUpdateRequest request = new WalletUpdateRequest(
                wallet.getId(),
                OperationType.DEPOSIT,
                BigDecimal.valueOf(50)
        );

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wallet.getId().toString()))
                .andExpect(jsonPath("$.balance").value(150.0));

        Wallet updatedWallet = walletRepository.findById(wallet.getId()).orElseThrow();
        Assertions.assertEquals(0, BigDecimal.valueOf(150.0).compareTo(updatedWallet.getBalance()));
    }

    @Test
    void updateWalletWithdrawSuccess() throws Exception {
        WalletUpdateRequest request = new WalletUpdateRequest(
                wallet.getId(),
                OperationType.WITHDRAWAL,
                BigDecimal.valueOf(50)
        );

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wallet.getId().toString()))
                .andExpect(jsonPath("$.balance").value(50.0));

        Wallet updatedWallet = walletRepository.findById(wallet.getId()).orElseThrow();
        Assertions.assertEquals(0, BigDecimal.valueOf(50.0).compareTo(updatedWallet.getBalance()));
    }

    @Test
    void updateWalletFailWithInsufficientFunds() throws Exception {
        WalletUpdateRequest request = new WalletUpdateRequest(
                wallet.getId(),
                OperationType.WITHDRAWAL,
                BigDecimal.valueOf(150)
        );

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Insufficient funds to withdraw, balance:" +
                        " 100.00, withdraw amount: 150"));
    }

    @Test
    void getWalletSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/wallet/{id}", wallet.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wallet.getId().toString()))
                .andExpect(jsonPath("$.balance").value(100.00));
    }

    @Test
    void getWalletFailWithNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(get("/api/v1/wallet/{id}", randomId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
