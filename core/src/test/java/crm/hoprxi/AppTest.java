
/*
 * Copyright (c) 2019. www.hoprxi.com All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package crm.hoprxi;

import mi.hoprxi.to.ByteToHex;
import org.javamoney.moneta.FastMoney;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.update("Qwe123465".getBytes(StandardCharsets.UTF_8));
        System.out.println("Qwe123465 Sha-256:" + ByteToHex.toHexStr(messageDigest.digest()));

        String source = "AnonymousCardWordSizeOf";
        Matcher matcher = Pattern.compile("[A-Z]").matcher(source);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String g = matcher.group();
            matcher.appendReplacement(sb, "_" + g.toLowerCase());
        }
        matcher.appendTail(sb);
        if (sb.charAt(0) == '_') {
            sb.delete(0, 1);
        }
        System.out.println(sb.toString());

        System.out.println(Monetary.getCurrency(Locale.US).getNumericCode() + " " + Monetary.getCurrency(Locale.US).getCurrencyCode());
        System.out.println(Monetary.getCurrency(Locale.CHINA).getNumericCode() + " " + Monetary.getCurrency(Locale.CHINA).getCurrencyCode());

        MonetaryAmount amount = Money.of(new BigDecimal("-23423.4536575763474262634657686795745643647678083534654"), "CNY");
        System.out.println("amount.getCurrency().getCurrencyCode():" + amount.getCurrency().getCurrencyCode());
        System.out.println("amount.signum() +-:" + amount.signum());
        System.out.println("amount.plus():" + amount.plus());
        System.out.println("amount.abs():" + amount.abs());
        System.out.println("amount.remainder(1):" + amount.remainder(1));
        System.out.println("amount.remainder(5):" + amount.remainder(5));
        System.out.println("amount.divideToIntegralValue(1):" + amount.divideToIntegralValue(1));
        System.out.println("amount.scaleByPowerOfTen(1):" + amount.scaleByPowerOfTen(1));
        System.out.println("amount.stripTrailingZeros():" + amount.stripTrailingZeros());
        System.out.println("amount.negate():" + amount.negate());
        System.out.println("amount.getNumber():" + amount.getNumber());
        System.out.println("amount.getNumberStripped():" + ((Money) amount).getNumberStripped());
        System.out.println("amount.getNumber().doubleValueExact():" + amount.getNumber().doubleValueExact());
        System.out.println("amount.getNumber().doubleValue():" + amount.getNumber().doubleValue());

        System.out.println("\nFastMoney:");
        MonetaryAmount amount1 = FastMoney.of(1051.45365, "CNY");
        MonetaryAmount amount2 = Money.of(1051.45365, "CNY");
        System.out.println("amount1.getNumber().doubleValue():" + amount1.getNumber().doubleValue());
        System.out.println("amount2.getNumber().doubleValue():" + amount2.getNumber().doubleValue());
        System.out.println("amount1.equals(amount2):" + amount1.equals(amount2));
        System.out.println("amount1.isEqualTo(amount2):" + amount1.isEqualTo(amount2));
        System.out.println("amount1.getContext():" + amount1.getContext());
        System.out.println("amount2.getContext():" + amount2.getContext());


        CurrencyUnit usd = Monetary.getCurrency("USD");
        MonetaryAmount dollars = Money.of(12.34567, usd);
        //MonetaryOperator roundingOperator = M
        //MonetaryAmount roundedDollars = dollars.with(roundingOperator); // USD 12.35

        ExchangeRateProvider exchangeRateProvider = MonetaryConversions.getExchangeRateProvider();
        List<String> defaultProviderChain = MonetaryConversions.getDefaultConversionProviderChain();
        System.out.println("defaultProviderChain:");
        for (String s : defaultProviderChain)
            System.out.println(s);

        // get a specific ExchangeRateProvider (here ECB)
        ExchangeRateProvider ecbExchangeRateProvider = MonetaryConversions.getExchangeRateProvider("IMF");
        ExchangeRate rate = ecbExchangeRateProvider.getExchangeRate("USD", "CNY");

        NumberValue factor = rate.getFactor(); // 1.2537 (at time writing)
        CurrencyUnit baseCurrency = rate.getBaseCurrency(); // EUR
        CurrencyUnit targetCurrency = rate.getCurrency(); // USD
        System.out.println("factor CNY to USD:" + factor);

        // get the CurrencyConversion from a specific provider
        CurrencyConversion dollarConversion = MonetaryConversions.getConversion("USD");
        CurrencyConversion ecbDollarConversion = ecbExchangeRateProvider.getCurrencyConversion("USD");

        MonetaryAmount tenEuro = Money.of(100, "JPY");

        // convert 10 euro to us dollar
        MonetaryAmount inDollar = tenEuro.with(dollarConversion);
        System.out.println(inDollar);
        inDollar = tenEuro.with(ecbDollarConversion);
        System.out.println("ecbDollarConversion:" + inDollar);
    }
}
