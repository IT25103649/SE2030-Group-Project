package backend.service;

import backend.model.Material;
import backend.repository.MaterialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    // Create / Update
    public Material saveMaterial(Material material) {
        return materialRepository.save(material);
    }

    // Read all
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    // Read one
    public Optional<Material> getMaterialById(int id) {
        return materialRepository.findById(id);
    }

    // Delete
    public void deleteMaterial(int id) {
        materialRepository.deleteById(id);
    }
}
