package com.usmanaslam.vulnscanner.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.usmanaslam.vulnscanner.dto.DependencyReport;
import com.usmanaslam.vulnscanner.model.Dependency;
import com.usmanaslam.vulnscanner.model.ScanStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class VersionCheckService {

    private final RestTemplate restTemplate;

    public VersionCheckService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async("taskExecutor")
    public CompletableFuture<DependencyReport> checkVersion(Dependency dep) {
        String url = String.format("https://search.maven.org/solrsearch/select?q=g:\"%s\"+AND+a:\"%s\"&rows=1&wt=json",
                dep.getGroupId(), dep.getArtifactId());
        
        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            if (response != null && response.has("response") && response.get("response").has("docs") && response.get("response").get("docs").size() > 0) {
                String latestVersion = response.get("response").get("docs").get(0).get("latestVersion").asText();
                
                ScanStatus status = dep.getCurrentVersion().equals(latestVersion) ? ScanStatus.UP_TO_DATE : ScanStatus.OUTDATED;
                String recommendation = status == ScanStatus.OUTDATED ? "Update to " + latestVersion : "None";
                
                return CompletableFuture.completedFuture(new DependencyReport(
                        dep.getGroupId(), dep.getArtifactId(), dep.getCurrentVersion(), latestVersion, status.name(), recommendation
                ));
            }
        } catch (Exception e) {
            // Fallback for errors
        }
        
        return CompletableFuture.completedFuture(new DependencyReport(
                dep.getGroupId(), dep.getArtifactId(), dep.getCurrentVersion(), "Unknown", ScanStatus.UNKNOWN.name(), "Could not verify"
        ));
    }
}
