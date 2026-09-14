package com.Garment.Garment_management.service;

import com.Garment.Garment_management.dto.InspectionDTO;
import com.Garment.Garment_management.model.InspectionResult;
import com.Garment.Garment_management.repository.InspectionResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InspectionResultService {

    private final InspectionResultRepository repository;

    public InspectionResultService(InspectionResultRepository repository) {
        this.repository = repository;
    }

    public List<InspectionDTO> getAllInspections() {
        return repository.findAll().stream()
                .map(entity -> convertToDTO(entity))
                .collect(Collectors.toList());
    }

    public Optional<InspectionDTO> getInspectionById(Long id) {
        return repository.findById(id).map(entity -> convertToDTO(entity));
    }

    public InspectionDTO saveInspection(InspectionDTO dto) {
        InspectionResult entity = convertToEntity(dto);
        InspectionResult saved = repository.save(entity);

        if ("REWORK".equalsIgnoreCase(saved.getResultStatus())) {
            notifyProductionPlanningManager(saved.getBatchId());
        }

        return convertToDTO(saved);
    }

    public void deleteInspection(Long id) {
        repository.deleteById(id);
    }

    private void notifyProductionPlanningManager(String batchId) {
        System.out.println("[SECURITY & SERVICE LOG] Production Planning Manager notified for REWORK on Batch ID: " + batchId);
    }

    private InspectionDTO convertToDTO(InspectionResult entity) {
        InspectionDTO dto = new InspectionDTO();
        dto.setId(entity.getId());
        dto.setBatchId(entity.getBatchId());
        dto.setInspectorName(entity.getInspectorName());
        dto.setResultStatus(entity.getResultStatus());
        dto.setDefectType(entity.getDefectType());
        dto.setSeverity(entity.getSeverity());
        dto.setDefectiveQuantity(entity.getDefectiveQuantity());
        dto.setRemarks(entity.getRemarks());
        dto.setPhotoUrl(entity.getPhotoUrl());
        return dto;
    }

    private InspectionResult convertToEntity(InspectionDTO dto) {
        InspectionResult entity = new InspectionResult();
        entity.setId(dto.getId());
        entity.setBatchId(dto.getBatchId());
        entity.setInspectorName(dto.getInspectorName());
        entity.setResultStatus(dto.getResultStatus());
        entity.setDefectType(dto.getDefectType());
        entity.setSeverity(dto.getSeverity());
        entity.setDefectiveQuantity(dto.getDefectiveQuantity());
        entity.setRemarks(dto.getRemarks());
        entity.setPhotoUrl(dto.getPhotoUrl());
        return entity;
    }
}