package com.Garment.Garment_management.service;

import com.Garment.Garment_management.model.PurchaseOrder;
import com.Garment.Garment_management.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcurementReportService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    public ProcurementReportService(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public Map<String, Object> generateProcurementReport(LocalDate startDate, LocalDate endDate) {
        List<PurchaseOrder> orders = purchaseOrderRepository.findByOrderDateBetween(startDate, endDate);

        BigDecimal totalSpent = orders.stream()
                .map(PurchaseOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pendingCount = orders.stream().filter(po -> po.getStatus().name().equals("PENDING")).count();
        long receivedCount = orders.stream().filter(po -> po.getStatus().name().equals("RECEIVED")).count();

        Map<String, Object> report = new HashMap<>();
        report.put("totalOrders", orders.size());
        report.put("totalSpent", totalSpent);
        report.put("pendingOrders", pendingCount);
        report.put("receivedOrders", receivedCount);
        report.put("periodStart", startDate);
        report.put("periodEnd", endDate);

        return report;
    }
}
