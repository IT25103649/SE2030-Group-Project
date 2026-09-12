package backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "minimum_stock_levels")
public class MinimumStockLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int minimumStockId;

    @OneToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(nullable = false)
    private int minimumQuantity;

    public MinimumStockLevel() {
    }

    public int getMinimumStockId() {
        return minimumStockId;
    }

    public void setMinimumStockId(int minimumStockId) {
        this.minimumStockId = minimumStockId;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }
}