# Java Showcase - Spring Boot Application

A modern Spring Boot application showcasing Java 21 features and best practices.

## Features

- Java 21 with modern language features
- Spring Boot 3.2.1
- RESTful API with multiple endpoints
- Pattern matching and switch expressions
- Records for data models
- Enhanced Stream API usage
- Maven build system

## Prerequisites

- Java 21 or higher
- Maven 3.6+ (or use the included Maven wrapper)

## Building the Application

```bash
# Using Maven
mvn clean package

# Or using Maven wrapper (if available)
./mvnw clean package
```

## Running the Application

```bash
# Using Maven
mvn spring-boot:run

# Or run the JAR directly
java -jar target/java-showcase-1.0.0.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### 1. Hello World
```bash
GET /api/hello?name=John
```
Response:
```json
{
  "message": "Hello, John!",
  "javaVersion": "21.0.8"
}
```

### 2. Process Data (Pattern Matching)
```bash
POST /api/process
Content-Type: application/json

"Hello World"
```
Response:
```json
{
  "input": "Hello World",
  "result": "Short string: Hello World",
  "type": "String"
}
```

### 3. Create User
```bash
POST /api/users
Content-Type: application/json

{
  "id": "1",
  "name": "John Doe",
  "email": "john@example.com",
  "age": 30
}
```

### 4. Transform Data
```bash
POST /api/transform
Content-Type: application/json

["hello", "world", "java", "spring"]
```
Response:
```json
["HELLO", "JAVA", "SPRING", "WORLD"]
```

### 5. Calculate
```bash
GET /api/calculate?a=10&b=5&operation=add
```
Response:
```json
{
  "operand1": 10,
  "operand2": 5,
  "operation": "add",
  "result": 15
}
```

Operations: `add`, `subtract`, `multiply`, `divide`

### 6. String Manipulation
```bash
POST /api/manipulate
Content-Type: application/json

{
  "text": "hello world",
  "operation": "uppercase"
}
```
Response:
```json
{
  "original": "hello world",
  "operation": "uppercase",
  "result": "HELLO WORLD"
}
```

Operations: `uppercase`, `lowercase`, `reverse`, `capitalize`

### 7. Health Check
```bash
GET /api/health
```

## Java 21 Features Demonstrated

1. **Pattern Matching for Switch**: Enhanced switch expressions with type patterns
2. **Records**: Immutable data carriers with compact syntax (`User.java`)
3. **Enhanced Pattern Matching**: Guard clauses and sophisticated pattern matching
4. **Stream API**: Modern collection processing
5. **Text Blocks**: Multi-line string literals (if used in code)

## Project Structure

```
java-showcase/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/demo/
│   │   │       ├── JavaShowcaseApplication.java
│   │   │       ├── controller/
│   │   │       │   └── FunctionController.java
│   │   │       ├── model/
│   │   │       │   └── User.java
│   │   │       └── service/
│   │   │           └── DataProcessingService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
├── pom.xml
└── README.md
```

## Testing with curl

```bash
# Hello endpoint
curl http://localhost:8080/api/hello?name=Developer

# Process data
curl -X POST http://localhost:8080/api/process \
  -H "Content-Type: application/json" \
  -d '"This is a very long string that will be truncated"'

# Create user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"id":"1","name":"Alice","email":"alice@example.com","age":25}'

# Transform data
curl -X POST http://localhost:8080/api/transform \
  -H "Content-Type: application/json" \
  -d '["apple","banana","cherry"]'

# Calculate
curl "http://localhost:8080/api/calculate?a=20&b=4&operation=multiply"

# Manipulate string
curl -X POST http://localhost:8080/api/manipulate \
  -H "Content-Type: application/json" \
  -d '{"text":"hello world","operation":"capitalize"}'
```

## Actuator Endpoints

- Health: `http://localhost:8080/actuator/health`
- Info: `http://localhost:8080/actuator/info`

## License

See LICENSE file for details.
