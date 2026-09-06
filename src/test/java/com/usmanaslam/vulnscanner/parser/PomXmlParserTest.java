package com.usmanaslam.vulnscanner.parser;

import com.usmanaslam.vulnscanner.model.Dependency;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PomXmlParserTest {

    private final PomXmlParser parser = new PomXmlParser();

    @Test
    void testParse() {
        String content = "<dependencies>\n" +
                "    <dependency>\n" +
                "        <groupId>org.springframework.boot</groupId>\n" +
                "        <artifactId>spring-boot-starter-web</artifactId>\n" +
                "        <version>3.2.0</version>\n" +
                "    </dependency>\n" +
                "</dependencies>";
        
        List<Dependency> deps = parser.parse(content);
        
        assertEquals(1, deps.size());
        assertEquals("org.springframework.boot", deps.get(0).getGroupId());
        assertEquals("spring-boot-starter-web", deps.get(0).getArtifactId());
        assertEquals("3.2.0", deps.get(0).getCurrentVersion());
    }
}
