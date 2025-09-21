# Contributing to SQL Builder

Thank you for your interest in contributing to SQL Builder! This document provides guidelines and instructions for contributing to this open-source project.

## Table of Contents

- [About SQL Builder](#about-sql-builder)
- [Getting Started](#getting-started)
- [Development Environment Setup](#development-environment-setup)
- [Project Structure](#project-structure)
- [Coding Standards](#coding-standards)
- [Testing Guidelines](#testing-guidelines)
- [Pull Request Process](#pull-request-process)
- [Issue Guidelines](#issue-guidelines)
- [Release Process](#release-process)
- [Community Guidelines](#community-guidelines)

## About SQL Builder

SQL Builder is a framework-independent, lightweight alternative to Spring JDBC Client and MyBatis. It provides:

- **Simpler Fluent & Smart API:** Focused on readability, type safety, and minimal configuration
- **Framework Independent:** Works with any Java framework (Spring Boot, Quarkus, etc.)
- **Lightweight:** Zero third-party dependencies
- **Core Features:** Queries, Batch operations, Stored Procedures, and Transactions

## Getting Started

### Prerequisites

- **JDK 17+** (Required)
- **Maven 3.6+** (Build tool)
- **Docker & Docker Compose** (For database testing)
- **Git** (Version control)

### Quick Start

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/sql-builder.git
   cd sql-builder
   ```
3. **Add upstream remote**:
   ```bash
   git remote add upstream https://github.com/tamilnadujug/sql-builder.git
   ```

## Development Environment Setup

### 1. Database Setup

The project uses PostgreSQL for testing. Start the database using Docker:

```bash
# Start PostgreSQL database
docker compose up -d

# Verify database is running
docker ps
```

The database will be available at `localhost:5432` with:
- Database: `sampledb`
- Username: `sampledb`
- Password: `sampledb`

### 2. Build and Test

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Run with coverage
mvn jacoco:prepare-agent test jacoco:report

# Full build with quality checks
mvn clean install
```

### 3. IDE Setup

#### IntelliJ IDEA
1. Import as Maven project
2. Set JDK 17 as project SDK
3. Enable annotation processing
4. Install Checkstyle plugin (optional)

#### Eclipse
1. Import as existing Maven project
2. Set JDK 17 as JRE
3. Install Checkstyle plugin (optional)

## Project Structure

```
sql-builder/
├── src/main/java/
│   ├── module-info.java              # Java module definition
│   └── org/tamilnadujug/
│       ├── SqlBuilder.java           # Main SQL Builder class
│       ├── Transaction.java          # Transaction management
│       └── sql/                      # Core interfaces
│           ├── Sql.java              # Main SQL interface
│           ├── RowMapper.java        # Result set mapping
│           ├── ParamMapper.java      # Parameter mapping
│           └── StatementMapper.java  # Statement mapping
├── src/test/java/
│   └── org/tamilnadujug/
│       ├── AllinAllTest.java         # Comprehensive integration tests
│       ├── SqlBuilderTest.java       # Unit tests
│       ├── TransactionTest.java      # Transaction tests
│       ├── StoredProcedureTest.java  # Stored procedure tests
│       ├── GeneratedKeyTest.java     # Generated key tests
│       └── CodeReviewTests.java      # Architecture tests
├── examples/                         # Framework integration examples
│   ├── sqlbuilder-springboot/        # Spring Boot example
│   └── sqlbuilder-quarkus/           # Quarkus example
├── docker/                           # Database setup
│   └── init.sql                      # Test database schema
├── .github/workflows/                # CI/CD pipelines
├── pom.xml                          # Maven configuration
└── checkstyle-suppressions.xml      # Code style suppressions
```

## Coding Standards

### 1. Code Style

The project uses **Checkstyle** with strict rules:

```bash
# Check code style
mvn checkstyle:check

# Generate style report
mvn checkstyle:checkstyle
```

**Key Style Rules:**
- Use 4 spaces for indentation (no tabs)
- Maximum line length: 120 characters
- Use `final` for method parameters and local variables when possible
- Follow Java naming conventions
- Include comprehensive JavaDoc for public APIs

### 2. Architecture Rules

The project enforces strict architecture rules via **ArchUnit**:

- **Zero External Dependencies:** Only use JDK classes and interfaces
- **No Framework Dependencies:** Keep the core framework-agnostic
- **Clean Separation:** Clear separation between core and examples

### 3. Code Quality

- **99% Line Coverage:** Maintain high test coverage
- **SonarQube Integration:** Code quality analysis
- **ArchUnit Tests:** Architecture compliance validation

## Testing Guidelines

### 1. Test Structure

```java
@ExtendWith(BaseTest.class)
class YourTestClass {
    
    @BeforeEach
    void setup() throws SQLException {
        // Test setup
    }
    
    @Test
    void testFeature() throws SQLException {
        // Test implementation
    }
}
```

### 2. Test Categories

- **Unit Tests:** Test individual methods and classes
- **Integration Tests:** Test with real database
- **Architecture Tests:** Validate architectural constraints
- **Example Tests:** Verify framework integrations

### 3. Database Testing

```java
// Use BaseTest for database setup
class YourTest extends BaseTest {
    
    @Test
    void testDatabaseOperation() throws SQLException {
        // Your test code
        // Database is automatically set up and torn down
    }
}
```

### 4. Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=SqlBuilderTest

# Run with coverage
mvn jacoco:prepare-agent test jacoco:report

# Run architecture tests
mvn test -Dtest=CodeReviewTests
```

## Pull Request Process

### 1. Before Submitting

- [ ] Fork the repository
- [ ] Create a feature branch: `git checkout -b feature/your-feature-name`
- [ ] Make your changes
- [ ] Add tests for new functionality
- [ ] Ensure all tests pass: `mvn clean test`
- [ ] Check code style: `mvn checkstyle:check`
- [ ] Update documentation if needed
- [ ] Commit with descriptive messages

### 2. Commit Message Format

```
type(scope): brief description

Detailed description of changes

Fixes #issue-number
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes
- `refactor`: Code refactoring
- `test`: Test additions/changes
- `chore`: Build/tooling changes

### 3. Pull Request Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] All tests pass locally

## Checklist
- [ ] Code follows project style guidelines
- [ ] Self-review completed
- [ ] Documentation updated
- [ ] No breaking changes (or documented)
```

### 4. Review Process

1. **Automated Checks:** CI/CD pipeline runs automatically
2. **Code Review:** At least one maintainer review required
3. **Testing:** All tests must pass
4. **Documentation:** Update docs for new features
5. **Approval:** Maintainer approval required for merge

## Issue Guidelines

### 1. Bug Reports

Use the bug report template and include:

- **Description:** Clear description of the bug
- **Steps to Reproduce:** Detailed reproduction steps
- **Expected Behavior:** What should happen
- **Actual Behavior:** What actually happens
- **Environment:** JDK version, OS, etc.
- **Code Sample:** Minimal reproducible example

### 2. Feature Requests

Use the feature request template and include:

- **Description:** Clear description of the feature
- **Use Case:** Why this feature is needed
- **Proposed Solution:** How you envision it working
- **Alternatives:** Other solutions considered
- **Additional Context:** Any other relevant information

### 3. Good First Issues

Look for issues labeled `good first issue` or `help wanted` for beginner-friendly contributions.

## Release Process

### 1. Version Management

- **Semantic Versioning:** MAJOR.MINOR.PATCH
- **Main Branch:** Stable releases
- **Develop Branch:** Development and pre-releases

### 2. Release Steps

1. Update version in `pom.xml`
2. Update `CHANGELOG.md`
3. Create release branch
4. Run full test suite
5. Create GitHub release
6. Publish to Maven Central

## Community Guidelines

### 1. Code of Conduct

- Be respectful and inclusive
- Welcome newcomers and help them learn
- Focus on constructive feedback
- Respect different opinions and approaches

### 2. Communication

- **GitHub Issues:** Bug reports and feature requests
- **GitHub Discussions:** General questions and ideas
- **Pull Requests:** Code contributions and reviews

### 3. Getting Help

- Check existing issues and discussions first
- Provide clear, detailed information
- Include code samples when possible
- Be patient and respectful

## Development Tips

### 1. Local Development

```bash
# Start database
docker compose up -d

# Run tests in watch mode
mvn test -Dtest=YourTestClass

# Check code coverage
mvn jacoco:report
open target/site/jacoco/index.html
```

### 2. Debugging

```bash
# Run with debug output
mvn test -Dtest=YourTestClass -X

# Run specific test method
mvn test -Dtest=YourTestClass#testMethod
```

### 3. Performance Testing

```bash
# Run performance tests
mvn test -Dtest=PerformanceTest

# Profile with JProfiler or similar tools
```

## License

By contributing to SQL Builder, you agree that your contributions will be licensed under the [Apache License 2.0](LICENSE.txt).

## Recognition

Contributors will be recognized in:
- GitHub contributors list
- Release notes
- Project documentation

Thank you for contributing to SQL Builder! 🚀
