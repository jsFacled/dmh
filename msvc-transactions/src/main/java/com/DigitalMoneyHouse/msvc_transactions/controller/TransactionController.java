package com.DigitalMoneyHouse.msvc_transactions.controller;
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/account/{id}/last-five")
    public ResponseEntity<List<TransactionDTO>> getLastFiveTransactions(@PathVariable Long id) {
        List<TransactionDTO> transactions = transactionService.findLastFiveTransactions(id);
        return ResponseEntity.ok(transactions);
    }
}
