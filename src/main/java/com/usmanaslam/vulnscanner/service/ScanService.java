package com.usmanaslam.vulnscanner.service;

import com.usmanaslam.vulnscanner.dto.DependencyReport;
import com.usmanaslam.vulnscanner.dto.ScanRequest;
import com.usmanaslam.vulnscanner.dto.ScanResponse;
import com.usmanaslam.vulnscanner.exception.ScanNotFoundException;
import com.usmanaslam.vulnscanner.exception.UnsupportedFileTypeException;
import com.usmanaslam.vulnscanner.model.Dependency;
import com.usmanaslam.vulnscanner.model.DependencyResult;
import com.usmanaslam.vulnscanner.model.ScanJob;
import com.usmanaslam.vulnscanner.model.ScanStatus;
import com.usmanaslam.vulnscanner.parser.DependencyParser;
import com.usmanaslam.vulnscanner.repository.DependencyResultRepository;
import com.usmanaslam.vulnscanner.repository.ScanJobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ScanService {

    private final List<DependencyParser> parsers;
    private final VersionCheckService versionCheckService;
    private final ScanJobRepository scanJobRepository;
    private final DependencyResultRepository resultRepository;

    public ScanService(List<DependencyParser> parsers, VersionCheckService versionCheckService,
                       ScanJobRepository scanJobRepository, DependencyResultRepository resultRepository) {
        this.parsers = parsers;
        this.versionCheckService = versionCheckService;
        this.scanJobRepository = scanJobRepository;
        this.resultRepository = resultRepository;
    }

    public ScanResponse scan(ScanRequest request) {
        DependencyParser parser = parsers.stream()
                .filter(p -> p.supports(request.fileType()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedFileTypeException("File type not supported: " + request.fileType()));

        List<Dependency> deps = parser.parse(request.content());

        ScanJob job = new ScanJob();
        job.setFileType(request.fileType());
        job.setTotalDependencies(deps.size());
        job.setScannedAt(LocalDateTime.now());
        job.setStatus("PENDING");
        scanJobRepository.save(job);

        List<CompletableFuture<DependencyReport>> futures = deps.stream()
                .map(versionCheckService::checkVersion)
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        int outdated = 0;
        int upToDate = 0;
        int unknown = 0;

        for (CompletableFuture<DependencyReport> future : futures) {
            try {
                DependencyReport report = future.get();
                DependencyResult res = new DependencyResult();
                res.setScanJob(job);
                res.setGroupId(report.groupId());
                res.setArtifactId(report.artifactId());
                res.setCurrentVersion(report.currentVersion());
                res.setLatestVersion(report.latestVersion());
                res.setStatus(ScanStatus.valueOf(report.status()));
                resultRepository.save(res);

                if (res.getStatus() == ScanStatus.OUTDATED) outdated++;
                else if (res.getStatus() == ScanStatus.UP_TO_DATE) upToDate++;
                else unknown++;
            } catch (Exception e) {
                unknown++;
            }
        }

        job.setOutdatedCount(outdated);
        job.setUpToDateCount(upToDate);
        job.setUnknownCount(unknown);
        job.setStatus("COMPLETED");
        scanJobRepository.save(job);

        return mapToResponse(job);
    }

    public ScanResponse getScan(Long id) {
        ScanJob job = scanJobRepository.findById(id)
                .orElseThrow(() -> new ScanNotFoundException("Scan not found with ID: " + id));
        return mapToResponse(job);
    }

    public List<ScanResponse> getAllScans() {
        return scanJobRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DependencyReport> getReport(Long id) {
        ScanJob job = scanJobRepository.findById(id)
                .orElseThrow(() -> new ScanNotFoundException("Scan not found with ID: " + id));

        return resultRepository.findByScanJobId(id).stream()
                .map(res -> new DependencyReport(
                        res.getGroupId(), res.getArtifactId(), res.getCurrentVersion(), res.getLatestVersion(),
                        res.getStatus().name(),
                        res.getStatus() == ScanStatus.OUTDATED ? "Update to " + res.getLatestVersion() : "None"
                ))
                .collect(Collectors.toList());
    }

    private ScanResponse mapToResponse(ScanJob job) {
        return new ScanResponse(job.getId(), job.getStatus(), job.getTotalDependencies(),
                job.getOutdatedCount(), job.getUpToDateCount(), job.getUnknownCount(), job.getScannedAt());
    }
}
