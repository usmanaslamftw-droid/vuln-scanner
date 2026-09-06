package com.usmanaslam.vulnscanner.model;

import jakarta.persistence.*;

@Entity
public class DependencyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "scan_job_id")
    private ScanJob scanJob;

    private String groupId;
    private String artifactId;
    private String currentVersion;
    private String latestVersion;

    @Enumerated(EnumType.STRING)
    private ScanStatus status;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ScanJob getScanJob() { return scanJob; }
    public void setScanJob(ScanJob scanJob) { this.scanJob = scanJob; }
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    public String getArtifactId() { return artifactId; }
    public void setArtifactId(String artifactId) { this.artifactId = artifactId; }
    public String getCurrentVersion() { return currentVersion; }
    public void setCurrentVersion(String currentVersion) { this.currentVersion = currentVersion; }
    public String getLatestVersion() { return latestVersion; }
    public void setLatestVersion(String latestVersion) { this.latestVersion = latestVersion; }
    public ScanStatus getStatus() { return status; }
    public void setStatus(ScanStatus status) { this.status = status; }
}
