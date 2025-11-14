package com.example.demo.showcase;

/**
 * Demonstrates IBAN4J - IBAN Validation and Generation
 * Covers IBAN validation, BIC codes, and bank information
 */
public class IBAN4JShowcase {

    public static void demonstrate() {
        System.out.println("\n========== IBAN4J SHOWCASE ==========\n");

        System.out.println("--- IBAN4J Overview ---");
        System.out.println("International Bank Account Number (IBAN) library\n");

        System.out.println("Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.iban4j</groupId>
                <artifactId>iban4j</artifactId>
                <version>3.2.7-RELEASE</version>
            </dependency>
            """);

        System.out.println("\n--- IBAN Generation ---");
        System.out.println("""
            import org.iban4j.*;
            
            // Generate IBAN
            Iban iban = new Iban.Builder()
                .countryCode(CountryCode.DE)
                .bankCode("37040044")
                .accountNumber("0532013000")
                .build();
            
            System.out.println("IBAN: " + iban);  // DE89370400440532013000
            
            // Random IBAN
            Iban randomIban = Iban.random(CountryCode.FR);
            
            // From string
            Iban parsed = Iban.valueOf("GB82WEST12345698765432");
            """);

        System.out.println("\n--- IBAN Validation ---");
        System.out.println("""
            // Validate IBAN
            try {
                IbanUtil.validate("DE89370400440532013000");
                System.out.println("Valid IBAN");
            } catch (IbanFormatException e) {
                System.out.println("Invalid format: " + e.getMessage());
            } catch (InvalidCheckDigitException e) {
                System.out.println("Invalid check digit");
            } catch (UnsupportedCountryException e) {
                System.out.println("Unsupported country");
            }
            
            // Check if valid
            boolean isValid = IbanUtil.isSupportedCountry(CountryCode.US);
            """);

        System.out.println("\n--- IBAN Parts ---");
        System.out.println("""
            Iban iban = Iban.valueOf("GB82WEST12345698765432");
            
            // Extract parts
            CountryCode country = iban.getCountryCode();
            String checkDigit = iban.getCheckDigit();
            String bankCode = iban.getBankCode();
            String accountNumber = iban.getAccountNumber();
            String branchCode = iban.getBranchCode();
            
            System.out.println("Country: " + country);
            System.out.println("Bank Code: " + bankCode);
            System.out.println("Account: " + accountNumber);
            
            // Format
            String formatted = IbanUtil.toFormattedString(iban.toString());
            System.out.println(formatted);  // GB82 WEST 1234 5698 7654 32
            """);

        System.out.println("\n--- BIC (Bank Identifier Code) ---");
        System.out.println("""
            import org.iban4j.bic.*;
            
            // Parse BIC
            Bic bic = Bic.valueOf("DEUTDEFF");
            
            System.out.println("Bank Code: " + bic.getBankCode());
            System.out.println("Country: " + bic.getCountryCode());
            System.out.println("Location: " + bic.getLocationCode());
            System.out.println("Branch: " + bic.getBranchCode());
            
            // Validate BIC
            try {
                BicUtil.validate("DEUTDEFF500");
                System.out.println("Valid BIC");
            } catch (BicFormatException e) {
                System.out.println("Invalid BIC: " + e.getMessage());
            }
            """);

        System.out.println("\n--- Country Support ---");
        System.out.println("""
            // Check supported countries
            System.out.println("Supported countries:");
            for (CountryCode country : CountryCode.values()) {
                if (IbanUtil.isSupportedCountry(country)) {
                    System.out.println(country.getName());
                }
            }
            
            // Get IBAN length for country
            int length = IbanUtil.getIbanLength(CountryCode.DE);
            System.out.println("German IBAN length: " + length);
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Always validate IBAN before processing");
        System.out.println("   ✓ Handle exceptions properly");
        System.out.println("   ✓ Store IBAN without spaces");
        System.out.println("   ✓ Display IBAN with formatting");
        System.out.println();
    }
}
