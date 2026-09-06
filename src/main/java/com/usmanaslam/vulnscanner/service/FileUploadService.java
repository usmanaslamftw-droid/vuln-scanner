package com.usmanaslam.vulnscanner.service;

import com.usmanaslam.vulnscanner.dto.ScanRequest;
import com.usmanaslam.vulnscanner.exception.UnsupportedFileTypeException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class FileUploadService {

    public ScanRequest processUpload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IllegalArgumentException("Filename is missing");
        }
        
        String fileType;
        if (filename.endsWith(".properties")) {
            fileType = "PROPERTIES";
        } else if (filename.endsWith(".gradle")) {
            fileType = "GRADLE";
        } else if (filename.endsWith("pom.xml") || filename.endsWith(".xml")) {
            fileType = "POM";
        } else {
            throw new UnsupportedFileTypeException("Unsupported file extension");
        }
        
        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            return new ScanRequest(content, fileType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }
}
