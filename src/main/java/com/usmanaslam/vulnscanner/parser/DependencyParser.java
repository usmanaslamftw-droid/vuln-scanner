package com.usmanaslam.vulnscanner.parser;

import com.usmanaslam.vulnscanner.model.Dependency;
import java.util.List;

public interface DependencyParser {
    List<Dependency> parse(String content);
    boolean supports(String fileType);
}
