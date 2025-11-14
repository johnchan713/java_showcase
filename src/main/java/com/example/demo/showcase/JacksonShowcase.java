package com.example.demo.showcase;

/**
 * Demonstrates Jackson JSON processing library
 * Covers ObjectMapper, serialization, deserialization, annotations, and customization
 */
public class JacksonShowcase {

    public static void demonstrate() {
        System.out.println("\n========== JACKSON JSON BINDING SHOWCASE ==========\n");

        jacksonOverviewDemo();
        objectMapperDemo();
        annotationsDemo();
        serializationDemo();
        deserializationDemo();
        customizationDemo();
        advancedFeaturesDemo();
    }

    // ========== Overview ==========

    private static void jacksonOverviewDemo() {
        System.out.println("--- Jackson Overview ---");
        System.out.println("High-performance JSON processor for Java\n");

        System.out.println("1. Core modules:");
        System.out.println("   jackson-core: Low-level streaming API");
        System.out.println("   jackson-databind: Data binding (Object<->JSON)");
        System.out.println("   jackson-annotations: Annotations for customization");

        System.out.println("\n2. Dependencies (included in Spring Boot):");
        System.out.println("""
            <dependency>
                <groupId>com.fasterxml.jackson.core</groupId>
                <artifactId>jackson-databind</artifactId>
            </dependency>

            <!-- For Java 8 date/time support -->
            <dependency>
                <groupId>com.fasterxml.jackson.datatype</groupId>
                <artifactId>jackson-datatype-jsr310</artifactId>
            </dependency>
            """);

        System.out.println();
    }

    // ========== ObjectMapper ==========

    private static void objectMapperDemo() {
        System.out.println("--- ObjectMapper ---");
        System.out.println("Main class for JSON operations\n");

        System.out.println("1. Basic usage:");
        System.out.println("""
            import com.fasterxml.jackson.databind.ObjectMapper;

            // Create ObjectMapper (thread-safe, reuse)
            ObjectMapper mapper = new ObjectMapper();

            // Object to JSON string
            User user = new User("John", 30);
            String json = mapper.writeValueAsString(user);
            // {"name":"John","age":30}

            // Object to JSON bytes
            byte[] jsonBytes = mapper.writeValueAsBytes(user);

            // Object to file
            mapper.writeValue(new File("user.json"), user);

            // JSON to Object
            String json = "{\\"name\\":\\"John\\",\\"age\\":30}";
            User user = mapper.readValue(json, User.class);

            // JSON file to Object
            User user = mapper.readValue(new File("user.json"), User.class);

            // JSON bytes to Object
            User user = mapper.readValue(jsonBytes, User.class);
            """);

        System.out.println("2. Configuration:");
        System.out.println("""
            ObjectMapper mapper = new ObjectMapper();

            // Ignore unknown properties
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Pretty print
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Include non-null fields only
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            // Include non-empty fields only
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

            // Date format
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            // Java 8 date/time support
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            """);

        System.out.println("3. Spring Boot configuration:");
        System.out.println("""
            # application.properties
            spring.jackson.serialization.indent-output=true
            spring.jackson.serialization.write-dates-as-timestamps=false
            spring.jackson.deserialization.fail-on-unknown-properties=false
            spring.jackson.default-property-inclusion=non_null

            # Java configuration
            @Configuration
            public class JacksonConfig {

                @Bean
                public ObjectMapper objectMapper() {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.registerModule(new JavaTimeModule());
                    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    return mapper;
                }
            }
            """);

        System.out.println();
    }

    // ========== Annotations ==========

    private static void annotationsDemo() {
        System.out.println("--- Jackson Annotations ---");
        System.out.println("Customize JSON mapping with annotations\n");

        System.out.println("1. Basic annotations:");
        System.out.println("""
            public class User {

                @JsonProperty("user_name")  // Custom property name
                private String name;

                @JsonIgnore  // Exclude from JSON
                private String password;

                @JsonInclude(JsonInclude.Include.NON_NULL)  // Only if not null
                private String email;

                @JsonFormat(pattern = "yyyy-MM-dd")  // Date format
                private LocalDate birthDate;

                // Getters/setters
            }

            // JSON output:
            {
              "user_name": "John",
              "birthDate": "1990-01-15"
            }
            """);

        System.out.println("2. Constructor annotations:");
        System.out.println("""
            public class User {
                private String name;
                private int age;

                @JsonCreator  // Constructor for deserialization
                public User(
                    @JsonProperty("name") String name,
                    @JsonProperty("age") int age
                ) {
                    this.name = name;
                    this.age = age;
                }
            }

            // Alternative: Builder pattern
            @JsonDeserialize(builder = User.Builder.class)
            public class User {
                private String name;
                private int age;

                @JsonPOJOBuilder(withPrefix = "")
                public static class Builder {
                    public Builder name(String name) { ... }
                    public Builder age(int age) { ... }
                    public User build() { ... }
                }
            }
            """);

        System.out.println("3. Property access:");
        System.out.println("""
            public class User {

                @JsonProperty(access = JsonProperty.Access.READ_ONLY)
                private String id;  // Serialize but not deserialize

                @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
                private String password;  // Deserialize but not serialize

                @JsonGetter("fullName")  // Custom getter
                public String getFullName() {
                    return firstName + " " + lastName;
                }

                @JsonSetter("fullName")  // Custom setter
                public void setFullName(String fullName) {
                    String[] parts = fullName.split(" ");
                    firstName = parts[0];
                    lastName = parts[1];
                }
            }
            """);

        System.out.println("4. Polymorphism:");
        System.out.println("""
            @JsonTypeInfo(
                use = JsonTypeInfo.Id.NAME,
                include = JsonTypeInfo.As.PROPERTY,
                property = "type"
            )
            @JsonSubTypes({
                @JsonSubTypes.Type(value = Dog.class, name = "dog"),
                @JsonSubTypes.Type(value = Cat.class, name = "cat")
            })
            public abstract class Animal {
                private String name;
            }

            public class Dog extends Animal {
                private String breed;
            }

            // JSON output:
            {
              "type": "dog",
              "name": "Buddy",
              "breed": "Golden Retriever"
            }
            """);

        System.out.println("5. Unwrapping:");
        System.out.println("""
            public class User {
                private String name;

                @JsonUnwrapped  // Flatten nested object
                private Address address;
            }

            public class Address {
                private String street;
                private String city;
            }

            // JSON output (address fields unwrapped):
            {
              "name": "John",
              "street": "123 Main St",
              "city": "New York"
            }
            """);

        System.out.println("6. Alias:");
        System.out.println("""
            public class User {

                @JsonAlias({"userName", "user_name", "username"})
                private String name;  // Accept any of these names in JSON
            }
            """);

        System.out.println();
    }

    // ========== Serialization ==========

    private static void serializationDemo() {
        System.out.println("--- Serialization (Object to JSON) ---");
        System.out.println("Convert Java objects to JSON\n");

        System.out.println("1. Collections:");
        System.out.println("""
            ObjectMapper mapper = new ObjectMapper();

            // List to JSON
            List<User> users = Arrays.asList(
                new User("John", 30),
                new User("Jane", 25)
            );
            String json = mapper.writeValueAsString(users);
            // [{"name":"John","age":30},{"name":"Jane","age":25}]

            // Map to JSON
            Map<String, Object> map = new HashMap<>();
            map.put("name", "John");
            map.put("age", 30);
            String json = mapper.writeValueAsString(map);
            // {"name":"John","age":30}
            """);

        System.out.println("2. Custom serializer:");
        System.out.println("""
            public class User {
                private String name;

                @JsonSerialize(using = CustomDateSerializer.class)
                private LocalDate birthDate;
            }

            public class CustomDateSerializer extends JsonSerializer<LocalDate> {

                @Override
                public void serialize(LocalDate value, JsonGenerator gen,
                                     SerializerProvider serializers)
                        throws IOException {
                    gen.writeString(value.format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    ));
                }
            }

            // JSON output:
            {
              "name": "John",
              "birthDate": "15/01/1990"
            }
            """);

        System.out.println("3. JsonNode (tree model):");
        System.out.println("""
            ObjectMapper mapper = new ObjectMapper();

            // Create JSON tree
            ObjectNode root = mapper.createObjectNode();
            root.put("name", "John");
            root.put("age", 30);

            ArrayNode hobbies = root.putArray("hobbies");
            hobbies.add("reading");
            hobbies.add("coding");

            String json = mapper.writeValueAsString(root);

            // JSON output:
            {
              "name": "John",
              "age": 30,
              "hobbies": ["reading", "coding"]
            }
            """);

        System.out.println("4. Streaming API:");
        System.out.println("""
            // For large JSON (memory efficient)
            JsonFactory factory = new JsonFactory();
            JsonGenerator generator = factory.createGenerator(
                new FileWriter("output.json")
            );

            generator.writeStartObject();
            generator.writeStringField("name", "John");
            generator.writeNumberField("age", 30);
            generator.writeEndObject();

            generator.close();
            """);

        System.out.println();
    }

    // ========== Deserialization ==========

    private static void deserializationDemo() {
        System.out.println("--- Deserialization (JSON to Object) ---");
        System.out.println("Convert JSON to Java objects\n");

        System.out.println("1. Generic types:");
        System.out.println("""
            ObjectMapper mapper = new ObjectMapper();

            // JSON array to List
            String json = "[{\\"name\\":\\"John\\"},{\\"name\\":\\"Jane\\"}]";

            List<User> users = mapper.readValue(json,
                new TypeReference<List<User>>() {}
            );

            // JSON to Map
            String json = "{\\"name\\":\\"John\\",\\"age\\":30}";

            Map<String, Object> map = mapper.readValue(json,
                new TypeReference<Map<String, Object>>() {}
            );

            // Nested generics
            String json = "{\\"users\\":[...],\\"count\\":2}";

            Response<List<User>> response = mapper.readValue(json,
                new TypeReference<Response<List<User>>>() {}
            );
            """);

        System.out.println("2. Custom deserializer:");
        System.out.println("""
            public class User {
                private String name;

                @JsonDeserialize(using = CustomDateDeserializer.class)
                private LocalDate birthDate;
            }

            public class CustomDateDeserializer extends JsonDeserializer<LocalDate> {

                @Override
                public LocalDate deserialize(JsonParser p, DeserializationContext ctxt)
                        throws IOException {
                    String date = p.getText();
                    return LocalDate.parse(date,
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    );
                }
            }
            """);

        System.out.println("3. JsonNode parsing:");
        System.out.println("""
            String json = "{\\"name\\":\\"John\\",\\"age\\":30}";

            JsonNode root = mapper.readTree(json);

            String name = root.get("name").asText();
            int age = root.get("age").asInt();

            // Check if field exists
            if (root.has("email")) {
                String email = root.get("email").asText();
            }

            // Iterate array
            JsonNode hobbies = root.get("hobbies");
            if (hobbies.isArray()) {
                for (JsonNode hobby : hobbies) {
                    System.out.println(hobby.asText());
                }
            }
            """);

        System.out.println("4. Streaming API:");
        System.out.println("""
            // For large JSON (memory efficient)
            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createParser(new File("input.json"));

            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = parser.getCurrentName();

                if ("name".equals(fieldName)) {
                    parser.nextToken();
                    String name = parser.getText();
                }
            }

            parser.close();
            """);

        System.out.println();
    }

    // ========== Customization ==========

    private static void customizationDemo() {
        System.out.println("--- Customization ---");
        System.out.println("Advanced Jackson configuration\n");

        System.out.println("1. Naming strategies:");
        System.out.println("""
            ObjectMapper mapper = new ObjectMapper();

            // Snake case (user_name)
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

            // Kebab case (user-name)
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);

            // Upper camel case (UserName)
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);

            // Lower case (username)
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CASE);

            // Or use annotation
            @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
            public class User {
                private String firstName;  // → first_name in JSON
            }
            """);

        System.out.println("2. Visibility:");
        System.out.println("""
            ObjectMapper mapper = new ObjectMapper();

            // Use fields instead of getters/setters
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);

            // Or use annotation
            @JsonAutoDetect(
                fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE
            )
            public class User {
                private String name;  // Serialized even without getter
            }
            """);

        System.out.println("3. Mixins:");
        System.out.println("""
            // Can't modify third-party class? Use mixin
            public abstract class UserMixin {
                @JsonIgnore
                abstract String getPassword();

                @JsonProperty("user_name")
                abstract String getName();
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.addMixIn(User.class, UserMixin.class);
            """);

        System.out.println("4. Views:");
        System.out.println("""
            public class Views {
                public static class Public {}
                public static class Internal extends Public {}
            }

            public class User {
                @JsonView(Views.Public.class)
                private String name;

                @JsonView(Views.Internal.class)
                private String email;

                @JsonView(Views.Internal.class)
                private String password;
            }

            // Serialize with view
            ObjectMapper mapper = new ObjectMapper();
            String publicJson = mapper
                .writerWithView(Views.Public.class)
                .writeValueAsString(user);
            // Only includes "name"

            String internalJson = mapper
                .writerWithView(Views.Internal.class)
                .writeValueAsString(user);
            // Includes all fields (Internal extends Public)
            """);

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced Features ---");
        System.out.println("Complex scenarios and best practices\n");

        System.out.println("1. Handle null and default values:");
        System.out.println("""
            @JsonInclude(JsonInclude.Include.NON_NULL)  // Class level
            public class User {

                @JsonInclude(JsonInclude.Include.NON_EMPTY)  // Field level
                private List<String> tags;

                @JsonProperty(defaultValue = "Unknown")
                private String name;

                @JsonSetter(nulls = Nulls.SKIP)  // Keep existing value if null
                private String email;
            }
            """);

        System.out.println("2. Merge and update:");
        System.out.println("""
            ObjectMapper mapper = new ObjectMapper();

            User existingUser = new User("John", 30);
            String updateJson = "{\\"age\\":31,\\"email\\":\\"john@example.com\\"}";

            // Merge JSON into existing object
            mapper.readerForUpdating(existingUser)
                .readValue(updateJson);

            // existingUser now has: name="John", age=31, email="john@example.com"
            """);

        System.out.println("3. Polymorphic deserialization:");
        System.out.println("""
            // Deserialize based on discriminator field
            @JsonTypeInfo(
                use = JsonTypeInfo.Id.NAME,
                property = "@type"
            )
            @JsonSubTypes({
                @JsonSubTypes.Type(value = EmailNotification.class, name = "email"),
                @JsonSubTypes.Type(value = SmsNotification.class, name = "sms")
            })
            public interface Notification {
                void send();
            }

            // JSON:
            {
              "@type": "email",
              "recipient": "user@example.com",
              "subject": "Hello"
            }
            """);

        System.out.println("4. Performance tips:");
        System.out.println("   ✓ Reuse ObjectMapper (thread-safe)");
        System.out.println("   ✓ Use streaming API for large JSON");
        System.out.println("   ✓ Disable features you don't need");
        System.out.println("   ✓ Use @JsonProperty on fields (avoid reflection)");
        System.out.println("   ✓ Pre-create TypeReference for generics");
        System.out.println("   ✓ Use byte[] instead of String when possible");

        System.out.println("\n5. Common issues:");
        System.out.println("   ✗ Don't create new ObjectMapper per request");
        System.out.println("   ✗ Don't ignore unknown properties in prod (security)");
        System.out.println("   ✗ Don't serialize sensitive data (use @JsonIgnore)");
        System.out.println("   ✗ Don't forget to handle circular references");

        System.out.println("\n6. Circular references:");
        System.out.println("""
            public class User {
                private String name;

                @JsonManagedReference  // Forward reference
                private List<Order> orders;
            }

            public class Order {
                private String id;

                @JsonBackReference  // Back reference (not serialized)
                private User user;
            }

            // Alternative: Use @JsonIdentityInfo
            @JsonIdentityInfo(
                generator = ObjectIdGenerators.PropertyGenerator.class,
                property = "id"
            )
            public class User {
                private String id;
                private List<Order> orders;
            }
            """);

        System.out.println();
    }
}
