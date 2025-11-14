# Java Showcase - Comprehensive Java 21 Feature Demonstration

A comprehensive showcase of modern Java 21 features, syntax, and best practices. This project demonstrates everything from basic syntax to advanced features like streams, concurrency, and collections.

## Features

- **Java 21** - Latest LTS version with modern language features
- **Spring Boot 3.2.1** - Modern framework foundation
- **Comprehensive Examples** - 8 major showcase modules covering all Java fundamentals
- **Real-world Patterns** - Practical examples of Java features
- **Educational** - Detailed demonstrations with explanations

## Prerequisites

- Java 21 or higher
- Maven 3.6+

## Quick Start

```bash
# Build the project
mvn clean package

# Run the showcase (displays all demonstrations)
mvn spring-boot:run

# Or run the JAR directly
java -jar target/java-showcase-1.0.0.jar
```

## Showcase Modules

### 1. BasicSyntaxShowcase
Demonstrates fundamental Java syntax:
- **Printing**: `System.out.println`, `printf`, formatted strings, text blocks
- **Loops**: for, while, do-while, nested loops, break/continue
- **Range-based loops**: for-each, enhanced iterations, forEach with lambdas
- **Array iteration**: 1D and 2D arrays, various iteration methods

**Location**: `src/main/java/com/example/demo/showcase/BasicSyntaxShowcase.java`

### 2. SwitchCaseShowcase
Comprehensive switch statement demonstrations:
- **Traditional switch**: Classic switch with break statements
- **Switch with Strings**: String matching in switch (Java 7+)
- **New switch expressions**: Arrow syntax, yield keyword (Java 14+)
- **Pattern matching**: Type patterns, guard clauses (Java 21+)
- **instanceof patterns**: Pattern matching with switch and instanceof

**Location**: `src/main/java/com/example/demo/showcase/SwitchCaseShowcase.java`

### 3. OOPShowcase
Object-Oriented Programming concepts:
- **Classes and Objects**: Constructors, methods, getters/setters
- **Inheritance**: extends, super, method overriding
- **Abstract Classes**: Abstract methods and concrete implementations
- **Interfaces**: Multiple interface implementation, default methods
- **instanceof Operator**: Type checking and pattern matching
- **Polymorphism**: Runtime polymorphism demonstrations

**Location**: `src/main/java/com/example/demo/showcase/OOPShowcase.java`

### 4. LambdaShowcase
Lambda expressions and functional programming:
- **Basic Lambdas**: Single and multiple parameters, method bodies
- **Functional Interfaces**: Custom @FunctionalInterface implementations
- **Built-in Interfaces**: Predicate, Function, Consumer, Supplier, BiFunction, etc.
- **Method References**: Static, instance, arbitrary object, constructor references
- **Lambda with Collections**: forEach, removeIf, replaceAll, sort
- **Custom Functional Interfaces**: TriFunction, Validator with composition

**Location**: `src/main/java/com/example/demo/showcase/LambdaShowcase.java`

### 5. StreamShowcase
Comprehensive Java Stream API demonstrations:
- **Creating Streams**: From collections, arrays, Stream.of(), infinite streams, ranges
- **Intermediate Operations**: filter, map, flatMap, distinct, sorted, limit, skip
- **Terminal Operations**: forEach, count, anyMatch, allMatch, findFirst, min, max
- **Map Operations**: map, mapToInt, mapToDouble, flatMap
- **Filter Operations**: Multiple filters, takeWhile, dropWhile
- **Reduce Operations**: Sum, product, min, max, custom reductions
- **Peek Operation**: Debugging and side effects
- **Parallel Streams**: parallelStream, performance comparisons
- **Collectors**: toList, toSet, joining, groupingBy, partitioningBy, summarizing

**Location**: `src/main/java/com/example/demo/showcase/StreamShowcase.java`

### 6. CollectionsShowcase
Detailed collection implementations and usage:

**List Implementations**:
- **ArrayList**: Fast random access, dynamic arrays
- **LinkedList**: Fast insertion/deletion, implements Deque

**Set Implementations**:
- **HashSet**: Unordered, O(1) operations, no duplicates
- **TreeSet**: Sorted (natural/comparator), O(log n), navigation methods
- **LinkedHashSet**: Insertion order maintained

**Map Implementations**:
- **HashMap**: Unordered, O(1) operations, allows null key
- **TreeMap**: Sorted by keys, O(log n), navigation methods
- **LinkedHashMap**: Insertion/access order, LRU cache example
- **ConcurrentHashMap**: Thread-safe, high concurrency, atomic operations

**Queue/Deque**:
- **PriorityQueue**: Min/max heap, priority ordering
- **ArrayDeque**: Double-ended queue, stack/queue operations

**Traversal Methods**: EntrySet, keySet, values, Iterator, forEach, streams

**Utility Methods**: Collections.sort, reverse, shuffle, binarySearch, etc.

**Location**: `src/main/java/com/example/demo/showcase/CollectionsShowcase.java`

### 7. TypesAndConversionsShowcase
Primitive types, wrappers, and conversions:
- **Primitives**: byte, short, int, long, float, double, char, boolean
- **Wrapper Classes**: Byte, Short, Integer, Long, Float, Double, Character, Boolean
- **Autoboxing/Unboxing**: Automatic conversions between primitives and wrappers
- **String Conversions**: valueOf, toString, parsing
- **Parsing**: parseInt, parseDouble, parseBoolean with radix support
- **Type Conversions**: Widening, narrowing, casting
- **Pair and Tuple**: Custom record-based implementations
- **Optional**: Handling null safely with Optional API

**Location**: `src/main/java/com/example/demo/showcase/TypesAndConversionsShowcase.java`

### 8. ConcurrencyShowcase
Multi-threading and concurrency features:
- **Basic Threading**: Thread class, Runnable interface, lambda threads
- **Volatile Keyword**: Memory visibility across threads
- **ThreadLocal**: Thread-specific values
- **Synchronized**: Thread-safe operations, synchronized methods and blocks
- **ExecutorService**: Thread pools, Callable, Future
- **Atomic Variables**: Lock-free thread-safe operations (AtomicInteger, etc.)

**Location**: `src/main/java/com/example/demo/showcase/ConcurrencyShowcase.java`

## REST API Endpoints

The application also includes a REST API with the following endpoints:

- `GET /api/hello` - Greeting endpoint
- `POST /api/process` - Pattern matching demonstration
- `POST /api/users` - User creation
- `POST /api/transform` - Data transformation
- `GET /api/calculate` - Mathematical operations
- `POST /api/manipulate` - String manipulation
- `GET /api/health` - Health check

See the curl examples below for usage.

## Project Structure

```
java-showcase/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/demo/
│   │   │       ├── JavaShowcaseApplication.java (Main + CommandLineRunner)
│   │   │       ├── showcase/
│   │   │       │   ├── BasicSyntaxShowcase.java
│   │   │       │   ├── SwitchCaseShowcase.java
│   │   │       │   ├── OOPShowcase.java
│   │   │       │   ├── LambdaShowcase.java
│   │   │       │   ├── StreamShowcase.java
│   │   │       │   ├── CollectionsShowcase.java
│   │   │       │   ├── TypesAndConversionsShowcase.java
│   │   │       │   └── ConcurrencyShowcase.java
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

## Java 21 Features Demonstrated

1. **Pattern Matching for Switch** - Type patterns with guard clauses
2. **Records** - Compact immutable data carriers
3. **Text Blocks** - Multi-line string literals
4. **Enhanced Pattern Matching** - instanceof with pattern variables
5. **Stream API** - Powerful functional-style operations
6. **Lambda Expressions** - Concise function literals
7. **Method References** - Compact lambda alternatives
8. **Optional API** - Null-safe value handling
9. **var Keyword** - Local variable type inference
10. **Sealed Classes** - Controlled inheritance (can be added)

## Running Specific Showcases

To run individual showcases, you can modify `JavaShowcaseApplication.java` or create your own main method:

```java
public static void main(String[] args) {
    // Run specific showcase
    BasicSyntaxShowcase.demonstrate();
    // or
    StreamShowcase.demonstrate();
}
```

## Testing with curl (REST API)

```bash
# Hello endpoint
curl http://localhost:8080/api/hello?name=Developer

# Process data with pattern matching
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

## Building and Running

### Compile Only
```bash
mvn clean compile
```

### Run Tests
```bash
mvn test
```

### Package as JAR
```bash
mvn clean package
```

### Run with Maven
```bash
mvn spring-boot:run
```

### Run JAR
```bash
java -jar target/java-showcase-1.0.0.jar
```

## Learning Path

Recommended order for studying the showcases:

1. **BasicSyntaxShowcase** - Start with fundamentals
2. **SwitchCaseShowcase** - Control flow and pattern matching
3. **OOPShowcase** - Object-oriented concepts
4. **TypesAndConversionsShowcase** - Type system understanding
5. **CollectionsShowcase** - Data structures
6. **LambdaShowcase** - Functional programming basics
7. **StreamShowcase** - Functional operations on collections
8. **ConcurrencyShowcase** - Multi-threading (advanced)

## Additional Resources

- [Official Java Documentation](https://docs.oracle.com/en/java/javase/21/)
- [Java 21 Release Notes](https://www.oracle.com/java/technologies/javase/21-relnote-issues.html)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)

## License

See LICENSE file for details.
