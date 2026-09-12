package backend.controller;

import backend.model.Material;
import backend.service.MaterialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@CrossOrigin
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    // CREATE
    @PostMapping
    public Material createMaterial(@RequestBody Material material) {
        return materialService.saveMaterial(material);
    }

    // READ ALL
    @GetMapping
    public List<Material> getAllMaterials() {
        return materialService.getAllMaterials();
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable int id) {
        return materialService.getMaterialById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Material> updateMaterial(
            @PathVariable int id,
            @RequestBody Material material) {

        return materialService.getMaterialById(id)
                .map(existingMaterial -> {
                    material.setMaterialId(id);
                    return ResponseEntity.ok(
                            materialService.saveMaterial(material)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable int id) {

        if (materialService.getMaterialById(id).isPresent()) {
            materialService.deleteMaterial(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}