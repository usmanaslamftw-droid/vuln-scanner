package com.usmanaslam.vulnscanner.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ScanJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileType;
    private int totalDependencies;
    private int outdatedCount;
    private int upToDateCount;
    private int unknownCount;
    private LocalDateTime scannedAt;
    
    private String status; // PENDING/COMPLETED/FAILED

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public int getTotalDependencies() { return totalDependencies; }
    public void setTotalDependencies(int totalDependencies) { this.totalDependencies = totalDependencies; }
    public int getOutdatedCount() { return outdatedCount; }
    public void setOutdatedCount(int outdatedCount) { this.outdatedCount = outdatedCount; }
    public int getUpToDateCount() { return upToDateCount; }
    public void setUpToDateCount(int upToDateCount) { this.upToDateCount = upToDateCount; }
    public int getUnknownCount() { return unknownCount; }
    public void setUnknownCount(int unknownCount) { this.unknownCount = unknownCount; }
    public LocalDateTime getScannedAt() { return scannedAt; }
    public void setScannedAt(LocalDateTime scannedAt) { this.scannedAt = scannedAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
