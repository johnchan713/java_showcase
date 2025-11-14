package com.example.demo.showcase;

/**
 * Demonstrates Stripe Java SDK
 * Covers payments, customers, subscriptions, and webhooks
 */
public class StripeShowcase {

    public static void demonstrate() {
        System.out.println("\n========== STRIPE PAYMENTS SHOWCASE ==========\n");

        System.out.println("--- Stripe Overview ---");
        System.out.println("Online payment processing platform\n");

        System.out.println("Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>com.stripe</groupId>
                <artifactId>stripe-java</artifactId>
                <version>24.16.0</version>
            </dependency>
            
            # application.properties
            stripe.api.key=sk_test_...
            """);

        System.out.println("\n--- Setup ---");
        System.out.println("""
            import com.stripe.Stripe;
            import com.stripe.model.*;
            import com.stripe.param.*;
            
            // Set API key
            Stripe.apiKey = "sk_test_...";
            """);

        System.out.println("\n--- Payment Intent (Recommended) ---");
        System.out.println("""
            // Create Payment Intent
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(2000L)  // $20.00 in cents
                .setCurrency("usd")
                .addPaymentMethodType("card")
                .setDescription("Order #12345")
                .putMetadata("order_id", "12345")
                .build();
            
            PaymentIntent intent = PaymentIntent.create(params);
            
            // Return client secret to frontend
            String clientSecret = intent.getClientSecret();
            
            // Confirm payment (usually done on frontend)
            PaymentIntentConfirmParams confirmParams = 
                PaymentIntentConfirmParams.builder()
                    .setPaymentMethod("pm_card_visa")
                    .build();
            
            intent = intent.confirm(confirmParams);
            
            System.out.println("Payment Status: " + intent.getStatus());
            """);

        System.out.println("\n--- Customer Management ---");
        System.out.println("""
            // Create customer
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setEmail("customer@example.com")
                .setName("John Doe")
                .setPhone("+1234567890")
                .putMetadata("user_id", "123")
                .build();
            
            Customer customer = Customer.create(customerParams);
            
            // Retrieve customer
            Customer customer = Customer.retrieve("cus_xxxxx");
            
            // Update customer
            CustomerUpdateParams updateParams = CustomerUpdateParams.builder()
                .setDefaultSource("card_xxxxx")
                .build();
            
            customer = customer.update(updateParams);
            
            // List customers
            CustomerListParams listParams = CustomerListParams.builder()
                .setLimit(10L)
                .build();
            
            CustomerCollection customers = Customer.list(listParams);
            """);

        System.out.println("\n--- Subscriptions ---");
        System.out.println("""
            // Create product
            ProductCreateParams productParams = ProductCreateParams.builder()
                .setName("Premium Subscription")
                .setDescription("Monthly premium access")
                .build();
            
            Product product = Product.create(productParams);
            
            // Create price
            PriceCreateParams priceParams = PriceCreateParams.builder()
                .setProduct(product.getId())
                .setCurrency("usd")
                .setUnitAmount(999L)  // $9.99
                .setRecurring(
                    PriceCreateParams.Recurring.builder()
                        .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                        .build()
                )
                .build();
            
            Price price = Price.create(priceParams);
            
            // Create subscription
            SubscriptionCreateParams subscriptionParams = 
                SubscriptionCreateParams.builder()
                    .setCustomer(customer.getId())
                    .addItem(
                        SubscriptionCreateParams.Item.builder()
                            .setPrice(price.getId())
                            .build()
                    )
                    .setTrialPeriodDays(14L)
                    .build();
            
            Subscription subscription = Subscription.create(subscriptionParams);
            
            // Cancel subscription
            subscription = subscription.cancel();
            """);

        System.out.println("\n--- Refunds ---");
        System.out.println("""
            // Create refund
            RefundCreateParams refundParams = RefundCreateParams.builder()
                .setPaymentIntent("pi_xxxxx")
                .setAmount(1000L)  // Partial refund $10.00
                .setReason(RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER)
                .build();
            
            Refund refund = Refund.create(refundParams);
            
            // Full refund
            RefundCreateParams fullRefund = RefundCreateParams.builder()
                .setPaymentIntent("pi_xxxxx")
                .build();
            
            Refund refund = Refund.create(fullRefund);
            """);

        System.out.println("\n--- Webhooks ---");
        System.out.println("""
            @RestController
            @RequestMapping("/webhooks")
            public class StripeWebhookController {
            
                private static final String webhookSecret = "whsec_...";
                
                @PostMapping("/stripe")
                public ResponseEntity<String> handleWebhook(
                        @RequestBody String payload,
                        @RequestHeader("Stripe-Signature") String sigHeader) {
                    
                    Event event;
                    try {
                        event = Webhook.constructEvent(
                            payload, sigHeader, webhookSecret
                        );
                    } catch (SignatureVerificationException e) {
                        return ResponseEntity.status(400).body("Invalid signature");
                    }
                    
                    // Handle event
                    switch (event.getType()) {
                        case "payment_intent.succeeded":
                            PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                                .getObject().orElse(null);
                            handlePaymentSuccess(intent);
                            break;
                            
                        case "payment_intent.payment_failed":
                            handlePaymentFailure(intent);
                            break;
                            
                        case "customer.subscription.created":
                            Subscription subscription = (Subscription) event.getData();
                            handleSubscriptionCreated(subscription);
                            break;
                            
                        case "invoice.payment_succeeded":
                            Invoice invoice = (Invoice) event.getData();
                            handleInvoicePayment(invoice);
                            break;
                    }
                    
                    return ResponseEntity.ok("Received");
                }
            }
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Use Payment Intents API");
        System.out.println("   ✓ Verify webhook signatures");
        System.out.println("   ✓ Handle idempotency");
        System.out.println("   ✓ Store Stripe customer IDs");
        System.out.println("   ✓ Use test mode for development");
        System.out.println();
    }
}
