package com.usmanaslam.vulnscanner.repository;

import com.usmanaslam.vulnscanner.model.DependencyResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DependencyResultRepository extends JpaRepository<DependencyResult, Long> {
    List<DependencyResult> findByScanJobId(Long scanJobId);
}
