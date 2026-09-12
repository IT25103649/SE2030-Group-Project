package backend.controller;

import backend.model.MinimumStockLevel;
import backend.service.MinimumStockLevelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/minimum-stock")
@CrossOrigin
public class MinimumStockLevelController {

    private final MinimumStockLevelService minimumStockLevelService;

    public MinimumStockLevelController(
            MinimumStockLevelService minimumStockLevelService) {
        this.minimumStockLevelService = minimumStockLevelService;
    }

    @PostMapping
    public ResponseEntity<?> setMinimumStockLevel(
            @RequestParam int materialId,
            @RequestParam int minimumQuantity) {

        try {

            MinimumStockLevel level =
                    minimumStockLevelService.setMinimumStockLevel(
                            materialId,
                            minimumQuantity
                    );

            return ResponseEntity.ok(level);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public List<MinimumStockLevel> getAllMinimumStockLevels() {
        return minimumStockLevelService.getAllMinimumStockLevels();
    }

    @GetMapping("/check/{materialId}")
    public ResponseEntity<?> checkLowStock(
            @PathVariable int materialId) {

        try {

            boolean lowStock =
                    minimumStockLevelService.isLowStock(materialId);

            return ResponseEntity.ok(lowStock);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}