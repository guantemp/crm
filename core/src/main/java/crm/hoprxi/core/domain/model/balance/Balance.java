/*
 * Copyright (c) 2020. www.hoprxi.com All Rights Reserved.
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
package crm.hoprxi.core.domain.model.balance;

import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.2 2020-04-02
 */
public class Balance {
    private static final Balance RMB_ZERO = new Balance(Money.zero(Monetary.getCurrency(Locale.CHINA)), Money.zero(Monetary.getCurrency(Locale.CHINA)));
    private static final Balance USD_ZERO = new Balance(Money.zero(Monetary.getCurrency(Locale.US)), Money.zero(Monetary.getCurrency(Locale.US)));
    private MonetaryAmount valuable;
    private MonetaryAmount redEnvelope;

    /**
     * @param valuable
     * @param redEnvelope required positive
     */
    public Balance(MonetaryAmount valuable, MonetaryAmount redEnvelope) {
        setValuable(valuable);
        setRedEnvelope(redEnvelope);
    }

    /**
     * @param valuable
     * @param redEnvelope
     * @return
     */
    public static Balance getInstance(MonetaryAmount valuable, MonetaryAmount redEnvelope) {
        if (valuable.isZero() && redEnvelope.isZero())
            return zero(valuable.getCurrency());
        return new Balance(valuable, redEnvelope);
    }

    public static Balance rmbZero() {
        return RMB_ZERO;
    }

    public static Balance usdZero() {
        return USD_ZERO;
    }

    private void setValuable(MonetaryAmount valuable) {
        Objects.requireNonNull(valuable, "valuable required");
        this.valuable = valuable;
    }

    private void setRedEnvelope(MonetaryAmount redEnvelope) {
        if (redEnvelope == null || redEnvelope.isNegative())
            throw new IllegalArgumentException("redPackets required positive");
        if (!redEnvelope.getCurrency().equals(valuable.getCurrency()))
            throw new IllegalArgumentException("redPackets currency required equal valuable currency");
        this.redEnvelope = redEnvelope;
    }

    /**
     * @param locale
     * @return
     */
    public static Balance zero(Locale locale) {
        if (locale == Locale.CHINA || locale == Locale.CHINESE || locale == Locale.SIMPLIFIED_CHINESE || locale == Locale.PRC)
            return RMB_ZERO;
        if (locale == Locale.US)
            return USD_ZERO;
        MonetaryAmount zero = Money.zero(Monetary.getCurrency(locale));
        return new Balance(zero, zero);
    }

    public static Balance zero(CurrencyUnit currencyUnit) {
        if (currencyUnit.getNumericCode() == 156)
            return RMB_ZERO;
        if (currencyUnit.getNumericCode() == 840)
            return USD_ZERO;
        MonetaryAmount zero = Money.zero(currencyUnit);
        return new Balance(zero, zero);
    }

    public MonetaryAmount valuable() {
        return valuable;
    }

    public MonetaryAmount redEnvelope() {
        return redEnvelope;
    }

    public MonetaryAmount total() {
        return valuable.add(redEnvelope);
    }

    /**
     * @param amount
     * @return
     */
    public Balance deposit(MonetaryAmount amount) {
        if (amount == null || amount.isNegativeOrZero())
            return this;
        CurrencyUnit currencyUnit = this.valuable.getCurrency();
        if (!currencyUnit.equals(amount.getCurrency()))
            throw new IllegalArgumentException("Inconsistent currency type,must is" + currencyUnit);
        return new Balance(valuable.add(amount), redEnvelope);
    }

    /**
     * @param redEnvelope
     * @return
     */
    public Balance awardRedEnvelope(MonetaryAmount redEnvelope) {
        if (redEnvelope == null || redEnvelope.isNegativeOrZero())
            return this;
        if (!this.redEnvelope.getCurrency().equals(redEnvelope.getCurrency()))
            throw new IllegalArgumentException("Inconsistent currency type,must is" + this.redEnvelope.getCurrency());
        return new Balance(valuable, this.redEnvelope.add(redEnvelope));
    }

    /**
     * @param redEnvelope
     * @return
     */
    public Balance revokeRedEnvelope(MonetaryAmount redEnvelope) {
        if (redEnvelope == null)
            return this;
        CurrencyUnit currencyUnit = this.valuable.getCurrency();
        if (!currencyUnit.equals(redEnvelope.getCurrency()))
            throw new IllegalArgumentException("Inconsistent currency type,must is" + currencyUnit);
        if (this.redEnvelope.isLessThan(redEnvelope))
            throw new InsufficientBalanceException("The redPackets insufficient balance");
        return new Balance(valuable, this.redEnvelope.subtract(redEnvelope));
    }

    /**
     * @param amount
     * @return
     */
    public Balance pay(MonetaryAmount amount) {
        if (amount == null || amount.isNegativeOrZero())
            return this;
        CurrencyUnit currencyUnit = valuable.getCurrency();
        if (!currencyUnit.equals(amount.getCurrency()))
            throw new IllegalArgumentException("Inconsistent currency type,must is" + currencyUnit);
        MonetaryAmount total = valuable.add(redEnvelope);
        if (total.isLessThan(amount))
            throw new InsufficientBalanceException("The insufficient balance");
        if (total.isEqualTo(amount))
            return zero(valuable.getCurrency());
        MonetaryAmount difference = valuable.subtract(amount);
        if (difference.isPositiveOrZero())
            return new Balance(difference, redEnvelope);
        return new Balance(Money.zero(currencyUnit), redEnvelope.add(difference));
    }

    /**
     * @param amount
     * @return
     */
    public Balance overdraw(MonetaryAmount amount) {
        if (amount == null)
            return this;
        CurrencyUnit currencyUnit = this.valuable.getCurrency();
        if (!currencyUnit.equals(amount.getCurrency()))
            throw new IllegalArgumentException("Inconsistent currency type,must is" + currencyUnit);
        MonetaryAmount total = valuable.add(redEnvelope);
        if (total.isGreaterThanOrEqualTo(amount)) {
            return pay(amount);
        } else {
            MonetaryAmount difference = redEnvelope.subtract(amount);
            if (difference.isPositiveOrZero())
                return new Balance(valuable, difference);
            difference = difference.negate();
            return new Balance(valuable.subtract(difference), Money.zero(currencyUnit));
        }
    }

    public CurrencyUnit currencyUnit() {
        return valuable.getCurrency();
    }

    /**
     * red packet amount cannot be cashed out
     *
     * @param amount
     * @return
     */
    public Balance withdrawalCash(MonetaryAmount amount) {
        if (amount == null)
            return this;
        CurrencyUnit currencyUnit = this.valuable.getCurrency();
        if (!currencyUnit.equals(amount.getCurrency()))
            throw new IllegalArgumentException("Inconsistent currency type,must is" + currencyUnit);
        if (valuable.isLessThan(amount))
            throw new InsufficientBalanceException("The valuable insufficient balance");
        return new Balance(valuable.subtract(amount), redEnvelope);
    }

    public boolean isZero() {
        return valuable.isZero() && redEnvelope.isZero();
    }

    public Balance add(Balance balance) {
        if (!valuable.getCurrency().equals(balance.valuable.getCurrency()))
            throw new IllegalArgumentException("Inconsistent currency type,must is" + valuable.getCurrency());
        return new Balance(valuable.add(balance.valuable), redEnvelope.add(balance.redEnvelope));
    }

    public Balance subtract(Balance balance) {
        if (!valuable.getCurrency().equals(balance.valuable.getCurrency()))
            throw new IllegalArgumentException("Inconsistent currency type,must is" + valuable.getCurrency());
        return new Balance(valuable.subtract(balance.valuable), redEnvelope.subtract(balance.redEnvelope));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Balance balance = (Balance) o;

        if (valuable != null ? !valuable.equals(balance.valuable) : balance.valuable != null) return false;
        return redEnvelope != null ? redEnvelope.equals(balance.redEnvelope) : balance.redEnvelope == null;
    }

    @Override
    public int hashCode() {
        int result = valuable != null ? valuable.hashCode() : 0;
        result = 31 * result + (redEnvelope != null ? redEnvelope.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Balance.class.getSimpleName() + "[", "]")
                .add("valuable=" + valuable)
                .add("redPackets=" + redEnvelope)
                .toString();
    }

    Balance pay(MonetaryAmount amount, PaymentStrategy strategy) {
        MonetaryAmount available = valuable.add(redEnvelope);
        if (available.isLessThan(amount))
            throw new InsufficientBalanceException("insufficient balance");
        /*
        BigDecimal big1 = valuable.getNumber().numberValue(BigDecimal.class);
        BigDecimal big2 = give.getNumber().numberValue(BigDecimal.class);
        BigDecimal big3 = big1.add(big2);
        System.out.println(big1+" "+big2+" "+big3);
        System.out.println(big2.divide(big3,20, BigDecimal.ROUND_HALF_EVEN));
 */
        switch (strategy) {
            case BALANCE_FIRST:
                valuable = valuable.subtract(amount);
                if (valuable.isNegative()) {
                    valuable = Money.zero(valuable.getCurrency());
                    redEnvelope = redEnvelope.add(valuable);
                }
                break;
            case RED_ENVELOPES_FIRST:
                MonetaryAmount g = redEnvelope.subtract(amount);
                if (g.isNegative()) {
                    redEnvelope = Money.zero(redEnvelope.getCurrency());
                    valuable = valuable.add(g);
                } else {
                    redEnvelope = g;
                }
                break;
            case RATIO:
                double d1 = valuable.getNumber().doubleValue();
                double d2 = redEnvelope.getNumber().doubleValue();
                double d3 = d2 / (d1 + d2);
                MonetaryAmount temp = amount.multiply(BigDecimal.valueOf(d3).setScale(5, BigDecimal.ROUND_HALF_EVEN));
                valuable = valuable.subtract(amount.subtract(temp));
                redEnvelope = redEnvelope.subtract(temp);
                break;
        }
        return new Balance(valuable, redEnvelope);
    }

    enum PaymentStrategy {
        BALANCE_FIRST, RED_ENVELOPES_FIRST, RATIO
    }
}
