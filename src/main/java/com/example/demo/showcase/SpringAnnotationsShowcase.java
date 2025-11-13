package com.example.demo.showcase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Demonstrates Spring Framework annotations
 * NOTE: This is educational code showing annotation usage patterns
 * Some beans may not be actually instantiated since this is a showcase
 */
public class SpringAnnotationsShowcase {

    public static void demonstrate() {
        System.out.println("\n========== SPRING ANNOTATIONS SHOWCASE ==========\n");

        stereotypeAnnotations();
        dependencyInjection();
        webAnnotations();
        configurationAnnotations();
        transactionalAnnotation();
        orderAndLazyAnnotations();
    }

    // ========== Stereotype Annotations ==========

    private static void stereotypeAnnotations() {
        System.out.println("--- Stereotype Annotations ---");
        System.out.println("Mark classes for component scanning and define their roles");

        System.out.println("\n@Component - Generic spring-managed component");
        System.out.println("  USE: General purpose beans");
        System.out.println("  EXAMPLE:");
        System.out.println("    @Component");
        System.out.println("    public class EmailService { }");

        System.out.println("\n@Service - Business logic layer");
        System.out.println("  USE: Service layer classes");
        System.out.println("  EXAMPLE:");
        System.out.println("    @Service");
        System.out.println("    public class UserService {");
        System.out.println("      public User findById(Long id) { ... }");
        System.out.println("    }");

        System.out.println("\n@Repository - Data access layer");
        System.out.println("  USE: DAO/Repository classes");
        System.out.println("  ADVANTAGE: Automatic exception translation");
        System.out.println("  EXAMPLE:");
        System.out.println("    @Repository");
        System.out.println("    public class UserRepository {");
        System.out.println("      public User save(User user) { ... }");
        System.out.println("    }");

        System.out.println("\n@Controller - Web layer (MVC)");
        System.out.println("  USE: Spring MVC controllers");
        System.out.println("  Returns view names");
        System.out.println("  EXAMPLE:");
        System.out.println("    @Controller");
        System.out.println("    public class HomeController {");
        System.out.println("      @GetMapping(\"/\")");
        System.out.println("      public String home() { return \"index\"; }");
        System.out.println("    }");

        System.out.println("\n@RestController - REST API controller");
        System.out.println("  USE: RESTful web services");
        System.out.println("  = @Controller + @ResponseBody");
        System.out.println("  Returns data directly (JSON/XML)");
        System.out.println("  EXAMPLE:");
        System.out.println("    @RestController");
        System.out.println("    public class UserApiController {");
        System.out.println("      @GetMapping(\"/api/users\")");
        System.out.println("      public List<User> getUsers() { ... }");
        System.out.println("    }");

        System.out.println();
    }

    // ========== Dependency Injection ==========

    private static void dependencyInjection() {
        System.out.println("--- Dependency Injection Annotations ---");

        System.out.println("\n@Autowired - Inject dependencies");
        System.out.println("  Constructor injection (RECOMMENDED):");
        System.out.println("    @Service");
        System.out.println("    public class UserService {");
        System.out.println("      private final UserRepository repository;");
        System.out.println("      @Autowired");
        System.out.println("      public UserService(UserRepository repository) {");
        System.out.println("        this.repository = repository;");
        System.out.println("      }");
        System.out.println("    }");

        System.out.println("\n  Field injection (NOT RECOMMENDED):");
        System.out.println("    @Service");
        System.out.println("    public class UserService {");
        System.out.println("      @Autowired");
        System.out.println("      private UserRepository repository;");
        System.out.println("    }");

        System.out.println("\n  Setter injection:");
        System.out.println("    @Service");
        System.out.println("    public class UserService {");
        System.out.println("      private UserRepository repository;");
        System.out.println("      @Autowired");
        System.out.println("      public void setRepository(UserRepository repository) {");
        System.out.println("        this.repository = repository;");
        System.out.println("      }");
        System.out.println("    }");

        System.out.println("\nLombok @RequiredArgsConstructor (with @Autowired):");
        System.out.println("  @Service");
        System.out.println("  @RequiredArgsConstructor");
        System.out.println("  public class UserService {");
        System.out.println("    private final UserRepository repository;");
        System.out.println("    // Constructor auto-generated and autowired");
        System.out.println("  }");

        System.out.println("\nBest Practice:");
        System.out.println("  ✓ Use constructor injection (immutability, testability)");
        System.out.println("  ✓ Use @RequiredArgsConstructor with Lombok");
        System.out.println("  ✗ Avoid field injection (hard to test, circular dependencies)");

        System.out.println();
    }

    // ========== Web Annotations ==========

    private static void webAnnotations() {
        System.out.println("--- Web/REST Annotations ---");

        System.out.println("\n@RequestMapping - Map HTTP requests");
        System.out.println("  Class level:");
        System.out.println("    @RestController");
        System.out.println("    @RequestMapping(\"/api/users\")");
        System.out.println("    public class UserController { }");

        System.out.println("\n  Method level:");
        System.out.println("    @RequestMapping(value = \"/{id}\", method = RequestMethod.GET)");
        System.out.println("    public User getUser(@PathVariable Long id) { ... }");

        System.out.println("\n@GetMapping - GET requests");
        System.out.println("    @GetMapping(\"/users\")");
        System.out.println("    public List<User> getUsers() { ... }");

        System.out.println("\n@PostMapping - POST requests");
        System.out.println("    @PostMapping(\"/users\")");
        System.out.println("    public User createUser(@RequestBody User user) { ... }");

        System.out.println("\n@PutMapping - PUT requests");
        System.out.println("    @PutMapping(\"/users/{id}\")");
        System.out.println("    public User updateUser(@PathVariable Long id, @RequestBody User user) { ... }");

        System.out.println("\n@DeleteMapping - DELETE requests");
        System.out.println("    @DeleteMapping(\"/users/{id}\")");
        System.out.println("    public void deleteUser(@PathVariable Long id) { ... }");

        System.out.println("\n@PathVariable - Extract path variables");
        System.out.println("    @GetMapping(\"/users/{id}\")");
        System.out.println("    public User getUser(@PathVariable Long id) { ... }");

        System.out.println("\n@RequestParam - Extract query parameters");
        System.out.println("    @GetMapping(\"/users\")");
        System.out.println("    public List<User> getUsers(");
        System.out.println("      @RequestParam(defaultValue = \"0\") int page) { ... }");

        System.out.println("\n@RequestBody - Bind request body to object");
        System.out.println("    @PostMapping(\"/users\")");
        System.out.println("    public User createUser(@RequestBody User user) { ... }");

        System.out.println("\n@ResponseBody - Return data directly (auto with @RestController)");
        System.out.println("    @Controller");
        System.out.println("    public class UserController {");
        System.out.println("      @GetMapping(\"/api/users\")");
        System.out.println("      @ResponseBody");
        System.out.println("      public List<User> getUsers() { ... }");
        System.out.println("    }");

        System.out.println();
    }

    // ========== Configuration Annotations ==========

    private static void configurationAnnotations() {
        System.out.println("--- Configuration Annotations ---");

        System.out.println("\n@Configuration - Java-based configuration");
        System.out.println("  Defines beans using @Bean methods");
        System.out.println("  EXAMPLE:");
        System.out.println("    @Configuration");
        System.out.println("    public class AppConfig {");
        System.out.println("      @Bean");
        System.out.println("      public DataSource dataSource() {");
        System.out.println("        return new HikariDataSource();");
        System.out.println("      }");
        System.out.println("    }");

        System.out.println("\n@Bean - Define a Spring bean");
        System.out.println("  Method name = bean name (or use @Bean(name=\"...\"))");
        System.out.println("  EXAMPLE:");
        System.out.println("    @Bean");
        System.out.println("    public ObjectMapper objectMapper() {");
        System.out.println("      ObjectMapper mapper = new ObjectMapper();");
        System.out.println("      mapper.configure(...);");
        System.out.println("      return mapper;");
        System.out.println("    }");

        System.out.println("\n@ComponentScan - Scan for components");
        System.out.println("  EXAMPLE:");
        System.out.println("    @Configuration");
        System.out.println("    @ComponentScan(basePackages = \"com.example.app\")");
        System.out.println("    public class AppConfig { }");

        System.out.println("\n@EnableAutoConfiguration - Spring Boot auto-configuration");
        System.out.println("  Automatically configures beans based on classpath");
        System.out.println("  Part of @SpringBootApplication");

        System.out.println("\n@SpringBootApplication - Main application annotation");
        System.out.println("  = @Configuration + @EnableAutoConfiguration + @ComponentScan");
        System.out.println("  EXAMPLE:");
        System.out.println("    @SpringBootApplication");
        System.out.println("    public class Application {");
        System.out.println("      public static void main(String[] args) {");
        System.out.println("        SpringApplication.run(Application.class, args);");
        System.out.println("      }");
        System.out.println("    }");

        System.out.println("\n@PropertySource - Load properties files");
        System.out.println("    @Configuration");
        System.out.println("    @PropertySource(\"classpath:application.properties\")");
        System.out.println("    public class AppConfig { }");

        System.out.println("\n@Value - Inject property values");
        System.out.println("    @Value(\"${app.name}\")");
        System.out.println("    private String appName;");

        System.out.println("\n@ConfigurationProperties - Bind properties to object");
        System.out.println("    @ConfigurationProperties(prefix = \"app\")");
        System.out.println("    public class AppProperties {");
        System.out.println("      private String name;");
        System.out.println("      private int timeout;");
        System.out.println("      // getters/setters");
        System.out.println("    }");

        System.out.println();
    }

    // ========== Transactional Annotation ==========

    private static void transactionalAnnotation() {
        System.out.println("--- @Transactional Annotation ---");
        System.out.println("Manages database transactions automatically");

        System.out.println("\nClass level:");
        System.out.println("  @Service");
        System.out.println("  @Transactional");
        System.out.println("  public class UserService {");
        System.out.println("    // All methods are transactional");
        System.out.println("  }");

        System.out.println("\nMethod level:");
        System.out.println("  @Service");
        System.out.println("  public class UserService {");
        System.out.println("    @Transactional");
        System.out.println("    public void createUser(User user) {");
        System.out.println("      userRepository.save(user);");
        System.out.println("      // Automatically commits or rolls back");
        System.out.println("    }");
        System.out.println("  }");

        System.out.println("\nWith parameters:");
        System.out.println("  @Transactional(");
        System.out.println("    propagation = Propagation.REQUIRED,");
        System.out.println("    isolation = Isolation.READ_COMMITTED,");
        System.out.println("    timeout = 30,");
        System.out.println("    readOnly = false,");
        System.out.println("    rollbackFor = Exception.class");
        System.out.println("  )");
        System.out.println("  public void complexOperation() { ... }");

        System.out.println("\nPropagation types:");
        System.out.println("  - REQUIRED (default): Use existing or create new");
        System.out.println("  - REQUIRES_NEW: Always create new transaction");
        System.out.println("  - SUPPORTS: Use transaction if exists, non-transactional otherwise");
        System.out.println("  - NOT_SUPPORTED: Execute non-transactionally");
        System.out.println("  - NEVER: Error if transaction exists");
        System.out.println("  - MANDATORY: Error if no transaction");

        System.out.println("\nRead-only optimization:");
        System.out.println("  @Transactional(readOnly = true)");
        System.out.println("  public User findById(Long id) {");
        System.out.println("    return userRepository.findById(id);");
        System.out.println("  }");

        System.out.println("\nBest Practices:");
        System.out.println("  ✓ Use on service layer (not controllers or repositories)");
        System.out.println("  ✓ Keep transactions short");
        System.out.println("  ✓ Use readOnly=true for queries");
        System.out.println("  ✓ Be aware of propagation behavior");
        System.out.println("  ✗ Don't call transactional methods from same class (proxy limitation)");

        System.out.println();
    }

    // ========== Order and Lazy Annotations ==========

    private static void orderAndLazyAnnotations() {
        System.out.println("--- @Order and @Lazy Annotations ---");

        System.out.println("\n@Order - Control bean initialization order");
        System.out.println("  Lower values have higher priority");
        System.out.println("  EXAMPLE:");
        System.out.println("    @Component");
        System.out.println("    @Order(1)");
        System.out.println("    public class FirstInitializer implements ApplicationRunner {");
        System.out.println("      public void run(ApplicationArguments args) {");
        System.out.println("        // Runs first");
        System.out.println("      }");
        System.out.println("    }");
        System.out.println();
        System.out.println("    @Component");
        System.out.println("    @Order(2)");
        System.out.println("    public class SecondInitializer implements ApplicationRunner {");
        System.out.println("      public void run(ApplicationArguments args) {");
        System.out.println("        // Runs second");
        System.out.println("      }");
        System.out.println("    }");

        System.out.println("\nUse with @Bean:");
        System.out.println("    @Configuration");
        System.out.println("    public class Config {");
        System.out.println("      @Bean");
        System.out.println("      @Order(1)");
        System.out.println("      public Filter firstFilter() { ... }");
        System.out.println();
        System.out.println("      @Bean");
        System.out.println("      @Order(2)");
        System.out.println("      public Filter secondFilter() { ... }");
        System.out.println("    }");

        System.out.println("\n@Lazy - Delay bean initialization");
        System.out.println("  ADVANTAGE: Faster application startup");
        System.out.println("  DISADVANTAGE: Errors appear later at runtime");
        System.out.println("  USE: Heavy beans not needed at startup");

        System.out.println("\n  On component:");
        System.out.println("    @Component");
        System.out.println("    @Lazy");
        System.out.println("    public class HeavyService {");
        System.out.println("      // Initialized only when first used");
        System.out.println("    }");

        System.out.println("\n  On @Bean:");
        System.out.println("    @Bean");
        System.out.println("    @Lazy");
        System.out.println("    public DataSource dataSource() {");
        System.out.println("      // Created only when injected/requested");
        System.out.println("    }");

        System.out.println("\n  On injection point:");
        System.out.println("    @Service");
        System.out.println("    public class UserService {");
        System.out.println("      private final ReportService reportService;");
        System.out.println("      ");
        System.out.println("      public UserService(@Lazy ReportService reportService) {");
        System.out.println("        this.reportService = reportService;");
        System.out.println("        // ReportService created only when used");
        System.out.println("      }");
        System.out.println("    }");

        System.out.println("\n  Global lazy initialization:");
        System.out.println("    # application.properties");
        System.out.println("    spring.main.lazy-initialization=true");

        System.out.println("\nBest Practices:");
        System.out.println("  @Order:");
        System.out.println("    ✓ Use for filters, interceptors, initializers");
        System.out.println("    ✓ Document why order matters");
        System.out.println("    ✗ Don't rely on it for business logic dependencies");
        System.out.println("  @Lazy:");
        System.out.println("    ✓ Use for optional/rarely-used beans");
        System.out.println("    ✓ Use for circular dependency resolution");
        System.out.println("    ✗ Don't use to hide slow startup issues");

        System.out.println();
    }

    // ========== Example Classes (for reference) ==========

    @Component
    static class ExampleComponent {
        // Generic component
    }

    @Service
    static class ExampleService {
        private final ExampleRepository repository;

        @Autowired
        public ExampleService(ExampleRepository repository) {
            this.repository = repository;
        }
    }

    @Repository
    static class ExampleRepository {
        // Data access layer
    }

    @RestController
    @RequestMapping("/api/example")
    static class ExampleController {
        private final ExampleService service;

        public ExampleController(ExampleService service) {
            this.service = service;
        }

        @GetMapping("/{id}")
        public String getExample(@PathVariable String id) {
            return "Example: " + id;
        }
    }

    @Configuration
    static class ExampleConfig {
        @Bean
        @Order(1)
        public String firstBean() {
            return "First";
        }

        @Bean
        @Lazy
        public String lazyBean() {
            return "Lazy loaded";
        }
    }
}
