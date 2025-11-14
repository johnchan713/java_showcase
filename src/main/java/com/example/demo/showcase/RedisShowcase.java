package com.example.demo.showcase;

/**
 * Demonstrates Redis integration with Spring Data Redis
 * Covers RedisTemplate, caching, pub/sub, and various data structures
 *
 * Note: This is a demonstration - Redis server not required to run
 */
public class RedisShowcase {

    public static void demonstrate() {
        System.out.println("\n========== REDIS SHOWCASE ==========\n");

        redisOverviewDemo();
        configurationDemo();
        redisTemplateDemo();
        dataStructuresDemo();
        cachingDemo();
        pubSubDemo();
        advancedFeaturesDemo();
    }

    // ========== Redis Overview ==========

    private static void redisOverviewDemo() {
        System.out.println("--- Redis Overview ---");
        System.out.println("In-memory data structure store\n");

        System.out.println("1. Use cases:");
        System.out.println("   ✓ Caching (session cache, page cache, API responses)");
        System.out.println("   ✓ Session management");
        System.out.println("   ✓ Pub/Sub messaging");
        System.out.println("   ✓ Real-time analytics");
        System.out.println("   ✓ Leaderboards and counters");
        System.out.println("   ✓ Rate limiting");
        System.out.println("   ✓ Distributed locks");

        System.out.println("\n2. Data structures:");
        System.out.println("   String: Simple key-value");
        System.out.println("   Hash: Maps (field-value pairs)");
        System.out.println("   List: Ordered collections");
        System.out.println("   Set: Unordered unique elements");
        System.out.println("   Sorted Set: Scored unique elements");
        System.out.println("   Bitmap: Bit-level operations");
        System.out.println("   HyperLogLog: Probabilistic cardinality");

        System.out.println("\n3. Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-redis</artifactId>
            </dependency>

            <!-- Lettuce (default, async/reactive) -->
            <!-- Already included in spring-boot-starter-data-redis -->

            <!-- OR Jedis (synchronous, simpler) -->
            <dependency>
                <groupId>redis.clients</groupId>
                <artifactId>jedis</artifactId>
            </dependency>
            """);

        System.out.println();
    }

    // ========== Configuration ==========

    private static void configurationDemo() {
        System.out.println("--- Redis Configuration ---");
        System.out.println("Configure Redis connection and templates\n");

        System.out.println("1. application.properties:");
        System.out.println("""
            # Redis server
            spring.data.redis.host=localhost
            spring.data.redis.port=6379
            spring.data.redis.password=secret
            spring.data.redis.database=0

            # Connection pool (Lettuce)
            spring.data.redis.lettuce.pool.max-active=8
            spring.data.redis.lettuce.pool.max-idle=8
            spring.data.redis.lettuce.pool.min-idle=0
            spring.data.redis.lettuce.pool.max-wait=-1ms

            # Timeout
            spring.data.redis.timeout=60000ms

            # SSL
            spring.data.redis.ssl.enabled=false

            # Cluster configuration
            spring.data.redis.cluster.nodes=node1:6379,node2:6379,node3:6379
            spring.data.redis.cluster.max-redirects=3

            # Sentinel configuration
            spring.data.redis.sentinel.master=mymaster
            spring.data.redis.sentinel.nodes=sentinel1:26379,sentinel2:26379
            """);

        System.out.println("2. Java Configuration (Lettuce):");
        System.out.println("""
            @Configuration
            public class RedisConfig {

                @Bean
                public LettuceConnectionFactory redisConnectionFactory() {
                    RedisStandaloneConfiguration config =
                        new RedisStandaloneConfiguration();
                    config.setHostName("localhost");
                    config.setPort(6379);
                    config.setPassword("secret");
                    config.setDatabase(0);

                    return new LettuceConnectionFactory(config);
                }

                @Bean
                public RedisTemplate<String, Object> redisTemplate(
                        RedisConnectionFactory connectionFactory) {

                    RedisTemplate<String, Object> template = new RedisTemplate<>();
                    template.setConnectionFactory(connectionFactory);

                    // Serializers
                    template.setKeySerializer(new StringRedisSerializer());
                    template.setHashKeySerializer(new StringRedisSerializer());
                    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
                    template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

                    template.afterPropertiesSet();
                    return template;
                }

                @Bean
                public StringRedisTemplate stringRedisTemplate(
                        RedisConnectionFactory connectionFactory) {
                    return new StringRedisTemplate(connectionFactory);
                }
            }
            """);

        System.out.println("3. Jedis Configuration:");
        System.out.println("""
            @Bean
            public JedisConnectionFactory jedisConnectionFactory() {
                RedisStandaloneConfiguration config =
                    new RedisStandaloneConfiguration();
                config.setHostName("localhost");
                config.setPort(6379);

                JedisClientConfiguration clientConfig =
                    JedisClientConfiguration.builder()
                        .usePooling()
                        .poolConfig(jedisPoolConfig())
                        .build();

                return new JedisConnectionFactory(config, clientConfig);
            }

            @Bean
            public JedisPoolConfig jedisPoolConfig() {
                JedisPoolConfig poolConfig = new JedisPoolConfig();
                poolConfig.setMaxTotal(8);
                poolConfig.setMaxIdle(8);
                poolConfig.setMinIdle(0);
                return poolConfig;
            }
            """);

        System.out.println("4. Cluster Configuration:");
        System.out.println("""
            @Bean
            public LettuceConnectionFactory redisClusterConnectionFactory() {
                RedisClusterConfiguration clusterConfig =
                    new RedisClusterConfiguration();

                clusterConfig.addClusterNode(new RedisNode("node1", 6379));
                clusterConfig.addClusterNode(new RedisNode("node2", 6379));
                clusterConfig.addClusterNode(new RedisNode("node3", 6379));

                clusterConfig.setMaxRedirects(3);

                return new LettuceConnectionFactory(clusterConfig);
            }
            """);

        System.out.println();
    }

    // ========== RedisTemplate Operations ==========

    private static void redisTemplateDemo() {
        System.out.println("--- RedisTemplate Operations ---");
        System.out.println("Basic Redis operations with Spring\n");

        System.out.println("1. String operations:");
        System.out.println("""
            @Service
            public class RedisService {

                @Autowired
                private RedisTemplate<String, Object> redisTemplate;

                public void stringOperations() {
                    ValueOperations<String, Object> ops = redisTemplate.opsForValue();

                    // Set value
                    ops.set("key", "value");

                    // Set with expiration
                    ops.set("key", "value", 60, TimeUnit.SECONDS);
                    ops.set("key", "value", Duration.ofMinutes(5));

                    // Get value
                    Object value = ops.get("key");

                    // Set if absent (SETNX)
                    Boolean set = ops.setIfAbsent("key", "value");

                    // Set if present
                    Boolean updated = ops.setIfPresent("key", "newValue");

                    // Get and set
                    Object oldValue = ops.getAndSet("key", "newValue");

                    // Increment
                    Long newValue = ops.increment("counter");
                    Long incremented = ops.increment("counter", 5);

                    // Decrement
                    Long decremented = ops.decrement("counter", 3);

                    // Multiple operations
                    Map<String, Object> map = new HashMap<>();
                    map.put("key1", "value1");
                    map.put("key2", "value2");
                    ops.multiSet(map);

                    List<Object> values = ops.multiGet(Arrays.asList("key1", "key2"));
                }
            }
            """);

        System.out.println("2. Key operations:");
        System.out.println("""
            public void keyOperations() {
                // Check if key exists
                Boolean exists = redisTemplate.hasKey("key");

                // Delete keys
                Boolean deleted = redisTemplate.delete("key");
                Long deletedCount = redisTemplate.delete(Arrays.asList("key1", "key2"));

                // Set expiration
                Boolean expired = redisTemplate.expire("key", 60, TimeUnit.SECONDS);
                Boolean expiredAt = redisTemplate.expireAt("key",
                    Date.from(Instant.now().plusSeconds(3600)));

                // Get TTL
                Long ttl = redisTemplate.getExpire("key");
                Long ttlInSeconds = redisTemplate.getExpire("key", TimeUnit.SECONDS);

                // Remove expiration
                Boolean persisted = redisTemplate.persist("key");

                // Rename key
                redisTemplate.rename("oldKey", "newKey");

                // Get all keys matching pattern
                Set<String> keys = redisTemplate.keys("user:*");

                // Random key
                String randomKey = redisTemplate.randomKey();

                // Get type
                DataType type = redisTemplate.type("key");
            }
            """);

        System.out.println("3. Execute custom commands:");
        System.out.println("""
            public void customCommands() {
                // Execute Redis command
                Object result = redisTemplate.execute(
                    (RedisCallback<Object>) connection -> {
                        return connection.get("key".getBytes());
                    }
                );

                // Pipeline (batch operations)
                List<Object> results = redisTemplate.executePipelined(
                    (RedisCallback<Object>) connection -> {
                        connection.set("key1".getBytes(), "value1".getBytes());
                        connection.set("key2".getBytes(), "value2".getBytes());
                        connection.get("key1".getBytes());
                        return null;
                    }
                );

                // Transaction
                List<Object> txResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
                    @Override
                    public List<Object> execute(RedisOperations operations) {
                        operations.multi();
                        operations.opsForValue().set("key1", "value1");
                        operations.opsForValue().set("key2", "value2");
                        return operations.exec();
                    }
                });
            }
            """);

        System.out.println();
    }

    // ========== Data Structures ==========

    private static void dataStructuresDemo() {
        System.out.println("--- Redis Data Structures ---");
        System.out.println("Lists, Sets, Hashes, and Sorted Sets\n");

        System.out.println("1. Hash operations:");
        System.out.println("""
            public void hashOperations() {
                HashOperations<String, String, Object> hash = redisTemplate.opsForHash();

                // Put field
                hash.put("user:1", "name", "John");
                hash.put("user:1", "age", 30);

                // Put all
                Map<String, Object> userData = new HashMap<>();
                userData.put("name", "John");
                userData.put("age", 30);
                userData.put("email", "john@example.com");
                hash.putAll("user:1", userData);

                // Get field
                Object name = hash.get("user:1", "name");

                // Get all
                Map<String, Object> user = hash.entries("user:1");

                // Get multiple fields
                List<Object> values = hash.multiGet("user:1",
                    Arrays.asList("name", "age"));

                // Check field exists
                Boolean hasField = hash.hasKey("user:1", "name");

                // Delete field
                Long deleted = hash.delete("user:1", "email");

                // Get all keys/values
                Set<String> keys = hash.keys("user:1");
                List<Object> allValues = hash.values("user:1");

                // Increment
                Long newAge = hash.increment("user:1", "age", 1);

                // Size
                Long size = hash.size("user:1");
            }
            """);

        System.out.println("2. List operations:");
        System.out.println("""
            public void listOperations() {
                ListOperations<String, Object> list = redisTemplate.opsForList();

                // Push to right (append)
                list.rightPush("queue", "item1");
                list.rightPush("queue", "item2");
                list.rightPushAll("queue", "item3", "item4", "item5");

                // Push to left (prepend)
                list.leftPush("stack", "item1");
                list.leftPushAll("stack", "item2", "item3");

                // Pop from right
                Object item = list.rightPop("queue");

                // Pop from left
                Object topItem = list.leftPop("stack");

                // Blocking pop (wait for item)
                Object blockedItem = list.rightPop("queue", 10, TimeUnit.SECONDS);

                // Get by index
                Object element = list.index("queue", 0);

                // Get range
                List<Object> range = list.range("queue", 0, -1);  // All elements

                // Set by index
                list.set("queue", 0, "newValue");

                // Size
                Long size = list.size("queue");

                // Trim (keep only range)
                list.trim("queue", 0, 99);  // Keep first 100 elements

                // Remove
                list.remove("queue", 1, "item1");  // Remove 1 occurrence
            }
            """);

        System.out.println("3. Set operations:");
        System.out.println("""
            public void setOperations() {
                SetOperations<String, Object> set = redisTemplate.opsForSet();

                // Add members
                set.add("tags", "java", "spring", "redis");

                // Remove members
                Long removed = set.remove("tags", "redis");

                // Check membership
                Boolean isMember = set.isMember("tags", "java");

                // Get all members
                Set<Object> members = set.members("tags");

                // Size
                Long size = set.size("tags");

                // Random member
                Object random = set.randomMember("tags");
                List<Object> randomMembers = set.randomMembers("tags", 3);

                // Pop random
                Object popped = set.pop("tags");

                // Set operations
                Set<Object> union = set.union("tags1", "tags2");
                Set<Object> intersect = set.intersect("tags1", "tags2");
                Set<Object> diff = set.difference("tags1", "tags2");

                // Store result
                set.unionAndStore("tags1", "tags2", "resultTags");
            }
            """);

        System.out.println("4. Sorted Set operations:");
        System.out.println("""
            public void sortedSetOperations() {
                ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();

                // Add with score
                zset.add("leaderboard", "player1", 100);
                zset.add("leaderboard", "player2", 200);
                zset.add("leaderboard", "player3", 150);

                // Add multiple
                Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();
                tuples.add(ZSetOperations.TypedTuple.of("player4", 180.0));
                tuples.add(ZSetOperations.TypedTuple.of("player5", 220.0));
                zset.add("leaderboard", tuples);

                // Get score
                Double score = zset.score("leaderboard", "player1");

                // Increment score
                Double newScore = zset.incrementScore("leaderboard", "player1", 50);

                // Rank (0-based, lowest to highest)
                Long rank = zset.rank("leaderboard", "player1");

                // Reverse rank (highest to lowest)
                Long reverseRank = zset.reverseRank("leaderboard", "player1");

                // Range by score
                Set<Object> topPlayers = zset.reverseRange("leaderboard", 0, 9);  // Top 10

                // Range with scores
                Set<ZSetOperations.TypedTuple<Object>> playersWithScores =
                    zset.reverseRangeWithScores("leaderboard", 0, 9);

                // Range by score value
                Set<Object> highScorers = zset.rangeByScore("leaderboard", 150, 250);

                // Count
                Long count = zset.count("leaderboard", 100, 200);

                // Size
                Long size = zset.size("leaderboard");

                // Remove
                zset.remove("leaderboard", "player1");

                // Remove by rank
                zset.removeRange("leaderboard", 0, 2);  // Remove lowest 3

                // Remove by score
                zset.removeRangeByScore("leaderboard", 0, 100);
            }
            """);

        System.out.println();
    }

    // ========== Caching ==========

    private static void cachingDemo() {
        System.out.println("--- Redis Caching ---");
        System.out.println("Spring Cache abstraction with Redis\n");

        System.out.println("1. Enable caching:");
        System.out.println("""
            @Configuration
            @EnableCaching
            public class CacheConfig {

                @Bean
                public RedisCacheManager cacheManager(
                        RedisConnectionFactory connectionFactory) {

                    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10))  // Default TTL
                        .disableCachingNullValues()
                        .serializeKeysWith(RedisSerializationContext.SerializationPair
                            .fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair
                            .fromSerializer(new GenericJackson2JsonRedisSerializer()));

                    // Cache-specific configurations
                    Map<String, RedisCacheConfiguration> cacheConfigurations =
                        new HashMap<>();

                    // Short-lived cache (1 minute)
                    cacheConfigurations.put("shortLived",
                        config.entryTtl(Duration.ofMinutes(1)));

                    // Long-lived cache (1 hour)
                    cacheConfigurations.put("longLived",
                        config.entryTtl(Duration.ofHours(1)));

                    return RedisCacheManager.builder(connectionFactory)
                        .cacheDefaults(config)
                        .withInitialCacheConfigurations(cacheConfigurations)
                        .build();
                }
            }
            """);

        System.out.println("2. @Cacheable:");
        System.out.println("""
            @Service
            public class UserService {

                @Cacheable(value = "users", key = "#id")
                public User findById(Long id) {
                    System.out.println("Fetching from database: " + id);
                    // Expensive database query
                    return userRepository.findById(id).orElse(null);
                    // Result cached, subsequent calls use cache
                }

                @Cacheable(value = "users", key = "#username", unless = "#result == null")
                public User findByUsername(String username) {
                    // Don't cache null results
                    return userRepository.findByUsername(username).orElse(null);
                }

                @Cacheable(value = "users", condition = "#id > 0")
                public User findByIdConditional(Long id) {
                    // Only cache if id > 0
                    return userRepository.findById(id).orElse(null);
                }
            }
            """);

        System.out.println("3. @CachePut:");
        System.out.println("""
            @CachePut(value = "users", key = "#user.id")
            public User updateUser(User user) {
                // Always executes method and updates cache
                return userRepository.save(user);
            }
            """);

        System.out.println("4. @CacheEvict:");
        System.out.println("""
            @CacheEvict(value = "users", key = "#id")
            public void deleteUser(Long id) {
                // Removes entry from cache
                userRepository.deleteById(id);
            }

            @CacheEvict(value = "users", allEntries = true)
            public void clearAllUsers() {
                // Clears entire cache
            }

            @CacheEvict(value = "users", allEntries = true, beforeInvocation = true)
            public void clearBeforeOperation() {
                // Clear cache before method executes
                // Useful if method might fail
            }
            """);

        System.out.println("5. @Caching:");
        System.out.println("""
            @Caching(
                cacheable = {
                    @Cacheable(value = "users", key = "#id"),
                    @Cacheable(value = "usersByEmail", key = "#result.email")
                },
                evict = {
                    @CacheEvict(value = "allUsers", allEntries = true)
                }
            )
            public User getUserComplex(Long id) {
                // Multiple cache operations
                return userRepository.findById(id).orElse(null);
            }
            """);

        System.out.println("6. Manual cache access:");
        System.out.println("""
            @Service
            public class CacheService {

                @Autowired
                private CacheManager cacheManager;

                public void manualCacheAccess() {
                    Cache cache = cacheManager.getCache("users");

                    // Put
                    cache.put("key", "value");

                    // Get
                    Cache.ValueWrapper wrapper = cache.get("key");
                    if (wrapper != null) {
                        Object value = wrapper.get();
                    }

                    // Evict
                    cache.evict("key");

                    // Clear
                    cache.clear();
                }
            }
            """);

        System.out.println();
    }

    // ========== Pub/Sub ==========

    private static void pubSubDemo() {
        System.out.println("--- Redis Pub/Sub ---");
        System.out.println("Publish/Subscribe messaging pattern\n");

        System.out.println("1. Configure listener:");
        System.out.println("""
            @Configuration
            public class RedisPubSubConfig {

                @Bean
                public RedisMessageListenerContainer messageListenerContainer(
                        RedisConnectionFactory connectionFactory) {

                    RedisMessageListenerContainer container =
                        new RedisMessageListenerContainer();
                    container.setConnectionFactory(connectionFactory);

                    // Subscribe to channel
                    container.addMessageListener(messageListener(),
                        new ChannelTopic("notifications"));

                    // Subscribe to pattern
                    container.addMessageListener(messageListener(),
                        new PatternTopic("user.*"));

                    return container;
                }

                @Bean
                public MessageListenerAdapter messageListener() {
                    return new MessageListenerAdapter(new MessageSubscriber());
                }
            }
            """);

        System.out.println("2. Subscriber:");
        System.out.println("""
            @Component
            public class MessageSubscriber implements MessageListener {

                @Override
                public void onMessage(Message message, byte[] pattern) {
                    String channel = new String(message.getChannel());
                    String body = new String(message.getBody());

                    System.out.println("Received on channel " + channel + ": " + body);

                    // Process message
                }
            }

            // Alternative: Annotation-based
            @Component
            public class AnnotationSubscriber {

                @RedisListener(topic = "notifications")
                public void handleNotification(String message) {
                    System.out.println("Notification: " + message);
                }

                @RedisListener(topic = "user.*", pattern = true)
                public void handleUserEvents(String message, String channel) {
                    System.out.println("User event on " + channel + ": " + message);
                }
            }
            """);

        System.out.println("3. Publisher:");
        System.out.println("""
            @Service
            public class MessagePublisher {

                @Autowired
                private RedisTemplate<String, Object> redisTemplate;

                public void publishNotification(String message) {
                    redisTemplate.convertAndSend("notifications", message);
                }

                public void publishUserEvent(String userId, String event) {
                    redisTemplate.convertAndSend("user." + userId, event);
                }

                public void publishObject(NotificationEvent event) {
                    // Auto-serialized to JSON
                    redisTemplate.convertAndSend("events", event);
                }
            }
            """);

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced Redis Features ---");
        System.out.println("Distributed locks, rate limiting, and more\n");

        System.out.println("1. Distributed lock:");
        System.out.println("""
            @Service
            public class DistributedLockService {

                @Autowired
                private StringRedisTemplate redisTemplate;

                public boolean acquireLock(String lockKey, String lockValue, long timeout) {
                    // SETNX with expiration (atomic operation)
                    Boolean locked = redisTemplate.opsForValue()
                        .setIfAbsent(lockKey, lockValue, timeout, TimeUnit.SECONDS);

                    return Boolean.TRUE.equals(locked);
                }

                public boolean releaseLock(String lockKey, String lockValue) {
                    // Lua script for atomic get-and-delete
                    String script =
                        "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                        "  return redis.call('del', KEYS[1]) " +
                        "else " +
                        "  return 0 " +
                        "end";

                    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
                    redisScript.setScriptText(script);
                    redisScript.setResultType(Long.class);

                    Long result = redisTemplate.execute(redisScript,
                        Collections.singletonList(lockKey), lockValue);

                    return result != null && result == 1L;
                }

                public void withLock(String lockKey, Runnable task) {
                    String lockValue = UUID.randomUUID().toString();
                    try {
                        if (acquireLock(lockKey, lockValue, 30)) {
                            task.run();
                        } else {
                            throw new RuntimeException("Could not acquire lock");
                        }
                    } finally {
                        releaseLock(lockKey, lockValue);
                    }
                }
            }
            """);

        System.out.println("2. Rate limiting:");
        System.out.println("""
            @Service
            public class RateLimiterService {

                @Autowired
                private StringRedisTemplate redisTemplate;

                public boolean isAllowed(String userId, int maxRequests, int windowSeconds) {
                    String key = "rate:" + userId;
                    Long currentTime = System.currentTimeMillis();
                    Long windowStart = currentTime - (windowSeconds * 1000L);

                    // Use sorted set with timestamps as scores
                    ZSetOperations<String, String> zset = redisTemplate.opsForZSet();

                    // Remove old entries
                    zset.removeRangeByScore(key, 0, windowStart);

                    // Count requests in window
                    Long count = zset.count(key, windowStart, currentTime);

                    if (count != null && count < maxRequests) {
                        // Add current request
                        zset.add(key, String.valueOf(currentTime), currentTime);
                        redisTemplate.expire(key, windowSeconds, TimeUnit.SECONDS);
                        return true;
                    }

                    return false;
                }

                // Token bucket algorithm
                public boolean isAllowedTokenBucket(String userId, int capacity, int refillRate) {
                    String key = "bucket:" + userId;
                    Long now = System.currentTimeMillis() / 1000;

                    String script =
                        "local tokens_key = KEYS[1] " +
                        "local timestamp_key = KEYS[2] " +
                        "local capacity = tonumber(ARGV[1]) " +
                        "local rate = tonumber(ARGV[2]) " +
                        "local now = tonumber(ARGV[3]) " +
                        "local tokens = tonumber(redis.call('get', tokens_key)) " +
                        "local last = tonumber(redis.call('get', timestamp_key)) " +
                        "if tokens == nil then tokens = capacity end " +
                        "if last == nil then last = now end " +
                        "local delta = math.max(0, now - last) " +
                        "local filled = math.min(capacity, tokens + (delta * rate)) " +
                        "local allowed = filled >= 1 " +
                        "if allowed then filled = filled - 1 end " +
                        "redis.call('setex', tokens_key, 86400, filled) " +
                        "redis.call('setex', timestamp_key, 86400, now) " +
                        "return allowed";

                    DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
                    redisScript.setScriptText(script);
                    redisScript.setResultType(Boolean.class);

                    return Boolean.TRUE.equals(redisTemplate.execute(redisScript,
                        Arrays.asList(key + ":tokens", key + ":timestamp"),
                        String.valueOf(capacity), String.valueOf(refillRate), String.valueOf(now)));
                }
            }
            """);

        System.out.println("3. Lua scripts:");
        System.out.println("""
            @Service
            public class LuaScriptService {

                @Autowired
                private RedisTemplate<String, Object> redisTemplate;

                public Long incrementWithMax(String key, long max) {
                    String script =
                        "local current = redis.call('get', KEYS[1]) " +
                        "if current == false then current = 0 else current = tonumber(current) end " +
                        "if current < tonumber(ARGV[1]) then " +
                        "  return redis.call('incr', KEYS[1]) " +
                        "else " +
                        "  return current " +
                        "end";

                    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
                    redisScript.setScriptText(script);
                    redisScript.setResultType(Long.class);

                    return redisTemplate.execute(redisScript,
                        Collections.singletonList(key), String.valueOf(max));
                }
            }
            """);

        System.out.println("4. Best practices:");
        System.out.println("   ✓ Always set expiration on keys (prevent memory leak)");
        System.out.println("   ✓ Use connection pooling");
        System.out.println("   ✓ Use pipelining for batch operations");
        System.out.println("   ✓ Use Lua scripts for atomic operations");
        System.out.println("   ✓ Monitor memory usage and eviction policy");
        System.out.println("   ✓ Use Redis Cluster for high availability");
        System.out.println("   ✓ Consider data persistence (RDB/AOF)");
        System.out.println("   ✗ Don't use Redis as primary database");
        System.out.println("   ✗ Don't store large objects (> 1MB)");
        System.out.println("   ✗ Avoid KEYS command in production");

        System.out.println();
    }
}
