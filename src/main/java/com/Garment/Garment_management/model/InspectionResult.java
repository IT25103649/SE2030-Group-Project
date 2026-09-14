package com.Garment.Garment_management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_results")
public class InspectionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_id", nullable = false, length = 50)
    private String batchId;

    @Column(name = "inspector_name", nullable = false, length = 100)
    private String inspectorName;

    @Column(name = "result_status", nullable = false, length = 20)
    private String resultStatus;

    @Column(name = "defect_type", length = 100)
    private String defectType;

    @Column(nullable = false, length = 20)
    private String severity;

    @Column(name = "defective_quantity", nullable = false)
    private Integer defectiveQuantity;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Lob
    @Column(name = "photo_url", columnDefinition = "LONGTEXT")
    private String photoUrl;

    @Column(name = "inspection_date", updatable = false)
    private LocalDateTime inspectionDate;

    public InspectionResult() {}

    @PrePersist
    protected void onCreate() {
        this.inspectionDate = LocalDateTime.now();
        sanitizeInputs();
    }

    @PreUpdate
    protected void onUpdate() {
        sanitizeInputs();
    }

    private void sanitizeInputs() {
        if (this.batchId != null) this.batchId = this.batchId.replaceAll("<[^>]*>", "").trim();
        if (this.inspectorName != null) this.inspectorName = this.inspectorName.replaceAll("<[^>]*>", "").trim();
        if (this.remarks != null) this.remarks = this.remarks.replaceAll("<[^>]*>", "").trim();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }

    public String getInspectorName() { return inspectorName; }
    public void setInspectorName(String inspectorName) { this.inspectorName = inspectorName; }

    public String getResultStatus() { return resultStatus; }
    public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }

    public String getDefectType() { return defectType; }
    public void setDefectType(String defectType) { this.defectType = defectType; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public Integer getDefectiveQuantity() { return defectiveQuantity; }
    public void setDefectiveQuantity(Integer defectiveQuantity) { this.defectiveQuantity = defectiveQuantity; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public LocalDateTime getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(LocalDateTime inspectionDate) { this.inspectionDate = inspectionDate; }
}