package com.example.demo.showcase;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates basic Java syntax including printing and loops
 */
public class BasicSyntaxShowcase {

    public static void demonstrate() {
        System.out.println("\n========== BASIC SYNTAX SHOWCASE ==========\n");

        printingExamples();
        loopExamples();
        rangeBasedLoops();
    }

    /**
     * Various printing methods in Java
     */
    private static void printingExamples() {
        System.out.println("--- Printing Examples ---");

        // Basic print
        System.out.print("Hello ");
        System.out.print("World");
        System.out.println("!");

        // Formatted printing
        System.out.printf("Formatted: %s, Number: %d, Float: %.2f%n", "text", 42, 3.14159);

        // String formatting
        String formatted = String.format("Name: %s, Age: %d", "John", 30);
        System.out.println(formatted);

        // Print with variables
        int number = 100;
        String text = "Java";
        System.out.println("Variable: " + text + ", Number: " + number);

        // Text blocks (Java 15+)
        String textBlock = """
                Multi-line string
                Line 2
                Line 3
                """;
        System.out.println("Text Block:\n" + textBlock);

        System.out.println();
    }

    /**
     * Traditional for and while loops
     */
    private static void loopExamples() {
        System.out.println("--- Loop Examples ---");

        // Traditional for loop
        System.out.print("For loop: ");
        for (int i = 0; i < 5; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        // While loop
        System.out.print("While loop: ");
        int count = 0;
        while (count < 5) {
            System.out.print(count + " ");
            count++;
        }
        System.out.println();

        // Do-while loop
        System.out.print("Do-while loop: ");
        int num = 0;
        do {
            System.out.print(num + " ");
            num++;
        } while (num < 5);
        System.out.println();

        // Nested loops
        System.out.println("Nested loops (multiplication table):");
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                System.out.printf("%d*%d=%d ", i, j, i * j);
            }
            System.out.println();
        }

        // Loop with break
        System.out.print("Loop with break: ");
        for (int i = 0; i < 10; i++) {
            if (i == 5) break;
            System.out.print(i + " ");
        }
        System.out.println();

        // Loop with continue
        System.out.print("Loop with continue (skip even): ");
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) continue;
            System.out.print(i + " ");
        }
        System.out.println();

        System.out.println();
    }

    /**
     * Range-based loops (for-each) and enhanced iterations
     */
    private static void rangeBasedLoops() {
        System.out.println("--- Range-Based Loops ---");

        // Array iteration
        int[] numbers = {10, 20, 30, 40, 50};
        System.out.print("Array iteration: ");
        for (int num : numbers) {
            System.out.print(num + " ");
        }
        System.out.println();

        // List iteration
        List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry");
        System.out.print("List iteration: ");
        for (String fruit : fruits) {
            System.out.print(fruit + " ");
        }
        System.out.println();

        // Array with index (traditional way)
        System.out.print("Array with index: ");
        for (int i = 0; i < numbers.length; i++) {
            System.out.printf("[%d]=%d ", i, numbers[i]);
        }
        System.out.println();

        // List with forEach and lambda (Java 8+)
        System.out.print("List forEach with lambda: ");
        fruits.forEach(fruit -> System.out.print(fruit + " "));
        System.out.println();

        // List with forEach and method reference
        System.out.print("List forEach with method reference: ");
        fruits.forEach(System.out::print);
        System.out.println();

        // Iterating over 2D array
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        System.out.println("2D array iteration:");
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }

        System.out.println();
    }
}
