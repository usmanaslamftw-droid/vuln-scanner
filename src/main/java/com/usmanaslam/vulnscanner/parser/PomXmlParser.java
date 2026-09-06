package com.usmanaslam.vulnscanner.parser;

import com.usmanaslam.vulnscanner.model.Dependency;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PomXmlParser implements DependencyParser {

    private static final Pattern POM_DEP_PATTERN = Pattern.compile(
            "<dependency>\\s*<groupId>([^<]+)</groupId>\\s*<artifactId>([^<]+)</artifactId>\\s*<version>([^<]+)</version>"
    );

    @Override
    public List<Dependency> parse(String content) {
        List<Dependency> dependencies = new ArrayList<>();
        Matcher matcher = POM_DEP_PATTERN.matcher(content);
        while (matcher.find()) {
            dependencies.add(new Dependency(matcher.group(1).trim(), matcher.group(2).trim(), matcher.group(3).trim()));
        }
        return dependencies;
    }

    @Override
    public boolean supports(String fileType) {
        return "POM".equalsIgnoreCase(fileType);
    }
}
