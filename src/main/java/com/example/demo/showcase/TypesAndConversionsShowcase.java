package com.example.demo.showcase;

import java.util.*;

/**
 * Demonstrates Java types, primitives, wrappers, conversions, parsing
 * Also includes Pair and Tuple implementations
 */
public class TypesAndConversionsShowcase {

    public static void demonstrate() {
        System.out.println("\n========== TYPES AND CONVERSIONS SHOWCASE ==========\n");

        primitivesDemo();
        wrappersDemo();
        autoboxingUnboxing();
        stringConversions();
        parsingDemo();
        typeConversions();
        pairAndTupleDemo();
        optionalDemo();
    }

    // ========== Primitives ==========

    private static void primitivesDemo() {
        System.out.println("--- Primitive Types ---");

        // byte: 8-bit, range: -128 to 127
        byte byteVar = 127;
        System.out.println("byte: " + byteVar + " (size: " + Byte.BYTES + " bytes)");

        // short: 16-bit, range: -32,768 to 32,767
        short shortVar = 32767;
        System.out.println("short: " + shortVar + " (size: " + Short.BYTES + " bytes)");

        // int: 32-bit, range: -2^31 to 2^31-1
        int intVar = 2147483647;
        System.out.println("int: " + intVar + " (size: " + Integer.BYTES + " bytes)");

        // long: 64-bit, range: -2^63 to 2^63-1
        long longVar = 9223372036854775807L;
        System.out.println("long: " + longVar + " (size: " + Long.BYTES + " bytes)");

        // float: 32-bit IEEE 754 floating point
        float floatVar = 3.14159f;
        System.out.println("float: " + floatVar + " (size: " + Float.BYTES + " bytes)");

        // double: 64-bit IEEE 754 floating point
        double doubleVar = 3.141592653589793;
        System.out.println("double: " + doubleVar + " (size: " + Double.BYTES + " bytes)");

        // char: 16-bit Unicode character
        char charVar = 'A';
        System.out.println("char: " + charVar + " (size: " + Character.BYTES + " bytes)");

        // boolean: true or false
        boolean boolVar = true;
        System.out.println("boolean: " + boolVar + " (size: not precisely defined)");

        System.out.println();
    }

    // ========== Wrapper Classes ==========

    private static void wrappersDemo() {
        System.out.println("--- Wrapper Classes ---");

        // Wrapper classes for primitives
        Byte byteObj = Byte.valueOf((byte) 10);
        Short shortObj = Short.valueOf((short) 100);
        Integer intObj = Integer.valueOf(1000);
        Long longObj = Long.valueOf(10000L);
        Float floatObj = Float.valueOf(3.14f);
        Double doubleObj = Double.valueOf(3.14159);
        Character charObj = Character.valueOf('A');
        Boolean boolObj = Boolean.valueOf(true);

        System.out.println("Byte: " + byteObj);
        System.out.println("Short: " + shortObj);
        System.out.println("Integer: " + intObj);
        System.out.println("Long: " + longObj);
        System.out.println("Float: " + floatObj);
        System.out.println("Double: " + doubleObj);
        System.out.println("Character: " + charObj);
        System.out.println("Boolean: " + boolObj);

        // Min and Max values
        System.out.println("\nMin/Max values:");
        System.out.println("Byte: " + Byte.MIN_VALUE + " to " + Byte.MAX_VALUE);
        System.out.println("Short: " + Short.MIN_VALUE + " to " + Short.MAX_VALUE);
        System.out.println("Integer: " + Integer.MIN_VALUE + " to " + Integer.MAX_VALUE);
        System.out.println("Long: " + Long.MIN_VALUE + " to " + Long.MAX_VALUE);
        System.out.println("Float: " + Float.MIN_VALUE + " to " + Float.MAX_VALUE);
        System.out.println("Double: " + Double.MIN_VALUE + " to " + Double.MAX_VALUE);

        // Wrapper class utility methods
        System.out.println("\nWrapper utilities:");
        System.out.println("Integer.compare(10, 20): " + Integer.compare(10, 20));
        System.out.println("Integer.max(10, 20): " + Integer.max(10, 20));
        System.out.println("Integer.min(10, 20): " + Integer.min(10, 20));
        System.out.println("Integer.sum(10, 20): " + Integer.sum(10, 20));
        System.out.println("Double.isNaN(0.0/0.0): " + Double.isNaN(0.0 / 0.0));
        System.out.println("Double.isInfinite(1.0/0.0): " + Double.isInfinite(1.0 / 0.0));

        System.out.println();
    }

    // ========== Autoboxing and Unboxing ==========

    private static void autoboxingUnboxing() {
        System.out.println("--- Autoboxing and Unboxing ---");

        // Autoboxing: primitive to wrapper
        int primitiveInt = 100;
        Integer wrapperInt = primitiveInt; // Autoboxing
        System.out.println("Autoboxing: int " + primitiveInt + " -> Integer " + wrapperInt);

        // Unboxing: wrapper to primitive
        Integer wrapperInt2 = 200;
        int primitiveInt2 = wrapperInt2; // Unboxing
        System.out.println("Unboxing: Integer " + wrapperInt2 + " -> int " + primitiveInt2);

        // In collections (only objects allowed)
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1); // Autoboxing
        numbers.add(2);
        numbers.add(3);
        int sum = 0;
        for (int num : numbers) { // Unboxing
            sum += num;
        }
        System.out.println("Sum using autoboxing/unboxing: " + sum);

        // Watch out for null
        Integer nullInt = null;
        try {
            int value = nullInt; // NullPointerException!
        } catch (NullPointerException e) {
            System.out.println("Unboxing null causes NullPointerException");
        }

        System.out.println();
    }

    // ========== String Conversions ==========

    private static void stringConversions() {
        System.out.println("--- String Conversions ---");

        // Primitive to String
        int num = 42;
        String str1 = String.valueOf(num);
        String str2 = Integer.toString(num);
        String str3 = "" + num;
        System.out.println("int to String: " + str1 + ", " + str2 + ", " + str3);

        // String to primitive
        String numStr = "123";
        int parsedInt = Integer.parseInt(numStr);
        System.out.println("String to int: " + parsedInt);

        // Other types
        double d = 3.14;
        String dStr = String.valueOf(d);
        System.out.println("double to String: " + dStr);

        boolean b = true;
        String bStr = String.valueOf(b);
        System.out.println("boolean to String: " + bStr);

        char c = 'A';
        String cStr = String.valueOf(c);
        System.out.println("char to String: " + cStr);

        // Array to String
        int[] array = {1, 2, 3, 4, 5};
        String arrayStr = Arrays.toString(array);
        System.out.println("Array to String: " + arrayStr);

        // Object to String
        Object obj = new Date();
        String objStr = obj.toString();
        System.out.println("Object to String: " + objStr);

        System.out.println();
    }

    // ========== Parsing ==========

    private static void parsingDemo() {
        System.out.println("--- Parsing ---");

        // Parse Integer
        String intStr = "123";
        int parsedInt = Integer.parseInt(intStr);
        System.out.println("parseInt('123'): " + parsedInt);

        // Parse with radix
        String hexStr = "FF";
        int hexInt = Integer.parseInt(hexStr, 16);
        System.out.println("parseInt('FF', 16): " + hexInt);

        String binaryStr = "1010";
        int binaryInt = Integer.parseInt(binaryStr, 2);
        System.out.println("parseInt('1010', 2): " + binaryInt);

        // Parse Double
        String doubleStr = "3.14159";
        double parsedDouble = Double.parseDouble(doubleStr);
        System.out.println("parseDouble('3.14159'): " + parsedDouble);

        // Parse Boolean
        String boolStr = "true";
        boolean parsedBool = Boolean.parseBoolean(boolStr);
        System.out.println("parseBoolean('true'): " + parsedBool);

        // Parse Long
        String longStr = "9999999999";
        long parsedLong = Long.parseLong(longStr);
        System.out.println("parseLong('9999999999'): " + parsedLong);

        // Parse Float
        String floatStr = "2.71828";
        float parsedFloat = Float.parseFloat(floatStr);
        System.out.println("parseFloat('2.71828'): " + parsedFloat);

        // valueOf vs parse
        Integer intObj = Integer.valueOf("456");
        int intPrim = Integer.parseInt("456");
        System.out.println("valueOf returns: " + intObj.getClass().getSimpleName());
        System.out.println("parseInt returns: int");

        // Handle parsing errors
        try {
            int invalid = Integer.parseInt("abc");
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: Cannot parse 'abc' as integer");
        }

        System.out.println();
    }

    // ========== Type Conversions ==========

    private static void typeConversions() {
        System.out.println("--- Type Conversions ---");

        // Widening (implicit)
        byte b = 10;
        short s = b;
        int i = s;
        long l = i;
        float f = l;
        double d = f;
        System.out.println("Widening: byte -> short -> int -> long -> float -> double");
        System.out.println("Result: " + d);

        // Narrowing (explicit casting)
        double d2 = 123.456;
        int i2 = (int) d2; // Truncates decimal part
        System.out.println("Narrowing: double " + d2 + " -> int " + i2);

        long l2 = 1000L;
        int i3 = (int) l2;
        System.out.println("Narrowing: long " + l2 + " -> int " + i3);

        // Numeric promotion
        byte b1 = 10;
        byte b2 = 20;
        int result = b1 + b2; // Result is int, not byte
        System.out.println("Numeric promotion: byte + byte = int");

        // Integer division vs float division
        int x = 5;
        int y = 2;
        System.out.println("Integer division: 5 / 2 = " + (x / y));
        System.out.println("Float division: 5 / 2.0 = " + (x / 2.0));

        // Convert between number types
        Integer intObj = 100;
        Long longObj = intObj.longValue();
        Double doubleObj = intObj.doubleValue();
        System.out.println("Integer to Long: " + longObj);
        System.out.println("Integer to Double: " + doubleObj);

        // Convert using wrapper classes
        String numStr = "255";
        int decimal = Integer.parseInt(numStr);
        String hex = Integer.toHexString(decimal);
        String octal = Integer.toOctalString(decimal);
        String binary = Integer.toBinaryString(decimal);
        System.out.println("Decimal: " + decimal);
        System.out.println("Hex: " + hex);
        System.out.println("Octal: " + octal);
        System.out.println("Binary: " + binary);

        System.out.println();
    }

    // ========== Pair and Tuple ==========

    // Java doesn't have built-in Pair/Tuple, but we can create them using records (Java 14+)
    record Pair<K, V>(K first, V second) {
        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }

    record Triple<A, B, C>(A first, B second, C third) {
        @Override
        public String toString() {
            return "(" + first + ", " + second + ", " + third + ")";
        }
    }

    // Generic Tuple for any number of elements
    record Tuple<T>(T... elements) {
        @Override
        public String toString() {
            return Arrays.toString(elements);
        }

        public T get(int index) {
            return elements[index];
        }

        public int size() {
            return elements.length;
        }
    }

    private static void pairAndTupleDemo() {
        System.out.println("--- Pair and Tuple ---");

        // Pair usage
        Pair<String, Integer> nameAge = new Pair<>("Alice", 25);
        System.out.println("Pair: " + nameAge);
        System.out.println("First: " + nameAge.first());
        System.out.println("Second: " + nameAge.second());

        // Different type pairs
        Pair<Integer, String> idName = new Pair<>(1, "Bob");
        Pair<Double, Double> coordinates = new Pair<>(3.14, 2.71);
        System.out.println("ID-Name: " + idName);
        System.out.println("Coordinates: " + coordinates);

        // Triple usage
        Triple<String, Integer, String> personInfo = new Triple<>("Charlie", 30, "Engineer");
        System.out.println("Triple: " + personInfo);
        System.out.println("Name: " + personInfo.first());
        System.out.println("Age: " + personInfo.second());
        System.out.println("Job: " + personInfo.third());

        // Generic Tuple
        Tuple<Object> tuple4 = new Tuple<>("Alice", 25, true, 3.14);
        System.out.println("Tuple: " + tuple4);
        System.out.println("Size: " + tuple4.size());
        System.out.println("Element 0: " + tuple4.get(0));
        System.out.println("Element 2: " + tuple4.get(2));

        // Map.Entry as Pair alternative
        Map.Entry<String, Integer> entry = Map.entry("Key", 100);
        System.out.println("Map.Entry as Pair: " + entry);
        System.out.println("Key: " + entry.getKey());
        System.out.println("Value: " + entry.getValue());

        // List of Pairs
        List<Pair<String, Integer>> pairs = Arrays.asList(
            new Pair<>("A", 1),
            new Pair<>("B", 2),
            new Pair<>("C", 3)
        );
        System.out.println("List of pairs: " + pairs);

        System.out.println();
    }

    // ========== Optional ==========

    private static void optionalDemo() {
        System.out.println("--- Optional (Handling null) ---");

        // Creating Optional
        Optional<String> optional1 = Optional.of("Hello");
        Optional<String> optional2 = Optional.ofNullable(null);
        Optional<String> optional3 = Optional.empty();

        System.out.println("Optional.of('Hello'): " + optional1);
        System.out.println("Optional.ofNullable(null): " + optional2);
        System.out.println("Optional.empty(): " + optional3);

        // isPresent and isEmpty
        System.out.println("optional1.isPresent(): " + optional1.isPresent());
        System.out.println("optional2.isEmpty(): " + optional2.isEmpty());

        // get (unsafe - throws exception if empty)
        String value1 = optional1.get();
        System.out.println("optional1.get(): " + value1);

        // orElse
        String value2 = optional2.orElse("Default");
        System.out.println("optional2.orElse('Default'): " + value2);

        // orElseGet
        String value3 = optional2.orElseGet(() -> "Computed Default");
        System.out.println("optional2.orElseGet(): " + value3);

        // orElseThrow
        try {
            optional2.orElseThrow(() -> new RuntimeException("Value not present"));
        } catch (RuntimeException e) {
            System.out.println("orElseThrow: " + e.getMessage());
        }

        // ifPresent
        optional1.ifPresent(val -> System.out.println("ifPresent: " + val));

        // ifPresentOrElse (Java 9+)
        optional2.ifPresentOrElse(
            val -> System.out.println("Value: " + val),
            () -> System.out.println("ifPresentOrElse: No value present")
        );

        // map
        Optional<Integer> length = optional1.map(String::length);
        System.out.println("map to length: " + length);

        // flatMap
        Optional<String> upperCase = optional1.flatMap(s -> Optional.of(s.toUpperCase()));
        System.out.println("flatMap to uppercase: " + upperCase);

        // filter
        Optional<String> filtered = optional1.filter(s -> s.length() > 3);
        System.out.println("filter (length > 3): " + filtered);

        // or (Java 9+)
        Optional<String> result = optional2.or(() -> Optional.of("Alternative"));
        System.out.println("or: " + result);

        // stream (Java 9+)
        long count = optional1.stream().count();
        System.out.println("stream count: " + count);

        System.out.println();
    }
}
