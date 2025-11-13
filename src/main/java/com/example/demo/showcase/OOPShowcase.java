package com.example.demo.showcase;

/**
 * Demonstrates Object-Oriented Programming concepts in Java
 * Including classes, inheritance, abstract classes, interfaces, and instanceof
 */
public class OOPShowcase {

    public static void demonstrate() {
        System.out.println("\n========== OOP SHOWCASE ==========\n");

        classesAndObjects();
        inheritanceDemo();
        abstractClassDemo();
        interfaceDemo();
        instanceofDemo();
        polymorphismDemo();
    }

    // ========== Classes and Objects ==========

    static class Person {
        private String name;
        private int age;

        // Constructor
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        // Method
        public void introduce() {
            System.out.println("Hi, I'm " + name + " and I'm " + age + " years old.");
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }

    private static void classesAndObjects() {
        System.out.println("--- Classes and Objects ---");

        Person person1 = new Person("Alice", 25);
        Person person2 = new Person("Bob", 30);

        person1.introduce();
        person2.introduce();

        System.out.println("person1: " + person1);
        System.out.println("person2: " + person2);

        System.out.println();
    }

    // ========== Inheritance ==========

    static class Animal {
        protected String name;
        protected int age;

        public Animal(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public void makeSound() {
            System.out.println(name + " makes a sound");
        }

        public void eat() {
            System.out.println(name + " is eating");
        }
    }

    static class Dog extends Animal {
        private String breed;

        public Dog(String name, int age, String breed) {
            super(name, age); // Call parent constructor
            this.breed = breed;
        }

        @Override
        public void makeSound() {
            System.out.println(name + " barks: Woof! Woof!");
        }

        public void fetch() {
            System.out.println(name + " is fetching the ball");
        }

        public String getBreed() {
            return breed;
        }
    }

    static class Cat extends Animal {
        private boolean indoor;

        public Cat(String name, int age, boolean indoor) {
            super(name, age);
            this.indoor = indoor;
        }

        @Override
        public void makeSound() {
            System.out.println(name + " meows: Meow!");
        }

        public void scratch() {
            System.out.println(name + " is scratching");
        }

        public boolean isIndoor() {
            return indoor;
        }
    }

    private static void inheritanceDemo() {
        System.out.println("--- Inheritance ---");

        Dog dog = new Dog("Buddy", 3, "Golden Retriever");
        Cat cat = new Cat("Whiskers", 2, true);

        dog.makeSound();
        dog.eat();
        dog.fetch();
        System.out.println("Breed: " + dog.getBreed());

        cat.makeSound();
        cat.eat();
        cat.scratch();
        System.out.println("Indoor cat: " + cat.isIndoor());

        System.out.println();
    }

    // ========== Abstract Classes ==========

    abstract static class Shape {
        protected String color;

        public Shape(String color) {
            this.color = color;
        }

        // Abstract method - must be implemented by subclasses
        public abstract double getArea();

        public abstract double getPerimeter();

        // Concrete method
        public void displayColor() {
            System.out.println("Color: " + color);
        }
    }

    static class Circle extends Shape {
        private double radius;

        public Circle(String color, double radius) {
            super(color);
            this.radius = radius;
        }

        @Override
        public double getArea() {
            return Math.PI * radius * radius;
        }

        @Override
        public double getPerimeter() {
            return 2 * Math.PI * radius;
        }
    }

    static class Rectangle extends Shape {
        private double width;
        private double height;

        public Rectangle(String color, double width, double height) {
            super(color);
            this.width = width;
            this.height = height;
        }

        @Override
        public double getArea() {
            return width * height;
        }

        @Override
        public double getPerimeter() {
            return 2 * (width + height);
        }
    }

    private static void abstractClassDemo() {
        System.out.println("--- Abstract Classes ---");

        Shape circle = new Circle("Red", 5.0);
        Shape rectangle = new Rectangle("Blue", 4.0, 6.0);

        System.out.println("Circle:");
        circle.displayColor();
        System.out.printf("Area: %.2f\n", circle.getArea());
        System.out.printf("Perimeter: %.2f\n", circle.getPerimeter());

        System.out.println("\nRectangle:");
        rectangle.displayColor();
        System.out.printf("Area: %.2f\n", rectangle.getArea());
        System.out.printf("Perimeter: %.2f\n", rectangle.getPerimeter());

        System.out.println();
    }

    // ========== Interfaces ==========

    interface Flyable {
        void fly();

        default void land() {
            System.out.println("Landing...");
        }
    }

    interface Swimmable {
        void swim();
    }

    static class Bird implements Flyable {
        private String name;

        public Bird(String name) {
            this.name = name;
        }

        @Override
        public void fly() {
            System.out.println(name + " is flying in the sky");
        }
    }

    static class Duck implements Flyable, Swimmable {
        private String name;

        public Duck(String name) {
            this.name = name;
        }

        @Override
        public void fly() {
            System.out.println(name + " is flying");
        }

        @Override
        public void swim() {
            System.out.println(name + " is swimming");
        }
    }

    private static void interfaceDemo() {
        System.out.println("--- Interfaces ---");

        Bird bird = new Bird("Eagle");
        bird.fly();
        bird.land();

        Duck duck = new Duck("Donald");
        duck.fly();
        duck.swim();
        duck.land();

        System.out.println();
    }

    // ========== instanceof operator ==========

    private static void instanceofDemo() {
        System.out.println("--- instanceof Operator ---");

        Animal dog = new Dog("Max", 4, "Labrador");
        Animal cat = new Cat("Luna", 3, true);
        Object shape = new Circle("Green", 3.0);

        // Traditional instanceof
        if (dog instanceof Dog) {
            System.out.println("dog is an instance of Dog");
        }
        if (dog instanceof Animal) {
            System.out.println("dog is an instance of Animal");
        }

        // Pattern matching instanceof (Java 16+)
        if (dog instanceof Dog d) {
            System.out.println("Dog breed: " + d.getBreed());
            d.fetch();
        }

        if (cat instanceof Cat c) {
            System.out.println("Cat is indoor: " + c.isIndoor());
            c.scratch();
        }

        // instanceof with different types
        checkType(dog);
        checkType(cat);
        checkType(shape);
        checkType("Hello");
        checkType(123);

        System.out.println();
    }

    private static void checkType(Object obj) {
        if (obj instanceof Dog d) {
            System.out.println("  It's a Dog named " + d.name);
        } else if (obj instanceof Cat c) {
            System.out.println("  It's a Cat named " + c.name);
        } else if (obj instanceof Shape s) {
            System.out.println("  It's a Shape with area: " + s.getArea());
        } else if (obj instanceof String s) {
            System.out.println("  It's a String: " + s);
        } else if (obj instanceof Integer i) {
            System.out.println("  It's an Integer: " + i);
        } else {
            System.out.println("  Unknown type");
        }
    }

    // ========== Polymorphism ==========

    private static void polymorphismDemo() {
        System.out.println("--- Polymorphism ---");

        // Array of animals demonstrating polymorphism
        Animal[] animals = {
            new Dog("Rex", 5, "German Shepherd"),
            new Cat("Mittens", 2, false),
            new Dog("Charlie", 3, "Beagle")
        };

        System.out.println("All animals making sounds:");
        for (Animal animal : animals) {
            animal.makeSound(); // Polymorphic call
        }

        System.out.println("\nAll animals eating:");
        for (Animal animal : animals) {
            animal.eat();
        }

        System.out.println("\nType-specific behaviors:");
        for (Animal animal : animals) {
            if (animal instanceof Dog dog) {
                dog.fetch();
            } else if (animal instanceof Cat cat) {
                cat.scratch();
            }
        }

        System.out.println();
    }
}
