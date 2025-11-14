package com.example.demo.showcase;

/**
 * Demonstrates Spring Context customization and configuration
 * Covers ApplicationContext, bean lifecycle, profiles, properties, and conditional beans
 */
public class SpringContextShowcase {

    public static void demonstrate() {
        System.out.println("\n========== SPRING CONTEXT CONFIGURATION SHOWCASE ==========\n");

        applicationContextDemo();
        beanLifecycleDemo();
        profilesDemo();
        propertiesDemo();
        conditionalBeansDemo();
        eventHandlingDemo();
        customizationDemo();
    }

    // ========== Application Context ==========

    private static void applicationContextDemo() {
        System.out.println("--- ApplicationContext ---");
        System.out.println("Core container managing beans and dependencies\n");

        System.out.println("1. Access ApplicationContext:");
        System.out.println("""
            @Component
            public class MyComponent implements ApplicationContextAware {

                private ApplicationContext applicationContext;

                @Override
                public void setApplicationContext(ApplicationContext context)
                        throws BeansException {
                    this.applicationContext = context;
                }

                public void useContext() {
                    // Get bean by name
                    MyService service = applicationContext.getBean("myService", MyService.class);

                    // Get bean by type
                    MyService service2 = applicationContext.getBean(MyService.class);

                    // Get all beans of a type
                    Map<String, MyService> beans =
                        applicationContext.getBeansOfType(MyService.class);

                    // Check if bean exists
                    boolean exists = applicationContext.containsBean("myService");

                    // Get bean names
                    String[] beanNames = applicationContext.getBeanDefinitionNames();

                    // Get environment
                    Environment env = applicationContext.getEnvironment();
                }
            }
            """);

        System.out.println("2. Inject ApplicationContext:");
        System.out.println("""
            @Component
            public class MyComponent {

                @Autowired
                private ApplicationContext applicationContext;

                // Or constructor injection
                public MyComponent(ApplicationContext applicationContext) {
                    this.applicationContext = applicationContext;
                }
            }
            """);

        System.out.println("3. Context hierarchy:");
        System.out.println("""
            @Configuration
            public class ContextConfig {

                @Bean
                public ApplicationContext childContext(
                        ApplicationContext parentContext) {

                    AnnotationConfigApplicationContext child =
                        new AnnotationConfigApplicationContext();
                    child.setParent(parentContext);
                    child.register(ChildConfig.class);
                    child.refresh();

                    return child;
                }
            }
            """);

        System.out.println();
    }

    // ========== Bean Lifecycle ==========

    private static void beanLifecycleDemo() {
        System.out.println("--- Bean Lifecycle ---");
        System.out.println("Customize bean initialization and destruction\n");

        System.out.println("1. @PostConstruct and @PreDestroy:");
        System.out.println("""
            @Component
            public class MyService {

                @PostConstruct
                public void init() {
                    System.out.println("Bean initialized - after dependency injection");
                    // Setup resources, connections, etc.
                }

                @PreDestroy
                public void cleanup() {
                    System.out.println("Bean destroyed - before shutdown");
                    // Close resources, connections, etc.
                }
            }
            """);

        System.out.println("2. InitializingBean and DisposableBean:");
        System.out.println("""
            @Component
            public class MyService implements InitializingBean, DisposableBean {

                @Override
                public void afterPropertiesSet() throws Exception {
                    System.out.println("afterPropertiesSet called");
                    // Initialization logic
                }

                @Override
                public void destroy() throws Exception {
                    System.out.println("destroy called");
                    // Cleanup logic
                }
            }
            """);

        System.out.println("3. @Bean initMethod and destroyMethod:");
        System.out.println("""
            @Configuration
            public class AppConfig {

                @Bean(initMethod = "init", destroyMethod = "cleanup")
                public MyService myService() {
                    return new MyService();
                }
            }

            public class MyService {
                public void init() {
                    System.out.println("Custom init method");
                }

                public void cleanup() {
                    System.out.println("Custom cleanup method");
                }
            }
            """);

        System.out.println("4. BeanPostProcessor:");
        System.out.println("""
            @Component
            public class CustomBeanPostProcessor implements BeanPostProcessor {

                @Override
                public Object postProcessBeforeInitialization(Object bean, String beanName)
                        throws BeansException {
                    System.out.println("Before initialization: " + beanName);
                    // Modify bean before init methods
                    return bean;
                }

                @Override
                public Object postProcessAfterInitialization(Object bean, String beanName)
                        throws BeansException {
                    System.out.println("After initialization: " + beanName);
                    // Wrap bean in proxy, add functionality
                    return bean;
                }
            }
            """);

        System.out.println("5. Bean lifecycle order:");
        System.out.println("   1. Constructor");
        System.out.println("   2. Setter injection (@Autowired setters)");
        System.out.println("   3. BeanPostProcessor.postProcessBeforeInitialization()");
        System.out.println("   4. @PostConstruct");
        System.out.println("   5. InitializingBean.afterPropertiesSet()");
        System.out.println("   6. @Bean(initMethod)");
        System.out.println("   7. BeanPostProcessor.postProcessAfterInitialization()");
        System.out.println("   --- Bean ready for use ---");
        System.out.println("   8. @PreDestroy");
        System.out.println("   9. DisposableBean.destroy()");
        System.out.println("   10. @Bean(destroyMethod)");

        System.out.println();
    }

    // ========== Profiles ==========

    private static void profilesDemo() {
        System.out.println("--- Spring Profiles ---");
        System.out.println("Environment-specific bean configuration\n");

        System.out.println("1. Activate profiles:");
        System.out.println("""
            # application.properties
            spring.profiles.active=dev,local

            # Command line
            java -jar app.jar --spring.profiles.active=prod

            # Environment variable
            export SPRING_PROFILES_ACTIVE=prod

            # Programmatically
            SpringApplication app = new SpringApplication(MyApp.class);
            app.setAdditionalProfiles("dev");
            app.run(args);
            """);

        System.out.println("2. Profile-specific beans:");
        System.out.println("""
            @Configuration
            @Profile("dev")
            public class DevConfig {

                @Bean
                public DataSource dataSource() {
                    // H2 in-memory database for dev
                    return new H2DataSource();
                }
            }

            @Configuration
            @Profile("prod")
            public class ProdConfig {

                @Bean
                public DataSource dataSource() {
                    // PostgreSQL for production
                    return new PostgreSQLDataSource();
                }
            }

            // Multiple profiles
            @Profile({"dev", "staging"})
            public class NonProdConfig { }

            // NOT profile
            @Profile("!prod")
            public class NonProductionConfig { }

            // Complex expressions
            @Profile("(dev | staging) & !cloud")
            public class ComplexProfileConfig { }
            """);

        System.out.println("3. Profile-specific properties:");
        System.out.println("""
            # application-dev.properties
            spring.datasource.url=jdbc:h2:mem:testdb
            logging.level.root=DEBUG

            # application-prod.properties
            spring.datasource.url=jdbc:postgresql://prod-db:5432/myapp
            logging.level.root=WARN

            # Load profile-specific file
            spring.config.import=classpath:application-${spring.profiles.active}.properties
            """);

        System.out.println("4. @Profile on methods:");
        System.out.println("""
            @Configuration
            public class DataConfig {

                @Bean
                @Profile("dev")
                public DataSource devDataSource() {
                    return new H2DataSource();
                }

                @Bean
                @Profile("prod")
                public DataSource prodDataSource() {
                    return new PostgreSQLDataSource();
                }
            }
            """);

        System.out.println();
    }

    // ========== Properties ==========

    private static void propertiesDemo() {
        System.out.println("--- Properties Configuration ---");
        System.out.println("External configuration with properties and YAML\n");

        System.out.println("1. @Value annotation:");
        System.out.println("""
            @Component
            public class MyComponent {

                @Value("${app.name}")
                private String appName;

                @Value("${app.version:1.0.0}")  // Default value
                private String version;

                @Value("${app.timeout:30}")
                private int timeout;

                @Value("#{${app.properties}}")  // Map injection
                private Map<String, String> properties;

                @Value("#{'${app.list}'.split(',')}")  // List injection
                private List<String> items;

                @Value("${app.enabled:true}")
                private boolean enabled;
            }
            """);

        System.out.println("2. @ConfigurationProperties:");
        System.out.println("""
            # application.properties
            app.name=My Application
            app.version=1.0.0
            app.server.host=localhost
            app.server.port=8080
            app.features[0]=feature1
            app.features[1]=feature2

            @ConfigurationProperties(prefix = "app")
            @Component
            public class AppProperties {

                private String name;
                private String version;
                private Server server;
                private List<String> features;

                // Nested configuration
                public static class Server {
                    private String host;
                    private int port;
                    // getters/setters
                }

                // getters/setters
            }

            // Usage
            @Service
            public class MyService {

                @Autowired
                private AppProperties appProperties;

                public void doSomething() {
                    String name = appProperties.getName();
                    int port = appProperties.getServer().getPort();
                }
            }
            """);

        System.out.println("3. Property sources:");
        System.out.println("""
            @Configuration
            @PropertySource("classpath:custom.properties")
            @PropertySource("file:/etc/myapp/config.properties")  // External file
            public class PropertyConfig {

                @Bean
                public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
                    return new PropertySourcesPlaceholderConfigurer();
                }
            }

            // Multiple property sources with order
            @Configuration
            @PropertySources({
                @PropertySource("classpath:default.properties"),
                @PropertySource(value = "classpath:custom.properties",
                               ignoreResourceNotFound = true)
            })
            public class MultiPropertyConfig { }
            """);

        System.out.println("4. Environment abstraction:");
        System.out.println("""
            @Component
            public class MyComponent {

                @Autowired
                private Environment env;

                public void useEnvironment() {
                    // Get property
                    String name = env.getProperty("app.name");
                    String nameWithDefault = env.getProperty("app.name", "Default");

                    // Get required property (throws if missing)
                    String required = env.getRequiredProperty("app.name");

                    // Get property as type
                    Integer port = env.getProperty("server.port", Integer.class, 8080);

                    // Check property exists
                    boolean exists = env.containsProperty("app.name");

                    // Get active profiles
                    String[] profiles = env.getActiveProfiles();

                    // Get default profiles
                    String[] defaultProfiles = env.getDefaultProfiles();
                }
            }
            """);

        System.out.println("5. YAML configuration:");
        System.out.println("""
            # application.yml
            app:
              name: My Application
              version: 1.0.0
              server:
                host: localhost
                port: 8080
              features:
                - feature1
                - feature2
              database:
                pools:
                  - name: primary
                    size: 10
                  - name: secondary
                    size: 5
            """);

        System.out.println();
    }

    // ========== Conditional Beans ==========

    private static void conditionalBeansDemo() {
        System.out.println("--- Conditional Beans ---");
        System.out.println("Create beans based on conditions\n");

        System.out.println("1. @ConditionalOnProperty:");
        System.out.println("""
            @Configuration
            @ConditionalOnProperty(name = "feature.enabled", havingValue = "true")
            public class FeatureConfig {

                @Bean
                public FeatureService featureService() {
                    return new FeatureService();
                }
            }

            // With default
            @ConditionalOnProperty(
                name = "feature.enabled",
                havingValue = "true",
                matchIfMissing = true  // Created if property missing
            )
            """);

        System.out.println("2. @ConditionalOnClass:");
        System.out.println("""
            @Configuration
            @ConditionalOnClass(name = "org.postgresql.Driver")
            public class PostgresConfig {

                @Bean
                public DataSource dataSource() {
                    // Only created if PostgreSQL driver on classpath
                    return new PostgreSQLDataSource();
                }
            }

            @ConditionalOnClass({RedisTemplate.class, Jedis.class})
            public class RedisConfig { }
            """);

        System.out.println("3. @ConditionalOnMissingBean:");
        System.out.println("""
            @Configuration
            public class DefaultConfig {

                @Bean
                @ConditionalOnMissingBean(DataSource.class)
                public DataSource defaultDataSource() {
                    // Created only if no DataSource bean exists
                    return new H2DataSource();
                }
            }
            """);

        System.out.println("4. @ConditionalOnBean:");
        System.out.println("""
            @Configuration
            public class DependentConfig {

                @Bean
                @ConditionalOnBean(DataSource.class)
                public DataSourceInitializer initializer(DataSource dataSource) {
                    // Only created if DataSource bean exists
                    return new DataSourceInitializer(dataSource);
                }
            }
            """);

        System.out.println("5. Other conditional annotations:");
        System.out.println("""
            // On web application
            @ConditionalOnWebApplication
            public class WebConfig { }

            // On resource
            @ConditionalOnResource(resources = "classpath:config.xml")
            public class XmlConfig { }

            // On expression
            @ConditionalOnExpression("${feature.enabled:false} and '${env}' == 'prod'")
            public class ComplexConditionConfig { }

            // On Java version
            @ConditionalOnJava(JavaVersion.SEVENTEEN)
            public class Java17Config { }
            """);

        System.out.println("6. Custom condition:");
        System.out.println("""
            public class CustomCondition implements Condition {

                @Override
                public boolean matches(ConditionContext context,
                                      AnnotatedTypeMetadata metadata) {
                    Environment env = context.getEnvironment();
                    // Custom logic
                    return env.getProperty("custom.enabled", Boolean.class, false);
                }
            }

            @Configuration
            @Conditional(CustomCondition.class)
            public class CustomConditionalConfig { }
            """);

        System.out.println();
    }

    // ========== Event Handling ==========

    private static void eventHandlingDemo() {
        System.out.println("--- Spring Events ---");
        System.out.println("Application event publishing and listening\n");

        System.out.println("1. Built-in events:");
        System.out.println("""
            @Component
            public class StartupListener {

                @EventListener
                public void onApplicationEvent(ContextRefreshedEvent event) {
                    System.out.println("Application context refreshed");
                }

                @EventListener
                public void onStart(ApplicationStartedEvent event) {
                    System.out.println("Application started");
                }

                @EventListener
                public void onReady(ApplicationReadyEvent event) {
                    System.out.println("Application ready to serve requests");
                }

                @EventListener
                public void onShutdown(ContextClosedEvent event) {
                    System.out.println("Application shutting down");
                }
            }
            """);

        System.out.println("2. Custom events:");
        System.out.println("""
            // Custom event
            public class UserRegisteredEvent extends ApplicationEvent {
                private final String username;

                public UserRegisteredEvent(Object source, String username) {
                    super(source);
                    this.username = username;
                }

                public String getUsername() {
                    return username;
                }
            }

            // Publisher
            @Service
            public class UserService {

                @Autowired
                private ApplicationEventPublisher eventPublisher;

                public void registerUser(String username) {
                    // Register user logic...

                    // Publish event
                    eventPublisher.publishEvent(
                        new UserRegisteredEvent(this, username)
                    );
                }
            }

            // Listener
            @Component
            public class UserEventListener {

                @EventListener
                public void onUserRegistered(UserRegisteredEvent event) {
                    System.out.println("User registered: " + event.getUsername());
                    // Send welcome email, log, etc.
                }

                // Async listener
                @EventListener
                @Async
                public void onUserRegisteredAsync(UserRegisteredEvent event) {
                    // Process asynchronously
                }

                // Conditional listener
                @EventListener(condition = "#event.username.length() > 5")
                public void onLongUsername(UserRegisteredEvent event) {
                    // Only triggered for usernames longer than 5 chars
                }
            }
            """);

        System.out.println("3. Transaction events:");
        System.out.println("""
            @Component
            public class TransactionEventListener {

                @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
                public void handleAfterCommit(UserRegisteredEvent event) {
                    // Called after transaction commits successfully
                    sendWelcomeEmail(event.getUsername());
                }

                @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
                public void handleAfterRollback(UserRegisteredEvent event) {
                    // Called after transaction rollback
                    logFailure(event);
                }

                @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
                public void handleBeforeCommit(UserRegisteredEvent event) {
                    // Called before transaction commits
                }
            }
            """);

        System.out.println();
    }

    // ========== Context Customization ==========

    private static void customizationDemo() {
        System.out.println("--- Context Customization ---");
        System.out.println("Advanced context configuration\n");

        System.out.println("1. BeanFactoryPostProcessor:");
        System.out.println("""
            @Component
            public class CustomBeanFactoryPostProcessor
                    implements BeanFactoryPostProcessor {

                @Override
                public void postProcessBeanFactory(
                        ConfigurableListableBeanFactory beanFactory)
                        throws BeansException {

                    // Modify bean definitions before instantiation
                    String[] beanNames = beanFactory.getBeanDefinitionNames();

                    for (String beanName : beanNames) {
                        BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
                        // Modify bean definition
                        bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
                    }
                }
            }
            """);

        System.out.println("2. ApplicationContextInitializer:");
        System.out.println("""
            public class CustomContextInitializer
                    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

                @Override
                public void initialize(ConfigurableApplicationContext context) {
                    // Add property sources
                    ConfigurableEnvironment env = context.getEnvironment();
                    Map<String, Object> props = new HashMap<>();
                    props.put("custom.property", "value");
                    env.getPropertySources().addFirst(
                        new MapPropertySource("customProps", props)
                    );
                }
            }

            // Register in application.properties
            context.initializer.classes=com.example.CustomContextInitializer
            """);

        System.out.println("3. EnvironmentPostProcessor:");
        System.out.println("""
            public class CustomEnvironmentPostProcessor
                    implements EnvironmentPostProcessor {

                @Override
                public void postProcessEnvironment(ConfigurableEnvironment environment,
                                                  SpringApplication application) {
                    // Modify environment before context creation
                    Properties props = new Properties();
                    props.put("custom.prop", "value");

                    environment.getPropertySources().addLast(
                        new PropertiesPropertySource("customProps", props)
                    );
                }
            }

            // Register in META-INF/spring.factories
            org.springframework.boot.env.EnvironmentPostProcessor=\\
              com.example.CustomEnvironmentPostProcessor
            """);

        System.out.println("4. Bean scopes:");
        System.out.println("""
            // Singleton (default) - one instance per context
            @Bean
            @Scope("singleton")
            public MyService singletonService() { }

            // Prototype - new instance each time
            @Bean
            @Scope("prototype")
            public MyService prototypeService() { }

            // Request - one per HTTP request (web apps)
            @Bean
            @Scope("request")
            public MyService requestService() { }

            // Session - one per HTTP session (web apps)
            @Bean
            @Scope("session")
            public MyService sessionService() { }

            // Custom scope
            @Bean
            @Scope("custom")
            public MyService customScopeService() { }
            """);

        System.out.println("5. Lazy initialization:");
        System.out.println("""
            // Lazy bean - created on first use
            @Bean
            @Lazy
            public ExpensiveService expensiveService() {
                return new ExpensiveService();
            }

            // Global lazy initialization
            spring.main.lazy-initialization=true

            // Eager bean (override global lazy)
            @Bean
            @Lazy(false)
            public CriticalService criticalService() {
                return new CriticalService();
            }
            """);

        System.out.println();
    }
}
