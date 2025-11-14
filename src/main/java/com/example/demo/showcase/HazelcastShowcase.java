package com.example.demo.showcase;

/**
 * Demonstrates Hazelcast - In-Memory Data Grid (IMDG)
 * Covers distributed data structures, clustering, locks, caching, and events
 */
public class HazelcastShowcase {

    public static void demonstrate() {
        System.out.println("\n========== HAZELCAST SHOWCASE ==========\n");

        hazelcastOverviewDemo();
        distributedDataStructuresDemo();
        distributedLockDemo();
        distributedCachingDemo();
        clusteringDemo();
        advancedFeaturesDemo();
    }

    // ========== Overview ==========

    private static void hazelcastOverviewDemo() {
        System.out.println("--- Hazelcast Overview ---");
        System.out.println("In-Memory Data Grid for distributed computing\n");

        System.out.println("1. Key features:");
        System.out.println("   • Distributed data structures (Map, Queue, List, Set, Topic)");
        System.out.println("   • Distributed caching with near cache");
        System.out.println("   • Distributed locks and semaphores");
        System.out.println("   • Cluster management and partitioning");
        System.out.println("   • Event listeners for data changes");
        System.out.println("   • WAN replication");
        System.out.println("   • CP Subsystem for strong consistency");

        System.out.println("\n2. Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>com.hazelcast</groupId>
                <artifactId>hazelcast</artifactId>
                <version>5.3.6</version>
            </dependency>
            <dependency>
                <groupId>com.hazelcast</groupId>
                <artifactId>hazelcast-spring</artifactId>
                <version>5.3.6</version>
            </dependency>
            """);

        System.out.println("3. Basic setup:");
        System.out.println("""
            import com.hazelcast.core.Hazelcast;
            import com.hazelcast.core.HazelcastInstance;
            import com.hazelcast.config.Config;

            // Programmatic configuration
            Config config = new Config();
            config.setClusterName("my-cluster");
            HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);

            // Spring Boot auto-configuration
            @Configuration
            public class HazelcastConfig {
                @Bean
                public Config hazelcastConfig() {
                    Config config = new Config();
                    config.setClusterName("spring-cluster");

                    // Network configuration
                    config.getNetworkConfig()
                        .setPort(5701)
                        .setPortAutoIncrement(true);

                    return config;
                }
            }
            """);

        System.out.println();
    }

    // ========== Distributed Data Structures ==========

    private static void distributedDataStructuresDemo() {
        System.out.println("--- Distributed Data Structures ---");
        System.out.println("Thread-safe distributed collections\n");

        System.out.println("1. IMap (Distributed HashMap):");
        System.out.println("""
            import com.hazelcast.map.IMap;

            @Service
            public class CacheService {

                @Autowired
                private HazelcastInstance hazelcastInstance;

                public void mapOperations() {
                    IMap<String, User> userMap = hazelcastInstance.getMap("users");

                    // Put and get
                    userMap.put("user1", new User("John", 30));
                    User user = userMap.get("user1");

                    // Put with TTL (expires after 60 seconds)
                    userMap.put("user2", new User("Jane", 25), 60, TimeUnit.SECONDS);

                    // Put if absent
                    userMap.putIfAbsent("user3", new User("Bob", 35));

                    // Replace
                    userMap.replace("user1", new User("John Updated", 31));

                    // Remove
                    userMap.remove("user2");

                    // Iterate
                    for (Map.Entry<String, User> entry : userMap.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }

                    // Size across all cluster members
                    int totalSize = userMap.size();
                }
            }
            """);

        System.out.println("2. IQueue (Distributed Queue):");
        System.out.println("""
            import com.hazelcast.collection.IQueue;

            public void queueOperations() {
                IQueue<String> queue = hazelcastInstance.getQueue("task-queue");

                // Producer
                queue.offer("task1");
                queue.offer("task2", 10, TimeUnit.SECONDS);  // With timeout

                // Consumer (blocking)
                String task = queue.take();  // Waits if empty

                // Consumer (non-blocking)
                String nextTask = queue.poll();  // Returns null if empty
                String timedTask = queue.poll(5, TimeUnit.SECONDS);  // Wait timeout

                // Peek without removing
                String peek = queue.peek();

                // Size
                int size = queue.size();
            }
            """);

        System.out.println("3. IList (Distributed ArrayList):");
        System.out.println("""
            import com.hazelcast.collection.IList;

            public void listOperations() {
                IList<String> list = hazelcastInstance.getList("shared-list");

                list.add("item1");
                list.add("item2");
                list.add(0, "first");  // Insert at index

                String item = list.get(1);
                list.remove(0);
                list.clear();

                // Distributed iteration
                for (String element : list) {
                    System.out.println(element);
                }
            }
            """);

        System.out.println("4. ISet (Distributed HashSet):");
        System.out.println("""
            import com.hazelcast.collection.ISet;

            public void setOperations() {
                ISet<String> set = hazelcastInstance.getSet("unique-items");

                set.add("item1");
                set.add("item2");
                set.add("item1");  // Duplicate, won't be added

                boolean contains = set.contains("item1");
                set.remove("item2");

                int size = set.size();  // Returns 1
            }
            """);

        System.out.println("5. ITopic (Pub/Sub):");
        System.out.println("""
            import com.hazelcast.topic.ITopic;
            import com.hazelcast.topic.Message;
            import com.hazelcast.topic.MessageListener;

            public void topicOperations() {
                ITopic<String> topic = hazelcastInstance.getTopic("notifications");

                // Subscribe
                topic.addMessageListener(new MessageListener<String>() {
                    @Override
                    public void onMessage(Message<String> message) {
                        System.out.println("Received: " + message.getMessageObject());
                        System.out.println("Published at: " + message.getPublishTime());
                    }
                });

                // Publish
                topic.publish("Hello Hazelcast!");

                // All cluster members with listeners will receive the message
            }
            """);

        System.out.println("6. MultiMap (One key, multiple values):");
        System.out.println("""
            import com.hazelcast.multimap.MultiMap;

            public void multiMapOperations() {
                MultiMap<String, String> multiMap =
                    hazelcastInstance.getMultiMap("tags");

                // Add multiple values for one key
                multiMap.put("user1", "java");
                multiMap.put("user1", "spring");
                multiMap.put("user1", "hazelcast");

                // Get all values for a key
                Collection<String> tags = multiMap.get("user1");  // [java, spring, hazelcast]

                // Remove specific value
                multiMap.remove("user1", "spring");

                // Remove all values for key
                multiMap.remove("user1");
            }
            """);

        System.out.println();
    }

    // ========== Distributed Locks ==========

    private static void distributedLockDemo() {
        System.out.println("--- Distributed Locks ---");
        System.out.println("Synchronization across cluster members\n");

        System.out.println("1. ILock (Distributed ReentrantLock):");
        System.out.println("""
            import com.hazelcast.cp.lock.FencedLock;

            @Service
            public class InventoryService {

                @Autowired
                private HazelcastInstance hazelcastInstance;

                public void updateInventory(String productId, int quantity) {
                    FencedLock lock = hazelcastInstance.getCPSubsystem()
                        .getLock("product-lock-" + productId);

                    lock.lock();
                    try {
                        // Critical section - only one cluster member at a time
                        int current = getInventory(productId);
                        updateInventory(productId, current - quantity);
                    } finally {
                        lock.unlock();
                    }
                }

                public boolean tryLockWithTimeout(String productId) {
                    FencedLock lock = hazelcastInstance.getCPSubsystem()
                        .getLock("product-lock-" + productId);

                    try {
                        if (lock.tryLock(5, TimeUnit.SECONDS)) {
                            try {
                                // Process
                                return true;
                            } finally {
                                lock.unlock();
                            }
                        }
                        return false;  // Could not acquire lock
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
            """);

        System.out.println("2. ISemaphore (Distributed Semaphore):");
        System.out.println("""
            import com.hazelcast.cp.ISemaphore;

            public void semaphoreExample() {
                ISemaphore semaphore = hazelcastInstance.getCPSubsystem()
                    .getSemaphore("connection-pool");

                // Initialize with 10 permits
                semaphore.init(10);

                try {
                    // Acquire permit
                    semaphore.acquire();

                    // Use resource (e.g., database connection)
                    performDatabaseOperation();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    // Release permit
                    semaphore.release();
                }

                // Try acquire with timeout
                if (semaphore.tryAcquire(3, TimeUnit.SECONDS)) {
                    try {
                        // Got permit
                    } finally {
                        semaphore.release();
                    }
                }
            }
            """);

        System.out.println("3. ICountDownLatch:");
        System.out.println("""
            import com.hazelcast.cp.ICountDownLatch;

            public void countDownLatchExample() {
                ICountDownLatch latch = hazelcastInstance.getCPSubsystem()
                    .getCountDownLatch("startup-latch");

                // Coordinator sets count
                latch.trySetCount(3);

                // Workers count down
                new Thread(() -> {
                    initializeDatabase();
                    latch.countDown();
                }).start();

                new Thread(() -> {
                    initializeCache();
                    latch.countDown();
                }).start();

                new Thread(() -> {
                    initializeMessaging();
                    latch.countDown();
                }).start();

                // Main thread waits
                try {
                    latch.await(30, TimeUnit.SECONDS);
                    System.out.println("All services initialized!");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            """);

        System.out.println();
    }

    // ========== Distributed Caching ==========

    private static void distributedCachingDemo() {
        System.out.println("--- Distributed Caching ---");
        System.out.println("High-performance caching with near cache\n");

        System.out.println("1. Map configuration:");
        System.out.println("""
            @Configuration
            public class HazelcastConfig {

                @Bean
                public Config hazelcastConfig() {
                    Config config = new Config();

                    // Map configuration
                    MapConfig mapConfig = new MapConfig("users");

                    // Eviction policy
                    mapConfig.setTimeToLiveSeconds(300);  // TTL: 5 minutes
                    mapConfig.setMaxIdleSeconds(60);      // Max idle: 1 minute

                    EvictionConfig evictionConfig = new EvictionConfig()
                        .setEvictionPolicy(EvictionPolicy.LRU)
                        .setMaxSizePolicy(MaxSizePolicy.PER_NODE)
                        .setSize(10000);
                    mapConfig.setEvictionConfig(evictionConfig);

                    // Backup configuration
                    mapConfig.setBackupCount(1);          // Sync backups
                    mapConfig.setAsyncBackupCount(1);     // Async backups

                    config.addMapConfig(mapConfig);
                    return config;
                }
            }
            """);

        System.out.println("2. Near Cache (local cache):");
        System.out.println("""
            // Near Cache configuration
            MapConfig mapConfig = new MapConfig("products");

            NearCacheConfig nearCacheConfig = new NearCacheConfig();
            nearCacheConfig.setTimeToLiveSeconds(60);
            nearCacheConfig.setMaxIdleSeconds(30);
            nearCacheConfig.setInvalidateOnChange(true);  // Invalidate on updates
            nearCacheConfig.setEvictionConfig(new EvictionConfig()
                .setEvictionPolicy(EvictionPolicy.LFU)
                .setSize(5000));

            mapConfig.setNearCacheConfig(nearCacheConfig);

            // Usage
            IMap<String, Product> products = hazelcastInstance.getMap("products");

            // First get: from cluster
            Product p1 = products.get("product1");

            // Subsequent gets: from local near cache (much faster)
            Product p2 = products.get("product1");
            Product p3 = products.get("product1");
            """);

        System.out.println("3. Entry Processor (atomic updates):");
        System.out.println("""
            import com.hazelcast.map.EntryProcessor;

            // Atomic counter increment
            IMap<String, Integer> counters = hazelcastInstance.getMap("counters");

            counters.executeOnKey("page-views", new EntryProcessor<String, Integer, Integer>() {
                @Override
                public Integer process(Map.Entry<String, Integer> entry) {
                    Integer current = entry.getValue();
                    if (current == null) {
                        current = 0;
                    }
                    entry.setValue(current + 1);
                    return current + 1;
                }
            });

            // Or using lambda
            counters.executeOnKey("page-views", entry -> {
                Integer value = entry.getValue() == null ? 0 : entry.getValue();
                entry.setValue(value + 1);
                return value + 1;
            });
            """);

        System.out.println("4. Query with predicates:");
        System.out.println("""
            import com.hazelcast.query.Predicates;

            IMap<String, User> users = hazelcastInstance.getMap("users");

            // Find users older than 25
            Collection<User> adults = users.values(
                Predicates.greaterThan("age", 25)
            );

            // Compound predicates
            Collection<User> result = users.values(
                Predicates.and(
                    Predicates.greaterEqual("age", 18),
                    Predicates.lessThan("age", 65),
                    Predicates.equal("active", true)
                )
            );

            // SQL-like query
            Collection<User> sqlResult = users.values(
                Predicates.sql("age > 25 AND active = true")
            );

            // Index for better performance
            users.addIndex(IndexType.SORTED, "age");
            users.addIndex(IndexType.HASH, "active");
            """);

        System.out.println();
    }

    // ========== Clustering ==========

    private static void clusteringDemo() {
        System.out.println("--- Clustering & Partitioning ---");
        System.out.println("Automatic data distribution and fault tolerance\n");

        System.out.println("1. Cluster configuration:");
        System.out.println("""
            # application.properties

            # Cluster name
            spring.hazelcast.config=classpath:hazelcast.xml

            # Or programmatic
            @Bean
            public Config hazelcastConfig() {
                Config config = new Config();
                config.setClusterName("prod-cluster");

                // Network configuration
                NetworkConfig networkConfig = config.getNetworkConfig();
                networkConfig.setPort(5701);
                networkConfig.setPortAutoIncrement(true);

                // Join configuration
                JoinConfig joinConfig = networkConfig.getJoin();

                // Multicast (default, for local dev)
                joinConfig.getMulticastConfig()
                    .setEnabled(true)
                    .setMulticastGroup("224.2.2.3")
                    .setMulticastPort(54327);

                // TCP/IP (for production)
                joinConfig.getTcpIpConfig()
                    .setEnabled(false)
                    .addMember("10.0.0.1")
                    .addMember("10.0.0.2")
                    .addMember("10.0.0.3");

                // AWS, Azure, GCP discovery plugins available

                return config;
            }
            """);

        System.out.println("2. Cluster members:");
        System.out.println("""
            import com.hazelcast.cluster.Member;
            import com.hazelcast.cluster.MembershipEvent;
            import com.hazelcast.cluster.MembershipListener;

            @Service
            public class ClusterMonitor {

                @Autowired
                private HazelcastInstance hazelcastInstance;

                @PostConstruct
                public void init() {
                    Cluster cluster = hazelcastInstance.getCluster();

                    // Get all members
                    Set<Member> members = cluster.getMembers();
                    System.out.println("Cluster has " + members.size() + " members");

                    // Get local member
                    Member localMember = cluster.getLocalMember();
                    System.out.println("Local: " + localMember.getAddress());

                    // Listen for membership changes
                    cluster.addMembershipListener(new MembershipListener() {
                        @Override
                        public void memberAdded(MembershipEvent event) {
                            System.out.println("Member added: " + event.getMember());
                        }

                        @Override
                        public void memberRemoved(MembershipEvent event) {
                            System.out.println("Member removed: " + event.getMember());
                        }
                    });
                }
            }
            """);

        System.out.println("3. Partition awareness:");
        System.out.println("""
            import com.hazelcast.partition.Partition;
            import com.hazelcast.partition.PartitionService;

            PartitionService partitionService = hazelcastInstance.getPartitionService();

            // Get partition for a key
            String key = "user123";
            Partition partition = partitionService.getPartition(key);

            System.out.println("Key '" + key + "' is in partition: " +
                partition.getPartitionId());
            System.out.println("Owner: " + partition.getOwner());

            // Partition-aware operations
            IMap<String, User> users = hazelcastInstance.getMap("users");
            users.executeOnKey(key, entry -> {
                // Runs on the member that owns this partition
                // No network overhead for data access
                return entry.getValue();
            });
            """);

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced Features ---");
        System.out.println("Event listeners, lifecycle, and best practices\n");

        System.out.println("1. Entry listeners:");
        System.out.println("""
            import com.hazelcast.core.EntryEvent;
            import com.hazelcast.map.listener.*;

            IMap<String, User> users = hazelcastInstance.getMap("users");

            users.addEntryListener(new EntryAddedListener<String, User>() {
                @Override
                public void entryAdded(EntryEvent<String, User> event) {
                    System.out.println("User added: " + event.getValue());
                }
            }, true);  // includeValue=true

            users.addEntryListener(new EntryUpdatedListener<String, User>() {
                @Override
                public void entryUpdated(EntryEvent<String, User> event) {
                    System.out.println("User updated: " +
                        event.getOldValue() + " -> " + event.getValue());
                }
            }, true);

            users.addEntryListener(new EntryRemovedListener<String, User>() {
                @Override
                public void entryRemoved(EntryEvent<String, User> event) {
                    System.out.println("User removed: " + event.getOldValue());
                }
            }, true);

            // Filtered listener
            users.addEntryListener(
                new EntryAddedListener<String, User>() {
                    @Override
                    public void entryAdded(EntryEvent<String, User> event) {
                        System.out.println("VIP user added: " + event.getValue());
                    }
                },
                Predicates.equal("vip", true),
                true
            );
            """);

        System.out.println("2. Lifecycle listener:");
        System.out.println("""
            import com.hazelcast.core.LifecycleEvent;
            import com.hazelcast.core.LifecycleListener;

            hazelcastInstance.getLifecycleService().addLifecycleListener(
                new LifecycleListener() {
                    @Override
                    public void stateChanged(LifecycleEvent event) {
                        switch (event.getState()) {
                            case STARTING:
                                System.out.println("Hazelcast starting...");
                                break;
                            case STARTED:
                                System.out.println("Hazelcast started!");
                                break;
                            case SHUTTING_DOWN:
                                System.out.println("Hazelcast shutting down...");
                                break;
                            case SHUTDOWN:
                                System.out.println("Hazelcast shutdown complete");
                                break;
                            case MERGING:
                                System.out.println("Split-brain merging...");
                                break;
                            case MERGED:
                                System.out.println("Split-brain merged");
                                break;
                        }
                    }
                }
            );
            """);

        System.out.println("3. CP Subsystem (strong consistency):");
        System.out.println("""
            // Configure CP Subsystem
            Config config = new Config();
            CPSubsystemConfig cpConfig = config.getCPSubsystemConfig();
            cpConfig.setCPMemberCount(3);  // Minimum 3 for Raft consensus
            cpConfig.setSessionTimeToLiveSeconds(300);

            // Use CP data structures for strong consistency
            FencedLock cpLock = hazelcastInstance.getCPSubsystem()
                .getLock("critical-lock");

            ISemaphore cpSemaphore = hazelcastInstance.getCPSubsystem()
                .getSemaphore("resource-pool");

            IAtomicLong cpCounter = hazelcastInstance.getCPSubsystem()
                .getAtomicLong("global-counter");

            // CP structures guarantee linearizability via Raft
            """);

        System.out.println("4. Spring Boot integration:");
        System.out.println("""
            @Configuration
            @EnableCaching
            public class CacheConfig {

                @Bean
                public HazelcastInstance hazelcastInstance() {
                    Config config = new Config();
                    config.setClusterName("spring-cache-cluster");
                    return Hazelcast.newHazelcastInstance(config);
                }

                @Bean
                public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
                    return new HazelcastCacheManager(hazelcastInstance);
                }
            }

            @Service
            public class UserService {

                @Cacheable(value = "users", key = "#id")
                public User findById(Long id) {
                    // Cached in Hazelcast
                    return userRepository.findById(id);
                }

                @CachePut(value = "users", key = "#user.id")
                public User update(User user) {
                    return userRepository.save(user);
                }

                @CacheEvict(value = "users", key = "#id")
                public void delete(Long id) {
                    userRepository.deleteById(id);
                }
            }
            """);

        System.out.println("5. Best practices:");
        System.out.println("   ✓ Use near cache for read-heavy workloads");
        System.out.println("   ✓ Configure proper backup counts (1-2 sync)");
        System.out.println("   ✓ Set TTL and eviction policies");
        System.out.println("   ✓ Add indexes for queried fields");
        System.out.println("   ✓ Use Entry Processors for atomic updates");
        System.out.println("   ✓ Monitor cluster health and partition distribution");
        System.out.println("   ✓ Use CP Subsystem for critical synchronization");
        System.out.println("   ✓ Proper network configuration for production");
        System.out.println("   ✗ Don't use multicast in production");
        System.out.println("   ✗ Don't store large objects (>1MB) in IMap");
        System.out.println("   ✗ Don't iterate large maps without predicates");

        System.out.println("\n6. Monitoring:");
        System.out.println("""
            // Management Center (web UI)
            config.getManagementCenterConfig()
                .setScriptingEnabled(true);

            // Metrics
            IMap<String, User> users = hazelcastInstance.getMap("users");
            LocalMapStats stats = users.getLocalMapStats();

            System.out.println("Hits: " + stats.getHits());
            System.out.println("Get operations: " + stats.getGetOperationCount());
            System.out.println("Put operations: " + stats.getPutOperationCount());
            System.out.println("Memory cost: " + stats.getOwnedEntryMemoryCost());
            System.out.println("Backup memory: " + stats.getBackupEntryMemoryCost());
            """);

        System.out.println();
    }
}
