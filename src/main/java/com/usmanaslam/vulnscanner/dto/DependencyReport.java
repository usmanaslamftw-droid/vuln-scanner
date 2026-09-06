package com.usmanaslam.vulnscanner.dto;

public record DependencyReport(
        String groupId,
        String artifactId,
        String currentVersion,
        String latestVersion,
        String status,
        String recommendation
) {}
