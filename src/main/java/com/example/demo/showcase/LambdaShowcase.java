package com.example.demo.showcase;

import java.util.*;
import java.util.function.*;

/**
 * Demonstrates Lambda expressions and Functional Interfaces in Java
 */
public class LambdaShowcase {

    public static void demonstrate() {
        System.out.println("\n========== LAMBDA SHOWCASE ==========\n");

        basicLambdas();
        functionalInterfaces();
        builtInFunctionalInterfaces();
        methodReferences();
        lambdaWithCollections();
        customFunctionalInterfaces();
    }

    // ========== Basic Lambdas ==========

    private static void basicLambdas() {
        System.out.println("--- Basic Lambdas ---");

        // Lambda with no parameters
        Runnable runnable = () -> System.out.println("Hello from lambda!");
        runnable.run();

        // Lambda with one parameter
        Consumer<String> printer = message -> System.out.println("Message: " + message);
        printer.accept("Lambda with one parameter");

        // Lambda with multiple parameters
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        System.out.println("5 + 3 = " + add.apply(5, 3));

        // Lambda with multiple statements
        BiFunction<Integer, Integer, Integer> multiply = (a, b) -> {
            int result = a * b;
            System.out.println("Multiplying " + a + " * " + b);
            return result;
        };
        System.out.println("Result: " + multiply.apply(4, 5));

        // Lambda with explicit type
        BiFunction<String, String, String> concat = (String s1, String s2) -> s1 + s2;
        System.out.println("Concatenation: " + concat.apply("Hello", "World"));

        System.out.println();
    }

    // ========== Functional Interfaces ==========

    @FunctionalInterface
    interface Calculator {
        int calculate(int a, int b);
    }

    @FunctionalInterface
    interface StringProcessor {
        String process(String str);
    }

    private static void functionalInterfaces() {
        System.out.println("--- Functional Interfaces ---");

        // Using custom functional interface
        Calculator add = (a, b) -> a + b;
        Calculator subtract = (a, b) -> a - b;
        Calculator multiply = (a, b) -> a * b;
        Calculator divide = (a, b) -> b != 0 ? a / b : 0;

        int x = 10, y = 5;
        System.out.println(x + " + " + y + " = " + add.calculate(x, y));
        System.out.println(x + " - " + y + " = " + subtract.calculate(x, y));
        System.out.println(x + " * " + y + " = " + multiply.calculate(x, y));
        System.out.println(x + " / " + y + " = " + divide.calculate(x, y));

        // String processor
        StringProcessor toUpper = str -> str.toUpperCase();
        StringProcessor toLower = str -> str.toLowerCase();
        StringProcessor reverse = str -> new StringBuilder(str).reverse().toString();

        String text = "Hello World";
        System.out.println("Original: " + text);
        System.out.println("Upper: " + toUpper.process(text));
        System.out.println("Lower: " + toLower.process(text));
        System.out.println("Reverse: " + reverse.process(text));

        System.out.println();
    }

    // ========== Built-in Functional Interfaces ==========

    private static void builtInFunctionalInterfaces() {
        System.out.println("--- Built-in Functional Interfaces ---");

        // Predicate<T> - takes T, returns boolean
        Predicate<Integer> isEven = num -> num % 2 == 0;
        Predicate<String> isLong = str -> str.length() > 5;
        System.out.println("Is 4 even? " + isEven.test(4));
        System.out.println("Is 7 even? " + isEven.test(7));
        System.out.println("Is 'Hello' long? " + isLong.test("Hello"));
        System.out.println("Is 'HelloWorld' long? " + isLong.test("HelloWorld"));

        // Function<T, R> - takes T, returns R
        Function<String, Integer> stringLength = str -> str.length();
        Function<Integer, String> intToString = num -> "Number: " + num;
        System.out.println("Length of 'Java': " + stringLength.apply("Java"));
        System.out.println(intToString.apply(42));

        // Consumer<T> - takes T, returns void
        Consumer<String> printUpper = str -> System.out.println(str.toUpperCase());
        System.out.print("Consumer: ");
        printUpper.accept("hello world");

        // Supplier<T> - takes nothing, returns T
        Supplier<Double> randomValue = () -> Math.random();
        Supplier<String> greeting = () -> "Hello!";
        System.out.println("Random value: " + randomValue.get());
        System.out.println("Greeting: " + greeting.get());

        // BiFunction<T, U, R> - takes T and U, returns R
        BiFunction<String, Integer, String> repeat = (str, times) -> str.repeat(times);
        System.out.println("Repeat: " + repeat.apply("Java ", 3));

        // BiPredicate<T, U> - takes T and U, returns boolean
        BiPredicate<String, Integer> isLengthEqual = (str, len) -> str.length() == len;
        System.out.println("Is 'Java' length 4? " + isLengthEqual.test("Java", 4));

        // BiConsumer<T, U> - takes T and U, returns void
        BiConsumer<String, Integer> printRepeat = (str, times) ->
            System.out.println(str + " (repeated " + times + " times)");
        printRepeat.accept("Lambda", 5);

        // UnaryOperator<T> - takes T, returns T
        UnaryOperator<Integer> square = x -> x * x;
        UnaryOperator<String> addPrefix = str -> "Prefix_" + str;
        System.out.println("Square of 5: " + square.apply(5));
        System.out.println("Add prefix: " + addPrefix.apply("Test"));

        // BinaryOperator<T> - takes two T, returns T
        BinaryOperator<Integer> max = (a, b) -> a > b ? a : b;
        BinaryOperator<String> concat = (s1, s2) -> s1 + s2;
        System.out.println("Max of 10 and 20: " + max.apply(10, 20));
        System.out.println("Concat: " + concat.apply("Hello", "World"));

        System.out.println();
    }

    // ========== Method References ==========

    private static void methodReferences() {
        System.out.println("--- Method References ---");

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        // Static method reference
        System.out.println("Static method reference:");
        names.forEach(LambdaShowcase::printUpperCase);

        // Instance method reference
        System.out.println("\nInstance method reference:");
        LambdaShowcase showcase = new LambdaShowcase();
        names.forEach(showcase::printLowerCase);

        // Reference to instance method of arbitrary object
        System.out.println("\nArbitrary object method reference:");
        names.stream()
            .map(String::toUpperCase)
            .forEach(System.out::println);

        // Constructor reference
        System.out.println("\nConstructor reference:");
        Function<String, StringBuilder> sbCreator = StringBuilder::new;
        StringBuilder sb = sbCreator.apply("Hello");
        System.out.println("Created StringBuilder: " + sb);

        // Array constructor reference
        IntFunction<int[]> arrayCreator = int[]::new;
        int[] array = arrayCreator.apply(5);
        System.out.println("Created array of length: " + array.length);

        System.out.println();
    }

    private static void printUpperCase(String str) {
        System.out.println("  " + str.toUpperCase());
    }

    private void printLowerCase(String str) {
        System.out.println("  " + str.toLowerCase());
    }

    // ========== Lambda with Collections ==========

    private static void lambdaWithCollections() {
        System.out.println("--- Lambda with Collections ---");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // forEach
        System.out.print("forEach: ");
        numbers.forEach(n -> System.out.print(n + " "));
        System.out.println();

        // removeIf
        List<Integer> mutableList = new ArrayList<>(numbers);
        mutableList.removeIf(n -> n % 2 == 0);
        System.out.println("After removeIf (remove even): " + mutableList);

        // replaceAll
        List<String> words = new ArrayList<>(Arrays.asList("apple", "banana", "cherry"));
        words.replaceAll(String::toUpperCase);
        System.out.println("After replaceAll: " + words);

        // sort
        List<String> names = new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob"));
        names.sort((a, b) -> a.compareTo(b));
        System.out.println("Sorted names: " + names);

        // sort with Comparator methods
        names.sort(Comparator.reverseOrder());
        System.out.println("Reverse sorted: " + names);

        // Map operations
        Map<String, Integer> map = new HashMap<>();
        map.put("Alice", 25);
        map.put("Bob", 30);
        map.put("Charlie", 35);

        System.out.println("Map forEach:");
        map.forEach((key, value) -> System.out.println("  " + key + ": " + value));

        // computeIfAbsent
        map.computeIfAbsent("David", key -> key.length() * 10);
        System.out.println("After computeIfAbsent: " + map);

        // merge
        map.merge("Alice", 5, (oldVal, newVal) -> oldVal + newVal);
        System.out.println("After merge: " + map);

        System.out.println();
    }

    // ========== Custom Functional Interfaces ==========

    @FunctionalInterface
    interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }

    @FunctionalInterface
    interface Validator<T> {
        boolean validate(T value);

        default Validator<T> and(Validator<T> other) {
            return value -> this.validate(value) && other.validate(value);
        }

        default Validator<T> or(Validator<T> other) {
            return value -> this.validate(value) || other.validate(value);
        }
    }

    private static void customFunctionalInterfaces() {
        System.out.println("--- Custom Functional Interfaces ---");

        // TriFunction - three parameters
        TriFunction<Integer, Integer, Integer, Integer> sum3 = (a, b, c) -> a + b + c;
        System.out.println("Sum of 1, 2, 3: " + sum3.apply(1, 2, 3));

        TriFunction<String, String, String, String> concat3 = (a, b, c) -> a + b + c;
        System.out.println("Concat: " + concat3.apply("Hello", " ", "World"));

        // Validator with composition
        Validator<String> notNull = str -> str != null;
        Validator<String> notEmpty = str -> !str.isEmpty();
        Validator<String> minLength = str -> str.length() >= 3;

        Validator<String> stringValidator = notNull.and(notEmpty).and(minLength);

        System.out.println("Validate 'Hi': " + stringValidator.validate("Hi"));
        System.out.println("Validate 'Hello': " + stringValidator.validate("Hello"));
        System.out.println("Validate null: " + stringValidator.validate(null));

        // Number validator
        Validator<Integer> positive = num -> num > 0;
        Validator<Integer> lessThan100 = num -> num < 100;
        Validator<Integer> numberValidator = positive.and(lessThan100);

        System.out.println("Validate 50: " + numberValidator.validate(50));
        System.out.println("Validate 150: " + numberValidator.validate(150));
        System.out.println("Validate -5: " + numberValidator.validate(-5));

        System.out.println();
    }
}
