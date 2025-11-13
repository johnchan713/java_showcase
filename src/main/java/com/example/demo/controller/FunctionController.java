package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.DataProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller showcasing various functions
 * Demonstrates Java 21 features and Spring Boot functionality
 */
@RestController
@RequestMapping("/api")
public class FunctionController {

    private final DataProcessingService dataProcessingService;

    public FunctionController(DataProcessingService dataProcessingService) {
        this.dataProcessingService = dataProcessingService;
    }

    /**
     * Simple greeting function
     */
    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello(@RequestParam(defaultValue = "World") String name) {
        return ResponseEntity.ok(Map.of(
            "message", "Hello, " + name + "!",
            "javaVersion", System.getProperty("java.version")
        ));
    }

    /**
     * Pattern matching function using Java 21 pattern matching features
     */
    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> processData(@RequestBody Object data) {
        String result = switch (data) {
            case String s when s.length() > 10 -> "Long string: " + s.substring(0, 10) + "...";
            case String s -> "Short string: " + s;
            case Integer i when i > 100 -> "Large number: " + i;
            case Integer i -> "Small number: " + i;
            case List<?> list -> "List with " + list.size() + " elements";
            case null -> "Null value received";
            default -> "Unknown type: " + data.getClass().getSimpleName();
        };

        return ResponseEntity.ok(Map.of(
            "input", data,
            "result", result,
            "type", data != null ? data.getClass().getSimpleName() : "null"
        ));
    }

    /**
     * User creation function
     */
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(user);
    }

    /**
     * Data transformation function
     */
    @PostMapping("/transform")
    public ResponseEntity<List<String>> transformData(@RequestBody List<String> data) {
        List<String> transformed = dataProcessingService.transformData(data);
        return ResponseEntity.ok(transformed);
    }

    /**
     * Mathematical function
     */
    @GetMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculate(
            @RequestParam int a,
            @RequestParam int b,
            @RequestParam(defaultValue = "add") String operation) {

        int result = switch (operation.toLowerCase()) {
            case "add" -> a + b;
            case "subtract" -> a - b;
            case "multiply" -> a * b;
            case "divide" -> b != 0 ? a / b : 0;
            default -> 0;
        };

        return ResponseEntity.ok(Map.of(
            "operand1", a,
            "operand2", b,
            "operation", operation,
            "result", result
        ));
    }

    /**
     * String manipulation function using Java 21 features
     */
    @PostMapping("/manipulate")
    public ResponseEntity<Map<String, String>> manipulateString(@RequestBody Map<String, String> request) {
        String input = request.getOrDefault("text", "");
        String operation = request.getOrDefault("operation", "uppercase");

        String result = switch (operation) {
            case "uppercase" -> input.toUpperCase();
            case "lowercase" -> input.toLowerCase();
            case "reverse" -> new StringBuilder(input).reverse().toString();
            case "capitalize" -> input.isEmpty() ? "" :
                Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
            default -> input;
        };

        return ResponseEntity.ok(Map.of(
            "original", input,
            "operation", operation,
            "result", result
        ));
    }

    /**
     * Health check function
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Java Showcase API",
            "java", System.getProperty("java.version")
        ));
    }
}
