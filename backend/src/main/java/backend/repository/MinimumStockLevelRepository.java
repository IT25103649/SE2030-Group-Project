package backend.repository;

import backend.model.MinimumStockLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinimumStockLevelRepository
        extends JpaRepository<MinimumStockLevel, Integer> {
}