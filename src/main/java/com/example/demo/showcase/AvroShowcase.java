package com.example.demo.showcase;

/**
 * Demonstrates Apache Avro - Data serialization framework
 * Covers schema definition, serialization, and deserialization
 */
public class AvroShowcase {

    public static void demonstrate() {
        System.out.println("\n========== APACHE AVRO SHOWCASE ==========\n");

        System.out.println("--- Apache Avro Overview ---");
        System.out.println("Binary data serialization with schema\n");

        System.out.println("Key features:");
        System.out.println("   • Compact binary format");
        System.out.println("   • Schema evolution support");
        System.out.println("   • Rich data structures");
        System.out.println("   • Code generation from schema");
        System.out.println("   • Language-independent");

        System.out.println("\nDependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.apache.avro</groupId>
                <artifactId>avro</artifactId>
                <version>1.11.3</version>
            </dependency>
            """);

        System.out.println("\n--- Avro Schema ---");
        System.out.println("""
            // user.avsc
            {
              "type": "record",
              "name": "User",
              "namespace": "com.example.avro",
              "fields": [
                {"name": "id", "type": "long"},
                {"name": "name", "type": "string"},
                {"name": "email", "type": ["null", "string"], "default": null},
                {"name": "age", "type": "int"},
                {"name": "active", "type": "boolean", "default": true},
                {"name": "tags", "type": {"type": "array", "items": "string"}},
                {
                  "name": "address",
                  "type": {
                    "type": "record",
                    "name": "Address",
                    "fields": [
                      {"name": "street", "type": "string"},
                      {"name": "city", "type": "string"},
                      {"name": "zipCode", "type": "string"}
                    ]
                  }
                }
              ]
            }
            """);

        System.out.println("\n--- Serialization (without code generation) ---");
        System.out.println("""
            import org.apache.avro.*;
            import org.apache.avro.generic.*;
            import org.apache.avro.io.*;
            
            // Load schema
            Schema schema = new Schema.Parser().parse(new File("user.avsc"));
            
            // Create record
            GenericRecord user = new GenericData.Record(schema);
            user.put("id", 1L);
            user.put("name", "John Doe");
            user.put("email", "john@example.com");
            user.put("age", 30);
            user.put("active", true);
            user.put("tags", Arrays.asList("premium", "verified"));
            
            // Serialize
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DatumWriter<GenericRecord> writer = new GenericDatumWriter<>(schema);
            Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            writer.write(user, encoder);
            encoder.flush();
            byte[] serialized = out.toByteArray();
            """);

        System.out.println("\n--- Deserialization ---");
        System.out.println("""
            // Deserialize
            DatumReader<GenericRecord> reader = new GenericDatumReader<>(schema);
            Decoder decoder = DecoderFactory.get()
                .binaryDecoder(serialized, null);
            GenericRecord user = reader.read(null, decoder);
            
            String name = user.get("name").toString();
            int age = (Integer) user.get("age");
            """);

        System.out.println("\n--- With Code Generation ---");
        System.out.println("""
            // Generate Java classes from schema
            // Maven plugin:
            <plugin>
                <groupId>org.apache.avro</groupId>
                <artifactId>avro-maven-plugin</artifactId>
                <version>1.11.3</version>
                <executions>
                    <execution>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>schema</goal>
                        </goals>
                        <configuration>
                            <sourceDirectory>${project.basedir}/src/main/avro/</sourceDirectory>
                            <outputDirectory>${project.basedir}/src/main/java/</outputDirectory>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            
            // Use generated class
            User user = User.newBuilder()
                .setId(1L)
                .setName("John Doe")
                .setEmail("john@example.com")
                .setAge(30)
                .setActive(true)
                .setTags(Arrays.asList("premium", "verified"))
                .build();
            
            // Serialize
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DatumWriter<User> writer = new SpecificDatumWriter<>(User.class);
            Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            writer.write(user, encoder);
            encoder.flush();
            """);

        System.out.println("\n--- Use with Kafka ---");
        System.out.println("""
            // Kafka producer with Avro
            Properties props = new Properties();
            props.put("bootstrap.servers", "localhost:9092");
            props.put("key.serializer", StringSerializer.class);
            props.put("value.serializer", KafkaAvroSerializer.class);
            props.put("schema.registry.url", "http://localhost:8081");
            
            KafkaProducer<String, User> producer = new KafkaProducer<>(props);
            producer.send(new ProducerRecord<>("users", user));
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Use code generation for type safety");
        System.out.println("   ✓ Version schemas carefully");
        System.out.println("   ✓ Use schema registry with Kafka");
        System.out.println("   ✓ Compact binary format saves space");
        System.out.println();
    }
}
