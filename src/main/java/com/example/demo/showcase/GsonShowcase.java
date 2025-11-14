package com.example.demo.showcase;

/**
 * Demonstrates Gson - Google's JSON library
 * Covers serialization, deserialization, custom serializers, and type adapters
 */
public class GsonShowcase {

    public static void demonstrate() {
        System.out.println("\n========== GSON SHOWCASE ==========\n");

        System.out.println("--- Gson Overview ---");
        System.out.println("Google's JSON serialization library\n");

        System.out.println("Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>com.google.code.gson</groupId>
                <artifactId>gson</artifactId>
                <version>2.10.1</version>
            </dependency>
            """);

        System.out.println("\n--- Basic Serialization/Deserialization ---");
        System.out.println("""
            import com.google.gson.Gson;
            import com.google.gson.GsonBuilder;
            
            Gson gson = new Gson();
            
            // Object to JSON
            User user = new User("John", 30);
            String json = gson.toJson(user);
            // {"name":"John","age":30}
            
            // JSON to Object
            User user = gson.fromJson(json, User.class);
            
            // Collections
            List<User> users = Arrays.asList(user1, user2);
            String jsonArray = gson.toJson(users);
            
            // Type for generics
            Type listType = new TypeToken<List<User>>(){}.getType();
            List<User> usersList = gson.fromJson(jsonArray, listType);
            """);

        System.out.println("\n--- Gson Configuration ---");
        System.out.println("""
            Gson gson = new GsonBuilder()
                .setPrettyPrinting()                    // Format output
                .serializeNulls()                       // Include null fields
                .setDateFormat("yyyy-MM-dd HH:mm:ss")  // Date format
                .disableHtmlEscaping()                  // Don't escape HTML
                .excludeFieldsWithoutExposeAnnotation() // Only @Expose fields
                .create();
            """);

        System.out.println("\n--- Annotations ---");
        System.out.println("""
            public class User {
                @SerializedName("user_name")  // Custom JSON field name
                private String name;
                
                @Expose  // Include in serialization (with excludeFieldsWithoutExposeAnnotation)
                private int age;
                
                @Expose(serialize = false, deserialize = true)  // Read-only
                private String password;
                
                private transient String temp;  // Exclude from JSON
            }
            """);

        System.out.println("\n--- Custom Serializer/Deserializer ---");
        System.out.println("""
            // Custom serializer
            class UserSerializer implements JsonSerializer<User> {
                @Override
                public JsonElement serialize(User user, Type type,
                        JsonSerializationContext context) {
                    JsonObject json = new JsonObject();
                    json.addProperty("full_name", user.getName());
                    json.addProperty("age_years", user.getAge());
                    return json;
                }
            }
            
            // Custom deserializer
            class UserDeserializer implements JsonDeserializer<User> {
                @Override
                public User deserialize(JsonElement json, Type type,
                        JsonDeserializationContext context) {
                    JsonObject obj = json.getAsJsonObject();
                    User user = new User();
                    user.setName(obj.get("full_name").getAsString());
                    user.setAge(obj.get("age_years").getAsInt());
                    return user;
                }
            }
            
            // Register
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserSerializer())
                .registerTypeAdapter(User.class, new UserDeserializer())
                .create();
            """);

        System.out.println("\n--- Type Adapter (combines ser/deser) ---");
        System.out.println("""
            class LocalDateAdapter extends TypeAdapter<LocalDate> {
                private final DateTimeFormatter formatter = 
                    DateTimeFormatter.ISO_LOCAL_DATE;
                
                @Override
                public void write(JsonWriter out, LocalDate value) throws IOException {
                    out.value(value != null ? value.format(formatter) : null);
                }
                
                @Override
                public LocalDate read(JsonReader in) throws IOException {
                    String dateStr = in.nextString();
                    return LocalDate.parse(dateStr, formatter);
                }
            }
            
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
            """);

        System.out.println("\n--- Comparison with Jackson ---");
        System.out.println("""
            Gson:
            ✓ Simpler API
            ✓ No annotations required
            ✓ Good for simple use cases
            ✗ Slower than Jackson
            ✗ Less features
            
            Jackson:
            ✓ Faster
            ✓ More features
            ✓ Better Spring integration
            ✗ More complex
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Reuse Gson instances (thread-safe)");
        System.out.println("   ✓ Use TypeToken for generics");
        System.out.println("   ✓ Use type adapters for custom types");
        System.out.println("   ✓ Handle null values appropriately");
        System.out.println();
    }
}
