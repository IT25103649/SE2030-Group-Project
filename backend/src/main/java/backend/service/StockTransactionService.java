package backend.service;

import backend.model.Material;
import backend.model.StockTransaction;
import backend.model.TransactionType;
import backend.repository.MaterialRepository;
import backend.repository.StockTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockTransactionService {

    private final StockTransactionRepository stockTransactionRepository;
    private final MaterialRepository materialRepository;

    public StockTransactionService(
            StockTransactionRepository stockTransactionRepository,
            MaterialRepository materialRepository) {

        this.stockTransactionRepository = stockTransactionRepository;
        this.materialRepository = materialRepository;
    }

    @Transactional
    public StockTransaction processTransaction(
            int materialId,
            TransactionType transactionType,
            int quantity,
            String batchNumber,
            String storageLocation) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0"
            );
        }

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Material not found"
                        ));

        if (transactionType == TransactionType.STOCK_OUT) {

            if (material.getQuantity() < quantity) {
                throw new IllegalArgumentException(
                        "Insufficient stock"
                );
            }

            material.setQuantity(
                    material.getQuantity() - quantity
            );

        } else if (transactionType == TransactionType.STOCK_IN) {

            material.setQuantity(
                    material.getQuantity() + quantity
            );
        }

        materialRepository.save(material);

        StockTransaction transaction = new StockTransaction();

        transaction.setMaterial(material);
        transaction.setTransactionType(transactionType);
        transaction.setQuantity(quantity);
        transaction.setBatchNumber(batchNumber);
        transaction.setStorageLocation(storageLocation);

        return stockTransactionRepository.save(transaction);
    }
}
