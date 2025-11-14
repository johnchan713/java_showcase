package com.example.demo.showcase;

/**
 * Demonstrates Strata - Market Risk Analytics by OpenGamma
 * Covers pricing, risk calculations, and market data
 */
public class StrataShowcase {

    public static void demonstrate() {
        System.out.println("\n========== STRATA SHOWCASE ==========\n");

        System.out.println("--- Strata Overview ---");
        System.out.println("Market risk analytics and pricing library\n");

        System.out.println("Key features:");
        System.out.println("   • Derivative pricing (swaps, options, futures)");
        System.out.println("   • Risk measures (PV01, DV01, Greeks)");
        System.out.println("   • Market data management");
        System.out.println("   • Curve calibration");
        System.out.println("   • Schedule generation");

        System.out.println("\nDependencies:");
        System.out.println("""
            <dependency>
                <groupId>com.opengamma.strata</groupId>
                <artifactId>strata-basics</artifactId>
                <version>2.12.38</version>
            </dependency>
            <dependency>
                <groupId>com.opengamma.strata</groupId>
                <artifactId>strata-pricer</artifactId>
                <version>2.12.38</version>
            </dependency>
            """);

        System.out.println("\n--- Basic Concepts ---");
        System.out.println("""
            import com.opengamma.strata.basics.currency.*;
            import com.opengamma.strata.basics.date.*;
            import com.opengamma.strata.product.swap.*;
            
            // Currencies
            Currency usd = Currency.USD;
            Currency eur = Currency.EUR;
            CurrencyAmount amount = CurrencyAmount.of(Currency.USD, 1_000_000);
            
            // Dates
            LocalDate tradeDate = LocalDate.of(2025, 1, 15);
            LocalDate maturityDate = tradeDate.plusYears(5);
            
            // Business Day Adjustment
            BusinessDayAdjustment bda = BusinessDayAdjustment.of(
                BusinessDayConventions.MODIFIED_FOLLOWING,
                HolidayCalendarIds.USNY
            );
            
            // Day Count Convention
            DayCount dayCount = DayCounts.ACT_360;
            """);

        System.out.println("\n--- Interest Rate Swap ---");
        System.out.println("""
            import com.opengamma.strata.product.swap.type.FixedIborSwapConventions;
            
            // Create a 5Y USD fixed-vs-LIBOR swap
            SwapTrade swap = FixedIborSwapConventions.USD_FIXED_6M_LIBOR_3M
                .createTrade(
                    tradeDate,           // Trade date
                    Period.ofYears(5),   // Tenor
                    BuySell.BUY,         // Pay fixed
                    1_000_000,           // Notional
                    0.025,               // Fixed rate 2.5%
                    ReferenceData.standard()
                );
            
            System.out.println("Swap: " + swap);
            """);

        System.out.println("\n--- Pricing ---");
        System.out.println("""
            import com.opengamma.strata.pricer.swap.*;
            import com.opengamma.strata.market.curve.*;
            
            // Build curve
            ImmutableRatesProvider ratesProvider = buildRatesProvider();
            
            // Create pricer
            DiscountingSwapProductPricer pricer = 
                DiscountingSwapProductPricer.DEFAULT;
            
            // Calculate present value
            CurrencyAmount pv = pricer.presentValue(
                swap.getProduct().resolve(ReferenceData.standard()),
                ratesProvider
            );
            
            System.out.println("Present Value: " + pv);
            
            // Calculate PV01 (DV01)
            CurrencyAmount pv01 = pricer.pv01CalibratedSum(
                swap.getProduct().resolve(ReferenceData.standard()),
                ratesProvider
            );
            
            System.out.println("PV01: " + pv01);
            """);

        System.out.println("\n--- FX Forward ---");
        System.out.println("""
            import com.opengamma.strata.product.fx.*;
            
            // Create FX Forward
            FxSingle fxForward = FxSingle.of(
                CurrencyAmount.of(Currency.EUR, 1_000_000),
                FxRate.of(Currency.EUR, Currency.USD, 1.10),
                LocalDate.of(2025, 6, 15)
            );
            
            // Price FX Forward
            DiscountingFxSingleProductPricer fxPricer = 
                DiscountingFxSingleProductPricer.DEFAULT;
            
            MultiCurrencyAmount fxPv = fxPricer.presentValue(
                fxForward,
                ratesProvider
            );
            """);

        System.out.println("\n--- Options ---");
        System.out.println("""
            import com.opengamma.strata.product.option.*;
            import com.opengamma.strata.pricer.option.*;
            
            // Black-Scholes pricing
            BlackVolatilityExpiryTenorProvider volatilities = buildVolSurface();
            
            // European Option
            // (Strata supports vanilla options, swaptions, caps/floors)
            
            // Swaption example
            Swaption swaption = Swaption.builder()
                .expiryDate(AdjustableDate.of(tradeDate.plusMonths(3)))
                .expiryTime(LocalTime.of(11, 0))
                .expiryZone(ZoneId.of("America/New_York"))
                .longShort(LongShort.LONG)
                .swaptionSettlement(PhysicalSwaptionSettlement.DEFAULT)
                .underlying(swap.getProduct())
                .build();
            """);

        System.out.println("\n--- Schedule Generation ---");
        System.out.println("""
            import com.opengamma.strata.basics.schedule.*;
            
            // Generate payment schedule
            PeriodicSchedule schedule = PeriodicSchedule.builder()
                .startDate(tradeDate)
                .endDate(maturityDate)
                .frequency(Frequency.P6M)
                .businessDayAdjustment(bda)
                .stubConvention(StubConvention.SHORT_INITIAL)
                .rollConvention(RollConventions.DAY_15)
                .build();
            
            Schedule resolvedSchedule = schedule.createSchedule(
                ReferenceData.standard()
            );
            
            resolvedSchedule.getPeriods().forEach(period -> {
                System.out.println(period.getStartDate() + " to " + 
                    period.getEndDate());
            });
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Cache rate providers");
        System.out.println("   ✓ Use reference data appropriately");
        System.out.println("   ✓ Handle holiday calendars");
        System.out.println("   ✓ Validate market data inputs");
        System.out.println();
    }
}
