package com.usmanaslam.vulnscanner.parser;

import com.usmanaslam.vulnscanner.model.Dependency;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesFileParserTest {

    private final PropertiesFileParser parser = new PropertiesFileParser();

    @Test
    void testParse() {
        String content = "spring-boot.version=3.2.0\n" +
                "lombok.version=1.18.28\n" +
                "org.apache.commons:commons-lang3=3.12.0";
        
        List<Dependency> deps = parser.parse(content);
        
        assertEquals(3, deps.size());
        
        assertTrue(deps.stream().anyMatch(d -> d.getArtifactId().equals("spring-boot-starter-web") && d.getCurrentVersion().equals("3.2.0")));
        assertTrue(deps.stream().anyMatch(d -> d.getArtifactId().equals("commons-lang3") && d.getCurrentVersion().equals("3.12.0")));
    }
}
