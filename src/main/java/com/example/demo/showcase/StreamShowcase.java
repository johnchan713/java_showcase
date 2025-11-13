package com.example.demo.showcase;

import java.util.*;
import java.util.stream.*;

/**
 * Demonstrates Java Stream API including map, filter, reduce, peek, parallelStream, etc.
 */
public class StreamShowcase {

    public static void demonstrate() {
        System.out.println("\n========== STREAM SHOWCASE ==========\n");

        creatingStreams();
        intermediateOperations();
        terminalOperations();
        mapOperations();
        filterOperations();
        reduceOperations();
        peekOperation();
        parallelStreams();
        advancedStreamOperations();
        collectorsDemo();
    }

    // ========== Creating Streams ==========

    private static void creatingStreams() {
        System.out.println("--- Creating Streams ---");

        // From collection
        List<String> list = Arrays.asList("A", "B", "C");
        Stream<String> streamFromList = list.stream();
        System.out.println("From List: " + streamFromList.collect(Collectors.toList()));

        // From array
        String[] array = {"X", "Y", "Z"};
        Stream<String> streamFromArray = Arrays.stream(array);
        System.out.println("From Array: " + streamFromArray.collect(Collectors.toList()));

        // Using Stream.of()
        Stream<Integer> streamOf = Stream.of(1, 2, 3, 4, 5);
        System.out.println("Stream.of(): " + streamOf.collect(Collectors.toList()));

        // Empty stream
        Stream<String> emptyStream = Stream.empty();
        System.out.println("Empty stream count: " + emptyStream.count());

        // Infinite streams
        Stream<Integer> limitedStream = Stream.iterate(0, n -> n + 2).limit(5);
        System.out.println("Iterate: " + limitedStream.collect(Collectors.toList()));

        Stream<Double> randomStream = Stream.generate(Math::random).limit(3);
        System.out.println("Generate (random): " + randomStream.collect(Collectors.toList()));

        // Range streams
        IntStream range = IntStream.range(1, 5); // 1 to 4
        System.out.println("IntStream.range(1, 5): " + range.boxed().collect(Collectors.toList()));

        IntStream rangeClosed = IntStream.rangeClosed(1, 5); // 1 to 5
        System.out.println("IntStream.rangeClosed(1, 5): " + rangeClosed.boxed().collect(Collectors.toList()));

        System.out.println();
    }

    // ========== Intermediate Operations ==========

    private static void intermediateOperations() {
        System.out.println("--- Intermediate Operations ---");

        List<String> words = Arrays.asList("apple", "banana", "cherry", "date", "elderberry");

        // filter
        List<String> filtered = words.stream()
            .filter(w -> w.length() > 5)
            .collect(Collectors.toList());
        System.out.println("Filtered (length > 5): " + filtered);

        // map
        List<Integer> lengths = words.stream()
            .map(String::length)
            .collect(Collectors.toList());
        System.out.println("Mapped to lengths: " + lengths);

        // flatMap
        List<List<Integer>> nestedList = Arrays.asList(
            Arrays.asList(1, 2, 3),
            Arrays.asList(4, 5),
            Arrays.asList(6, 7, 8, 9)
        );
        List<Integer> flattened = nestedList.stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
        System.out.println("FlatMap: " + flattened);

        // distinct
        List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 5, 5);
        List<Integer> distinct = numbers.stream()
            .distinct()
            .collect(Collectors.toList());
        System.out.println("Distinct: " + distinct);

        // sorted
        List<String> sorted = words.stream()
            .sorted()
            .collect(Collectors.toList());
        System.out.println("Sorted: " + sorted);

        List<String> sortedReverse = words.stream()
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
        System.out.println("Sorted reverse: " + sortedReverse);

        // limit and skip
        List<String> limited = words.stream()
            .limit(3)
            .collect(Collectors.toList());
        System.out.println("Limit(3): " + limited);

        List<String> skipped = words.stream()
            .skip(2)
            .collect(Collectors.toList());
        System.out.println("Skip(2): " + skipped);

        System.out.println();
    }

    // ========== Terminal Operations ==========

    private static void terminalOperations() {
        System.out.println("--- Terminal Operations ---");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // forEach
        System.out.print("forEach: ");
        numbers.stream().forEach(n -> System.out.print(n + " "));
        System.out.println();

        // count
        long count = numbers.stream().filter(n -> n > 5).count();
        System.out.println("Count (> 5): " + count);

        // anyMatch, allMatch, noneMatch
        boolean anyEven = numbers.stream().anyMatch(n -> n % 2 == 0);
        boolean allPositive = numbers.stream().allMatch(n -> n > 0);
        boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);
        System.out.println("Any even: " + anyEven);
        System.out.println("All positive: " + allPositive);
        System.out.println("None negative: " + noneNegative);

        // findFirst, findAny
        Optional<Integer> first = numbers.stream().findFirst();
        Optional<Integer> any = numbers.stream().findAny();
        System.out.println("Find first: " + first.orElse(-1));
        System.out.println("Find any: " + any.orElse(-1));

        // min, max
        Optional<Integer> min = numbers.stream().min(Integer::compareTo);
        Optional<Integer> max = numbers.stream().max(Integer::compareTo);
        System.out.println("Min: " + min.orElse(-1));
        System.out.println("Max: " + max.orElse(-1));

        // toArray
        Integer[] array = numbers.stream().toArray(Integer[]::new);
        System.out.println("To array length: " + array.length);

        System.out.println();
    }

    // ========== Map Operations ==========

    private static void mapOperations() {
        System.out.println("--- Map Operations ---");

        List<String> words = Arrays.asList("hello", "world", "java", "stream");

        // map - transform to different type
        List<Integer> lengths = words.stream()
            .map(String::length)
            .collect(Collectors.toList());
        System.out.println("Map to lengths: " + lengths);

        // map - transform to uppercase
        List<String> upper = words.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        System.out.println("Map to uppercase: " + upper);

        // mapToInt, mapToDouble, mapToLong
        int totalLength = words.stream()
            .mapToInt(String::length)
            .sum();
        System.out.println("Total length (mapToInt + sum): " + totalLength);

        double average = words.stream()
            .mapToInt(String::length)
            .average()
            .orElse(0.0);
        System.out.println("Average length: " + average);

        // flatMap - flatten nested structures
        List<String> sentences = Arrays.asList("Hello World", "Java Stream", "Flat Map");
        List<String> allWords = sentences.stream()
            .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
            .collect(Collectors.toList());
        System.out.println("FlatMap words: " + allWords);

        // flatMapToInt
        List<String> numbers = Arrays.asList("1,2,3", "4,5", "6,7,8");
        int sum = numbers.stream()
            .flatMapToInt(s -> Arrays.stream(s.split(","))
                .mapToInt(Integer::parseInt))
            .sum();
        System.out.println("FlatMapToInt sum: " + sum);

        System.out.println();
    }

    // ========== Filter Operations ==========

    private static void filterOperations() {
        System.out.println("--- Filter Operations ---");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Simple filter
        List<Integer> evens = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println("Even numbers: " + evens);

        // Multiple filters
        List<Integer> filtered = numbers.stream()
            .filter(n -> n > 3)
            .filter(n -> n < 8)
            .filter(n -> n % 2 != 0)
            .collect(Collectors.toList());
        System.out.println("Multiple filters (>3, <8, odd): " + filtered);

        // Filter with complex condition
        List<String> words = Arrays.asList("apple", "banana", "cherry", "date", "elderberry", "fig");
        List<String> longWords = words.stream()
            .filter(w -> w.length() > 5 && w.contains("e"))
            .collect(Collectors.toList());
        System.out.println("Long words with 'e': " + longWords);

        // takeWhile and dropWhile (Java 9+)
        List<Integer> takeWhileResult = numbers.stream()
            .takeWhile(n -> n < 6)
            .collect(Collectors.toList());
        System.out.println("TakeWhile (< 6): " + takeWhileResult);

        List<Integer> dropWhileResult = numbers.stream()
            .dropWhile(n -> n < 6)
            .collect(Collectors.toList());
        System.out.println("DropWhile (< 6): " + dropWhileResult);

        System.out.println();
    }

    // ========== Reduce Operations ==========

    private static void reduceOperations() {
        System.out.println("--- Reduce Operations ---");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // Reduce with identity
        int sum = numbers.stream()
            .reduce(0, (a, b) -> a + b);
        System.out.println("Sum (reduce): " + sum);

        int product = numbers.stream()
            .reduce(1, (a, b) -> a * b);
        System.out.println("Product (reduce): " + product);

        // Reduce without identity
        Optional<Integer> max = numbers.stream()
            .reduce((a, b) -> a > b ? a : b);
        System.out.println("Max (reduce): " + max.orElse(-1));

        Optional<Integer> min = numbers.stream()
            .reduce(Integer::min);
        System.out.println("Min (reduce): " + min.orElse(-1));

        // Reduce with strings
        List<String> words = Arrays.asList("Java", "Stream", "API");
        String concatenated = words.stream()
            .reduce("", (a, b) -> a + b);
        System.out.println("Concatenated: " + concatenated);

        String joined = words.stream()
            .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);
        System.out.println("Joined: " + joined);

        // Three-argument reduce (for parallel processing)
        int parallelSum = numbers.parallelStream()
            .reduce(0,
                (a, b) -> a + b,
                (a, b) -> a + b);
        System.out.println("Parallel sum: " + parallelSum);

        System.out.println();
    }

    // ========== Peek Operation ==========

    private static void peekOperation() {
        System.out.println("--- Peek Operation ---");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // Peek for debugging
        System.out.println("Using peek for debugging:");
        List<Integer> result = numbers.stream()
            .peek(n -> System.out.println("  Original: " + n))
            .map(n -> n * 2)
            .peek(n -> System.out.println("  After map: " + n))
            .filter(n -> n > 5)
            .peek(n -> System.out.println("  After filter: " + n))
            .collect(Collectors.toList());
        System.out.println("Final result: " + result);

        // Peek to modify external state (side effect - use carefully)
        List<String> processed = new ArrayList<>();
        List<String> words = Arrays.asList("apple", "banana", "cherry");
        words.stream()
            .peek(w -> processed.add("Processed: " + w))
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        System.out.println("Processed list: " + processed);

        System.out.println();
    }

    // ========== Parallel Streams ==========

    private static void parallelStreams() {
        System.out.println("--- Parallel Streams ---");

        List<Integer> numbers = IntStream.rangeClosed(1, 10)
            .boxed()
            .collect(Collectors.toList());

        // Sequential stream
        long startSeq = System.nanoTime();
        int sumSeq = numbers.stream()
            .map(n -> n * 2)
            .reduce(0, Integer::sum);
        long endSeq = System.nanoTime();
        System.out.println("Sequential sum: " + sumSeq + " (time: " + (endSeq - startSeq) / 1000 + " μs)");

        // Parallel stream
        long startPar = System.nanoTime();
        int sumPar = numbers.parallelStream()
            .map(n -> n * 2)
            .reduce(0, Integer::sum);
        long endPar = System.nanoTime();
        System.out.println("Parallel sum: " + sumPar + " (time: " + (endPar - startPar) / 1000 + " μs)");

        // Converting to parallel
        long count = IntStream.range(1, 1000)
            .parallel()
            .filter(n -> n % 2 == 0)
            .count();
        System.out.println("Parallel count of evens: " + count);

        // Check if parallel
        boolean isParallel = numbers.parallelStream().isParallel();
        System.out.println("Is parallel: " + isParallel);

        // Convert back to sequential
        List<Integer> result = numbers.parallelStream()
            .sequential()
            .collect(Collectors.toList());
        System.out.println("Sequential result: " + result);

        System.out.println();
    }

    // ========== Advanced Stream Operations ==========

    private static void advancedStreamOperations() {
        System.out.println("--- Advanced Stream Operations ---");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Chaining multiple operations
        List<Integer> processed = numbers.stream()
            .filter(n -> n % 2 == 0)
            .map(n -> n * n)
            .sorted(Comparator.reverseOrder())
            .limit(3)
            .collect(Collectors.toList());
        System.out.println("Chained operations: " + processed);

        // IntStream operations
        IntSummaryStatistics stats = IntStream.rangeClosed(1, 100)
            .summaryStatistics();
        System.out.println("Statistics: " + stats);
        System.out.println("  Count: " + stats.getCount());
        System.out.println("  Sum: " + stats.getSum());
        System.out.println("  Min: " + stats.getMin());
        System.out.println("  Max: " + stats.getMax());
        System.out.println("  Average: " + stats.getAverage());

        // Grouping with streams (covered in collectors)
        Map<Boolean, List<Integer>> partitioned = numbers.stream()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("Partitioned: " + partitioned);

        System.out.println();
    }

    // ========== Collectors Demo ==========

    private static void collectorsDemo() {
        System.out.println("--- Collectors ---");

        List<String> words = Arrays.asList("apple", "banana", "cherry", "date", "elderberry", "apple");

        // toList, toSet
        List<String> list = words.stream().collect(Collectors.toList());
        Set<String> set = words.stream().collect(Collectors.toSet());
        System.out.println("ToList: " + list);
        System.out.println("ToSet (unique): " + set);

        // joining
        String joined = words.stream().collect(Collectors.joining());
        System.out.println("Joining: " + joined);

        String joinedComma = words.stream().collect(Collectors.joining(", "));
        System.out.println("Joining with comma: " + joinedComma);

        String joinedFull = words.stream()
            .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("Joining with prefix/suffix: " + joinedFull);

        // counting
        long count = words.stream().collect(Collectors.counting());
        System.out.println("Counting: " + count);

        // summarizing
        IntSummaryStatistics lengthStats = words.stream()
            .collect(Collectors.summarizingInt(String::length));
        System.out.println("Length statistics: " + lengthStats);

        // groupingBy
        Map<Integer, List<String>> groupedByLength = words.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println("Grouped by length: " + groupedByLength);

        Map<Character, List<String>> groupedByFirst = words.stream()
            .collect(Collectors.groupingBy(s -> s.charAt(0)));
        System.out.println("Grouped by first char: " + groupedByFirst);

        // partitioningBy
        Map<Boolean, List<String>> partitioned = words.stream()
            .collect(Collectors.partitioningBy(s -> s.length() > 5));
        System.out.println("Partitioned (length > 5): " + partitioned);

        // toMap
        Map<String, Integer> wordLengths = words.stream()
            .distinct()
            .collect(Collectors.toMap(s -> s, String::length));
        System.out.println("ToMap: " + wordLengths);

        System.out.println();
    }
}
