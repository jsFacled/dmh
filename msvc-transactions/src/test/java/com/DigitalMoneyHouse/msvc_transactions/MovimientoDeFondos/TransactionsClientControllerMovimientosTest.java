package com.DigitalMoneyHouse.msvc_transactions.MovimientoDeFondos;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(TransactionsClientController.class)
public class TransactionsClientControllerMovimientosTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionClientService transactionClientService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TransactionsClientController(transactionClientService)).build();
    }

    @Test
    public void testExecuteTransfer() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(
                1L, LocalDateTime.now(), 1L, TransactionType.RECHARGE_FROM_CARD,
                ProductOriginType.CARD, 1L, 2L, BigDecimal.valueOf(100), "Test transaction"
        );

        when(transactionClientService.executeTransfer(any(TransactionDTO.class))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/accounts/1/transactions")
                        .contentType("application/json")
                        .content("{\"originAccountId\":1,\"type\":\"RECHARGE_FROM_CARD\",\"productOriginType\":\"CARD\",\"productOriginId\":1,\"destinationAccountId\":2,\"amount\":100,\"description\":\"Test transaction\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTransactionsByAccountId() throws Exception {
        when(transactionClientService.getTransactionsByAccountId(1L))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/accounts/1/activity"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}
