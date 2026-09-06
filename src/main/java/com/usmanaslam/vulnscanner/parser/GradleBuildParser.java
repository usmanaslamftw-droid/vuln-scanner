package com.usmanaslam.vulnscanner.parser;

import com.usmanaslam.vulnscanner.model.Dependency;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GradleBuildParser implements DependencyParser {

    private static final Pattern GRADLE_DEP_PATTERN = Pattern.compile(
            "(?:implementation|testImplementation|runtimeOnly|compileOnly|api|testRuntimeOnly)\\s+['\"]([^:'\"]+):([^:'\"]+):([^:'\"]+)['\"]"
    );

    @Override
    public List<Dependency> parse(String content) {
        List<Dependency> dependencies = new ArrayList<>();
        Matcher matcher = GRADLE_DEP_PATTERN.matcher(content);
        while (matcher.find()) {
            String groupId = matcher.group(1);
            String artifactId = matcher.group(2);
            String version = matcher.group(3);
            dependencies.add(new Dependency(groupId, artifactId, version));
        }
        return dependencies;
    }

    @Override
    public boolean supports(String fileType) {
        return "GRADLE".equalsIgnoreCase(fileType);
    }
}
