package backend.service;

import backend.model.Material;
import backend.model.MinimumStockLevel;
import backend.repository.MaterialRepository;
import backend.repository.MinimumStockLevelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MinimumStockLevelService {

    private final MinimumStockLevelRepository minimumStockLevelRepository;
    private final MaterialRepository materialRepository;

    public MinimumStockLevelService(
            MinimumStockLevelRepository minimumStockLevelRepository,
            MaterialRepository materialRepository) {

        this.minimumStockLevelRepository = minimumStockLevelRepository;
        this.materialRepository = materialRepository;
    }

    public MinimumStockLevel setMinimumStockLevel(
            int materialId,
            int minimumQuantity) {

        if (minimumQuantity < 0) {
            throw new IllegalArgumentException(
                    "Minimum quantity cannot be negative"
            );
        }

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Material not found"
                        ));

        MinimumStockLevel minimumStockLevel =
                new MinimumStockLevel();

        minimumStockLevel.setMaterial(material);
        minimumStockLevel.setMinimumQuantity(minimumQuantity);

        return minimumStockLevelRepository.save(
                minimumStockLevel
        );
    }

    public List<MinimumStockLevel> getAllMinimumStockLevels() {
        return minimumStockLevelRepository.findAll();
    }

    public boolean isLowStock(int materialId) {

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Material not found"
                        ));

        List<MinimumStockLevel> levels =
                minimumStockLevelRepository.findAll();

        for (MinimumStockLevel level : levels) {

            if (level.getMaterial().getMaterialId() == materialId) {

                return material.getQuantity()
                        <= level.getMinimumQuantity();
            }
        }

        return false;
    }
}

