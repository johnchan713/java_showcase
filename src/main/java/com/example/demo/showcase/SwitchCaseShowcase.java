package com.example.demo.showcase;

/**
 * Demonstrates switch-case statements including old and new syntax,
 * pattern matching, and switch with strings and objects
 */
public class SwitchCaseShowcase {

    public static void demonstrate() {
        System.out.println("\n========== SWITCH CASE SHOWCASE ==========\n");

        traditionalSwitch();
        switchWithStrings();
        newSwitchExpression();
        switchWithPatternMatching();
        switchWithObjects();
    }

    /**
     * Traditional switch statement with break
     */
    private static void traditionalSwitch() {
        System.out.println("--- Traditional Switch ---");

        int day = 3;
        String dayName;
        switch (day) {
            case 1:
                dayName = "Monday";
                break;
            case 2:
                dayName = "Tuesday";
                break;
            case 3:
                dayName = "Wednesday";
                break;
            case 4:
                dayName = "Thursday";
                break;
            case 5:
                dayName = "Friday";
                break;
            case 6:
            case 7:
                dayName = "Weekend";
                break;
            default:
                dayName = "Invalid day";
                break;
        }
        System.out.println("Day " + day + " is: " + dayName);

        // Switch with fall-through
        int month = 2;
        System.out.print("Month " + month + " has ");
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                System.out.println("31 days");
                break;
            case 4: case 6: case 9: case 11:
                System.out.println("30 days");
                break;
            case 2:
                System.out.println("28/29 days");
                break;
            default:
                System.out.println("Invalid month");
        }

        System.out.println();
    }

    /**
     * Switch with String (Java 7+)
     */
    private static void switchWithStrings() {
        System.out.println("--- Switch with Strings ---");

        String color = "RED";
        String colorCode;
        switch (color) {
            case "RED":
                colorCode = "#FF0000";
                break;
            case "GREEN":
                colorCode = "#00FF00";
                break;
            case "BLUE":
                colorCode = "#0000FF";
                break;
            default:
                colorCode = "#000000";
                break;
        }
        System.out.println(color + " color code: " + colorCode);

        // Switch with enum
        enum Season { SPRING, SUMMER, FALL, WINTER }
        Season season = Season.SUMMER;
        String activity;
        switch (season) {
            case SPRING:
                activity = "Plant flowers";
                break;
            case SUMMER:
                activity = "Go swimming";
                break;
            case FALL:
                activity = "Collect leaves";
                break;
            case WINTER:
                activity = "Build snowman";
                break;
            default:
                activity = "Unknown";
        }
        System.out.println("Season " + season + ": " + activity);

        System.out.println();
    }

    /**
     * New switch expression (Java 14+)
     */
    private static void newSwitchExpression() {
        System.out.println("--- New Switch Expression (Java 14+) ---");

        // Switch expression with arrow syntax
        int day = 3;
        String dayType = switch (day) {
            case 1, 2, 3, 4, 5 -> "Weekday";
            case 6, 7 -> "Weekend";
            default -> "Invalid";
        };
        System.out.println("Day " + day + " type: " + dayType);

        // Switch expression with yield
        String grade = "B";
        int score = switch (grade) {
            case "A" -> 90;
            case "B" -> 80;
            case "C" -> 70;
            case "D" -> {
                System.out.println("  Need improvement!");
                yield 60;
            }
            default -> {
                System.out.println("  Invalid grade!");
                yield 0;
            }
        };
        System.out.println("Grade " + grade + " score: " + score);

        // Switch with multiple statements
        int number = 5;
        String result = switch (number) {
            case 1, 2, 3 -> "Small";
            case 4, 5, 6 -> {
                String category = "Medium";
                yield category + " sized";
            }
            case 7, 8, 9 -> "Large";
            default -> "Out of range";
        };
        System.out.println("Number " + number + " is: " + result);

        System.out.println();
    }

    /**
     * Switch with pattern matching (Java 21+)
     */
    private static void switchWithPatternMatching() {
        System.out.println("--- Switch with Pattern Matching (Java 21+) ---");

        // Pattern matching with type patterns
        Object obj = "Hello";
        String description = switch (obj) {
            case Integer i -> "Integer: " + i;
            case String s -> "String of length " + s.length() + ": " + s;
            case Double d -> "Double: " + d;
            case null -> "Null value";
            default -> "Unknown type: " + obj.getClass().getSimpleName();
        };
        System.out.println(description);

        // Pattern matching with guards
        Object value = 42;
        String category = switch (value) {
            case Integer i when i < 0 -> "Negative integer";
            case Integer i when i == 0 -> "Zero";
            case Integer i when i > 0 && i <= 10 -> "Small positive integer";
            case Integer i -> "Large positive integer";
            case String s when s.isEmpty() -> "Empty string";
            case String s -> "Non-empty string";
            case null -> "Null";
            default -> "Other type";
        };
        System.out.println("Value " + value + ": " + category);

        // Multiple values with pattern matching
        demonstratePatternMatchingWithGuards(100);
        demonstratePatternMatchingWithGuards("Test");
        demonstratePatternMatchingWithGuards(-5);

        System.out.println();
    }

    private static void demonstratePatternMatchingWithGuards(Object obj) {
        String result = switch (obj) {
            case Integer i when i > 100 -> "Large number: " + i;
            case Integer i when i > 0 -> "Positive number: " + i;
            case Integer i when i < 0 -> "Negative number: " + i;
            case Integer i -> "Zero";
            case String s when s.length() > 5 -> "Long string";
            case String s -> "Short string: " + s;
            case null -> "Null";
            default -> "Other";
        };
        System.out.println("  " + obj + " -> " + result);
    }

    /**
     * Switch with instanceof pattern and objects
     */
    private static void switchWithObjects() {
        System.out.println("--- Switch with Objects and instanceof ---");

        // Using instanceof in traditional way
        Object item = "Java";
        if (item instanceof String str) {
            System.out.println("instanceof String: " + str.toUpperCase());
        } else if (item instanceof Integer num) {
            System.out.println("instanceof Integer: " + num);
        }

        // Using switch with pattern matching (better approach)
        processObject("Hello World");
        processObject(123);
        processObject(45.67);
        processObject(true);
        processObject(null);

        // Switch with record patterns
        record Point(int x, int y) {}
        Object shape = new Point(10, 20);
        String shapeInfo = switch (shape) {
            case Point(int x, int y) -> "Point at (" + x + ", " + y + ")";
            case null -> "No shape";
            default -> "Unknown shape";
        };
        System.out.println(shapeInfo);

        System.out.println();
    }

    private static void processObject(Object obj) {
        String result = switch (obj) {
            case String s -> "String: '" + s + "' (length: " + s.length() + ")";
            case Integer i -> "Integer: " + i + " (double: " + (i * 2) + ")";
            case Double d -> "Double: " + d + " (rounded: " + Math.round(d) + ")";
            case Boolean b -> "Boolean: " + b + " (negated: " + !b + ")";
            case null -> "Null object";
            default -> "Unknown type: " + obj.getClass().getName();
        };
        System.out.println("  " + result);
    }
}
