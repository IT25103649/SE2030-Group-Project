package backend.controller;

import backend.model.StockTransaction;
import backend.model.TransactionType;
import backend.service.StockTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-transactions")
@CrossOrigin
public class StockTransactionController {

    private final StockTransactionService stockTransactionService;

    public StockTransactionController(
            StockTransactionService stockTransactionService) {

        this.stockTransactionService = stockTransactionService;
    }

    @PostMapping
    public ResponseEntity<?> processTransaction(
            @RequestParam int materialId,
            @RequestParam TransactionType transactionType,
            @RequestParam int quantity,
            @RequestParam String batchNumber,
            @RequestParam String storageLocation) {

        try {

            StockTransaction transaction =
                    stockTransactionService.processTransaction(
                            materialId,
                            transactionType,
                            quantity,
                            batchNumber,
                            storageLocation
                    );

            return ResponseEntity.ok(transaction);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getTransactionHistory() {

        return ResponseEntity.ok(
                stockTransactionService.getAllTransactions()
        );
    }
}