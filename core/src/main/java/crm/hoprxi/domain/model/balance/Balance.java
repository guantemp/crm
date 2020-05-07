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
package crm.hoprxi.domain.model.balance;

import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
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
    private MonetaryAmount redPackets;

    /**
     * @param valuable
     * @param redPackets required positive
     */
    public Balance(MonetaryAmount valuable, MonetaryAmount redPackets) {
        setValuable(valuable);
        setRedPackets(redPackets);
    }

    /**
     * @param valuable
     * @param redPackets
     * @return
     */
    public static Balance getInstance(MonetaryAmount valuable, MonetaryAmount redPackets) {
        if (valuable.isZero() && redPackets.isZero())
            return zero(valuable.getCurrency());
        return new Balance(valuable, redPackets);
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

    private void setRedPackets(MonetaryAmount redPackets) {
        if (redPackets == null || redPackets.isNegative())
            throw new IllegalArgumentException("redPackets required positive");
        if (!redPackets.getCurrency().equals(valuable.getCurrency()))
            throw new IllegalArgumentException("redPackets currency required equal valuable currency");
        this.redPackets = redPackets;
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

    public MonetaryAmount redPackets() {
        return redPackets;
    }

    public MonetaryAmount total() {
        return valuable.add(redPackets);
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
        return new Balance(valuable.add(amount), redPackets);
    }

    /**
     * @param redPackets
     * @return
     */
    public Balance giveRedPackets(MonetaryAmount redPackets) {
        if (redPackets == null || redPackets.isNegativeOrZero())
            return this;
        if (!this.redPackets.getCurrency().equals(redPackets.getCurrency()))
            throw new IllegalArgumentException("Inconsistent currency type,must is" + this.redPackets.getCurrency());
        return new Balance(valuable, this.redPackets.add(redPackets));
    }

    /**
     * @param redPackets
     * @return
     */
    public Balance withdrawRedPackets(MonetaryAmount redPackets) {
        if (redPackets == null)
            return this;
        CurrencyUnit currencyUnit = this.valuable.getCurrency();
        if (!currencyUnit.equals(redPackets.getCurrency()))
            throw new IllegalArgumentException("Inconsistent currency type,must is" + currencyUnit);
        if (this.redPackets.isLessThan(redPackets))
            throw new InsufficientBalanceException("The redPackets insufficient balance");
        return new Balance(valuable, this.redPackets.subtract(redPackets));
    }


    Balance pay(MonetaryAmount amount, PaymentStrategy strategy) {
        MonetaryAmount available = valuable.add(redPackets);
        if (available.isLessThan(amount))
            throw new InsufficientBalanceException("insufficient balance");
        switch (strategy) {
            case BALANCE_FIRST:
                valuable = valuable.subtract(amount);
                if (valuable.isNegative()) {
                    valuable = Money.zero(valuable.getCurrency());
                    redPackets = redPackets.add(valuable);
                }
                break;
            case RED_ENVELOPES_FIRST:
                MonetaryAmount g = redPackets.subtract(amount);
                if (g.isNegative()) {
                    redPackets = Money.zero(redPackets.getCurrency());
                    valuable = valuable.add(g);
                } else {
                    redPackets = g;
                }
                break;
            case RATIO:
                double d1 = valuable.getNumber().doubleValue();
                double d2 = redPackets.getNumber().doubleValue();
                double d3 = d2 / (d1 + d2);
                MonetaryAmount temp = amount.multiply(d3);
                valuable = valuable.subtract(amount.subtract(temp));
                redPackets = redPackets.subtract(temp);
                break;
        }
        return new Balance(valuable, redPackets);
    }

    /**
     * @param amount
     * @return
     */
    public Balance pay(MonetaryAmount amount) {
        if (amount == null)
            return this;
        CurrencyUnit currencyUnit = this.valuable.getCurrency();
        if (!currencyUnit.equals(amount.getCurrency()))
            throw new IllegalArgumentException("Inconsistent currency type,must is" + currencyUnit);
        MonetaryAmount available = valuable.add(redPackets);
        if (available.isLessThan(amount))
            throw new InsufficientBalanceException("The insufficient balance");
        if (amount.isEqualTo(available))
            return zero(valuable.getCurrency());
/*
        BigDecimal big1 = valuable.getNumber().numberValue(BigDecimal.class);
        BigDecimal big2 = give.getNumber().numberValue(BigDecimal.class);
        BigDecimal big3 = big1.add(big2);
        System.out.println(big1+" "+big2+" "+big3);
        System.out.println(big2.divide(big3,20, BigDecimal.ROUND_HALF_EVEN));
 */
        if (this.valuable.isGreaterThanOrEqualTo(amount))
            return new Balance(valuable.subtract(amount), redPackets);
        MonetaryAmount temp = amount.subtract(valuable);
        return new Balance(Money.zero(this.valuable.getCurrency()), redPackets.subtract(temp));
/*
        double d1 = valuable.getNumber().doubleValue();
        double d2 = give.getNumber().doubleValue();
        double d3 = d2 / (d1 + d2);
        MonetaryAmount temp = amount.multiply(BigDecimal.valueOf(d3).setScale(scale, BigDecimal.ROUND_HALF_EVEN));
        return new Balance(valuable.subtract(amount.subtract(temp)), give.subtract(temp));
        */
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
        MonetaryAmount total = valuable.add(redPackets);
        if (total.isGreaterThanOrEqualTo(amount)) {
            if (valuable.isPositiveOrZero())
                return pay(amount);
            return new Balance(valuable, redPackets.subtract(amount));
        }
        if (redPackets.isGreaterThanOrEqualTo(amount))
            return new Balance(valuable, redPackets.subtract(amount));
        MonetaryAmount surplus = amount.subtract(redPackets);
        return new Balance(valuable.subtract(surplus), Money.zero(redPackets.getCurrency()));
    }

    /**
     * Red packet amount cannot be cashed out
     *
     * @param amount
     * @return
     */
    public Balance withdrawal(MonetaryAmount amount) {
        if (amount == null)
            return this;
        CurrencyUnit currencyUnit = this.valuable.getCurrency();
        if (!currencyUnit.equals(amount.getCurrency()))
            throw new IllegalArgumentException("Inconsistent currency type,must is" + currencyUnit);
        if (valuable.isLessThan(amount))
            throw new InsufficientBalanceException("The valuable insufficient balance");
        return new Balance(valuable.subtract(amount), redPackets);
    }

    public boolean isZero() {
        return valuable.isZero() && redPackets.isZero();
    }

    public Balance add(Balance balance) {
        if (!valuable.getCurrency().equals(balance.valuable.getCurrency()))
            throw new IllegalArgumentException("Inconsistent currency type,must is" + valuable.getCurrency());
        return new Balance(valuable.add(balance.valuable), redPackets.add(balance.redPackets));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Balance balance = (Balance) o;

        if (valuable != null ? !valuable.equals(balance.valuable) : balance.valuable != null) return false;
        return redPackets != null ? redPackets.equals(balance.redPackets) : balance.redPackets == null;
    }

    @Override
    public int hashCode() {
        int result = valuable != null ? valuable.hashCode() : 0;
        result = 31 * result + (redPackets != null ? redPackets.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Balance.class.getSimpleName() + "[", "]")
                .add("valuable=" + valuable)
                .add("give=" + redPackets)
                .toString();
    }

    enum PaymentStrategy {
        BALANCE_FIRST, RED_ENVELOPES_FIRST, RATIO
    }
}
