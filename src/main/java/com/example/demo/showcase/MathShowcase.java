package com.example.demo.showcase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Demonstrates Java Math library and mathematical operations
 */
public class MathShowcase {

    public static void demonstrate() {
        System.out.println("\n========== MATH SHOWCASE ==========\n");

        basicMathOperations();
        mathConstants();
        trigonometricFunctions();
        exponentialAndLogarithmic();
        roundingFunctions();
        powerAndRoot();
        randomNumbers();
        minMaxAbsFunctions();
        specialMathFunctions();
        mathStaticImport();
    }

    // ========== Basic Math Operations ==========

    private static void basicMathOperations() {
        System.out.println("--- Basic Math Operations ---");

        int a = 10, b = 3;

        // Basic arithmetic
        System.out.println("Addition: " + a + " + " + b + " = " + (a + b));
        System.out.println("Subtraction: " + a + " - " + b + " = " + (a - b));
        System.out.println("Multiplication: " + a + " * " + b + " = " + (a * b));
        System.out.println("Division: " + a + " / " + b + " = " + (a / b));
        System.out.println("Modulo: " + a + " % " + b + " = " + (a % b));

        // Math.addExact, subtractExact, multiplyExact (throws on overflow)
        System.out.println("\nExact operations (throw on overflow):");
        System.out.println("addExact(10, 3): " + Math.addExact(10, 3));
        System.out.println("subtractExact(10, 3): " + Math.subtractExact(10, 3));
        System.out.println("multiplyExact(10, 3): " + Math.multiplyExact(10, 3));

        try {
            int overflow = Math.addExact(Integer.MAX_VALUE, 1);
        } catch (ArithmeticException e) {
            System.out.println("addExact overflow: ArithmeticException");
        }

        // Math.incrementExact, decrementExact, negateExact
        System.out.println("incrementExact(10): " + Math.incrementExact(10));
        System.out.println("decrementExact(10): " + Math.decrementExact(10));
        System.out.println("negateExact(10): " + Math.negateExact(10));

        // Math.floorDiv and floorMod
        System.out.println("\nFloor division (always rounds down):");
        System.out.println("floorDiv(10, 3): " + Math.floorDiv(10, 3));
        System.out.println("floorDiv(-10, 3): " + Math.floorDiv(-10, 3));
        System.out.println("floorMod(10, 3): " + Math.floorMod(10, 3));
        System.out.println("floorMod(-10, 3): " + Math.floorMod(-10, 3));

        System.out.println();
    }

    // ========== Math Constants ==========

    private static void mathConstants() {
        System.out.println("--- Math Constants ---");

        System.out.println("Math.PI: " + Math.PI);
        System.out.println("Math.E (Euler's number): " + Math.E);

        // Useful for comparisons
        System.out.println("\nDouble limits:");
        System.out.println("Double.MAX_VALUE: " + Double.MAX_VALUE);
        System.out.println("Double.MIN_VALUE: " + Double.MIN_VALUE);
        System.out.println("Double.POSITIVE_INFINITY: " + Double.POSITIVE_INFINITY);
        System.out.println("Double.NEGATIVE_INFINITY: " + Double.NEGATIVE_INFINITY);
        System.out.println("Double.NaN: " + Double.NaN);

        // Integer limits
        System.out.println("\nInteger limits:");
        System.out.println("Integer.MAX_VALUE: " + Integer.MAX_VALUE);
        System.out.println("Integer.MIN_VALUE: " + Integer.MIN_VALUE);

        System.out.println();
    }

    // ========== Trigonometric Functions ==========

    private static void trigonometricFunctions() {
        System.out.println("--- Trigonometric Functions ---");
        System.out.println("All angles in RADIANS (use Math.toRadians() for degrees)");

        double degrees = 45;
        double radians = Math.toRadians(degrees);

        System.out.println("\nAngle: 45 degrees = " + radians + " radians");

        // Basic trig functions
        System.out.println("sin(45°): " + Math.sin(radians));
        System.out.println("cos(45°): " + Math.cos(radians));
        System.out.println("tan(45°): " + Math.tan(radians));

        // Inverse trig functions (return radians)
        System.out.println("\nInverse functions:");
        System.out.println("asin(0.5): " + Math.asin(0.5) + " rad = " + Math.toDegrees(Math.asin(0.5)) + "°");
        System.out.println("acos(0.5): " + Math.acos(0.5) + " rad = " + Math.toDegrees(Math.acos(0.5)) + "°");
        System.out.println("atan(1): " + Math.atan(1) + " rad = " + Math.toDegrees(Math.atan(1)) + "°");

        // atan2 (two-argument arctangent)
        System.out.println("\natan2 (handles quadrants correctly):");
        System.out.println("atan2(1, 1): " + Math.toDegrees(Math.atan2(1, 1)) + "°");
        System.out.println("atan2(1, -1): " + Math.toDegrees(Math.atan2(1, -1)) + "°");

        // Hyperbolic functions
        System.out.println("\nHyperbolic functions:");
        System.out.println("sinh(1): " + Math.sinh(1));
        System.out.println("cosh(1): " + Math.cosh(1));
        System.out.println("tanh(1): " + Math.tanh(1));

        // Convert between degrees and radians
        System.out.println("\nConversions:");
        System.out.println("toRadians(180°): " + Math.toRadians(180));
        System.out.println("toDegrees(π): " + Math.toDegrees(Math.PI));

        System.out.println();
    }

    // ========== Exponential and Logarithmic ==========

    private static void exponentialAndLogarithmic() {
        System.out.println("--- Exponential and Logarithmic Functions ---");

        // Exponential
        System.out.println("exp(1) (e^1): " + Math.exp(1));
        System.out.println("exp(2) (e^2): " + Math.exp(2));

        // expm1(x) = e^x - 1 (accurate for small x)
        System.out.println("expm1(0.001): " + Math.expm1(0.001));

        // Natural logarithm (base e)
        System.out.println("\nNatural logarithm (ln):");
        System.out.println("log(Math.E): " + Math.log(Math.E));
        System.out.println("log(10): " + Math.log(10));

        // log1p(x) = ln(1 + x) (accurate for small x)
        System.out.println("log1p(0.001): " + Math.log1p(0.001));

        // Base-10 logarithm
        System.out.println("\nBase-10 logarithm:");
        System.out.println("log10(100): " + Math.log10(100));
        System.out.println("log10(1000): " + Math.log10(1000));

        // Custom base logarithm
        double logBase2Of8 = Math.log(8) / Math.log(2);
        System.out.println("log₂(8): " + logBase2Of8);

        System.out.println();
    }

    // ========== Rounding Functions ==========

    private static void roundingFunctions() {
        System.out.println("--- Rounding Functions ---");

        double value = 3.7;
        double negative = -3.7;

        System.out.println("Value: " + value);
        System.out.println("ceil(3.7): " + Math.ceil(value) + " (round up)");
        System.out.println("floor(3.7): " + Math.floor(value) + " (round down)");
        System.out.println("round(3.7): " + Math.round(value) + " (round to nearest)");
        System.out.println("rint(3.7): " + Math.rint(value) + " (round to nearest even)");

        System.out.println("\nNegative value: " + negative);
        System.out.println("ceil(-3.7): " + Math.ceil(negative));
        System.out.println("floor(-3.7): " + Math.floor(negative));
        System.out.println("round(-3.7): " + Math.round(negative));

        // Difference between round and rint
        System.out.println("\nround vs rint for .5:");
        System.out.println("round(2.5): " + Math.round(2.5) + " (rounds up)");
        System.out.println("rint(2.5): " + Math.rint(2.5) + " (rounds to even)");
        System.out.println("round(3.5): " + Math.round(3.5));
        System.out.println("rint(3.5): " + Math.rint(3.5) + " (rounds to even)");

        // nextAfter, nextUp, nextDown
        System.out.println("\nNext floating-point values:");
        System.out.println("nextAfter(1.0, 2.0): " + Math.nextAfter(1.0, 2.0));
        System.out.println("nextUp(1.0): " + Math.nextUp(1.0));
        System.out.println("nextDown(1.0): " + Math.nextDown(1.0));

        // ulp - Unit in Last Place
        System.out.println("ulp(1.0): " + Math.ulp(1.0));

        System.out.println();
    }

    // ========== Power and Root ==========

    private static void powerAndRoot() {
        System.out.println("--- Power and Root Functions ---");

        // Power
        System.out.println("pow(2, 3) (2³): " + Math.pow(2, 3));
        System.out.println("pow(2, 10) (2¹⁰): " + Math.pow(2, 10));
        System.out.println("pow(5, 0.5) (√5): " + Math.pow(5, 0.5));

        // Square root
        System.out.println("\nSquare root:");
        System.out.println("sqrt(16): " + Math.sqrt(16));
        System.out.println("sqrt(2): " + Math.sqrt(2));
        System.out.println("sqrt(-1): " + Math.sqrt(-1) + " (NaN)");

        // Cube root
        System.out.println("\nCube root:");
        System.out.println("cbrt(8): " + Math.cbrt(8));
        System.out.println("cbrt(27): " + Math.cbrt(27));
        System.out.println("cbrt(-8): " + Math.cbrt(-8));

        // hypot - sqrt(x² + y²) without overflow
        System.out.println("\nHypotenuse (Pythagorean theorem):");
        System.out.println("hypot(3, 4): " + Math.hypot(3, 4));
        System.out.println("hypot(5, 12): " + Math.hypot(5, 12));

        // scalb - x * 2^scaleFactor (efficient)
        System.out.println("\nScalb (multiply by power of 2):");
        System.out.println("scalb(1.0, 3) (1 * 2³): " + Math.scalb(1.0, 3));
        System.out.println("scalb(5.0, 2) (5 * 2²): " + Math.scalb(5.0, 2));

        System.out.println();
    }

    // ========== Random Numbers ==========

    private static void randomNumbers() {
        System.out.println("--- Random Numbers ---");

        // Math.random() - [0.0, 1.0)
        System.out.println("Math.random(): " + Math.random());
        System.out.println("Math.random(): " + Math.random());

        // Random integer in range [min, max]
        int min = 1, max = 100;
        int randomInt = (int) (Math.random() * (max - min + 1)) + min;
        System.out.println("Random [1-100]: " + randomInt);

        // java.util.Random (better for multiple random values)
        System.out.println("\njava.util.Random:");
        Random random = new Random();
        System.out.println("nextInt(): " + random.nextInt());
        System.out.println("nextInt(100): " + random.nextInt(100));
        System.out.println("nextLong(): " + random.nextLong());
        System.out.println("nextDouble(): " + random.nextDouble());
        System.out.println("nextBoolean(): " + random.nextBoolean());
        System.out.println("nextGaussian(): " + random.nextGaussian());

        // Random with seed (reproducible)
        Random seededRandom = new Random(12345);
        System.out.println("\nSeeded Random (seed=12345):");
        System.out.println("First: " + seededRandom.nextInt(100));
        System.out.println("Second: " + seededRandom.nextInt(100));

        // ThreadLocalRandom (better for concurrent use)
        System.out.println("\nThreadLocalRandom (thread-safe):");
        System.out.println("nextInt(1, 101): " + ThreadLocalRandom.current().nextInt(1, 101));
        System.out.println("nextDouble(0.0, 1.0): " + ThreadLocalRandom.current().nextDouble(0.0, 1.0));

        System.out.println();
    }

    // ========== Min, Max, Abs Functions ==========

    private static void minMaxAbsFunctions() {
        System.out.println("--- Min, Max, Abs Functions ---");

        // Min and Max
        System.out.println("min(10, 20): " + Math.min(10, 20));
        System.out.println("max(10, 20): " + Math.max(10, 20));
        System.out.println("min(-5, -10): " + Math.min(-5, -10));
        System.out.println("max(-5, -10): " + Math.max(-5, -10));

        // Works with different types
        System.out.println("min(3.14, 2.71): " + Math.min(3.14, 2.71));
        System.out.println("max(3.14, 2.71): " + Math.max(3.14, 2.71));

        // Absolute value
        System.out.println("\nAbsolute value:");
        System.out.println("abs(-10): " + Math.abs(-10));
        System.out.println("abs(10): " + Math.abs(10));
        System.out.println("abs(-3.14): " + Math.abs(-3.14));

        // signum - returns -1.0, 0.0, or 1.0
        System.out.println("\nSignum (sign of number):");
        System.out.println("signum(10): " + Math.signum(10));
        System.out.println("signum(-10): " + Math.signum(-10));
        System.out.println("signum(0): " + Math.signum(0));

        // copySign - magnitude of first arg, sign of second
        System.out.println("\ncopySign (magnitude of first, sign of second):");
        System.out.println("copySign(10, -1): " + Math.copySign(10, -1));
        System.out.println("copySign(-10, 1): " + Math.copySign(-10, 1));

        System.out.println();
    }

    // ========== Special Math Functions ==========

    private static void specialMathFunctions() {
        System.out.println("--- Special Math Functions ---");

        // IEEEremainder - IEEE 754 remainder
        System.out.println("IEEEremainder(10, 3): " + Math.IEEEremainder(10, 3));
        System.out.println("IEEEremainder(10.5, 3): " + Math.IEEEremainder(10.5, 3));

        // getExponent - unbiased exponent
        System.out.println("\ngetExponent:");
        System.out.println("getExponent(1024.0): " + Math.getExponent(1024.0));
        System.out.println("getExponent(0.125): " + Math.getExponent(0.125));

        // fma - fused multiply-add (a*b+c)
        System.out.println("\nfma (fused multiply-add):");
        System.out.println("fma(2, 3, 4) (2*3+4): " + Math.fma(2, 3, 4));
        System.out.println("fma(1.5, 2.5, 1.0): " + Math.fma(1.5, 2.5, 1.0));

        // Special value checks
        System.out.println("\nSpecial value checks:");
        System.out.println("isNaN(0.0/0.0): " + Double.isNaN(0.0 / 0.0));
        System.out.println("isInfinite(1.0/0.0): " + Double.isInfinite(1.0 / 0.0));
        System.out.println("isFinite(1.0): " + Double.isFinite(1.0));
        System.out.println("isFinite(∞): " + Double.isFinite(Double.POSITIVE_INFINITY));

        // Compare with NaN
        System.out.println("\nNaN comparisons:");
        double nan = Double.NaN;
        System.out.println("NaN == NaN: " + (nan == nan) + " (always false!)");
        System.out.println("Double.isNaN(NaN): " + Double.isNaN(nan));
        System.out.println("Double.compare(NaN, NaN): " + Double.compare(nan, nan));

        System.out.println();
    }

    // ========== Math with Static Import ==========

    private static void mathStaticImport() {
        System.out.println("--- Using Static Import ---");
        System.out.println("Tip: import static java.lang.Math.*;");
        System.out.println("Then use: sqrt(16) instead of Math.sqrt(16)");

        System.out.println("\nCommon calculations:");

        // Distance between two points
        double x1 = 0, y1 = 0, x2 = 3, y2 = 4;
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        System.out.println("Distance (0,0) to (3,4): " + distance);

        // Or using hypot
        double distance2 = Math.hypot(x2 - x1, y2 - y1);
        System.out.println("Distance using hypot: " + distance2);

        // Circle area
        double radius = 5;
        double area = Math.PI * Math.pow(radius, 2);
        System.out.println("Circle area (r=5): " + area);

        // Circle circumference
        double circumference = 2 * Math.PI * radius;
        System.out.println("Circle circumference (r=5): " + circumference);

        // Sphere volume
        double volume = (4.0 / 3.0) * Math.PI * Math.pow(radius, 3);
        System.out.println("Sphere volume (r=5): " + volume);

        // Convert temperature C to F
        double celsius = 25;
        double fahrenheit = celsius * 9 / 5 + 32;
        System.out.println(celsius + "°C = " + fahrenheit + "°F");

        // Percentage calculation
        double value = 80;
        double total = 200;
        double percentage = (value / total) * 100;
        System.out.println(value + " is " + percentage + "% of " + total);

        // Compound interest
        double principal = 1000;
        double rate = 0.05; // 5%
        int years = 10;
        double amount = principal * Math.pow(1 + rate, years);
        System.out.println("$" + principal + " at 5% for " + years + " years: $" +
                         String.format("%.2f", amount));

        System.out.println();
    }
}
