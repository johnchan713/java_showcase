# Java Showcase - Comprehensive Java 21 Enterprise Demonstration

A comprehensive showcase of modern Java 21 features, enterprise frameworks, and industry-standard libraries. This project demonstrates everything from basic syntax to advanced features including reactive programming, distributed systems, financial technologies, machine learning, and more.

## Features

- **Java 21** - Latest LTS version with modern language features
- **Spring Boot 3.2.1** - Modern framework foundation
- **58 Comprehensive Showcases** - Covering all major Java technologies and frameworks
- **Enterprise-Ready** - Real-world patterns and industry best practices
- **Educational** - Detailed demonstrations with explanations and use cases

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

## Showcase Categories

### Core Java Features (18 showcases)

#### 1. BasicSyntaxShowcase
Fundamental Java syntax: printing, loops, range-based iterations, array operations

#### 2. SwitchCaseShowcase
Traditional switch, switch expressions, pattern matching, guard clauses

#### 3. OOPShowcase
Classes, inheritance, abstract classes, interfaces, polymorphism, instanceof patterns

#### 4. ObjectMethodsShowcase
toString(), equals(), hashCode(), clone(), finalize(), wait/notify

#### 5. StringProcessingShowcase
String manipulation, StringBuilder, StringBuffer, regular expressions, text blocks

#### 6. DateTimeShowcase
LocalDate, LocalTime, LocalDateTime, ZonedDateTime, formatting, parsing, calculations

#### 7. LambdaShowcase
Lambda expressions, functional interfaces, method references, composition

#### 8. StreamShowcase
Stream API, intermediate operations, terminal operations, collectors, parallel streams

#### 9. CollectionsShowcase
List, Set, Map implementations (ArrayList, HashSet, HashMap, TreeMap, etc.)

#### 10. AdvancedCollectionsShowcase
Specialized collections, concurrent collections, immutable collections

#### 11. TypesAndConversionsShowcase
Primitives, wrappers, autoboxing, string conversions, Optional, records

#### 12. ConcurrencyShowcase
Threading, synchronized, ExecutorService, atomic variables, thread pools

#### 13. ThreadPoolExecutorShowcase
Custom thread pools, task scheduling, work stealing, rejection policies

#### 14. MathShowcase
BigInteger, BigDecimal, Math operations, random numbers, statistics

#### 15. ReflectionShowcase
Class introspection, dynamic method invocation, field access, annotations

#### 16. GenericsShowcase
Generic classes, methods, bounded types, wildcards, type erasure

#### 17. CryptographyShowcase
Hashing (MD5, SHA), encryption (AES, RSA), digital signatures, key generation

#### 18. TestingShowcase
JUnit 5, Mockito, test doubles, parameterized tests, best practices

### Spring Framework (2 showcases)

#### 19. SpringAnnotationsShowcase
@Component, @Autowired, @Value, @Configuration, @Bean, dependency injection

#### 20. SpringContextShowcase
ApplicationContext, bean lifecycle, profiles, events, SpEL

### Security (1 showcase)

#### 21. SpringSecurityShowcase
Authentication, authorization, password encoding, JWT, OAuth2, CORS

### Data Processing & Serialization (4 showcases)

#### 22. JacksonShowcase
JSON parsing, serialization, ObjectMapper, JsonNode, custom serializers

#### 23. JAXBShowcase
XML binding, marshalling, unmarshalling, annotations, schema validation

#### 24. GsonShowcase
Google's JSON library, serialization, custom adapters, type tokens

#### 25. AvroShowcase
Apache Avro, schema evolution, binary serialization, code generation

### Messaging & Event Streaming (4 showcases)

#### 26. KafkaShowcase
Producer, consumer, streams, transactions, serialization, partitioning

#### 27. JMSShowcase
Java Message Service, queues, topics, message selectors, transactions

#### 28. RabbitMQShowcase
AMQP protocol, exchanges, queues, routing, acknowledgments, dead letter queues

#### 29. SolaceShowcase
Enterprise messaging, guaranteed delivery, pub/sub, request/reply

### Data Storage & Caching (4 showcases)

#### 30. JDBCShowcase
Database connectivity, prepared statements, transactions, batch operations

#### 31. RedisShowcase
Key-value operations, data structures, pub/sub, transactions, pipelining

#### 32. DynamoDBShowcase
AWS NoSQL database, CRUD operations, queries, indexes, batch operations

#### 33. HazelcastShowcase
In-memory data grid, distributed maps, queues, locks, events

### Reactive Programming & Networking (5 showcases)

#### 34. ReactorShowcase
Project Reactor, Mono, Flux, operators, backpressure, schedulers

#### 35. NettyShowcase
Asynchronous networking, TCP/HTTP servers, channels, pipelines, codecs

#### 36. DisruptorShowcase
LMAX Disruptor, ring buffer, high-performance inter-thread messaging

#### 37. WebSocketShowcase
WebSocket protocol, bidirectional communication, STOMP, real-time updates

#### 38. ApacheMINAShowcase
Network application framework, IoT protocols, custom codecs, filters

### Integration & Routing (1 showcase)

#### 39. ApacheCamelShowcase
Enterprise Integration Patterns, routing, transformations, endpoints

### Big Data (1 showcase)

#### 40. HadoopShowcase
HDFS operations, MapReduce, YARN, distributed file processing

### Resilience & Observability (3 showcases)

#### 41. Resilience4jShowcase
Circuit breaker, rate limiter, retry, bulkhead, time limiter, cache

#### 42. MicrometerShowcase
Metrics collection, counters, gauges, timers, distribution summaries

#### 43. LoggingShowcase
SLF4J, Logback, log levels, MDC, structured logging, log patterns

### Testing & Automation (2 showcases)

#### 44. AwaitilityShowcase
Asynchronous testing, polling, conditions, timeouts, Hamcrest matchers

#### 45. SeleniumShowcase
Web browser automation, WebDriver, page objects, waits, assertions

### DevOps & Container Orchestration (2 showcases)

#### 46. DockerClientShowcase
Container management, images, volumes, networks, Docker API integration

#### 47. KubernetesShowcase
Pod management, deployments, services, config maps, secrets, scaling

### Financial & Trading (3 showcases)

#### 48. QuickFIXJShowcase
FIX protocol for electronic trading, session management, order handling, execution reports

#### 49. StrataShowcase
Market risk analytics, derivative pricing (swaps, options, FX forwards), yield curves

#### 50. TA4JShowcase
Technical analysis indicators (SMA, RSI, MACD, Bollinger Bands), backtesting, trading strategies

### Payment Processing (2 showcases)

#### 51. StripeShowcase
Payment intents, customers, subscriptions, refunds, webhooks, payment methods

#### 52. JavaMoneyShowcase
JSR 354 Money and Currency API, monetary amounts, currency conversion, formatting

### Banking & Finance (1 showcase)

#### 53. IBAN4JShowcase
IBAN validation and generation, BIC codes, country code validation

### Blockchain & Cryptocurrency (1 showcase)

#### 54. BitcoinJShowcase
Bitcoin wallets, HD wallets (BIP32/39/44), transactions, SPV, blockchain interaction

### Mathematics & Linear Algebra (1 showcase)

#### 55. OjAlgoShowcase
Linear algebra, matrix operations, linear programming, optimization, eigenvalues

### Machine Learning (1 showcase)

#### 56. SMILEShowcase
Classification (Random Forest, Logistic Regression, SVM), regression, clustering (K-Means, DBSCAN), dimensionality reduction (PCA, t-SNE)

### Matrix Operations (1 showcase)

#### 57. EJMLShowcase
Efficient Java Matrix Library, matrix operations, decompositions (SVD, QR, Eigenvalue), solving linear systems

### WebSocket Communication (1 showcase)

#### 58. WebSocketShowcase
Real-time bidirectional communication, STOMP messaging, pub/sub patterns

## Technology Stack

### Core Technologies
- Java 21 (LTS)
- Spring Boot 3.2.1
- Maven 3.6+

### Data Processing
- Jackson 2.15.3 (JSON)
- JAXB (XML)
- Google Gson 2.10.1
- Apache Avro 1.11.3

### Messaging & Streaming
- Apache Kafka 3.6.1
- RabbitMQ 5.20.0
- Solace 10.22.0
- Spring JMS

### Data Storage
- Redis (Lettuce 6.3.1)
- AWS DynamoDB 2.21.45
- Hazelcast 5.3.6
- JDBC/H2

### Reactive & Networking
- Project Reactor 3.6.1
- Netty 4.1.104
- LMAX Disruptor 4.0.0
- WebSocket (Spring)

### Integration
- Apache Camel 4.3.0
- Apache MINA 2.2.3
- Apache Hadoop 3.3.6

### Resilience & Monitoring
- Resilience4j 2.2.0
- Micrometer 1.12.1
- SLF4J/Logback

### Testing & DevOps
- JUnit 5
- Mockito 5.8.0
- Awaitility 4.2.0
- Selenium 4.16.1
- Docker Java 3.3.4
- Kubernetes Java Client 19.0.0

### Security
- Spring Security 6.2.1
- Bouncy Castle 1.77

### Financial & Trading
- QuickFIX/J 2.3.1
- Strata 2.12.38
- TA4J 0.15

### Payment & Banking
- Stripe Java 24.16.0
- JavaMoney (Moneta) 1.4.2
- IBAN4J 3.2.7-RELEASE

### Blockchain
- BitcoinJ 0.16.2

### Mathematics & ML
- ojAlgo 53.0.0
- SMILE 3.0.2
- EJML 0.43.1

## Project Structure

```
java-showcase/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/demo/
│   │   │       ├── JavaShowcaseApplication.java (Main + CommandLineRunner)
│   │   │       ├── showcase/
│   │   │       │   ├── [58 showcase files]
│   │   │       │   ├── BasicSyntaxShowcase.java
│   │   │       │   ├── QuickFIXJShowcase.java
│   │   │       │   ├── SMILEShowcase.java
│   │   │       │   └── ...
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
10. **Virtual Threads** - Lightweight concurrency (Project Loom)

## Use Cases by Domain

### Financial Services
- **Trading Systems**: QuickFIX/J for FIX protocol integration
- **Risk Management**: Strata for derivative pricing and market risk analytics
- **Algorithmic Trading**: TA4J for technical analysis and backtesting
- **Payment Processing**: Stripe for payment intents, subscriptions, webhooks
- **Banking**: IBAN4J for account validation, JavaMoney for currency handling

### Blockchain & Cryptocurrency
- **Wallet Management**: BitcoinJ for HD wallets and transaction signing
- **Blockchain Integration**: SPV client, transaction broadcasting

### Data Science & Machine Learning
- **Classification & Regression**: SMILE for supervised learning
- **Clustering**: K-Means, DBSCAN, hierarchical clustering
- **Dimensionality Reduction**: PCA, t-SNE, UMAP
- **Linear Algebra**: ojAlgo for matrix operations and optimization
- **Matrix Operations**: EJML for high-performance computations

### Enterprise Integration
- **Message Routing**: Apache Camel for EIP patterns
- **Event Streaming**: Kafka for distributed event processing
- **Messaging**: RabbitMQ, Solace for reliable message delivery
- **Caching**: Redis, Hazelcast for distributed caching

### Microservices & Cloud
- **Resilience**: Resilience4j for circuit breakers, retries, rate limiting
- **Observability**: Micrometer for metrics, distributed tracing
- **Container Orchestration**: Kubernetes client for pod management
- **Service Mesh**: Integration patterns for microservices

### Real-time Applications
- **Reactive Streams**: Project Reactor for backpressure handling
- **WebSocket**: Bidirectional real-time communication
- **High-Performance**: LMAX Disruptor for low-latency messaging
- **Async I/O**: Netty for non-blocking network applications

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

## Running Specific Showcases

To run individual showcases, you can modify `JavaShowcaseApplication.java` or create your own main method:

```java
public static void main(String[] args) {
    // Run specific showcase
    QuickFIXJShowcase.demonstrate();
    // or
    SMILEShowcase.demonstrate();
    // or
    ReactorShowcase.demonstrate();
}
```

## Learning Path

### Beginner
1. BasicSyntaxShowcase
2. SwitchCaseShowcase
3. OOPShowcase
4. TypesAndConversionsShowcase
5. CollectionsShowcase

### Intermediate
6. LambdaShowcase
7. StreamShowcase
8. ConcurrencyShowcase
9. StringProcessingShowcase
10. DateTimeShowcase

### Advanced - Enterprise
11. SpringAnnotationsShowcase
12. KafkaShowcase
13. RedisShowcase
14. ReactorShowcase
15. Resilience4jShowcase

### Advanced - Financial
16. QuickFIXJShowcase
17. StrataShowcase
18. TA4JShowcase
19. StripeShowcase
20. JavaMoneyShowcase

### Advanced - Machine Learning
21. SMILEShowcase
22. OjAlgoShowcase
23. EJMLShowcase

### Advanced - DevOps
24. DockerClientShowcase
25. KubernetesShowcase
26. MicrometerShowcase

## REST API Endpoints

The application includes a REST API with the following endpoints:

- `GET /api/hello` - Greeting endpoint
- `POST /api/process` - Pattern matching demonstration
- `POST /api/users` - User creation
- `POST /api/transform` - Data transformation
- `GET /api/calculate` - Mathematical operations
- `POST /api/manipulate` - String manipulation
- `GET /api/health` - Health check

### Testing with curl

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

## Additional Resources

### Official Documentation
- [Official Java Documentation](https://docs.oracle.com/en/java/javase/21/)
- [Java 21 Release Notes](https://www.oracle.com/java/technologies/javase/21-relnote-issues.html)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)

### Financial Libraries
- [QuickFIX/J Documentation](https://www.quickfixj.org/)
- [Strata Documentation](https://strata.opengamma.io/)
- [TA4J Documentation](https://ta4j.github.io/ta4j-wiki/)
- [Stripe Java Documentation](https://stripe.com/docs/api/java)

### Machine Learning & Mathematics
- [SMILE Documentation](https://haifengl.github.io/)
- [ojAlgo Documentation](https://www.ojalgo.org/)
- [EJML Documentation](http://ejml.org/)

### Enterprise Integration
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Project Reactor Documentation](https://projectreactor.io/docs)
- [Resilience4j Documentation](https://resilience4j.readme.io/)

## License

See LICENSE file for details.

## Contributing

This is a comprehensive educational project. Feel free to use it for learning Java 21 and enterprise Java development.
