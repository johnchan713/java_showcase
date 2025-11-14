package com.example.demo.showcase;

/**
 * Demonstrates JavaMoney (JSR 354) - Money and Currency API
 * Covers monetary amounts, currency conversion, and formatting
 */
public class JavaMoneyShowcase {

    public static void demonstrate() {
        System.out.println("\n========== JAVAMONEY SHOWCASE ==========\n");

        System.out.println("--- JavaMoney Overview ---");
        System.out.println("JSR 354 - Money and Currency API\n");

        System.out.println("Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.javamoney</groupId>
                <artifactId>moneta</artifactId>
                <version>1.4.2</version>
                <type>pom</type>
            </dependency>
            """);

        System.out.println("\n--- Monetary Amounts ---");
        System.out.println("""
            import javax.money.*;
            import org.javamoney.moneta.*;
            
            // Create monetary amounts
            MonetaryAmount amount1 = Money.of(100.50, "USD");
            MonetaryAmount amount2 = Money.of(50.25, "USD");
            
            // Using FastMoney (faster, less precise)
            MonetaryAmount fast = FastMoney.of(99.99, "EUR");
            
            // Arithmetic operations
            MonetaryAmount sum = amount1.add(amount2);
            MonetaryAmount diff = amount1.subtract(amount2);
            MonetaryAmount product = amount1.multiply(2);
            MonetaryAmount quotient = amount1.divide(2);
            
            System.out.println("Sum: " + sum);  // USD 150.75
            
            // Comparisons
            boolean isGreater = amount1.isGreaterThan(amount2);
            boolean isEqual = amount1.isEqualTo(amount2);
            
            // Absolute and negate
            MonetaryAmount abs = amount1.abs();
            MonetaryAmount neg = amount1.negate();
            """);

        System.out.println("\n--- Currency Operations ---");
        System.out.println("""
            import javax.money.CurrencyUnit;
            import javax.money.Monetary;
            
            // Get currency
            CurrencyUnit usd = Monetary.getCurrency("USD");
            CurrencyUnit eur = Monetary.getCurrency("EUR");
            
            System.out.println("Currency: " + usd.getCurrencyCode());
            System.out.println("Numeric Code: " + usd.getNumericCode());
            System.out.println("Default Fraction Digits: " + 
                usd.getDefaultFractionDigits());
            
            // All available currencies
            Set<CurrencyUnit> currencies = Monetary.getCurrencies();
            """);

        System.out.println("\n--- Currency Conversion ---");
        System.out.println("""
            import javax.money.convert.*;
            import org.javamoney.moneta.convert.*;
            
            // Get exchange rate provider
            ExchangeRateProvider provider = MonetaryConversions.getExchangeRateProvider();
            
            // Convert
            MonetaryAmount usdAmount = Money.of(100, "USD");
            CurrencyConversion conversion = provider.getCurrencyConversion("EUR");
            MonetaryAmount eurAmount = usdAmount.with(conversion);
            
            System.out.println("USD " + usdAmount + " = " + eurAmount);
            
            // Get exchange rate
            ExchangeRate rate = provider.getExchangeRate("USD", "EUR");
            System.out.println("Exchange Rate: " + rate.getFactor());
            
            // Chain conversions
            MonetaryAmount result = Money.of(100, "USD")
                .with(MonetaryConversions.getConversion("EUR"))
                .with(MonetaryConversions.getConversion("GBP"));
            """);

        System.out.println("\n--- Formatting ---");
        System.out.println("""
            import javax.money.format.*;
            
            MonetaryAmount amount = Money.of(1234.56, "USD");
            
            // Default formatting
            MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(
                Locale.US
            );
            String formatted = format.format(amount);
            System.out.println(formatted);  // $1,234.56
            
            // Custom format
            MonetaryAmountFormat customFormat = MonetaryFormats.getAmountFormat(
                AmountFormatQueryBuilder.of(Locale.GERMANY).build()
            );
            System.out.println(customFormat.format(amount));  // 1.234,56 $
            
            // Parse
            MonetaryAmount parsed = format.parse("$5,678.90");
            """);

        System.out.println("\n--- Rounding ---");
        System.out.println("""
            import org.javamoney.moneta.function.*;
            
            MonetaryAmount amount = Money.of(123.456, "USD");
            
            // Round to currency precision
            MonetaryOperator rounder = Monetary.getDefaultRounding();
            MonetaryAmount rounded = amount.with(rounder);
            
            // Custom rounding
            MonetaryOperator customRound = MonetaryRoundings.getRounding(
                RoundingQueryBuilder.of()
                    .setScale(2)
                    .set(RoundingMode.HALF_UP)
                    .build()
            );
            
            // Ceiling/Floor
            MonetaryAmount ceil = MonetaryFunctions.majorPart().apply(amount);
            """);

        System.out.println("\n--- Grouping and Aggregation ---");
        System.out.println("""
            import org.javamoney.moneta.function.*;
            
            List<MonetaryAmount> amounts = Arrays.asList(
                Money.of(100, "USD"),
                Money.of(200, "USD"),
                Money.of(300, "USD")
            );
            
            // Sum
            MonetaryAmount total = amounts.stream()
                .reduce(MonetaryFunctions::sum)
                .orElse(Money.of(0, "USD"));
            
            // Min/Max
            MonetaryAmount min = amounts.stream()
                .reduce(MonetaryFunctions::min)
                .orElse(null);
            
            MonetaryAmount max = amounts.stream()
                .reduce(MonetaryFunctions::max)
                .orElse(null);
            
            // Group by currency
            Map<CurrencyUnit, List<MonetaryAmount>> grouped = amounts.stream()
                .collect(Collectors.groupingBy(MonetaryAmount::getCurrency));
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Use Money for precision");
        System.out.println("   ✓ Use FastMoney for performance");
        System.out.println("   ✓ Always specify currency");
        System.out.println("   ✓ Handle currency conversion carefully");
        System.out.println("   ✗ Don't use double for money");
        System.out.println();
    }
}
