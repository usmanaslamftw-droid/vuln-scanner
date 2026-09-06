package com.usmanaslam.vulnscanner.controller;

import com.usmanaslam.vulnscanner.dto.DependencyReport;
import com.usmanaslam.vulnscanner.dto.ScanRequest;
import com.usmanaslam.vulnscanner.dto.ScanResponse;
import com.usmanaslam.vulnscanner.service.FileUploadService;
import com.usmanaslam.vulnscanner.service.ScanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scans")
@Tag(name = "Vulnerability Scanner", description = "Scan dependencies for outdated versions")
public class ScanController {

    private final ScanService scanService;
    private final FileUploadService fileUploadService;

    public ScanController(ScanService scanService, FileUploadService fileUploadService) {
        this.scanService = scanService;
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Upload a file to scan", description = "Uploads a .properties, .gradle, or pom.xml file for dependency scanning.")
    @ApiResponse(responseCode = "202", description = "Scan initiated")
    public ScanResponse uploadFile(@RequestParam("file") MultipartFile file) {
        ScanRequest request = fileUploadService.processUpload(file);
        return scanService.scan(request);
    }

    @PostMapping("/text")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Submit text content to scan", description = "Submit file content directly as text for scanning.")
    @ApiResponse(responseCode = "202", description = "Scan initiated")
    public ScanResponse scanText(@RequestBody ScanRequest request) {
        return scanService.scan(request);
    }

    @GetMapping
    @Operation(summary = "Get all scans", description = "Retrieve a list of all dependency scans.")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public List<ScanResponse> getAllScans() {
        return scanService.getAllScans();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a scan by ID", description = "Retrieve a specific scan by its ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Scan not found")
    public ScanResponse getScan(@PathVariable Long id) {
        return scanService.getScan(id);
    }

    @GetMapping("/{id}/report")
    @Operation(summary = "Get a dependency report for a scan", description = "Retrieve the detailed dependency report for a specific scan ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Scan not found")
    public List<DependencyReport> getReport(@PathVariable Long id) {
        return scanService.getReport(id);
    }
}
