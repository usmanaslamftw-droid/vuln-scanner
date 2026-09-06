package com.usmanaslam.vulnscanner.parser;

import com.usmanaslam.vulnscanner.model.Dependency;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradleBuildParserTest {

    private final GradleBuildParser parser = new GradleBuildParser();

    @Test
    void testParse() {
        String content = "dependencies {\n" +
                "    implementation 'org.springframework.boot:spring-boot-starter-web:3.2.0'\n" +
                "    testImplementation \"org.junit.jupiter:junit-jupiter:5.9.3\"\n" +
                "}";
        
        List<Dependency> deps = parser.parse(content);
        
        assertEquals(2, deps.size());
        assertEquals("org.springframework.boot", deps.get(0).getGroupId());
        assertEquals("spring-boot-starter-web", deps.get(0).getArtifactId());
        assertEquals("3.2.0", deps.get(0).getCurrentVersion());
        
        assertEquals("org.junit.jupiter", deps.get(1).getGroupId());
        assertEquals("junit-jupiter", deps.get(1).getArtifactId());
        assertEquals("5.9.3", deps.get(1).getCurrentVersion());
    }
}
