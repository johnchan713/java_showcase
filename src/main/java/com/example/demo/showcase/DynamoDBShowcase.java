package com.example.demo.showcase;

/**
 * Demonstrates AWS DynamoDB with AWS SDK v2
 * Covers DynamoDB operations, DynamoDBMapper, Enhanced Client, and best practices
 *
 * Note: This is a demonstration - requires AWS credentials and DynamoDB access
 */
public class DynamoDBShowcase {

    public static void demonstrate() {
        System.out.println("\n========== AWS DYNAMODB SHOWCASE ==========\n");

        dynamoDbOverviewDemo();
        basicOperationsDemo();
        enhancedClientDemo();
        queriesDemo();
        advancedFeaturesDemo();
    }

    // ========== Overview ==========

    private static void dynamoDbOverviewDemo() {
        System.out.println("--- DynamoDB Overview ---");
        System.out.println("AWS fully managed NoSQL database\n");

        System.out.println("1. Key concepts:");
        System.out.println("   Table: Collection of items");
        System.out.println("   Item: Collection of attributes (like row)");
        System.out.println("   Partition Key: Primary key (required)");
        System.out.println("   Sort Key: Optional secondary key for sorting");
        System.out.println("   GSI: Global Secondary Index");
        System.out.println("   LSI: Local Secondary Index");

        System.out.println("\n2. Dependencies (AWS SDK v2):");
        System.out.println("""
            <dependency>
                <groupId>software.amazon.awssdk</groupId>
                <artifactId>dynamodb</artifactId>
                <version>2.20.26</version>
            </dependency>

            <dependency>
                <groupId>software.amazon.awssdk</groupId>
                <artifactId>dynamodb-enhanced</artifactId>
                <version>2.20.26</version>
            </dependency>

            # application.properties
            aws.region=us-east-1
            aws.accessKeyId=YOUR_ACCESS_KEY
            aws.secretAccessKey=YOUR_SECRET_KEY

            # Or use IAM roles (recommended)
            """);

        System.out.println("\n3. Configuration:");
        System.out.println("""
            @Configuration
            public class DynamoDbConfig {

                @Value("${aws.region}")
                private String region;

                @Bean
                public DynamoDbClient dynamoDbClient() {
                    return DynamoDbClient.builder()
                        .region(Region.of(region))
                        .build();
                }

                @Bean
                public DynamoDbEnhancedClient dynamoDbEnhancedClient(
                        DynamoDbClient dynamoDbClient) {
                    return DynamoDbEnhancedClient.builder()
                        .dynamoDbClient(dynamoDbClient)
                        .build();
                }
            }
            """);

        System.out.println();
    }

    // ========== Basic Operations ==========

    private static void basicOperationsDemo() {
        System.out.println("--- Basic DynamoDB Operations ---");
        System.out.println("CRUD operations with low-level client\n");

        System.out.println("1. PutItem:");
        System.out.println("""
            @Service
            public class UserService {

                @Autowired
                private DynamoDbClient dynamoDbClient;

                public void createUser(String userId, String name, int age) {
                    Map<String, AttributeValue> item = new HashMap<>();
                    item.put("userId", AttributeValue.builder().s(userId).build());
                    item.put("name", AttributeValue.builder().s(name).build());
                    item.put("age", AttributeValue.builder().n(String.valueOf(age)).build());

                    PutItemRequest request = PutItemRequest.builder()
                        .tableName("Users")
                        .item(item)
                        .build();

                    dynamoDbClient.putItem(request);
                }
            }
            """);

        System.out.println("2. GetItem:");
        System.out.println("""
            public Map<String, AttributeValue> getUser(String userId) {
                Map<String, AttributeValue> key = new HashMap<>();
                key.put("userId", AttributeValue.builder().s(userId).build());

                GetItemRequest request = GetItemRequest.builder()
                    .tableName("Users")
                    .key(key)
                    .build();

                GetItemResponse response = dynamoDbClient.getItem(request);

                return response.item();
            }
            """);

        System.out.println("3. UpdateItem:");
        System.out.println("""
            public void updateUserAge(String userId, int newAge) {
                Map<String, AttributeValue> key = new HashMap<>();
                key.put("userId", AttributeValue.builder().s(userId).build());

                Map<String, AttributeValueUpdate> updates = new HashMap<>();
                updates.put("age", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().n(String.valueOf(newAge)).build())
                    .action(AttributeAction.PUT)
                    .build());

                UpdateItemRequest request = UpdateItemRequest.builder()
                    .tableName("Users")
                    .key(key)
                    .attributeUpdates(updates)
                    .build();

                dynamoDbClient.updateItem(request);
            }

            // With update expression (preferred)
            public void updateUser(String userId, String name, int age) {
                Map<String, AttributeValue> key = new HashMap<>();
                key.put("userId", AttributeValue.builder().s(userId).build());

                Map<String, AttributeValue> values = new HashMap<>();
                values.put(":name", AttributeValue.builder().s(name).build());
                values.put(":age", AttributeValue.builder().n(String.valueOf(age)).build());

                UpdateItemRequest request = UpdateItemRequest.builder()
                    .tableName("Users")
                    .key(key)
                    .updateExpression("SET #n = :name, age = :age")
                    .expressionAttributeNames(Map.of("#n", "name"))
                    .expressionAttributeValues(values)
                    .build();

                dynamoDbClient.updateItem(request);
            }
            """);

        System.out.println("4. DeleteItem:");
        System.out.println("""
            public void deleteUser(String userId) {
                Map<String, AttributeValue> key = new HashMap<>();
                key.put("userId", AttributeValue.builder().s(userId).build());

                DeleteItemRequest request = DeleteItemRequest.builder()
                    .tableName("Users")
                    .key(key)
                    .build();

                dynamoDbClient.deleteItem(request);
            }
            """);

        System.out.println();
    }

    // ========== Enhanced Client ==========

    private static void enhancedClientDemo() {
        System.out.println("--- DynamoDB Enhanced Client ---");
        System.out.println("Object mapping with annotations\n");

        System.out.println("1. Define entity:");
        System.out.println("""
            import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

            @DynamoDbBean
            public class User {

                private String userId;
                private String name;
                private int age;
                private String email;
                private LocalDateTime createdAt;

                @DynamoDbPartitionKey
                public String getUserId() {
                    return userId;
                }

                @DynamoDbAttribute("name")
                public String getName() {
                    return name;
                }

                @DynamoDbAttribute("age")
                public int getAge() {
                    return age;
                }

                @DynamoDbSecondaryPartitionKey(indexNames = "EmailIndex")
                public String getEmail() {
                    return email;
                }

                @DynamoDbAttribute("createdAt")
                @DynamoDbConvertedBy(LocalDateTimeConverter.class)
                public LocalDateTime getCreatedAt() {
                    return createdAt;
                }

                // Setters...
            }

            // Custom converter
            public class LocalDateTimeConverter
                    implements AttributeConverter<LocalDateTime> {

                @Override
                public AttributeValue transformFrom(LocalDateTime input) {
                    return AttributeValue.builder()
                        .s(input.toString())
                        .build();
                }

                @Override
                public LocalDateTime transformTo(AttributeValue input) {
                    return LocalDateTime.parse(input.s());
                }
            }
            """);

        System.out.println("2. CRUD operations:");
        System.out.println("""
            @Service
            public class UserService {

                @Autowired
                private DynamoDbEnhancedClient enhancedClient;

                private DynamoDbTable<User> getUserTable() {
                    return enhancedClient.table("Users",
                        TableSchema.fromBean(User.class));
                }

                // Create
                public void createUser(User user) {
                    getUserTable().putItem(user);
                }

                // Read
                public User getUser(String userId) {
                    return getUserTable().getItem(
                        Key.builder().partitionValue(userId).build()
                    );
                }

                // Update
                public void updateUser(User user) {
                    getUserTable().updateItem(user);
                }

                // Delete
                public void deleteUser(String userId) {
                    getUserTable().deleteItem(
                        Key.builder().partitionValue(userId).build()
                    );
                }

                // Conditional update
                public void updateIfExists(User user) {
                    Expression condition = Expression.builder()
                        .expression("attribute_exists(userId)")
                        .build();

                    getUserTable().updateItem(
                        UpdateItemEnhancedRequest.builder(User.class)
                            .item(user)
                            .conditionExpression(condition)
                            .build()
                    );
                }
            }
            """);

        System.out.println("3. Batch operations:");
        System.out.println("""
            public void batchWrite(List<User> users) {
                DynamoDbTable<User> table = getUserTable();

                WriteBatch.Builder<User> batchBuilder = WriteBatch.builder(User.class)
                    .mappedTableResource(table);

                for (User user : users) {
                    batchBuilder.addPutItem(user);
                }

                BatchWriteItemEnhancedRequest request =
                    BatchWriteItemEnhancedRequest.builder()
                        .writeBatches(batchBuilder.build())
                        .build();

                enhancedClient.batchWriteItem(request);
            }

            public List<User> batchGet(List<String> userIds) {
                DynamoDbTable<User> table = getUserTable();

                ReadBatch.Builder<User> batchBuilder = ReadBatch.builder(User.class)
                    .mappedTableResource(table);

                for (String userId : userIds) {
                    batchBuilder.addGetItem(
                        Key.builder().partitionValue(userId).build()
                    );
                }

                BatchGetItemEnhancedRequest request =
                    BatchGetItemEnhancedRequest.builder()
                        .readBatches(batchBuilder.build())
                        .build();

                return enhancedClient.batchGetItem(request)
                    .resultsForTable(table)
                    .stream()
                    .collect(Collectors.toList());
            }
            """);

        System.out.println();
    }

    // ========== Queries and Scans ==========

    private static void queriesDemo() {
        System.out.println("--- Queries and Scans ---");
        System.out.println("Retrieve multiple items\n");

        System.out.println("1. Query (uses partition key):");
        System.out.println("""
            public List<User> getUsersByPartitionKey(String userId) {
                DynamoDbTable<User> table = getUserTable();

                QueryConditional condition = QueryConditional
                    .keyEqualTo(Key.builder().partitionValue(userId).build());

                return table.query(condition)
                    .items()
                    .stream()
                    .collect(Collectors.toList());
            }

            // Query with sort key range
            public List<Order> getOrdersByDateRange(
                    String userId,
                    LocalDate startDate,
                    LocalDate endDate) {

                QueryConditional condition = QueryConditional
                    .sortBetween(
                        Key.builder()
                            .partitionValue(userId)
                            .sortValue(startDate.toString())
                            .build(),
                        Key.builder()
                            .partitionValue(userId)
                            .sortValue(endDate.toString())
                            .build()
                    );

                return getOrderTable().query(condition)
                    .items()
                    .stream()
                    .collect(Collectors.toList());
            }
            """);

        System.out.println("2. Query with filter:");
        System.out.println("""
            public List<User> getUsersOver18(String userId) {
                Expression filterExpression = Expression.builder()
                    .expression("age > :minAge")
                    .expressionValues(Map.of(
                        ":minAge", AttributeValue.builder().n("18").build()
                    ))
                    .build();

                QueryConditional condition = QueryConditional
                    .keyEqualTo(Key.builder().partitionValue(userId).build());

                return getUserTable().query(
                    QueryEnhancedRequest.builder()
                        .queryConditional(condition)
                        .filterExpression(filterExpression)
                        .build()
                ).items().stream().collect(Collectors.toList());
            }
            """);

        System.out.println("3. Scan (full table scan - expensive!):");
        System.out.println("""
            public List<User> scanAllUsers() {
                return getUserTable().scan()
                    .items()
                    .stream()
                    .collect(Collectors.toList());
            }

            // Scan with filter
            public List<User> scanActiveUsers() {
                Expression filterExpression = Expression.builder()
                    .expression("active = :active")
                    .expressionValues(Map.of(
                        ":active", AttributeValue.builder().bool(true).build()
                    ))
                    .build();

                return getUserTable().scan(
                    ScanEnhancedRequest.builder()
                        .filterExpression(filterExpression)
                        .build()
                ).items().stream().collect(Collectors.toList());
            }
            """);

        System.out.println("4. Pagination:");
        System.out.println("""
            public Page<User> getUsersPage(String lastEvaluatedKey, int limit) {
                QueryConditional condition = QueryConditional
                    .keyEqualTo(Key.builder().partitionValue("someId").build());

                QueryEnhancedRequest.Builder requestBuilder =
                    QueryEnhancedRequest.builder()
                        .queryConditional(condition)
                        .limit(limit);

                if (lastEvaluatedKey != null) {
                    requestBuilder.exclusiveStartKey(
                        Map.of("userId",
                            AttributeValue.builder().s(lastEvaluatedKey).build())
                    );
                }

                PageIterable<User> pages = getUserTable().query(requestBuilder.build());

                // Get first page
                Page<User> firstPage = pages.iterator().next();

                return firstPage;
            }
            """);

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced DynamoDB Features ---");
        System.out.println("Transactions, TTL, streams, and best practices\n");

        System.out.println("1. Transactions:");
        System.out.println("""
            public void transferFunds(String fromUserId, String toUserId, int amount) {
                DynamoDbTable<Account> accountTable = getAccountTable();

                TransactWriteItemsEnhancedRequest request =
                    TransactWriteItemsEnhancedRequest.builder()
                        .addUpdateItem(accountTable, TransactUpdateItemEnhancedRequest.builder(Account.class)
                            .item(getAccount(fromUserId).withBalance(-amount))
                            .build())
                        .addUpdateItem(accountTable, TransactUpdateItemEnhancedRequest.builder(Account.class)
                            .item(getAccount(toUserId).withBalance(amount))
                            .build())
                        .build();

                enhancedClient.transactWriteItems(request);
                // Both succeed or both fail
            }
            """);

        System.out.println("2. Conditional writes:");
        System.out.println("""
            public void updateIfPriceUnchanged(Product product, double expectedPrice) {
                Expression condition = Expression.builder()
                    .expression("price = :expectedPrice")
                    .expressionValues(Map.of(
                        ":expectedPrice",
                        AttributeValue.builder().n(String.valueOf(expectedPrice)).build()
                    ))
                    .build();

                try {
                    getProductTable().updateItem(
                        UpdateItemEnhancedRequest.builder(Product.class)
                            .item(product)
                            .conditionExpression(condition)
                            .build()
                    );
                } catch (ConditionalCheckFailedException e) {
                    // Price changed, handle accordingly
                }
            }
            """);

        System.out.println("3. TTL (Time To Live):");
        System.out.println("""
            @DynamoDbBean
            public class Session {

                @DynamoDbPartitionKey
                private String sessionId;

                @DynamoDbAttribute("ttl")
                private Long ttl;  // Unix timestamp

                // Constructor
                public Session(String sessionId, Duration expiresIn) {
                    this.sessionId = sessionId;
                    this.ttl = Instant.now().plus(expiresIn).getEpochSecond();
                }

                // Getters/setters
            }

            // Enable TTL on table (via AWS Console or CLI)
            // aws dynamodb update-time-to-live --table-name Sessions \\
            //   --time-to-live-specification "Enabled=true, AttributeName=ttl"
            """);

        System.out.println("4. Global Secondary Index:");
        System.out.println("""
            @DynamoDbBean
            public class User {

                @DynamoDbPartitionKey
                private String userId;

                @DynamoDbSecondaryPartitionKey(indexNames = "EmailIndex")
                private String email;

                // Other fields...
            }

            // Query using GSI
            public User findByEmail(String email) {
                DynamoDbIndex<User> emailIndex = getUserTable()
                    .index("EmailIndex");

                QueryConditional condition = QueryConditional
                    .keyEqualTo(Key.builder().partitionValue(email).build());

                return emailIndex.query(condition)
                    .items()
                    .stream()
                    .findFirst()
                    .orElse(null);
            }
            """);

        System.out.println("5. Best practices:");
        System.out.println("   ✓ Use partition key for even distribution");
        System.out.println("   ✓ Use Query instead of Scan when possible");
        System.out.println("   ✓ Design tables for access patterns");
        System.out.println("   ✓ Use GSI for alternate query patterns");
        System.out.println("   ✓ Enable point-in-time recovery");
        System.out.println("   ✓ Use TTL for auto-expiring data");
        System.out.println("   ✓ Batch operations for efficiency");
        System.out.println("   ✗ Don't scan large tables");
        System.out.println("   ✗ Don't use hot partition keys");
        System.out.println("   ✗ Don't store large items (> 400KB)");

        System.out.println("\n6. Capacity modes:");
        System.out.println("   Provisioned:");
        System.out.println("     - Specify RCU/WCU");
        System.out.println("     - Predictable workload");
        System.out.println("     - Lower cost for steady traffic");
        System.out.println("\n   On-Demand:");
        System.out.println("     - Pay per request");
        System.out.println("     - Unpredictable/spiky workload");
        System.out.println("     - No capacity planning");

        System.out.println();
    }
}
