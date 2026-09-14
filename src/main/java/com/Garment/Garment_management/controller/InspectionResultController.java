package com.Garment.Garment_management.controller;

import com.Garment.Garment_management.dto.InspectionDTO;
import com.Garment.Garment_management.service.InspectionResultService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inspections")
@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
public class InspectionResultController {

    private final InspectionResultService service;

    public InspectionResultController(InspectionResultService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InspectionDTO>> getAllInspections() {
        return ResponseEntity.ok(service.getAllInspections());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InspectionDTO> getInspectionById(@PathVariable Long id) {
        return service.getInspectionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InspectionDTO> saveInspection(@Valid @RequestBody InspectionDTO dto) {
        InspectionDTO saved = service.saveInspection(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInspection(@PathVariable Long id) {
        service.deleteInspection(id);
        return ResponseEntity.noContent().build();
    }
}