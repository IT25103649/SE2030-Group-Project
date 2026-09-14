package com.Garment.Garment_management.repository;

import com.Garment.Garment_management.model.PoStatus;
import com.Garment.Garment_management.model.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Page<PurchaseOrder> findBySupplierId(Long supplierId, Pageable pageable);
    Page<PurchaseOrder> findByStatus(PoStatus status, Pageable pageable);
    List<PurchaseOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
}
