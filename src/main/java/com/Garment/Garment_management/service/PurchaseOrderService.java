package com.Garment.Garment_management.service;

import com.Garment.Garment_management.dto.ProcurementDto;
import com.Garment.Garment_management.exception.CustomException;
import com.Garment.Garment_management.exception.ResourceNotFoundException;
import com.Garment.Garment_management.model.*;
import com.Garment.Garment_management.repository.PurchaseOrderRepository;
import com.Garment.Garment_management.repository.RawMaterialRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierService supplierService;
    private final RawMaterialRepository rawMaterialRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository,
                                SupplierService supplierService,
                                RawMaterialRepository rawMaterialRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.supplierService = supplierService;
        this.rawMaterialRepository = rawMaterialRepository;
    }

    public Page<PurchaseOrder> getAllPurchaseOrders(PoStatus status, Pageable pageable) {
        if (status != null) {
            return purchaseOrderRepository.findByStatus(status, pageable);
        }
        return purchaseOrderRepository.findAll(pageable);
    }

    public PurchaseOrder getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with id: " + id));
    }

    @Transactional
    public PurchaseOrder createPurchaseOrder(ProcurementDto.PurchaseOrderRequest request, User createdBy) {
        Supplier supplier = supplierService.getSupplierById(request.getSupplierId());

        if (supplier.getStatus() != SupplierStatus.ACTIVE) {
            throw new CustomException("Cannot create PO for inactive supplier");
        }

        PurchaseOrder po = PurchaseOrder.builder()
                .supplier(supplier)
                .orderDate(LocalDate.now())
                .expectedDate(request.getExpectedDate())
                .status(PoStatus.PENDING)
                .createdBy(createdBy)
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<PoItem> items = request.getItems().stream().map(itemReq -> {
            RawMaterial material = rawMaterialRepository.findById(itemReq.getMaterialId())
                    .orElseThrow(() -> new CustomException("Material not found: " + itemReq.getMaterialId()));
            
            return PoItem.builder()
                    .purchaseOrder(po)
                    .material(material)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(itemReq.getUnitPrice())
                    .build();
        }).collect(Collectors.toList());

        for (PoItem item : items) {
            totalAmount = totalAmount.add(item.getUnitPrice().multiply(item.getQuantity()));
        }

        po.setItems(items);
        po.setTotalAmount(totalAmount);

        return purchaseOrderRepository.save(po);
    }

    @Transactional
    public PurchaseOrder updatePurchaseOrderStatus(Long id, PoStatus status) {
        PurchaseOrder po = getPurchaseOrderById(id);
        
        if (po.getStatus() == PoStatus.CANCELLED || po.getStatus() == PoStatus.RECEIVED) {
            throw new CustomException("Cannot change status of a completed/cancelled PO");
        }

        po.setStatus(status);
        return purchaseOrderRepository.save(po);
    }

    public void deletePurchaseOrder(Long id) {
        PurchaseOrder po = getPurchaseOrderById(id);
        if (po.getStatus() != PoStatus.PENDING) {
            throw new CustomException("Can only delete PENDING purchase orders");
        }
        purchaseOrderRepository.deleteById(id);
    }
}
