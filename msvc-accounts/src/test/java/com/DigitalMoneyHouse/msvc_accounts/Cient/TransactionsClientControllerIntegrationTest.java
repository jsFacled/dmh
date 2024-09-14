package com.DigitalMoneyHouse.msvc_accounts.Cient;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.TransactionsClientController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransactionsClientController.class)
public class TransactionsClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetLastFiveRecipients() throws Exception {
        // Execute
        mockMvc.perform(get("/accounts/1/transferences"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].cvu").value("CVU123"))
                .andExpect(jsonPath("$[0].alias").value("Alias1"));
    }

    @Test
    void testExecuteTransfer() throws Exception {
        // Setup
        String jsonRequest = "{ \"originAccountId\": 1, \"type\": \"TRANSFER_BETWEEN_ACCOUNTS\", \"productOriginType\": \"ACCOUNT\", \"productOriginId\": 1, \"destinationAccountId\": 2, \"amount\": 100, \"description\": \"Test transfer\" }";

        // Execute
        mockMvc.perform(post("/accounts/1/transferences")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    // Additional integration tests for error cases
}
