package com.example.demo.showcase;

/**
 * Demonstrates TA4J - Technical Analysis Library
 * Covers indicators, strategies, and backtesting
 */
public class TA4JShowcase {

    public static void demonstrate() {
        System.out.println("\n========== TA4J TECHNICAL ANALYSIS SHOWCASE ==========\n");

        System.out.println("--- TA4J Overview ---");
        System.out.println("Technical analysis library for trading strategies\n");

        System.out.println("Key features:");
        System.out.println("   • 130+ technical indicators");
        System.out.println("   • Trading strategies and rules");
        System.out.println("   • Backtest framework");
        System.out.println("   • Bar series (OHLCV data)");

        System.out.println("\nDependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.ta4j</groupId>
                <artifactId>ta4j-core</artifactId>
                <version>0.15</version>
            </dependency>
            """);

        System.out.println("\n--- Bar Series (Price Data) ---");
        System.out.println("""
            import org.ta4j.core.*;
            import org.ta4j.core.num.DecimalNum;
            
            // Create bar series
            BarSeries series = new BaseBarSeries("AAPL");
            
            // Add bars (time, open, high, low, close, volume)
            series.addBar(ZonedDateTime.now(), 150.0, 152.0, 149.0, 151.0, 1000000);
            series.addBar(ZonedDateTime.now().plusDays(1), 151.0, 153.0, 150.5, 152.5, 1100000);
            
            // From CSV
            BarSeries series = CsvTradesLoader.loadBitstampSeries();
            """);

        System.out.println("\n--- Technical Indicators ---");
        System.out.println("""
            import org.ta4j.core.indicators.*;
            import org.ta4j.core.indicators.helpers.*;
            
            // Closing Price
            ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
            
            // Simple Moving Average (SMA)
            SMAIndicator sma20 = new SMAIndicator(closePrice, 20);
            SMAIndicator sma50 = new SMAIndicator(closePrice, 50);
            
            // Exponential Moving Average (EMA)
            EMAIndicator ema12 = new EMAIndicator(closePrice, 12);
            EMAIndicator ema26 = new EMAIndicator(closePrice, 26);
            
            // MACD
            MACDIndicator macd = new MACDIndicator(closePrice, 12, 26);
            EMAIndicator signal = new EMAIndicator(macd, 9);
            
            // RSI (Relative Strength Index)
            RSIIndicator rsi = new RSIIndicator(closePrice, 14);
            
            // Bollinger Bands
            SMAIndicator sma = new SMAIndicator(closePrice, 20);
            StandardDeviationIndicator sd = new StandardDeviationIndicator(closePrice, 20);
            BollingerBandsUpperIndicator upper = new BollingerBandsUpperIndicator(sma, sd);
            BollingerBandsLowerIndicator lower = new BollingerBandsLowerIndicator(sma, sd);
            
            // ATR (Average True Range)
            ATRIndicator atr = new ATRIndicator(series, 14);
            
            // Stochastic Oscillator
            StochasticOscillatorKIndicator stochK = new StochasticOscillatorKIndicator(series, 14);
            SMAIndicator stochD = new SMAIndicator(stochK, 3);
            
            // Get values
            Num currentRsi = rsi.getValue(series.getEndIndex());
            System.out.println("Current RSI: " + currentRsi);
            """);

        System.out.println("\n--- Trading Rules and Strategy ---");
        System.out.println("""
            import org.ta4j.core.rules.*;
            
            // Entry Rule: SMA Cross + RSI oversold
            Rule entryRule = new CrossedUpIndicatorRule(sma20, sma50)
                .and(new UnderIndicatorRule(rsi, 30));
            
            // Exit Rule: SMA Cross down OR RSI overbought
            Rule exitRule = new CrossedDownIndicatorRule(sma20, sma50)
                .or(new OverIndicatorRule(rsi, 70));
            
            // Create strategy
            Strategy strategy = new BaseStrategy("SMA-RSI Strategy", entryRule, exitRule);
            
            // Alternative: MACD Strategy
            Rule macdEntryRule = new CrossedUpIndicatorRule(macd, signal);
            Rule macdExitRule = new CrossedDownIndicatorRule(macd, signal);
            Strategy macdStrategy = new BaseStrategy("MACD Strategy", 
                macdEntryRule, macdExitRule);
            """);

        System.out.println("\n--- Backtesting ---");
        System.out.println("""
            import org.ta4j.core.analysis.criteria.*;
            
            // Run backtest
            BarSeriesManager manager = new BarSeriesManager(series);
            TradingRecord tradingRecord = manager.run(strategy);
            
            // Print trades
            System.out.println("Number of trades: " + tradingRecord.getTradeCount());
            
            tradingRecord.getTrades().forEach(trade -> {
                System.out.println("Entry: " + trade.getEntry());
                System.out.println("Exit: " + trade.getExit());
                System.out.println("Profit: " + trade.getProfit());
            });
            
            // Calculate performance metrics
            TotalProfitCriterion profit = new TotalProfitCriterion();
            Num totalProfit = profit.calculate(series, tradingRecord);
            
            NumberOfWinningPositionsCriterion winningCount = 
                new NumberOfWinningPositionsCriterion();
            Num wins = winningCount.calculate(series, tradingRecord);
            
            MaximumDrawdownCriterion maxDD = new MaximumDrawdownCriterion();
            Num drawdown = maxDD.calculate(series, tradingRecord);
            
            System.out.println("Total Profit: " + totalProfit);
            System.out.println("Winning Trades: " + wins);
            System.out.println("Max Drawdown: " + drawdown);
            
            // Sharpe Ratio
            SharpeRatioCriterion sharpe = new SharpeRatioCriterion();
            Num sharpeRatio = sharpe.calculate(series, tradingRecord);
            System.out.println("Sharpe Ratio: " + sharpeRatio);
            """);

        System.out.println("\n--- Common Indicators ---");
        System.out.println("""
            Trend Indicators:
            - SMA, EMA, WMA (Moving Averages)
            - MACD (Moving Average Convergence Divergence)
            - ADX (Average Directional Index)
            - Aroon Indicator
            - Ichimoku Cloud
            
            Momentum Indicators:
            - RSI (Relative Strength Index)
            - Stochastic Oscillator
            - Williams %R
            - CCI (Commodity Channel Index)
            - ROC (Rate of Change)
            
            Volatility Indicators:
            - Bollinger Bands
            - ATR (Average True Range)
            - Keltner Channels
            - Standard Deviation
            
            Volume Indicators:
            - OBV (On-Balance Volume)
            - Chaikin Money Flow
            - Volume Weighted Average Price (VWAP)
            - Accumulation/Distribution
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Test strategies on historical data");
        System.out.println("   ✓ Avoid curve fitting/overfitting");
        System.out.println("   ✓ Consider transaction costs");
        System.out.println("   ✓ Use multiple timeframes");
        System.out.println("   ✓ Combine indicators wisely");
        System.out.println();
    }
}
