package com.example.demo.showcase;

/**
 * Demonstrates JDBC (Java Database Connectivity) and Spring JdbcTemplate
 * Covers SQL operations, transactions, connection pooling, and best practices
 */
public class JDBCShowcase {

    public static void demonstrate() {
        System.out.println("\n========== JDBC SHOWCASE ==========\n");

        jdbcOverviewDemo();
        jdbcTemplateDemo();
        namedParameterDemo();
        transactionsDemo();
        advancedFeaturesDemo();
    }

    // ========== Overview ==========

    private static void jdbcOverviewDemo() {
        System.out.println("--- JDBC Overview ---");
        System.out.println("Java Database Connectivity API\n");

        System.out.println("1. Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-jdbc</artifactId>
            </dependency>

            <!-- H2 Database (in-memory for testing) -->
            <dependency>
                <groupId>com.h2database</groupId>
                <artifactId>h2</artifactId>
                <scope>runtime</scope>
            </dependency>

            # application.properties
            spring.datasource.url=jdbc:h2:mem:testdb
            spring.datasource.driverClassName=org.h2.Driver
            spring.datasource.username=sa
            spring.datasource.password=

            # Connection pool (HikariCP - default)
            spring.datasource.hikari.maximum-pool-size=10
            spring.datasource.hikari.minimum-idle=5
            spring.datasource.hikari.connection-timeout=30000
            """);

        System.out.println();
    }

    // ========== JdbcTemplate ==========

    private static void jdbcTemplateDemo() {
        System.out.println("--- JdbcTemplate ---");
        System.out.println("Spring's simplified JDBC access\n");

        System.out.println("1. Query operations:");
        System.out.println("""
            @Repository
            public class UserRepository {

                @Autowired
                private JdbcTemplate jdbcTemplate;

                // Query for single object
                public User findById(Long id) {
                    String sql = "SELECT * FROM users WHERE id = ?";

                    return jdbcTemplate.queryForObject(sql,
                        new BeanPropertyRowMapper<>(User.class), id);
                }

                // Query for list
                public List<User> findAll() {
                    String sql = "SELECT * FROM users";

                    return jdbcTemplate.query(sql,
                        new BeanPropertyRowMapper<>(User.class));
                }

                // Query with RowMapper
                public List<User> findByName(String name) {
                    String sql = "SELECT * FROM users WHERE name = ?";

                    return jdbcTemplate.query(sql, (rs, rowNum) -> {
                        User user = new User();
                        user.setId(rs.getLong("id"));
                        user.setName(rs.getString("name"));
                        user.setAge(rs.getInt("age"));
                        user.setEmail(rs.getString("email"));
                        return user;
                    }, name);
                }

                // Query for primitive/simple type
                public int countUsers() {
                    return jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM users", Integer.class
                    );
                }

                public String findUserName(Long id) {
                    return jdbcTemplate.queryForObject(
                        "SELECT name FROM users WHERE id = ?",
                        String.class, id
                    );
                }

                // Query for Map
                public Map<String, Object> findUserMap(Long id) {
                    return jdbcTemplate.queryForMap(
                        "SELECT * FROM users WHERE id = ?", id
                    );
                }
            }
            """);

        System.out.println("2. Update operations:");
        System.out.println("""
            public int create(User user) {
                String sql = "INSERT INTO users (name, age, email) VALUES (?, ?, ?)";

                return jdbcTemplate.update(sql,
                    user.getName(), user.getAge(), user.getEmail());
            }

            public int update(User user) {
                String sql = "UPDATE users SET name = ?, age = ?, email = ? WHERE id = ?";

                return jdbcTemplate.update(sql,
                    user.getName(), user.getAge(), user.getEmail(), user.getId());
            }

            public int delete(Long id) {
                return jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
            }

            // Batch updates
            public int[] batchUpdate(List<User> users) {
                String sql = "INSERT INTO users (name, age, email) VALUES (?, ?, ?)";

                return jdbcTemplate.batchUpdate(sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i)
                                throws SQLException {
                            User user = users.get(i);
                            ps.setString(1, user.getName());
                            ps.setInt(2, user.getAge());
                            ps.setString(3, user.getEmail());
                        }

                        @Override
                        public int getBatchSize() {
                            return users.size();
                        }
                    });
            }
            """);

        System.out.println("3. Get generated keys:");
        System.out.println("""
            public Long createAndReturnId(User user) {
                String sql = "INSERT INTO users (name, age, email) VALUES (?, ?, ?)";

                KeyHolder keyHolder = new GeneratedKeyHolder();

                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql,
                        new String[]{"id"});
                    ps.setString(1, user.getName());
                    ps.setInt(2, user.getAge());
                    ps.setString(3, user.getEmail());
                    return ps;
                }, keyHolder);

                return keyHolder.getKey().longValue();
            }
            """);

        System.out.println();
    }

    // ========== Named Parameters ==========

    private static void namedParameterDemo() {
        System.out.println("--- NamedParameterJdbcTemplate ---");
        System.out.println("Use named parameters instead of ? placeholders\n");

        System.out.println("1. Basic usage:");
        System.out.println("""
            @Repository
            public class UserRepository {

                @Autowired
                private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

                public User findById(Long id) {
                    String sql = "SELECT * FROM users WHERE id = :id";

                    MapSqlParameterSource params = new MapSqlParameterSource();
                    params.addValue("id", id);

                    return namedParameterJdbcTemplate.queryForObject(sql,
                        params, new BeanPropertyRowMapper<>(User.class));
                }

                // Multiple parameters
                public List<User> findByAgeRange(int minAge, int maxAge) {
                    String sql = "SELECT * FROM users WHERE age BETWEEN :minAge AND :maxAge";

                    Map<String, Object> params = new HashMap<>();
                    params.put("minAge", minAge);
                    params.put("maxAge", maxAge);

                    return namedParameterJdbcTemplate.query(sql,
                        params, new BeanPropertyRowMapper<>(User.class));
                }

                // Bean properties as parameters
                public int create(User user) {
                    String sql = "INSERT INTO users (name, age, email) " +
                                "VALUES (:name, :age, :email)";

                    SqlParameterSource params = new BeanPropertySqlParameterSource(user);

                    return namedParameterJdbcTemplate.update(sql, params);
                }
            }
            """);

        System.out.println("2. IN clause:");
        System.out.println("""
            public List<User> findByIds(List<Long> ids) {
                String sql = "SELECT * FROM users WHERE id IN (:ids)";

                MapSqlParameterSource params = new MapSqlParameterSource();
                params.addValue("ids", ids);

                return namedParameterJdbcTemplate.query(sql,
                    params, new BeanPropertyRowMapper<>(User.class));
            }
            """);

        System.out.println();
    }

    // ========== Transactions ==========

    private static void transactionsDemo() {
        System.out.println("--- Transactions ---");
        System.out.println("ACID properties with @Transactional\n");

        System.out.println("1. Declarative transactions:");
        System.out.println("""
            @Service
            public class UserService {

                @Autowired
                private UserRepository userRepository;

                @Transactional
                public void transferPoints(Long fromUserId, Long toUserId, int points) {
                    // Both succeed or both rollback
                    userRepository.deductPoints(fromUserId, points);
                    userRepository.addPoints(toUserId, points);

                    // If exception thrown, both operations rollback
                }

                @Transactional(readOnly = true)
                public User findById(Long id) {
                    // Optimization for read-only transactions
                    return userRepository.findById(id);
                }

                @Transactional(
                    propagation = Propagation.REQUIRES_NEW,
                    isolation = Isolation.READ_COMMITTED,
                    timeout = 30,
                    rollbackFor = Exception.class
                )
                public void complexTransaction() {
                    // Custom transaction settings
                }
            }
            """);

        System.out.println("2. Programmatic transactions:");
        System.out.println("""
            @Service
            public class UserService {

                @Autowired
                private TransactionTemplate transactionTemplate;

                public void performTransaction() {
                    transactionTemplate.execute(status -> {
                        try {
                            // Transaction operations
                            userRepository.update(user);
                            orderRepository.create(order);

                            return true;
                        } catch (Exception e) {
                            status.setRollbackOnly();
                            return false;
                        }
                    });
                }
            }
            """);

        System.out.println("3. Isolation levels:");
        System.out.println("   READ_UNCOMMITTED: Dirty reads possible");
        System.out.println("   READ_COMMITTED: No dirty reads (default)");
        System.out.println("   REPEATABLE_READ: Consistent reads");
        System.out.println("   SERIALIZABLE: Highest isolation, slowest");

        System.out.println("\n4. Propagation types:");
        System.out.println("   REQUIRED: Use existing or create new (default)");
        System.out.println("   REQUIRES_NEW: Always create new");
        System.out.println("   NESTED: Nested within existing");
        System.out.println("   SUPPORTS: Use existing if available");
        System.out.println("   NOT_SUPPORTED: Execute non-transactionally");
        System.out.println("   NEVER: Error if transaction exists");
        System.out.println("   MANDATORY: Error if no transaction");

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced JDBC Features ---");
        System.out.println("Connection pooling, stored procedures, and best practices\n");

        System.out.println("1. Connection pooling (HikariCP - default):");
        System.out.println("""
            # application.properties
            spring.datasource.hikari.maximum-pool-size=10
            spring.datasource.hikari.minimum-idle=5
            spring.datasource.hikari.connection-timeout=30000
            spring.datasource.hikari.idle-timeout=600000
            spring.datasource.hikari.max-lifetime=1800000
            spring.datasource.hikari.leak-detection-threshold=60000

            # Java configuration
            @Bean
            public DataSource dataSource() {
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl("jdbc:postgresql://localhost/mydb");
                config.setUsername("user");
                config.setPassword("password");
                config.setMaximumPoolSize(10);

                return new HikariDataSource(config);
            }
            """);

        System.out.println("2. Stored procedures:");
        System.out.println("""
            public List<User> callStoredProcedure(int minAge) {
                SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withProcedureName("get_users_by_age")
                    .returningResultSet("users",
                        new BeanPropertyRowMapper<>(User.class));

                Map<String, Object> params = new HashMap<>();
                params.put("min_age", minAge);

                Map<String, Object> result = jdbcCall.execute(params);

                return (List<User>) result.get("users");
            }
            """);

        System.out.println("3. ResultSetExtractor:");
        System.out.println("""
            // For complex result sets (e.g., one-to-many)
            public Map<User, List<Order>> findUsersWithOrders() {
                String sql = "SELECT u.*, o.* FROM users u " +
                           "LEFT JOIN orders o ON u.id = o.user_id";

                return jdbcTemplate.query(sql, rs -> {
                    Map<User, List<Order>> result = new HashMap<>();
                    User currentUser = null;
                    List<Order> orders = null;

                    while (rs.next()) {
                        Long userId = rs.getLong("id");

                        if (currentUser == null || !currentUser.getId().equals(userId)) {
                            currentUser = new User();
                            currentUser.setId(userId);
                            currentUser.setName(rs.getString("name"));

                            orders = new ArrayList<>();
                            result.put(currentUser, orders);
                        }

                        if (rs.getLong("order_id") != 0) {
                            Order order = new Order();
                            order.setId(rs.getLong("order_id"));
                            orders.add(order);
                        }
                    }

                    return result;
                });
            }
            """);

        System.out.println("4. Best practices:");
        System.out.println("   ✓ Use connection pooling (HikariCP)");
        System.out.println("   ✓ Close resources (try-with-resources)");
        System.out.println("   ✓ Use parameterized queries (prevent SQL injection)");
        System.out.println("   ✓ Use transactions for multiple operations");
        System.out.println("   ✓ Set appropriate isolation levels");
        System.out.println("   ✓ Use batch updates for bulk operations");
        System.out.println("   ✓ Monitor connection pool metrics");
        System.out.println("   ✗ Don't concatenate SQL with user input");
        System.out.println("   ✗ Don't forget to close connections");
        System.out.println("   ✗ Don't use SELECT * in production");

        System.out.println("\n5. Schema initialization:");
        System.out.println("""
            # application.properties
            spring.sql.init.mode=always
            spring.sql.init.schema-locations=classpath:schema.sql
            spring.sql.init.data-locations=classpath:data.sql

            # schema.sql
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                age INT,
                email VARCHAR(100)
            );

            # data.sql
            INSERT INTO users (name, age, email) VALUES
                ('John Doe', 30, 'john@example.com'),
                ('Jane Smith', 25, 'jane@example.com');
            """);

        System.out.println();
    }
}
