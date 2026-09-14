package com.Garment.Garment_management.repository;

import com.Garment.Garment_management.model.InspectionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionResultRepository extends JpaRepository<InspectionResult, Long> {
}