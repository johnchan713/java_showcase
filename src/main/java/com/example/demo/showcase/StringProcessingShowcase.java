package com.example.demo.showcase;

/**
 * Demonstrates String processing including StringBuilder, StringBuffer, and Builder pattern
 * WITH advantages and disadvantages clearly marked
 */
public class StringProcessingShowcase {

    public static void demonstrate() {
        System.out.println("\n========== STRING PROCESSING SHOWCASE ==========\n");

        stringImmutability();
        stringBuilderDemo();
        stringBufferDemo();
        stringBuilderVsStringBuffer();
        builderPatternDemo();
        stringMethodsDemo();
    }

    // ========== String Immutability ==========

    private static void stringImmutability() {
        System.out.println("--- String Immutability ---");
        System.out.println("ADVANTAGE: Thread-safe, can be shared safely, string pool optimization");
        System.out.println("DISADVANTAGE: Creates new objects on modification, inefficient for concat");

        String str = "Hello";
        System.out.println("Original: " + str);
        System.out.println("Identity: " + System.identityHashCode(str));

        str = str + " World"; // Creates NEW object
        System.out.println("After concat: " + str);
        System.out.println("New Identity: " + System.identityHashCode(str));

        // String pool
        String s1 = "Java";
        String s2 = "Java";
        String s3 = new String("Java");

        System.out.println("\nString pool:");
        System.out.println("s1 == s2 (literal): " + (s1 == s2) + " (same object in pool)");
        System.out.println("s1 == s3 (new): " + (s1 == s3) + " (different object)");
        System.out.println("s1 == s3.intern(): " + (s1 == s3.intern()) + " (intern returns pooled)");

        // Inefficient string concatenation
        System.out.println("\nInefficient concatenation (creates many objects):");
        String result = "";
        for (int i = 0; i < 5; i++) {
            result += i + " "; // BAD: Creates new String each time
        }
        System.out.println("Result: " + result);
        System.out.println("Created 5+ String objects!");

        System.out.println();
    }

    // ========== StringBuilder ==========

    private static void stringBuilderDemo() {
        System.out.println("--- StringBuilder ---");
        System.out.println("✓ ADVANTAGE: Mutable, efficient for string building");
        System.out.println("✓ ADVANTAGE: No new objects created on modification");
        System.out.println("✓ ADVANTAGE: Fast performance");
        System.out.println("✗ DISADVANTAGE: NOT thread-safe");
        System.out.println("USE CASE: Single-threaded string manipulation");

        // Creating StringBuilder
        StringBuilder sb = new StringBuilder();
        System.out.println("\nInitial capacity: " + sb.capacity());

        // Append operations
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        sb.append("!");
        System.out.println("After appends: " + sb);

        // Insert
        sb.insert(6, "Java ");
        System.out.println("After insert: " + sb);

        // Replace
        sb.replace(0, 5, "Hi");
        System.out.println("After replace: " + sb);

        // Delete
        sb.delete(2, 5);
        System.out.println("After delete: " + sb);

        // Reverse
        StringBuilder rev = new StringBuilder("12345");
        rev.reverse();
        System.out.println("Reversed '12345': " + rev);

        // Character operations
        sb.setCharAt(0, 'H');
        System.out.println("After setCharAt: " + sb);

        // Length and capacity
        System.out.println("Length: " + sb.length());
        System.out.println("Capacity: " + sb.capacity());

        // Efficient loop concatenation
        System.out.println("\nEfficient concatenation:");
        StringBuilder efficient = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            efficient.append(i).append(" ");
        }
        System.out.println("Result: " + efficient);
        System.out.println("Only 1 StringBuilder object created!");

        // Method chaining
        String result = new StringBuilder()
            .append("Java")
            .append(" ")
            .append("21")
            .append(" ")
            .append("Rocks")
            .toString();
        System.out.println("Method chaining: " + result);

        System.out.println();
    }

    // ========== StringBuffer ==========

    private static void stringBufferDemo() {
        System.out.println("--- StringBuffer ---");
        System.out.println("✓ ADVANTAGE: Mutable, efficient for string building");
        System.out.println("✓ ADVANTAGE: Thread-safe (synchronized methods)");
        System.out.println("✗ DISADVANTAGE: Slower than StringBuilder due to synchronization");
        System.out.println("✗ DISADVANTAGE: Overhead when thread-safety not needed");
        System.out.println("USE CASE: Multi-threaded string manipulation");

        // Creating StringBuffer
        StringBuffer sbuf = new StringBuffer("Hello");
        System.out.println("\nInitial: " + sbuf);

        // All methods similar to StringBuilder but synchronized
        sbuf.append(" World");
        System.out.println("After append: " + sbuf);

        sbuf.insert(5, " Java");
        System.out.println("After insert: " + sbuf);

        sbuf.reverse();
        System.out.println("After reverse: " + sbuf);

        // Thread-safe operations
        System.out.println("\nThread-safe example:");
        StringBuffer shared = new StringBuffer();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                shared.append("A");
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                shared.append("B");
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final length: " + shared.length());
        System.out.println("Expected: 2000, Actual: " + shared.length());
        System.out.println("No characters lost due to synchronization!");

        System.out.println();
    }

    // ========== StringBuilder vs StringBuffer Comparison ==========

    private static void stringBuilderVsStringBuffer() {
        System.out.println("--- StringBuilder vs StringBuffer Comparison ---");

        // Performance test
        int iterations = 10000;

        // StringBuilder
        long sbStart = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("x");
        }
        long sbTime = System.nanoTime() - sbStart;

        // StringBuffer
        long sbufStart = System.nanoTime();
        StringBuffer sbuf = new StringBuffer();
        for (int i = 0; i < iterations; i++) {
            sbuf.append("x");
        }
        long sbufTime = System.nanoTime() - sbufStart;

        // String (inefficient)
        long strStart = System.nanoTime();
        String str = "";
        for (int i = 0; i < 1000; i++) { // Less iterations to avoid long wait
            str += "x";
        }
        long strTime = System.nanoTime() - strStart;

        System.out.println("Performance comparison (" + iterations + " appends):");
        System.out.println("StringBuilder: " + (sbTime / 1000) + " μs (FASTEST)");
        System.out.println("StringBuffer:  " + (sbufTime / 1000) + " μs (slower due to sync)");
        System.out.println("String concat:  " + (strTime / 1000) + " μs (1000 iterations - SLOWEST)");

        System.out.println("\n┌─────────────────────────────────────────────────────┐");
        System.out.println("│ QUICK REFERENCE                                     │");
        System.out.println("├─────────────────────────────────────────────────────┤");
        System.out.println("│ String:                                             │");
        System.out.println("│   - Immutable                                       │");
        System.out.println("│   - Thread-safe (can't be modified)                │");
        System.out.println("│   - Use for: Fixed strings, small operations       │");
        System.out.println("│                                                     │");
        System.out.println("│ StringBuilder:                                      │");
        System.out.println("│   - Mutable                                         │");
        System.out.println("│   - NOT thread-safe                                │");
        System.out.println("│   - Use for: Single-threaded string building       │");
        System.out.println("│   - RECOMMENDED for most cases                     │");
        System.out.println("│                                                     │");
        System.out.println("│ StringBuffer:                                       │");
        System.out.println("│   - Mutable                                         │");
        System.out.println("│   - Thread-safe (synchronized)                     │");
        System.out.println("│   - Use for: Multi-threaded string building        │");
        System.out.println("│   - Slower than StringBuilder                      │");
        System.out.println("└─────────────────────────────────────────────────────┘");

        System.out.println();
    }

    // ========== Builder Pattern ==========

    private static void builderPatternDemo() {
        System.out.println("--- Builder Pattern ---");
        System.out.println("ADVANTAGE: Readable object construction with many parameters");
        System.out.println("ADVANTAGE: Immutable objects, parameter validation");
        System.out.println("ADVANTAGE: Optional parameters without telescoping constructors");

        // Using the builder
        User user = new User.Builder("john@example.com")
            .name("John Doe")
            .age(30)
            .phone("123-456-7890")
            .address("123 Main St")
            .build();

        System.out.println("\nBuilt user: " + user);

        // Minimal user
        User minimalUser = new User.Builder("jane@example.com")
            .name("Jane")
            .build();

        System.out.println("Minimal user: " + minimalUser);

        System.out.println("\nBuilder Pattern Benefits:");
        System.out.println("- Clear, readable object construction");
        System.out.println("- No need for many constructors");
        System.out.println("- Enforces immutability");
        System.out.println("- Can validate before building");

        System.out.println();
    }

    // User class with Builder pattern
    static class User {
        private final String email; // Required
        private final String name;
        private final int age;
        private final String phone;
        private final String address;

        private User(Builder builder) {
            this.email = builder.email;
            this.name = builder.name;
            this.age = builder.age;
            this.phone = builder.phone;
            this.address = builder.address;
        }

        public static class Builder {
            // Required parameters
            private final String email;

            // Optional parameters
            private String name = "";
            private int age = 0;
            private String phone = "";
            private String address = "";

            public Builder(String email) {
                this.email = email;
            }

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder age(int age) {
                this.age = age;
                return this;
            }

            public Builder phone(String phone) {
                this.phone = phone;
                return this;
            }

            public Builder address(String address) {
                this.address = address;
                return this;
            }

            public User build() {
                // Validation can go here
                if (email == null || email.isEmpty()) {
                    throw new IllegalStateException("Email is required");
                }
                return new User(this);
            }
        }

        @Override
        public String toString() {
            return "User{email='" + email + "', name='" + name + "', age=" + age +
                   ", phone='" + phone + "', address='" + address + "'}";
        }
    }

    // ========== String Methods ==========

    private static void stringMethodsDemo() {
        System.out.println("--- String Methods ---");

        String str = "  Hello World  ";

        // Trimming
        System.out.println("Original: '" + str + "'");
        System.out.println("trim(): '" + str.trim() + "'");
        System.out.println("strip(): '" + str.strip() + "' (Unicode-aware)");

        // Case conversion
        System.out.println("toLowerCase(): " + str.toLowerCase());
        System.out.println("toUpperCase(): " + str.toUpperCase());

        // Substring
        String text = "Java Programming";
        System.out.println("substring(0, 4): " + text.substring(0, 4));
        System.out.println("substring(5): " + text.substring(5));

        // Replace
        System.out.println("replace('a', 'A'): " + text.replace('a', 'A'));
        System.out.println("replaceAll('[aeiou]', '*'): " + text.replaceAll("[aeiou]", "*"));

        // Split
        String csv = "apple,banana,cherry";
        String[] fruits = csv.split(",");
        System.out.println("split(','): " + java.util.Arrays.toString(fruits));

        // Join (Java 8+)
        String joined = String.join(" | ", fruits);
        System.out.println("join(' | '): " + joined);

        // Contains, startsWith, endsWith
        System.out.println("contains('Pro'): " + text.contains("Pro"));
        System.out.println("startsWith('Java'): " + text.startsWith("Java"));
        System.out.println("endsWith('ing'): " + text.endsWith("ing"));

        // Format
        String formatted = String.format("Name: %s, Age: %d, Score: %.2f", "Alice", 25, 95.5);
        System.out.println("Formatted: " + formatted);

        // Repeat (Java 11+)
        System.out.println("'*'.repeat(10): " + "*".repeat(10));

        // isBlank, isEmpty (Java 11+)
        System.out.println("'  '.isBlank(): " + "  ".isBlank());
        System.out.println("'  '.isEmpty(): " + "  ".isEmpty());
        System.out.println("''.isEmpty(): " + "".isEmpty());

        System.out.println();
    }
}
