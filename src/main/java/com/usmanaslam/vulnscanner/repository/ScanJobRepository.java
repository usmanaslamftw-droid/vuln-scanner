package com.usmanaslam.vulnscanner.repository;

import com.usmanaslam.vulnscanner.model.ScanJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanJobRepository extends JpaRepository<ScanJob, Long> {
}
