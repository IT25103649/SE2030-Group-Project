package com.Garment.Garment_management.controller;

import com.Garment.Garment_management.dto.ProcurementDto;
import com.Garment.Garment_management.model.PoStatus;
import com.Garment.Garment_management.model.PurchaseOrder;
import com.Garment.Garment_management.model.User;
import com.Garment.Garment_management.security.CustomUserDetails;
import com.Garment.Garment_management.service.PurchaseOrderService;
import com.Garment.Garment_management.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/procurement/orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;
    private final UserService userService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService, UserService userService) {
        this.purchaseOrderService = purchaseOrderService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_PROCUREMENT') or hasRole('ADMIN')")
    public ResponseEntity<Page<PurchaseOrder>> getPurchaseOrders(
            @RequestParam(required = false) PoStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(purchaseOrderService.getAllPurchaseOrders(status, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_PROCUREMENT') or hasRole('ADMIN')")
    public ResponseEntity<PurchaseOrder> getPurchaseOrder(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrderById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_PROCUREMENT') or hasRole('ADMIN')")
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(
            @RequestBody ProcurementDto.PurchaseOrderRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.getUserById(userDetails.getId());
        return ResponseEntity.ok(purchaseOrderService.createPurchaseOrder(request, user));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('MANAGE_PROCUREMENT') or hasRole('ADMIN')")
    public ResponseEntity<PurchaseOrder> updateStatus(@PathVariable Long id, @RequestParam PoStatus status) {
        return ResponseEntity.ok(purchaseOrderService.updatePurchaseOrderStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePurchaseOrder(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);
        return ResponseEntity.ok("Purchase Order deleted successfully");
    }
}
