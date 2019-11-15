
package crm.hoprxi;

import mi.hoprxi.to.ByteToHex;
import org.javamoney.moneta.Money;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.NumberValue;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update("".getBytes(StandardCharsets.UTF_8));
        System.out.println(ByteToHex.toHexStr(messageDigest.digest()));

        MonetaryAmount amount = Money.of(new BigDecimal("223.525436464565457567564"), "CNY");
        MonetaryAmount amount1 = Money.of(1051.45, Monetary.getCurrency(Locale.getDefault()));
        System.out.println("amount.signum():" + amount.signum());
        System.out.println("amount.abs():" + amount.abs());
        System.out.println("amount.remainder(1):" + amount.remainder(1));
        System.out.println("amount.divideToIntegralValue(1):" + amount.divideToIntegralValue(1));
        System.out.println("amount.scaleByPowerOfTen(1):" + amount.scaleByPowerOfTen(1));
        System.out.println("amount.stripTrailingZeros():" + amount.stripTrailingZeros());
        System.out.println("amount.negate():" + amount.negate());
        System.out.println("amount.getNumber():" + amount.getNumber());
        System.out.println("amount.etNumberStripped():" + ((Money) amount).getNumberStripped());
        double d1 = amount.abs().getNumber().doubleValueExact();
        double d2 = amount1.getNumber().doubleValue();
        System.out.println(d1);
        System.out.println(amount.abs().getNumber().doubleValue());
        double d3 = d1 / (d1 + d2);
        MonetaryAmount amount2 = amount.multiply(d3);
        System.out.println(amount2);

        CurrencyUnit usd = Monetary.getCurrency("USD");
        MonetaryAmount dollars = Money.of(12.34567, usd);
        //MonetaryOperator roundingOperator = M
        //MonetaryAmount roundedDollars = dollars.with(roundingOperator); // USD 12.35

        ExchangeRateProvider exchangeRateProvider = MonetaryConversions.getExchangeRateProvider();
        List<String> defaultProviderChain = MonetaryConversions.getDefaultConversionProviderChain();
        for (String s : defaultProviderChain)
            System.out.println(s);


        // get a specific ExchangeRateProvider (here ECB)
        ExchangeRateProvider ecbExchangeRateProvider = MonetaryConversions.getExchangeRateProvider("ECB");

        ExchangeRate rate = exchangeRateProvider.getExchangeRate("EUR", "USD");

        NumberValue factor = rate.getFactor(); // 1.2537 (at time writing)
        CurrencyUnit baseCurrency = rate.getBaseCurrency(); // EUR
        CurrencyUnit targetCurrency = rate.getCurrency(); // USD
        System.out.println(factor);


        CurrencyConversion dollarConversion = MonetaryConversions.getConversion("USD");

        // get the CurrencyConversion from a specific provider
        CurrencyConversion ecbDollarConversion = ecbExchangeRateProvider.getCurrencyConversion("USD");

        MonetaryAmount tenEuro = Money.of(100, "JPY");

        // convert 10 euro to us dollar
        MonetaryAmount inDollar = tenEuro.with(dollarConversion);
        System.out.println(inDollar);
    }
}
