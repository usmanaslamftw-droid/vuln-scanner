package com.usmanaslam.vulnscanner.service;

import com.usmanaslam.vulnscanner.dto.DependencyReport;
import com.usmanaslam.vulnscanner.dto.ScanRequest;
import com.usmanaslam.vulnscanner.dto.ScanResponse;
import com.usmanaslam.vulnscanner.model.Dependency;
import com.usmanaslam.vulnscanner.model.ScanJob;
import com.usmanaslam.vulnscanner.parser.DependencyParser;
import com.usmanaslam.vulnscanner.parser.PropertiesFileParser;
import com.usmanaslam.vulnscanner.repository.DependencyResultRepository;
import com.usmanaslam.vulnscanner.repository.ScanJobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ScanServiceTest {

    @Mock
    private VersionCheckService versionCheckService;

    @Mock
    private ScanJobRepository scanJobRepository;

    @Mock
    private DependencyResultRepository resultRepository;

    private ScanService scanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        List<DependencyParser> parsers = List.of(new PropertiesFileParser());
        scanService = new ScanService(parsers, versionCheckService, scanJobRepository, resultRepository);
    }

    @Test
    void testScan() {
        ScanRequest req = new ScanRequest("spring-boot.version=3.2.0", "PROPERTIES");
        
        when(scanJobRepository.save(any(ScanJob.class))).thenAnswer(i -> {
            ScanJob job = i.getArgument(0);
            if (job.getId() == null) job.setId(1L);
            return job;
        });
        
        DependencyReport report = new DependencyReport("org.springframework.boot", "spring-boot-starter-web", "3.2.0", "3.2.0", "UP_TO_DATE", "None");
        when(versionCheckService.checkVersion(any(Dependency.class))).thenReturn(CompletableFuture.completedFuture(report));
        
        ScanResponse response = scanService.scan(req);
        
        assertEquals(1, response.totalDependencies());
        assertEquals(1, response.upToDateCount());
        assertEquals("COMPLETED", response.status());
    }
}
