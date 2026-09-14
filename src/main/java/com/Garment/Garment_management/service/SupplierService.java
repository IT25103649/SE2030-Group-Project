package com.Garment.Garment_management.service;

import com.Garment.Garment_management.dto.ProcurementDto;
import com.Garment.Garment_management.exception.ResourceNotFoundException;
import com.Garment.Garment_management.model.Supplier;
import com.Garment.Garment_management.model.SupplierStatus;
import com.Garment.Garment_management.repository.SupplierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Page<Supplier> getAllSuppliers(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return supplierRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
        }
        return supplierRepository.findAll(pageable);
    }

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
    }

    public Supplier createSupplier(ProcurementDto.SupplierRequest request) {
        Supplier supplier = Supplier.builder()
                .name(request.getName())
                .contactPerson(request.getContactPerson())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .status(request.getStatus() != null ? request.getStatus() : SupplierStatus.ACTIVE)
                .build();
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, ProcurementDto.SupplierRequest request) {
        Supplier supplier = getSupplierById(id);
        supplier.setName(request.getName());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());
        if (request.getStatus() != null) {
            supplier.setStatus(request.getStatus());
        }
        return supplierRepository.save(supplier);
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}
