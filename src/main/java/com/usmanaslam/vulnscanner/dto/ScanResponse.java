package com.usmanaslam.vulnscanner.dto;

import java.time.LocalDateTime;

public record ScanResponse(
        Long id,
        String status,
        int totalDependencies,
        int outdatedCount,
        int upToDateCount,
        int unknownCount,
        LocalDateTime scannedAt
) {}
