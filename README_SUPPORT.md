# Support Guide

This document outlines how to build, test, and obtain support for **UNISoN**.

## Build Prerequisites
- **Java Development Kit 17** – set `JAVA_HOME` accordingly and ensure `java` is on your `PATH`.
- **Apache Maven 3.8+** – used for building and running tests.
- **Git** – required for fetching the source and contributing changes.

Verify your setup with:
```bash
java -version
mvn -v
```

## Build & Configuration
The project uses Maven for builds. Typical commands:

```bash
mvn clean package    # compile and package the application
```

Integration tests are skipped by default via the `skipITs` property. When needed, override this property.

### Skipping Tests
Use `-DskipTests` to skip **all** tests during a build:
```bash
mvn clean package -DskipTests
```

## Running Tests
### Unit Tests
Run the unit test suite:
```bash
mvn test
```

### Integration Tests
Integration tests reside under `src/it` and require network access. Enable and run them with:
```bash
mvn verify -DskipITs=false
```

## Common Failures
- **Missing dependencies or plugins**: ensure internet access to Maven Central or configure a proxy in `~/.m2/settings.xml`.
- **Wrong Java version**: builds require JDK 17. Check `JAVA_HOME` and `java -version`.
- **Integration test timeouts**: verify required external services (e.g., NNTP server) are reachable or run with `-DskipITs=true`.

## Getting Help
- Report issues on the project's GitHub issue tracker.
- Visit <http://unison.leonarduk.com> for additional information.
- For discussions and community support, use the SourceForge forums.
