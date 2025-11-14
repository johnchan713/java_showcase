package com.example.demo;

import com.example.demo.showcase.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main Spring Boot Application
 * Uses Java 21 features and demonstrates comprehensive Java showcase
 */
@SpringBootApplication
public class JavaShowcaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaShowcaseApplication.class, args);
    }

    /**
     * CommandLineRunner to execute all showcases on startup
     * Set spring.main.web-application-type=none in application.properties to run only showcases
     */
    @Bean
    public CommandLineRunner showcaseRunner() {
        return args -> {
            System.out.println("\n╔══════════════════════════════════════════════════════╗");
            System.out.println("║       JAVA 21 COMPREHENSIVE SHOWCASE                 ║");
            System.out.println("║       Demonstrating Modern Java Features            ║");
            System.out.println("╚══════════════════════════════════════════════════════╝");

            // Run all showcases
            BasicSyntaxShowcase.demonstrate();
            SwitchCaseShowcase.demonstrate();
            OOPShowcase.demonstrate();
            ObjectMethodsShowcase.demonstrate();
            StringProcessingShowcase.demonstrate();
            DateTimeShowcase.demonstrate();
            LambdaShowcase.demonstrate();
            StreamShowcase.demonstrate();
            CollectionsShowcase.demonstrate();
            AdvancedCollectionsShowcase.demonstrate();
            TypesAndConversionsShowcase.demonstrate();
            ConcurrencyShowcase.demonstrate();
            SpringAnnotationsShowcase.demonstrate();
            MathShowcase.demonstrate();
            ThreadPoolExecutorShowcase.demonstrate();
            ReflectionShowcase.demonstrate();
            GenericsShowcase.demonstrate();
            CryptographyShowcase.demonstrate();
            SpringSecurityShowcase.demonstrate();
            TestingShowcase.demonstrate();
            KafkaShowcase.demonstrate();
            SpringContextShowcase.demonstrate();
            RedisShowcase.demonstrate();
            Resilience4jShowcase.demonstrate();
            LoggingShowcase.demonstrate();
            ApacheCamelShowcase.demonstrate();
            JacksonShowcase.demonstrate();
            JAXBShowcase.demonstrate();
            JMSShowcase.demonstrate();
            JDBCShowcase.demonstrate();
            DynamoDBShowcase.demonstrate();
            HazelcastShowcase.demonstrate();
            SolaceShowcase.demonstrate();
            HadoopShowcase.demonstrate();
            ApacheMINAShowcase.demonstrate();
            SeleniumShowcase.demonstrate();
            MicrometerShowcase.demonstrate();
            WebSocketShowcase.demonstrate();
            ReactorShowcase.demonstrate();
            DisruptorShowcase.demonstrate();
            NettyShowcase.demonstrate();
            RabbitMQShowcase.demonstrate();
            GsonShowcase.demonstrate();
            AvroShowcase.demonstrate();
            AwaitilityShowcase.demonstrate();
            DockerClientShowcase.demonstrate();
            KubernetesShowcase.demonstrate();

            System.out.println("\n╔══════════════════════════════════════════════════════╗");
            System.out.println("║       ALL SHOWCASES COMPLETED!                       ║");
            System.out.println("╚══════════════════════════════════════════════════════╝\n");
        };
    }
}
