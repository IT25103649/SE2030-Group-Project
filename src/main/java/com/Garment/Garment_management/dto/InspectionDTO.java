package com.Garment.Garment_management.dto;

import jakarta.validation.constraints.*;

public class InspectionDTO {

    private Long id;

    @NotBlank(message = "Batch ID is required")
    @Size(max = 50, message = "Batch ID cannot exceed 50 characters")
    private String batchId;

    @NotBlank(message = "Inspector name is required")
    @Size(max = 100, message = "Inspector name cannot exceed 100 characters")
    private String inspectorName;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(PASS|FAIL|REWORK)$", message = "Invalid status. Allowed values: PASS, FAIL, REWORK")
    private String resultStatus;

    private String defectType;

    @NotBlank(message = "Severity is required")
    private String severity;

    @NotNull(message = "Defective quantity is required")
    @Min(value = 1, message = "Defective quantity must be at least 1")
    private Integer defectiveQuantity;

    @NotBlank(message = "Remarks are required")
    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;

    private String photoUrl;

    public InspectionDTO() {}

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
}