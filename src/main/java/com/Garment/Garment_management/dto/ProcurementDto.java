package com.Garment.Garment_management.dto;

import com.Garment.Garment_management.model.PoStatus;
import com.Garment.Garment_management.model.SupplierStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ProcurementDto {
    @Data
    public static class SupplierRequest {
        private String name;
        private String contactPerson;
        private String email;
        private String phone;
        private String address;
        private SupplierStatus status;
    }

    @Data
    public static class PoItemRequest {
        private Long materialId;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
    }

    @Data
    public static class PurchaseOrderRequest {
        private Long supplierId;
        private LocalDate expectedDate;
        private List<PoItemRequest> items;
    }
}
