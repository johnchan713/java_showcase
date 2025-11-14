package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for data processing operations
 * Demonstrates Java 21 features like enhanced Stream API usage
 */
@Service
public class DataProcessingService {

    /**
     * Transform data using Java streams and modern features
     */
    public List<String> transformData(List<String> data) {
        return data.stream()
            .filter(s -> s != null && !s.isBlank())
            .map(String::trim)
            .map(String::toUpperCase)
            .sorted()
            .collect(Collectors.toList());
    }

    /**
     * Process numbers with pattern matching
     */
    public String processNumber(Object number) {
        return switch (number) {
            case Integer i when i < 0 -> "Negative: " + i;
            case Integer i when i == 0 -> "Zero";
            case Integer i when i > 0 && i <= 10 -> "Small positive: " + i;
            case Integer i -> "Large positive: " + i;
            case Double d -> "Decimal: " + d;
            case null -> "Null value";
            default -> "Unknown number type";
        };
    }

    /**
     * Filter and process strings using modern Java features
     */
    public List<String> filterAndProcess(List<String> data, int minLength) {
        return data.stream()
            .filter(s -> s != null && s.length() >= minLength)
            .map(s -> switch (s.length()) {
                case 1, 2, 3 -> s.toUpperCase();
                case 4, 5 -> s.toLowerCase();
                default -> {
                    var capitalized = s.substring(0, 1).toUpperCase() +
                                    s.substring(1).toLowerCase();
                    yield capitalized;
                }
            })
            .toList(); // Java 16+ feature: stream.toList()
    }
}
