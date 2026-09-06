# Vuln Scanner

A REST API that accepts a dependency file (`.properties`, `build.gradle`, or `pom.xml`), parses out dependencies and versions, checks them against Maven Central for the latest versions, and flags outdated ones.

![Java 21](https://img.shields.io/badge/Java-21-blue.svg)
![Spring Boot 3.5](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen.svg)
![Gradle](https://img.shields.io/badge/Gradle-8.14-02303A.svg)

## Features
- **Multi-format Support**: Parses `PROPERTIES`, `GRADLE`, and `POM` files.
- **Async Processing**: Queries Maven Central asynchronously to quickly scan dozens of dependencies.
- **Strategy Pattern**: Easily extensible parser design.
- **H2 In-Memory DB**: Fast, zero-setup persistence for scan jobs and results.

## Architecture Diagram

```text
+-----------+       +-------------------+       +-----------------------+
|  Client   | ----> |  ScanController   | ----> |      ScanService      |
+-----------+       +-------------------+       +-----------------------+
                                                            |
                                                            v
                                                +-----------------------+
                                                |   DependencyParser    |
                                                | (Strategy Interface)  |
                                                +-----------------------+
                                                /           |           \
                                +----------------+ +----------------+ +------------+
                                |PropertiesParser| |  GradleParser  | | PomParser  |
                                +----------------+ +----------------+ +------------+
                                                            |
                                                            v
                                                +-----------------------+
                                                |  VersionCheckService  |
                                                |     (@Async calls)    |
                                                +-----------------------+
                                                            |
                                                            v
                                                +-----------------------+
                                                |    Maven Central API  |
                                                +-----------------------+
```

## How to Build & Run
1. Navigate to the project directory.
2. Build the project:
   ```bash
   JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home ./gradlew clean build
   ```
3. Run the application:
   ```bash
   JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home ./gradlew bootRun
   ```

## API Endpoints
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/scans/upload` | Upload a file to scan |
| POST | `/api/v1/scans/text` | Submit text content to scan |
| GET | `/api/v1/scans` | Get all scans |
| GET | `/api/v1/scans/{id}` | Get a specific scan |
| GET | `/api/v1/scans/{id}/report` | Get the dependency report for a scan |

## Sample Request
```bash
curl -X POST http://localhost:8081/api/v1/scans/text \
-H "Content-Type: application/json" \
-d '{
  "fileType": "PROPERTIES",
  "content": "spring-boot.version=3.2.0"
}'
```

## Postman Collection
A complete Postman collection is available in the `postman/` directory.

## Swagger UI
Once running, access Swagger at: `http://localhost:8081/swagger-ui.html`
