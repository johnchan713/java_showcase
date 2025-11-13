package com.example.demo.showcase;

import java.util.*;

/**
 * Demonstrates Object methods including hashCode, equals, comparisons, and toString
 */
public class ObjectMethodsShowcase {

    public static void demonstrate() {
        System.out.println("\n========== OBJECT METHODS SHOWCASE ==========\n");

        toStringDemo();
        equalsDemo();
        hashCodeDemo();
        comparableDemo();
        comparatorDemo();
        objectsUtilityDemo();
    }

    // ========== toString() ==========

    static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }

        public String getName() { return name; }
        public int getAge() { return age; }
    }

    private static void toStringDemo() {
        System.out.println("--- toString() Method ---");
        System.out.println("Purpose: String representation of object for debugging/logging");

        Person person = new Person("Alice", 25);
        System.out.println("With toString(): " + person);

        // Without toString override
        Object obj = new Object();
        System.out.println("Default toString(): " + obj);
        System.out.println("Format: ClassName@HashCodeInHex");

        // Arrays
        int[] array = {1, 2, 3};
        System.out.println("Array default toString(): " + array);
        System.out.println("Arrays.toString(): " + Arrays.toString(array));

        System.out.println();
    }

    // ========== equals() and == ==========

    static class Book {
        private String title;
        private String author;

        public Book(String title, String author) {
            this.title = title;
            this.author = author;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true; // Same reference
            if (obj == null || getClass() != obj.getClass()) return false;

            Book book = (Book) obj;
            return Objects.equals(title, book.title) &&
                   Objects.equals(author, book.author);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, author);
        }

        @Override
        public String toString() {
            return "Book{'" + title + "' by " + author + "}";
        }
    }

    private static void equalsDemo() {
        System.out.println("--- equals() vs == ---");
        System.out.println("== : Compares references (memory addresses)");
        System.out.println("equals(): Compares object content (when overridden)");

        // String comparison
        String str1 = new String("Hello");
        String str2 = new String("Hello");
        String str3 = str1;

        System.out.println("\nString comparison:");
        System.out.println("str1 == str2: " + (str1 == str2) + " (different objects)");
        System.out.println("str1.equals(str2): " + str1.equals(str2) + " (same content)");
        System.out.println("str1 == str3: " + (str1 == str3) + " (same reference)");

        // Integer comparison
        Integer int1 = new Integer(100);
        Integer int2 = new Integer(100);
        Integer int3 = 100; // Autoboxing, cached for -128 to 127
        Integer int4 = 100;

        System.out.println("\nInteger comparison:");
        System.out.println("new Integer(100) == new Integer(100): " + (int1 == int2));
        System.out.println("new Integer(100).equals(new Integer(100)): " + int1.equals(int2));
        System.out.println("100 == 100 (cached): " + (int3 == int4));

        // Custom object comparison
        Book book1 = new Book("1984", "Orwell");
        Book book2 = new Book("1984", "Orwell");
        Book book3 = book1;

        System.out.println("\nCustom object (Book) comparison:");
        System.out.println("book1 == book2: " + (book1 == book2) + " (different objects)");
        System.out.println("book1.equals(book2): " + book1.equals(book2) + " (same content)");
        System.out.println("book1 == book3: " + (book1 == book3) + " (same reference)");

        System.out.println("\nKey Points:");
        System.out.println("- Always override equals() when overriding hashCode()");
        System.out.println("- equals() must be: reflexive, symmetric, transitive, consistent");
        System.out.println("- Use Objects.equals() for null-safe comparison");

        System.out.println();
    }

    // ========== hashCode() ==========

    private static void hashCodeDemo() {
        System.out.println("--- hashCode() Method ---");
        System.out.println("Purpose: Generate integer hash for hash-based collections");
        System.out.println("Contract: Equal objects must have equal hash codes");

        Book book1 = new Book("1984", "Orwell");
        Book book2 = new Book("1984", "Orwell");
        Book book3 = new Book("Animal Farm", "Orwell");

        System.out.println("\nHashCode examples:");
        System.out.println("book1 hashCode: " + book1.hashCode());
        System.out.println("book2 hashCode: " + book2.hashCode() + " (equal to book1)");
        System.out.println("book3 hashCode: " + book3.hashCode() + " (different)");

        // HashMap usage
        HashMap<Book, Integer> bookStock = new HashMap<>();
        bookStock.put(book1, 10);
        bookStock.put(book3, 5);

        System.out.println("\nHashMap with Book keys:");
        System.out.println("Stock of " + book1 + ": " + bookStock.get(book2));
        System.out.println("(book2 finds book1's value due to equals/hashCode)");

        // String hashCode
        String str1 = "Hello";
        String str2 = "Hello";
        System.out.println("\nString hashCodes:");
        System.out.println("'Hello' hashCode: " + str1.hashCode());
        System.out.println("'Hello' hashCode: " + str2.hashCode() + " (same)");

        // HashCode for primitives
        System.out.println("\nPrimitive wrapper hashCodes:");
        System.out.println("Integer(42): " + Integer.valueOf(42).hashCode());
        System.out.println("Long(42L): " + Long.valueOf(42L).hashCode());
        System.out.println("Double(3.14): " + Double.valueOf(3.14).hashCode());

        // Objects.hash utility
        int hash = Objects.hash("Title", "Author", 2024);
        System.out.println("Objects.hash('Title', 'Author', 2024): " + hash);

        System.out.println("\nBest Practices:");
        System.out.println("- Use Objects.hash() for combining multiple fields");
        System.out.println("- Include all fields used in equals()");
        System.out.println("- Must be consistent with equals()");

        System.out.println();
    }

    // ========== Comparable ==========

    static class Student implements Comparable<Student> {
        private String name;
        private int score;

        public Student(String name, int score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public int compareTo(Student other) {
            // Natural ordering by score (descending)
            return Integer.compare(other.score, this.score);
        }

        @Override
        public String toString() {
            return name + "(" + score + ")";
        }

        public String getName() { return name; }
        public int getScore() { return score; }
    }

    private static void comparableDemo() {
        System.out.println("--- Comparable Interface ---");
        System.out.println("Purpose: Define natural ordering for objects");
        System.out.println("Method: int compareTo(T other)");
        System.out.println("Return: negative (less), 0 (equal), positive (greater)");

        List<Student> students = new ArrayList<>();
        students.add(new Student("Alice", 85));
        students.add(new Student("Bob", 92));
        students.add(new Student("Charlie", 78));
        students.add(new Student("David", 92));

        System.out.println("\nBefore sorting: " + students);

        Collections.sort(students); // Uses compareTo
        System.out.println("After sorting (by score desc): " + students);

        // TreeSet uses Comparable
        TreeSet<Student> sortedSet = new TreeSet<>(students);
        System.out.println("TreeSet (natural order): " + sortedSet);

        // Binary search requires sorted list
        Student searchKey = new Student("", 92);
        int index = Collections.binarySearch(students, searchKey);
        System.out.println("Binary search for score 92: index " + index);

        System.out.println("\nComparable Best Practices:");
        System.out.println("- Consistent with equals: compareTo==0 iff equals()==true");
        System.out.println("- Use Integer.compare(), Double.compare() for primitives");
        System.out.println("- Natural ordering should be intuitive");

        System.out.println();
    }

    // ========== Comparator ==========

    private static void comparatorDemo() {
        System.out.println("--- Comparator Interface ---");
        System.out.println("Purpose: Define custom ordering separate from class");
        System.out.println("Method: int compare(T o1, T o2)");
        System.out.println("Advantage: Multiple orderings, external to class");

        List<Student> students = new ArrayList<>();
        students.add(new Student("Alice", 85));
        students.add(new Student("Bob", 92));
        students.add(new Student("Charlie", 78));

        // Sort by name
        Comparator<Student> byName = (s1, s2) -> s1.getName().compareTo(s2.getName());
        students.sort(byName);
        System.out.println("Sorted by name: " + students);

        // Sort by score (ascending)
        Comparator<Student> byScoreAsc = Comparator.comparingInt(Student::getScore);
        students.sort(byScoreAsc);
        System.out.println("Sorted by score (asc): " + students);

        // Sort by score (descending)
        Comparator<Student> byScoreDesc = Comparator.comparingInt(Student::getScore).reversed();
        students.sort(byScoreDesc);
        System.out.println("Sorted by score (desc): " + students);

        // Chained comparators
        Comparator<Student> byScoreThenName = Comparator
            .comparingInt(Student::getScore)
            .thenComparing(Student::getName);
        students.add(new Student("Amy", 92));
        students.sort(byScoreThenName);
        System.out.println("Sorted by score then name: " + students);

        // Null-safe comparators
        Comparator<Student> nullSafe = Comparator.nullsFirst(byName);
        List<Student> withNull = new ArrayList<>(students);
        withNull.add(null);
        withNull.sort(nullSafe);
        System.out.println("With null (nullsFirst): " + withNull);

        // TreeSet with Comparator
        TreeSet<Student> customSet = new TreeSet<>(byName);
        customSet.addAll(students);
        System.out.println("TreeSet with name comparator: " + customSet);

        System.out.println("\nComparator Methods:");
        System.out.println("- comparing(), comparingInt/Long/Double()");
        System.out.println("- thenComparing() for chaining");
        System.out.println("- reversed() for reverse order");
        System.out.println("- nullsFirst/Last() for null handling");

        System.out.println();
    }

    // ========== Objects Utility Class ==========

    private static void objectsUtilityDemo() {
        System.out.println("--- Objects Utility Class (java.util.Objects) ---");
        System.out.println("Purpose: Null-safe operations and utilities");

        String str1 = "Hello";
        String str2 = null;

        // equals (null-safe)
        System.out.println("Objects.equals('Hello', 'Hello'): " + Objects.equals(str1, str1));
        System.out.println("Objects.equals('Hello', null): " + Objects.equals(str1, str2));
        System.out.println("Objects.equals(null, null): " + Objects.equals(str2, str2));

        // deepEquals (for arrays)
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        System.out.println("Objects.deepEquals(arr1, arr2): " + Objects.deepEquals(arr1, arr2));

        // hash and hashCode
        System.out.println("Objects.hash('a', 1, true): " + Objects.hash("a", 1, true));
        System.out.println("Objects.hashCode('Hello'): " + Objects.hashCode(str1));
        System.out.println("Objects.hashCode(null): " + Objects.hashCode(str2));

        // toString
        System.out.println("Objects.toString('Hello'): " + Objects.toString(str1));
        System.out.println("Objects.toString(null, 'default'): " + Objects.toString(str2, "default"));

        // requireNonNull (validation)
        try {
            Objects.requireNonNull(str2, "String must not be null");
        } catch (NullPointerException e) {
            System.out.println("requireNonNull threw: " + e.getMessage());
        }

        // isNull and nonNull (predicates)
        System.out.println("Objects.isNull(null): " + Objects.isNull(str2));
        System.out.println("Objects.nonNull('Hello'): " + Objects.nonNull(str1));

        // compare (null-safe comparison)
        Integer int1 = 10;
        Integer int2 = 20;
        System.out.println("Objects.compare(10, 20, naturalOrder): " +
            Objects.compare(int1, int2, Comparator.naturalOrder()));

        System.out.println("\nObjects Utility Methods:");
        System.out.println("- equals(), deepEquals() - null-safe equality");
        System.out.println("- hash(), hashCode() - hash code generation");
        System.out.println("- toString() - null-safe string conversion");
        System.out.println("- requireNonNull() - null validation");
        System.out.println("- isNull(), nonNull() - null checks");
        System.out.println("- compare() - null-safe comparison");

        System.out.println();
    }
}
