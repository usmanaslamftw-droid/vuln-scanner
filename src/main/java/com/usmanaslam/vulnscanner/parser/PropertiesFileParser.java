package com.usmanaslam.vulnscanner.parser;

import com.usmanaslam.vulnscanner.model.Dependency;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.*;

@Component
public class PropertiesFileParser implements DependencyParser {

    private static final Map<String, String> PROP_TO_COORD = new HashMap<>();
    static {
        PROP_TO_COORD.put("spring-boot", "org.springframework.boot:spring-boot-starter-web");
        PROP_TO_COORD.put("lombok", "org.projectlombok:lombok");
        PROP_TO_COORD.put("jackson", "com.fasterxml.jackson.core:jackson-databind");
        PROP_TO_COORD.put("junit", "org.junit.jupiter:junit-jupiter");
        PROP_TO_COORD.put("slf4j", "org.slf4j:slf4j-api");
        PROP_TO_COORD.put("logback", "ch.qos.logback:logback-classic");
        PROP_TO_COORD.put("hibernate", "org.hibernate.orm:hibernate-core");
        PROP_TO_COORD.put("mysql", "com.mysql:mysql-connector-j");
        PROP_TO_COORD.put("postgresql", "org.postgresql:postgresql");
        PROP_TO_COORD.put("h2", "com.h2database:h2");
    }

    @Override
    public List<Dependency> parse(String content) {
        List<Dependency> dependencies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(content))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // Check for direct groupId:artifactId=version format
                if (line.matches("^[\\w.\\-]+:[\\w.\\-]+=.+")) {
                    int eqIdx = line.indexOf('=');
                    String coord = line.substring(0, eqIdx);
                    String version = line.substring(eqIdx + 1).trim();
                    String[] parts = coord.split(":");
                    if (parts.length == 2) {
                        dependencies.add(new Dependency(parts[0], parts[1], version));
                    }
                }
                // Check for name.version=x.y.z format
                else if (line.contains(".version=")) {
                    int eqIdx = line.indexOf('=');
                    String key = line.substring(0, eqIdx).trim();
                    String version = line.substring(eqIdx + 1).trim();
                    String lib = key.replace(".version", "");
                    if (PROP_TO_COORD.containsKey(lib)) {
                        String[] parts = PROP_TO_COORD.get(lib).split(":");
                        dependencies.add(new Dependency(parts[0], parts[1], version));
                    }
                }
            }
        } catch (Exception e) {
            // Log or ignore
        }
        return dependencies;
    }

    @Override
    public boolean supports(String fileType) {
        return "PROPERTIES".equalsIgnoreCase(fileType);
    }
}
