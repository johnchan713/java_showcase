package com.example.demo.showcase;

import java.util.*;
import java.util.function.Function;

/**
 * Demonstrates Java Generics including type parameters, wildcards, and varargs
 * Covers generic classes, methods, bounded types, wildcards (? extends, ? super), and variadic arguments
 */
public class GenericsShowcase {

    public static void demonstrate() {
        System.out.println("\n========== GENERICS & WILDCARDS SHOWCASE ==========\n");

        genericClassDemo();
        genericMethodDemo();
        boundedTypesDemo();
        wildcardsDemo();
        variadicArgumentsDemo();
        typeErasureDemo();
        genericInheritanceDemo();
    }

    // ========== Generic Classes ==========

    private static void genericClassDemo() {
        System.out.println("--- Generic Classes ---");
        System.out.println("Type-safe containers with type parameters\n");

        // Single type parameter
        Box<String> stringBox = new Box<>("Hello");
        System.out.println("1. Box<String>: " + stringBox.get());

        Box<Integer> intBox = new Box<>(42);
        System.out.println("   Box<Integer>: " + intBox.get());

        // Multiple type parameters
        Pair<String, Integer> pair = new Pair<>("Age", 30);
        System.out.println("\n2. Pair<String, Integer>:");
        System.out.println("   Key: " + pair.getKey());
        System.out.println("   Value: " + pair.getValue());

        // Generic with complex types
        Box<List<String>> listBox = new Box<>(Arrays.asList("a", "b", "c"));
        System.out.println("\n3. Box<List<String>>: " + listBox.get());

        System.out.println("\nAdvantages:");
        System.out.println("  ✓ Type safety at compile time");
        System.out.println("  ✓ No casting needed");
        System.out.println("  ✓ Code reusability");
        System.out.println("  ✗ Cannot use primitives (use wrappers)");

        System.out.println();
    }

    // ========== Generic Methods ==========

    private static void genericMethodDemo() {
        System.out.println("--- Generic Methods ---");
        System.out.println("Methods with type parameters\n");

        // Generic method - type inferred
        System.out.println("1. Type inference:");
        printArray(new String[]{"Java", "Python", "C++"});
        printArray(new Integer[]{1, 2, 3, 4, 5});

        // Generic method with return type
        System.out.println("\n2. Generic method with return:");
        String first = getFirst(Arrays.asList("Apple", "Banana", "Cherry"));
        System.out.println("   First element: " + first);

        Integer firstNum = getFirst(Arrays.asList(10, 20, 30));
        System.out.println("   First number: " + firstNum);

        // Multiple type parameters
        System.out.println("\n3. Multiple type parameters:");
        Pair<String, Integer> result = makePair("Count", 100);
        System.out.println("   " + result);

        // Generic method in non-generic class
        String reversed = reverse("Hello");
        System.out.println("\n4. Reverse string: " + reversed);

        System.out.println();
    }

    // ========== Bounded Type Parameters ==========

    private static void boundedTypesDemo() {
        System.out.println("--- Bounded Type Parameters ---");
        System.out.println("Restrict type parameters with extends/super\n");

        // Upper bound (extends)
        System.out.println("1. Upper bound - <T extends Number>:");
        System.out.println("   Sum integers: " + sumNumbers(Arrays.asList(1, 2, 3, 4, 5)));
        System.out.println("   Sum doubles: " + sumNumbers(Arrays.asList(1.5, 2.5, 3.0)));

        // Multiple bounds
        System.out.println("\n2. Multiple bounds - <T extends Comparable<T> & Serializable>:");
        String max1 = findMax("apple", "banana", "cherry");
        System.out.println("   Max string: " + max1);

        Integer max2 = findMax(10, 50, 30);
        System.out.println("   Max integer: " + max2);

        // Bounded class
        System.out.println("\n3. Bounded class - NumberBox<T extends Number>:");
        NumberBox<Integer> intBox = new NumberBox<>(42);
        System.out.println("   Integer box: " + intBox.get());
        System.out.println("   Double value: " + intBox.doubleValue());

        NumberBox<Double> doubleBox = new NumberBox<>(3.14);
        System.out.println("   Double box: " + doubleBox.get());
        System.out.println("   Double value: " + doubleBox.doubleValue());

        // Won't compile: NumberBox<String> - String is not a Number

        System.out.println();
    }

    // ========== Wildcards ==========

    private static void wildcardsDemo() {
        System.out.println("--- Wildcards (?, ? extends, ? super) ---");
        System.out.println("Flexible type matching for generics\n");

        // Unbounded wildcard (?)
        System.out.println("1. Unbounded wildcard (?):");
        List<?> unknownList = Arrays.asList(1, 2, 3);
        printList(unknownList);
        unknownList = Arrays.asList("a", "b", "c");
        printList(unknownList);
        System.out.println("   USE: Read-only access, type unknown");

        // Upper bounded wildcard (? extends)
        System.out.println("\n2. Upper bounded wildcard (? extends Number):");
        System.out.println("   RULE: Can READ as Number, cannot ADD (except null)");
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);

        System.out.println("   Sum of integers: " + sumList(integers));
        System.out.println("   Sum of doubles: " + sumList(doubles));
        System.out.println("   USE: Consumer/Reading from structure");

        // Lower bounded wildcard (? super)
        System.out.println("\n3. Lower bounded wildcard (? super Integer):");
        System.out.println("   RULE: Can ADD Integer, cannot READ (as Object only)");
        List<Number> numbers = new ArrayList<>();
        addIntegers(numbers);
        System.out.println("   Added integers to List<Number>: " + numbers);

        List<Object> objects = new ArrayList<>();
        addIntegers(objects);
        System.out.println("   Added integers to List<Object>: " + objects);
        System.out.println("   USE: Producer/Writing to structure");

        // PECS: Producer Extends, Consumer Super
        System.out.println("\n4. PECS Principle:");
        System.out.println("   Producer Extends: Use <? extends T> when reading");
        System.out.println("   Consumer Super: Use <? super T> when writing");
        System.out.println("   Example: Collections.copy(List<? super T> dest, List<? extends T> src)");

        List<Integer> source = Arrays.asList(1, 2, 3);
        List<Number> destination = new ArrayList<>(Arrays.asList(0, 0, 0));
        Collections.copy(destination, source); // dest is super (consumer), src is extends (producer)
        System.out.println("   Copied: " + destination);

        // Wildcard capture
        System.out.println("\n5. Wildcard vs Concrete Type:");
        List<String> stringList = Arrays.asList("a", "b", "c");
        swapFirst(stringList); // Works with concrete type
        System.out.println("   After swap: " + stringList);

        System.out.println();
    }

    // ========== Variadic Arguments (Varargs) ==========

    private static void variadicArgumentsDemo() {
        System.out.println("--- Variadic Arguments (Varargs) ---");
        System.out.println("Methods accepting variable number of arguments\n");

        // Basic varargs
        System.out.println("1. Basic varargs:");
        System.out.println("   Sum of 3 numbers: " + sum(1, 2, 3));
        System.out.println("   Sum of 5 numbers: " + sum(10, 20, 30, 40, 50));
        System.out.println("   Sum of no numbers: " + sum());

        // Varargs with other parameters
        System.out.println("\n2. Varargs with other parameters:");
        printWithPrefix("Values: ", 1, 2, 3, 4, 5);
        printWithPrefix("Items: ", "a", "b", "c");

        // Generic varargs
        System.out.println("\n3. Generic varargs:");
        List<String> list1 = asList("Java", "Python", "C++");
        System.out.println("   Created list: " + list1);

        List<Integer> list2 = asList(1, 2, 3, 4, 5);
        System.out.println("   Created list: " + list2);

        // Varargs with arrays
        System.out.println("\n4. Varargs is actually an array:");
        String[] array = {"x", "y", "z"};
        printWithPrefix("Array: ", array); // Can pass array directly

        // Multiple varargs NOT allowed (compile error)
        // void invalid(String... a, int... b) {} // ERROR: varargs must be last

        System.out.println("\n5. Varargs rules:");
        System.out.println("   ✓ Must be last parameter");
        System.out.println("   ✓ Only one varargs per method");
        System.out.println("   ✓ Treated as array inside method");
        System.out.println("   ✓ Can pass array or individual values");
        System.out.println("   ✓ Can be empty (zero arguments)");

        // Safe/unsafe varargs
        System.out.println("\n6. Safe varargs with @SafeVarargs:");
        printAll("One", "Two", "Three");
        printAll(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));

        System.out.println();
    }

    // ========== Type Erasure ==========

    private static void typeErasureDemo() {
        System.out.println("--- Type Erasure ---");
        System.out.println("How generics work at runtime\n");

        System.out.println("1. Type erasure concept:");
        System.out.println("   ✓ Generics exist at compile time only");
        System.out.println("   ✓ Type information erased at runtime");
        System.out.println("   ✓ Replaced with bounds (Object or bound type)");

        Box<String> stringBox = new Box<>("Test");
        Box<Integer> intBox = new Box<>(123);

        System.out.println("\n2. Runtime type checking:");
        System.out.println("   stringBox class: " + stringBox.getClass());
        System.out.println("   intBox class: " + intBox.getClass());
        System.out.println("   Same class: " + (stringBox.getClass() == intBox.getClass()));

        // Cannot do: if (list instanceof List<String>) {} // ERROR
        // Can do: if (list instanceof List<?>) {} // OK

        System.out.println("\n3. Limitations due to type erasure:");
        System.out.println("   ✗ Cannot create generic array: new T[10]");
        System.out.println("   ✗ Cannot use instanceof with type parameter: obj instanceof T");
        System.out.println("   ✗ Cannot create instance: new T()");
        System.out.println("   ✗ Cannot have static field: static T field");
        System.out.println("   ✗ Cannot overload with erasure clash: void m(List<String>) and void m(List<Integer>)");

        System.out.println();
    }

    // ========== Generic Inheritance ==========

    private static void genericInheritanceDemo() {
        System.out.println("--- Generic Inheritance ---");
        System.out.println("Inheritance with generic types\n");

        // Extending generic class
        System.out.println("1. Extending generic class:");
        StringBox stringBox = new StringBox("Hello");
        System.out.println("   StringBox: " + stringBox.get());
        System.out.println("   Length: " + stringBox.length());

        // Generic subclass
        System.out.println("\n2. Generic subclass:");
        OrderedBox<Integer> orderedBox = new OrderedBox<>(42, 1);
        System.out.println("   Value: " + orderedBox.get());
        System.out.println("   Order: " + orderedBox.getOrder());

        // Type relationships
        System.out.println("\n3. Type relationships:");
        System.out.println("   List<String> is NOT a subtype of List<Object>");
        System.out.println("   Integer[] IS a subtype of Number[]");
        System.out.println("   Use wildcards for flexibility: List<? extends Number>");

        System.out.println();
    }

    // ========== Helper Classes and Methods ==========

    // Generic class with single type parameter
    static class Box<T> {
        private T value;

        public Box(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
        }
    }

    // Generic class with multiple type parameters
    static class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() { return key; }
        public V getValue() { return value; }

        @Override
        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }

    // Bounded generic class
    static class NumberBox<T extends Number> {
        private T value;

        public NumberBox(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }

        public double doubleValue() {
            return value.doubleValue();
        }
    }

    // Generic class inheritance
    static class StringBox extends Box<String> {
        public StringBox(String value) {
            super(value);
        }

        public int length() {
            return get().length();
        }
    }

    // Generic subclass
    static class OrderedBox<T> extends Box<T> {
        private int order;

        public OrderedBox(T value, int order) {
            super(value);
            this.order = order;
        }

        public int getOrder() {
            return order;
        }
    }

    // Generic method
    static <T> void printArray(T[] array) {
        System.out.print("   Array: [");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }

    // Generic method with return
    static <T> T getFirst(List<T> list) {
        return list.isEmpty() ? null : list.get(0);
    }

    // Multiple type parameters
    static <K, V> Pair<K, V> makePair(K key, V value) {
        return new Pair<>(key, value);
    }

    // Generic method with bound
    static <T extends Number> double sumNumbers(List<T> list) {
        double sum = 0;
        for (T num : list) {
            sum += num.doubleValue();
        }
        return sum;
    }

    // Multiple bounds
    static <T extends Comparable<T> & java.io.Serializable> T findMax(T a, T b, T c) {
        T max = a;
        if (b.compareTo(max) > 0) max = b;
        if (c.compareTo(max) > 0) max = c;
        return max;
    }

    // Wildcard methods
    static void printList(List<?> list) {
        System.out.println("   List: " + list);
    }

    // Upper bounded wildcard
    static double sumList(List<? extends Number> list) {
        double sum = 0;
        for (Number num : list) {
            sum += num.doubleValue();
        }
        return sum;
    }

    // Lower bounded wildcard
    static void addIntegers(List<? super Integer> list) {
        for (int i = 1; i <= 3; i++) {
            list.add(i);
        }
    }

    // Swap helper
    static <T> void swapFirst(List<T> list) {
        if (list.size() >= 2) {
            T temp = list.get(0);
            list.set(0, list.get(1));
            list.set(1, temp);
        }
    }

    // Reverse string
    static <T> T reverse(T input) {
        if (input instanceof String) {
            @SuppressWarnings("unchecked")
            T result = (T) new StringBuilder((String) input).reverse().toString();
            return result;
        }
        return input;
    }

    // Varargs methods
    static int sum(int... numbers) {
        int total = 0;
        for (int num : numbers) {
            total += num;
        }
        return total;
    }

    static <T> void printWithPrefix(String prefix, T... values) {
        System.out.print("   " + prefix);
        for (T value : values) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    @SafeVarargs
    static <T> List<T> asList(T... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

    @SafeVarargs
    static <T> void printAll(T... items) {
        System.out.print("   Items: ");
        for (T item : items) {
            System.out.print(item + " ");
        }
        System.out.println();
    }
}
