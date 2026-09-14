package com.Garment.Garment_management.repository;

import com.Garment.Garment_management.model.PoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoItemRepository extends JpaRepository<PoItem, Long> {
}
